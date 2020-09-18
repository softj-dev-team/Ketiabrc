package com.webapp.abrc.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface CategoryMapper {
	List<Map<String,Object>> getCategorys(Map<String,Object> params);
	Map<String,Object> getCategoryOne(Map<String,Object> params);
    int cateUpdate(Map<String, Object> params);
}
