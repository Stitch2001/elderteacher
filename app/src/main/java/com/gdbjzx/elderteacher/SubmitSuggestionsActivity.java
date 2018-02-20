package com.gdbjzx.elderteacher;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_suggestions);
        final ImageButton audioMessageSend = (ImageButton) findViewById(R.id.voice_message_send);
        ImageButton textMessageSend =(ImageButton) findViewById(R.id.text_message_send);
        final EditText editText = (EditText) findViewById(R.id.editText);
        audioMessageSend.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        //startVoice();
                        audioMessageSend.setImageResource(R.drawable.loose_to_unspeak);
                        break;
                    case MotionEvent.ACTION_UP:
                        //stopVoice();
                        audioMessageSend.setImageResource(R.drawable.press_to_speak);
                        ProgressDialog progressDialog = new ProgressDialog(SubmitSuggestionsActivity.this);
                        progressDialog.setTitle("意见反馈");
                        progressDialog.setMessage("发送中...");
                        progressDialog.setCancelable(true);
                        progressDialog.show();
                        //uploadVoice();
                        progressDialog.dismiss();
                        AlertDialog.Builder dialog = new AlertDialog.Builder(SubmitSuggestionsActivity.this)
                                .setTitle("意见反馈")
                                .setMessage("发送成功！")
                                .setCancelable(true)
                                .setPositiveButton("关闭",null);
                        dialog.show();
                        break;
                    default:break;
                }
                return false;
            }
        });
        textMessageSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog progressDialog = new ProgressDialog(SubmitSuggestionsActivity.this);
                progressDialog.setTitle("意见反馈");
                progressDialog.setMessage("发送中...");
                progressDialog.setCancelable(true);
                progressDialog.show();
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
                progressDialog.dismiss();
                AlertDialog.Builder dialog = new AlertDialog.Builder(SubmitSuggestionsActivity.this)
                        .setTitle("意见反馈")
                        .setMessage("发送成功！")
                        .setCancelable(true)
                        .setPositiveButton("关闭",null);
                dialog.show();
                editText.setText("");
            }
        });
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
