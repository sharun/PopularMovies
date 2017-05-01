package com.sharuns.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by sharuns on 1/21/2017.
 */

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        if (savedInstanceState == null) {

            Bundle arguments = new Bundle();
            String data[] = getIntent().getStringArrayExtra(Intent.EXTRA_TEXT);
            arguments.putStringArray(Intent.EXTRA_TEXT, data);
            DetailActivityFragment fragment = new DetailActivityFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, fragment)
                    .commit();
        }
    }


}
