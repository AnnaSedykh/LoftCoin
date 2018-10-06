package com.annasedykh.loftcoin.screens.launch;

import android.app.Activity;
import android.os.Bundle;

import com.annasedykh.loftcoin.App;
import com.annasedykh.loftcoin.data.prefs.Prefs;
import com.annasedykh.loftcoin.screens.start.StartActivity;
import com.annasedykh.loftcoin.screens.welcome.WelcomeActivity;

public class LaunchActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Prefs prefs = ((App) getApplication()).getPrefs();

        if (prefs.isFirstLaunch()) {
            WelcomeActivity.startInNewTask(this);
        } else {
            StartActivity.startInNewTask(this);
        }
    }
}
