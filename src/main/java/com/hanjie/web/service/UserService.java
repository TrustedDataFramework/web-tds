package com.hanjie.web.service;

import com.hanjie.web.entity.Account;
import com.hanjie.web.entity.Organ;
import com.hanjie.web.entity.TxHash;
import com.hanjie.web.entity.User;

import java.util.Date;
import java.util.List;


public interface UserService {
    //登录
    Account loginByName_1(String address);

    Account registerByName(String userName);

    boolean addAccount(byte[] address,String username,int role,String organ,int status);

    List<Account> auditUser(int pageSize,int pageIndex);

    List<Account> auditUser_2(int pageSize,int pageIndex);

    int auditCount();

    Organ selectOrganName(String organ);

    boolean auditUserRegisterSuccess(String address);

    boolean auditUserRegisterFail(String address);

    boolean addHash(byte[] address, String hash, int status, String timestamp);

}
