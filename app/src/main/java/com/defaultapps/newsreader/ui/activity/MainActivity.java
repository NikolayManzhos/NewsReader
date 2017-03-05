package com.defaultapps.newsreader.ui.activity;

import android.app.Application;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.defaultapps.newsreader.App;
import com.defaultapps.newsreader.R;
import com.defaultapps.newsreader.data.local.sp.SharedPreferencesManager;
import com.defaultapps.newsreader.ui.fragment.MainViewImpl;
import com.defaultapps.newsreader.ui.fragment.SetUpViewImpl;


import javax.inject.Inject;

public class MainActivity extends AppCompatActivity implements SetUpViewImpl.SourcesListener, MainViewImpl.MainViewListener {
    
    @Inject
    Application application;

    @Inject
    SharedPreferencesManager sharedPreferencesManager;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getAppComponent(this).inject(this);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            if (sharedPreferencesManager.getSource() == null) {
                replaceFragment(new SetUpViewImpl());
            } else {
                replaceFragment(new MainViewImpl());
            }
        }
    }

    @Override
    public void sourceClicked() {
        MainViewImpl mainFragment = new MainViewImpl();
        sharedPreferencesManager.setForceLoadStatus(true);
        replaceFragment(mainFragment);
    }

    @Override
    public void settingsClicked() {
        replaceFragment(new SetUpViewImpl());
    }

    @Override
    public void openWebView(String url) {
        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra("URL", url);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 1){
            finish();
        } else {
            super.onBackPressed();
        }
    }

    private void replaceFragment (Fragment fragment){
        String backStateName =  fragment.getClass().getName();

        FragmentManager manager = getSupportFragmentManager();
        boolean fragmentPopped = manager.popBackStackImmediate(backStateName, 0);

        if (!fragmentPopped && manager.findFragmentByTag(backStateName) == null) {
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.contentFrame, fragment, backStateName);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.addToBackStack(backStateName);
            ft.commit();
        }
    }
}
