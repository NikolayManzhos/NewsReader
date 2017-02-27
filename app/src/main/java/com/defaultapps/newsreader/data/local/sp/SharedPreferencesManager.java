package com.defaultapps.newsreader.data.local.sp;

import javax.inject.Inject;
import javax.inject.Singleton;


@Singleton
public class SharedPreferencesManager {

    private SharedPreferencesHelper sharedPreferencesHelper;

    private final String CACHE_TIME = "cache_exp_time";
    private final String LANGUAGE_CODE = "language_code";

    @Inject
    public SharedPreferencesManager(SharedPreferencesHelper sharedPreferencesHelper) {
        this.sharedPreferencesHelper = sharedPreferencesHelper;
    }

    public long getCacheTime() {
        return sharedPreferencesHelper.getLong(CACHE_TIME);
    }

    public void setCacheTime(long currentTime) {
        sharedPreferencesHelper.putLong(CACHE_TIME, currentTime);
    }

    public String getLanguageCode() {
        return sharedPreferencesHelper.getString(LANGUAGE_CODE);
    }

    public void setLanguageCode(String languageCode) {
        sharedPreferencesHelper.putString(LANGUAGE_CODE, languageCode);
    }




}
