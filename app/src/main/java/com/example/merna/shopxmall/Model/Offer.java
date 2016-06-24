package com.example.merna.shopxmall.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by Merna on 6/23/2016.
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class Offer implements Serializable {

    String Description;
    String Title;
    String offerImage;
    String DiscountBefore;
    String DiscountAfter;
    String status;

    public Offer() {
    }

    public String getDescription() {
        return Description;
    }

    public String getDiscountAfter() {
        return DiscountAfter;
    }

    public String getDiscountBefore() {
        return DiscountBefore;
    }

    public String getOfferImage() {
        return offerImage;
    }

    public String getStatus() {
        return status;
    }

    public String getTitle() {
        return Title;
    }
}
