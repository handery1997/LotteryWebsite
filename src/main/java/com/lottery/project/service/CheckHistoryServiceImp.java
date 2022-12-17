package com.lottery.project.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.lottery.project.model.Account;
import com.lottery.project.model.CheckHistory;
import com.lottery.project.repository.CheckHistoryRepository;

@Service
public class CheckHistoryServiceImp implements CheckHistoryService{
	@Autowired
	private CheckHistoryRepository checkHistoryRepository;
	@Override
	public void saveHistory(CheckHistory history) {
		this.checkHistoryRepository.save(history);	
	}
	@Override
	public Page<CheckHistory> findPageCheck(Account account, int pageNo, int pageSize) {
		Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
		return checkHistoryRepository.findByUserIdLike(account.getId(), pageable);
	}
	@Override
	public void deleteCheck(long id) {
		checkHistoryRepository.deleteById(id);
	}
	@Override
	public List<CheckHistory> searchDate(Date date) {
		List<CheckHistory> allList = checkHistoryRepository.findAll();
		List<CheckHistory> resultList= new ArrayList<>();
		for (CheckHistory i : allList) {
			if(i.getDrawDate().equals(date)) {
				resultList.add(i);
			}
		}
		return resultList;
	}
	@Override
	public List<CheckHistory> searchResult(String result) {
		List<CheckHistory> allList = checkHistoryRepository.findAll();
		List<CheckHistory> resultList= new ArrayList<>();
		for (CheckHistory i : allList) {
			if(i.getOutcome().toLowerCase().contains(result.toLowerCase())) {
				resultList.add(i);
			}
		}
		return resultList;
	}
	

}
