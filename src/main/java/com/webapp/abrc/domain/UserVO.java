package com.webapp.abrc.domain;

import lombok.Data;

@Data
public class UserVO extends SearchVO{
    private String user_name;
    private String user_id;
    private String password;
    private String log_type;
}
