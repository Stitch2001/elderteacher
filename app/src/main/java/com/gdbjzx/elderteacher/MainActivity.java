package com.gdbjzx.elderteacher;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Image[] images = {new Image(R.drawable.main_item_1),new Image(R.drawable.main_item_2),
            new Image(R.drawable.main_item_3),new Image(R.drawable.main_item_4),
            new Image(R.drawable.main_item_5),new Image(R.drawable.main_item_6),
            new Image(R.drawable.main_item_7),new Image(R.drawable.main_item_8),
            new Image(R.drawable.main_item_9)};

    private List<Image> imageList = new ArrayList<>();

    private Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*设置标题栏*/
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        /*设置标题栏*/
        /*设置主体*/
        initItems();//加载图片
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        GridLayoutManager layoutManager = new GridLayoutManager(this,2);//设置网格布局
        recyclerView.setLayoutManager(layoutManager);//加载网格布局
        adapter = new Adapter(imageList);//设置适配器
        recyclerView.setAdapter(adapter);//加载适配器
        /*设置主体*/
    }

    private void initItems(){
        imageList.clear();
        for (Image image:images){
            imageList.add(image);
        }
    }

}
