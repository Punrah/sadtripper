package com.tripper.sadtripper.persistence;

/**
 * Created by Startup on 5/30/17.
 */

public class Item {


    public String ItemCode;
    public String Description;
    public String Packing;


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

    public String getPacking() {
        return Packing;
    }

    public void setPacking(String packing) {
        Packing = packing;
    }
}