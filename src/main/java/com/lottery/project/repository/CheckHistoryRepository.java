package com.lottery.project.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lottery.project.model.CheckHistory;

@Repository
public interface CheckHistoryRepository extends JpaRepository<CheckHistory,Long> {
	Page<CheckHistory> findByUserIdLike(long userId, Pageable pageable);
}
