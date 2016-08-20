package com.example.tim.thefarzaapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

/**
 * Created by Tim on 5/28/2016.
 */
public class Splashy extends Activity {

    private static int SPLASH_SCREEN_TIME = 3000;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashy_activity);


        new Handler().postDelayed(new Runnable() {


            @Override
            public void run() {

                Intent mainScreen = new Intent(Splashy.this, LaunchSpotifyLoginActivity.class);
                startActivity(mainScreen);


                finish();
            }

        }, SPLASH_SCREEN_TIME);

    }
}
