package com.gdbjzx.elderteacher;

import android.widget.ImageView;

/**
 * Created by Administrator on 2018/2/18.
 */

public class Image {

    private int imageId = 0;
    private int imageLable = 0;

    public Image(int imageLable,int imageId){
        this.imageLable = imageLable;
        this.imageId = imageId;
    }

    //imageLable用于判断点击了哪个按钮，imageId用于加载图片

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public int getImageLable() {
        return imageLable;
    }

    public void setImageLable(int imageLable) {
        this.imageLable = imageLable;
    }
}
