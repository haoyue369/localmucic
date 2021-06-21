package com.dyql.media.test.bean;


/**
 * @author : 12453
 * @since : 2020/12/28
 * Function:
 */
public class SongSheetBean {
    private String title;
    private String firstAlbumPath;
    private String count;

    public SongSheetBean(String title, String firstAlbumPath, String count) {
        this.title = title;
        this.firstAlbumPath = firstAlbumPath;
        this.count = count;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getFirstAlbumPath() {
        return firstAlbumPath;
    }

    public void setFirstAlbumPath(String firstAlbumPath) {
        this.firstAlbumPath = firstAlbumPath;
    }
}
