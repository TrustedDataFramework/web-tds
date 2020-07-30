package com.hanjie.web.entity;

import lombok.Data;

@Data
public class Account {
    private byte[] address;

    private String username;

    private int role;

    private int organ;

    private int status;

    private String address_utf8;

    private String organName;
}
