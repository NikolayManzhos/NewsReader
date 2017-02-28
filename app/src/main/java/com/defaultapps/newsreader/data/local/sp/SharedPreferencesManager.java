package com.defaultapps.newsreader.data.local.sp;

import javax.inject.Inject;
import javax.inject.Singleton;


@Singleton
public class SharedPreferencesManager {

    private SharedPreferencesHelper sharedPreferencesHelper;

    private final String CACHE_TIME = "cache_exp_time";
    private final String SOURCE_CODE = "source_code";
    private final String SORT_ORDER = "sort_order";

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

    public String getSource() {
        return sharedPreferencesHelper.getString(SOURCE_CODE);
    }

    public void setSource(String sourceCode) {
        sharedPreferencesHelper.putString(SOURCE_CODE, sourceCode);
    }

    public String getSort() {
        return sharedPreferencesHelper.getString(SORT_ORDER);
    }

    public void setSort(String sortOrder) {
        sharedPreferencesHelper.putString(SORT_ORDER, sortOrder);
    }




}
