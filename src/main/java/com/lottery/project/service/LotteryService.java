package com.lottery.project.service;

import java.util.Date;
import java.util.List;


import org.springframework.data.domain.Page;

import com.lottery.project.model.Lottery;

public interface LotteryService {
	List<Lottery> getAllLottery();
	void saveLottery(Lottery lottery);
	Lottery getLotteryByID(long id);
	Page<Lottery> findPaginated(int pageNo,int pageSize);
	Lottery findByDate(Date date, String type);
	String checkMax(String number, Lottery lottery);
	public String checkMega(String [] numberArr, Lottery lottery);
	boolean validateMega(String [] arr);
	boolean validateMax(String [] arr);
	List<Lottery> getMaxLottery();	
	List<Lottery> getMegaLottery();	
	void deleteLottery(long id);
	Lottery getLastestMax();
	Lottery getLastestMega();
	boolean validateDateMax(Date date);
	boolean validateDateMega(Date date);

}
