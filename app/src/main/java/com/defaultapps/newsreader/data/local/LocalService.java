package com.defaultapps.newsreader.data.local;


import android.content.Context;

import com.defaultapps.newsreader.data.entity.articles.ArticlesResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;

import javax.inject.Inject;

public class LocalService {

    private final String FILE_NAME = "response.cache";
    private final Gson gson = new Gson();
    private final File file;

    @Inject
    public LocalService(Context context) {
        file = new File(context.getCacheDir(), FILE_NAME);
    }

    /**
     * Writes response from api to file.
     * Do not execute on the UI thread!
     * @param response contains multiple articles info.
     */
    public void writeResponseToFile(ArticlesResponse response) throws IOException {
        BufferedOutputStream bs = new BufferedOutputStream(new FileOutputStream(file));
        bs.write(gson.toJson(response).getBytes());
        bs.flush();
        bs.close();
    }

    /**
     * Read response from file.
     * Do not execute on the UI thread!
     */
    public ArticlesResponse readResponseFromFile() throws IOException {
        Type articlesResponseType = new TypeToken<ArticlesResponse>(){}.getType();
        String line = "";
        StringBuilder jsonString = new StringBuilder();

        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        while ((line = bufferedReader.readLine()) != null) {
            jsonString.append(line);
        }
        return gson.fromJson(jsonString.toString(), articlesResponseType);
    }

    public void deleteCache() {
        if (isCacheAvailable()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    file.delete();
                }
            }).run();
        }
    }

    public boolean isCacheAvailable() {
        return file.exists();
    }
}
