package com.hanjie.web.entity;
import lombok.Data;

@Data
public class User {
    private Integer id;

    private String username;

    private String password;

    private String address;

    private int role;

    private String country;

    private String keystore;

}
