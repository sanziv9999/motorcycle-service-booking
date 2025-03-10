package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.model.StockAndProducts;

public interface StockAndProductsRepository extends JpaRepository<StockAndProducts, Integer> {

	List<StockAndProducts> findAllById(int id);
	
	long count();

	@Query("SELECT SUM(sp.quantity * sp.unitPrice) FROM StockAndProducts sp")
    float calculateTotalStockPrice();
}
