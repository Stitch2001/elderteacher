package com.gdbjzx.elderteacher;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SetStudyNumberActivity extends AppCompatActivity {

    private int maxReviewNumber = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_study_number);
        final TextView studyNumberView = (TextView) findViewById(R.id.study_number_view);
        Button increase = (Button) findViewById(R.id.increase);
        Button decrease = (Button) findViewById(R.id.decrease);
        Button submit = (Button) findViewById(R.id.submit);
        increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                maxReviewNumber++;
                studyNumberView.setText("每 "+ Integer.toString(maxReviewNumber)+" 步复习一次");
            }
        });
        decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                maxReviewNumber--;
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
}
