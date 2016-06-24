package com.example.merna.shopxmall;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.merna.shopxmall.Model.Category;
import com.example.merna.shopxmall.mPicasso.PicassoClient;
import com.example.merna.shopxmall.utils.Constants;
import com.firebase.client.Firebase;
import com.firebase.client.Query;
import com.firebase.ui.FirebaseListAdapter;

/**
 * Created by Merna on 6/23/2016.
 */

public class CategoryAdapter extends FirebaseListAdapter<Category> {
    Activity activity;

    public CategoryAdapter(Activity activity, Class<Category> modelClass, int modelLayout, Query ref) {
        super(activity, modelClass, modelLayout, ref);
        this.activity = activity;
    }

    @Override
    protected void populateView(View v, Category model, final int position) {
        super.populateView(v, model, position);

        ImageView img=(ImageView)v.findViewById(R.id.imgC);
        TextView txt=(TextView)v.findViewById(R.id.txtC);
        ImageView close=(ImageView)v.findViewById(R.id.deleteBtn);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setMessage("Are you sure you want to remove this area ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String itemKey=getRef(position).getKey();
                                Firebase itemRef = new Firebase(Constants.FIREBASE_URL).child("Categories").child(itemKey);
                                itemRef.removeValue();
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

        txt.setText(model.getCategoryName());
        PicassoClient.downloadImg(activity,model.getImgCategory(),img);

    }
}
