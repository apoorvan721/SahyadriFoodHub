package com.example.sahyadrifoodhub;

import java.io.Serializable;

public class ModelClass implements Serializable {

    String breakfastname ,breakfastprice, breakfastselect ;
    int breakfastdp;

    public ModelClass(String breakfastname, String breakfastprice, String breakfastselect, int breakfastdp) {
        this.breakfastname = breakfastname;
        this.breakfastprice = breakfastprice;
        this.breakfastselect = breakfastselect;
        this.breakfastdp = breakfastdp;
    }
}

