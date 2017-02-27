package com.defaultapps.newsreader.data.net;

import com.defaultapps.newsreader.data.entity.articles.ArticlesResponse;
import com.defaultapps.newsreader.data.entity.sources.SourcesResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Api {

    @GET("sources")
    Call<SourcesResponse> getSources(
            @Query("language") String languageCode);
}
