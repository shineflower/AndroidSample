package com.jackie.sample.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class CarItem implements Serializable {
    String shopName;
    String shopId;
    String hxid;
    String logo;
    String shopStatus;
    ArrayList<String> contact;
    ArrayList<CarGoods> items;
    String shopFreeExp;
    String shopFreeExpBtn;
    // calculate接口新增字段
    String totalFee; // 购买一家店里的总价格
    String totalPriceOrig; // 总原价
    String expressPrice; // 邮费
    ArrayList<ErrMsg> errors; // 错误信息

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public ArrayList<CarGoods> getItems() {
        return items;
    }

    public void setItems(ArrayList<CarGoods> items) {
        this.items = items;
    }

    public String getShopStatus() {
        return shopStatus;
    }

    public void setShopStatus(String shopStatus) {
        this.shopStatus = shopStatus;
    }

    public String getHxid() {
        return hxid;
    }

    public void setHxid(String hxid) {
        this.hxid = hxid;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public ArrayList<String> getContact() {
        return contact;
    }

    public void setContact(ArrayList<String> contact) {
        this.contact = contact;
    }

    public String getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(String totalFee) {
        this.totalFee = totalFee;
    }

    public String getTotalPriceOrig() {
        return totalPriceOrig;
    }

    public void setTotalPriceOrig(String totalPriceOrig) {
        this.totalPriceOrig = totalPriceOrig;
    }

    public String getExpressPrice() {
        return expressPrice;
    }

    public void setExpressPrice(String expressPrice) {
        this.expressPrice = expressPrice;
    }

    public ArrayList<ErrMsg> getErrors() {
        return errors;
    }

    public void setErrors(ArrayList<ErrMsg> errors) {
        this.errors = errors;
    }

    public String getShopFreeExp() {
        return shopFreeExp;
    }

    public void setShopFreeExp(String shopFreeExp) {
        this.shopFreeExp = shopFreeExp;
    }

    public String getShopFreeExpBtn() {
        return shopFreeExpBtn;
    }

    public void setShopFreeExpBtn(String shopFreeExpBtn) {
        this.shopFreeExpBtn = shopFreeExpBtn;
    }
}
