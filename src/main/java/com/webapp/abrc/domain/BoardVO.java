package com.webapp.abrc.domain;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class BoardVO extends SearchVO {
    private int usr_no;
    private String bgno;
    private String brdno;
    private String brdtitle;
    private String brdwriter;
    private String brdmemo;
    private String brddate;
    private String brdhit;
    private String brddeleteflag;
    private String user_id;
    private String password;
    /* 첨부파일 */
    private List<MultipartFile> uploadfile;

    public List<MultipartFile> getUploadfile() {
        return uploadfile;
    }

    public void setUploadfile(List<MultipartFile> uploadfile) {
        this.uploadfile = uploadfile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public int getUsr_no() {
        return usr_no;
    }

    public void setUsr_no(int usr_no) {
        this.usr_no = usr_no;
    }

    public String getBgno() {
        return bgno;
    }

    public void setBgno(String bgno) {
        this.bgno = bgno;
    }

    public String getBrdno() {
        return brdno;
    }

    public void setBrdno(String brdno) {
        this.brdno = brdno;
    }

    public String getBrdtitle() {
        return brdtitle;
    }

    public void setBrdtitle(String brdtitle) {
        this.brdtitle = brdtitle;
    }

    public String getBrdwriter() {
        return brdwriter;
    }

    public void setBrdwriter(String brdwriter) {
        this.brdwriter = brdwriter;
    }

    public String getBrdmemo() {
        return brdmemo;
    }

    public void setBrdmemo(String brdmemo) {
        this.brdmemo = brdmemo;
    }

    public String getBrddate() {
        return brddate;
    }

    public void setBrddate(String brddate) {
        this.brddate = brddate;
    }

    public String getBrdhit() {
        return brdhit;
    }

    public void setBrdhit(String brdhit) {
        this.brdhit = brdhit;
    }

    public String getBrddeleteflag() {
        return brddeleteflag;
    }

    public void setBrddeleteflag(String brddeleteflag) {
        this.brddeleteflag = brddeleteflag;
    }
}
