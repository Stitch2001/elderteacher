package com.gdbjzx.elderteacher;

import android.content.ComponentName;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class FinishStudyActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;

    private boolean isPlaying = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_study);
        final ImageView back = (ImageView) findViewById(R.id.back);

        Intent intent = getIntent();
        final String packageName = intent.getStringExtra("PackageName");
        final String className = intent.getStringExtra("ClassName");
        int musicFinishId = intent.getIntExtra("MusicFinishId",0);

        /*播放语音*/
        mediaPlayer = MediaPlayer.create(FinishStudyActivity.this,musicFinishId);
        mediaPlayer.start();
        isPlaying = true;
        /*播放语音*/

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (packageName){
                    case "":
                        finish();
                        break;
                    default:
                        Intent intent = new Intent();
                        ComponentName componentName = new ComponentName(packageName,className);
                        intent.setAction(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_LAUNCHER);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setComponent(componentName);
                        startActivity(intent);
                        finish();
                        break;
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isPlaying){
            mediaPlayer.reset();
        }
    }
}
