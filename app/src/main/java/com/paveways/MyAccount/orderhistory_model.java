package com.paveways.MyAccount;

public class orderhistory_model {

    private String orderid, securecode, price, date, name, phone,email;

    public orderhistory_model( String orderid, String shipping, String price, String date, String name, String email,String phone){

        this.orderid = orderid;
        this.securecode = shipping;
        this.price = price;
        this.date = date;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
