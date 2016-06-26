package com.example.merna.shopxmall.Login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.merna.shopxmall.R;
import com.example.merna.shopxmall.ui.MainActivity;
import com.example.merna.shopxmall.utils.Constants;
import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;


/**
 * A placeholder fragment containing a simple view.
 */
public class LoginActivityFragment extends Fragment {

    Firebase mFirebaseRef;
    ProgressDialog mAuthProgressDialog;
    SharedPreferences preferences;
    Button LoginBtn;
    EditText mEditTextEmailInput, mEditTextPasswordInput;
    String type;
    Context cn;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        cn=  context;
    }

    public LoginActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);

        mFirebaseRef = new Firebase(Constants.FIREBASE_URL);
        mEditTextEmailInput = (EditText) rootView.findViewById(R.id.edit_text_email);
        mEditTextPasswordInput = (EditText) rootView.findViewById(R.id.edit_text_password);
        LoginBtn = (Button) rootView.findViewById(R.id.login_with_password);
        mAuthProgressDialog = new ProgressDialog(getContext());
        mAuthProgressDialog.setTitle(getString(R.string.progress_dialog_loading));
        mAuthProgressDialog.setMessage(getString(R.string.progress_dialog_authenticating_with_firebase));
        mAuthProgressDialog.setCancelable(false);

        mFirebaseRef.addAuthStateListener(new Firebase.AuthStateListener() {
            @Override
            public void onAuthStateChanged(AuthData authData) {
                Log.d("authdata in login", String.valueOf(authData));
                if (authData != null) {
                    // user is logged in
                    Intent i = new Intent(cn, MainActivity.class);
                    startActivity(i);
                } else {
                    // user is not logged in
                    LoginBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            signInPassword();
                        }
                    });

                    /**
                     * Call signInPassword() when user taps "Done" keyboard action
                     */
                    mEditTextPasswordInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                        @Override
                        public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {

                            if (actionId == EditorInfo.IME_ACTION_DONE || keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                                signInPassword();
                            }
                            return true;
                        }
                    });
                }
            }
        });


        return rootView;

    }


    public void signInPassword() {
        String email = mEditTextEmailInput.getText().toString();
        String password = mEditTextPasswordInput.getText().toString();
        /**
         *
         * If email and password are not empty show progress dialog and try to authenticate
         */
        if (email.equals("")) {
            mEditTextEmailInput.setError(getString(R.string.error_cannot_be_empty));
            return;
        }

        if (password.equals("")) {
            mEditTextPasswordInput.setError(getString(R.string.error_cannot_be_empty));
            return;
        }
        mAuthProgressDialog.show();
        mFirebaseRef.authWithPassword(email, password, new MyAuthResultHandler(Constants.PASSWORD_PROVIDER));
    }


    private class MyAuthResultHandler implements Firebase.AuthResultHandler {

        private final String provider;

        public MyAuthResultHandler(String provider) {
            this.provider = provider;
        }

        /**
         * On successful authentication call setAuthenticatedUser if it was not already
         * called in
         */
        @Override
        public void onAuthenticated(AuthData authData) {
            mAuthProgressDialog.dismiss();
            Log.i("LOG", provider + " " + getString(R.string.log_message_auth_successful));

            if (authData != null) {
                /* Go to main activity */
                String uid = authData.getUid();
                Firebase userRrf = new Firebase(Constants.FIREBASE_URL_USERS).child(uid);
                userRrf.addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        type=(String)dataSnapshot.child("type").getValue();
                        Log.e("testtype",type);
                        if (type.equals("Mall")) {
//                            SharedPreferences.Editor editor = preferences.edit();
//                            editor.putString("login", "yes");
//                            editor.commit();
                            Intent intent = new Intent(getContext(), MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        } else {
                            showErrorToast("you'r email can't login as a mallOwner");
                        }
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });
            }
        }

        @Override
        public void onAuthenticationError(FirebaseError firebaseError) {
            mAuthProgressDialog.dismiss();

            /**
             * Use utility method to check the network connection state
             * Show "No network connection" if there is no connection
             * Show Firebase specific error message otherwise
             */
            switch (firebaseError.getCode()) {
                case FirebaseError.INVALID_EMAIL:
                case FirebaseError.USER_DOES_NOT_EXIST:
                    mEditTextEmailInput.setError(getString(R.string.error_message_email_issue));
                    break;
                case FirebaseError.INVALID_PASSWORD:
                    mEditTextPasswordInput.setError(firebaseError.getMessage());
                    break;
                case FirebaseError.NETWORK_ERROR:
                    showErrorToast(getString(R.string.error_message_failed_sign_in_no_network));
                    break;
                default:
                    showErrorToast(firebaseError.toString());
            }
        }

        private void showErrorToast(String message) {
            Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
        }

    }


}
