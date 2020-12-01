package com.itbam.pixceler.util;

import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.itbam.pixceler.view.activity.MainActivity;


public class FullScreen extends MainActivity {

    private final AppCompatActivity mainActivity;

    public FullScreen(AppCompatActivity mainActivity) {
        this.mainActivity = mainActivity;

    }

   @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    private void hideSystemUI() {
        View decorView = mainActivity.getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE

                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }
}
