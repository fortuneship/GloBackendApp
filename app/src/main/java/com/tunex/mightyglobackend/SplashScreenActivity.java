package com.tunex.mightyglobackend;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashScreenActivity extends AppCompatActivity {



    private TextView textView;
    private ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        textView = (TextView) findViewById(R.id.txt);
        imageView = (ImageView) findViewById(R.id.imageView);

        Animation myAnimation = AnimationUtils.loadAnimation(this,R.anim.mytransition);
        textView.startAnimation(myAnimation);
        imageView.startAnimation(myAnimation);

        final Intent i = new Intent(this, MainActivity.class);

        Thread timer = new Thread(){

            public void run(){

                try {
                    sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {

                    startActivity(i);
                    finish();
                }
            }
        };
        timer.start();

    }
}
