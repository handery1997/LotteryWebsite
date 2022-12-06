package com.lottery.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lottery.project.model.Account;

public interface AccountRepository extends JpaRepository<Account, Long>{

}
