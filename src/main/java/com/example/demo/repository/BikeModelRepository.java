package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.BikeManufactureCompany;
import com.example.demo.model.BikeModel;

public interface BikeModelRepository extends JpaRepository<BikeModel, Integer> {
	
	List<BikeModel> findByCompany(BikeManufactureCompany company);

	List<BikeModel> findByCompanyId(int id);
	

	long count();
	
}
