package com.example.merna.shopxmall.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Merna on 5/19/2016.
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class Area {

    String imgArea;
    String areaName;
    String uuidBeacon;
    String major;
    String manor;

    public Area() {
    }

    public Area(String areaName, String uuidBeacon, String imgArea) {
        this.areaName = areaName;
        this.uuidBeacon = uuidBeacon;
        this.imgArea = imgArea;
    }

    public Area(String areaName, String uuidBeacon, String major, String manor) {
        this.areaName = areaName;
        this.uuidBeacon = uuidBeacon;
        this.major = major;
        this.manor = manor;
    }

    public String getImgArea() {
        return imgArea;
    }

    public String getUuidBeacon() {
        return uuidBeacon;
    }

    public String getAreaName() {
        return areaName;
    }

    public String getMajor() {
        return major;
    }

    public String getManor() {
        return manor;
    }
}
