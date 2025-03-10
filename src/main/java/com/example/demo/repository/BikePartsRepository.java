package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.BikeParts;

public interface BikePartsRepository extends JpaRepository<BikeParts, Integer> {

	
	long count();
	
}
