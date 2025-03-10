package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.ServiceType;

public interface ServiceTypeRepository extends JpaRepository<ServiceType, Integer>{

	Optional<ServiceType> findByServiceType(String serviceType);

	
	long count();
}
