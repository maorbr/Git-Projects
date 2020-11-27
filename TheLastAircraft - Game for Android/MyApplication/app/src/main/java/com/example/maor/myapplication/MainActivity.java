package com.example.maor.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    MediaPlayer backgroundSound;
    MediaPlayer btnSound;
    private static boolean backgroundSoundFlag = true;
    private static ImageButton popupMenuBtn;


    public static boolean isBackgroundSoundFlag() {
        return backgroundSoundFlag;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        backgroundSound = MediaPlayer.create(MainActivity.this, R.raw.alienswamp);
        backgroundSound.setLooping(true);
        backgroundSound.setVolume(1000, 1000);
        if (MainActivity.isBackgroundSoundFlag() == true) {
            backgroundSound.start();
        }

        popupMenuBtn = findViewById(R.id.sound_btn);
        popupMenuBtn.setBackgroundResource(R.drawable.ic_volume_up_black_24dp);

        popupMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(MainActivity.this, view);
                getMenuInflater().inflate(R.menu.action_main_menu, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.sound_action_menu_enable:
                                popupMenuBtn.setBackgroundResource(R.drawable.ic_volume_up_black_24dp);
                                backgroundSound.start();
                                backgroundSoundFlag = true;
                                return true;

                            case R.id.sound_action_menu_disable:
                                popupMenuBtn.setBackgroundResource(R.drawable.ic_volume_off_black_24dp);
                                backgroundSound.pause();
                                backgroundSoundFlag = false;
                                return true;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });

        ImageButton aboutBtn = findViewById(R.id.about_btn);
        aboutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, R.string.about_btn, Toast.LENGTH_SHORT).show();
            }
        });

        ImageButton createMainActivityBtn = findViewById(R.id.play_btn);
        createMainActivityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSound = MediaPlayer.create(MainActivity.this, R.raw.select);
                btnSound.start();
                Intent intent = new Intent(MainActivity.this, AircraftAppActivity.class);
                startActivity(intent);
            }
        });

        ImageButton quitBtn = findViewById(R.id.quit_btn);
        quitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSound = MediaPlayer.create(MainActivity.this, R.raw.select);
                btnSound.start();
                closeDialogMethod();
            }
        });

        ImageButton helpBtn = findViewById(R.id.help_btn);
        helpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSound = MediaPlayer.create(MainActivity.this, R.raw.select);
                btnSound.start();
                Intent intent = new Intent(MainActivity.this, HelpActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        backgroundSound.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (backgroundSoundFlag == true) {
            backgroundSound.start();
        }
    }

    @Override
    public void onBackPressed() {
        closeDialogMethod();
    }

    private void closeDialogMethod() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.dialog_quit_title_show).setIcon(R.drawable.ic_exit_to_app).setMessage(R.string.dialog_quit_message)
                .setPositiveButton(R.string.dialog_quit_yes, new ExitAppAlertDialogListener()).setNegativeButton(R.string.dialog_quit_no, new ExitAppAlertDialogListener()).setCancelable(false).show();
    }

    private class ExitAppAlertDialogListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (which == DialogInterface.BUTTON_POSITIVE) {
                int pid = android.os.Process.myPid();
                android.os.Process.killProcess(pid);
                finish();
            } else if (which == DialogInterface.BUTTON_NEGATIVE) {
                Toast.makeText(MainActivity.this, R.string.dialog_quit_toast, Toast.LENGTH_SHORT).show();
            }
        }

    }

}
