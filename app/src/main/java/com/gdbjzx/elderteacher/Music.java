package com.gdbjzx.elderteacher;

/**
 * Created by Administrator on 2018/2/19.
 */

public class Music {

    private String musicName;

    private int musicId;

    private int musicLabel;

    public Music(String musicName, int musicId, int musicLabel) {
        this.musicName = musicName;
        this.musicId = musicId;
        this.musicLabel = musicLabel;
    }

    //musicName用于显示歌曲名称，musicId用于获取歌曲资源，musicLabel用于歌曲标记

    public String getMusicName() {
        return musicName;
    }

    public void setMusicName(String musicName) {
        this.musicName = musicName;
    }

    public int getMusicId() {
        return musicId;
    }

    public void setMusicId(int musicId) {
        this.musicId = musicId;
    }

    public int getMusicLabel() {
        return musicLabel;
    }

    public void setMusicLabel(int musicLabel) {
        this.musicLabel = musicLabel;
    }
}
