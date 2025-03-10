package com.example.demo.controller;



import java.util.Optional;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.mailsender.PasswordChangeMessage;
import com.example.demo.mailsender.mailSender;
import com.example.demo.model.User;
import com.example.demo.repository.ServiceBookingRepository;
import com.example.demo.repository.StockAndProductsRepository;
import com.example.demo.repository.UserRepository;
import com.fasterxml.jackson.annotation.JsonCreator.Mode;

import jakarta.servlet.http.HttpSession;


@Controller
public class signupController {
	@Autowired
	private UserRepository uRepo;
	
	@Autowired
	private ServiceBookingRepository sbRepo;
	
	@Autowired
	private StockAndProductsRepository sapRepo;

	@GetMapping("/")
	public String landing() {
		return "index.html";
	}

	@PostMapping("/register")
	public String signup(@ModelAttribute User u, Model model) {
		
		
		Optional<User> existingUser = uRepo.findByEmail(u.getEmail());
		if(existingUser.isPresent()) {
			model.addAttribute("errormessage", "Email already exist! Try new one");
			return "index.html";
		}
		
	 	Optional<User> existingPhone = uRepo.findByPhone(u.getPhone());
		    if(existingPhone.isPresent()) {
		        model.addAttribute("errormessage", "Phone number already exists! Please try a new one.");
		        return "index.html";
		    }
		String hashPwd = DigestUtils.sha3_256Hex(u.getPassword());
		u.setPassword(hashPwd);
		uRepo.save(u);
		model.addAttribute("message", "Signup successful!!");
		return "index.html";
	}

	@PostMapping("/login")
	public String login(@ModelAttribute User u, Model model, HttpSession session) {
		if (uRepo.existsByEmailAndPassword(u.getEmail(), DigestUtils.sha3_256Hex(u.getPassword()))) {
			String username = uRepo.findUsernameByEmail(u.getEmail());
			String role = uRepo.findRoleByEmail(u.getEmail());
			if (username != null) {
				session.setAttribute("role", role);
				model.addAttribute("email", u.getEmail());
				model.addAttribute("message", "Login successful! Please click on dashborad.");
				session.setAttribute("email", u.getEmail());
				session.setAttribute("username", username);

				System.out.println(session.getAttribute("role"));
				System.out.println(session.getAttribute("email"));
				session.setMaxInactiveInterval(1800);
				return "index.html";
			}
			

		}
		model.addAttribute("errormessage", "Username or password incorrect. Please try again!");
		return "index.html";
	}

	@GetMapping("/adminDash")
	public String dashboard(HttpSession session, Model model) {

		if (session.getAttribute("username") != null && session.getAttribute("role").equals("admin")) {
			String username = (String) session.getAttribute("username");
			System.out.println(session.getAttribute("email"));
			session.setAttribute("totalBookings", sbRepo.count());
			session.setAttribute("totalStocks", sapRepo.count());
			session.setAttribute("totalStockPrice", sapRepo.calculateTotalStockPrice());
			
			model.addAttribute(username, username);
			return "adminDash.html";
		} else {
			model.addAttribute("errormessage", "Admin can only access this page");
			return "index.html";
		}

	}

	@GetMapping("dashboard")
	public String dashborad(HttpSession session, Model model) {
		if (session.getAttribute("username") != null && session.getAttribute("role").equals("customer")) {

			String username = (String) session.getAttribute("username");
			model.addAttribute(username, username);
			return "userdash.html";

		} else {
			model.addAttribute("errormessage", "Customer can only access this page");
			return "index.html";
		}
	}

	@GetMapping("logout")
	public String logout(HttpSession session, Model model) {
		session.invalidate();
		model.addAttribute("logoutMessage", "You have been logged out successfully.");
		return "index.html";

	}

	@PostMapping("otpSender")
	public String otpSender(Model model, @RequestParam("email") String email, HttpSession session) {
		int otp = new mailSender().sendOtp(email);
		session.setAttribute("otp", otp);
		session.setAttribute("email", email);

		System.out.println(otp + email);
		return "index.html";

	}

	
	  @PostMapping("checkOtp") public String otpChecker(HttpSession
	  session, @RequestParam("otpcode")Integer otpcode){
	  if(session.getAttribute("otp")!=null) {
	  
	  int otp = (int) session.getAttribute("otp"); 
	  if(otp == otpcode) { 
		  String status="matched"; 
		  session.setAttribute("status", status);
		  System.out.println(session.getAttribute("status"));
		  
		  }else { 
			  String status="mismatch"; 
			  session.setAttribute("status", status);
			  System.out.println(session.getAttribute("status"));
	  
	  } 
	  
	  }else {
		  return "index.html" ; 
	  }
	  
	  	
	    return "index.html";
	  }
	  
	  
	  @PostMapping("newPassword")
	  public String newPassword(@ModelAttribute User u, HttpSession session, @RequestParam("newPassword") String newPassword){
		  String email =  (String) session.getAttribute("email");
		  if(email != null) {
			  	String hashPwd = DigestUtils.sha3_256Hex(newPassword);
				u.setPassword(hashPwd);
			  	uRepo.updatePasswordByEmail(email, hashPwd);
				new PasswordChangeMessage().sendPasswordChangeMessage(email);
				System.out.println("password changed successfully" + hashPwd);
				session.invalidate();
		  }
		  return "index.html";
		  
		  
	  }
	  
	  @GetMapping("editProfile")
	  
	  public String profileChange() {
		  return "";
	  }
	  
	  
	  @PostMapping("editProfile")
	  public String changeProfile(){
		  return "";
	  }
	  
	  
	  
	 

}
