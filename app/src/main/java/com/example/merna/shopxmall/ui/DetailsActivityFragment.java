package com.example.merna.shopxmall.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.merna.shopxmall.Model.Shop;
import com.example.merna.shopxmall.R;
import com.example.merna.shopxmall.mPicasso.PicassoClient;
import com.example.merna.shopxmall.utils.Constants;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailsActivityFragment extends Fragment {

    public static final String TAG = DetailsActivityFragment.class.getSimpleName();
    static final String DETAIL_SHOP = "DETAIL_SHOP";
    String shop,email,pass;
    ImageView Img;
    TextView mTxtShopName, mTxtCategoryTitle, mTxtPhone, mTxtFb, mTxtTwitter, mTxtArea, mTxtBeacon, mTxtOffersNum, mTxtCategory;


    public DetailsActivityFragment() {
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_details, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_Edit) {
            Intent in = new Intent(getContext(),AddActivity.class);
            in.putExtra("uuid", shop);
            startActivity(in);
            return true;
        }

        if (id == R.id.action_Delete) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage("Are you sure you want to delete this shop ?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Firebase gRef = new Firebase(Constants.FIREBASE_URL);
                            gRef.removeUser(email,pass, new Firebase.ResultHandler() {
                                @Override
                                public void onSuccess() {
                                    Toast.makeText(getContext(), "deleted", Toast.LENGTH_SHORT).show();
//                                    Firebase shopRef = new Firebase(Constants.FIREBASE_URL).child("Shops")
//                                            .child(shop).child("status");
//                                    shopRef.setValue("true");
                                    Firebase itemRef = new Firebase(Constants.FIREBASE_URL).child("Shops").child(shop);
                                    itemRef.removeValue();

                                    Firebase userRef = new Firebase(Constants.FIREBASE_URL).child("users").child(shop);
                                    userRef.removeValue();
                                }

                                @Override
                                public void onError(FirebaseError firebaseError) {

                                }
                            });
                            // activity.finish();
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();

            return true;
        }


        return super.onOptionsItemSelected(item);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_details, container, false);
        setHasOptionsMenu(true);
        Bundle arguments = getArguments();
        if (arguments != null) {
            shop = (String) arguments.getSerializable(DetailsActivityFragment.DETAIL_SHOP);

            Img = (ImageView) rootView.findViewById(R.id.dLogo);
            mTxtShopName = (TextView) rootView.findViewById(R.id.ShopName);
            mTxtCategoryTitle = (TextView) rootView.findViewById(R.id.ShopCategory);
            mTxtArea = (TextView) rootView.findViewById(R.id.areaData);
            mTxtBeacon = (TextView) rootView.findViewById(R.id.beaconData);
            mTxtOffersNum = (TextView) rootView.findViewById(R.id.offersNumData);
            mTxtCategory = (TextView) rootView.findViewById(R.id.categoryData);
            mTxtPhone = (TextView) rootView.findViewById(R.id.phoneData);
            mTxtFb = (TextView) rootView.findViewById(R.id.fbData);
            mTxtTwitter = (TextView) rootView.findViewById(R.id.twitterData);

            Firebase shopData = new Firebase(Constants.FIREBASE_URL).child("Shops").child(shop);
            shopData.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    email= (String) dataSnapshot.child("shopEmail").getValue();
                    pass= (String) dataSnapshot.child("password").getValue();
                    mTxtShopName.setText((CharSequence) dataSnapshot.child("shopName").getValue());
                    mTxtCategory.setText((CharSequence) dataSnapshot.child("category").getValue());
                    mTxtArea.setText((CharSequence) dataSnapshot.child("areaInMAll").getValue());
                    //** here beacon
                    //** num offers
                    mTxtCategoryTitle.setText((CharSequence) dataSnapshot.child("category").getValue());
                    mTxtPhone.setText((CharSequence) dataSnapshot.child("phone").getValue());
                    mTxtFb.setText((CharSequence) dataSnapshot.child("fbContact").getValue());
                    mTxtTwitter.setText((CharSequence) dataSnapshot.child("twitterContact").getValue());
                    String uri = (String) dataSnapshot.child("logo").getValue();
                    PicassoClient.downloadImg(getActivity(), uri, Img);
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }


            });

        }
            return rootView;
        }


}