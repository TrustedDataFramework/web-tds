package com.hanjie.web.controller;

import org.bouncycastle.util.encoders.Hex;
import org.sdk.keystore.KeystoreUtility;
import org.tdf.common.util.HexBytes;
import org.tdf.crypto.keystore.KeyStoreImpl;
import org.tdf.crypto.keystore.SMKeystore;

public class APIResponse<T> {
    private static final String CODE_SUCCESS = "success";

    private static final String CODE_FAIL = "fail";

    private String code;
    private T data;
    private String msg;

    public APIResponse(){

    }

    public APIResponse(String code){
        this.code = code;
    }

    public APIResponse(String code, T data){
        this.code = code;
        this.data = data;
    }

    public APIResponse(String code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public static APIResponse success(){
        return new APIResponse(CODE_SUCCESS);
    }

    public static APIResponse success(Object data){
        return new APIResponse(CODE_SUCCESS, data);
    }

    public static APIResponse fail(String msg){
        return new APIResponse(CODE_FAIL, msg);
    }

    public static APIResponse widthCode(String errorCode) {
        return new APIResponse(errorCode);
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
