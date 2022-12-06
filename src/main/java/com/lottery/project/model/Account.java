package com.lottery.project.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Account")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Account {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private long id;
	
	@Email(message = "Email is invalid!")
	@NotEmpty
	@Column(name = "user_mail")
	private String userMail;
	
	@NotEmpty(message = "Please input your password!")
	@Size(min = 8, max = 20, message = "Password must have at least 8 entries!")
	@Column(name = "password")
	private String password;

	@Column(name = "account_role")
	private String accountRole;
	@NotEmpty(message = "Please input your name!")
	@Column(name = "full_name")
	private String fullName;
	@NotEmpty(message = "Please input your phone number!")
	@Column(name = "user_phone")
	private String userPhone;
	
	@Column(name = "status")
	private String status;

	

	public Account(long id, String userMail, String password, String accountRole, String fullName, String userPhone,
			String status) {
		super();
		this.id = id;
		this.userMail = userMail;
		this.password = password;
		this.accountRole = accountRole;
		this.fullName = fullName;
		this.userPhone = userPhone;
		this.status = status;
	}

	public Account() {
		super();
		// TODO Auto-generated constructor stub
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUserMail() {
		return userMail;
	}

	public void setUserMail(String userMail) {
		this.userMail = userMail;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAccountRole() {
		return accountRole;
	}

	public void setAccountRole(String accountRole) {
		this.accountRole = accountRole;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	
	
}
