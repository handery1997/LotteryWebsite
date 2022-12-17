package com.lottery.project.model;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "checkresulthistory")
public class CheckHistory {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="check_id")
	private long checkId;
	@Column(name = "user_id")
	private long userId;
	@Column(name = "lotteryID")
	private long lotteryId;
	@Column(name = "check_date")
	@JsonFormat(pattern="MM-dd-yyyy")
	private Date checkDate;
	@Column(name = "draw_date")
	@JsonFormat(pattern="MM-dd-yyyy")
	private Date drawDate;
	@Column(name = "lottery_type")
	private String lotteryType;
	@Column(name = "input_number")
	private String checkNumber;
	@Column(name = "outcome")
	private String outcome;

	public CheckHistory(long checkId, long userId, Date checkDate, Date drawDate, long lotteryId, String lotteryType,
			String checkNumber, String outcome) {
		super();
		this.checkId = checkId;
		this.userId = userId;
		this.checkDate = checkDate;
		this.drawDate = drawDate;
		this.lotteryId = lotteryId;
		this.lotteryType = lotteryType;
		this.checkNumber = checkNumber;
		this.outcome = outcome;
	}

	public long getCheckId() {
		return checkId;
	}

	public void setCheckId(long checkId) {
		this.checkId = checkId;
	}

	public String getLotteryType() {
		return lotteryType;
	}

	public void setLotteryType(String lotteryType) {
		this.lotteryType = lotteryType;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public Date getCheckDate() {
		return checkDate;
	}

	public void setCheckDate(Date checkDate) {
		this.checkDate = checkDate;
	}

	public Date getDrawDate() {
		return drawDate;
	}

	public void setDrawDate(Date drawDate) {
		this.drawDate = drawDate;
	}

	public long getLotteryId() {
		return lotteryId;
	}

	public void setLotteryId(long lotteryId) {
		this.lotteryId = lotteryId;
	}

	public String getCheckNumber() {
		return checkNumber;
	}

	public void setCheckNumber(String checkNumber) {
		this.checkNumber = checkNumber;
	}

	public String getOutcome() {
		return outcome;
	}

	public void setOutcome(String outcome) {
		this.outcome = outcome;
	}

	public CheckHistory() {
		super();
	}

}
