package com.github.jessyzu.jsongood;

import android.util.Log;

import com.google.gson.Gson;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class RpcManager extends OkHttpClient {

    public String url;
    public Map<String, Object> attachments;
    public Map<String, String> customHttpHeaders;

    public JSONRequestEncoder requestEncoder;
    public JSONResponseDecoder responseDecoder;

    public static RpcManager managerWithURL(String url) {
        RpcManager manager = new RpcManager();
        manager.url = url;
        Gson requestGson = new Gson();
        Gson responseGson = new Gson();

        manager.requestEncoder = new JSONRequestEncoder(requestGson);
        manager.responseDecoder = new JSONResponseDecoder(responseGson);
        return manager;
    }

    public void invoke(String methodEndPoint, Object[] parameters, RpcCallback rpcCallback) {
        invoke(methodEndPoint, parameters, null, rpcCallback);

    }

    public void invoke(String methodEndPoint, Object[] parameters, Map<String, Object> attachments, final RpcCallback rpcCallback) {
        final MediaType contentType
                = MediaType.parse("application/json; charset=utf-8");

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("methodEndPoint", methodEndPoint);
        data.put("parameters", parameters);
        Map<String, Object> mergedAttachments = new HashMap<String, Object>();
        if (this.attachments != null) {
            mergedAttachments.putAll(this.attachments);

        }
        if (attachments != null) {
            mergedAttachments.putAll(attachments);

        }
        data.put("attachments", mergedAttachments);
        //put unique requestId
        String requestId= new StringBuilder(methodEndPoint).append(System.currentTimeMillis()+"").append(new Random().nextInt()+"").toString();
        data.put("requestId", requestId);

        byte[] payload = this.requestEncoder.encode(data);
        RequestBody body = RequestBody.create(contentType, payload);
        Request.Builder requestBuilder = new Request.Builder();

        if (this.customHttpHeaders != null) {
            for (Map.Entry<String, String> header : this.customHttpHeaders.entrySet()) {
                requestBuilder.addHeader(header.getKey(), header.getValue());
            }
        }
        final Request request = requestBuilder.url(this.url)
                .post(body)
                .build();
        Call call = this.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

                Log.d(RPCConstants.JSONGOOD_LOG_FLAG, request.toString());
                if (e != null) {
                    Log.d(RPCConstants.JSONGOOD_LOG_FLAG, "IOException", e);
                }
                rpcCallback.failure(request, null, e);

            }

            @Override
            public void onResponse(Response response) throws IOException {

                if (response.isSuccessful()) {
                    Log.d(RPCConstants.JSONGOOD_LOG_FLAG, response.toString());
                    RpcResult rpcResult=responseDecoder.decode(response.body().bytes());
                    rpcCallback.success(rpcResult);

                } else {
                    rpcCallback.failure(request, response, null);
                }

            }
        });
    }

}
