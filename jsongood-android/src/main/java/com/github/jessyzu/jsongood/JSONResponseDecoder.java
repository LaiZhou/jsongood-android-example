package com.github.jessyzu.jsongood;

import com.google.gson.Gson;

import java.nio.charset.Charset;


public class JSONResponseDecoder {

    public Gson gson;

    public JSONResponseDecoder() {
    }

    public JSONResponseDecoder(Gson gson) {
        this.gson = gson;
    }

    public RpcResult decode(byte[] payload) {

        byte[] jsonData = decodePayload(payload);
        String jsonStr=new String(jsonData, Charset.forName("UTF-8"));
        RpcResult result = gson.fromJson(jsonStr, RpcResult.class);
        return result;
    }


    public byte[] decodePayload(byte[] payload) {
        //子类可以实现
        return payload;
    }
}
