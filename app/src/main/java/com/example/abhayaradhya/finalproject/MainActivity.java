package com.example.abhayaradhya.finalproject;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;


public class MainActivity extends AppCompatActivity {

    private static Activity mainMenuActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        mainMenuActivity = this;
    }

    public static Activity getInstance(){
        return mainMenuActivity;
    }

    public void launchGame1(View v){
        Intent intent = new Intent(this, Game1.class);
        startActivity(intent);
    }

    public void showRules(View v){
        Intent intent = new Intent(this, Rules.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed(){}
}
