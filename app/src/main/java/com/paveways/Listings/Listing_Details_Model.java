package com.paveways.Listings;

public class Listing_Details_Model {

    private String l_id, l_name, img_ulr,l_price,l_stock ;

    public Listing_Details_Model(String l_id, String l_name, String img_ulr, String l_price, String l_stock) {
        this.l_id = l_id;
        this.l_name = l_name;
        this.img_ulr = img_ulr;
        this.l_price = l_price;
        this.l_stock = l_stock;
    }

    public String getL_id() {
        return l_id;
    }

    public void setL_id(String l_id) {
        this.l_id = l_id;
    }

    public String getL_name() {
        return l_name;
    }

    public void setL_name(String l_name) {
        this.l_name = l_name;
    }

    public String getImg_ulr() {
        return img_ulr;
    }

    public void setImg_ulr(String img_ulr) {
        this.img_ulr = img_ulr;
    }

    public String getL_price() {
        return l_price;
    }

    public void setL_price(String l_price) {
        this.l_price = l_price;
    }

    public String getL_stock() {
        return l_stock;
    }

    public void setL_stock(String l_stock) {
        this.l_stock = l_stock;
    }
}
