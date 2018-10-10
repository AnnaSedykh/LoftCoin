package com.annasedykh.loftcoin.screens.start;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.annasedykh.loftcoin.App;
import com.annasedykh.loftcoin.R;
import com.annasedykh.loftcoin.data.api.Api;
import com.annasedykh.loftcoin.data.prefs.Prefs;
import com.annasedykh.loftcoin.screens.main.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StartActivity extends AppCompatActivity implements StartView {

    public static void startInNewTask(Context context) {
        Intent starter = new Intent(context, StartActivity.class);
        starter.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(starter);
    }

    @BindView(R.id.start_top_corner)
    ImageView topCorner;

    @BindView(R.id.start_bottom_corner)
    ImageView bottomCorner;


    private StartPresenter presenter;
    private Api api;
    private Prefs prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        ButterKnife.bind(this);

        api = ((App) getApplication()).getApi();
        prefs = ((App) getApplication()).getPrefs();

        presenter = new StartPresenterImpl(api, prefs);
        presenter.attachView(this);
        presenter.loadRate();
    }

    @Override
    protected void onStart() {
        super.onStart();

        startAnimations();
    }

    private void startAnimations() {

        ObjectAnimator innerAnimator = ObjectAnimator.ofFloat(topCorner, "rotation", 0, 360);
        innerAnimator.setDuration(30000);
        innerAnimator.setRepeatMode(ValueAnimator.RESTART);
        innerAnimator.setRepeatCount(ValueAnimator.INFINITE);
        innerAnimator.setInterpolator(new LinearInterpolator());

        ObjectAnimator outerAnimator = ObjectAnimator.ofFloat(bottomCorner, "rotation", 0, -360);
        outerAnimator.setDuration(60000);
        outerAnimator.setRepeatMode(ValueAnimator.RESTART);
        outerAnimator.setRepeatCount(ValueAnimator.INFINITE);
        outerAnimator.setInterpolator(new LinearInterpolator());

        AnimatorSet set = new AnimatorSet();
        set.play(innerAnimator).with(outerAnimator);
        set.start();

    }

    @Override
    protected void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }

    @Override
    public void navigateToMainScreen() {
        MainActivity.startInNewTask(this);
    }
}
