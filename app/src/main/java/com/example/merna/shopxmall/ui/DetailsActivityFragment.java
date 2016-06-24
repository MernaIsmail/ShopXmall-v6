package com.example.merna.shopxmall.ui;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.merna.shopxmall.Model.Shop;
import com.example.merna.shopxmall.R;
import com.example.merna.shopxmall.mPicasso.PicassoClient;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailsActivityFragment extends Fragment {

    public static final String TAG = DetailsActivityFragment.class.getSimpleName();
    static final String DETAIL_SHOP = "DETAIL_SHOP";
    Shop shop;
    ImageView Img;
    TextView mTxtShopName,mTxtCategoryTitle,mTxtPhone,mTxtFb,mTxtTwitter,mTxtArea,mTxtBeacon,mTxtOffersNum,mTxtCategory;


    public DetailsActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView= inflater.inflate(R.layout.fragment_details, container, false);

        Bundle arguments = getArguments();
        if (arguments != null) {
          shop= (Shop) arguments.getSerializable(DetailsActivityFragment.DETAIL_SHOP);

            Img=(ImageView)rootView.findViewById(R.id.dLogo);
            mTxtShopName=(TextView)rootView.findViewById(R.id.ShopName);
            mTxtCategoryTitle=(TextView)rootView.findViewById(R.id.ShopCategory);
            mTxtArea=(TextView)rootView.findViewById(R.id.areaData);
            mTxtBeacon=(TextView)rootView.findViewById(R.id.beaconData);
            mTxtOffersNum=(TextView)rootView.findViewById(R.id.offersNumData);
            mTxtCategory=(TextView)rootView.findViewById(R.id.categoryData);
            mTxtPhone=(TextView)rootView.findViewById(R.id.phoneData);
            mTxtFb=(TextView)rootView.findViewById(R.id.fbData);
            mTxtTwitter=(TextView)rootView.findViewById(R.id.twitterData);

            mTxtShopName.setText(shop.getShopName());
            mTxtCategoryTitle.setText(shop.getCategory());
            mTxtArea.setText(shop.getAreaInMAll());
            //** here beacon
            //** num offers
            mTxtCategory.setText(shop.getCategory());
            mTxtPhone.setText(shop.getPhone());
            mTxtFb.setText(shop.getFbContact());
            mTxtTwitter.setText(shop.getTwitterContact());
            PicassoClient.downloadImg(getActivity(),shop.getLogo(),Img);
        }


        return rootView;
    }

}
