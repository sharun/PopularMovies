package com.sharuns.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.sharuns.popularmovies.sync.MovieLoader;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by sharuns on 1/21/2017.
 */

public class Utility {
   /* public static  final String API_KEY = "15d99159b5919c67b9c361d7d3f77f6b";*/
    private static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";
    private static final String IMAGE_SIZE = "w342";
    private static final String PATH_SEPARATOR = "/";
    //public static final String DEVELOPER_KEY = "AIzaSyDpzDiD894g3Um10YZPz0HuqPlfMmpSwoY";
    //public static final String DEVELOPER_KEY = "AIzaSyAb64xXx4CRUSzQnxMxMGchHlOfKL4Li0I"
    public static String[] mMoviesArray;


    public static boolean isInternetAvailable(Context ctx){
        ConnectivityManager cm = (ConnectivityManager)ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }


    public static void displayInternetConnectionMessage(Context ctx){
        Toast.makeText(ctx, "Check Internet Connection", Toast.LENGTH_SHORT).show();
    }

    public static String getPreferredSortOrder(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(context.getString(R.string.pref_sort_order_key),
                context.getString(R.string.pref_sort_order_default));
    }

    public static String getImageURL(String imagePath) {
        StringBuilder imageURL = new StringBuilder();

        imageURL.append(IMAGE_BASE_URL);
        imageURL.append(IMAGE_SIZE);
        imageURL.append(PATH_SEPARATOR);
        imageURL.append(imagePath);

        return imageURL.toString();
    }

    public static String[] loadFavoriteMovieIds (Context context) {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Set<String> favoritMovieIdsSet =  prefs.getStringSet(DetailActivityFragment.FAVORITE_MOVIE_IDS, null);

        if (favoritMovieIdsSet != null) {
            String[] array = new String[favoritMovieIdsSet.size()];

            Iterator<String> movieIdsIter = favoritMovieIdsSet.iterator();

            int i = 0;
            while (movieIdsIter.hasNext()) {
                array[i] = movieIdsIter.next();
                i = i + 1;
            }
            return array;
        }

        return null;
    }

    public static String arrayToString(String[] args) {
        StringBuilder argsBuilder = new StringBuilder();

        final int argsCount = args.length;
        for (int i = 0; i < argsCount; i++) {
            argsBuilder.append(args[i]);

            if (i < argsCount - 1) {
                argsBuilder.append(",");
            }
        }

        return argsBuilder.toString();
    }

    public static int getMovieStatus(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getInt(context.getString(R.string.pref_movie_status_key), MovieLoader.MOVIE_STATUS_UNKNOWN);
    }

    /*Source:
    * http://stackoverflow.com/questions/17693578/android-how-to-display-2-listviews-in-one-activity-one-after-the-other
    * */

    public static void setDynamicHeight(ListView mListView) {
        ListAdapter mListAdapter = mListView.getAdapter();
        if (mListAdapter == null) {
            // when adapter is null
            return;
        }
        int height = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(mListView.getWidth(), View.MeasureSpec.AT_MOST);
        for (int i = 0; i < mListAdapter.getCount(); i++) {
            View listItem = mListAdapter.getView(i, null, mListView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            height += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = mListView.getLayoutParams();
        params.height = height + (mListView.getDividerHeight() * (mListAdapter.getCount()));
        mListView.setLayoutParams(params);
        mListView.requestLayout();
    }

    public static String getVideoHTML(String videoId) {

        String html =
                "<iframe class=\"youtube-player\" "
                        + "style=\"border: 0; width: 100%; height: 95%;"
                        + "padding:0px; margin:0px\" "
                        + "id=\"ytplayer\" type=\"text/html\" "
                        + "src=\"http://www.youtube.com/embed/" + videoId
                        + "?fs=0\" frameborder=\"0\" " + "allowfullscreen autobuffer "
                        + "controls onclick=\"this.play()\">\n" + "</iframe>\n";
        return html;
    }

    public static boolean canResolveIntent(Intent intent, Context context) {
        List<ResolveInfo> resolveInfo = context.getPackageManager().queryIntentActivities(intent, 0);
        return resolveInfo != null && !resolveInfo.isEmpty();
    }

    public static boolean isMovieIdFavorite(String [] favoriteMovieIds, String movieId) {
        boolean result = false;

        if (favoriteMovieIds == null || favoriteMovieIds.length == 0) return result;

        for (int i = 0; i < favoriteMovieIds.length; i++) {
            if (movieId.trim().equals(favoriteMovieIds[i].trim())){
                result = true;
                break;
            }
        }

        return result;
    }

    public static void  setMovieIDs(String[] movieIDs) {

        for (int i = 0; i < movieIDs.length; i++) {
            System.out.print("Enter a string to put at position " + i + " of the array: ");
            mMoviesArray[i] = movieIDs[i];
        }

    }

    public static String[] getMovieIDs() {
        return mMoviesArray;
    }
}

