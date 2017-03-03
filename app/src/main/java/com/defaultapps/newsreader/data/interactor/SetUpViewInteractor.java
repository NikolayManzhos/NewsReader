package com.defaultapps.newsreader.data.interactor;


import android.os.AsyncTask;
import android.util.Log;

import com.defaultapps.newsreader.data.entity.sources.Source;
import com.defaultapps.newsreader.data.entity.sources.SourcesResponse;

import com.defaultapps.newsreader.data.net.NetworkService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Response;

public class SetUpViewInteractor {

    private AsyncTask<Void, Void, Void> downloadFromNetTask;
    private NetworkService networkService;
    private SetUpInteractorCallback callback;
    private boolean responseStatus;

    private SourcesResponse data;
    private List<String> sourcesName;
    private List<String> sourcesUrl;
    private List<String> sourcesDescription;
    private List<String> sourcesId;
    private List<List<String>> sourcesSortAvailable;

    public interface SetUpInteractorCallback {
        void onSuccess(List<String> sourcesName,
                       List<String> sourcesDescription,
                       List<String> sourcesUrl,
                       List<String> sourcesId,
                       List<List<String>> sourcesSortAvailable);
        void onFailure();
    }

    @Inject
    public SetUpViewInteractor(NetworkService networkService) {
        this.networkService = networkService;
    }

    public void bindInteractor(SetUpInteractorCallback callback) {
        this.callback = callback;
    }


    public void loadSourcesData(final String languageCode) {
        downloadFromNetTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    Response<SourcesResponse> response = networkService.getNetworkCall().getSources(languageCode).execute();
                    data = response.body();
                    if (response.isSuccessful()) {
                        parseData(data);
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
                super.onPostExecute(aVoid);
                if (callback != null) {
                    if (responseStatus) {
                        callback.onSuccess(sourcesName,
                                sourcesDescription,
                                sourcesUrl,
                                sourcesId,
                                sourcesSortAvailable);
                    } else {
                        callback.onFailure();
                    }
                }
            }
        };
        downloadFromNetTask.execute();
    }

    private void parseData(SourcesResponse dataToParse) {
        sourcesName = new ArrayList<>();
        sourcesUrl = new ArrayList<>();
        sourcesDescription = new ArrayList<>();
        sourcesId = new ArrayList<>();
        sourcesSortAvailable = new ArrayList<>();
        for (Source source : dataToParse.getSources() ) {
            sourcesName.add(source.getName());
            sourcesUrl.add(source.getUrlsToLogos().getMedium());
            sourcesDescription.add(source.getDescription());
            sourcesId.add(source.getId());
            sourcesSortAvailable.add(source.getSortBysAvailable());
        }
    }
}
