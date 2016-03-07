package com.github.jessyzu.jsongood;

import android.test.InstrumentationTestCase;
import android.util.Log;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.util.concurrent.TimeUnit;


public class OkHttpTests extends InstrumentationTestCase{


    public void testPost() throws  Exception{
         final MediaType JSON
                = MediaType.parse("application/json; charset=utf-8");

        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(3, TimeUnit.SECONDS);
        RequestBody body = RequestBody.create(JSON, "1");
        Request request = new Request.Builder()
                .url("http://30.10.162.97:8080/api")
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        Log.i(RPCConstants.JSONGOOD_LOG_FLAG,response.body().string());
    }
}
