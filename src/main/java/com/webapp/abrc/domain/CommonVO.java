package com.webapp.abrc.domain;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class CommonVO extends SearchVO {
    private String table_name;
    private String pk;
    private String[] chk;
    private String[] product_cd_arr;
}
