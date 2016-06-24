package com.example.merna.shopxmall.ui;

import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.merna.shopxmall.Model.Offer;
import com.example.merna.shopxmall.OfferAdapter;
import com.example.merna.shopxmall.R;
import com.example.merna.shopxmall.mPicasso.PicassoClient;
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
public class ShowOffersFragment extends Fragment {

    public static final String TAG = ShowOffersFragment.class.getSimpleName();
    static final String DETAIL_OFFER = "DETAIL_OFFER";

    GridView mGridView;
    OfferAdapter mOfferAdapter;
    ImageView oImg;
    TextView oName,oCategory;

    public ShowOffersFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_show_offers, container, false);
        oImg=(ImageView)rootView.findViewById(R.id.oLogo);
        oName=(TextView)rootView.findViewById(R.id.oShopName);
        oCategory=(TextView)rootView.findViewById(R.id.oShopCategory);

        Bundle arguments = getArguments();
        if (arguments != null)
        {
            String uidOffer= (String) arguments.getSerializable(ShowOffersFragment.DETAIL_OFFER);

            mGridView=(GridView)rootView.findViewById(R.id.gridViewOffer);
            Firebase offerList=new Firebase(Constants.FIREBASE_URL).child("OfferList").child(uidOffer);
            Query query = offerList.orderByChild("status").equalTo("false");
            mOfferAdapter = new OfferAdapter(getActivity(),Offer.class, R.layout.offer_item, query);
            mGridView.setAdapter(mOfferAdapter);
            Firebase shopData=new Firebase(Constants.FIREBASE_URL).child("Shops").child(uidOffer);
            shopData.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    dataSnapshot.child("shopName").getValue();
                    oName.setText((CharSequence) dataSnapshot.child("shopName").getValue());
                    oCategory.setText((CharSequence) dataSnapshot.child("category").getValue());
                    String uri= (String) dataSnapshot.child("logo").getValue();
                    PicassoClient.downloadImg(getActivity(),uri,oImg);
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });


        }



        return rootView;
    }
}
