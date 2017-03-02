package com.defaultapps.newsreader.ui.activity;

import android.app.Application;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.defaultapps.newsreader.App;
import com.defaultapps.newsreader.R;
import com.defaultapps.newsreader.data.local.sp.SharedPreferencesManager;
import com.defaultapps.newsreader.ui.fragment.MainViewImpl;
import com.defaultapps.newsreader.ui.fragment.SetUpViewImpl;


import javax.inject.Inject;

public class MainActivity extends AppCompatActivity implements SetUpViewImpl.SourcesListener {
    
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

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.contentFrame, fragment)
                .commit();
    }

    @Override
    public void sourceClicked() {
        MainViewImpl mainFragment = new MainViewImpl();
        replaceFragment(mainFragment);
    }
}
