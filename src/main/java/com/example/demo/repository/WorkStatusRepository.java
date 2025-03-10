package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.WorkStatus;

public interface WorkStatusRepository extends JpaRepository<WorkStatus, Integer> {

}
