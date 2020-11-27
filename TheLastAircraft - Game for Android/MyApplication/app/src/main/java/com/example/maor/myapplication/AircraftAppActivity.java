package com.example.maor.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class AircraftAppActivity extends AppCompatActivity {

    private AircraftView appView;
    private Handler handler = new Handler();
    private final static long Interval = 30;
    private  Timer timer = new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        appView = new AircraftView(this, null);
        setContentView(appView);
        timerSchedule();
    }

    private void timerSchedule(){
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        appView.invalidate();
                    }
                });
            }
        }, 0, Interval);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (appView.backgroundSound.isPlaying()) {
            appView.backgroundSound.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (MainActivity.isBackgroundSoundFlag() == true)
            appView.backgroundSound.start();
        appView.backgroundSound.setVolume(5, 5);
    }

    @Override
    public void onBackPressed() {
        closeDialogMethod();
    }

    private void closeDialogMethod() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.dialog_quit_title_show).setIcon(R.drawable.ic_keyboard_backspace).setMessage(R.string.game_dialog_quit_message)
                .setPositiveButton(R.string.dialog_quit_yes, new AircraftAppActivity.ExitGameAlertDialogListener())
                .setNegativeButton(R.string.dialog_quit_no, new AircraftAppActivity.ExitGameAlertDialogListener()).setCancelable(true).show();
    }

    private class ExitGameAlertDialogListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (which == DialogInterface.BUTTON_POSITIVE) {
                Intent intent = new Intent(AircraftAppActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else if (which == DialogInterface.BUTTON_NEGATIVE) {
                //timerSchedule();
                Toast.makeText(AircraftAppActivity.this, R.string.dialog_quit_toast, Toast.LENGTH_SHORT).show();
            }
        }
    }



}
