package com.stopwatchapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

public class MainActivity extends BaseActivity {
    private AppCompatTextView timerTextView;
    private AppCompatButton startButton, pauseButton, resetButton;
    private Handler handler;
    private AppCompatImageView imageView;
    private long startTime, timeInMilliseconds, timeSwapBuff, updateTime = 0L;

    private Runnable updateTimerThread = new Runnable() {
        @Override
        public void run() {
            timeInMilliseconds = System.currentTimeMillis() - startTime;

            updateTime = timeSwapBuff + timeInMilliseconds;

            int secs = (int) (updateTime / 1000);
            int mins = secs / 60;
            secs = secs % 60;
            int milliseconds = (int) (updateTime % 1000);

            // Formatting the string to include minutes, seconds, and milliseconds
            timerTextView.setText(String.format("%02d:%02d:%02d.%02d", mins, secs, milliseconds / 10, milliseconds % 10));

            handler.postDelayed(this, 0);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        removeStatusBarWithBlackIcon();
        timerTextView = findViewById(R.id.timerTextView);
        startButton = findViewById(R.id.startButton);
        pauseButton = findViewById(R.id.pauseButton);
        resetButton = findViewById(R.id.resetButton);
        imageView = findViewById(R.id.imageView);

        handler = new Handler();

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTime = System.currentTimeMillis();
                handler.postDelayed(updateTimerThread, 0);
                startButton.setEnabled(false);
                pauseButton.setEnabled(true);
                imageView.setImageResource(R.drawable.stopwatch_pause);

            }
        });

        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeSwapBuff += timeInMilliseconds;
                handler.removeCallbacks(updateTimerThread);
                startButton.setEnabled(true);
                pauseButton.setEnabled(false);
                imageView.setImageResource(R.drawable.stopwatch_play);
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTime = 0L;
                timeSwapBuff = 0L;
                timeInMilliseconds = 0L;
                updateTime = 0L;
                timerTextView.setText("00:00:00.00");
                handler.removeCallbacks(updateTimerThread);
                startButton.setEnabled(true);
                pauseButton.setEnabled(false);
                imageView.setImageResource(R.drawable.stopwatch_play);
            }
        });

        // Initially, the pause button should be disabled
        pauseButton.setEnabled(false);
    }
}