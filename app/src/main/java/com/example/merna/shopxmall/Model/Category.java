package com.example.merna.shopxmall.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Merna on 5/19/2016.
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class Category {

    String imgCategory;
    String categoryName;

    public Category() {
    }

    public Category( String imgCategory) {
        this.imgCategory = imgCategory;
    }

    public Category(String categoryName, String imgCategory) {
        this.categoryName = categoryName;
        this.imgCategory = imgCategory;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getImgCategory() {
        return imgCategory;
    }
}
