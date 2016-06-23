package com.example.merna.shopxmall;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.merna.shopxmall.Model.Category;
import com.example.merna.shopxmall.mPicasso.PicassoClient;
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
    protected void populateView(View v, Category model, int position) {
        super.populateView(v, model, position);

        ImageView img=(ImageView)v.findViewById(R.id.imgC);
        TextView txt=(TextView)v.findViewById(R.id.txtC);


        txt.setText(model.getCategoryName());
        PicassoClient.downloadImg(activity,model.getImgCategory(),img);
    }
}
