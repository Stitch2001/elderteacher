package com.gdbjzx.elderteacher;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

public class SecondMenuActivity extends AppCompatActivity {

    private Image[] images = new Image[11];

    private List<Image> imageList = new ArrayList<>();

    private MenuAdapter menuAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        for (int i=0;i<=10;i++){
            images[i] = new Image(-1,0);
        }//初始化数组，防止崩溃

        /*设置标题栏*/
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        /*设置标题栏*/

        /*加载图片*/
        Intent intent = getIntent();
        int imageLabel = intent.getIntExtra("imageLabel",0);
        initMenu(imageLabel);
        /*加载图片*/

        /*设置主体*/
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        GridLayoutManager layoutManager = new GridLayoutManager(this,2);//设置网格布局
        recyclerView.setLayoutManager(layoutManager);//加载网格布局
        menuAdapter = new MenuAdapter(imageList);//设置适配器
        recyclerView.setAdapter(menuAdapter);//加载适配器
        /*设置主体*/
    }

    private void initMenu(int imageLabel) {
        switch (imageLabel){
            case 1:
                images[1] = new Image(11,R.drawable.menu_1_1);
                images[2] = new Image(12,R.drawable.menu_1_2);
                images[3] = new Image(13,R.drawable.menu_1_3);
                images[4] = new Image(0,R.drawable.menu_back);
            default:
                break;
        }//判断应当加载哪一个菜单
        imageList.clear();
        for (int i=1;i<=10;i++){
            if (images[i].getImageLabel()!= -1){
                imageList.add(images[i]);
            }
        }
    }
}
