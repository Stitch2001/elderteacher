package com.gdbjzx.elderteacher;

import android.widget.ImageView;

/**
 * Created by Administrator on 2018/2/18.
 */

public class Image {

    private int imageId = 0;
    private int imageLabel = 0;

    public Image(int imageLabel,int imageId){
        this.imageLabel = imageLabel;
        this.imageId = imageId;
    }

    //imageLabel用于判断点击了哪个按钮，imageId用于加载图片

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public int getImageLabel() {
        return imageLabel;
    }

    public void setImageLabel(int imageLabel) {
        this.imageLabel = imageLabel;
    }
}
