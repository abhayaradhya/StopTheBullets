package com.example.abhayaradhya.finalproject;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.Date;
import java.util.Queue;
import java.util.Random;

public class Game1 extends AppCompatActivity {

    private Player player;
    private RelativeLayout relLayout;
    private ImageView gameView;
    private ImageView playerView;
    private ImageView shieldView;
    private ArrayList<Integer> enemyQueue; //controls timing of enemy spawns
    private ArrayList<Bullet> enemyList; //lists all enemy objects that currently exist
    private int queueIndex;
    private double viewHeight;
    private double viewWidth;
    private double leftButtonWidth;
    private double midButtonsSplit;
    private double rightButtonWidth;
    private double startTime;
    private double bpm;
    private double gameSpeed;
    private boolean playing;
    private Update update;
    private Handler handler;
    private Rect shieldRect;
    private Rect playerRect;
    private int shieldDirection;
    private int health;
    private static Activity gameActivity;

    //1 = left, 2 = top, 3 = bottom, 4 = right

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_game1);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        gameActivity = this;

        bpm = 120;
        gameSpeed = (60/bpm) * 1000;

        health = 3;
        update = new Update();
        handler = new Handler();
        relLayout = findViewById(R.id.relativeLayout);
        playerView = findViewById(R.id.playerView);
        shieldView = findViewById(R.id.shieldView);
        //shieldView.setScaleType(ImageView.ScaleType.MATRIX);
        //enemyView = findViewById(R.id.enemyView);
        gameView = findViewById(R.id.baseView);
        enemyQueue = new ArrayList<Integer>();
        enemyList = new ArrayList<Bullet>();
        viewHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
        System.out.println(viewHeight);
        viewWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        System.out.println(viewWidth);
        leftButtonWidth = viewWidth * 0.3;
        rightButtonWidth = viewWidth * 0.3;
        midButtonsSplit = viewHeight / 2;

        gameView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == motionEvent.ACTION_DOWN) {
                    System.out.println("x: " + motionEvent.getX() + ", y: " + motionEvent.getY());
                    if (motionEvent.getX() <= leftButtonWidth) {
                        System.out.println("Button 1 pressed");
                        handleInput(1);
                    } else if (motionEvent.getX() >= (viewWidth - rightButtonWidth)) {
                        System.out.println("Button 4 pressed");
                        handleInput(4);
                    } else if (motionEvent.getX() > leftButtonWidth && motionEvent.getX() < (viewWidth - rightButtonWidth)) {
                        if (motionEvent.getY() < midButtonsSplit) {
                            System.out.println("Button 2 pressed");
                            handleInput(2);
                        } else if (motionEvent.getY() >= midButtonsSplit) {
                            System.out.println("Button 3 pressed");
                            handleInput(3);
                        } else {
                            System.out.println("ERROR - middle button could not be determined");
                        }
                    } else {
                        System.out.println("ERROR - button could not be determined");
                    }
                }
                return true;
            }
        });

        playerView.setImageDrawable(getResources().getDrawable(R.drawable.player_sprite));
        shieldView.setImageDrawable(getResources().getDrawable(R.drawable.shield1));
        shieldDirection = 1;
        //handleInput(1);
        queueIndex = 0;
        startTime = System.currentTimeMillis();
        playing = true;

        playerRect = new Rect();
        playerView.getHitRect(playerRect);

        shieldRect = new Rect();
        shieldView.getHitRect(shieldRect);

        //enemyQueue.add(2000);
        //enemyQueue.add(2500);

        handler.postDelayed(update, (long)50);

        GameThread gameThread = new GameThread();
        gameThread.execute();
    }

    public void updateBoard() {
        //check if any new bullets need to be spawned
        double tempTime = System.currentTimeMillis();
        /*if (queueIndex<enemyQueue.size() && (tempTime - startTime) >= enemyQueue.get(queueIndex)) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    launchBullet();
                }
            });
            queueIndex++;
        }*/
        if (tempTime - startTime >= 1500) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    launchBullet();
                }
            });
            //queueIndex++;
            startTime = tempTime;
        }

    }

    public void updatePosition(){
        for (int i=0; i<enemyList.size(); i++){
            Bullet b = enemyList.get(i);
            int bulletDirection = b.getDirection();
            if(bulletDirection == 1){
                //b.bulletView.setX(b.bulletView.getX()+b.bulletView.getWidth());
                b.bulletView.setX(b.bulletView.getX()+5);
                Rect tempRect = new Rect();
                b.bulletView.getHitRect(tempRect);
                b.setHitbox(tempRect);
                checkCollision(b);
            } else if(bulletDirection == 2){
                //b.bulletView.setY(b.bulletView.getY()+b.bulletView.getHeight());
                b.bulletView.setY(b.bulletView.getY()+5);
                Rect tempRect = new Rect();
                b.bulletView.getHitRect(tempRect);
                b.setHitbox(tempRect);
                checkCollision(b);
            } else if(bulletDirection == 3){
                //b.bulletView.setY(b.bulletView.getY()-b.bulletView.getHeight());
                b.bulletView.setY(b.bulletView.getY()-5);
                Rect tempRect = new Rect();
                b.bulletView.getHitRect(tempRect);
                b.setHitbox(tempRect);
                checkCollision(b);
            } else if(bulletDirection == 4){
                //b.bulletView.setX(b.bulletView.getX()-b.bulletView.getWidth());
                b.bulletView.setX(b.bulletView.getX()-5);
                Rect tempRect = new Rect();
                b.bulletView.getHitRect(tempRect);
                b.setHitbox(tempRect);
                checkCollision(b);
            }
        }
    }

    public void checkCollision(Bullet b){
        /*if(Rect.intersects(b.getHitbox(), shieldRect)){
            enemyList.remove(b);
            relLayout.removeView(b.bulletView);
            b = null;
        } else if (Rect.intersects(b.getHitbox(), playerRect) && !(Rect.intersects(b.getHitbox(), shieldRect))){
            enemyList.remove(b);
            relLayout.removeView(b.bulletView);
            b = null;
            health--;
            if(health <= 0){
                Intent intent = new Intent(this, GameOver.class);
                startActivity(intent);
            }
        }*/
        if(Rect.intersects(b.getHitbox(), shieldRect)){
            if(b.getDirection() == shieldDirection) {
                enemyList.remove(b);
                relLayout.removeView(b.bulletView);
                b = null;
            } else {
                enemyList.remove(b);
                relLayout.removeView(b.bulletView);
                b = null;
                health--;
                if(health <= 0){
                    Intent intent = new Intent(this, GameOver.class);
                    startActivity(intent);
                }
            }
        }
    }

    public void launchBullet(){
        Random rand = new Random();
        int direction = rand.nextInt(4)+1;
        //int direction = 4;
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        if(direction == 1){
            Bullet bullet = new Bullet(getResources().getDrawable(R.drawable.enemy_sprite_horiz), direction, gameSpeed, this, viewWidth, viewHeight, relLayout);
            enemyList.add(bullet);
            relLayout.addView(bullet.bulletView, layoutParams);
            System.out.println("bullet launched");
            System.out.println("bullet direction: " + bullet.getDirection());
        } else if(direction == 2){
            Bullet bullet = new Bullet(getResources().getDrawable(R.drawable.enemy_sprite_vert), direction, gameSpeed, this, viewWidth, viewHeight, relLayout);
            enemyList.add(bullet);
            relLayout.addView(bullet.bulletView, layoutParams);
            System.out.println("bullet launched");
            System.out.println("bullet direction: " + bullet.getDirection());
        } else if(direction == 3){
            Bullet bullet = new Bullet(getResources().getDrawable(R.drawable.enemy_sprite_vert), direction, gameSpeed, this, viewWidth, viewHeight, relLayout);
            enemyList.add(bullet);
            relLayout.addView(bullet.bulletView, layoutParams);
            System.out.println("bullet launched");
            System.out.println("bullet direction: " + bullet.getDirection());
        } else if(direction == 4){
            Bullet bullet = new Bullet(getResources().getDrawable(R.drawable.enemy_sprite_horiz), direction, gameSpeed, this, viewWidth,  viewHeight, relLayout);
            enemyList.add(bullet);
            relLayout.addView(bullet.bulletView, layoutParams);
            System.out.println("bullet launched");
            System.out.println("bullet direction: " + bullet.getDirection());
        }

    }

    public void handleInput(int buttonNum){
        if (buttonNum==1){
            shieldView.setImageDrawable(getResources().getDrawable(R.drawable.shield1));
            shieldDirection = 1;
            shieldView.getHitRect(shieldRect);
        } else if(buttonNum==2){
            shieldView.setImageDrawable(getResources().getDrawable(R.drawable.shield2));
            shieldDirection = 2;
            shieldView.getHitRect(shieldRect);
        } else if(buttonNum==3){
            shieldView.setImageDrawable(getResources().getDrawable(R.drawable.shield3));
            shieldDirection = 3;
            shieldView.getHitRect(shieldRect);
        } else {
            shieldView.setImageDrawable(getResources().getDrawable(R.drawable.shield4));
            shieldDirection = 4;
            shieldView.getHitRect(shieldRect);
        }
    }

    public static Activity getInstance(){
        return gameActivity;
    }

    private class GameThread extends AsyncTask<Void, Void, Void> {
        protected Void doInBackground(Void... values) {
            while(playing){
                updateBoard();
            }
            return null;
        }
        protected void onProgressUpdate(Void... progress) {}
        protected void onPostExecute(Void result) {}
    }

    private class Update implements Runnable{
        private View view;

        @Override
        public void run() {
            updatePosition();
            System.out.println("update has run");
            handler.postDelayed(update, (long) 50);
        }
    }
}
