package com.webapp.abrc.domain;

public class SearchVO extends PageVO{
    private int usr_no;
    private String bgno;
    private String searchKeyword = "";         // 검색 키워드
    private String searchType = "";            // 검색 필드: 제목, 내용
    private String[] searchTypeArr;            // 검색 필드를 배열로 변환

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

    public String getSearchKeyword() {
        return searchKeyword;
    }

    public void setSearchKeyword(String searchKeyword) {
        this.searchKeyword = searchKeyword;
    }

    public String getSearchType() {
        return searchType;
    }

    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }

    public String[] getSearchTypeArr() {
        return searchType.split(",");
    }

    public void setSearchTypeArr(String[] searchTypeArr) {
        this.searchTypeArr = searchTypeArr;
    }
}
