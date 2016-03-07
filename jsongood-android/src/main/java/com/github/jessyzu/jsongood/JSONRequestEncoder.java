package com.github.jessyzu.jsongood;

import com.google.gson.Gson;

import java.nio.charset.Charset;
import java.util.Map;


public class JSONRequestEncoder {

    public Gson gson;

    public JSONRequestEncoder() {
    }

    public JSONRequestEncoder(Gson gson) {
        this.gson = gson;
    }

    public byte[] encode(Map<String, Object> data) {

        String jsonData = gson.toJson(data);
        byte[] result = encodePayload(jsonData.getBytes(Charset.forName("UTF-8")));
        return result;
    }


    public byte[] encodePayload(byte[] payload) {
        //子类可以实现
        return payload;
    }
}
