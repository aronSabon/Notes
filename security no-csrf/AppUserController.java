package com.apptechnosoft.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")

public class AppUserController {
	@Autowired
	AppUserService appUserService;

	private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);

	@GetMapping("/userList")
	private String getUserList(Model model) {
		model.addAttribute("userList",appUserService.getAllUser());
		return"AppUserList";
	}	
	
	@GetMapping("/deleteUser")
	private String getUserList(@RequestParam int id) {
		appUserService.deleteUserById(id);
		return"redirect:/admin/userList";
	}

	@GetMapping("/editUser")
	private String editUser(@RequestParam int id,Model model) {
		try {

			model.addAttribute("userRoles", UserRole.values());
			model.addAttribute("userModel",appUserService.getUserById(id));
			return"EditAppUser";
		} catch (Exception e) {
			  e.printStackTrace();
		        return "error"; // A fallback view for errors		}
		}
	}

	@PostMapping("/updateUser")
	private String updateUser(@ModelAttribute AppUser appUser, RedirectAttributes redirectAttributes) {
		try {
		System.out.println(appUser.getUsername());
		String previousPassword = appUserService.getUserById(appUser.getId()).getPassword();
		String newPassword = appUser.getPassword();
		System.out.println("old"+previousPassword);
		System.out.println("new:"+newPassword);
		if (appUser.getPassword() != null && !appUser.getPassword().isEmpty() 
		        && !appUser.getPassword().equals(previousPassword)) {
		    appUser.setPassword(encoder.encode(appUser.getPassword()));
		}
		appUserService.updateUser(appUser);
		redirectAttributes.addFlashAttribute("message", "User update successful!");
		redirectAttributes.addFlashAttribute("status", "success");
	} catch (Exception e) {
		// Catch any exception that occurs during the save operation
		redirectAttributes.addFlashAttribute("message", "User update failed!");
		redirectAttributes.addFlashAttribute("status", "error");
	}
		return"redirect:/admin/userList";
	}
	@GetMapping("/register")
	private String register(Model model) {
		model.addAttribute("userRoles", UserRole.values());
		System.out.println(UserRole.values());
		System.out.println("check check");
		return"AddAppUser";
	}

	@PostMapping("/register")
	private String postRegister(@ModelAttribute AppUser appUser,Model model,RedirectAttributes redirectAttributes) {
		try {
		AppUser userCheck = appUserService.findByUsername(appUser.getUsername());
		if(userCheck!=null) {
			model.addAttribute("message","username already exists!!!");
			return"AddAppUser";
		}
		appUser.setPassword(encoder.encode(appUser.getPassword()));
		appUserService.addUser(appUser);
		redirectAttributes.addFlashAttribute("message", "Add user successful!");
		redirectAttributes.addFlashAttribute("status", "success");
	} catch (Exception e) {
		// Catch any exception that occurs during the save operation
		redirectAttributes.addFlashAttribute("message", "Add user failed!");
		redirectAttributes.addFlashAttribute("status", "error");
	}
		return "redirect:/admin/userList";
	}
}