package com.github.jessyzu.jsongood;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;


public interface RpcCallback {

    void success(RpcResult result);

    void failure(Request request,Response response,IOException e);
}
