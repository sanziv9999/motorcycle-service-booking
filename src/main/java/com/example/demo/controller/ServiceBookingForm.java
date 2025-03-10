package com.example.demo.controller;

import java.awt.Dialog.ModalExclusionType;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import com.example.demo.esewaSecurity.SecurityKeyGen;
import com.example.demo.mailsender.ServiceBookedMail;
import com.example.demo.mailsender.mailSender;
import com.example.demo.model.BikeManufactureCompany;
import com.example.demo.model.BikeModel;
import com.example.demo.model.ServiceBooking;
import com.example.demo.model.ServiceSubCategory;
import com.example.demo.model.ServiceType;
import com.example.demo.model.User;
import com.example.demo.model.WorkStatus;
import com.example.demo.repository.BikeManufactureCompanyRepository;
import com.example.demo.repository.BikeModelRepository;
import com.example.demo.repository.ServiceBookingRepository;
import com.example.demo.repository.ServiceSubCategoryRepository;
import com.example.demo.repository.ServiceTypeRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.WorkStatusRepository;
import com.fasterxml.jackson.annotation.JsonCreator.Mode;

import jakarta.servlet.http.HttpSession;

@Controller
public class ServiceBookingForm {

	@Autowired
	private BikeManufactureCompanyRepository bmcRepo;
	@Autowired
	private BikeModelRepository bmRepo;
	
	@Autowired
	private ServiceBookingRepository sbRepo;
	
	@Autowired
	private UserRepository uRepo;
	
	@Autowired
	private ServiceTypeRepository stRepo;
	
	@Autowired
	private ServiceSubCategoryRepository sscRepo;
	
	@Autowired
	private WorkStatusRepository wsRepo;
	
	
	
	
	
	@GetMapping("book")
	public String homePageBook(Model model, HttpSession session){
		String username = (String) session.getAttribute("username");
		if(session.getAttribute("role").equals("customer")) {
			model.addAttribute("message","Welcome");
			return "servicebookingForm.html";
		}else {
			model.addAttribute("errormessage","Please login first.");
			return "index.html";
		}
		
	}
	
	
	@GetMapping("/serviceBookingForm")
	
	public String serviveBookingForm(Model model){
		
		List<BikeManufactureCompany> bmcList=bmcRepo.findAll();
		model.addAttribute("bmcList", bmcList);
		
		List<BikeModel> bmList = bmRepo.findAll();
		model.addAttribute("bmList", bmList);
		
		List<ServiceType> stList = stRepo.findAll();
		model.addAttribute("stList", stList );
		
		
		List<ServiceSubCategory> sscList = sscRepo.findAll();
		model.addAttribute("sscList", sscList);
		
		List<WorkStatus> wsList = wsRepo.findAll();
		model.addAttribute("wsList", wsList);
				
		
		 
		return "serviceBookingForm.html";
	}
	
	@PostMapping("/serviceBook")
	public String serviceBook(Model model, @ModelAttribute ServiceBooking sb, HttpSession session, @RequestParam("date") String date, @RequestParam("serviceName") String serviceName){
		String email = (String) session.getAttribute("email");
		if(sbRepo.findStatusByEmail(email)!="completed") {
			
		
		
		List<BikeManufactureCompany> bmcList=bmcRepo.findAll();
		model.addAttribute("bmcList", bmcList);
		
		List<BikeModel> bmList = bmRepo.findAll();
		model.addAttribute("bmList", bmList);
		
		List<ServiceType> stList = stRepo.findAll();
		model.addAttribute("stList", stList );
		
		
		List<ServiceSubCategory> sscList = sscRepo.findAll();
		model.addAttribute("sscList", sscList);
		
		ServiceSubCategory subCat = sscRepo.findByServiceName(serviceName);
		
		if(subCat!=null) {
			sb.setServiceImg(subCat.getServiceImg());
		}
		
		
		sbRepo.save(sb);
		new ServiceBookedMail().sendBookedMessage(email, date);
		
		
		model.addAttribute("message", "Service have been booked successfully!");
		
		return "serviceBookingForm.html";
		}else {
			List<BikeManufactureCompany> bmcList=bmcRepo.findAll();
			model.addAttribute("bmcList", bmcList);
			
			List<BikeModel> bmList = bmRepo.findAll();
			model.addAttribute("bmList", bmList);
			
			List<ServiceType> stList = stRepo.findAll();
			model.addAttribute("stList", stList );
			
			
			List<ServiceSubCategory> sscList = sscRepo.findAll();
			model.addAttribute("sscList", sscList);
			
			model.addAttribute("errormessage", "Your previous service haven't been complted");
			return "serviceBookingForm.html";
		}
		
	}
	
	@GetMapping("serviceBookedPage")
	public String serviceBookedPage( Model model) {
		
		List<ServiceBooking> sbList = sbRepo.findAll();
		
		model.addAttribute("sbList",sbList);
		
		List<WorkStatus> wsList = wsRepo.findAll();
		model.addAttribute("wsList", wsList);
		
		return "serviceBookedPage.html";
	}
	
