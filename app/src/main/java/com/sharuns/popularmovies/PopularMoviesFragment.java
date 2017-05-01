package com.sharuns.popularmovies;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.sharuns.popularmovies.adapter.GridViewMoviesAdapter;
import com.sharuns.popularmovies.data.PopularMoviesContract;
import com.sharuns.popularmovies.sync.MovieLoader;
import com.sharuns.popularmovies.sync.PopularMoviesSyncAdapter;

import static com.sharuns.popularmovies.R.id.gridView;

/**
 * A placeholder fragment containing a simple view.
 */
public class PopularMoviesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG= PopularMoviesFragment.class.getSimpleName();
    GridViewMoviesAdapter mMovieAdapter;
    private GridView mGridView;
    private boolean mTwoPane;
    private static final int FORECAST_LOADER = 0;

    public static final String TOP_RATED = "top_rated";
    public static final String POPULAR = "popular";

    private String mSortBy = POPULAR;

    public static final String[] MOVIE_COLUMNS = {
            PopularMoviesContract.MovieEntry.TABLE_NAME + "." + PopularMoviesContract.MovieEntry._ID,
            PopularMoviesContract.MovieEntry.COLUMN_MOVIE_ID,
            PopularMoviesContract.MovieEntry.COLUMN_IS_ADULT,
            PopularMoviesContract.MovieEntry.COLUMN_BACK_DROP_PATH,
            PopularMoviesContract.MovieEntry.COLUMN_ORIGINAL_LANGUAGE,
            PopularMoviesContract.MovieEntry.COLUMN_ORIGINAL_TITLE,
            PopularMoviesContract.MovieEntry.COLUMN_OVERVIEW,
            PopularMoviesContract.MovieEntry.COLUMN_RELEASE_DATE,
            PopularMoviesContract.MovieEntry.COLUMN_POSTER_PATH,
            PopularMoviesContract.MovieEntry.COLUMN_POPULARITY,
            PopularMoviesContract.MovieEntry.COLUMN_TITLE,
            PopularMoviesContract.MovieEntry.COLUMN_IS_VIDEO,
            PopularMoviesContract.MovieEntry.COLUMN_VOTE_AVERAGE,
            PopularMoviesContract.MovieEntry.COLUMN_VOTE_COUNT,
            PopularMoviesContract.MovieEntry.COLUMN_RUNTIME,
            PopularMoviesContract.MovieEntry.COLUMN_STATUS,
            PopularMoviesContract.MovieEntry.COLUMN_DATE
    };

    public static final int COL_MOVIE_PK_ID = 0;
    public static final int COL_MOVIE_ID = 1;
    public static final int COL_IS_ADULT   = 2;
    public static final int COL_BACK_DROP_PATH = 3;
    public static final int COL_ORIGINAL_LANGUAGE = 4;
    public static final int COL_ORIGINAL_TITLE = 5;
    public static final int COL_OVERVIEW = 6;
    public static final int COL_RELEASE_DATE = 7;
    public static final int COL_POSTER_PATH = 8;
    public static final int COL_POPULARITY = 9;
    public static final int COL_TITLE = 10;
    public static final int COL_IS_VIDEO = 11;
    public static final int COL_VOTE_AVERAGE = 12;
    public static final int COL_VOTE_COUNT = 13;
    public static final int COL_RUNTIME = 14;
    public static final int COL_STATUS = 15;
    public static final int COL_DATE = 16;

    public PopularMoviesFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        // Get a reference to the ListView, and attach this adapter to it.
        mGridView = (GridView) rootView.findViewById(gridView);
        mMovieAdapter = new GridViewMoviesAdapter(getActivity(), null, 0);
        TextView emptyTextView = (TextView) rootView.findViewById(R.id.gridView_empty);
        emptyTextView.setText(getString(R.string.tv_no_movies_favorite));
        mGridView.setEmptyView(emptyTextView);
        mGridView.setAdapter(mMovieAdapter);
        /*if(Utility.isInternetAvailable(getContext())) {
            updateMovies(mSortBy);
        } else {
            Utility.displayInternetConnectionMessage(getContext());
        }
*/
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView adapterView, View view, int position, long id) {
                // CursorAdapter returns a cursor at the correct position for getItem(), or null
                // if it cannot seek to that position.
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);

                //TODO
                String backDropPath = cursor.getString(COL_BACK_DROP_PATH);
                String posterPath = cursor.getString(COL_POSTER_PATH);
                if (backDropPath == null || backDropPath.equals("null")) {
                    if (posterPath != null && !posterPath.equals("null")) {
                        backDropPath = posterPath;
                    }
                }

                if (posterPath == null || posterPath.equals("null")) {
                    if (backDropPath != null && !backDropPath.equals("null")) {
                        posterPath = backDropPath;
                    }
                }


                String data[] = {backDropPath, posterPath, cursor.getString(COL_RELEASE_DATE).toString(),
                        Double.toString(cursor.getDouble(COL_RUNTIME)), Double.toString(cursor.getDouble(COL_VOTE_AVERAGE)), cursor.getString(COL_OVERVIEW),
                        cursor.getString(COL_ORIGINAL_TITLE), Integer.toString(cursor.getInt(COL_MOVIE_ID)), Boolean.toString(mTwoPane)};
                ((Callback) getActivity()).onItemSelected(data);
             }
        });

        return rootView;
    }

  /*  @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_sort_by_popularity:
                if (item.isChecked()) {
                    item.setChecked(false);
                } else {
                    item.setChecked(true);
                }
                mSortBy = POPULAR;
                updateMovies(mSortBy);
                return true;
            case R.id.action_sort_by_rating:
                if (item.isChecked()) {
                    item.setChecked(false);
                } else {
                    item.setChecked(true);
                }
                mSortBy = TOP_RATED;
                updateMovies(mSortBy);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
*/

    void onSortOrderChanged( ) {
        PopularMoviesSyncAdapter.syncImmediately(getActivity());
        getLoaderManager().restartLoader(FORECAST_LOADER, null, this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(FORECAST_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sortOrderSelected = prefs.getString(getActivity().getString(R.string.pref_sort_order_key), null);

        String sortOrder = PopularMoviesContract.MovieEntry.COLUMN_POPULARITY + " DESC";

        if(sortOrderSelected != null && sortOrderSelected.equals(getActivity().getString(R.string.pref_sort_order_vote_average))) {
            sortOrder = PopularMoviesContract.MovieEntry.COLUMN_VOTE_AVERAGE + " DESC";
        }

        Uri movieUri = PopularMoviesContract.MovieEntry.buildMovieUri();
        Log.v("Sharun", movieUri.toString());

        return new CursorLoader(getActivity(),
                movieUri,
                MOVIE_COLUMNS,
                null,
                null,
                sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        mMovieAdapter.swapCursor(cursor);
        updateEmptyView();

    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mMovieAdapter.swapCursor(null);
    }


    public void setTwoPane(boolean mTwoPane) {
        this.mTwoPane = mTwoPane;
    }

    private void updateEmptyView() {
        if (mMovieAdapter.getCount() == 0) {
            TextView emptyTextView = (TextView) getView().findViewById(R.id.gridView_empty);
            if (null != emptyTextView) {
                int message = R.string.empty_movies_list;
                int status = Utility.getMovieStatus(getActivity());
                switch (status) {
                    case MovieLoader.MOVIE_STATUS_SERVER_DOWN:
                        message = R.string.empty_movies_list_server_down;
                        break;
                    case MovieLoader.MOVIE_STATUS_SERVER_INVALID:
                        message = R.string.empty_movies_list_server_error;
                        break;
                    default:
                        String [] favoriteMovieIds = Utility.loadFavoriteMovieIds(getActivity());
                        if ((favoriteMovieIds == null || favoriteMovieIds.length <= 0) &&
                                Utility.getPreferredSortOrder(getActivity()).equals(getActivity().getString(R.string.pref_sort_order_favorite))) {
                            message = R.string.tv_no_movies_favorite;
                        }
                        else if (!Utility.isInternetAvailable(getActivity())) {
                            message = R.string.empty_movies_list_no_network;
                        }
                        break;
                }
                emptyTextView.setText(message);
            }
        }
    }

    public interface Callback {
        /**
         * DetailFragmentCallback for when an item has been selected.
         */
        public void onItemSelected(String data[]);
    }
}
