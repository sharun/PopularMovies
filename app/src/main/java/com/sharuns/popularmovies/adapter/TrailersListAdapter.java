package com.sharuns.popularmovies.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.sharuns.popularmovies.DetailActivityFragment;
import com.sharuns.popularmovies.R;


/**
 * Created by sharuns on 4/22/2017.
 */

public class TrailersListAdapter extends
        CursorAdapter {

    public TrailersListAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        View view = LayoutInflater.from(context).inflate(R.layout.list_trailers_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ViewHolder viewHolder = (ViewHolder) view.getTag();

        String trailerName = cursor.getString(DetailActivityFragment.COL_NAME);

        viewHolder.trailerNameTextView.setText(trailerName);
    }

    public static class ViewHolder {
        public final TextView trailerNameTextView;

        public ViewHolder(View vi) {
            trailerNameTextView = (TextView)vi.findViewById(R.id.textview_trailer_name);
        }
    }
}

