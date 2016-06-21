package com.example.merna.shopxmall.ui;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import com.example.merna.shopxmall.Model.Shop;
import com.example.merna.shopxmall.R;
import com.example.merna.shopxmall.shopAdapter;
import com.example.merna.shopxmall.utils.Constants;
import com.firebase.client.Firebase;
import com.firebase.client.Query;

/**
 * A placeholder fragment containing a simple view.
 */
public class ShowActivityFragment extends Fragment {

    ListView mListView;
    shopAdapter mShopAdapter;


    public ShowActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View rootView=inflater.inflate(R.layout.fragment_show, container, false);

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Click action
                Intent intent = new Intent(getActivity(), AddActivity.class);
                startActivity(intent);
            }
        });

        mListView=(ListView)rootView.findViewById(R.id.listView);
        Firebase shopList=new Firebase(Constants.FIREBASE_URL).child("Shops");
        Query query = shopList.orderByChild("status").equalTo("false");
        mShopAdapter = new shopAdapter(getActivity(),Shop.class, R.layout.shop_item, query);
        mListView.setAdapter(mShopAdapter);


        mListView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                   Shop shop=mShopAdapter.getItem(position);
                   Log.d("clicked",shop.getShopName());
                 //  ((Callback) getActivity()).onItemSelected(shop);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return rootView;
    }



    public interface Callback {
        void onItemSelected(Shop shop);
    }
}
