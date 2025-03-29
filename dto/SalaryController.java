package com.apptechnosoft.controller.backend;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.apptechnosoft.dto.SalaryDetailsDTO;
import com.apptechnosoft.service.SalaryService;


@Controller
public class SalaryController {
	@Autowired
	SalaryService salaryService;
	
	
	@GetMapping("/salariesList")
	private String getsalaryList(Model model) {
		 List<SalaryDetailsDTO> salaryList = salaryService.getAllSalariesWithDoctorOrEmployee();
	        model.addAttribute("salaryList", salaryList);
		return"SalaryList";
	}

}
