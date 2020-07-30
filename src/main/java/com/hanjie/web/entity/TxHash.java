package com.hanjie.web.entity;

import lombok.Data;

import java.util.Date;

@Data
public class TxHash {
    private String address;

    private String hash;

    private int status;

    private Date timestamp;
}
