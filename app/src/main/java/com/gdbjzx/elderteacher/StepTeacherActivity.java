package com.gdbjzx.elderteacher;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.Random;

import pl.droidsonroids.gif.GifDrawable;

public class StepTeacherActivity extends AppCompatActivity {

    private int[] imageId = {0,0,0,0,0,0,0,0,0,0};//赋初值，这里是10个0
    private int[] circleX = {0,0,0,0,0,0,0,0,0,0};
    private int[] circleY = {0,0,0,0,0,0,0,0,0,0};
    private int[] fingerX = {0,0,0,0,0,0,0,0,0,0};
    private int[] fingerY = {0,0,0,0,0,0,0,0,0,0};
    private float[] fingerRotation = {0,0,0,0,0,0,0,0,0,0};
    private boolean isReview = false;
    private int learnedNumber = 1;
    private int maxReviewNumber = 3;
    private int reviewNumber;
    private int wholeStepNumber;
    private int width;
    private int height;
    private ImageView imageView;
    private ImageView brightCircularView;
    private ImageView movingFingerView;
    private TextView tipsText;
    private GifDrawable gifDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_teacher);

        /*初始化控件*/
        imageView = (ImageView) findViewById(R.id.image_view);
        brightCircularView = (ImageView) findViewById(R.id.bright_circular_view);
        movingFingerView = (ImageView) findViewById(R.id.moving_finger_view);
        tipsText = (TextView) findViewById(R.id.tips_text);
        final Button sureVolume = (Button) findViewById(R.id.sure_volume);
        try {
            GifDrawable gifDrawable = new GifDrawable(getResources(),R.drawable.moving_finger);
            movingFingerView.setImageDrawable(gifDrawable);
        } catch (IOException e){
            e.printStackTrace();
        }
        /*初始化控件*/

        /*设置小贴士*/
        Random random = new Random();
        setTipsText(random.nextInt(2)+1);//随机数，由于目前收集到的只有一个，故只设置一个小贴士
        tipsText.setVisibility(View.VISIBLE);
        /*设置小贴士*/

        /*接收Label并初始化步骤传授策略*/
        Intent intent = getIntent();
        int label = intent.getIntExtra("Label",0);
        getAndroidScreenProperty();
        initStepGroup(label);
        /*接收Label并初始化步骤传授策略*/

        /*确认音量*/
        sureVolume.setVisibility(View.VISIBLE);
        sureVolume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               sureVolume.setVisibility(View.GONE);
               tipsText.setVisibility(View.GONE);
               /*设置一次性传授的步骤数*/
               Intent intent = new Intent(StepTeacherActivity.this,SetStudyNumberActivity.class);
               startActivityForResult(intent,1);
               /*设置一次性传授的步骤数*/
            }
        });
        /*确认音量*/

        /*步骤传授*/
        brightCircularView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("learnedNumber",Integer.toString(learnedNumber));
                Log.d("reviewNumber",Integer.toString(reviewNumber));
                Log.d("isReview",Boolean.toString(isReview));
                if (!isReview){//非复习时操作
                    if (reviewNumber == learnedNumber ){
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(StepTeacherActivity.this);
                        alertDialog.setTitle("复习");
                        alertDialog.setMessage("复习完毕，继续学习");
                        alertDialog.setCancelable(true);
                        alertDialog.setPositiveButton("确定",null).show();
                    }
                    if ( learnedNumber % maxReviewNumber == 0 || learnedNumber == wholeStepNumber ){
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(StepTeacherActivity.this);
                        alertDialog.setTitle("复习");
                        alertDialog.setMessage("开始复习");
                        alertDialog.setCancelable(false);
                        alertDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                isReview = true;
                                if (learnedNumber == wholeStepNumber){
                                    reviewNumber = 1;
                                    stepTeaching(reviewNumber);
                                } else if (learnedNumber % maxReviewNumber == 0){
                                    reviewNumber = learnedNumber - 2;
                                    stepTeaching(reviewNumber);
                                }
                            }
                        }).show();
                    } else {
                        if (reviewNumber == learnedNumber){
                            learnedNumber--;
                            reviewNumber--;
                        }
                        learnedNumber++;
                        stepTeaching(learnedNumber);
                    }
                } else {//复习时操作
                    Log.d("StepTeacher","Clicked");
                    if (learnedNumber == wholeStepNumber){
                        reviewNumber++;
                        stepTeaching(reviewNumber);
                        if (reviewNumber == wholeStepNumber){
                            isReview = false;
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(StepTeacherActivity.this);
                            alertDialog.setTitle("学习");
                            alertDialog.setMessage("学习完毕");
                            alertDialog.setCancelable(true);
                            alertDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            }).show();
                        }
                    } else if (learnedNumber % maxReviewNumber == 0){
                        reviewNumber++;
                        stepTeaching(reviewNumber);
                        if (reviewNumber == learnedNumber){
                            isReview = false;
                            learnedNumber++;
                            reviewNumber++;
                        }
                    }
                }
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isReview){
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(StepTeacherActivity.this);
                    alertDialog.setTitle("复习");
                    alertDialog.setMessage("您点错了，需要重新学习吗？");
                    alertDialog.setCancelable(true);
                    alertDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            isReview = false;
                            learnedNumber = reviewNumber;
                            reviewNumber = 0;
                            stepTeaching(learnedNumber);
                        }
                    });
                    alertDialog.setNegativeButton("不用，我还记得",null);
                    alertDialog.show();
                }
            }
        });
        /*步骤传授*/

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:
                if (requestCode == RESULT_OK){
                    maxReviewNumber = data.getIntExtra("maxReviewNumber",3);
                    /*步骤传授初始化*/
                    brightCircularView.setVisibility(View.VISIBLE);
                    movingFingerView.setVisibility(View.VISIBLE);
                    stepTeaching(1);
                    /*步骤传授初始化*/
                } else finish();
        }
    }

    private void setTipsText(int i) {
        switch (i){
            case 1:
                tipsText.setText("小贴士：“软件”的意思，通俗地说，我们在使用的都是软件，比如微信就是一款软件，腾讯新闻也是一款软件");
                break;
            default:
                break;
        }
    }

    private void initStepGroup(int label){
        /*此处内容仅为方便展示使用，实际操作应与http技术结合，从远程服务器获得数据*/
        switch (label){
            case 38:
                wholeStepNumber = 6;//设置总步骤数

                imageId[1] = R.drawable.weixin_text_size_1;
                circleX[1] = Integer.parseInt(new java.text.DecimalFormat("0").format(width*0.75));
                circleY[1] = Integer.parseInt(new java.text.DecimalFormat("0").format(height*0.85));
                fingerX[1] = Integer.parseInt(new java.text.DecimalFormat("0").format(width*0.75));
                fingerY[1] = Integer.parseInt(new java.text.DecimalFormat("0").format(height*0.68));
                fingerRotation[1] = 180;

                imageId[2] = R.drawable.weixin_text_size_2;
                circleX[2] = Integer.parseInt(new java.text.DecimalFormat("0").format(0));
                circleY[2] = Integer.parseInt(new java.text.DecimalFormat("0").format(height*0.66));
                fingerX[2] = Integer.parseInt(new java.text.DecimalFormat("0").format(0));
                fingerY[2] = Integer.parseInt(new java.text.DecimalFormat("0").format(height*0.73));
                fingerRotation[2] = 0;

                imageId[3] = R.drawable.weixin_text_size_3;
                circleX[3] = Integer.parseInt(new java.text.DecimalFormat("0").format(0));
                circleY[3] = Integer.parseInt(new java.text.DecimalFormat("0").format(height*0.48));
                fingerX[3] = Integer.parseInt(new java.text.DecimalFormat("0").format(0));
                fingerY[3] = Integer.parseInt(new java.text.DecimalFormat("0").format(height*0.6));
                fingerRotation[3] = 0;

                imageId[4] = R.drawable.weixin_text_size_4;
                circleX[4] = Integer.parseInt(new java.text.DecimalFormat("0").format(0));
                circleY[4] = Integer.parseInt(new java.text.DecimalFormat("0").format(height*0.3));
                fingerX[4] = Integer.parseInt(new java.text.DecimalFormat("0").format(width*0));
                fingerY[4] = Integer.parseInt(new java.text.DecimalFormat("0").format(height*0.42));
                fingerRotation[4] = 0;

                imageId[5] = R.drawable.weixin_text_size_5;
                circleX[5] = Integer.parseInt(new java.text.DecimalFormat("0").format(width*0.31));
                circleY[5] = Integer.parseInt(new java.text.DecimalFormat("0").format(height*0.83));
                fingerX[5] = Integer.parseInt(new java.text.DecimalFormat("0").format(width*0.3));
                fingerY[5] = Integer.parseInt(new java.text.DecimalFormat("0").format(height*0.68));
                fingerRotation[5] = 180;

                imageId[6] = R.drawable.weixin_text_size_6;
                circleX[6] = Integer.parseInt(new java.text.DecimalFormat("0").format(0));//隐藏黄圈
                circleY[6] = Integer.parseInt(new java.text.DecimalFormat("0").format(0));//隐藏黄圈
                fingerX[6] = Integer.parseInt(new java.text.DecimalFormat("0").format(width*0.7));
                fingerY[6] = Integer.parseInt(new java.text.DecimalFormat("0").format(height*0.75));
                fingerRotation[6] = 180;
                break;
        }
        /*此处内容仅为方便展示使用，实际操作应与http技术结合，从远程服务器获得数据*/
    }//初始化步骤传授策略

    private void stepTeaching(int step){
        imageView.setImageResource(imageId[step]);
        brightCircularView.setX(circleX[step]);
        brightCircularView.setY(circleY[step]);
        movingFingerView.setX(fingerX[step]);
        movingFingerView.setY(fingerY[step]);
        movingFingerView.setRotation(fingerRotation[step]);
        if (isReview){
            brightCircularView.setImageResource(R.drawable.invisible_circular);
            movingFingerView.setVisibility(View.GONE);
            tipsText.setText("复习中");
            Log.d("Teaching","SetText");
            tipsText.setTextColor(Color.WHITE);
            tipsText.setVisibility(View.VISIBLE);
        } else {
            try {
                gifDrawable = new GifDrawable(getResources(),R.drawable.bright_circular);
                brightCircularView.setImageDrawable(gifDrawable);
            } catch (IOException e){
                e.printStackTrace();
            }
            movingFingerView.setVisibility(View.VISIBLE);
            tipsText.setVisibility(View.GONE);
        }
    }//传授步骤

    public void getAndroidScreenProperty() {
        WindowManager wm = (WindowManager) this.getSystemService(StepTeacherActivity.this.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        width = dm.widthPixels;         // 屏幕宽度（像素）720
        height = dm.heightPixels;       // 屏幕高度（像素）1280
    }//获取屏幕宽高

}
