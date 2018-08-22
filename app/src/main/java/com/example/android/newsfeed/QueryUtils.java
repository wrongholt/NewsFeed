package com.example.android.newsfeed;


import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class QueryUtils {
    public static final String LOG_TAG = QueryUtils.class.getSimpleName();
    private static final String KEY_RESPONSE = "response";
    private static final String KEY_RESULT = "results";
    private static final String KEY_TITLE = "webTitle";
    private static final String KEY_URL = "webUrl";
    private static final String KEY_DATE = "webPublicationDate";
    private static final String KEY_SECTION_NAME = "sectionName";
    private static final String KEY_AUTHOR = "webTitle";
    private static final String KEY_TAGS = "tags";


    private QueryUtils() {
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static List<NewsArticle> extractFeatureFromJson(String articleJSON) {
        if (TextUtils.isEmpty(articleJSON)) {
            return null;
        }

        List<NewsArticle> articles = new ArrayList<>();

        try {

            JSONObject root = new JSONObject(articleJSON);
            JSONObject response = root.getJSONObject(KEY_RESPONSE);
            JSONArray articlesArray = response.getJSONArray(KEY_RESULT);
            NewsArticle article;
            for (int i = 0; i < articlesArray.length(); i++) {

                JSONObject currentArticle = articlesArray.getJSONObject(i);

                String heading = currentArticle.getString(KEY_SECTION_NAME);

                String details = currentArticle.getString(KEY_TITLE);

                String url = currentArticle.getString(KEY_URL);

                String date = currentArticle.getString(KEY_DATE);

                JSONArray tags = currentArticle.getJSONArray(KEY_TAGS);
                if (tags.length() > 0) {
                    JSONObject currentArticle2 = tags.getJSONObject(0);

                    String author = currentArticle2.getString(KEY_AUTHOR);

                    article = new NewsArticle(heading, details, date, author, url);
                } else {
                    article = new NewsArticle(heading, details, date, url);
                }
                articles.add(article);
            }

        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        return articles;
    }

    public static List<NewsArticle> fetchData(String requestUrl) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try {

            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        List<NewsArticle> articles = extractFeatureFromJson(jsonResponse);

        return articles;
    }
}
