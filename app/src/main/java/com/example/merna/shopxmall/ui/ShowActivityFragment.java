package com.example.merna.shopxmall.ui;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import com.example.merna.shopxmall.Model.Shop;
import com.example.merna.shopxmall.R;
import com.example.merna.shopxmall.shopAdapter;
import com.example.merna.shopxmall.utils.Constants;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

/**
 * A placeholder fragment containing a simple view.
 */
public class ShowActivityFragment extends Fragment {

    ListView mListView;
    shopAdapter mShopAdapter;
    Firebase shopList;

    public ShowActivityFragment() {
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_show, menu);
        //****** search code *********

        final SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        SearchManager searchManger = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManger.getSearchableInfo(getActivity().getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                doMySearch(newText);
                return false;
            }
        });


    }


    //** search code **
    void doMySearch(String query) {

        Query query2 = shopList.orderByChild("shopName").startAt(query).endAt(query + "\uf8ff");
       // query2.orderByChild("status").equalTo("false");
        mShopAdapter = new shopAdapter(getActivity(), Shop.class, R.layout.shop_item, query2);
        mListView.setAdapter(mShopAdapter);
    }
    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            doMySearch(query);
        }
    }
    //****************
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_show, container, false);
        setHasOptionsMenu(true);
        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Click action
                Intent intent = new Intent(getActivity(), AddActivity.class);
                startActivity(intent);
            }
        });

        handleIntent(getActivity().getIntent());
        mListView = (ListView) rootView.findViewById(R.id.listView);
        shopList = new Firebase(Constants.FIREBASE_URL).child("Shops");
        Query query = shopList.orderByChild("status").equalTo("false");
        mShopAdapter = new shopAdapter(getActivity(), Shop.class, R.layout.shop_item, query);
        mListView.setAdapter(mShopAdapter);


        mListView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Shop shop = mShopAdapter.getItem(position);
                Log.d("clicked", shop.getShopName());
                //  ((Callback) getActivity()).onItemSelected(shop);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mShopAdapter != null) {
            mShopAdapter.cleanup();
//           finish();
        }
    }

}
