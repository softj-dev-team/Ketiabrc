package com.webapp.abrc.controller;

import com.webapp.abrc.domain.BoardGroupVO;
import com.webapp.abrc.domain.BoardVO;
import com.webapp.abrc.domain.FileVO;
import com.webapp.abrc.domain.UserVO;
import com.webapp.abrc.mapper.BoardMapper;
import com.webapp.abrc.mapper.CategoryMapper;
import com.webapp.abrc.mapper.ReservationMapper;
import com.webapp.abrc.mapper.UserMapper;
import com.webapp.abrc.service.BoardGroupService;
import com.webapp.abrc.service.BoardService;
import com.webapp.abrc.service.RestapiService;
import com.webapp.abrc.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.util.CollectionUtils.isEmpty;

@Controller
public class MainController {
    @Autowired
    BoardGroupService boardGroupService;
    @Autowired
    BoardService boardService;
    @Autowired
    RestapiService restapiService;
    @Autowired
    BoardMapper boardMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    CategoryMapper categoryMapper;
    @Autowired
    ReservationMapper reservationMapper;
    @Value("${downloadPath}")
    private String downloadPath;

    //메인 페이지
    @RequestMapping("/")
    public String main(@RequestParam HashMap<String, Object> params, HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model) {

        return "redirect:/login";
    }

    //예약자 메인
    @RequestMapping("/eq-res")
    public String eqRes(@RequestParam HashMap<String, Object> params, HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model) {

        params.put("rs_cate_upper_code","0");
		List<Map<String,Object>> category1List = categoryMapper.getCategorys(params);
		params.put("rs_cate_upper_code",null);
		List<Map<String,Object>> category2List = categoryMapper.getCategorys(params);

		List<Map<String, Object>> reservationList = reservationMapper.getReservations(params);

        params.put("user_id",session.getAttribute("loginId"));
        //로그인 확인
        Map<String, Object> user = userMapper.userSelectOne(params);

        model.addAttribute("user", user);
        model.addAttribute("category1List", category1List);
        model.addAttribute("category2List", category2List);
        model.addAttribute("list", reservationList);
        model.addAttribute("jsFileName","eq-res");

        if(params.get("rs_cate_id") != null){
            Map<String, Object> categoryOne = categoryMapper.getCategoryOne(params);
            model.addAttribute("categoryOne", categoryOne);
        }
        return "sub/eq-res";

    }

    //로그인
    @RequestMapping("/login")
    public String login(@RequestParam HashMap<String, Object> params, HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model) {
        model.addAttribute("jsFileName","login");
        return "login";
    }

    //로그아웃
    @RequestMapping("/logout")
	public String logout(HttpSession session, UserVO userVO) {
		userVO.setUser_id((String) session.getAttribute("loginId"));
        userVO.setLog_type("logout");
        restapiService.insertUserHistory(userVO);

        session.invalidate();
		return "redirect:/";
	}

    //회원가입
    @RequestMapping("/sign")
    public String join(@RequestParam HashMap<String, Object> params, HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model) {
        List<Map<String,Object>> emplist  = restapiService.empList(params);

        model.addAttribute("emplist", emplist);
        model.addAttribute("jsFileName","sign");
        return "sub/join";
    }

    //회원정보변경
    @RequestMapping("/pwCh")
    public String pwCh(@RequestParam HashMap<String, Object> params, HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model) {
        if(session.getAttribute("loginId") == null) {
			return "redirect:/login";
		}
        params.put("user_id",session.getAttribute("loginId"));
        Map<String, Object> user = userMapper.userSelectOne(params);

        model.addAttribute("user", user);
        model.addAttribute("jsFileName","pwCh");
        return "sub/pwCh";
    }


    //게시판 리스트
    @RequestMapping("/bulletin_board")
    public String boardList(@RequestParam HashMap params, BoardVO boardVO, HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model) {
        BoardGroupVO bgInfo = boardGroupService.selectBoardGroupOne4Used(boardVO.getBgno());
        if(boardVO.getDisplayRowCount()==null || boardVO.getDisplayRowCount() < 10){
            boardVO.setDisplayRowCount(10);
        }

        boardVO.setStaticRowEnd(10);
        boardVO.pageCalculate( boardService.selectBoardCount(boardVO) ); // startRow, endRow

        List<?> listview  = boardService.selectBoardList(boardVO);

        model.addAttribute("params", params);
        model.addAttribute("listview", listview);
        model.addAttribute("searchVO", boardVO);
        model.addAttribute("bgInfo", bgInfo);

        return "sub/bulletin-board";
    }

    //글쓰기
    @RequestMapping("/bulletin_board_t")
    public String boardWrite(@RequestParam HashMap<String, Object> params, BoardVO boardVO, HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model) {
        String Referer = request.getHeader("Referer");
        String bgno = request.getParameter("bgno");
        String brdno = request.getParameter("brdno");

        if (brdno != null) {
            BoardVO boardInfo = boardService.selectBoardOne(brdno);
            List<?> listview = boardService.selectBoard8FileList(brdno);

            model.addAttribute("boardInfo", boardInfo);
            model.addAttribute("listview", listview);
            bgno = boardInfo.getBgno();
        }
        BoardGroupVO bgInfo = boardGroupService.selectBoardGroupOne4Used(bgno);
        if (bgInfo == null) {
            return "redirect:" + Referer;
        }

        params.put("user_id",session.getAttribute("loginId"));
        Map<String, Object> user = userMapper.userSelectOne(params);

        model.addAttribute("user", user);
        model.addAttribute("bgno", bgno);
        model.addAttribute("bgInfo", bgInfo);

        return "sub/bulletin-board-t";
    }

