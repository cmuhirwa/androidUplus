package info.androidhive.uplus;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import gr.net.maroulis.library.EasySplashScreen;
import info.androidhive.uplus.activity.HomeActivity;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EasySplashScreen config = new EasySplashScreen(SplashScreen.this)
                .withFullScreen()
                .withTargetActivity(HomeActivity.class)
                .withSplashTimeOut(1000)
                .withBackgroundColor(Color.parseColor("#333540"))
                .withLogo(R.drawable.splash)
                //.withHeaderText("Welcome to Uplus")
                .withFooterText("Copyright 2017")
                //.withBeforeLogoText("UPLUS")
                .withAfterLogoText("You plus Me = Wealth");

        //config.getHeaderTextView().setTextColor(android.graphics.Color.parseColor("#f1592a"));
        config.getFooterTextView().setTextColor(android.graphics.Color.parseColor("#ffffff"));
        config.getAfterLogoTextView().setTextColor(android.graphics.Color.parseColor("#ffffff"));
        //config.getBeforeLogoTextView().setTextColor(android.graphics.Color.parseColor("#ffffff"));

        View view = config.create();

        setContentView(view);
    }
}
