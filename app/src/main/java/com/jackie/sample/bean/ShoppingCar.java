package com.jackie.sample.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Administrator on 2015/8/29.
 */
public class ShoppingCar implements Serializable {
    ArrayList<CarItem> results;
    CouponsInfo other;
    String count;

    public ArrayList<CarItem> getResults() {
        return results;
    }

    public void setResults(ArrayList<CarItem> results) {
        this.results = results;
    }

    public CouponsInfo getOther() {
        return other;
    }

    public void setOther(CouponsInfo other) {
        this.other = other;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}
