package com.lottery.project.service;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Date;

import org.hibernate.query.criteria.internal.expression.function.AggregationFunction.COUNT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.lottery.project.model.Account;
import com.lottery.project.model.Lottery;
import com.lottery.project.repository.LotteryRepository;

@Service
public class LotteryServiceImp implements LotteryService {
	@Autowired
	private LotteryRepository lotteryRepository;

	@Override
	public List<Lottery> getAllLottery() {
		return lotteryRepository.findAll(Sort.by("drawDate").ascending());
	}

	@Override
	public void saveLottery(Lottery lottery) {
		this.lotteryRepository.save(lottery);

	}

	@Override
	public Lottery getLotteryByID(long id) {
		Optional<Lottery> optional = lotteryRepository.findById(id);
		Lottery lottery = null;
		if (optional.isPresent()) {
			lottery = optional.get();
		} else {
			throw new RuntimeException("Id non exist!");
		}
		return lottery;
	}

	@Override
	public Page<Lottery> findPaginated(int pageNo, int pageSize) {
		Sort sort = Sort.by("drawDate").descending();
		Pageable pageable = PageRequest.of(pageNo -1, pageSize,sort);
		return this.lotteryRepository.findAll(pageable);
	}

	@Override
	public Lottery findByDate(Date date, String type) {
		List<Lottery> list = getAllLottery();
		for(Lottery i:list) {
			if(i.getDrawDate().equals(date) && i.getLotteryType().equals(type)) {
				return i;
			}
		}
		return null;
	}

	@Override
	public String checkMax(String number, Lottery lottery) {
		String[] special = lottery.getSpecialPrice().split(", ");
		String[] first = lottery.getFirstPrice().split(", ");
		String[] second = lottery.getSecondPrice().split(", ");
		String[] third = lottery.getThirdPrice().split(", ");
		String result ="";
		for(String i : special) {
			if(number.equals(i)) {
				result+="Special price";
				break;
			}	
		}
		
		for(String i : first) {
			if(number.equals(i)) {
				if(result.equals("")) {
					result+="First price";
				break;
				} else {
					result+=", first price";
				}
			}	
		}
		for(String i : second) {
			if(number.equals(i)) {
				if(result.equals("")) {
					result+="Second price";
				break;
				} else {
					result+=", second price";
					break;
				}
			}	
		}
		for(String i : third) {
			if(number.equals(i)) {
				if(result.equals("")) {
					result+="Third price";
				break;
				} else {
					result+=", third price";
					break;
				}
			}	
		}
		if(result.equals("")) {
			result="Good luck next time!";
		}
		
		return result;
	}
	@Override
	public String checkMega(String [] numberArr, Lottery lottery) {
		String [] numberSet = lottery.getWinNumber().split(", ");
		String result ="";
		int count=0;
		for(String i:numberArr) {
			for(String j:numberSet) {
				if(i.equals(j)) {
					count += 1;
					break;
				}
				
			}
		}
		if(count<=2) {
			result = "Good luck next time!";
			
		}else if(count == 3){
			result = "third price";
		}else if(count == 4){
			result = "second price";
		}else if(count == 5){
			result = "first price";
		}
		else if(count == 6){
			result = "jackpot";
		}
		return result;
	}

	@Override
	public boolean validateMega(String[] arr) {
		for(int i = 0; i<arr.length-1;i++) {
			if(Integer.parseInt(arr[i])>45) {
				return false;
			}
			if(Integer.parseInt(arr[i+1])>45) {
				return false;
			}
			if(arr[i].equals(arr[i+1])) {
				return false;
			}
		}
		return true;
	}
	@Override
	public boolean validateMax(String[] arr) {
		for(int i = 0; i<arr.length-1;i++) {
			if(arr[i].equals(arr[i+1])) {
				return false;
			}
		}
		return true;
	}

	@Override
	public List<Lottery> getMaxLottery() {
		List<Lottery> list = getAllLottery();
		List<Lottery> listMax = new ArrayList<>();
		for(Lottery i:list) {
			if(i.getLotteryType().equals("max")) {
				listMax.add(i);
			}
		}
		
		return listMax;
	}

	@Override
	public List<Lottery> getMegaLottery() {
		List<Lottery> list = getAllLottery();
		List<Lottery> listMega = new ArrayList<>();
		for(Lottery i:list) {
			if(i.getLotteryType().equals("mega")) {
				listMega.add(i);
			}
		}
		return listMega;
	}

	@Override
	public void deleteLottery(long id) {
		lotteryRepository.deleteById(id);
	}

	@Override
	public Lottery getLastestMax() {
		List<Lottery> list = getMaxLottery();
		Comparator<Lottery> lotteryDateComparator = Comparator.comparing(Lottery::getDrawDate);
		Lottery lottery = list.stream().max(lotteryDateComparator).get();
		return lottery;
	}

	@Override
	public Lottery getLastestMega() {
		List<Lottery> list = getMegaLottery();
		Comparator<Lottery> lotteryDateComparator = Comparator.comparing(Lottery::getDrawDate);
		Lottery lottery = list.stream().max(lotteryDateComparator).get();
		return lottery;
	}

	@Override
	public boolean validateDateMax(Date date) {
		List<Lottery>list=getMaxLottery();
		Date currentDate = new Date();
		for(Lottery a: list) {
			if(a.getDrawDate().equals(date)) {
				return false;
			}
		} 
		if(date.before(currentDate)) {
			return false;
		}
		return true;
	}

	@Override
	public boolean validateDateMega(Date date) {
		List<Lottery>list=getMegaLottery();
		Date currentDate = new Date();
		for(Lottery a: list) {
			if(a.getDrawDate().equals(date)) {
				return false;
			}
			
		} 
		if(date.before(currentDate)) {
			return false;
		}
		return true;
	}

}
