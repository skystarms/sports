package com.mbc.sports.member;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.UUID;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.mbc.sports.HomeController;

@Controller
public class MemberController {
	
private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

	@Autowired
	SqlSession sqlsession;
	
	String path="C:\\project\\sports\\sports\\src\\main\\webapp\\image\\super";
	
	@RequestMapping(value = "/signup", method = RequestMethod.GET)
	public String sign_up() {
		return "signup";
	}
	
	@RequestMapping(value = "/general", method = RequestMethod.GET)
	public String generalmember_sign_up() {
		return "general";
	}
	
	@RequestMapping(value = "/super", method = RequestMethod.GET)
	public String super_sign_up() {
		return "super";
	}
	
	@RequestMapping(value = "/generalsave", method = RequestMethod.POST)
	public String generalmember_save(HttpServletRequest request) {
		
		String id=request.getParameter("id");
		String pw=request.getParameter("pw");
		PasswordEncoder passwordEncoder=new BCryptPasswordEncoder();
		pw=passwordEncoder.encode(pw);
		String name=request.getParameter("name");
		String birthday=request.getParameter("birthday");
		String tel=request.getParameter("tel");
		String email=request.getParameter("email");
		String zipp_code=request.getParameter("zipp_code");
		String user_add1=request.getParameter("user_add1");
		String user_add2=request.getParameter("user_add2");
		String sport=request.getParameter("sport");
		String team=request.getParameter("team");
		String part=request.getParameter("part");
		
		MemberService ss = sqlsession.getMapper(MemberService.class);
		ss.normalinsert(id,pw,name,birthday,tel,email,zipp_code,user_add1,user_add2,sport,team,part);
		
		return "login";
	}
	
	@RequestMapping(value = "/supersave", method = RequestMethod.POST)
	public String superintendent_save(MultipartHttpServletRequest mul) throws IOException {
		
		String id=mul.getParameter("id");
		String pw=mul.getParameter("pw");
		PasswordEncoder passwordEncoder=new BCryptPasswordEncoder();
		pw=passwordEncoder.encode(pw);
		String name=mul.getParameter("name");
		String birthday=mul.getParameter("birthday");
		String tel=mul.getParameter("tel");
		String email=mul.getParameter("email");
		String zipp_code=mul.getParameter("zipp_code");
		String user_add1=mul.getParameter("user_add1");
		String user_add2=mul.getParameter("user_add2");
		String sport=mul.getParameter("sport");
		String team=mul.getParameter("team");
		MultipartFile voe_mf = mul.getFile("voe");
		String voe_fname = voe_mf.getOriginalFilename();
		voe_fname = voe_filesave(voe_fname, voe_mf.getBytes());
		voe_mf.transferTo(new File(path+"\\"+voe_fname));
		
		MultipartFile rr_mf = mul.getFile("rr");
		String rr_fname = rr_mf.getOriginalFilename();
		rr_fname = rr_filesave(voe_fname, voe_mf.getBytes());
		rr_mf.transferTo(new File(path+"\\"+rr_fname));
		
		String part=mul.getParameter("part");
		
		MemberService ss = sqlsession.getMapper(MemberService.class);
		ss.superinsert(id,pw,name,birthday,tel,email,zipp_code,user_add1,user_add2,sport,team,voe_fname,rr_fname,part);
		
		return "redirect:/";
	}

	private String voe_filesave(String voe_fname, byte[] bytes) {
		UUID uuid = UUID.randomUUID();
		String fname = uuid.toString()+"_"+voe_fname;
		return fname;
	}
	
	private String rr_filesave(String rr_fname, byte[] bytes) {
		UUID uuid = UUID.randomUUID();
		String fname = uuid.toString()+"_"+rr_fname;
		return fname;
	}

	@RequestMapping(value = "/idcheck", method = RequestMethod.POST)
	public void ID_Check(String id, HttpServletResponse response) throws IOException {
		
		response.setContentType("text/html;charset=utf-8");
		MemberService ss = sqlsession.getMapper(MemberService.class);
		int result = ss.getId(id);
		PrintWriter prw =response.getWriter();
		prw.print(result);
	}
	
	@RequestMapping(value = "/signup_lookup", method = RequestMethod.GET)
	public String sign_up_look_up(Model mo) {
				
		MemberService ss = sqlsession.getMapper(MemberService.class);
		ArrayList<MemberDTO> list = ss.allget();
		mo.addAttribute("list", list);
		
		return "signup_look_up";
	}
	
	@RequestMapping(value = "/del_mem_view", method = RequestMethod.GET)
	public String Member_Delete_View(String id, Model mo) {
		
		MemberService ss = sqlsession.getMapper(MemberService.class);
		MemberDTO del_mem = ss.memberget(id);
		mo.addAttribute("del", del_mem);
		
		return "del_mem_view2";
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public String Member_Delete(HttpServletRequest request) {
		
		String id = request.getParameter("id");
		String part = request.getParameter("part");
		
		if(part.equals("����")) {
			String voe_fname = request.getParameter("voe");
			String rr_fname = request.getParameter("rr");
			
			File voe_img = new File(path+"\\"+voe_fname);
			voe_img.delete();
			
			File rr_img = new File(path+"\\"+rr_fname);
			rr_img.delete();
		}
		
		MemberService ss = sqlsession.getMapper(MemberService.class);
		ss.del_mem(id);
		
		return "redirect:/signup_lookup";
	}
	
	@RequestMapping(value = "/detail", method = RequestMethod.GET)
	public String Detail_View(String id, Model mo) {
		
		MemberService ss = sqlsession.getMapper(MemberService.class);
		MemberDTO want_mem = ss.memberget(id);
		mo.addAttribute("wm", want_mem);
		
		return "detail_view";
	}
	
	@RequestMapping(value = "/mypage", method = RequestMethod.GET)
	public String Mypage(String id, Model mo) {
		
		MemberService ss = sqlsession.getMapper(MemberService.class);
		MemberDTO mypage_member = ss.memberget(id);
		mo.addAttribute("my", mypage_member);
		
		return "mypage";
	}

}