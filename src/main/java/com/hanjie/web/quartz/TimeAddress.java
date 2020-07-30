package com.hanjie.web.quartz;

import com.alibaba.fastjson.JSONObject;
import com.hanjie.web.dao.UserDao;
import com.hanjie.web.entity.TxHash;
import lombok.extern.slf4j.Slf4j;
import org.sdk.util.HttpclientUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
public class TimeAddress {

    @Autowired
    UserDao userDao;

    @Scheduled(fixedDelay = 1000 * 120) // 上一次开始执行时间点之后2分钟再执行
    public void selectRPCbyHash() throws Exception {
        List<TxHash> list = userDao.selectByHash();
        log.info("审核待广播成功============="+list.size()+"============");
        for(int i = 0 ;i<list.size() ; i++){
            String result = HttpclientUtil.sendGet("http://192.168.1.167:7010/rpc/transaction/"+list.get(i).getHash(),"");
            JSONObject json_result = JSONObject.parseObject(result);
            if(json_result.getInteger("code") == 200) {
                userDao.userTxHashSuccaerrBroad(list.get(i).getAddress());
                userDao.auditUserRegisterSuccessAndBroad(list.get(i).getAddress());
                log.info("=============广播成功============"+list.get(i).getHash());
            }else{
               Thread.sleep(1000*30);
            }
        }
    }
}
