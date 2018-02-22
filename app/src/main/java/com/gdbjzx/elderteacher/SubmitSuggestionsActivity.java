package com.gdbjzx.elderteacher;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import java.io.File;
import java.util.UUID;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SubmitSuggestionsActivity extends AppCompatActivity {

    private MediaRecorder mRecorder;

    private File voiceFile;

    private MediaPlayer mediaPlayer;

    private boolean isPlaying = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_suggestions);
        /*注册控件*/
        final ImageButton audioMessageSend = (ImageButton) findViewById(R.id.voice_message_send);
        ImageButton textMessageSend =(ImageButton) findViewById(R.id.text_message_send);
        final EditText editText = (EditText) findViewById(R.id.editText);
        /*注册控件*/

        /*播放语音*/
        mediaPlayer = MediaPlayer.create(SubmitSuggestionsActivity.this,R.raw.submit_suggestions_1);
        mediaPlayer.start();
        isPlaying = true;
        /*播放语音*/

        /*注册按下录音按钮和抬起录音按钮的事件*/
        audioMessageSend.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (isPlaying){
                    mediaPlayer.reset();
                    isPlaying = false;
                }
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        //startVoice();
                        audioMessageSend.setImageResource(R.drawable.loose_to_unspeak);//更改录音按钮图片
                        break;
                    case MotionEvent.ACTION_UP:
                        //stopVoice();
                        audioMessageSend.setImageResource(R.drawable.press_to_speak);//更改录音按钮图片
                        ProgressDialog progressDialog = new ProgressDialog(SubmitSuggestionsActivity.this);
                        progressDialog.setTitle("意见反馈");
                        progressDialog.setMessage("发送中...");
                        progressDialog.setCancelable(true);
                        progressDialog.show();//显示加载中提示
                        //uploadVoice();
                        progressDialog.dismiss();//隐藏加载中提示
                        AlertDialog.Builder dialog = new AlertDialog.Builder(SubmitSuggestionsActivity.this)
                                .setTitle("意见反馈")
                                .setMessage("发送成功！")
                                .setCancelable(true)
                                .setPositiveButton("关闭",null);
                        dialog.show();//显示成功提示
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
        /*注册点击发送按钮的事件*/
        textMessageSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog progressDialog = new ProgressDialog(SubmitSuggestionsActivity.this);
                progressDialog.setTitle("意见反馈");
                progressDialog.setMessage("发送中...");
                progressDialog.setCancelable(true);
                progressDialog.show();//显示加载中提示
                /*发送建议*/
                OkHttpClient client = new OkHttpClient();
                RequestBody requestBody = new FormBody.Builder()
                        .add("Suggestions",editText.getText().toString())
                        .build();
                Request request = new Request.Builder()
                        .url("http://www.gdbjzx.com")
                        .post(requestBody)
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                } catch (Exception e){
                    e.printStackTrace();
                }
                /*发送建议*/
                progressDialog.dismiss();//隐藏加载中提示
                AlertDialog.Builder dialog = new AlertDialog.Builder(SubmitSuggestionsActivity.this)
                        .setTitle("意见反馈")
                        .setMessage("发送成功！")
                        .setCancelable(true)
                        .setPositiveButton("关闭",null);
                dialog.show();//显示发送成功提示
                editText.setText("");//清空输入框
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isPlaying){
            mediaPlayer.reset();
            isPlaying = false;
        }
    }

    /*限于个人水平问题，录音部分未成功开发
    private void startVoice(){
        voiceFile = new File(getExternalCacheDir()+"Sound.amr");
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        mRecorder.setOutputFile(voiceFile);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
        try {
            mRecorder.prepare();
        } catch (Exception e){
            e.printStackTrace();
        }
        mRecorder.start();
    }

    private void stopVoice() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
        Log.d("startVoice",Long.toString(voiceFile.length()));
    }

    private void uploadVoice(){
        //上传声音
    }*/
}
