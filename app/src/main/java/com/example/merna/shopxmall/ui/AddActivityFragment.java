package com.example.merna.shopxmall.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.cloudinary.Cloudinary;
import com.example.merna.shopxmall.Model.Shop;
import com.example.merna.shopxmall.R;
import com.example.merna.shopxmall.mPicasso.PicassoClient;
import com.example.merna.shopxmall.utils.Constants;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A placeholder fragment containing a simple view.
 */
public class AddActivityFragment extends Fragment {

    Firebase mFirebaseRef;
    private ProgressDialog mAuthProgressDialog;
    EditText mEditTextEmailInput, mEditTextPasswordInput, mEditTextShopName, mEditTextcallInput, mEditTextFbContact, mEditTextTwitterContact;
    ImageButton addPic;
    private static final int PICK_IMAGE = 100;
    String mEmail, mPassword, mShopName, mCallContact, mFbContact, mTwitterContact,mAreaInMall,mCategory;
    String imageFile = null;
    Shop newShop;
    String toEdit = null;
    String uuidRef,email,pass;
    Uri imageUri;
    Cloudinary cloudinary;
    String Generated_Id;
    InputStream in;
    ArrayList<String> spinnerCatrgoryList = new ArrayList<String>();
    ArrayList<String> spinnerAreaList = new ArrayList<String>();
    Spinner spinner;
    Spinner spinnerCustom;


    public AddActivityFragment() {
    }

    public String generatePIN() {
        int randomPIN = (int) (Math.random() * 9000) + 1000;
        return String.valueOf(randomPIN);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }
    public boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();
        if (netinfo != null && netinfo.isConnectedOrConnecting()) {
            android.net.NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if ((mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting()))
                return true;
            else return false;
        } else return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:

                mEmail = mEditTextEmailInput.getText().toString();
                mPassword = mEditTextPasswordInput.getText().toString();
                mShopName = mEditTextShopName.getText().toString();
                mCallContact = mEditTextcallInput.getText().toString();
                mFbContact = mEditTextFbContact.getText().toString();
                mTwitterContact = mEditTextTwitterContact.getText().toString();

