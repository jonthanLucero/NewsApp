package com.jlucero.newsapp;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Andrés on 5/25/19.
 */

public class NewsUtils
{

    private static final String TAG =  NewsUtils.class.getSimpleName();

    //JSON TAGS
    private static final String SECTION_NAME            = "sectionName";
    private static final String PUBLICATION_DATE        = "webPublicationDate";
    private static final String PUBLICATION_TITLE       = "webTitle";
    private static final String PUBLICATION_URL         = "webUrl";
    private static final String TAGS                    = "tags";



    public static String getNewsDateTime(String value)
    {
        String dateTimeString="";
        //In Simple Format Value
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
        inputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        //DateTime String response format
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date;
        try
        {
            date = inputFormat.parse(value);
            String formattedDate = outputFormat.format(date);
            dateTimeString = formattedDate;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return  dateTimeString;
    }

    public static List<News> getNewsDataFromServer(String urlString)
    {
        List<News> newsListGotten;

        Log.d(TAG, "NEWS urlString: "+urlString);

        if(urlString == null)
            return null;

        URL url = convertStringToURL(urlString);
        Log.d(TAG, "NEWS url converted: "+url);

        String jsonResponse = requestToServer(url);
        Log.d(TAG, "NEWS response: "+jsonResponse);

        newsListGotten = extractDataFromJson(jsonResponse);
        return newsListGotten;
    }

    private static URL convertStringToURL(String url)
    {
        URL urlGotten = null;
        if(url == null)
            return null;

        try{
            urlGotten = new URL(url);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return urlGotten;
    }

    private static String requestToServer(URL url) {
        HttpURLConnection urlConnection;
        String jsonResponse = "";
        InputStream inputStream;

        if(url == null) {
            return null;
        }

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if(urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.d(TAG, "NEWS makeHttpRequest: "+jsonResponse);
        return jsonResponse;
    }

    private static String readFromStream (InputStream inputStream) {
        InputStreamReader streamReader;
        StringBuilder result = new StringBuilder() ;
        BufferedReader bufferedReader;

        streamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
        bufferedReader = new BufferedReader(streamReader);

        try {
            String line = bufferedReader.readLine();
            while (line != null) {
                result.append(line);
                line = bufferedReader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result.toString();
    }

    private static List<News> extractDataFromJson (String jsonResponse) {

        List<News> newsList = new ArrayList<>();
        try {

            JSONObject json = new JSONObject(jsonResponse);
            JSONObject response = json.getJSONObject("response");
            JSONArray results = response.getJSONArray("results");

            Log.d("LOG","NEWS cargando la info de la lista");

            for (int i = 0; i < results.length(); i++) {

                JSONObject currentNews = results.getJSONObject(i);

                String sectionName              = currentNews.getString(SECTION_NAME);
                String publicationDate          = currentNews.getString(PUBLICATION_DATE);
                String publicationTitle         = currentNews.getString(PUBLICATION_TITLE);
                String publicationURL           = currentNews.getString(PUBLICATION_URL);

                Log.d("LOG","NEWS sectionName="+sectionName);
                Log.d("LOG","NEWS publicationDate="+publicationDate);
                Log.d("LOG","NEWS publicationTitle="+publicationTitle);
                Log.d("LOG","NEWS publicationURL="+publicationURL);

                String author="";
                try {
                    //Check if exists tags
                    if(currentNews.getJSONArray(TAGS) != null)
                    {
                        JSONArray tags                  = currentNews.getJSONArray(TAGS);
                        if (tags.length()!= 0)
                        {
                            JSONObject currenttagsauthor = tags.getJSONObject(0);
                            author = currenttagsauthor.getString(PUBLICATION_TITLE);
                        }
                        else
                            author = "No Author ..";
                    }
                }
                catch (JSONException je)
                {
                    author = "No Author ..";
                }

                News news = new News(sectionName,publicationDate,publicationTitle,publicationURL,author);
                newsList.add(news);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return newsList;
    }
}
