package com.example.merna.shopxmall.ui;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.cloudinary.Cloudinary;
import com.example.merna.shopxmall.AreaAdapter;
import com.example.merna.shopxmall.CategoryAdapter;
import com.example.merna.shopxmall.Model.Area;
import com.example.merna.shopxmall.Model.Category;
import com.example.merna.shopxmall.R;
import com.example.merna.shopxmall.utils.Constants;
import com.firebase.client.Firebase;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * A placeholder fragment containing a simple view.
 */
public class AddCategoryFragment extends Fragment {

    GridView gridCategory;
    CategoryAdapter categoryAdapter;
    ImageButton imgC;
    EditText eTxtC;
    Button btnC;
    Cloudinary cloudinary;
    InputStream in;
    String imageFile;
    Uri cImg;

    public AddCategoryFragment() {
    }

    public String generatePIN() {
        int randomPIN = (int) (Math.random() * 9000) + 1000;
        return String.valueOf(randomPIN);
    }

    public void uploadImg(Uri img) {
        String Generated_Id = generatePIN();
        final Map<String, String> options = new HashMap<>();
        options.put("public_id", Generated_Id);

        try {
            in = getActivity().getContentResolver().openInputStream(img);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            cImg = data.getData();
            imgC.setImageURI(cImg);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_add_category, container, false);
        imgC=(ImageButton) rootView.findViewById(R.id.imgCategory);
        eTxtC=(EditText)rootView.findViewById(R.id.etxtNameCategory);
        btnC=(Button) rootView.findViewById(R.id.addCategory);

        Map config = new HashMap();
        config.put("cloud_name", "gp");
        config.put("api_key", "667862958976234");
        config.put("api_secret", "zAQ9orjld73mDil8fFsdDNXUQrg");
        cloudinary = new Cloudinary(config);

        gridCategory = (GridView) rootView.findViewById(R.id.gridviewCategory);
        Firebase CategoryRef = new Firebase(Constants.FIREBASE_URL).child("Categories");
        categoryAdapter = new CategoryAdapter(getActivity(), Category.class, R.layout.category_item, CategoryRef);
        gridCategory.setAdapter(categoryAdapter);


        imgC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent imgFrom = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(imgFrom, 100);
            }
        });

        btnC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=eTxtC.getText().toString();
                if (TextUtils.isEmpty(name)) {
                    eTxtC.setError("Please Enter Name!");
                }else {

                    if (cImg != null) {
                        uploadImg(cImg);
                    }

                    Firebase categoryRef = new Firebase(Constants.FIREBASE_URL).child("Categories").child(name);
                    Category newCategory = new Category(name, imageFile);
                    categoryRef.setValue(newCategory);
                    Toast.makeText(getContext(), "category added", Toast.LENGTH_LONG);
                }
            }
        });

        return rootView;
    }
}