                if (isConnected(getContext())) {

                    boolean validEmail = isEmailValid(mEmail);
                    boolean validUserName = isUserNameValid(mShopName);
                    boolean validPassword = isPasswordValid(mPassword);
                    if (!validEmail || !validUserName || !validPassword) return false;
                    if (TextUtils.isEmpty(mCallContact)) {
                        mEditTextcallInput.setError("Please Enter The Number!");
                        return false;
                    } else if (mCallContact.length() < 11) {
                        mEditTextcallInput.setError("Please Enter The Correct Number!");
                        return false;
                    }

/**
 * If everything was valid show the progress dialog to indicate that
 * account creation has started
 *
 */Log.d("immm", String.valueOf(imageUri));

                    if (imageUri != null) {

                        Generated_Id = generatePIN();
                        final Map<String, String> options = new HashMap<>();
                        options.put("public_id", Generated_Id);

                        try {
                            in = ((AddActivity) getActivity()).getContentResolver().openInputStream(imageUri);
                            imageFile = cloudinary.url().generate(Generated_Id + ".jpg");
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }

                        Runnable runnable = new Runnable() {
                            @Override
                            public void run() {

                                try {
                                    cloudinary.uploader().upload(in, options);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    Log.d("errior", "imgtest");
                                }

                            }
                        };
                        new Thread(runnable).start();


                    }

                    if (toEdit != null) { //update
                        Firebase eRef = new Firebase(Constants.FIREBASE_URL).child("Shops").child(toEdit);
                        Firebase oRef = new Firebase(Constants.FIREBASE_URL);
                        oRef.changeEmail(email, pass, mEmail, new Firebase.ResultHandler() {
                            @Override
                            public void onSuccess() {
                                Log.d("change email", "done");
                            }

                            @Override
                            public void onError(FirebaseError firebaseError) {

                            }
                        });
                        oRef.changePassword(mEmail, pass, mPassword, new Firebase.ResultHandler() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError(FirebaseError firebaseError) {

                            }
                        });
                        Map<String, Object> updates = new HashMap<String, Object>();
                        updates.put("shopEmail", mEmail);
                        updates.put("shopName", mShopName);
                        updates.put("password", mPassword);
                        if (imageFile != null) {
                            updates.put("logo", imageFile);
                        }
                        updates.put("phone", mCallContact);
                        updates.put("fbContact", mFbContact);
                        updates.put("twitterContact", mTwitterContact);
                        updates.put("areaInMAll",mAreaInMall);
                        updates.put("category",mCategory);
                        eRef.updateChildren(updates);
                        Intent intent = new Intent(getContext(),MainActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    } else {
                        mAuthProgressDialog.show();
                        /**
                         * Create new user with specified email and password
                         */
                        mFirebaseRef.createUser(mEmail, mPassword, new Firebase.ValueResultHandler<Map<String, Object>>() {

                            @Override
                            public void onSuccess(Map<String, Object> result) {
                                /* Dismiss the progress dialog */
                                mAuthProgressDialog.dismiss();
                                Log.i("LOG_TAG", getString(R.string.log_message_auth_successful));
                                String uid = (String) result.get("uid");
                                newShop = new Shop(mShopName, mEmail, mPassword, imageFile, mCallContact, mFbContact, mTwitterContact,mAreaInMall,mCategory);
                                createUserInFirebaseHelper(uid, newShop);
                                Intent intent = new Intent(getContext(), MainActivity.class);
                                startActivity(intent);
                                getActivity().finish();
                            }

                            @Override
                            public void onError(FirebaseError firebaseError) {
                /* Error occurred, log the error and dismiss the progress dialog */
                                Log.d("LOG_TAG\"", getString(R.string.log_error_occurred) +
                                        firebaseError);
                                mAuthProgressDialog.dismiss();
                /* Display the appropriate error message */
                                if (firebaseError.getCode() == FirebaseError.EMAIL_TAKEN) {
                                    mEditTextEmailInput.setError(getString(R.string.error_email_taken));
                                } else {
                                    showErrorToast(firebaseError.getMessage());
                                }

                            }

                        });
                    }
                } else {

                    AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                    alertDialog.setTitle("No Internet connection.");
                    alertDialog.setMessage("You have no internet connection");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            imageUri = data.getData();
            addPic.setImageURI(imageUri);
//            try {
//                Bitmap bmp = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), Uri.parse(String.valueOf(imageUri)));
//                ByteArrayOutputStream bYtE = new ByteArrayOutputStream();
//                bmp.compress(Bitmap.CompressFormat.PNG, 50, bYtE);
//                bmp.recycle();
//                byte[] byteArray = bYtE.toByteArray();
//                imageFile = Base64.encodeToString(byteArray, Base64.DEFAULT);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

        }
    }

    private void createUserInFirebaseHelper(final String uid, final Shop shop) {

        //create User with type shop in userListDB
        final Firebase userLocation = new Firebase(Constants.FIREBASE_URL_USERS).child(uid);

        userLocation.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                /* If there is no user, make one */
                if (dataSnapshot.getValue() == null) {
                 /* Set raw version of date to the ServerValue.TIMESTAMP value and save into dateCreatedMap */
                    Shop newUser = new Shop(mEmail);
                    userLocation.setValue(newUser);
                    Log.e("create", newUser.getShopEmail() + newUser.getType());
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.d("LOG_TAG", getString(R.string.log_error_occurred) + firebaseError.getMessage());
            }
        });

        //create shop info in ShopListDB
        final Firebase shopList = new Firebase(Constants.FIREBASE_URL).child("Shops").child(uid);
        shopList.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {

                    shopList.setValue(shop);


                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }


    private boolean isEmailValid(String email) {
        boolean isGoodEmail =
                (email != null && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches());
        if (!isGoodEmail) {
            mEditTextEmailInput.setError(String.format(getString(R.string.error_invalid_email_not_valid),
                    email));
            return false;
        }
        return isGoodEmail;
    }

    private boolean isUserNameValid(String userName) {
        if (userName.equals("")) {
            mEditTextShopName.setError(getResources().getString(R.string.error_cannot_be_empty));
            return false;
        }
        return true;
    }

    private boolean isPasswordValid(String password) {
        if (password.length() < 6) {
            mEditTextPasswordInput.setError(getResources().getString(R.string.error_invalid_password_not_valid));
            return false;
        }
        return true;
    }

    private void showErrorToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_add, container, false);

        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        ((AddActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.close);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Are you sure you want to return to home and cancel this data ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent in = new Intent(getContext(), MainActivity.class);
                                startActivity(in);
                            }
                        })
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();

            }
        });
        ((AddActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);

        Map config = new HashMap();
        config.put("cloud_name", "gp");
        config.put("api_key", "667862958976234");
        config.put("api_secret", "zAQ9orjld73mDil8fFsdDNXUQrg");
        cloudinary = new Cloudinary(config);



        spinnerCustom = (Spinner) rootView.findViewById(R.id.areaSpinnerCustom);
        Firebase areaRef = new Firebase(Constants.FIREBASE_URL).child("Area");
        areaRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    child.child("areaName").getValue();
                    spinnerAreaList.add((String) child.child("areaName").getValue());
                }
                CustomSpinnerAdapter adapter=new CustomSpinnerAdapter(getContext(),spinnerAreaList);
                spinnerCustom.setAdapter(adapter);
                spinnerCustom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Log.d("itemSelected", (String) parent.getItemAtPosition(position));
                        String selectedItemText = (String)  parent.getSelectedItem();;
                        mAreaInMall= (String) parent.getSelectedItem();
                        // If user change the default selection
                        // First item is disable and it is used for hint
                        if (position > 0) {
                            // Notify the selected item text
                            Toast.makeText(getContext(), "Selected : " + selectedItemText, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                //** **
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        spinner = (Spinner) rootView.findViewById(R.id.category_spinner);
        Firebase CateegoryRef = new Firebase(Constants.FIREBASE_URL).child("Categories");
        CateegoryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    child.child("categoryName").getValue();
                    spinnerCatrgoryList.add((String) child.child("categoryName").getValue());
                }
//                ArrayAdapter adapter = new ArrayAdapter(getContext(),
//                        android.R.layout.simple_spinner_item, spinnerCatrgoryList);
//                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                CustomSpinnerAdapter adapter=new CustomSpinnerAdapter(getContext(),spinnerCatrgoryList);
                spinner.setAdapter(adapter);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Log.d("itemSelected", (String) parent.getItemAtPosition(position));
                        String selectedItemText = (String) parent.getItemAtPosition(position);
                        mCategory= (String) parent.getSelectedItem();
                        // If user change the default selection
                        // First item is disable and it is used for hint
                        if (position > 0) {
                            // Notify the selected item text
                            Toast.makeText(getContext(), "Selected : " + selectedItemText, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                //** **
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


        Intent intent = getActivity().getIntent();
        // String shopUUID = intent.getStringExtra("editobj");
        toEdit = (String) intent.getSerializableExtra("uuid");
        //uuidRef = intent.getStringExtra("uuid");

        mFirebaseRef = new Firebase(Constants.FIREBASE_URL);
        mEditTextShopName = (EditText) rootView.findViewById(R.id.shopName);
        mEditTextEmailInput = (EditText) rootView.findViewById(R.id.email);
        mEditTextPasswordInput = (EditText) rootView.findViewById(R.id.password);
        mEditTextcallInput = (EditText) rootView.findViewById(R.id.callContacts);
        mEditTextFbContact = (EditText) rootView.findViewById(R.id.fbContacts);
        mEditTextTwitterContact = (EditText) rootView.findViewById(R.id.twitterContacts);
        addPic = (ImageButton) rootView.findViewById(R.id.addPic);

        addPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(gallery, PICK_IMAGE);
            }
        });

        if (toEdit != null) {
            Firebase shopData = new Firebase(Constants.FIREBASE_URL).child("Shops").child(toEdit);
            shopData.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    email= (String) dataSnapshot.child("shopEmail").getValue();
                    pass= (String) dataSnapshot.child("password").getValue();
                    mEditTextEmailInput.setText((CharSequence) dataSnapshot.child("shopEmail").getValue());
                    mEditTextPasswordInput.setText((CharSequence) dataSnapshot.child("password").getValue());
                    mEditTextShopName.setText((CharSequence) dataSnapshot.child("shopName").getValue());
                    mEditTextcallInput.setText((CharSequence) dataSnapshot.child("phone").getValue());
                    mEditTextFbContact.setText((CharSequence) dataSnapshot.child("fbContact").getValue());
                    mEditTextTwitterContact.setText((CharSequence) dataSnapshot.child("twitterContact").getValue());
                    String uri = (String) dataSnapshot.child("logo").getValue();
                    PicassoClient.downloadImg(getActivity(), uri, addPic);
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }


            });

        }


        mAuthProgressDialog = new ProgressDialog(getContext());
        mAuthProgressDialog.setTitle(getResources().getString(R.string.progress_dialog_loading));
        mAuthProgressDialog.setMessage(getResources().getString(R.string.progress_dialog_creating_user_with_firebase));
        mAuthProgressDialog.setCancelable(false);
        return  rootView;
    }


    public class CustomSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {

        private ArrayList<String> asr;

        public CustomSpinnerAdapter(Context context, ArrayList<String> asr) {
            this.asr = asr;
        }


        public int getCount() {
            return asr.size();
        }

        public Object getItem(int i) {
            return asr.get(i);
        }

        public long getItemId(int i) {
            return (long) i;
        }


        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            TextView txt = new TextView(getContext());
            txt.setPadding(20, 20, 20, 20);
            txt.setTextSize(18);
            txt.setText(asr.get(position));
            txt.setTextColor(Color.parseColor("#000000"));
            return txt;
        }

        public View getView(int i, View view, ViewGroup viewgroup) {
            TextView txt = new TextView(getContext());
            txt.setPadding(20, 20, 20, 20);
            txt.setTextSize(16);
            txt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.r_arrow, 0);
            txt.setText(asr.get(i));
            txt.setTextColor(Color.parseColor("#000000"));
            return txt;
        }

    }
}
