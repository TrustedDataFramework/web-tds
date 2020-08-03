package com.hanjie.web.service.impl;

import com.hanjie.web.dao.UserDao;
import com.hanjie.web.entity.Account;
import com.hanjie.web.entity.Organ;
import com.hanjie.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao userDao;

    @Override
    public Account loginByName_1(String address) {
        return userDao.userLogin_1(address);
    }

    @Override
    public Account registerByName(String userName) {
        return userDao.registerByName(userName);
    }

    @Override
    public boolean addAccount(byte[] address, String username, int role, String organ, int status) {
        return userDao.addAccount(address,username,role,organ,status);
    }

    @Override
    public List<Account> auditUser(int pageSize,int pageIndex) {
        return userDao.auditUser(pageSize,pageIndex);
    }

    @Override
    public List<Account> auditUser_2(int pageSize,int pageIndex) {
        return userDao.auditUser_2(pageSize,pageIndex);
    }

    @Override
    public int auditCount(){
        return userDao.auditCount();
    };

    @Override
    public int userCount(){
        return userDao.userCount();
    };

    @Override
    public Organ selectOrganName(String organ) {
        return userDao.selectOrganName(organ);
    }

    @Override
    public boolean auditUserRegisterSuccess(String address) {
        return userDao.auditUserRegisterSuccess(address);
    }

    @Override
    public boolean auditUserRegisterFail(String address) {
        return userDao.auditUserRegisterFail(address);
    }

    @Override
    public boolean addHash(byte[] address, String hash, int status, String timestamp) {
        return userDao.addHash(address,hash,status,timestamp);
    }

    public boolean updateUser(String username ,int role ,int organ ,String address){
        return userDao.updateUser(username,role,organ,address);
    }

}
