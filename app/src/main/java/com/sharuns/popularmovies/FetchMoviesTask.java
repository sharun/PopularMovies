package com.sharuns.popularmovies;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sharuns on 2/11/2017.
 */


public class FetchMoviesTask extends AsyncTask<String, Void, List<Movie>> {

    private static final String TAG= FetchMoviesTask.class.getSimpleName();
    private Context mContext;

    private AsyncTaskCompleteListener<List<Movie>> listener;

    public FetchMoviesTask(Context context, AsyncTaskCompleteListener<List<Movie>> listener)
    {
        this.mContext = context;
        this.listener = listener;
    }

    private List<Movie> getMoviesDataFromJson(String forecastJsonStr)
            throws JSONException {
        JSONObject movieJson = new JSONObject(forecastJsonStr);
        JSONArray movieArray = movieJson.getJSONArray("results");
        List<Movie> movieList = new ArrayList<>();
        for (int i = 0; i < movieArray.length(); i++) {
            JSONObject movieJsonObject = movieArray.getJSONObject(i);
            Movie movie = new Movie(movieJsonObject);
            movieList.add(movie);
        }
        return movieList;
    }

    @Override
    protected void onPostExecute(List<Movie> movieList) {
        super.onPostExecute(movieList);
        listener.onTaskComplete(movieList);
    }

    @Override
    protected List<Movie> doInBackground(String... params) {
        //These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.

        if(params.length == 0){
            return null;
        }

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String movieJsonStr = null;

        String appKey = "15d99159b5919c67b9c361d7d3f77f6b";
        try {
            // Construct the URL for the OpenWeatherMap query
            // Possible parameters are available at OWM's forecast API page, at
            // http://openweathermap.org/API#forecast

            String BASE_URL = "https://api.themoviedb.org/3/movie/";

            if(params[0].equalsIgnoreCase(PopularMoviesFragment.POPULAR)) {
                BASE_URL = BASE_URL.concat(params[0] + "?");
            } else if(params[0].equalsIgnoreCase(PopularMoviesFragment.TOP_RATED)) {
                BASE_URL = BASE_URL.concat(params[0] + "?");
            }

            final String API_KEY_PARAM = "api_key";

            //URL url = new URL("http://api.openweathermap.org/data/2.5/forecast/daily?q=94043&mode=json&units=metric&cnt=7");

            Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                    .appendQueryParameter(API_KEY_PARAM, BuildConfig.MyMovieDBApiKey)
                    .build();


            URL url = new URL(builtUri.toString());

            Log.v(TAG, "Built URI" +builtUri.toString());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                return null;
            }

            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                return null;
            }
            movieJsonStr = buffer.toString();
            Log.v(TAG, "Forecast JSON String: " +movieJsonStr);
        } catch (IOException e) {
            Log.e(TAG, "Error ", e);
            return null;
        } finally{
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(TAG, "Error closing stream", e);
                }
            }
        }

        try {
            return getMoviesDataFromJson(movieJsonStr);
        } catch (JSONException e) {
            Log.e(TAG,e.getMessage(),e);
            e.printStackTrace();
        }

        return null;
    }

}
