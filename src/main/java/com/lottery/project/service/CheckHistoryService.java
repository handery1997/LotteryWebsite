package com.lottery.project.service;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;

import com.lottery.project.model.Account;
import com.lottery.project.model.CheckHistory;

public interface CheckHistoryService {
	void saveHistory(CheckHistory history);
	Page<CheckHistory> findPageCheck(Account account,int pageNo,int pageSize);
	void deleteCheck(long id);
	List<CheckHistory> searchDate(Date date);
	List<CheckHistory> searchResult(String result);
	
}
