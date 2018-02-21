package com.gdbjzx.elderteacher;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Random;

public class PressPracticeActivity extends AppCompatActivity {

    private final int SHORT_PRESS = 0;

    private final int LONG_PRESS = 1;

    private final int UPDATE_TIME = 1;

    private boolean mWorking = false;

    private Random random;

    private int pressMode = SHORT_PRESS;//初值赋为短按

    private int currentTime;

    private int pressTime;

    private TextView pressTimeView;

    private Handler handler = new Handler(){
        int showSecond;
        int showMilliSecond;
        public void handleMessage(Message message){
            /*解析为秒与毫秒*/
            showSecond = message.what/1000;
            showMilliSecond = message.what%1000;
            /*解析为秒与毫秒*/
            pressTimeView.setText(Integer.toString(showSecond)+"."+Integer.toString(showMilliSecond));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_press_practice);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        /*注册控件*/
        Button shortPress = (Button) findViewById(R.id.short_press);
        Button longPress = (Button) findViewById(R.id.long_press);
        Button exactlyPress = (Button) findViewById(R.id.exactly_press);
        pressTimeView = (TextView) findViewById(R.id.press_time_view);
        final ImageButton press = (ImageButton) findViewById(R.id.press);
        final ImageView littleButton = (ImageView) findViewById(R.id.little_button);
        final TextView tip = (TextView) findViewById(R.id.tip);
        /*注册控件*/

        /*设置时间获取线程*/
        new Thread(new Runnable() {
            @Override
            public void run() {
                currentTime = 0;
                Calendar calendar;
                Message message;
                while (true){
                    while (mWorking) {
                        calendar = Calendar.getInstance();
                        if (calendar.get(Calendar.SECOND)*1000+calendar.get(Calendar.MILLISECOND) - currentTime > 50){
                            currentTime = calendar.get(Calendar.SECOND)*1000+calendar.get(Calendar.MILLISECOND);//转换成以毫秒为单位
                            message = new Message();
                            message.what = currentTime - pressTime;
                            handler.sendMessage(message);//将时间差发送至主线程
                        } else if (calendar.get(Calendar.SECOND)*1000+calendar.get(Calendar.MILLISECOND) - currentTime < -50 ){
                            currentTime = calendar.get(Calendar.SECOND)*1000+calendar.get(Calendar.MILLISECOND);//转换成以毫秒为单位
                            pressTime = pressTime - 60000;//跨越分钟时，对pressTime进行处理
                            message = new Message();
                            message.what = currentTime - pressTime;
                            handler.sendMessage(message);//将时间差发送至主线程
                        }
                    }
                }
            }
        }).start();
        /*设置时间获取线程*/

        /*注册点击事件*/
        shortPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                littleButton.setVisibility(View.GONE);
                press.setVisibility(View.VISIBLE);
                pressTimeView.setVisibility(View.VISIBLE);
                pressTimeView.setText("0.00");
                tip.setVisibility(View.VISIBLE);
                tip.setText("短按时间小于0.5秒");
                pressMode = SHORT_PRESS;//标记为短按
            }
        });
        longPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                littleButton.setVisibility(View.GONE);
                press.setVisibility(View.VISIBLE);
                pressTimeView.setVisibility(View.VISIBLE);
                pressTimeView.setText("0.00");
                tip.setVisibility(View.VISIBLE);
                tip.setText("长按时间大于0.5秒");
                pressMode = LONG_PRESS;//标记为长按
            }
        });
        exactlyPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                random = new Random();//初始化随机数
                /*设置随机小圆点位置*/
                ConstraintSet constraintSet = new ConstraintSet();
                ConstraintLayout constraintLayout = (ConstraintLayout) findViewById(R.id.press_practice_layout);//获取父控件
                constraintSet.clone(constraintLayout);//设置父控件
                constraintSet.connect(littleButton.getId(),ConstraintSet.TOP,ConstraintSet.PARENT_ID,ConstraintSet.BOTTOM,
                        random.nextInt(900)+100);//垂直方向位置
                constraintSet.connect(littleButton.getId(),constraintSet.START,ConstraintSet.PARENT_ID,ConstraintSet.END,
                        random.nextInt(500)+100);//水平方向位置
                constraintSet.applyTo(constraintLayout);//加载至父控件
                /*设置随机小圆点位置*/
                littleButton.setVisibility(View.VISIBLE);
                press.setVisibility(View.GONE);
                pressTimeView.setVisibility(View.GONE);
                tip.setVisibility(View.GONE);
            }
        });
        press.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Calendar calendar = Calendar.getInstance();//初始化获取时间
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        pressTime = calendar.get(Calendar.SECOND)*1000+calendar.get(Calendar.MILLISECOND);//获取点击事件
                        mWorking = true;//开启线程
                        press.setImageResource(R.drawable.loose_to_finish);
                        if (pressMode == SHORT_PRESS){
                            tip.setText("短按时间小于0.5秒");
                        } else if (pressMode == LONG_PRESS){
                            tip.setText("长按时间大于0.5秒");
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        mWorking = false;//关闭线程
                        press.setImageResource(R.drawable.press_to_practice);
                        /*判断是否成功*/
                        if (currentTime - pressTime > 500){
                            if (pressMode == SHORT_PRESS){
                                tip.setText("就差那么一点！");
                            } else if (pressMode == LONG_PRESS){
                                tip.setText("完美！");
                            }
                        } else if (currentTime - pressTime <= 500){
                            if (pressMode == SHORT_PRESS){
                                tip.setText("完美！");
                            } else if (pressMode == LONG_PRESS){
                                tip.setText("就差那么一点！");
                            }
                        }
                        /*判断是否成功*/
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
        littleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                random = new Random();//初始化随机数
                /*设置随机小圆点位置*/
                ConstraintSet constraintSet = new ConstraintSet();
                ConstraintLayout constraintLayout = (ConstraintLayout) findViewById(R.id.press_practice_layout);//获取父控件
                constraintSet.clone(constraintLayout);//设置父控件
                constraintSet.connect(littleButton.getId(),ConstraintSet.TOP,ConstraintSet.PARENT_ID,ConstraintSet.BOTTOM,
                        random.nextInt(900)+100);//垂直方向位置
                constraintSet.connect(littleButton.getId(),constraintSet.START,ConstraintSet.PARENT_ID,ConstraintSet.END,
                        random.nextInt(500)+100);//水平方向位置
                constraintSet.applyTo(constraintLayout);//加载至父控件
                /*设置随机小圆点位置*/
            }
        });
        /*注册点击事件*/
    }

}
