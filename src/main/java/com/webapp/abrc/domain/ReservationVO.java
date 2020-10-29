package com.webapp.abrc.domain;

import lombok.Data;

@Data
public class ReservationVO {
    private int rs_no;
    private String user_id;
    private String user_name;
    private String rs_date;
    private String rs_start_time;
    private String rs_end_time;
    private String hp;
    private int rs_cate_id;
    private String rs_memo;
    private String rs_deleteflag;
    private int resource_id;
}
