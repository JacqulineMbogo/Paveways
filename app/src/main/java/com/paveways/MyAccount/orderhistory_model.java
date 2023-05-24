package com.paveways.MyAccount;

public class orderhistory_model {

    private String orderid, securecode, price, date;

    public orderhistory_model( String orderid, String shipping, String price, String date){

        this.orderid = orderid;
        this.securecode = shipping;
        this.price = price;
        this.date = date;
    }

    public String getorder_id(){ return orderid;}
    public void setorder_id(String id){ this.orderid = id;}

    public String getsecurecode(){ return securecode;}
    public void setshippingaddress(String name){ this.securecode = name;}


    public String getprice(){ return price;}
    public void setprice(String price){ this.price = price;}

    public String getdate(){ return date;}
    public void setdate(String date){ this.date = date;}

}
