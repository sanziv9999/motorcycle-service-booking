package com.example.demo.model;


import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table
@DynamicUpdate
public class BikeModel {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@ManyToOne
	@JoinColumn(name="companyId")
	private BikeManufactureCompany company;
	
	private String modelName;
	
	private String bikePic;
	
	private int year;
	
	
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getModelName() {
		return modelName;
	}
	public void setModelName(String modelName) {
		this.modelName = modelName;
	}
	public BikeManufactureCompany getCompany() {
		return company;
	}
	public void setCompany(BikeManufactureCompany company) {
		this.company = company;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public String getBikePic() {
		return bikePic;
	}
	public void setBikePic(String bikePic) {
		this.bikePic = bikePic;
	}
	
	
	
	
	
	
	
	
	
	

}
