package com.gdbjzx.elderteacher;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ChooseMusicActivity extends AppCompatActivity {

    private Music[] musics = new Music[21];

    private List<Music> musicList = new ArrayList<>();

    private MusicAdapter musicAdapter;

    private MediaPlayer mediaPlayer;

    private boolean isPlaying = false;

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

        /*播放语音*/
        mediaPlayer = MediaPlayer.create(ChooseMusicActivity.this,R.raw.menu_1_1);
        mediaPlayer.start();
        isPlaying = true;
        /*播放语音*/

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

        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (isPlaying){
                    mediaPlayer.reset();
                    isPlaying = false;
                    return true;
                } else {
                    return false;
                }
            }
        });
    }

    private void initMusics() {
        /*此处内容仅为方便展示使用，实际操作应与http技术结合，从远程服务器获得音乐资源*/
        musics[1] = new Music("难忘今宵 - 李谷一",R.raw.music_1,1);
        musics[2] = new Music("当那一天来临 - 彭丽媛",R.raw.music_2,2);
        musics[3] = new Music("国际歌 - 唐朝乐队",R.raw.music_3,3);
        musics[4] = new Music("十送红军 - 刘紫玲",R.raw.music_4,4);
        /*此处内容仅为方便展示使用，实际操作应与http技术结合，从远程服务器获得音乐资源*/
        musicList.clear();
        int i;
        for (i=1;i<=19;i++){
            if (musics[i].getMusicLabel()!= 0){
                musicList.add(musics[i]);
            } else
                break;
        }
        musics[i].setMusicName("点击这里，告诉我们您想要的铃声");
        musicList.add(musics[i]);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isPlaying){
            mediaPlayer.reset();
        }
    }
}
