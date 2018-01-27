package info.androidhive.uplus;

import android.Manifest;
import android.graphics.Color;
import android.os.Bundle;

import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntroFragment;

import info.androidhive.uplus.fragments.OneFragment;

import static info.androidhive.uplus.R.id.image;

/**
 * Created by RwandaFab on 8/10/2017.
 */

public class MyIntro extends AppIntro2 {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Add your slide's fragments here
        // AppIntro will automatically generate the dots indicator and buttons.


        // Instead of fragments, you can also use our default slide
        // Just set a title, description, background and image. AppIntro will do the rest
        addSlide(AppIntroFragment.newInstance("Uplus Mutual Partner", "Contribute ,share ,and Send Money Instantly Using only your android device", R.drawable.facebook_avatar, Integer.parseInt("#00796b")));

        // OPTIONAL METHODS

        // SHOW or HIDE the statusbar
        showStatusBar(true);

        // Edit the color of the nav bar on Lollipop+ devices

        // Turn vibration on and set intensity
        // NOTE: you will need to ask VIBRATE permission in Manifest if you haven't already

        // Animations -- use only one of the below. Using both could cause errors.
        setFadeAnimation(); // OR

    }

    @Override
    public void onNextPressed() {
        // Do something when users tap on Next button.
    }

    @Override
    public void onDonePressed() {
        // Do something when users tap on Done button.
        finish();
    }

    @Override
    public void onSlideChanged() {
        // Do something when slide is changed
    }
}
