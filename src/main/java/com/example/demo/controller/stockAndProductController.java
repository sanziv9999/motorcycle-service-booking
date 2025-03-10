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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.model.BikeManufactureCompany;
import com.example.demo.model.BikeModel;
import com.example.demo.model.StockAndProducts;
import com.example.demo.repository.BikeManufactureCompanyRepository;
import com.example.demo.repository.BikeModelRepository;
import com.example.demo.repository.BikePartsRepository;
import com.example.demo.repository.StockAndProductsRepository;
import com.fasterxml.jackson.annotation.JsonCreator.Mode;

@Controller
public class stockAndProductController {

    @Autowired
    private StockAndProductsRepository sapRepo;
	@Autowired
	private BikeManufactureCompanyRepository bmcRepo;
	@Autowired
	private BikeModelRepository bmRepo;
	
	@Autowired   
	private BikePartsRepository bpRepo;
	
    

    @GetMapping("/StockManagement")
    public String stockManagement(Model model) {
    	List<BikeModel> bmList = bmRepo.findAll();
		model.addAttribute("bmList", bmList);
		List<BikeManufactureCompany> bmcList=bmcRepo.findAll();
		model.addAttribute("bmcList", bmcList);
		
		
		List<StockAndProducts> spList = sapRepo.findAll();
		model.addAttribute("spList", spList);
        return "Stocks.html";
    }

