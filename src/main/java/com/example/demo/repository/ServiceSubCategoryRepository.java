package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.ServiceSubCategory;

public interface ServiceSubCategoryRepository extends JpaRepository<ServiceSubCategory, Integer> {
	ServiceSubCategory  findByServiceName(String serviceName);
	
	long count();
	
}
