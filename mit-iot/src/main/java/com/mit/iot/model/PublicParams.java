package com.mit.iot.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PublicParams {
    private String client_id;

    private String method;

    private String access_token;

    private String timestamp;

    private String sign;

    private String ipAndPort;
}
