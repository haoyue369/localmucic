package com.dyql.media.test;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dyql.media.test.adapter.MusicAdapter;
import com.dyql.media.test.bean.MusicBean;
import com.dyql.media.test.model.LocalMusicModel;
import com.dyql.media.test.model.MusicModel;
import com.dyql.media.test.presenter.MusicPresenter;
import com.dyql.media.test.service.MusicService;
import com.dyql.media.test.util.ImmersiveStatusBarUtil;
import com.dyql.media.test.util.PermissionUtil;
import com.dyql.media.test.view.BaseActivity;
import com.dyql.media.test.view.LocalMusicView;

import java.util.List;

public class MainActivity extends BaseActivity<MusicPresenter,LocalMusicView> implements LocalMusicView {
    private static final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView mMusicRv;
    private MusicAdapter mMusicAdapter;
    private MusicModel mMusicModel;
    //绑定音乐服务
    private MyConn mMyConn;
    private Intent mServiceIntent;
    private MusicService mMusicService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMusicRv = findViewById(R.id.main_activity_rv_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,
                                                                    LinearLayoutManager.VERTICAL,
                                                                    false);
        mMusicRv.setLayoutManager(layoutManager);
        mMusicModel = new LocalMusicModel();
        mMusicModel.showLocalMusic(new MusicModel.OnMusicListener() {
            @Override
            public void OnComplete(List<MusicBean> beans) {

            }
        },getApplicationContext());
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.fetch();//查询本地音乐数据，先启动再绑定
        this.startService(mServiceIntent);
        this.bindService(mServiceIntent,mMyConn, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        this.unbindService(mMyConn);
        this.stopService(mServiceIntent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mMyConn!=null) mMyConn= null;
        if (mServiceIntent != null) mServiceIntent =null;
        if (mMusicService != null) mMusicService =null;
        if (mMusicAdapter != null) mMusicAdapter =null;
        if (mMusicRv != null) mMusicRv =null;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (!PermissionUtil.isGetPermission(this)) {
                PermissionUtil.getStorage(this);
            }else{
                Log.d(TAG, "onRequestPermissionsResult: 已经获得权限！");
            }
        }
    }

    @Override
    protected MusicPresenter createPresenter() {
        return new MusicPresenter();
    }

    @Override
    public void showLocalMusic(List<MusicBean> beans) {
        UpdateMusicAdapter(beans);
    }

    @Override
    public void showErrorMessage(String msg) {

    }

    @Override
    protected void init() {
        //该初始化先于OnCreate执行
        ImmersiveStatusBarUtil.transparentBar(this,true);
        if (!PermissionUtil.isGetPermission(this)) PermissionUtil.getStorage(this);
        ImmersiveStatusBarUtil.transparentBar(this,true);

        //初始化绑定音乐服务
        mServiceIntent = new Intent(this,MusicService.class);
        mMyConn = new MyConn();
    }

    private class MyConn implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MyMusicBinder binder = (MusicService.MyMusicBinder) service;
            mMusicService = binder.getMusicService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "onServiceDisconnected: ");
            if (mMusicService != null) mMusicService=null;
        }
    }
    private void UpdateMusicAdapter(List<MusicBean> beans) {
        Log.d("111", "UpdateMusicAdapter: "+beans.size());
        if (mMusicAdapter != null) {
            mMusicAdapter = null;
            mMusicAdapter = new MusicAdapter(this,beans);
        }else mMusicAdapter = new MusicAdapter(this,beans);
        mMusicRv.setAdapter(mMusicAdapter);
        mMusicAdapter.notifyDataSetChanged();
        setEventListener(beans);
    }

    private void setEventListener(List<MusicBean> beans) {
        if (beans == null || beans.size() <= 0) return;
        mMusicAdapter.setItemClickListener(new MusicAdapter.OnItemClickListener() {
            @Override
            public void ItemClickListener(View v, int position) {
                //列表点击事件
                Log.d(TAG, "ItemClickListener: "+position);
                if (mMusicService==null) return;
                MusicBean bean = beans.get(position);
                mMusicService.OnPlay(bean.getPath());

            }

            @Override
            public void ItemViewClickListener(View v, int position) {
                //每一项列表的更多图标点击事件
                Log.d(TAG, "点击了更多图标"+position);
            }
        });

        }


}

