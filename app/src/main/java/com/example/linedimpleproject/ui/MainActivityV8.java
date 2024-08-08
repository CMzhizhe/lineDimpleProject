package com.example.linedimpleproject.ui;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.linedimpleproject.R;
import com.gxx.linelibrary.RecentView;
import com.gxx.linelibrary.StepsLineView;

public class MainActivityV8 extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_v8);

        RecentView recentView = this.findViewById(R.id.recent_view);
        Button btStart = findViewById(R.id.bt_start);


        ValueAnimator valueAnimator =  ValueAnimator.ofFloat(0.0f,1.0f);
        btStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                valueAnimator.start();
            }
        });

        valueAnimator.setDuration(10 * 1000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(@NonNull ValueAnimator animation) {
                float progress = (float) animation.getAnimatedValue();
                recentView.setProgress(progress);
            }
        });

    }
}
