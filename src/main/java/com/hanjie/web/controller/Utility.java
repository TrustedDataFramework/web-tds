package com.hanjie.web.controller;

import com.alibaba.fastjson.JSONObject;
import org.sdk.util.HttpclientUtil;

public class Utility {
    public static int getNonce(String pubkeyOraddress){
        String result = HttpclientUtil.sendGet("http://192.168.1.167:7010/rpc/account/"+pubkeyOraddress,"");
        JSONObject json_result = JSONObject.parseObject(result);
        JSONObject json_data = (JSONObject) json_result.get("data");
        return (int) json_data.get("nonce");
    }
}
