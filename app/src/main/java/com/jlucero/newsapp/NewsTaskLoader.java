package com.jlucero.newsapp;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.util.List;

public class NewsTaskLoader extends AsyncTaskLoader<List<News>> {

    private static String mURL;

    public NewsTaskLoader(Context context, String url) {
        super(context);
        mURL = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<News> loadInBackground() {
        if (mURL == null) {
            return null;
        }

        List<News> newsList = NewsUtils.getNewsDataFromServer(mURL);
        return newsList;
    }
}