    //글 저장
    @RequestMapping("/boardSave")
    public String boardSave(@RequestParam HashMap<String, Object> params, UserVO userVO, BoardVO boardInfo, HttpServletRequest request, HttpSession session){

        params.put("user_id",session.getAttribute("loginId"));
        //로그인 확인
        Map<String, Object> user = userMapper.userSelectOne(params);
        if(isEmpty(user)){

        }else{
            boardInfo.setUsr_no((Integer)user.get("usr_no"));
        }

        String[] fileno = request.getParameterValues("fileno");
        FileUtil fs = new FileUtil();
        List<FileVO> filelist = null;
        if(boardInfo.getUploadfile() != null) {
            filelist = fs.saveAllFiles(boardInfo.getUploadfile(),downloadPath+"board");
        }

        boardService.insertBoard(boardInfo, filelist, fileno);

        return "redirect:/bulletin_board?bgno=" + boardInfo.getBgno();
    }

    //글 상세보기
    @RequestMapping("/bulletin_view")
    public String boardView(@RequestParam HashMap<String, Object> params, BoardVO boardVO, HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model) {
        String Referer =request.getHeader("Referer");
        String brdno = request.getParameter("brdno");

        params.put("user_id",session.getAttribute("loginId"));
        //로그인 확인
        Map<String, Object> user = userMapper.userSelectOne(params);

        boardService.updateBoard8Read(brdno);
        BoardVO boardInfo = boardService.selectBoardOne(brdno);
        List<?> listview = boardService.selectBoard8FileList(brdno);

        BoardGroupVO bgInfo = boardGroupService.selectBoardGroupOne4Used(boardInfo.getBgno());
        if (bgInfo == null) {
            return "redirect:" + Referer;

        }

        model.addAttribute("user", user);
        model.addAttribute("boardInfo", boardInfo);
        model.addAttribute("listview", listview);
        model.addAttribute("bgInfo", bgInfo);

        return "sub/bulletin-view";
    }

    //회원관리
    @RequestMapping("/member-manag")
    public String member_manager(@RequestParam HashMap<String, Object> params, HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model) {

        return "admin/member-manag";
    }

    //장비예약 페이지
    @RequestMapping("/eq-res2")
    public String eqRes2(@RequestParam HashMap<String, Object> params, HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model) {
		params.put("rs_cate_upper_code","0");
		List<Map<String,Object>> category1List = categoryMapper.getCategorys(params);
		params.put("rs_cate_upper_code",null);
		List<Map<String,Object>> category2List = categoryMapper.getCategorys(params);

		params.put("user_id",session.getAttribute("loginId"));
        //로그인 확인
        Map<String, Object> user = userMapper.userSelectOne(params);

        model.addAttribute("category1List", category1List);
        model.addAttribute("category2List", category2List);
        model.addAttribute("user", user);
        model.addAttribute("jsFileName","eq-res2");

        return "sub/eq-res2";
    }

    //회원관리
    @RequestMapping("/member_manager")
    public String memberManager(@RequestParam HashMap<String, Object> params, UserVO userVO, HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model) {
        if(session.getAttribute("loginId") == null || !session.getAttribute("grant").equals(3)) {
			return "redirect:/login";
		}
        userVO.setDisplayRowCount(10);
        userVO.setStaticRowEnd(10);
        userVO.pageCalculate( restapiService.getManagerUserListCount(userVO) );

        List<Map<String,Object>> list  = restapiService.userList(userVO);
        List<Map<String,Object>> userGrantlist = restapiService.getUserGrantList(userVO);

        model.addAttribute("params", params);
        model.addAttribute("list", list);
        model.addAttribute("userGrantlist", userGrantlist);
        model.addAttribute("jsFileName","member_manager");
        model.addAttribute("searchVO", userVO);

        return "admin/member-manag";
    }

    //카테고리 관리
    @RequestMapping("/category")
    public String category(@RequestParam HashMap<String, Object> params, HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model) {
        if(session.getAttribute("loginId") == null || !session.getAttribute("grant").equals(3)) {
			return "redirect:/login";
		}
        params.put("rs_cate_upper_code","0");
		List<Map<String,Object>> category1List = categoryMapper.getCategorys(params);
		params.put("rs_cate_upper_code",null);
		List<Map<String,Object>> category2List = categoryMapper.getCategorys(params);

		params.put("user_id",session.getAttribute("loginId"));
        //로그인 확인
        Map<String, Object> user = userMapper.userSelectOne(params);

        model.addAttribute("category1List", category1List);
        model.addAttribute("category2List", category2List);
        model.addAttribute("jsFileName","category");
        return "admin/category";
    }
}
