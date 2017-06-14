package com.jackie.sample.gmail.network;

import com.jackie.sample.gmail.bean.Message;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;


/**
 * Created by Jackie on 2017/6/13.
 */

public interface ApiInterface {
    @GET("inbox.json")
    Call<List<Message>> getInbox();
}
