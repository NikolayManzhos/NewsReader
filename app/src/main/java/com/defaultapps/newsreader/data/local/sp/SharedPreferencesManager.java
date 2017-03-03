package com.defaultapps.newsreader.data.local.sp;

import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;


@Singleton
public class SharedPreferencesManager {

    private SharedPreferencesHelper sharedPreferencesHelper;

    private final String CACHE_TIME = "cache_exp_time";
    private final String SOURCE_CODE = "source_code";
    private final String SORT_ORDER = "sort_order";
    private final String SORTING_AVAILABLE = "sorting_available";
    private final String FORCE_LOAD = "force_load";

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

    public boolean getForceLoadStatus() {
        return sharedPreferencesHelper.getBoolean(FORCE_LOAD);
    }

    public void setForceLoadStatus(boolean loadStatus) {
        sharedPreferencesHelper.putBoolean(FORCE_LOAD, loadStatus);
    }

    public Set<String> getSortingAvailable() {
        return sharedPreferencesHelper.getStringSet(SORTING_AVAILABLE);
    }

    public void setSortingAvailable(Set<String> sorting) {
        sharedPreferencesHelper.putStringSet(SORTING_AVAILABLE, sorting);
    }




}
