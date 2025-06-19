package com.lemon.notes.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.apptechnosoft.constant.DoctorStatus;
import com.apptechnosoft.constant.SalaryStatus;
import com.apptechnosoft.model.Doctor;
import com.apptechnosoft.model.HealthPackage;
import com.apptechnosoft.model.Salary;
import com.apptechnosoft.model.Specialization;

public class ControllerForDynamicSelectInput {

	
	@PostMapping("/addDoctor")
	private String postDoctor(@ModelAttribute Doctor doctor, @RequestParam(required=false) MultipartFile image) {
		if(!image.isEmpty()) {
			try {
				Files.copy(image.getInputStream(), 
						Path.of("src/main/resources/static/images/doctorImages/"+ image.getOriginalFilename()), 
						StandardCopyOption.REPLACE_EXISTING);
				doctor.setImageName(image.getOriginalFilename());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		List<Specialization> specializationList = doctor.getSpecialization().stream().filter(s -> s != null).collect(Collectors.toList());
		doctor.setSpecialization(specializationList);
		doctor.setJoinDate(LocalDate.now());
		doctor.setStatus(DoctorStatus.ACTIVE);
doctorService.addDoctor(doctor);
//		if (doctor.getSalary() == null) {
//		    doctor.setSalary(new ArrayList<>());
//		}

		// Create Salary instance
		Salary salary1 = new Salary();
		LocalDate endOfMonth = doctor.getJoinDate().withDayOfMonth(doctor.getJoinDate().lengthOfMonth());
		int totalDaysInMonth = doctor.getJoinDate().lengthOfMonth();
		int remainingDays = Period.between(doctor.getJoinDate(), endOfMonth).getDays() + 1;

		// Calculate salary for remaining days
		double startingMonthSalary = Math.round(
		    Math.floor((doctor.getSalary() / totalDaysInMonth) * remainingDays) * 100.0) / 100.0;

		salary1.setAmount(startingMonthSalary);
		salary1.setMonth(LocalDate.now().getMonth().toString());
		salary1.setPayDate(LocalDate.now().withDayOfMonth(28));
		salary1.setStatus(SalaryStatus.UNPAID);
	    salary1.setDoctor(doctor);  

		salaryService.addSalary(salary1); 
		

		return "redirect:/addDoctor"; 

	}
}
