package com.jackie.sample.utils;


import com.jackie.sample.bean.CommentData;
import com.jackie.sample.bean.ShoppingCar;
import com.squareup.okhttp.ResponseBody;

import java.util.HashMap;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;

/**
 * Created by Jackie on 2016/6/3.
 */
public class HttpUtils {
    public static final String BASE_URL_1 = "http://develop.toomao.com:9000";
    public static final String BASE_URL_2 = "https://api.toomao.com";

    public interface GetDataService {
        // 加请求头
        @Headers({
                // 测试环境token：cd13bcc90a2e432ca42630dc5a96e7ad
                // 正式环境token https: 55a47a5d0cf2a07ccfa49d0a
                "X-AVOSCloud-Session-Token:55a47a5d0cf2a07ccfa49d0a",
                "User-Agent: toomao/2.4.2 NetType/wifi",
                "x-client-id:dv_kandaoqinghulue,sy_qinghulue",
                "x-channel:360"
        })
        // 请求购物车数据，同步请求，接口里面的方法都必须有返回值，不能是void
        @GET("/1.1/cart/show")
        Call<ShoppingCar> get1_1Data();


        @Headers({
                // 测试环境token：cd13bcc90a2e432ca42630dc5a96e7ad
                // 正式环境token https: 55a47a5d0cf2a07ccfa49d0a
                "X-AVOSCloud-Session-Token:55a47a5d0cf2a07ccfa49d0a",
                "User-Agent: toomao/2.4.2 NetType/wifi",
                "x-client-id:dv_kandaoqinghulue,sy_qinghulue",
                "x-channel:360"
        })
        // post添加评论
        @POST("/1.1/activities/{active}/addcomments")
        Call<CommentData> addComment(@Path("active") String active, @Body HashMap<String, String> data);


        @Headers({
                // 测试环境token：cd13bcc90a2e432ca42630dc5a96e7ad
                // 正式环境token https: 55a47a5d0cf2a07ccfa49d0a
                "X-AVOSCloud-Session-Token:cd13bcc90a2e432ca42630dc5a96e7ad",
                "User-Agent: toomao/2.5.1 NetType/wifi",
                "x-client-id:dv_kandaoqinghulue,sy_qinghulue",
                "apikey:64ee55183b48d3971bce76495de9d20c",
                "x-channel:360"
        })
        // put给活动点赞
        @PUT("/1.1/activities/{activeid}/praise")
        Call<ResponseBody> zanActive(@Path("activeid") String activeid);


        @Headers({
                // 测试环境token：cd13bcc90a2e432ca42630dc5a96e7ad
                // 正式环境token https: 55a47a5d0cf2a07ccfa49d0a
                "X-AVOSCloud-Session-Token:cd13bcc90a2e432ca42630dc5a96e7ad",
                "User-Agent: toomao/2.5.1 NetType/wifi",
                "x-client-id:dv_kandaoqinghulue,sy_qinghulue",
                "apikey:64ee55183b48d3971bce76495de9d20c",
                "x-channel:360"
        })
        // put给活动点赞
        @DELETE("/1.1/orders/{orderId}")
        Call<ResponseBody> delOrder(@Path("orderId") String orderId);
    }
}