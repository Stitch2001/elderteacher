package com.gdbjzx.elderteacher;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class PressPracticeActivity extends AppCompatActivity {

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
        final ImageView press = (ImageView) findViewById(R.id.press);
        final ImageView littleButton = (ImageView) findViewById(R.id.little_button);
        final TextView pressTime = (TextView) findViewById(R.id.press_time);
        final TextView tip = (TextView) findViewById(R.id.tip);
        /*注册控件*/
        shortPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                littleButton.setVisibility(View.GONE);
                press.setVisibility(View.VISIBLE);
                pressTime.setVisibility(View.VISIBLE);
                tip.setVisibility(View.VISIBLE);
                tip.setText("短按时间小于1秒");
            }
        });
        longPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                littleButton.setVisibility(View.GONE);
                press.setVisibility(View.VISIBLE);
                pressTime.setVisibility(View.VISIBLE);
                tip.setVisibility(View.VISIBLE);
                tip.setText("长按时间大于1秒");
            }
        });
        exactlyPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double littleButtonX = Math.random();
                double littleButtonY = Math.random();
                Log.d("PressPracticeActivity",Double.toString(littleButtonX));
                //
                littleButton.setVisibility(View.VISIBLE);
                press.setVisibility(View.GONE);
                pressTime.setVisibility(View.GONE);
                tip.setVisibility(View.GONE);
            }
        });
    }

}
