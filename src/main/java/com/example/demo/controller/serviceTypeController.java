package com.example.demo.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.model.BikeManufactureCompany;
import com.example.demo.model.BikeModel;
import com.example.demo.model.ServiceSubCategory;
import com.example.demo.model.ServiceType;
import com.example.demo.model.StockAndProducts;
import com.example.demo.repository.ServiceSubCategoryRepository;
import com.example.demo.repository.ServiceTypeRepository;

@Controller
public class serviceTypeController {
	@Autowired
	private ServiceTypeRepository stRepo;
	@Autowired
	private ServiceSubCategoryRepository sscRepo;
	
	
	@PostMapping("/addServiceTypeForm")
	public String addServiceType( @RequestParam("serviceName") String serviceName, @RequestParam("serviceType") String serviceType, @RequestParam("price") float price , @RequestParam("serviceImg") MultipartFile serviceImg, Model model){
		
		Optional<ServiceType> existingServiceType = stRepo.findByServiceType(serviceType);
		
		
		ServiceType stype = new ServiceType();
		if(existingServiceType.isPresent()) {
			stype = existingServiceType.get();
		}else {
			stype = new ServiceType();
			stype.setServiceType(serviceType);
			stRepo.save(stype);
		}
		
		ServiceSubCategory subCat = new ServiceSubCategory();
		subCat.setType(stype);
		subCat.setServiceName(serviceName);
		subCat.setPrice(price);
		subCat.setServiceImg(serviceImg.getOriginalFilename());
		ServiceSubCategory sc = sscRepo.save(subCat);
		if(sc!=null) {
			try {
                File saveDir = new ClassPathResource("static/assets").getFile();
                Path imgPath = Paths.get(saveDir.getAbsolutePath(), serviceImg.getOriginalFilename());
                Files.copy(serviceImg.getInputStream(), imgPath, StandardCopyOption.REPLACE_EXISTING);

                // Set the itemPic property with the file path
                subCat.setServiceImg(imgPath.toString());

            	List<ServiceType> stList = stRepo.findAll();
        		model.addAttribute("stList", stList );
        		
        		
        		List<ServiceSubCategory> sscList = sscRepo.findAll();
        		model.addAttribute("sscList", sscList);
        		
      
                return "serviceDetails.html";
            } catch (IOException e) {
                e.printStackTrace();
            	List<ServiceType> stList = stRepo.findAll();
        		model.addAttribute("stList", stList );
        		
        		
        		List<ServiceSubCategory> sscList = sscRepo.findAll();
        		model.addAttribute("sscList", sscList);
                return "serviceDetails.html"; // Some error page
            }
			
		}
		
	
		
		
		
		return "serviceDetails.html";
	}
	
	

}
