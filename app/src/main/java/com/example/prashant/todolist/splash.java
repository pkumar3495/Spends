package com.example.prashant.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

public class splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        final Button startButton = (Button) findViewById(R.id.start_button);

        final ImageView splashImage = (ImageView) findViewById(R.id.splashImange);
        Animation fade1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        final Animation fade2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        final Animation stepUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.step_up);
        final Animation buttonStepUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.button_step_up);
        final Animation holdFade = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.hold_fade_off);
        final Animation fastFadeIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fast_fade_in);
        splashImage.setAnimation(fade1);

        fade1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                startButton.setAnimation(holdFade);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                splashImage.setAnimation(stepUp);
                stepUp.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        startButton.setAnimation(buttonStepUp);
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
//                        startButton.setAnimation(fade2);
                        startButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                finish();
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                            }
                        });
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }
}
