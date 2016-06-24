package com.example.merna.shopxmall;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.merna.shopxmall.Model.Shop;
import com.example.merna.shopxmall.mPicasso.PicassoClient;
import com.example.merna.shopxmall.ui.DetailsActivityFragment;
import com.example.merna.shopxmall.ui.MainActivity;
import com.example.merna.shopxmall.ui.ShowActivity;
import com.example.merna.shopxmall.ui.ShowOffers;
import com.example.merna.shopxmall.ui.ShowOffersFragment;
import com.example.merna.shopxmall.utils.Constants;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.ui.FirebaseListAdapter;

/**
 * Created by Merna on 5/1/2016.
 */

public class shopAdapter extends FirebaseListAdapter<Shop> {
    Activity activity;

    public shopAdapter(Activity activity, Class<Shop> modelClass, int modelLayout, Query ref) {
        super(activity, modelClass, modelLayout, ref);
        this.activity = activity;
    }

    @Override
    protected void populateView(View v, final Shop model, final int position) {
        super.populateView(v, model);

        TextView name = (TextView) v.findViewById(R.id.ShopName);
        TextView category = (TextView) v.findViewById(R.id.ShopCategory);
        ImageView reportIcon = (ImageView) v.findViewById(R.id.reportIcon);
        final ImageView offerIcon = (ImageView) v.findViewById(R.id.offerIcon);
        final ImageView detailIcon = (ImageView) v.findViewById(R.id.detailsIcon);
        ImageView logo = (ImageView) v.findViewById(R.id.ShopLogo);


        name.setText(model.getShopName());
        category.setText(model.getCategory());

        String img = model.getLogo();

        if (model.getLogo() != null) {
            PicassoClient.downloadImg(activity, img, logo);
        }

        detailIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("clicled", String.valueOf(position));

                detailIcon.setImageResource(R.drawable.analysis_icon_active);
                offerIcon.setImageResource(R.drawable.offers_icon_unactive);

                Bundle arguments = new Bundle();
                arguments.putSerializable("DETAIL_SHOP", model);

                DetailsActivityFragment fragment = new DetailsActivityFragment();
                fragment.setArguments(arguments);

                ((MainActivity) activity).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.shop_detail_container, fragment, DetailsActivityFragment.TAG)
                        .commit();
            }
        });

        offerIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                detailIcon.setImageResource(R.drawable.analysis_icon_unactive);
                offerIcon.setImageResource(R.drawable.offers_icon_active);

                Bundle arguments = new Bundle();
                arguments.putSerializable("DETAIL_OFFER", getRef(position).getKey());
                Log.e("adapter",getRef(position).getKey());
                ShowOffersFragment fragment = new ShowOffersFragment();
                fragment.setArguments(arguments);


                ((MainActivity) activity).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.shop_detail_container, fragment, ShowOffersFragment.TAG)
                        .commit();
            }
        });
    }
}
