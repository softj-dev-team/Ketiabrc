package com.webapp.abrc.controller;

import com.webapp.abrc.domain.BoardGroupVO;
import com.webapp.abrc.domain.BoardVO;
import com.webapp.abrc.domain.ReservationVO;
import com.webapp.abrc.domain.UserVO;
import com.webapp.abrc.mapper.ReservationMapper;
import com.webapp.abrc.service.BoardGroupService;
import com.webapp.abrc.service.BoardService;
import com.webapp.abrc.service.RestapiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class RestapiController {
    @Autowired
    RestapiService restapiService;
    @Autowired
    BoardGroupService boardGroupService;
    @Autowired
    BoardService boardService;
    @Autowired
    ReservationMapper reservationMapper;

    //회원가입 처리
    @RequestMapping("/signupProc")
    public Map<String, Object> signupProc(@RequestParam HashMap params, UserVO userVO){
        Map<String, Object> result = new HashMap<String, Object>();

        restapiService.insertUser(params, result);

        userVO.setLog_type("join");
        restapiService.insertUserHistory(userVO);

        return result;
    }

    //로그인
    @RequestMapping("/loginProc")
    public Map<String, Object> loginProc(@RequestParam HashMap<String, Object> params, UserVO userVO, HttpSession session){
        Map<String, Object> result = new HashMap<String, Object>();

        restapiService.userSelectOne(params, result, session);
        if(session.getAttribute("grant").equals(3)){
            userVO.setLog_type("adminlogin");
        } else {
            userVO.setLog_type("login");
        }
        restapiService.insertUserHistory(userVO);

        return result;
    }

	//비밀번호 변경
    @RequestMapping("/pwChProc")
    public Map<String, Object> pwChProc(@RequestParam HashMap<String, Object> params, UserVO userVO, HttpSession session){
        Map<String, Object> result = new HashMap<String, Object>();

        restapiService.updatePw(params, result, session);

        return result;
    }

    //카테고리
	@RequestMapping("/getCategorys")
	public Map<String, Object> getCategorys(@RequestParam HashMap<String,Object> params, HttpServletRequest req, HttpServletResponse res, HttpSession sess, ModelMap model) {
		Map<String,Object> result = new HashMap<>();

		restapiService.getCategorys(params, result);

		return result;
	}

	//게시글 삭제
	@RequestMapping("/deleteBoard")
	public Map<String, Object> deleteBoard(@RequestParam HashMap<String,Object> params, HttpServletRequest request, HttpServletResponse res, HttpSession sess,ModelMap model) {
		Map<String,Object> result = new HashMap<>();

        String brdno = request.getParameter("brdno");
        BoardVO boardInfo = boardService.selectBoardOne(brdno);
		restapiService.deleteBoard(params, result);

        result.put("redirectUrl", "/bulletin_board?bgno=" + boardInfo.getBgno());
		return result;
	}

	//예약 저장
    @RequestMapping("/resSave")
    public Map<String, Object> resSave(@RequestParam HashMap params, HttpServletRequest req, HttpServletResponse res, HttpSession session, ModelMap model){
        Map<String, Object> result = new HashMap<String, Object>();

        params.put("user_id", session.getAttribute("loginId"));
        restapiService.insertResSave(params, result);

        return result;
    }

    //예약선택
	@RequestMapping("/getReservationDetail")
	public Map<String, Object> getReservationDetail(@RequestParam HashMap<String,Object> params, HttpServletRequest req, HttpServletResponse res, HttpSession sess, ModelMap model, ReservationVO reservationVO) {
		Map<String,Object> result = new HashMap<>();

		result.put("detail",reservationMapper.getReservationDetail(params));

		return result;
	}

    //예약 수정
    @RequestMapping("/resUpate")
    public Map<String, Object> resUpate(@RequestParam HashMap params, HttpServletRequest req, HttpServletResponse res, HttpSession session, ModelMap model){
        Map<String, Object> result = new HashMap<String, Object>();

        params.put("user_id", session.getAttribute("loginId"));
        restapiService.resUpate(params, result, session);

        return result;
    }

    //카테고리 - 예약 제한 수 변경
    @RequestMapping("/cateUpdate")
    public Map<String, Object> cateUpdate(@RequestParam HashMap<String, Object> params, UserVO userVO, HttpSession session){
        Map<String, Object> result = new HashMap<String, Object>();

        restapiService.cateUpdate(params, result, session);

        return result;
    }

    //회원권한 변경
    @RequestMapping("/grantChange")
    public Map<String, Object> grantChange(@RequestParam HashMap<String, Object> params, UserVO userVO, HttpSession session){
        Map<String, Object> result = new HashMap<String, Object>();

        restapiService.grantChange(params, result, session);

        return result;
    }

    //사원등록
    @RequestMapping("/empSave")
    public Map<String, Object> empSave(@RequestParam HashMap<String, Object> params, UserVO userVO){
        Map<String, Object> result = new HashMap<String, Object>();

        restapiService.empSave(params, result);

        return result;
    }

}
