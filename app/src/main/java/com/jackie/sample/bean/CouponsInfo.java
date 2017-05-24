package com.jackie.sample.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/12/15.
 */
public class CouponsInfo implements Serializable {
    String totalPriceDiscount;
    String shopFreeExpIds;
    String activeCoupon;
    String giftCard;
    String activeCouponTotalValue;
    String couponsCount;
    String activeCouponUsedValue;
    public String getTotalPriceDiscount() {
        return totalPriceDiscount;
    }

    public void setTotalPriceDiscount(String totalPriceDiscount) {
        this.totalPriceDiscount = totalPriceDiscount;
    }

    public String getGiftCard() {
        return giftCard;
    }

    public void setGiftCard(String giftCard) {
        this.giftCard = giftCard;
    }



    public String getCouponsCount() {
        return couponsCount;
    }

    public void setCouponsCount(String couponsCount) {
        this.couponsCount = couponsCount;
    }

    public String getShopFreeExpIds() {
        return shopFreeExpIds;
    }

    public void setShopFreeExpIds(String shopFreeExpIds) {
        this.shopFreeExpIds = shopFreeExpIds;
    }

    public String getActiveCoupon() {
        return activeCoupon;
    }

    public void setActiveCoupon(String activeCoupon) {
        this.activeCoupon = activeCoupon;
    }

    public String getActiveCouponTotalValue() {
        return activeCouponTotalValue;
    }

    public void setActiveCouponTotalValue(String activeCouponTotalValue) {
        this.activeCouponTotalValue = activeCouponTotalValue;
    }

    public String getActiveCouponUsedValue() {
        return activeCouponUsedValue;
    }

    public void setActiveCouponUsedValue(String activeCouponUsedValue) {
        this.activeCouponUsedValue = activeCouponUsedValue;
    }
}
