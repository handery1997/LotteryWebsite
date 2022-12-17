package com.lottery.project.controller;

import java.text.ParseException;
import java.sql.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.lottery.project.model.Account;
import com.lottery.project.model.CheckHistory;
import com.lottery.project.service.CheckHistoryService;

@Controller
public class CheckHistoryController {
	@Autowired
	private CheckHistoryService checkHistoryService;

	@GetMapping(value = "/checkhistory")
	public String checkHistory(HttpSession session, Model model) {
		Account account = (Account) session.getAttribute("loggedin");
		if (account != null) {
			return checkPagination(1, model, session);
		} else {
			return "redirect:/home";
		}
	}
	@GetMapping(value = "/checkhistorydate")
	public String checkHistoryDate(HttpSession session, Model model) {
		Account account = (Account) session.getAttribute("loggedin");
		if (account != null) {
			return checkDatePagination(1, model, session);
		} else {
			return "redirect:/home";
		}
	}

	@GetMapping(value = "/checkhistory/{pageNo}")
	public String checkPagination(@PathVariable(value = "pageNo") int pageNo, Model model, HttpSession session) {
		Account account = (Account) session.getAttribute("loggedin");
		if (account != null) {
			int pageSize = 10;
			Page<CheckHistory> page = checkHistoryService.findPageCheck(account, pageNo, pageSize);
			List<CheckHistory> list = page.getContent();
			model.addAttribute("currentPage", pageNo);
			model.addAttribute("totalPages", page.getTotalPages());
			model.addAttribute("totalItems", page.getTotalElements());
			model.addAttribute("checkHistory", list);
			return "checkhistory";
		}else {
			return "redirect:/home";
		}
	}
	
	@GetMapping(value = "/checkhistorydate/{pageNo}")
	public String checkDatePagination(@PathVariable(value = "pageNo") int pageNo, Model model, HttpSession session) {
		Account account = (Account) session.getAttribute("loggedin");
		if (account != null) {
			int pageSize = 10;
			Page<CheckHistory> page = checkHistoryService.findPageCheck(account, pageNo, pageSize);
			List<CheckHistory> list = page.getContent();
			model.addAttribute("currentPage", pageNo);
			model.addAttribute("totalPages", page.getTotalPages());
			model.addAttribute("totalItems", page.getTotalElements());
			model.addAttribute("checkHistory", list);
			return "checkhistorydate";
		}else {
			return "redirect:/home";
		}
	}
	@GetMapping(value = "/deletecheck/{id}")
	public String deleteCheck(@PathVariable (value="id") long id) {
		checkHistoryService.deleteCheck(id);
		return "redirect:/checkhistory";
	}
	@PostMapping(value="/deletemulticheck")
	public String deleteMultiCheck(HttpServletRequest request) {
		for(String i : request.getParameterValues("id")) {
			long id = Long.parseLong(i);
			checkHistoryService.deleteCheck(id);
		}
		return "redirect:/checkhistory";
	}
	@PostMapping(value="/searchcheck")
	public String searchCheck(HttpServletRequest request,@RequestParam (value="categories") String category,Model model,@RequestParam(value="date",required = false) Date date) throws ParseException {
		if(category.equals("searchdate")) {
			List<CheckHistory> dateList = checkHistoryService.searchDate(date);
			model.addAttribute("checkHistory", dateList);
			return "checkhistorydate";
		}else {
			String result = request.getParameter("result");
			List<CheckHistory> resultList = checkHistoryService.searchResult(result);
			model.addAttribute("checkHistory", resultList);
			return "checkhistory";
		}
	}
}
