package com.example.demo.controller;

import java.io.File;
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
import com.example.demo.repository.BikeManufactureCompanyRepository;
import com.example.demo.repository.BikeModelRepository;

@Controller
public class bikeModelController {

    @Autowired
    private BikeModelRepository bmRepo;

    @Autowired
    private BikeManufactureCompanyRepository bmcRepo;
	

    @GetMapping("/addbikemodel")
    public String bikemodel() {
        return "BikeModel.html";
    }

    @PostMapping("/bikemodelform")
    public String bikemodelform(@RequestParam("companyName") String companyName,
                                 @RequestParam("modelName") String modelName,
                                 @RequestParam("year") Integer year,
                                 @RequestParam("companyLogo") MultipartFile companyLogo, 
                                 @RequestParam("bikePic") MultipartFile bikePic, Model model1) {

        Optional<BikeManufactureCompany> existingCompanyOptional = bmcRepo.findByCompanyName(companyName);

        BikeManufactureCompany company;
        if (existingCompanyOptional.isPresent()) {
            company = existingCompanyOptional.get();
        } else {
            company = new BikeManufactureCompany();
            company.setCompanyName(companyName);
            company.setCompanyLogo(companyLogo.getOriginalFilename());

            // Save the company with the new logo
            company = bmcRepo.save(company); 

            // Now handle saving the company logo file
            try {
                File saveDir = new ClassPathResource("static/assets").getFile();
                Path companyLogoPath = Paths.get(saveDir.getAbsolutePath() + File.separator + companyLogo.getOriginalFilename());
                Files.copy(companyLogo.getInputStream(), companyLogoPath, StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Company Logo saved to: " + companyLogoPath);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Create and save the bike model
       
    	BikeModel model = new BikeModel();
        model.setCompany(company);
        model.setModelName(modelName);
        model.setYear(year);
        model.setBikePic(bikePic.getOriginalFilename());
        
        bmRepo.save(model);
        
        if (!bikePic.isEmpty()) {
            // Now handle saving the bike picture file
            try {
                File saveDir = new ClassPathResource("static/assets").getFile();
                String fileName = bikePic.getOriginalFilename();
                Path bikePicPath = Paths.get(saveDir.getAbsolutePath() + File.separator + bikePic.getOriginalFilename());
                Files.copy(bikePic.getInputStream(), bikePicPath, StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Bike Picture saved to: " + bikePicPath);
                model.setBikePic(fileName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        List<BikeManufactureCompany> bmcList=bmcRepo.findAll();
		model1.addAttribute("bmcList", bmcList);
		
		List<BikeModel> bmList = bmRepo.findAll();
		model1.addAttribute("bmList", bmList);
        
        return "bikeCompany.html"; // Redirect to the add bike model page after form submission
    }

}
