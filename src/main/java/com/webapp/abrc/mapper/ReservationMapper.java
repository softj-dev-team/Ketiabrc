package com.webapp.abrc.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface ReservationMapper {
    int insertResSave(Map<String, Object> params);
    int getReservationCount(Map<String, Object> params);
	List<Map<String,Object>> getReservations(Map<String, Object> params);
    int resUpdate(Map<String, Object> params);
	Map<String,Object> getReservationDetail(Map<String, Object> params);
	int resDel(Map<String,Object> params);
}
