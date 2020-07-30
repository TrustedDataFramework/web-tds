package com.hanjie.web.dao;


import com.hanjie.web.entity.Account;
import com.hanjie.web.entity.Organ;
import com.hanjie.web.entity.TxHash;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserDao  {
    //登录
    @Select("select * from account where address = #{address}")
    Account userLogin_1(@Param("address") String address);

    //用户注册
    @Insert("insert into account (address,username,role,organ,status) values (#{address}, #{username},#{role}, #{organ}, #{status})")
    boolean addAccount(@Param("address") byte[] address,@Param("username") String username,@Param("role") int role,@Param("organ") String organ,@Param("status") int status);

    @Select("select * from account where username =#{username}")
    Account registerByName(@Param("username") String username);

    //超级管理员审核
    @Select("select * from account where role != 0 and status = 0 limit #{pageSize} offset #{pageIndex}")
    List<Account> auditUser(int pageSize,int pageIndex);

    //超级管理员审核
    @Select("select * from account where role != 0 and status = 2 limit #{pageSize} offset #{pageIndex}")
    List<Account> auditUser_2(int pageSize,int pageIndex);

    @Select("select count(*) from account where role != 0 and status = 0")
    int auditCount();

    //查询机构名称
    @Select("select * from organ where id =#{organ}")
    Organ selectOrganName(String organ);

    //超级管理员审核成功未上链
    @Update("update account set status = 1 where address = #{address}")
    boolean auditUserRegisterSuccess(String address);

    //超级管理员审核成功并已上链
    @Update("update account set status = 2 where address = #{address}")
    boolean auditUserRegisterSuccessAndBroad(String address);

    //超级管理员审核拒绝
    @Update("update account set status = 3 where address = #{address}")
    boolean auditUserRegisterFail(String address);

    //新增hash状态
    @Insert("insert into hash (address,hash,status,timestamp) values (#{address}, #{hash}, #{status},#{timestamp})")
    boolean addHash(@Param("address") byte[] address,@Param("hash") String hash,@Param("status") int status,@Param("timestamp") String timestamp);

    @Select("select * from hash where status = '1'")
    List<TxHash> selectByHash();

    @Update("update hash set status = 2 where address = #{address}")
    boolean userTxHashSuccaerrBroad(String address);
}
