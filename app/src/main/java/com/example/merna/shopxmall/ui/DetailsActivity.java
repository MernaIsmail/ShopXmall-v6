package com.example.merna.shopxmall.ui;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.merna.shopxmall.R;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        if (savedInstanceState == null) {

            Bundle arguments = new Bundle();

            arguments.putSerializable(DetailsActivityFragment.DETAIL_SHOP,
                    getIntent().getSerializableExtra(DetailsActivityFragment.DETAIL_SHOP));

            DetailsActivityFragment fragment = new DetailsActivityFragment();
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.shop_detail_container, fragment)
                    .commit();
        }

    }
}

