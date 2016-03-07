package com.github.jessyzu.jsongood;

import com.google.gson.Gson;

import java.util.Map;

public class RpcResult {

    /**
     *
     */
    private static final long serialVersionUID = -2940045837162445419L;

    private static final  Gson gson=new Gson();

    private int code;
    private String message;
    private Object data;


    public RpcResult() {
        super();

    }

    public RpcResult(int code, String message) {
        super();
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return this.code == 1000;
    }

    @Override
    public String toString() {
        return "RpcResult [code=" + code + ", message=" + message + ", data="
                + data + "]";
    }

    public <T> T dataToObject(Class<T> clazz) {
        if (data instanceof Map) {
            T clazzData = gson.fromJson(gson.toJson(data),
                    clazz);
            return clazzData;
        } else if (data.getClass().equals(clazz)) {
            return (T) data;
        } else {
            return null;
        }
    }

}
