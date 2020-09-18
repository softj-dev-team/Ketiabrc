package com.webapp.abrc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;

@Controller
public class ManagerController {

    //매니저 메인
    @RequestMapping("/Manager/main")
    public String Managermain(@RequestParam HashMap<String, Object> prams, HttpServletRequest req, HttpServletResponse res, HttpSession session, ModelMap model) {
        model.addAttribute("jsFileName","login");
        return "admin_login";
    }

    //로그인
    @RequestMapping("/Manager/login")
    public String Managerlogin(@RequestParam HashMap<String, Object> prams, HttpServletRequest req, HttpServletResponse res, HttpSession session, ModelMap model) {
        model.addAttribute("jsFileName","Managerlogin");
        return "login";
    }

    //로그아웃
    @RequestMapping("/Manger/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/";
	}

    //비밀번호 변경
    @RequestMapping("/Manger/pwCh")
    public String ManagerpwCh(@RequestParam HashMap<String, Object> prams, HttpServletRequest req, HttpServletResponse res, HttpSession session, ModelMap model) {
        if(session.getAttribute("loginId") == null) {
			return "redirect:/login";
		}
        model.addAttribute("jsFileName","ManagerpwCh");
        return "sub/pwCh";
    }
}
