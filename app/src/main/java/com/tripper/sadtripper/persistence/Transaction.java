package com.tripper.sadtripper.persistence;

/**
 * Created by Startup on 5/30/17.
 */

public class Transaction {

    public String TransactionName;
    public String Date;
    public String idWebsol;
    public String FarmerCode;
    public String FarmerName;
    public String FieldCode;
    public String ItemCode;
    public String Description;
    public String Price;
    public String Qty;
    public String Packing;
    public String NoPo;
    public String lotnumber;


    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getFarmerCode() {
        return FarmerCode;
    }

    public void setFarmerCode(String farmerCode) {
        FarmerCode = farmerCode;
    }

    public String getIdWebsol() {
        return idWebsol;
    }

    public void setIdWebsol(String idWebsol) {
        this.idWebsol = idWebsol;
    }

    public String getFarmerName() {
        return FarmerName;
    }

    public void setFarmerName(String farmerName) {
        FarmerName = farmerName;
    }

    public String getFieldCode() {
        return FieldCode;
    }

    public void setFieldCode(String fieldCode) {
        FieldCode = fieldCode;
    }

    public String getItemCode() {
        return ItemCode;
    }

    public void setItemCode(String itemCode) {
        ItemCode = itemCode;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getQty() {
        return Qty;
    }

    public void setQty(String qty) {
        Qty = qty;
    }

    public String getPacking() {
        return Packing;
    }

    public void setPacking(String packing) {
        Packing = packing;
    }

    public String getNoPo() {
        return NoPo;
    }

    public void setNoPo(String noPo) {
        NoPo = noPo;
    }

    public String getLotnumber() {
        return lotnumber;
    }

    public void setLotnumber(String lotnumber) {
        this.lotnumber = lotnumber;
    }
}