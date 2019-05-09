package com.example.musica;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    Button plps, prev, next, slctbtn;
    SeekBar prgbar;
    TextView tf1, tf2;
    MediaPlayer mp;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate: Thread : "+ Thread.currentThread().getId());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        plps = findViewById(R.id.plbtn);
        plps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                plpsClick();
            }
        });
        prev = findViewById(R.id.prevbtn);
        next = findViewById(R.id.nxtbtn);
        slctbtn = findViewById(R.id.slctbtn);
        mp = MediaPlayer.create(this, R.raw.jumpsuit);
        mp.setVolume(100, 100);
        tf1 = findViewById(R.id.textView);
        tf2 = findViewById(R.id.textView2);
        tf1.setText(formatTime(mp.getCurrentPosition()));
        tf2.setText(formatTime(mp.getDuration()));
        prgbar = findViewById(R.id.seekBar);
        prgbar.setMax(mp.getDuration());
        prgbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tf1.setText(formatTime(progress));
                mp.seekTo(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mp != null) {
                    try {
                        Log.i(TAG, "Updater Thread : " + Thread.currentThread().getId() + " " + mp.getCurrentPosition());
                        Message msg = new Message();
                        msg.what = mp.getCurrentPosition();
                        handler.sendMessage(msg);
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            prgbar.setProgress(msg.what);
            tf1.setText(formatTime(msg.what));
        }
    };

    public String formatTime(int ms) {
        int min = (ms / 1000) / 60;
        int sec = (ms / 1000) % 60;
        String time = min + ":";
        if (sec < 10) {
            time = time + "0" + sec;
        } else {
            time = time + sec;
        }
        return time;
    }

    public void plpsClick() {
        if (mp.isPlaying()) {
            mp.pause();
            plps.setText("Play");
        } else {
            mp.start();
            plps.setText("Pause");
        }
    }
}
