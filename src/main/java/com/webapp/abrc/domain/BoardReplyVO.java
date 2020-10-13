package com.webapp.abrc.domain;

import lombok.Data;

@Data
public class BoardReplyVO {
    private String brdno;
    private String reno;
    private String rewriter;
    private String user_id;
    private String redeleteflag;
    private String rememo;
    private String redate;
    private String reparent;
    private String redepth;
    private Integer reorder;
}
