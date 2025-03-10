package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.model.BikeManufactureCompany;
import com.example.demo.model.BikeModel;
import com.example.demo.repository.BikeManufactureCompanyRepository;
import com.example.demo.repository.BikeModelRepository;


@Controller
public class Navigation {

	@Autowired
	private BikeManufactureCompanyRepository bmcRepo;
	@Autowired
	private BikeModelRepository bmRepo;
	
	@GetMapping("/home")
	public String home(){
		return "index.html";
	}
	
	@GetMapping("/about")
	public String about() {
		return "about.html";
	}
	
	@GetMapping("/service")
	public String service() {
		return "service.html";
		
	}
	
	@GetMapping("/contact")
	public String contact(){
		return "contact.html";
	}
	
	
	@GetMapping("/booking")
	public String booking(){
		
		
		return "booking.html";
	}
	
	@GetMapping("/team")
	public String team(){
		return "team.html";
	}
	
	@GetMapping("/testimonial")
	public String testimonial(){
		return "testimonial.html";
	}

	
	

	}

	

