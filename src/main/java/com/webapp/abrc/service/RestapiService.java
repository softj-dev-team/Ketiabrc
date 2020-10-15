package com.webapp.abrc.service;

import com.webapp.abrc.domain.BoardVO;
import com.webapp.abrc.domain.FileVO;
import com.webapp.abrc.domain.SearchVO;
import com.webapp.abrc.domain.UserVO;
import com.webapp.abrc.mapper.BoardMapper;
import com.webapp.abrc.mapper.CategoryMapper;
import com.webapp.abrc.mapper.ReservationMapper;
import com.webapp.abrc.mapper.UserMapper;
import com.webapp.abrc.util.FileUtil;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.util.CollectionUtils.isEmpty;

@Service
public class RestapiService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private ReservationMapper reservationMapper;
    @Autowired
    private BoardMapper boardMapper;

    public void insertUser(Map<String, Object> params, Map<String, Object> result){
    	params.put("password", BCrypt.hashpw((String)params.get("password"), BCrypt.gensalt()));
        userMapper.insertUser(params);

        result.put("redirectUrl", "/login");
        result.put("msg", "success");
    }
    public void insertUserHistory(UserVO userVO) {
        userMapper.insertUserHistory(userVO);
    }

    public void userSelectOne(Map<String, Object> params, Map<String, Object> result, HttpSession session){
        Map<String, Object> user = userMapper.userSelectOne(params);
		if(user == null) {
			result.put("msg","idFail");
		}else {
			if(BCrypt.checkpw((String)params.get("password"), (String)user.get("password"))) {
				result.put("msg","success");
				session.setAttribute("loginId", user.get("user_id"));
				session.setAttribute("grant", user.get("user_grant"));
			}else {
				result.put("msg","pwFail");
			}
		}
    }

    public void updatePw(Map<String, Object> params, Map<String, Object> result, HttpSession session){
    	params.put("user_id", session.getAttribute("loginId"));
        params.put("password", BCrypt.hashpw((String)params.get("password"), BCrypt.gensalt()));
        userMapper.updatePw(params);

        result.put("redirectUrl", "/");
        result.put("msg", "success");
    }

    public void getCategorys(Map<String,Object> params, Map<String,Object> result) {
		List<Map<String, Object>> categorys = categoryMapper.getCategorys(params);

		result.put("categorys",categorys);
	}

	public void deleteBoard(Map<String,Object> params, Map<String,Object> result) {
        boardMapper.deleteBoard(params);

		result.put("msg", "success");
	}

	public void insertResSave(Map<String,Object> params, Map<String,Object> result) {
        int reservationCount = reservationMapper.getReservationCount(params);
        Map<String, Object> categoryOne = categoryMapper.getCategoryOne(params);
        Map<String, Object> user = userMapper.userSelectOne(params);

        if(params.get("user_id") == null){
            result.put("msg", "LoginFail");
        } else if((Integer) categoryOne.get("rs_max_quantity") <= reservationCount){
            result.put("msg", "reCountFail");
        } else if(categoryOne.get("rs_cate_upper_code").equals(5) && !user.get("user_grant").equals(2)){
            result.put("msg", "grantFail");
        } else {
            reservationMapper.insertResSave(params);

            result.put("redirectUrl", "/eq-res?rs_cate_id=" + params.get("rs_cate_id") +"&rs_max_quantity=" + categoryOne.get("rs_max_quantity"));
            result.put("msg", "success");
        }
    }

    public void resUpdate(Map<String,Object> params, Map<String,Object> result, HttpSession session) {
        Map<String, Object> reservtion = reservationMapper.getReservationDetail(params);
        if(session.getAttribute("loginId") == null){
            result.put("msg", "LoginFail");
        } else if(!session.getAttribute("loginId").equals(reservtion.get("user_id"))){
            result.put("msg", "idFail");
        } else {
            params.put("user_id", reservtion.get("user_id"));
            reservationMapper.resUpdate(params);
            result.put("msg", "success");
        }
    }

    public void resDel(Map<String,Object> params, Map<String,Object> result, HttpSession session) {
        Map<String, Object> reservtion = reservationMapper.getReservationDetail(params);

        if(session.getAttribute("loginId") == null){
            result.put("msg", "LoginFail");
        } else if(!session.getAttribute("loginId").equals(reservtion.get("user_id"))){
            result.put("msg", "idFail");
        } else {
            reservationMapper.resDel(params);
            result.put("msg", "success");
        }

        if(session.getAttribute("loginId") != null && session.getAttribute("grant").equals(3)){
            reservationMapper.resDel(params);
            result.put("msg", "success");
        }
    }

    public void cateUpdate(Map<String, Object> params, Map<String, Object> result, HttpSession session){
    	categoryMapper.cateUpdate(params);

        result.put("redirectUrl", "/category");
        result.put("msg", "success");
    }

    public int getManagerUserListCount(SearchVO params) {
        return userMapper.getManagerUserListCount(params);
    }

    public List<Map<String,Object>> userList(SearchVO params) {
        return userMapper.userList(params);
    }

    public List<Map<String,Object>> getUserGrantList(SearchVO params) {
        return userMapper.getUserGrantList(params);
    }

    public void grantChange(Map<String, Object> params, Map<String, Object> result, HttpSession session){
    	userMapper.grantChange(params);

        result.put("redirectUrl", "/member_manager");
        result.put("msg", "success");
    }

    public void empSave(Map<String, Object> params, Map<String, Object> result){
        userMapper.empSave(params);

        result.put("redirectUrl", "/member_manager");
        result.put("msg", "success");
    }

    public List<Map<String,Object>> empList(Map<String, Object> params) {
        return userMapper.empList(params);
    }

    public void deleteUser(Map<String, Object> user, Map<String, Object> result) {
		userMapper.deleteUser(user);

		result.put("redirectUrl", "/logout");
        result.put("msg", "success");
	}
}
