package com.webapp.abrc.controller;

import com.google.gson.JsonObject;
import com.webapp.abrc.domain.BoardGroupVO;
import com.webapp.abrc.domain.BoardVO;
import com.webapp.abrc.domain.ReservationVO;
import com.webapp.abrc.domain.UserVO;
import com.webapp.abrc.mapper.ReservationMapper;
import com.webapp.abrc.mapper.UserMapper;
import com.webapp.abrc.service.BoardGroupService;
import com.webapp.abrc.service.BoardService;
import com.webapp.abrc.service.RestapiService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.springframework.util.CollectionUtils.isEmpty;

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
    @Autowired
    UserMapper userMapper;
    @Value("${fileUploadPath}")
	private String fileUploadPath;

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
        if(result.get("msg").equals("success")){
            if(session.getAttribute("grant").equals(3)){
                userVO.setLog_type("adminlogin");
            } else {
                userVO.setLog_type("login");
            }
            restapiService.insertUserHistory(userVO);
        }

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
    @RequestMapping("/resUpdate")
    public Map<String, Object> resUpdate(@RequestParam HashMap params, HttpServletRequest req, HttpServletResponse res, HttpSession session, ModelMap model){
        Map<String, Object> result = new HashMap<String, Object>();

        restapiService.resUpdate(params, result, session);

        return result;
    }

    //예약 취소
    @RequestMapping("/resDel")
    public Map<String, Object> resDel(@RequestParam HashMap params, HttpServletRequest req, HttpServletResponse res, HttpSession session, ModelMap model){
        Map<String, Object> result = new HashMap<String, Object>();

        restapiService.resDel(params, result, session);

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

    //게시판 에디터 이미지 업로드
    @RequestMapping(value="/uploadSummernoteImageFile", method = RequestMethod.POST, produces = "application/json")
    public HashMap<String, Object> uploadSummernoteImageFile(@RequestParam("file") MultipartFile multipartFile, HttpServletRequest request) throws Exception{
        HashMap<String, Object> resultMap = new HashMap<String, Object>();

        JsonObject jsonObject = new JsonObject();

        String fileRoot = fileUploadPath;	//저장될 외부 파일 경로
        String originalFileName = multipartFile.getOriginalFilename();	//오리지날 파일명
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));	//파일 확장자

        String savedFileName = UUID.randomUUID() + extension;	//저장될 파일 명

        File targetFile = new File(fileRoot + savedFileName);
        Object siteUrl = request.getRequestURL().toString().replace(request.getRequestURI(),"");
        try {
            InputStream fileStream = multipartFile.getInputStream();
            FileUtils.copyInputStreamToFile(fileStream, targetFile);	//파일 저장
            resultMap.put("url", siteUrl+"/download/"+savedFileName);
            resultMap.put("responseCode", "success");

        } catch (IOException e) {
            FileUtils.deleteQuietly(targetFile);	//저장된 파일 삭제
            resultMap.put("responseCode", "error");
            e.printStackTrace();
        }

        return resultMap;
    }

    //회원 탈퇴
    @RequestMapping(value = "/withdrawProc", method = RequestMethod.POST, produces = "application/json")
    public Map<String, Object>  withdrawProc(@RequestParam HashMap params, UserVO userVO, HttpSession session){
    	Map<String, Object> result = new HashMap<String, Object>();

    	if(!session.getAttribute("grant").equals(3)){
            params.put("user_id",session.getAttribute("loginId"));
        }
        //로그인 확인
        Map<String, Object> user = userMapper.userSelectOne(params);

        if(!isEmpty(user)){
            restapiService.deleteUser(user, result);
        }

    	return result;
    }
}
