package com.defaultapps.newsreader.data.interactor;


import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.defaultapps.newsreader.data.entity.sources.Source;
import com.defaultapps.newsreader.data.entity.sources.SourcesResponse;
import com.defaultapps.newsreader.data.local.LocalService;
import com.defaultapps.newsreader.data.local.sp.SharedPreferencesManager;
import com.defaultapps.newsreader.data.net.NetworkService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Response;

public class SetUpViewInteractor {

    private AsyncTask<Void, Void, Void> downloadFromNetTask;
    private AsyncTask<Void, Void, Void> loadFromCacheTask;
    private NetworkService networkService;
    private LocalService localService;
    private SharedPreferencesManager sharedPreferencesManager;
    private MainViewInteractorCallback callback;
    private boolean responseStatus;

    private SourcesResponse data;
    private List<String> sourcesName;
    private List<String> sourcesUrl;
    private List<String> sourcesDescription;


    public interface MainViewInteractorCallback {
        void onSuccess(List<String> sourcesName, List<String> sourcesDescription, List<String> sourcesUrl);
        void onFailure();
    }


    @Inject
    public SetUpViewInteractor(NetworkService networkService, LocalService localService, SharedPreferencesManager sharedPreferencesManager) {
        this.networkService = networkService;
        this.localService = localService;
        this.sharedPreferencesManager = sharedPreferencesManager;
    }

    public void bindInteractor(MainViewInteractorCallback callback) {
        this.callback = callback;
    }


    public void loadSourcesData(final String languageCode) {
        downloadFromNetTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    Response<SourcesResponse> response = networkService.getNetworkCall().getSources(languageCode).execute();
                    data = response.body();
//                      localService.writeResponseToFile(data);
                    if (response.isSuccessful()) {
                        parseData(data);
                        sharedPreferencesManager.setLanguageCode(languageCode);
                        responseStatus = true;
                    } else {
                        responseStatus = false;
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                    Log.d("AsyncTask", "FAILED TO WRITE DATA");
                    responseStatus = false;
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if (callback != null) {
                    if (responseStatus) {
                        Log.d("AsyncTaskNet", "SUCCESS");
                        callback.onSuccess(sourcesName, sourcesDescription, sourcesUrl);
                        sourcesUrl = null;
                        sourcesDescription = null;
                    } else {
                        Log.d("AsyncTaskNet", "FAILURE");
                        callback.onFailure();
                    }
                }
            }
        };
        downloadFromNetTask.execute();
    }

//    public void loadDataFromCache() {
//        loadFromCacheTask = new AsyncTask<Void, Void, Void>() {
//            @Override
//            protected void onPreExecute() {
//                super.onPreExecute();
//            }
//
//            @Override
//            protected Void doInBackground(Void... params) {
//                try {
//                    data = localService.readResponseFromFile();
//                    parseData(data);
//                    responseStatus = true;
//                } catch (IOException ex) {
//                    ex.printStackTrace();
//                    Log.d("AsyncTaskLocal", "FAILED TO READ DATA");
//                    responseStatus = false;
//                }
//                return null;
//            }
//
//            @Override
//            protected void onPostExecute(Void aVoid) {
//                super.onPostExecute(aVoid);
//                if (callback != null) {
//                    if (responseStatus) {
//                        Log.d("AsyncTaskLocal", "SUCCESS");
//                        callback.onSuccess(photosUrl, photosTitle);
//                        photosUrl = null;
//                        photosTitle = null;
//                    } else {
//                        Log.d("AsyncTaskLocal", "FAILURE");
//                        callback.onFailure();
//                    }
//                }
//            }
//        };
//        if (!loadFromCacheTask.getStatus().equals(AsyncTask.Status.RUNNING)) {
//            loadFromCacheTask.execute();
//        }
//    }

    private void parseData(SourcesResponse dataToParse) {
        sourcesName = new ArrayList<>();
        sourcesUrl = new ArrayList<>();
        sourcesDescription = new ArrayList<>();
        for (Source source : dataToParse.getSources() ) {
            sourcesName.add(source.getName());
            sourcesUrl.add(source.getUrlsToLogos().getMedium());
            sourcesDescription.add(source.getDescription());
        }
    }
}
