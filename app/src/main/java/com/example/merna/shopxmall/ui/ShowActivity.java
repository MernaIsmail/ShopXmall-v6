package com.example.merna.shopxmall.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.merna.shopxmall.Model.Shop;
import com.example.merna.shopxmall.R;

public class ShowActivity extends AppCompatActivity implements ShowActivityFragment.Callback{

    private boolean mTwoPane;

    private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mActivityTitle;
    String[] mArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        if (findViewById(R.id.shop_detail_container) != null) {
            mTwoPane = true;
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.shop_detail_container, new DetailsActivityFragment(),
                                DetailsActivityFragment.TAG)
                        .commit();
            }
        } else {
            mTwoPane = false;
        }



//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    @Override
    public void onItemSelected(Shop shop) {
        if (mTwoPane) {
            Bundle arguments = new Bundle();
            arguments.putSerializable(DetailsActivityFragment.DETAIL_SHOP, shop);

            DetailsActivityFragment fragment = new DetailsActivityFragment();
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.shop_detail_container, fragment, DetailsActivityFragment.TAG)
                    .commit();
        } else {
            Intent intent = new Intent(this, DetailsActivity.class)
                    .putExtra(DetailsActivityFragment.DETAIL_SHOP, shop);
            startActivity(intent);
        }
    }
}
