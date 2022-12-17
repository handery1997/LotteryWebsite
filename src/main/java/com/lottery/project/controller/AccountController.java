package com.lottery.project.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lottery.project.model.Account;
import com.lottery.project.service.AccountService;
import com.lottery.project.service.EmailSenderService;

@Controller
public class AccountController {
	@Autowired
	private AccountService accountService;
	@Autowired
	private EmailSenderService emailSenderService;

	@GetMapping(value = "/login")
	public String showLogin(Model model, HttpSession session) {
		Account account = (Account) session.getAttribute("loggedin");
		if (account != null) {
			return "redirect:/home";
		}
		return "login-form";
	}

	@GetMapping(value = "/register")
	public String showRegister(Model model, HttpSession session) {
		Account account = (Account) session.getAttribute("loggedin");
		if (account != null) {
			return "redirect:/home";
		}
		Account newAccount = new Account();
		model.addAttribute("addAccount", newAccount);
		return "register-form";
	}

	@GetMapping(value = "/addNewAccount")
	public String addNewAccount(Model model, HttpSession session) {
		Account account = (Account) session.getAttribute("loggedin");
		if (account != null) {
			if (account.getAccountRole().equals("Admin")) {
				Account newAccount = new Account();
				newAccount.setPassword("123456789");
				model.addAttribute("addAccount", newAccount);
				return "add-account";
			}
		}
		return "redirect:/home";
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
			String newPass = accountService.resetPassword(account);
			emailSenderService.sendEmailAdd(account.getUserMail(), newPass);
			accountService.saveAccount(account);
			return "redirect:/usermanage";
		}
	}

	@PostMapping(value = "/addAccountFormRegister")
	public String saveAccountRegister(@Valid @ModelAttribute(value = "addAccount") Account account,
			BindingResult bindingResult, @RequestParam(value = "userMail") String mail, Model model,
			@RequestParam(value = "textPass") String textPass) {
		if (bindingResult.hasErrors()) {
			if (accountService.checkExistMail(mail)) {
				model.addAttribute("existMail", "Email already used!");
			}
			if (!account.getPassword().equals(textPass)) {
				model.addAttribute("error", "Password not match!");
			}
			return "register-form";
		} else if (accountService.checkExistMail(mail)) {
			model.addAttribute("existMail", "Email already used!");
			if (!account.getPassword().equals(textPass)) {
				model.addAttribute("error", "Password not match!");
			}
			return "register-form";

		} else if (!account.getPassword().equals(textPass)) {
			model.addAttribute("error", "Password not match!");
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
	public String updateAccount(@PathVariable(value = "id") long id, Model model, HttpSession session) {
		Account account1 = (Account) session.getAttribute("loggedin");
		if (account1 != null) {
			if (account1.getAccountRole().equals("Admin")) {
				Account account = accountService.getAccountById(id);
				model.addAttribute("updateAccount", account);
				return "update-account";
			}
		}
		return "redirect:/home";
	}

	@PostMapping(value = "/updateAccountForm")
	public String updateAccount(@Valid @ModelAttribute(value = "updateAccount") Account account,
			BindingResult bindingResult, Model model) {

		if (bindingResult.hasErrors()) {
			model.addAttribute("mess", "error");
			return "update-account";
		} else {
			accountService.saveAccount(account);
			return "redirect:/usermanage";
		}
	}

	@GetMapping(value = "/usermanage/{pageNo}")
	public String userPagination(@PathVariable(value = "pageNo") int pageNo, Model model, HttpSession session) {
		Account account = (Account) session.getAttribute("loggedin");
		if (account != null) {
			if (account.getAccountRole().equals("Admin")) {
				int pageSize = 10;
				Page<Account> page = accountService.findPageAccount(pageNo, pageSize);
				List<Account> list = page.getContent();
				model.addAttribute("currentPage", pageNo);
				model.addAttribute("totalPages", page.getTotalPages());
				model.addAttribute("totalItems", page.getTotalElements());
				model.addAttribute("AccService", list);
				return "user-management";
			}
		}
		return "redirect:/home";

	}

	@GetMapping(value = "/usermanage")
	public String showUserManage(Model model, HttpSession session) {
		return userPagination(1, model, session);
	}

	@GetMapping(value = "/deleteUser/{id}")
	public String deleteAccount(@PathVariable(value = "id") long id, HttpSession session) {
		Account account = (Account) session.getAttribute("loggedin");
		if (account != null) {
			if (account.getAccountRole().equals("Admin")) {
				accountService.deleteAccount(id);
				return "redirect:/usermanage";
			}
		}
		return "redirect:/home";
	}

	@PostMapping(value = "/verifyaccount")
	public String verifyAccount(@RequestParam(value = "mail") String mail,
			@RequestParam(value = "password") String password,
			@RequestParam(value = "remember", required = false) String remember, Model model, HttpSession session,
			HttpServletResponse response) {
		Account account = accountService.findByEmail(mail);
		if (account != null) {
			if (account.getPassword().equals(password)) {
				session.setAttribute("loggedin", account);
				model.addAttribute("result", "Login successful!");
				return "login-form";
			} else {
				model.addAttribute("result", "Wrong password!");
				return "login-form";
			}
		} else {
			model.addAttribute("result", "Account not exist!");
			return "login-form";
		}

	}

	@GetMapping(value = "/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/home";
	}

	@PostMapping(value = "/deletemultiaccount")
	public String deleteMulAccount(HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
		for (String i : request.getParameterValues("id")) {
			long id = Long.parseLong(i);
			Account account = accountService.getAccountById(id);
			if (account.getAccountRole().equals("Admin")) {
				redirectAttributes.addFlashAttribute("error", "Can not delete admin");
				return "redirect:/usermanage";
			}
		}
		for (String i : request.getParameterValues("id")) {
			long id = Long.parseLong(i);
			accountService.deleteAccount(id);
		}
		return "redirect:/usermanage";
	}

	@PostMapping(value = "/searchaccount")
	public String searchDate(@RequestParam(value = "search") String search,
			@RequestParam(value = "categories") String category, Model model) {
		List<Account> account = accountService.getAllAccount();
		List<Account> accountList = new ArrayList<Account>();
		if (category.equals("searchmail")) {
			for (Account a : account) {
				if (a.getUserMail().equals(search)) {
					accountList.add(a);
				}
			}
		} else {
			for (Account a : account) {
				if (a.getUserPhone().equals(search)) {
					accountList.add(a);
				}
			}
		}
		model.addAttribute("AccService", accountList);
		return "user-management";
	}

	@GetMapping(value = "/resetpass/{id}")
	public String resetPassword(@PathVariable(value = "id") long id, RedirectAttributes redirectAttributes,
			HttpSession session) {
		Account account1 = (Account) session.getAttribute("loggedin");
		if (account1 != null) {
			if (account1.getAccountRole().equals("Admin")) {
				Account account = accountService.getAccountById(id);
				String newPass = accountService.resetPassword(account);
				emailSenderService.sendEmail(account.getUserMail(), newPass);
				accountService.saveAccount(account);
				redirectAttributes.addFlashAttribute("mess", "Reset successful");
				return "redirect:/updateForm/" + id;
			}
		}
		return "redirect:/home";
	}

	@GetMapping(value = "/changepassword")
	public String changePass(HttpSession session) {
		Account account = (Account) session.getAttribute("loggedin");
		if (account != null) {
			return "changepassword";
		}
		return "redirect:/home";
	}

	@PostMapping(value = "/changepasswordform")
	public String changePassForm(HttpSession session, @RequestParam(value = "oldpass") String oldPass,
			@RequestParam(value = "newpass") String newPass, @RequestParam(value = "textpass") String textPass,
			Model model) {
		Account account = (Account) session.getAttribute("loggedin");
		if (account != null) {
			if (oldPass.equals(account.getPassword())) {
				if (newPass.equals(textPass)) {
					account.setPassword(newPass);
					accountService.saveAccount(account);
					model.addAttribute("note", "Change password successful!");
				} else if (newPass.length() < 8) {
					model.addAttribute("errorpass", "Password must be more than 8 characters or numbers!");
					return "changepassword";
				} else {
					model.addAttribute("errorcheck", "Confirm password not match with password!");
					return "changepassword";
				}
			} else {
				model.addAttribute("errorold", "Password not match with current password!");
				return "changepassword";
			}
		}
		return "changepassword";

	}

	@GetMapping(value = "/forgotpassword")
	public String forgotPass() {
		return "resetpassword";
	}

	@PostMapping(value = "/resetpassform")
	public String resetPass(@RequestParam(value = "email") String email, Model model) {
		Account account = accountService.findByEmail(email);
		if (account != null) {
			String newPass = accountService.resetPassword(account);
			accountService.saveAccount(account);
			emailSenderService.sendEmail(email, newPass);
			model.addAttribute("note", "Your password is reset! Please login into your email to get new password!");
			return "resetpassword";
		} else {
			model.addAttribute("errormail", "Email not exist please register!");
			return "resetpassword";
		}

	}
}
