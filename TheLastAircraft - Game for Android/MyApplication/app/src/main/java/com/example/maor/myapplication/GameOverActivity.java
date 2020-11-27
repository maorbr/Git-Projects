package com.example.maor.myapplication;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class GameOverActivity extends AppCompatActivity {

    TextView yourScoreTv;
    TextView bestScoreTv;
    int bestScore1, bestScore2, bestScore3;
    int lastScore = 0;
    MediaPlayer btnSound;
    MediaPlayer backgroundSound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        backgroundSound = MediaPlayer.create(GameOverActivity.this, R.raw.death_end);
        backgroundSound.setLooping(true);
        backgroundSound.setVolume(1000, 1000);
        backgroundSound.start();

        yourScoreTv = findViewById(R.id.TextMenuYourScore_num);
        bestScoreTv = findViewById(R.id.TextMenuBestScore_num);

        //load score
        SharedPreferences preferences = getSharedPreferences("SCORE", 0);
        lastScore = preferences.getInt("lastScore", 0);
        bestScore1 = preferences.getInt("bestScore1", 0);
        bestScore2 = preferences.getInt("bestScore2", 0);
        bestScore3 = preferences.getInt("bestScore3", 0);

        //replace if there is higher score
        if (lastScore > bestScore3) {
            bestScore3 = lastScore;
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("bestScore3", bestScore3);
            editor.apply();
        }
        if (lastScore > bestScore2) {
            int temp = bestScore2;
            bestScore2 = lastScore;
            bestScore3 = temp;
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("bestScore3", bestScore3);
            editor.putInt("bestScore2", bestScore2);
            editor.apply();
        }
        if (lastScore > bestScore1) {
            int temp = bestScore1;
            bestScore1 = lastScore;
            bestScore2 = temp;
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("bestScore2", bestScore2);
            editor.putInt("bestScore1", bestScore1);
            editor.apply();
        }

        yourScoreTv.setText(lastScore + "");
        bestScoreTv.setText("1 :    " + bestScore1 + "\n" + "2 :    " + bestScore2 + "\n" + "3 :    " + bestScore3);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_menu_game_over,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.share_action_menu){
            try{
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "My best score on The Last Aircraft is " + lastScore + " !");
                sendIntent.setType("text/plain");
                sendIntent.setPackage("com.whatsapp");
                startActivity(sendIntent);
            }
            catch (ActivityNotFoundException e){
                Toast.makeText(GameOverActivity.this, "Please install WhatsApp for using this action.", Toast.LENGTH_LONG).show();
            }
        }
        else if(item.getItemId() == R.id.go_to_main_menu_action_menu){
            btnSound = MediaPlayer.create(GameOverActivity.this,R.raw.select);
            btnSound.start();

            Intent mStartActivity = new Intent(GameOverActivity.this, MainActivity.class);
//            int mPendingIntentId = 123456;
//            PendingIntent mPendingIntent = PendingIntent.getActivity(GameOverActivity.this, mPendingIntentId, mStartActivity,
//                    PendingIntent.FLAG_CANCEL_CURRENT);
//            AlarmManager mgr = (AlarmManager) GameOverActivity.this.getSystemService(Context.ALARM_SERVICE);
//            mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
//            System.exit(0);
            backgroundSound.pause();
            Intent intent = new Intent(GameOverActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
//        Intent mStartActivity = new Intent(GameOverActivity.this, MainActivity.class);
//        int mPendingIntentId = 123456;
//        PendingIntent mPendingIntent = PendingIntent.getActivity(GameOverActivity.this, mPendingIntentId, mStartActivity,
//                PendingIntent.FLAG_CANCEL_CURRENT);
//        AlarmManager mgr = (AlarmManager) GameOverActivity.this.getSystemService(Context.ALARM_SERVICE);
//        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
//        System.exit(0);
        backgroundSound.pause();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        backgroundSound.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        backgroundSound.start();
    }

}



