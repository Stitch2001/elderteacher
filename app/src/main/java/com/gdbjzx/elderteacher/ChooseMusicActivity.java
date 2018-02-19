package com.gdbjzx.elderteacher;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ChooseMusicActivity extends AppCompatActivity {

    private Music[] musics = new Music[21];

    private List<Music> musicList = new ArrayList<>();

    private MusicAdapter musicAdapter;

    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        for (int i=1;i<=20;i++){
            musics[i] = new Music("",0,0);
        }//初始化数组，防止崩溃

        /*设置标题栏*/
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        /*设置标题栏*/

        /*加载内容*/
        initMusics();
        /*加载内容*/

        /*设置主体*/
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        GridLayoutManager layoutManager = new GridLayoutManager(this,1);//设置线性布局
        recyclerView.setLayoutManager(layoutManager);//加载线性布局
        musicAdapter = new MusicAdapter(musicList);//设置适配器
        recyclerView.setAdapter(musicAdapter);//加载适配器
        /*设置主体*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initMusics() {
        /*此处内容仅为方便展示使用，实际操作应与http技术结合，从远程服务器获得音乐资源*/
        musics[1] = new Music("难忘今宵 - 李谷一",R.raw.music_1,1);
        musics[2] = new Music("当那一天来临 - 彭丽媛",R.raw.music_2,2);
        /*此处内容仅为方便展示使用，实际操作应与http技术结合，从远程服务器获得音乐资源*/
        musicList.clear();
        for (int i=1;i<=20;i++){
            if (musics[i].getMusicLabel()!= 0){
                musicList.add(musics[i]);
            }
        }
    }
}
