package com.dyql.media.test.presenter;

import android.content.Context;

import com.dyql.media.test.view.BaseView;

import java.lang.ref.WeakReference;


public class BasePresenter<T extends BaseView> {
    /*
        提取下层共有的部分
        context 上下文对象，查询并获取本地音乐
        可能有一个方法名大小写得问题
     */

    protected WeakReference<T> mLocalMusicView;
    protected Context context;


    public void attachView(T view,Context context){
        mLocalMusicView = new WeakReference<>(view);
        this.context = context.getApplicationContext();
    }

    public void detachView(){
        if (mLocalMusicView != null) {
            mLocalMusicView.clear();
            mLocalMusicView = null;
        }
        if (context != null) context = null;
    }
}
