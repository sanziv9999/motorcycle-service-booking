package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.BikeManufactureCompany;

public interface BikeManufactureCompanyRepository extends JpaRepository<BikeManufactureCompany, Integer> {

	Optional<BikeManufactureCompany> findByCompanyName(String companyName);
	

	long count();
}
