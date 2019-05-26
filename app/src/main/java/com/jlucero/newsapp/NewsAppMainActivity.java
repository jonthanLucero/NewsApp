package com.jlucero.newsapp;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;


public class NewsAppMainActivity extends AppCompatActivity implements LoaderCallbacks<List<News>>
{
    //ID OF THE NEWS ASYNCTASKLOADER
    public static final int NEWS_LOADER_ID = 1020;

    //URL to make the request a get the information of the news
    public static final String GUARDIAN_URL = "http://content.guardianapis.com/search";


    ListView     mListView;
    LinearLayout contentNewsListLinearLayout;
    LinearLayout loadingNewsLinearLayout;
    TextView      mContentLayoutText;
    NewsAdapter mAdapter;
    LoaderManager loaderManager;
    int mNewsCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_app_main);

        mListView = findViewById(R.id.newsList);
        contentNewsListLinearLayout       = findViewById(R.id.news_information);
        loadingNewsLinearLayout           = findViewById(R.id.loading_news);

        contentNewsListLinearLayout.setVisibility(View.GONE);
        loadingNewsLinearLayout.setVisibility(View.GONE);

        mContentLayoutText  =  contentNewsListLinearLayout.findViewById(R.id.layoutTitle);
        Button updateNews = contentNewsListLinearLayout.findViewById(R.id.action_download_news);

        //Check Internet Connection
        if(checkInternetConnectivity())
        {
            loaderManager = getSupportLoaderManager();
            loaderManager.initLoader(NEWS_LOADER_ID, null, this);
        }
        //If there is no internet connection then show no internet connection message
        else
        {
            mContentLayoutText.setText(R.string.no_internet_Connection);
            contentNewsListLinearLayout.setVisibility(View.VISIBLE);
        }

        updateNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reloadNews();
            }
        });
    }

    @Override
    public Loader<List<News>> onCreateLoader(int id, Bundle args)
    {
        loadingNewsLinearLayout.setVisibility(View.VISIBLE);
        Log.d("LOG","NEWS Se llama oncreateloader");
        return new NewsTaskLoader(this,convertURLToUri());
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> data)
    {
        loadingNewsLinearLayout.setVisibility(View.GONE);
        contentNewsListLinearLayout.setVisibility(View.GONE);
        mNewsCount = data.size();
        Log.d("LOG","NEWS Se termino el proceso "+data);
        if(mNewsCount > 0)
        {
            mAdapter = new NewsAdapter(this, data);
            mListView.setAdapter(mAdapter);
            setListViewClick();
        }
        else {
            mContentLayoutText.setText(R.string.not_found_info);
            contentNewsListLinearLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        mAdapter.clear();
    }

    public void reloadNews()
    {
        if(checkInternetConnectivity())
        {
            Log.d("LOG","NEWS Se refresca las noticias");
            loaderManager = getSupportLoaderManager();
            loaderManager.restartLoader(NEWS_LOADER_ID, null, this);
        }
        else
        {
            mContentLayoutText.setText(R.string.no_internet_Connection);
            contentNewsListLinearLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_news_app, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if(itemId == R.id.update_news){
            reloadNews();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean checkInternetConnectivity()
    {
        boolean status = false;
        ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected())
            status = true;
        return  status;
    }

    public void setListViewClick()
    {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                News currentNews = mAdapter.getItem(position);
                Uri newsUri = Uri.parse(currentNews.getPublicationURL());
                Intent webIntent = new Intent(Intent.ACTION_VIEW, newsUri);
                startActivity(webIntent);
            }
        });
    }

    public String convertURLToUri()
    {
        String URI;
        Uri baseUri = Uri.parse(GUARDIAN_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("show-tags", "contributor");
        uriBuilder.appendQueryParameter("q", "USA");
        uriBuilder.appendQueryParameter("api-key", "test");
        URI = uriBuilder.toString();
        return URI;
    }
}
