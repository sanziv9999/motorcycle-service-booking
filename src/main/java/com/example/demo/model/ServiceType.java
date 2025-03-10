package com.example.demo.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Table
@Entity
public class ServiceType {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private String serviceType;
	
	@OneToMany(mappedBy = "Type")
	private List<ServiceSubCategory> serviceList;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getServiceType() {
		return serviceType;
	}
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	public List<ServiceSubCategory> getServiceList() {
		return serviceList;
	}
	public void setServiceList(List<ServiceSubCategory> serviceList) {
		this.serviceList = serviceList;
	}
	
	
	
	
	

}
