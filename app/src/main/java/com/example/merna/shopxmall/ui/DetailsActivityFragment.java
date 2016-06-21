package com.example.merna.shopxmall.ui;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.merna.shopxmall.Model.Shop;
import com.example.merna.shopxmall.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailsActivityFragment extends Fragment {

    public static final String TAG = DetailsActivityFragment.class.getSimpleName();
    static final String DETAIL_SHOP = "DETAIL_SHOP";

    public DetailsActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView= inflater.inflate(R.layout.fragment_details, container, false);
        Bundle arguments = getArguments();

        if (arguments != null) {
            Shop shop = arguments.getParcelable(DetailsActivityFragment.DETAIL_SHOP);
            String shopname=shop.getShopName();
            Log.e("name",shopname);
        }
        return rootView;
    }
}
