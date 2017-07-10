/*
 * Copyright 2015 Yan Zhenjie
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jackie.sample.framework.nohttp;

import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;

/**
 * Created in Nov 4, 2015 12:02:55 PM.
 *
 * @author Yan Zhenjie.
 */
public class HttpResponseListener<T> implements OnResponseListener<T> {
    /**
     * Request.
     */
    private Request<?> mRequest;
    /**
     * 结果回调.
     */
    private HttpListener<T> callback;

    /**
     * @param request      请求对象.
     * @param httpCallback 回调对象.
     */
    public HttpResponseListener(Request<?> request, HttpListener<T> httpCallback) {
        this.mRequest = request;
        this.callback = httpCallback;
    }

    /**
     * 开始请求, 这里显示一个dialog.
     */
    @Override
    public void onStart(int what) {

    }

    /**
     * 结束请求, 这里关闭dialog.
     */
    @Override
    public void onFinish(int what) {

    }

    /**
     * 成功回调.
     */
    @Override
    public void onSucceed(int what, Response<T> response) {
        int responseCode = response.getHeaders().getResponseCode();
        if (responseCode > 400) {
            if (responseCode == 405) {// 405表示服务器不支持这种请求方法，比如GET、POST、TRACE中的TRACE就很少有服务器支持。

            } else {// 但是其它400+的响应码服务器一般会有流输出。

            }
        }
        if (callback != null) {
            callback.onSucceed(what, response);
        }
    }

    /**
     * 失败回调.
     */
    @Override
    public void onFailed(int what, Response<T> response) {
//        Exception exception = response.getException();
//        if (exception instanceof NetworkError) {// 网络不好
//            Snackbar.show(mActivity, R.string.error_please_check_network);
//        } else if (exception instanceof TimeoutError) {// 请求超时
//            Snackbar.show(mActivity, R.string.error_timeout);
//        } else if (exception instanceof UnKnownHostError) {// 找不到服务器
//            Snackbar.show(mActivity, R.string.error_not_found_server);
//        } else if (exception instanceof URLError) {// URL是错的
//            Snackbar.show(mActivity, R.string.error_url_error);
//        } else if (exception instanceof NotFoundCacheError) {
//            // 这个异常只会在仅仅查找缓存时没有找到缓存时返回
//            Snackbar.show(mActivity, R.string.error_not_found_cache);
//        } else if (exception instanceof ProtocolException) {
//            Snackbar.show(mActivity, R.string.error_system_unsupport_method);
//        } else if (exception instanceof ParseError) {
//            Snackbar.show(mActivity, R.string.error_parse_data_error);
//        } else {
//            Snackbar.show(mActivity, R.string.error_unknow);
//        }
//        Logger.e("错误：" + exception.getMessage());
        if (callback != null)
            callback.onFailed(what, response);
    }

}