    @PostMapping("/stockAdding")
    public String addStock(@RequestParam("itemPic") MultipartFile itemPic,
                           @RequestParam("itemName") String itemName,
                           @RequestParam("itemType") String itemType,
                           @RequestParam("model") String bikemodel,
                           @RequestParam("brand") String brand,
                           @RequestParam("quantity") int quantity,
                           @RequestParam("unitPrice") int unitPrice,
                           @RequestParam("purchaseDate") String purchaseDate,
                           @RequestParam("description") String desc, Model model) {

        try {
            StockAndProducts sp = new StockAndProducts();
            sp.setItemName(itemName);
            sp.setBrand(brand);

            sp.setItemType(itemType);
            sp.setModel(bikemodel);
            sp.setQuantity(quantity);
            sp.setUnitPrice(unitPrice);
            sp.setDescription(desc);
            sp.setPurchasedDate(purchaseDate);
            sp.setItemPic(itemPic.getOriginalFilename());

            StockAndProducts saved = sapRepo.save(sp);

            // Save the image file
            if (saved != null) {
                try {
                    File saveDir = new ClassPathResource("static/assets").getFile();
                    Path imgPath = Paths.get(saveDir.getAbsolutePath(), itemPic.getOriginalFilename());
                    Files.copy(itemPic.getInputStream(), imgPath, StandardCopyOption.REPLACE_EXISTING);

                    // Set the itemPic property with the file path
                    sp.setItemPic(imgPath.toString());

                    System.out.println("Item Picture saved to: " + imgPath);
                    List<BikeModel> bmList = bmRepo.findAll();
            		model.addAttribute("bmList", bmList);
            		List<BikeManufactureCompany> bmcList=bmcRepo.findAll();
            		model.addAttribute("bmcList", bmcList);
            		
            		
            		List<StockAndProducts> spList = sapRepo.findAll();
            		model.addAttribute("spList", spList);
                    return "Stocks.html";
                } catch (IOException e) {
                    e.printStackTrace();
                    List<BikeModel> bmList = bmRepo.findAll();
            		model.addAttribute("bmList", bmList);
            		List<BikeManufactureCompany> bmcList=bmcRepo.findAll();
            		model.addAttribute("bmcList", bmcList);
            		
            		
            		List<StockAndProducts> spList = sapRepo.findAll();
            		model.addAttribute("spList", spList);
                    return "Stocks.html"; // Some error page
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<BikeModel> bmList = bmRepo.findAll();
		model.addAttribute("bmList", bmList);
		List<BikeManufactureCompany> bmcList=bmcRepo.findAll();
		model.addAttribute("bmcList", bmcList);
		
		
		List<StockAndProducts> spList = sapRepo.findAll();
		model.addAttribute("spList", spList);
        return "Stocks.html"; // Some error page
    }
    
    
    @GetMapping("/stockEdit/{id}")
    public String stockEdit(@PathVariable int id, Model model) {
    	
    	List<BikeModel> bmList = bmRepo.findAll();
 		model.addAttribute("bmList", bmList);
 		List<BikeManufactureCompany> bmcList=bmcRepo.findAll();
 		model.addAttribute("bmcList", bmcList);
 		
 		
 		StockAndProducts sp = sapRepo.getById(id);
 		model.addAttribute("spList", sp);
    	
    	
        return "editStock.html";
    }
    
    
    @PostMapping("/editStockForm")
    public String editStock(@RequestParam("itemPic") MultipartFile itemPic,
                            @RequestParam("itemName") String itemName,
                            @RequestParam("itemType") String itemType,
                            @RequestParam("model") String bikemodel,
                            @RequestParam("brand") String brand,
                            @RequestParam("quantity") int quantity,
                            @RequestParam("unitPrice") float unitPrice,
                            @RequestParam("purchasedDate") String purchaseDate,
                            @RequestParam("description") String desc,
                            Model model,
                            @RequestParam("id") Integer id) {
        try {
            if (id != null) {
                Optional<StockAndProducts> optionalSp = sapRepo.findById(id);
                if (optionalSp.isPresent()) {
                    StockAndProducts sp = optionalSp.get();
                    sp.setItemName(itemName);
                    sp.setBrand(brand);
                    sp.setItemType(itemType);
                    sp.setModel(bikemodel);
                    sp.setQuantity(quantity);
                    sp.setUnitPrice(unitPrice);
                    sp.setDescription(desc);
                    sp.setPurchasedDate(purchaseDate);
                    if (!itemPic.isEmpty()) {
                        sp.setItemPic(itemPic.getOriginalFilename());
                    }

                    // Save the updated entity
                    StockAndProducts saved = sapRepo.save(sp);

                    // Save the image file
                    if (saved != null) {
                        try {
                            // Save image file
                            File saveDir = new ClassPathResource("static/assets").getFile();
                            Path imgPath = Paths.get(saveDir.getAbsolutePath(), itemPic.getOriginalFilename());
                            Files.copy(itemPic.getInputStream(), imgPath, StandardCopyOption.REPLACE_EXISTING);
                            // Set the itemPic property with the file path
                            sp.setItemPic(imgPath.toString());
                            System.out.println("Item Picture saved to: " + imgPath);
                        } catch (IOException e) {
                            e.printStackTrace();
                            // Handle error if image saving fails
                        }
                    }

                    // Update model attributes and return appropriate view
                    updateModelAttributes(model);
                    return "Stocks.html";
                }
            }

            // If ID is not provided or not found, create a new entity
            StockAndProducts sp = new StockAndProducts();
            sp.setItemName(itemName);
            sp.setBrand(brand);
            sp.setItemType(itemType);
            sp.setModel(bikemodel);
            sp.setQuantity(quantity);
            sp.setUnitPrice(unitPrice);
            sp.setDescription(desc);
            sp.setPurchasedDate(purchaseDate);
            sp.setItemPic(itemPic.getOriginalFilename());

            // Save the new entity
            StockAndProducts saved = sapRepo.save(sp);

            // Save the image file
            if (saved != null) {
                try {
                    // Save image file
                    File saveDir = new ClassPathResource("static/assets").getFile();
                    Path imgPath = Paths.get(saveDir.getAbsolutePath(), itemPic.getOriginalFilename());
                    Files.copy(itemPic.getInputStream(), imgPath, StandardCopyOption.REPLACE_EXISTING);
                    // Set the itemPic property with the file path
                    sp.setItemPic(imgPath.toString());
                    System.out.println("Item Picture saved to: " + imgPath);
                } catch (IOException e) {
                    e.printStackTrace();
                    // Handle error if image saving fails
                }
            }

            // Update model attributes and return appropriate view
            updateModelAttributes(model);
            return "Stocks.html";
        } catch (Exception e) {
            e.printStackTrace();
            // Handle exceptions
            return "error"; // or some error page
        }
    }

    // Helper method to update model attributes
    private void updateModelAttributes(Model model) {
        List<BikeModel> bmList = bmRepo.findAll();
        model.addAttribute("bmList", bmList);
        List<BikeManufactureCompany> bmcList = bmcRepo.findAll();
        model.addAttribute("bmcList", bmcList);
        List<StockAndProducts> spList = sapRepo.findAll();
        model.addAttribute("spList", spList);
    }


    @GetMapping("/stockAdd/{id}")
    public String stockAdd(@PathVariable int id) {
        return "updateStock.html";
    }
    

    @GetMapping("/stockDelete/{id}")
    public String stockDelete(@PathVariable int id, Model model) {
    	
    	sapRepo.deleteById(id);
    	
        List<BikeModel> bmList = bmRepo.findAll();
   		model.addAttribute("bmList", bmList);
   		List<BikeManufactureCompany> bmcList=bmcRepo.findAll();
   		model.addAttribute("bmcList", bmcList);
   		
   		
   		List<StockAndProducts> spList = sapRepo.findAll();
   		model.addAttribute("spList", spList);
   		return "Stocks.html"; // Some error page
    }
}
