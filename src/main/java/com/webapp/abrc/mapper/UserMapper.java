package com.webapp.abrc.mapper;

import com.webapp.abrc.domain.SearchVO;
import com.webapp.abrc.domain.UserVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface UserMapper {
    int insertUser(Map<String, Object> params);
    int insertUserHistory(UserVO userVO);
    Map<String,Object> userSelectOne(Map<String,Object> params);
    int updatePw(Map<String, Object> params);
    int getManagerUserListCount(SearchVO params);
    List<Map<String,Object>> userList(SearchVO params);
    List<Map<String,Object>> getUserGrantList(SearchVO params);
    int grantChange(Map<String, Object> params);
    int empSave(Map<String, Object> params);
    List<Map<String,Object>> empList(Map<String, Object> params);
    int deleteUser(Map<String,Object> user);
}
