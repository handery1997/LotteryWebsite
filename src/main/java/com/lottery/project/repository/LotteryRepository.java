package com.lottery.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lottery.project.model.Lottery;

public interface LotteryRepository extends JpaRepository<Lottery, Long>{

}
