package com.dyql.media.test.presenter;

import android.util.Log;

import com.dyql.media.test.bean.MusicBean;
import com.dyql.media.test.model.LocalMusicModel;
import com.dyql.media.test.model.MusicModel;
import com.dyql.media.test.view.LocalMusicView;

import java.util.List;


public class MusicPresenter<T extends LocalMusicView> extends BasePresenter {

    private static final String TAG = MusicPresenter.class.getSimpleName();

    private MusicModel mMusicModel = new LocalMusicModel(); //向上转型

    public void fetch(){
        if (mLocalMusicView != null) {
            mMusicModel.showLocalMusic(new MusicModel.OnMusicListener() {
                @Override
                public void OnComplete(List<MusicBean> beans) {
                    Log.d(TAG, "OnComplete: "+(beans != null));
                    ((T) mLocalMusicView.get()).showLocalMusic(beans);
                }
            },context);
        }
    }
}
