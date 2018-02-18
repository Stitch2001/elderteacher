package com.gdbjzx.elderteacher;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Image[] images = new Image[10];

    private List<Image> imageList = new ArrayList<>();

    private Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        for (int i=0;i<=9;i++){
            images[i] = new Image(0,0);
        }//初始化数组，防止崩溃

        /*设置标题栏*/
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        /*设置标题栏*/

        /*加载图片*/
        Intent intent = getIntent();
        if (intent.getIntExtra("imageLable",0) != 0){
            initItems(intent.getIntExtra("imageLable",0));
        } else initItems(0);
        /*加载图片*/

        /*设置主体*/
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        GridLayoutManager layoutManager = new GridLayoutManager(this,2);//设置网格布局
        recyclerView.setLayoutManager(layoutManager);//加载网格布局
        adapter = new Adapter(imageList);//设置适配器
        recyclerView.setAdapter(adapter);//加载适配器
        /*设置主体*/
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void initItems(int imageLable) {
        switch (imageLable){
            case 0:
                images[1] = new Image(1,R.drawable.main_item_1);
                images[2] = new Image(2,R.drawable.main_item_2);
                images[3] = new Image(3,R.drawable.main_item_3);
                images[4] = new Image(4,R.drawable.main_item_4);
                images[5] = new Image(5,R.drawable.main_item_5);
                images[6] = new Image(6,R.drawable.main_item_6);
                images[7] = new Image(7,R.drawable.main_item_7);
                images[8] = new Image(8,R.drawable.main_item_8);
                images[9] = new Image(9,R.drawable.main_item_9);
                break;
            default:
                break;
        }//判断应当加载哪一组图片
        imageList.clear();
        for (int i=1;i<=9;i++){
            if (images[i].getImageLable()!= 0){
                imageList.add(images[i]);
            }
        }
    }
}
