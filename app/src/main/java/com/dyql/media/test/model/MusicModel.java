package com.dyql.media.test.model;

import android.content.Context;

import com.dyql.media.test.bean.MusicBean;

import java.util.List;



public interface MusicModel {
    void showLocalMusic(OnMusicListener onMusicListener, Context context);
    interface OnMusicListener{
        void OnComplete(List<MusicBean> beans);
    }
}
