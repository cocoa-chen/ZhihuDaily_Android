package com.example.chenaibin.zhihudaily;

/**
 * Created by chenaibin on 15/3/24.
 */
public interface HttpCallbackListener {
    void onFinish(String response);
    void onError(Exception e);
}
