package com.example.abhayaradhya.finalproject;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

public class GameOver extends AppCompatActivity {

    private static Activity gameOverActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_game_over);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        Game1.getInstance().finish();

        gameOverActivity = this;
    }

    @Override
    public void onBackPressed(){}

    public void restartButton(View v){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public static Activity getInstance(){
        return gameOverActivity;
    }
}
