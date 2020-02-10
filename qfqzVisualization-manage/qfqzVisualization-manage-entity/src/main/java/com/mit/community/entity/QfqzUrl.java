package com.mit.community.entity;


import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

@TableName("qfqzUrl")
@Data
public class QfqzUrl {

    private int id;
    private String url;
    private String userId;
}
