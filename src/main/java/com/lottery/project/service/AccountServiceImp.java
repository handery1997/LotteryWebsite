package com.lottery.project.service;

import java.util.List;
import java.util.Optional;

import javax.management.RuntimeErrorException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.lottery.project.model.Account;
import com.lottery.project.repository.AccountRepository;

@Service
public class AccountServiceImp implements AccountService{
	@Autowired
	private AccountRepository accountRepository;

	@Override
	public List<Account> getAllAccount() {

		return accountRepository.findAll();
	}

	@Override
	public void saveAccount(Account account) {
		this.accountRepository.save(account);
	}

	@Override
	public Account getAccountById(long id) {
		Optional<Account> optional = accountRepository.findById(id);
		Account account = null;
		if(optional.isPresent()) {
			account=optional.get();	
		}
		else {
			throw new RuntimeException("Id not found");
		}
		return account;
	}

	@Override
	public void deleteAccount(long id) {
		accountRepository.deleteById(id);	
	}

	@Override
	public Page<Account> findPageAccount(int pageNo, int pageSize) {
		Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
		return accountRepository.findAll(pageable);
	}

	@Override
	public Account findByEmail(String email) {
		List<Account>list = getAllAccount();
		Account account = null;
		for(Account a: list) {
			if(a.getUserMail().equals(email)) {
				account = a;
				return account;
			}
		} 
		return account;
	}

	@Override
	public boolean checkExistMail(String email) {
		List<Account>list=getAllAccount();
		for(Account a: list) {
			if(a.getUserMail().equals(email)) {
				return true;
			}
		} 
		return false;
	}
	
	
	
}
