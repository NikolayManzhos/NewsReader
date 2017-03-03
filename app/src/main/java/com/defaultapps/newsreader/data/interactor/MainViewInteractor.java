package com.defaultapps.newsreader.data.interactor;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.defaultapps.newsreader.R;
import com.defaultapps.newsreader.data.entity.articles.Article;
import com.defaultapps.newsreader.data.entity.articles.ArticlesResponse;
import com.defaultapps.newsreader.data.local.LocalService;
import com.defaultapps.newsreader.data.local.sp.SharedPreferencesManager;
import com.defaultapps.newsreader.data.net.NetworkService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Response;

public class MainViewInteractor {

    private Context context; //Application context
    private NetworkService networkService;
    private LocalService localService;
    private SharedPreferencesManager sharedPreferencesManager;
    private MainViewInteractorCallback callback;
    private boolean responseStatus = false;

    private List<String> articlesTitle, articlesImageUrl, articlesDescription, articlesDirectUrl;

    private AsyncTask<Void, Void, Void> downloadArticlesTask;
    private AsyncTask<Void, Void, Void> loadFromCacheTask;

    private final long CACHE_EXP_TIME = 86400000;


    public interface MainViewInteractorCallback {
        void onSuccess(List<String> articlesTitle, List<String> articlesDescription, List<String> articlesImageUrl, List<String> articlesDirectUrl);
        void onFailure();
    }

    @Inject
    public MainViewInteractor(NetworkService networkService,
                              LocalService localService,
                              SharedPreferencesManager sharedPreferencesManager,
                              Context context) {
        this.context = context;
        this.networkService = networkService;
        this.localService = localService;
        this.sharedPreferencesManager = sharedPreferencesManager;
    }

    public void bindInteractor(MainViewInteractorCallback mainViewInteractorCallback) {
        this.callback = mainViewInteractorCallback;
    }

    public void loadArticlesData() {
        downloadArticlesTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    Response<ArticlesResponse> response = networkService
                            .getNetworkCall()
                            .getArticles(sharedPreferencesManager.getSource(), sharedPreferencesManager.getSort(), context.getString(R.string.NEWS_API_KEY))
                            .execute();
                    if (response.isSuccessful()) {
                        parseData(response.body());
                        sharedPreferencesManager.setCacheTime(System.currentTimeMillis());
                        localService.writeResponseToFile(response.body());
                        responseStatus = true;
                    } else {
                        responseStatus = false;
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                    responseStatus = false;
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                if (callback != null) {
                    if (responseStatus) {
                        callback.onSuccess(articlesTitle, articlesDescription, articlesImageUrl, articlesDirectUrl);
                    } else {
                        callback.onFailure();
                    }
                }
                super.onPostExecute(aVoid);
            }
        }.execute();
    }

    public void loadDataFromCache() {
        if (localService.isCacheAvailable()
                && sharedPreferencesManager.getCacheTime() != 0
                && (System.currentTimeMillis() - sharedPreferencesManager.getCacheTime()) < CACHE_EXP_TIME ) {
            loadFromCacheTask = new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    try {
                        ArticlesResponse data = localService.readResponseFromFile();
                        parseData(data);
                        responseStatus = true;
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        Log.d("AsyncTaskLocal", "FAILED TO READ DATA");
                        responseStatus = false;
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    if (callback != null) {
                        if (responseStatus) {
                            Log.d("AsyncTaskLocal", "SUCCESS");
                            callback.onSuccess(articlesTitle, articlesDescription, articlesImageUrl, articlesDirectUrl);
                        } else {
                            Log.d("AsyncTaskLocal", "FAILURE");
                            callback.onFailure();
                        }
                    }
                }
            };
            loadFromCacheTask.execute();

        } else {
            localService.deleteCache();
            loadArticlesData();
        }
    }

    private void parseData(ArticlesResponse articles) {
        articlesTitle = new ArrayList<>();
        articlesDescription = new ArrayList<>();
        articlesImageUrl = new ArrayList<>();
        articlesDirectUrl = new ArrayList<>();
        for (Article article : articles.getArticles()) {
            articlesTitle.add(article.getTitle());
            articlesDescription.add(article.getDescription());
            articlesImageUrl.add(article.getUrlToImage());
            articlesDirectUrl.add(article.getUrl());
        }
    }
}
