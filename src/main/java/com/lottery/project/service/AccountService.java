package com.lottery.project.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.lottery.project.model.Account;


public interface AccountService {
	List<Account> getAllAccount();
	void saveAccount(Account account);
	Account getAccountById(long id);
	void deleteAccount(long id);
	Page <Account> findPageAccount(int pageNo,int pageSize);
	Account findByEmail(String email);
	boolean checkExistMail(String email);
	String resetPassword(Account account);
}
