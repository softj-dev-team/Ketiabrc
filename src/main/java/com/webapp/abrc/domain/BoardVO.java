package com.webapp.abrc.domain;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class BoardVO extends SearchVO {
    private int usr_no;
    private String bgno;
    private String brdno;
    private String brdtitle;
    private String brdwriter;
    private String brdmemo;
    private String brddate;
    private String brddeleteflag;
    private String user_id;
    private String brdhit;
    private String reno;

    /* 첨부파일 */
    private List<MultipartFile> uploadfile;
}
