package com.gdbjzx.elderteacher;

import android.media.MediaPlayer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Image[] images = new Image[10];

    private List<Image> imageList = new ArrayList<>();

    private MenuAdapter menuAdapter;

    private boolean isPlaying = false;

    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*设置标题栏*/
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        /*设置标题栏*/

        /*加载图片*/
        initMenu();
        /*加载图片*/

        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this)
                .setTitle("提示")
                .setMessage("本软件为演示使用的测试版本，目前只开放了“声音太小了”→“电话铃声太小了”、“字太小了”→“微信字太小了”、" +
                        "“总是按不对”三大模块，请知悉。")
                .setPositiveButton("我知道了",null);
        dialog.show();

        /*播放语音*/
        mediaPlayer = MediaPlayer.create(MainActivity.this,R.raw.menu_main_1);
        mediaPlayer.start();
        isPlaying = true;
        /*播放语音*/

        /*设置主体*/
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        GridLayoutManager layoutManager = new GridLayoutManager(this,2);//设置网格布局
        recyclerView.setLayoutManager(layoutManager);//加载网格布局
        menuAdapter = new MenuAdapter(imageList);//设置适配器
        recyclerView.setAdapter(menuAdapter);//加载适配器
        /*设置主体*/
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isPlaying){
            mediaPlayer.reset();
        }
    }

    private void initMenu() {
        images[1] = new Image(1,R.drawable.menu_main_1);
        images[2] = new Image(2,R.drawable.menu_main_2);
        images[3] = new Image(3,R.drawable.menu_main_3);
        images[4] = new Image(4,R.drawable.menu_main_4);
        images[5] = new Image(5,R.drawable.menu_main_5);
        images[6] = new Image(6,R.drawable.menu_main_6);
        images[7] = new Image(7,R.drawable.menu_main_7);
        images[8] = new Image(8,R.drawable.menu_main_8);
        images[9] = new Image(9,R.drawable.menu_main_9);
        imageList.clear();
        for (int i=1;i<=9;i++){
            if (images[i].getImageLabel()!= 0){
                imageList.add(images[i]);
            }
        }
    }
}
