package com.dyql.media.test.view;

import com.dyql.media.test.bean.MusicBean;

import java.util.List;


public interface LocalMusicView extends BaseView{
    void showLocalMusic(List<MusicBean> beans);
    //数据
}
