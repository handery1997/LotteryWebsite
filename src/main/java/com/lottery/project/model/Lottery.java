package com.lottery.project.model;

import java.sql.Date;
import java.util.Arrays;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "Lottery_DB")
public class Lottery {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "lotteryID")
	private long id;
	@Column(name = "draw_date")
	private Date drawDate;
	@Column(name = "lottery_type")
	private String lotteryType;
	@Column(name = "winning_number_set")
	private String winNumber;
	@Column(name = "special_price")
	private String specialPrice;
	@Column(name = "first_price")
	private String firstPrice;
	@Column(name = "second_price")
	private String secondPrice;
	@Column(name = "third_price")
	private String thirdPrice;

	public Lottery(long id, Date drawDate, String lotteryType, String winNumber, String specialPrice, String firstPrice,
			String secondPrice, String thirdPrice) {
		super();
		this.id = id;
		this.drawDate = drawDate;
		this.lotteryType = lotteryType;
		this.winNumber = winNumber;
		this.specialPrice = specialPrice;
		this.firstPrice = firstPrice;
		this.secondPrice = secondPrice;
		this.thirdPrice = thirdPrice;
	}

	public Lottery() {
		super();
		// TODO Auto-generated constructor stub
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getDrawDate() {
		return drawDate;
	}

	public void setDrawDate(Date drawDate) {
		this.drawDate = drawDate;
	}

	public String getLotteryType() {
		return lotteryType;
	}

	public void setLotteryType(String lotteryType) {
		this.lotteryType = lotteryType;
	}

	public String getWinNumber() {
		return winNumber;
	}

	public void setWinNumber(String winNumber) {
		this.winNumber = winNumber;
	}

	public String getSpecialPrice() {
		return specialPrice;
	}

	public void setSpecialPrice(String specialPrice) {
		this.specialPrice = specialPrice;
	}

	public String getFirstPrice() {
		return firstPrice;
	}

	public void setFirstPrice(String firstPrice) {
		this.firstPrice = firstPrice;
	}

	public String getSecondPrice() {
		return secondPrice;
	}

	public void setSecondPrice(String secondPrice) {
		this.secondPrice = secondPrice;
	}

	public String getThirdPrice() {
		return thirdPrice;
	}

	public void setThirdPrice(String thirdPrice) {
		this.thirdPrice = thirdPrice;
	}
	public String[] specialArray() {
		String [] special = specialPrice.split(", ");
		Arrays.sort(special);
		return special;
	}
	public String[] firstArray() {
		String [] first = firstPrice.split(", ");
		Arrays.sort(first);
		return first;
	}
	public String[] secondArray() {
		String [] second = secondPrice.split(", ");
		Arrays.sort(second);
		return second;
	}
	public String[] thirdArray() {
		String [] third = thirdPrice.split(", ");
		Arrays.sort(third);
		return third;
	}
	public String[] winArray() {
		String [] win = winNumber.split(", ");
		Arrays.sort(win);
		return win;
	}
	
}
