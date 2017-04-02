package com.sharuns.popularmovies;

/**
 * Created by sharuns on 2/11/2017.
 */

public interface AsyncTaskCompleteListener<T> {
    /**
     * Invoked when the AsyncTask has completed its execution.
     * @param result The resulting object from the AsyncTask.
     */
    public void onTaskComplete(T result);
}
