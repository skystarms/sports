package com.mbc.sports.soccerdirect;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class SoccerDirectController {
	@Autowired
	SqlSession sqlSession;
	
	private static final Logger logger = LoggerFactory.getLogger(SoccerDirectController.class);
	@RequestMapping(value = "/soccercalendar", method = RequestMethod.GET)
	public String soccercalendar(HttpServletRequest request, Model model) {
		String areaname = request.getParameter("name");
		DirectService ds = sqlSession.getMapper(DirectService.class);
		ArrayList <DirectDTO> list = ds.selectCalendarArea(areaname);
		model.addAttribute("player_list", list);
		return "soccercalendar";
	}

	@RequestMapping(value = "/soccerstrategy", method = RequestMethod.GET)
	public String soccerstrategy(HttpServletRequest request, Model model) {
		String areaname1 = request.getParameter("name");
		String areaname2 = request.getParameter("area");
		return "soccerstrategy";
	}
}