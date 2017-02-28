package com.defaultapps.newsreader.data.interactor;

import android.content.Context;
import android.os.AsyncTask;

import com.defaultapps.newsreader.R;
import com.defaultapps.newsreader.data.entity.articles.ArticlesResponse;
import com.defaultapps.newsreader.data.local.LocalService;
import com.defaultapps.newsreader.data.local.sp.SharedPreferencesManager;
import com.defaultapps.newsreader.data.net.NetworkService;

import java.io.IOException;

import javax.inject.Inject;

import retrofit2.Response;

public class MainViewInteractor {

    private Context context; //Application context
    private NetworkService networkService;
    private LocalService localService;
    private SharedPreferencesManager sharedPreferencesManager;
    private MainViewInteractorCallback callback;
    private boolean responseStatus = false;

    private AsyncTask<Void, Void, Void> downloadArticlesTask;


    public interface MainViewInteractorCallback {
        void onSuccess();
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

    public void loadArtcilesData() {
        downloadArticlesTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    Response<ArticlesResponse> response = networkService
                            .getNetworkCall()
                            .getArticles(sharedPreferencesManager.getSource(), sharedPreferencesManager.getSort(), context.getString(R.string.NEWS_API_KEY))
                            .execute();
                    responseStatus = true;
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
                        callback.onSuccess();
                    } else {
                        callback.onFailure();
                    }
                }
                super.onPostExecute(aVoid);
            }
        }.execute();
    }
}
