package com.paveways.WebResponse;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class GetOrderProductDetails {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("Information")
    @Expose
    private List<Information> information = null;
    @SerializedName("subtotal")
    @Expose
    private String subtotal;
    @SerializedName("shippingfee")
    @Expose
    private String shippingfee;
    @SerializedName("grandtotal")
    @Expose
    private String grandtotal;

    @SerializedName("payment_status")
    @Expose
    private String payment_status;

    public String getPayment_status() {
        return payment_status;
    }

    public void setPayment_status(String payment_status) {
        this.payment_status = payment_status;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<Information> getInformation() {
        return information;
    }

    public void setInformation(List<Information> information) {
        this.information = information;
    }

    public String getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(String subtotal) {
        this.subtotal = subtotal;
    }

    public String getShippingfee() {
        return shippingfee;
    }

    public void setShippingfee(String shippingfee) {
        this.shippingfee = shippingfee;
    }

    public String getGrandtotal() {
        return grandtotal;
    }

    public void setGrandtotal(String grandtotal) {
        this.grandtotal = grandtotal;
    }


    public class Information {

        @SerializedName("prod_id")
        @Expose
        private String prodId;
        @SerializedName("qty")
        @Expose
        private String qty;
        @SerializedName("price")
        @Expose
        private String price;
        @SerializedName("date")
        @Expose
        private String date;
        @SerializedName("prod_name")
        @Expose
        private String prodName;
        @SerializedName("prod_total")
        @Expose
        private String prodTotal;

        public String getProdId() {
            return prodId;
        }

        public void setProdId(String prodId) {
            this.prodId = prodId;
        }

        public String getQty() {
            return qty;
        }

        public void setQty(String qty) {
            this.qty = qty;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getProdName() {
            return prodName;
        }

        public void setProdName(String prodName) {
            this.prodName = prodName;
        }

        public String getProdTotal() {
            return prodTotal;
        }

        public void setProdTotal(String prodTotal) {
            this.prodTotal = prodTotal;
        }

    }
}