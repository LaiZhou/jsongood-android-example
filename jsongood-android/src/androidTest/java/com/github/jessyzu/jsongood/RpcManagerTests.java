package com.github.jessyzu.jsongood;

import android.os.Looper;
import android.test.InstrumentationTestCase;
import android.util.Log;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class RpcManagerTests extends InstrumentationTestCase {

    public void testRpc() throws Exception {


        final CountDownLatch signal = new CountDownLatch(1);

        RpcManager manager = RpcManager.managerWithURL("http://30.10.162.97:8080/api");
        Map<String, String> customHttpHeaders = new HashMap<String, String>();
        customHttpHeaders.put("platform", "android");
        manager.customHttpHeaders = customHttpHeaders;
        Map<String, Object> attachments = new HashMap<String, Object>();
        attachments.put("rpcVersion", "1.0");
        manager.attachments = attachments;

        Param p1 = new Param();
        Param p2 = new Param();
        p1.setIntParam(1);
        p1.setDoubleParam(0.01);
        List<String> list = new ArrayList<String>();
        list.add("a");
        Map<String, String> map = new HashMap<String, String>();
        map.put("a", "b");
        p1.setListParam(list);
        p1.setMapParam(map);

        p2.setIntParam(2);
        p2.setDoubleParam(0.01);
        List<String> list1 = new ArrayList<String>();
        list1.add("a");
        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("a", "b");
        p2.setListParam(list1);
        p2.setMapParam(map1);
        p1.setParam(p2);

        manager.invoke("com.github.jessyZu.jsongood.demo.api.DemoService:sayHello1:1.0.0", new Object[]{p1, p2, new Param[]{p1, null}}, new RpcCallback() {
            @Override
            public void success(RpcResult result) {
                if (result.isSuccess()) {
                    Param data = result.dataToObject(Param.class);
                    boolean isMainThread = Looper.myLooper() == Looper.getMainLooper();
                    Log.i(RPCConstants.JSONGOOD_LOG_FLAG, data.toString());

                }
                signal.countDown();
            }

            @Override
            public void failure(Request request, Response response, IOException e) {
                signal.countDown();
                fail();
            }
        });

        signal.await(120, TimeUnit.SECONDS);
    }
}
