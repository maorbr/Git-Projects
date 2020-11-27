package com.example.maor.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class AircraftView extends View {

    private Bitmap background;
    private int canvasWidth, CanvasHeight;
    private boolean touchScreenEnable = false;

    private Paint levelUp = new Paint();
    int levelCounter;

    private Paint scorePaint = new Paint();
    static private int score;
    final private Bitmap scoreIcon[] = new Bitmap[1];

    private Paint healthPaint = new Paint();
    private int healthCounter;
    final private Bitmap health[] = new Bitmap[7];

    private double objectOddsAppearing;

    final private Bitmap jet[] = new Bitmap[2];
    private int jetAxisX = 15, jetAxisY, jetSpeed;
    private int minJetY, maxJetY;

    final private Bitmap recoverTool[] = new Bitmap[1];
    private int recoverToolAxisX, recoverToolAxisY, recoverToolSpeed = 15;
    final private Bitmap recoverFlash[] = new Bitmap[1];

    private int DegreeRotate = 360;

    private int coinAxisX, coinAxisY, coinSpeed = 10, coinDegreeRotateSpeed = 10;
    final private Bitmap coin[] = new Bitmap[1];
    final private Bitmap coinFlash[] = new Bitmap[1];

    private int gemAxisX, gemAxisY, gemSpeed = 5, gemDegreeRotateSpeed = 5;
    final private Bitmap gem[] = new Bitmap[1];
    private int treasureAxisX, treasureAxisY, treasureSpeed = 8;
    final private Bitmap treasure[] = new Bitmap[1];
    final private Bitmap gemFlash[] = new Bitmap[2];

    private int rocketAxisX, rocketAxisY, rocketSpeed = 25;
    final Bitmap rocket[] = new Bitmap[1];
    private int deadlyRocketAxisX, deadlyRocketAxisY, deadlyRocketSpeed = 20;
    final private Bitmap deadlyRocket[] = new Bitmap[1];
    final private Bitmap explosion[] = new Bitmap[2];

    final private MediaPlayer gemCollectSound;
    final private MediaPlayer treasureCollectSound;
    final private MediaPlayer coinCollectSound;
    final private MediaPlayer RocketCollectSound;
    final private MediaPlayer RecoverCollectSound;
    final private MediaPlayer explosionSound;
    public MediaPlayer backgroundSound;

    public AircraftView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        backgroundSound = MediaPlayer.create(this.getContext(), R.raw.lord);
        backgroundSound.setLooping(true); // Set looping
        backgroundSound.setVolume(5, 5);
        if (MainActivity.isBackgroundSoundFlag() == true) {
            backgroundSound.start();
        }

        coinCollectSound = MediaPlayer.create(this.getContext(), R.raw.coincollect);
        coinCollectSound.setVolume(1000, 1000);

        gemCollectSound = MediaPlayer.create(this.getContext(), R.raw.gemcollect);
        gemCollectSound.setVolume(10000, 10000);

        treasureCollectSound = MediaPlayer.create(this.getContext(), R.raw.gemcollect);
        treasureCollectSound.setVolume(10000, 10000);

        RecoverCollectSound = MediaPlayer.create(this.getContext(), R.raw.recover);
        RecoverCollectSound.setVolume(10000, 10000);

        explosionSound = MediaPlayer.create(this.getContext(), R.raw.explosion);
        explosionSound.setVolume(1000000, 1000000);

        RocketCollectSound = MediaPlayer.create(this.getContext(), R.raw.hit);
        RocketCollectSound.setVolume(1000, 1000);

        try {
            jet[0] = BitmapFactory.decodeResource(getResources(), R.drawable.jet01);
            jet[1] = BitmapFactory.decodeResource(getResources(), R.drawable.jet02);
            scoreIcon[0] = BitmapFactory.decodeResource(getResources(), R.drawable.score);
            background = BitmapFactory.decodeResource(getResources(), R.drawable.sky2);
            coin[0] = BitmapFactory.decodeResource(getResources(), R.drawable.coin);
            coinFlash[0] = BitmapFactory.decodeResource(getResources(), R.drawable.coincollect);
            gem[0] = BitmapFactory.decodeResource(getResources(), R.drawable.gem);
            gemFlash[0] = BitmapFactory.decodeResource(getResources(), R.drawable.gemflash);
            gemFlash[1] = BitmapFactory.decodeResource(getResources(), R.drawable.gemflash2);
            explosion[0] = BitmapFactory.decodeResource(getResources(), R.drawable.explosion);
            explosion[1] = BitmapFactory.decodeResource(getResources(), R.drawable.explosion2);
            recoverFlash[0] = BitmapFactory.decodeResource(getResources(), R.drawable.muzzleflash);
            deadlyRocket[0] = BitmapFactory.decodeResource(getResources(), R.drawable.deadlyrocket);
            treasure[0] = BitmapFactory.decodeResource(getResources(), R.drawable.treasure);
            recoverTool[0] = BitmapFactory.decodeResource(getResources(), R.drawable.recover);
            rocket[0] = BitmapFactory.decodeResource(getResources(), R.drawable.rocket);
            health[0] = BitmapFactory.decodeResource(getResources(), R.drawable.life0);
            health[1] = BitmapFactory.decodeResource(getResources(), R.drawable.life1);
            health[2] = BitmapFactory.decodeResource(getResources(), R.drawable.life2);
            health[3] = BitmapFactory.decodeResource(getResources(), R.drawable.life3);
            health[4] = BitmapFactory.decodeResource(getResources(), R.drawable.life4);
            health[5] = BitmapFactory.decodeResource(getResources(), R.drawable.life5);
            health[6] = BitmapFactory.decodeResource(getResources(), R.drawable.life6);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        scorePaint.setColor(Color.YELLOW);
        scorePaint.setTextSize(80);
        scorePaint.setTypeface(Typeface.DEFAULT_BOLD);
        scorePaint.setAntiAlias(true);

        healthPaint.setColor(Color.WHITE);
        healthPaint.setTextSize(60);
        healthPaint.setTypeface(Typeface.DEFAULT_BOLD);
        healthPaint.setAntiAlias(true);

        levelUp.setColor(Color.RED);
        levelUp.setTextSize(100);
        levelUp.setTypeface(Typeface.DEFAULT_BOLD);
        levelUp.setAntiAlias(true);

        levelCounter = 0;
        jetAxisY = 100;
        score = 0;
        healthCounter = 6;
        objectOddsAppearing = 0;
        minJetY = jet[0].getHeight();
        maxJetY = CanvasHeight - minJetY;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawBitmap(background, 0, 0, null);

        canvasWidth = canvas.getWidth();
        CanvasHeight = canvas.getHeight();
        objectOddsAppearing++;

        jetAxisY = jetAxisY + jetSpeed;
        if (jetAxisY < minJetY) {
            jetAxisY = minJetY;
        }
        if (jetAxisY > maxJetY) {
            jetAxisY = maxJetY;
        }
        jetSpeed += 2;

        if (touchScreenEnable) {
            canvas.drawBitmap(jet[1], jetAxisX, jetAxisY, null);
            touchScreenEnable = false;
        } else {
            canvas.drawBitmap(jet[0], jetAxisX, jetAxisY, null);
        }

        coinAxisX = coinAxisX - coinSpeed;
        gemAxisX = gemAxisX - gemSpeed;
        recoverToolAxisX = recoverToolAxisX - recoverToolSpeed;
        rocketAxisX = rocketAxisX - rocketSpeed;
        deadlyRocketAxisX = deadlyRocketAxisX - deadlyRocketSpeed;
        treasureAxisX = treasureAxisX - treasureSpeed;

        if (catchChecker(coinAxisX, coinAxisY)) {
            coinCollectSound.start();
            canvas.drawBitmap(coinFlash[0], jetAxisX + 200, jetAxisY - 50, null);
            score += 5;
            coinAxisX = -100;
        }

        if (catchChecker(gemAxisX, gemAxisY)) {
            gemCollectSound.start();
            canvas.drawBitmap(gemFlash[0], jetAxisX + 200, jetAxisY + 20, null);
            score += 50;
            gemAxisX = -100;
        }

        if (catchChecker(treasureAxisX, treasureAxisY)) {
            treasureCollectSound.start();
            canvas.drawBitmap(gemFlash[1], jetAxisX + 200, jetAxisY + 20, null);
            score += 200;
            treasureAxisX = -145;
        }

        if (catchChecker(recoverToolAxisX, recoverToolAxisY)) {
            RecoverCollectSound.start();
            canvas.drawBitmap(recoverFlash[0], jetAxisX + 200, jetAxisY, null);
            healthCounter = 6;
            recoverToolAxisX = -100;
        }

        if (catchChecker(rocketAxisX, rocketAxisY) || catchChecker(deadlyRocketAxisX, deadlyRocketAxisY)) {

            if (catchChecker(rocketAxisX, rocketAxisY)) {
                rocketAxisX = -100;
                healthCounter--;
                canvas.drawBitmap(explosion[1], jetAxisX + 100, jetAxisY + 20, null);
            }

            if (catchChecker(deadlyRocketAxisX, deadlyRocketAxisY)) {
                canvas.drawBitmap(explosion[0], deadlyRocketAxisX + 100, deadlyRocketAxisY + 20, null);
                canvas.drawBitmap(explosion[0], deadlyRocketAxisX - 100, deadlyRocketAxisY - 20, null);
                deadlyRocketAxisX = -140;
                healthCounter = 0;
            }

            if (healthCounter == 0) {
                backgroundSound.stop();
                explosionSound.start();
                canvas.drawBitmap(health[0], 20, 15, null);
                canvas.drawBitmap(explosion[0], jetAxisX + 50, jetAxisY + 20, null);
                canvas.drawBitmap(explosion[0], jetAxisX + 100, jetAxisY + 20, null);
                canvas.drawBitmap(explosion[0], jetAxisX + 20, jetAxisY + 20, null);
                canvas.drawBitmap(explosion[0], jetAxisX + 120, jetAxisY + 20, null);
                canvas.drawBitmap(explosion[0], jetAxisX + 70, jetAxisY + 20, null);
                Toast.makeText(getContext(), R.string.game_game_over, Toast.LENGTH_LONG).show();

                SharedPreferences preferences = getContext().getSharedPreferences("SCORE", 0);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("lastScore", score);
                editor.apply();

                backgroundSound.reset();

                Intent gameOverIntent = new Intent(getContext(), GameOverActivity.class);
                gameOverIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                getContext().startActivity(gameOverIntent);
            } else {
                RocketCollectSound.start();
            }
        }

        if (objectOddsAppearing > 20) {
            coinRandomThrowing();
            gemRandomThrowing();
            treasureRandomThrowing();
            rocketRandomThrowing();
            deadlyRocketRandomThrowing();
            recoverToolRandomThrowing();

            canvas.drawBitmap(coin[0], coinRotateMe(), null);
            canvas.drawBitmap(gem[0], gemRotateMe(), null);
            canvas.drawBitmap(rocket[0], rocketAxisX, rocketAxisY, null);
            canvas.drawBitmap(recoverTool[0], recoverToolAxisX, recoverToolAxisY, null);
            canvas.drawBitmap(deadlyRocket[0], deadlyRocketAxisX, deadlyRocketAxisY, null);
            canvas.drawBitmap(treasure[0], treasureAxisX, treasureAxisY, null);

            canvas.drawBitmap(scoreIcon[0], 5, 100, null);
            canvas.drawText(score + "", 180, 180, scorePaint);
            canvas.drawText("HP :", 10, 60, healthPaint);
            for (int i = 1; i < 7; i++)
                if (healthCounter == i) {
                    canvas.drawBitmap(health[i], 130, 15, null);
                }
        }

        gameSpeedLevelUp(score);
        levelUpToast(score);
    }

    public boolean catchChecker(int x, int y) {
        if (jetAxisX < x && x < (jetAxisX + jet[0].getWidth()) && jetAxisY < y && y < (jetAxisY + jet[0].getWidth())) {
            return true;
        }
        return false;
    }

    public void coinRandomThrowing() {
        minJetY = jet[0].getHeight();
        maxJetY = CanvasHeight - minJetY;

        if (coinAxisX < 0) {
            coinAxisX = canvasWidth + 20;
            coinAxisY = (int) Math.floor((Math.random() * (maxJetY - minJetY * 0.45)) + minJetY);
        }
    }

    public void gemRandomThrowing() {
        minJetY = jet[0].getHeight();
        maxJetY = CanvasHeight - minJetY;

        if (gemAxisX < 0) {
            if (objectOddsAppearing % 500 == 0) {
                gemAxisX = canvasWidth + 20;
                gemAxisY = (int) Math.floor((Math.random() * (maxJetY - minJetY * 0.5)) + minJetY);
            }
        }
    }

    public void treasureRandomThrowing() {
        minJetY = jet[0].getHeight();
        maxJetY = CanvasHeight - minJetY;

        if (treasureAxisX < 0) {
            if (objectOddsAppearing % 2100 == 0) {
                treasureAxisX = canvasWidth + 20;
                treasureAxisY = (int) Math.floor((Math.random() * (maxJetY - minJetY * 0.9)) + minJetY);
            }
        }
    }

    public void rocketRandomThrowing() {
        minJetY = jet[0].getHeight();
        maxJetY = CanvasHeight - minJetY;

        if (rocketAxisX < 0) {
            rocketAxisX = canvasWidth + 20;
            rocketAxisY = (int) Math.floor((Math.random() * (maxJetY - minJetY * 0.3)) + minJetY);
        }
    }

    public void deadlyRocketRandomThrowing() {
        minJetY = jet[0].getHeight();
        maxJetY = CanvasHeight - minJetY;

        if (deadlyRocketAxisX < 0) {
            if (objectOddsAppearing % 300 == 0) {
                deadlyRocketAxisX = canvasWidth + 20;
                deadlyRocketAxisY = (int) Math.floor((Math.random() * (maxJetY - minJetY * 0.8)) + minJetY);
            }
        }
    }

    public void recoverToolRandomThrowing() {
        minJetY = jet[0].getHeight();
        maxJetY = CanvasHeight - minJetY;

        if (recoverToolAxisX < 0) {
            if (objectOddsAppearing % 1600 == 0) {
                recoverToolAxisX = canvasWidth + 20;
                recoverToolAxisY = (int) Math.floor((Math.random() * (maxJetY - minJetY * 0.5)) + minJetY);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.AXIS_PRESSURE) {
            touchScreenEnable = true;
            jetSpeed = -25;
        }
        return true;
    }

    public Matrix coinRotateMe() {
        Matrix mtx = new Matrix();
        mtx.postRotate(DegreeRotate, coin[0].getHeight() / 2, coin[0].getWidth() / 2);
        mtx.postTranslate(coinAxisX, coinAxisY);  //The coordinates where we want to put our bitmap
        DegreeRotate = DegreeRotate - coinDegreeRotateSpeed; //degree of rotation
        return mtx;
    }

    public Matrix gemRotateMe() {
        Matrix mtx = new Matrix();
        mtx.postRotate(DegreeRotate, 0, gem[0].getWidth() - 10);
        mtx.postTranslate(gemAxisX, gemAxisY);  //The coordinates where we want to put our bitmap
        DegreeRotate = DegreeRotate - gemDegreeRotateSpeed; //degree of rotation
        return mtx;
    }

    public void gameSpeedLevelUp(int score) {

        if (score <= 200) {
            recoverToolSpeed = 12;
            coinSpeed = 13;
            gemSpeed = 8;
            rocketSpeed = 28;
            deadlyRocketSpeed = 22;
            treasureSpeed = 10;
            coinDegreeRotateSpeed = 12;
            gemDegreeRotateSpeed = 7;
        } else if (score <= 500) {
            recoverToolSpeed = 15;
            coinSpeed = 15;
            gemSpeed = 10;
            rocketSpeed = 38;
            deadlyRocketSpeed = 30;
            treasureSpeed = 14;
            coinDegreeRotateSpeed = 18;
            gemDegreeRotateSpeed = 9;
        } else if (score <= 1000) {
            recoverToolSpeed = 17;
            coinSpeed = 22;
            gemSpeed = 14;
            rocketSpeed = 43;
            deadlyRocketSpeed = 38;
            treasureSpeed = 17;
            coinDegreeRotateSpeed = 22;
            gemDegreeRotateSpeed = 10;
        } else if (score <= 1500) {
            recoverToolSpeed = 20;
            coinSpeed = 24;
            gemSpeed = 22;
            rocketSpeed = 45;
            deadlyRocketSpeed = 36;
            treasureSpeed = 22;
            coinDegreeRotateSpeed = 28;
            gemDegreeRotateSpeed = 14;
        } else if (score <= 2000) {
            recoverToolSpeed = 25;
            coinSpeed = 40;
            gemSpeed = 35;
            rocketSpeed = 68;
            deadlyRocketSpeed = 50;
            treasureSpeed = 30;
            coinDegreeRotateSpeed = 35;
            gemDegreeRotateSpeed = 16;
        } else if (score <= 3000) {
            recoverToolSpeed = 20;
            coinSpeed = 50;
            gemSpeed = 40;
            rocketSpeed = 85;
            deadlyRocketSpeed = 70;
            treasureSpeed = 42;
            coinDegreeRotateSpeed = 50;
            gemDegreeRotateSpeed = 20;
        } else{
            recoverToolSpeed = 30;
            coinSpeed = 65;
            gemSpeed = 55;
            rocketSpeed = 105;
            deadlyRocketSpeed = 80;
            treasureSpeed = 50;
            coinDegreeRotateSpeed = 60;
            gemDegreeRotateSpeed = 30;
        }
    }

    public void levelUpToast(int score) {
        if (this.score >= 200 && this.score <= 350) {
            if (levelCounter == 0) {
                Toast.makeText(getContext(), R.string.game_speed_up, Toast.LENGTH_LONG).show();
                levelCounter++;
            }
        } else if (this.score >= 500 && this.score <= 650) {
            if (levelCounter == 1) {
                Toast.makeText(getContext(), R.string.game_speed_up, Toast.LENGTH_LONG).show();
                levelCounter++;
            }
        } else if (this.score >= 1000 && this.score <= 1150) {
            if (levelCounter == 2) {
                Toast.makeText(getContext(), R.string.game_speed_up, Toast.LENGTH_LONG).show();
                levelCounter++;
            }
        } else if (this.score >= 1500 && this.score <= 1650) {
            if (levelCounter == 3) {
                Toast.makeText(getContext(), R.string.game_speed_up, Toast.LENGTH_LONG).show();
                levelCounter++;
            }
        } else if (this.score >= 2000 && this.score <= 2150) {
            if (levelCounter == 4) {
                Toast.makeText(getContext(), R.string.game_speed_up, Toast.LENGTH_LONG).show();
                levelCounter++;
            }
        } else if (this.score >= 3000 && this.score <= 3150) {
            if (levelCounter == 5) {
                Toast.makeText(getContext(), R.string.game_speed_up, Toast.LENGTH_LONG).show();
                levelCounter++;
            }
        }
    }


}
