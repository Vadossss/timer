package com.example.timer;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private CheckBox mSingleShotCheckBox;
    private Button mStartButton, mCancelButton;
    private TextView mCounterTextView;
    private Timer myTimer;
    private TextView time;
    private Switch switchBtn;
    private TextView textTime;
    private TextView tm;
    private TextView ts;
    private int minutes = 0;
    private int seconds = 0;
    MediaPlayer mPlayer;

    public int getMinutes() {
        return minutes;
    }
    public void setMinutes(int value) {
        if (value > 59) {
            minutes = 59;
            //tm.setText("59");
        }
        else if (value < 0) {
            minutes = 0;
        }
        else {
            minutes = value;
        }
    }
    public int getSeconds() {
        return seconds;
    }
    public void setSeconds(int value) {
        if (value > 59) {
            seconds = 59;
            //ts.setText("59");
        }
        else if (value < -1) {
            seconds = 0;
        }
        else {
            seconds = value;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textTime = (TextView)findViewById(R.id.time);
        switchBtn = (Switch) findViewById(R.id.switch_timer);


        ImageView img = findViewById(R.id.animationView);
        // устанавливаем ресурс анимации
        img.setBackgroundResource(R.drawable.animation);
        // получаем объект анимации
        frameAnimation = (AnimationDrawable) img.getBackground();
        mPlayer= MediaPlayer.create(this, R.raw.music);
    }

    private void timerTick() {
        this.runOnUiThread(doTask);
    }

    private Runnable doTask = new Runnable() {
        public void run() {
            if (getSeconds() == 0 && getMinutes() > 0) {
                setMinutes(getMinutes()-1);
                seconds = 60;
            }

            if (getSeconds() > 0) {
                setSeconds(getSeconds()-1);
            }
            textTime.setText(String.format("%02d:%02d", getMinutes(), getSeconds()));

            if (getSeconds() == 0 && getMinutes() == 0) {
                myTimer.cancel();
                frameAnimation.stop();
                switchBtn.setChecked(false);
                mPlayer.start();
//                mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                    @Override
//                    public void onCompletion(MediaPlayer mp) {
//                        mPlayer.stop();
//                    }
//                });
            }

        }
    };

    boolean flagOn = false;
    AnimationDrawable frameAnimation;
    public void switchClick(View v) {
        //flagOn = !flagOn;
        if (getMinutes() == 0 && getSeconds() == 0) {
            switchBtn.setChecked(false);
            return;
        }
        if (switchBtn.isChecked()) {
            myTimer = new Timer();
            myTimer.schedule(new TimerTask() {
                public void run() {
                    timerTick();
                }
            }, 0, 1000); // каждые 5 секунд
            seconds++;
            frameAnimation.start();
        }
        else if (!switchBtn.isChecked()) {
            myTimer.cancel();
            frameAnimation.stop();
        }


    }
    private void newValue() {
        tm = (TextView) findViewById(R.id.minute);
        ts = (TextView) findViewById(R.id.seconds);
        if (!tm.getText().toString().isEmpty()) {
            setMinutes(Integer.parseInt(tm.getText().toString()));
        }
        else if (tm.getText().toString().isEmpty()) {
            setMinutes(0);
        }
        if (!ts.getText().toString().isEmpty()) {
            setSeconds(Integer.parseInt(ts.getText().toString()));
        }
        else if (ts.getText().toString().isEmpty()) {
            setSeconds(0);
        }
        textTime.setText(String.format("%02d:%02d", getMinutes(), getSeconds()));
    }
    public void refreshClick(View v) {
        newValue();
        if (myTimer != null)
            myTimer.cancel();
        switchBtn.setChecked(false);
        frameAnimation.stop();
    }
    public void confirmClick(View v) {
        newValue();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(tm.getWindowToken(), 0);
        tm.clearFocus();
        ts.clearFocus();
    }
}