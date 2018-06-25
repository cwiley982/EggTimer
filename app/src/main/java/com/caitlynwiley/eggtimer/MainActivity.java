package com.caitlynwiley.eggtimer;

import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    SeekBar timerSeekBar;
    TextView timerTextView;
    Button startStopButton;
    CountDownTimer timer;
    String go = "GO";
    String stop = "STOP";
    int minutes;
    int seconds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timerSeekBar = findViewById(R.id.timerSeekBar);
        timerTextView = findViewById(R.id.timeLeft);
        startStopButton = findViewById(R.id.startStopButton);
        startStopButton.setText(go);

        timerSeekBar.setMax(10 * 60);
        timerSeekBar.setProgress(30);
        timerSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                minutes = progress / 60;
                seconds = progress % 60;
                updateTimer(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        startStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((Button) v).getText().equals(go)) {
                    //disable seek bar
                    timerSeekBar.setEnabled(false);

                    //update button text
                    startStopButton.setText(stop);

                    //start countdown timer
                    long lengthOfTimer = ((minutes * 60) + seconds) * 1000;
                    timer = new CountDownTimer(lengthOfTimer + 100, 1000) {
                        public void onTick(long millisUntilDone) {
                            //update timer text
                            seconds--;
                            if (seconds == -1) {
                                seconds = 59;
                                minutes--;
                            }
                            updateTimer((int) millisUntilDone / 1000);
                        }

                        public void onFinish() {
                            //show 0:00 on timer
                            timerTextView.setText("0:00");
                            //play a sound
                            MediaPlayer mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.alien_siren);
                            mediaPlayer.start();
                            resetTimer();
                        }
                    }.start();
                } else {
                    //stop timer
                    timer.cancel();
                    resetTimer();
                }
            }
        });
    }

    public void updateTimer(int secondsLeft) {
        String secString = String.valueOf(seconds);
        if (secString.length() == 1) {
            secString = "0" + secString;
        }
        timerTextView.setText(String.format("%d:%s", minutes, secString));
    }

    public void resetTimer() {
        //enable seek bar
        timerSeekBar.setEnabled(true);
        //set timer to 30 seconds
        timerSeekBar.setProgress(30);
        //update button text
        startStopButton.setText(go);
    }
}
