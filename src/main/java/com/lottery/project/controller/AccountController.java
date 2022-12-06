package com.lottery.project.controller;

import java.net.http.HttpRequest;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.catalina.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.lottery.project.model.Account;
import com.lottery.project.service.AccountService;

@Controller
public class AccountController {
	@Autowired
	private AccountService accountService;

	@GetMapping(value = "/login")
	public String showLogin(Model model) {
		return "login-form";
	}

	@GetMapping(value = "/register")
	public String showRegister(Model model) {
		Account newAccount = new Account();
		model.addAttribute("addAccount", newAccount);
		return "register-form";
	}

	@GetMapping(value = "/addNewAccount")
	public String addNewAccount(Model model) {
		Account newAccount = new Account();
		model.addAttribute("addAccount", newAccount);
		return "add-account";
	}

	@PostMapping(value = "/addAccountForm")
	public String saveAccount(@Valid @ModelAttribute(value = "addAccount") Account account, BindingResult bindingResult,
			@RequestParam(value = "userMail") String mail, Model model) {
		if (bindingResult.hasErrors()) {
			if (accountService.checkExistMail(mail)) {
				model.addAttribute("existMail", "Email already used!");
			}
			return "add-account";
		} else if (accountService.checkExistMail(mail)) {
			model.addAttribute("existMail", "Email already used!");
			return "add-account";

		} else {
			account.setAccountRole("Normal");
			account.setStatus("Active");
			accountService.saveAccount(account);
			return "redirect:/usermanage";
		}
	}

	@PostMapping(value = "/addAccountFormRegister")
	public String saveAccountRegister(@Valid @ModelAttribute(value = "addAccount") Account account,
			BindingResult bindingResult, @RequestParam(value = "userMail") String mail, Model model) {
		if (bindingResult.hasErrors()) {
			if (accountService.checkExistMail(mail)) {
				model.addAttribute("existMail", "Email already used!");
			}
			return "register-form";
		} else if (accountService.checkExistMail(mail)) {
			model.addAttribute("existMail", "Email already used!");
			return "register-form";

		} else {
			account.setAccountRole("Normal");
			account.setStatus("Active");
			accountService.saveAccount(account);
			model.addAttribute("note", "Register Successful!");
			return "register-form";
		}
	}

	@GetMapping(value = "/updateForm/{id}")
	public String updateAccount(@PathVariable(value = "id") long id, Model model) {
		Account account = accountService.getAccountById(id);
		model.addAttribute("updateAccount", account);
		return "update-account";
	}

	@PostMapping(value = "/updateAccountForm")
	public String updateAccount(@Valid @ModelAttribute(value = "updateAccount") Account account,
			BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			return "update-account";
		} else {
			accountService.saveAccount(account);
			return "redirect:/usermanage";
		}
	}

	@GetMapping(value = "/usermanage/{pageNo}")
	public String userPagination(@PathVariable(value = "pageNo") int pageNo, Model model) {
		int pageSize = 10;
		Page<Account> page = accountService.findPageAccount(pageNo, pageSize);
		List<Account> list = page.getContent();
		model.addAttribute("currentPage", pageNo);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("AccService", list);
		return "user-management";

	}

	@GetMapping(value = "/usermanage")
	public String showUserManage(Model model) {
		return userPagination(1, model);
	}

	@GetMapping(value = "/deleteUser/{id}")
	public String deleteAccount(@PathVariable(value = "id") long id) {
		accountService.deleteAccount(id);
		return "redirect:/usermanage";
	}

	@PostMapping(value = "/verifyaccount")
	public String verifyAccount(@RequestParam(value = "mail") String mail,
			@RequestParam(value = "password") String password, @RequestParam(value = "remember",required = false) String remember, Model model,HttpSession session,HttpServletResponse response) {
		Account account = accountService.findByEmail(mail);
		if(account!=null) {
			if(account.getPassword().equals(password)) {
				session.setAttribute("loggedin", account);
				model.addAttribute("result","Login successful!");
				return "login-form";
			} else {
				model.addAttribute("result","Wrong password!");
				return "login-form";
			}
		}else {
			model.addAttribute("result","Account not exist!");
			return "login-form";
		}
		
		
	}
	@GetMapping(value="/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/home";
	}
	@PostMapping(value="/deletemultiaccount")
	public String deleteMulAccount(HttpServletRequest request, Model model) {
		for(String i: request.getParameterValues("id")) {
			long id = Long.parseLong(i);
			accountService.deleteAccount(id);
		}
		return "redirect:/usermanage";
	}
}
