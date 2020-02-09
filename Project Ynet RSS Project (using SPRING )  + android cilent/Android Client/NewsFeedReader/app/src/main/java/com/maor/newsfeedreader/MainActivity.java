package com.maor.newsfeedreader;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    private Button showNewsBtn;
    private Button byAuthorBtn;
    private Button exitBtn;
    private TextView byAuthorTv;
    private String authorName;
    private MediaPlayer btnSound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        showNewsBtn = (Button) findViewById(R.id.show_news_btn);
        showNewsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSound = MediaPlayer.create(MainActivity.this, R.raw.select);
                btnSound.start();
                Intent intent = new Intent(MainActivity.this, AllNewsActivity.class);
                startActivity(intent);
            }
        });


        byAuthorBtn = (Button) findViewById(R.id.by_author_btn);
        byAuthorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSound = MediaPlayer.create(MainActivity.this, R.raw.select);
                btnSound.start();
                byAuthorTv = findViewById(R.id.show_news_tv);
                authorName = byAuthorTv.getText().toString();
                Intent intent = new Intent(MainActivity.this, AllNewsActivity.class);
                intent.putExtra("AuthorKey", authorName);
                startActivity(intent);
            }
        });

        exitBtn = (Button) findViewById(R.id.exit_btn);
        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSound = MediaPlayer.create(MainActivity.this, R.raw.select);
                btnSound.start();
                closeDialogMethod();
            }
        });
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