	@GetMapping("userBookedView")
	public String userBookedView( Model model, HttpSession session) {
		
		List<ServiceBooking> sbList = sbRepo.findByEmail((String) session.getAttribute("email"));
		
		model.addAttribute("sbList",sbList);
		
		List<WorkStatus> wsList = wsRepo.findAll();
		model.addAttribute("wsList", wsList);
		
		return "userBookedView.html";
	}
	
	@GetMapping("/cancelBooking/{id}")
	public String calcelBooking(Model model, @PathVariable int id, HttpSession session) {
		
		try{
			ServiceBooking sb = sbRepo.getById(id);
			if(sb != null) {
				sb.setStatus("Cancelled");
				sbRepo.save(sb);
				new ServiceBookedMail().sendCancelledMessage((String) session.getAttribute("email"));
			}else {
				System.out.println("Booked id not found");
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		List<ServiceBooking> sbList = sbRepo.findByEmail((String) session.getAttribute("email"));
		model.addAttribute("sloibList",sbList);
		return "userBookedView.html";
	}
	
	@GetMapping("/deleteData/{id}")
	
	public String deleteData(Model model, @PathVariable int id){
		sbRepo.deleteById(id);
		
		List<ServiceBooking> sbList = sbRepo.findAll();
		
		model.addAttribute("sbList",sbList);
		
		List<WorkStatus> wsList = wsRepo.findAll();
		model.addAttribute("wsList", wsList);
		
		model.addAttribute("sbList",sbList);
		return "serviceBookedPage.html";
	}
	
	

	@GetMapping("/statusEdit/{id}")
	public String statusEdit(@PathVariable int id , Model model) {
		
		List<BikeManufactureCompany> bmcList=bmcRepo.findAll();
		model.addAttribute("bmcList", bmcList);
		
		List<BikeModel> bmList = bmRepo.findAll();
		model.addAttribute("bmList", bmList);
		
		List<ServiceType> stList = stRepo.findAll();
		model.addAttribute("stList", stList );
		
		
		List<ServiceSubCategory> sscList = sscRepo.findAll();
		model.addAttribute("sscList", sscList);
		
		ServiceBooking sb = sbRepo.getById(id);
		model.addAttribute("sb", sb);
		
		List<WorkStatus> wsList = wsRepo.findAll();
		model.addAttribute("wsList", wsList);
		return "adminEditForm.html";
		
	}
	
	@PostMapping("editBookedService")
	public String editBookedService(Model model ,@ModelAttribute ServiceBooking sb, HttpSession session) { 
		
		sbRepo.save(sb);
		
		List<ServiceBooking> sbList = sbRepo.findAll();
		
		model.addAttribute("sbList",sbList);
		
		List<WorkStatus> wsList = wsRepo.findAll();
		model.addAttribute("wsList", wsList);
		
		return "serviceBookedPage.html";
	}
	
	
	@GetMapping("bikeCompanyAndModels")
	public String bikeCompanyAndModels(Model model){
		
		List<BikeManufactureCompany> bmcList=bmcRepo.findAll();
		model.addAttribute("bmcList", bmcList);
		
		List<BikeModel> bmList = bmRepo.findAll();
		model.addAttribute("bmList", bmList);
		
		return "bikeCompany.html";
	}
	
	@GetMapping("servicesTypes")
	
	public String servicesTypes(Model model){
		
		List<ServiceType> stList = stRepo.findAll();
		model.addAttribute("stList", stList );
		
		
		List<ServiceSubCategory> sscList = sscRepo.findAll();
		model.addAttribute("sscList", sscList);
		
		return "serviceDetails.html";
	}
	
	@GetMapping("/payBill/{id}/{serviceName}")
	public String payBill(@PathVariable String serviceName, HttpSession session, Model model){
		ServiceSubCategory ssc = sscRepo.findByServiceName(serviceName);
		if(ssc!=null) {
			
			String key="8gBm/:&EnhH.1/q";
			String uuid = UUID.randomUUID().toString();
			int price= (int)ssc.getPrice();
			String totalPrice =String.valueOf(price);
			String message = "total_amount=" + totalPrice + ",transaction_uuid=" + uuid + ",product_code=" + serviceName;

			
			String message1 = "total_amount=100,transaction_uuid=11-201-13,product_code=EPAYTEST";
			String signature = new SecurityKeyGen().generateHmacSha256(key, message);
			System.out.println(signature);
			
			
			session.setAttribute("id",uuid );
			session.setAttribute("serviceName", serviceName);
			session.setAttribute("price",totalPrice );
			session.setAttribute("signature", signature);
			session.setAttribute("fieldName", message);
			System.out.println(totalPrice);
			
			
			List<ServiceBooking> sbList = sbRepo.findByEmail((String) session.getAttribute("email"));
			
			model.addAttribute("sbList",sbList);
			
			List<WorkStatus> wsList = wsRepo.findAll();
			model.addAttribute("wsList", wsList);
			
			return "userBookedView.html";
		}
		
		return "userBookedView.html";
	}
	
	
	
	
	
	
	
	
	
}
