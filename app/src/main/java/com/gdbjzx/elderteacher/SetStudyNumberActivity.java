package com.gdbjzx.elderteacher;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SetStudyNumberActivity extends AppCompatActivity {

    private int maxReviewNumber = 3;

    private int wholeStepNumber;

    private MediaPlayer mediaPlayer;

    private boolean isPlaying = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_study_number);
        /*注册控件*/
        final TextView studyNumberView = (TextView) findViewById(R.id.study_number_view);
        final Button increase = (Button) findViewById(R.id.increase);
        final Button decrease = (Button) findViewById(R.id.decrease);
        Button submit = (Button) findViewById(R.id.submit);
        /*注册控件*/

        /*播放语音*/
        mediaPlayer = MediaPlayer.create(SetStudyNumberActivity.this,R.raw.study_step_2);
        mediaPlayer.start();
        isPlaying = true;
        /*播放语音*/

        Intent intent = getIntent();
        intent.getIntExtra("wholeStepNumber", wholeStepNumber);
        decrease.setEnabled(false);
        increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                maxReviewNumber++;
                decrease.setEnabled(true);
                if (wholeStepNumber - 2 <= maxReviewNumber){
                    increase.setEnabled(false);
                }
                studyNumberView.setText("每 "+ Integer.toString(maxReviewNumber)+" 步复习一次");
            }
        });
        decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                maxReviewNumber--;
                increase.setEnabled(true);
                if (maxReviewNumber == 3) decrease.setEnabled(false);
                studyNumberView.setText("每 "+ Integer.toString(maxReviewNumber)+" 步复习一次");
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("maxReviewNumber",maxReviewNumber);
                setResult(RESULT_OK,intent);
                finish();
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
