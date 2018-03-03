package com.gdbjzx.elderteacher;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
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
    private int[] musicId = {0,0,0,0,0,0,0,0,0,0};

    private boolean isReview = false;//是否为复习状态标记
    private boolean isLastStepBack = false;//最后一步是否为返回键标记
    private String[] finishIntent = {"","",""};//完成后操作标记（打开应用or退出学习），第一个是包名，第二个是类名，第三个……反正只写两个会报错
    private int learnedNumber = 1;//正在学习的步骤数
    private int maxReviewNumber = 3;//一次性最大传授的步骤数
    private int reviewNumber;//正在复习的步骤数
    private int wholeStepNumber;//全部步骤数
    private int width;//手机屏幕宽度
    private int height;//手机屏幕高度
    private int musicFinishId;//学习完毕时的语音id
    private ImageView imageView;//底层图片
    private ImageView brightCircularView;//黄圈图片
    private ImageView movingFingerView;//手指图片
    private TextView tipsText;//小贴士文本
    private GifDrawable gifDrawable;//加载gif的拓展包
    private MediaPlayer mediaPlayer;//音乐播放器
    private boolean isPlaying = false;//是否正在播放音乐标记

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
        }//加载手指图片
        /*初始化控件*/

        /*播放语音*/
        mediaPlayer = MediaPlayer.create(StepTeacherActivity.this,R.raw.study_step_1);
        mediaPlayer.start();
        isPlaying = true;
        /*播放语音*/

        /*设置小贴士*/
        Random random = new Random();
        setTipsText(random.nextInt(1)+1);//随机显示，由于目前收集到的只有一个，故只设置一个小贴士
        tipsText.setVisibility(View.VISIBLE);
        /*设置小贴士*/

        /*接收Label并初始化步骤传授策略*/
        Intent intent = getIntent();
        final int label = intent.getIntExtra("Label",0);
        getAndroidScreenProperty();//获取屏幕宽高
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
               intent.putExtra("wholeStepNumber",wholeStepNumber);
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
                    if (reviewNumber == learnedNumber ){//判断是否为刚刚复习完
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(StepTeacherActivity.this);
                        alertDialog.setTitle("复习");
                        alertDialog.setMessage("复习完毕，继续学习");
                        alertDialog.setCancelable(true);
                        alertDialog.setPositiveButton("确定",null).show();
                    }
                    if ( learnedNumber % maxReviewNumber == 0 || learnedNumber == wholeStepNumber ){//判断是否需要复习
                        if (isPlaying){
                            mediaPlayer.reset();
                            mediaPlayer = MediaPlayer.create(StepTeacherActivity.this,R.raw.step_recite_1);
                            mediaPlayer.start();
                            isPlaying = true;
                        }
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(StepTeacherActivity.this);
                        alertDialog.setTitle("复习");
                        alertDialog.setMessage("开始复习");
                        alertDialog.setCancelable(false);
                        alertDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                isReview = true;
                                if (learnedNumber == wholeStepNumber){//判断是否为全步骤复习
                                    reviewNumber = 1;
                                    stepTeaching(reviewNumber);
                                } else if (learnedNumber % maxReviewNumber == 0){//判断是否为阶段性复习
                                    reviewNumber = learnedNumber - (maxReviewNumber - 1);
                                    stepTeaching(reviewNumber);
                                }
                            }
                        }).show();
                    } else {//不需要复习
                        if (reviewNumber == learnedNumber){//为了判断是否为刚刚复习完，我给两个步骤数都加了1，现在要减回去，否则将有影响
                            learnedNumber--;
                            reviewNumber--;
                        }
                        learnedNumber++;//步骤数自增
                        if (isPlaying){
                            mediaPlayer.reset();
                            mediaPlayer = MediaPlayer.create(StepTeacherActivity.this,musicId[learnedNumber]);
                            mediaPlayer.start();
                            isPlaying = true;
                        }
                        stepTeaching(learnedNumber);
                    }
                } else {//复习时操作
                    if (learnedNumber == wholeStepNumber){//判断是否为全步骤复习
                        reviewNumber++;//第122行，复习步骤为1时已经执行过，此处递增
                        stepTeaching(reviewNumber);
                        if (reviewNumber -1 == wholeStepNumber){//判断是否学习完毕，因为上面做了自增，故此处需-1
                            isReview = false;
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(StepTeacherActivity.this);
                            alertDialog.setTitle("学习");
                            alertDialog.setMessage("学习完毕");
                            alertDialog.setCancelable(true);
                            alertDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(StepTeacherActivity.this,FinishStudyActivity.class);
                                    intent.putExtra("PackageName",finishIntent[1]);
                                    intent.putExtra("ClassName",finishIntent[2]);
                                    startActivity(intent);//打开学习完毕界面，同时传递完毕后要进行的动作
                                    finish();
                                }
                            }).show();
                        }
                    } else if (learnedNumber % maxReviewNumber == 0){//判断是否为阶段性复习
                        reviewNumber++;//因为第125行，故此处递增
                        stepTeaching(reviewNumber);
                        if (reviewNumber == learnedNumber){
                            isReview = false;
                            learnedNumber++;//为了判断是否需要弹出“复习完毕”所作处理，否则将有影响
                            reviewNumber++;//为了判断是否需要弹出“复习完毕”所做处理，否则将有影响
                        }
                    }
                }
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//复习时点错的操作
                if (isReview){//判断是否正在复习
                    if (isPlaying){
                        mediaPlayer.reset();
                        mediaPlayer = MediaPlayer.create(StepTeacherActivity.this,R.raw.step_error);
                        mediaPlayer.start();
                        isPlaying = true;
                    }
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(StepTeacherActivity.this);
                    alertDialog.setTitle("复习");
                    alertDialog.setMessage("您点错了，需要重新学习吗？");
                    alertDialog.setCancelable(true);
                    alertDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            isLastStepBack = true;
                            isReview = false;
                            learnedNumber = reviewNumber;//把正在学习的步骤数调整为正在复习的步骤数
                            reviewNumber = 0;//正在复习的步骤数调为0，否则将报错
                            stepTeaching(learnedNumber);
                        }
                    });
                    alertDialog.setNegativeButton("不用，我还记得",null);
                    alertDialog.show();
                }
            }
        });
        /*步骤传授*/
        /*以上步骤传授模块内容是本人一下午辛苦摸索的结果，算法经多次调试未发现问题，如果您没有更优算法，请勿随意改动*/

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {//获取用户设置的一次性传授的步骤数
        switch (requestCode){
            case 1:
                if (resultCode == RESULT_OK){
                    maxReviewNumber = data.getIntExtra("maxReviewNumber",3);
                    /*步骤传授初始化*/
                    brightCircularView.setVisibility(View.VISIBLE);
                    movingFingerView.setVisibility(View.VISIBLE);
                    mediaPlayer.reset();
                    mediaPlayer = MediaPlayer.create(StepTeacherActivity.this,musicId[1]);
                    mediaPlayer.start();
                    isPlaying = true;
                    stepTeaching(1);//开始传授，接下来所有的点击操作都交给brightCircularView和imageView负责
                    /*步骤传授初始化*/
                } else finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if ( learnedNumber == wholeStepNumber && reviewNumber != wholeStepNumber && isLastStepBack ){//判断点击返回的原因是不是因为步骤需要
            isLastStepBack = false;
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(StepTeacherActivity.this);
            alertDialog.setTitle("复习");
            alertDialog.setMessage("开始复习");
            alertDialog.setCancelable(false);
            alertDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (isPlaying){
                        mediaPlayer.reset();
                        mediaPlayer = MediaPlayer.create(StepTeacherActivity.this,R.raw.step_recite_1);
                        mediaPlayer.start();
                        isPlaying = true;
                    }
                    isReview = true;
                    reviewNumber = 1;
                    stepTeaching(reviewNumber);
                }
            }).show();
        } else if (reviewNumber == wholeStepNumber){//因为点击返回没有使步骤数+1，所以这里判断没有使reviewNumber-1
            isReview = false;
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(StepTeacherActivity.this);
            alertDialog.setTitle("学习");
            alertDialog.setMessage("学习完毕");
            alertDialog.setCancelable(true);
            alertDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(StepTeacherActivity.this,FinishStudyActivity.class);
                    intent.putExtra("PackageName",finishIntent[1]);
                    intent.putExtra("ClassName",finishIntent[2]);
                    intent.putExtra("MusicFinishId",musicFinishId);
                    startActivity(intent);//打开学习完毕界面，同时传递完毕后要进行的动作
                    finish();
                }
            }).show();
        } else {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(StepTeacherActivity.this);
            alertDialog.setTitle("学习");
            alertDialog.setMessage("您还在学习，确定要离开吗？");
            alertDialog.setCancelable(false);
            alertDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            alertDialog.setNegativeButton("返回",null);
            alertDialog.show();
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

    @Override
    protected void onStop() {
        super.onStop();
        if (isPlaying){
            mediaPlayer.reset();
        }
    }

    private void initStepGroup(int label){
        /*此处内容仅为方便展示使用，实际操作应与http技术结合，从远程服务器获得数据*/
        switch (label){
            case 38:
                wholeStepNumber = 6;//设置总步骤数
                isLastStepBack = true;//标记最后一步为返回键
                musicFinishId = R.raw.weixin_textsize_finish_1;
                finishIntent[1] ="com.tencent.mm";
                finishIntent[2] ="com.tencent.mm.ui.LauncherUI";//标记完成后要打开微信主界面

                imageId[1] = R.drawable.weixin_text_size_1;
                circleX[1] = Integer.parseInt(new java.text.DecimalFormat("0").format(width*0.75));
                circleY[1] = Integer.parseInt(new java.text.DecimalFormat("0").format(height*0.85));
                fingerX[1] = Integer.parseInt(new java.text.DecimalFormat("0").format(width*0.75));
                fingerY[1] = Integer.parseInt(new java.text.DecimalFormat("0").format(height*0.68));
                fingerRotation[1] = 180;
                musicId[1] = R.raw.weixin_textsize_1;

                imageId[2] = R.drawable.weixin_text_size_2;
                circleX[2] = Integer.parseInt(new java.text.DecimalFormat("0").format(0));
                circleY[2] = Integer.parseInt(new java.text.DecimalFormat("0").format(height*0.66));
                fingerX[2] = Integer.parseInt(new java.text.DecimalFormat("0").format(0));
                fingerY[2] = Integer.parseInt(new java.text.DecimalFormat("0").format(height*0.73));
                fingerRotation[2] = 0;
                musicId[2] = R.raw.weixin_textsize_2;

                imageId[3] = R.drawable.weixin_text_size_3;
                circleX[3] = Integer.parseInt(new java.text.DecimalFormat("0").format(0));
                circleY[3] = Integer.parseInt(new java.text.DecimalFormat("0").format(height*0.48));
                fingerX[3] = Integer.parseInt(new java.text.DecimalFormat("0").format(0));
                fingerY[3] = Integer.parseInt(new java.text.DecimalFormat("0").format(height*0.6));
                fingerRotation[3] = 0;
                musicId[3] = R.raw.weixin_textsize_3;

                imageId[4] = R.drawable.weixin_text_size_4;
                circleX[4] = Integer.parseInt(new java.text.DecimalFormat("0").format(0));
                circleY[4] = Integer.parseInt(new java.text.DecimalFormat("0").format(height*0.3));
                fingerX[4] = Integer.parseInt(new java.text.DecimalFormat("0").format(width*0));
                fingerY[4] = Integer.parseInt(new java.text.DecimalFormat("0").format(height*0.42));
                fingerRotation[4] = 0;
                musicId[4] = R.raw.weixin_textsize_4;

                imageId[5] = R.drawable.weixin_text_size_5;
                circleX[5] = Integer.parseInt(new java.text.DecimalFormat("0").format(width*0.31));
                circleY[5] = Integer.parseInt(new java.text.DecimalFormat("0").format(height*0.83));
                fingerX[5] = Integer.parseInt(new java.text.DecimalFormat("0").format(width*0.3));
                fingerY[5] = Integer.parseInt(new java.text.DecimalFormat("0").format(height*0.68));
                fingerRotation[5] = 180;
                musicId[5] = R.raw.weixin_textsize_5;

                imageId[6] = R.drawable.weixin_text_size_6;
                circleX[6] = Integer.parseInt(new java.text.DecimalFormat("0").format(width));//隐藏黄圈
                circleY[6] = Integer.parseInt(new java.text.DecimalFormat("0").format(height));//隐藏黄圈
                fingerX[6] = Integer.parseInt(new java.text.DecimalFormat("0").format(width*0.7));
                fingerY[6] = Integer.parseInt(new java.text.DecimalFormat("0").format(height*0.75));
                fingerRotation[6] = 180;
                musicId[6] = R.raw.weixin_textsize_6;
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
