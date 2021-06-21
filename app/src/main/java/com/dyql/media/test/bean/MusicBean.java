package com.dyql.media.test.bean;

/**

 * 作用: 抽象一首歌曲，用于展示或者设置其信息
 */
public class MusicBean {
    String id;//排序序号
    String title;//歌曲名
    String artist;//歌手名
    String album;//专辑名
    String albumPath;//专辑图片地址
    String path;//音乐文件地址
    long duration;//音乐时长

    public MusicBean(String id, String title, String artist, String album, String albumPath, String path, long duration) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.albumPath = albumPath;
        this.path = path;
        this.duration = duration;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getAlbumPath() {
        return albumPath;
    }

    public void setAlbumPath(String albumPath) {
        this.albumPath = albumPath;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }
}
