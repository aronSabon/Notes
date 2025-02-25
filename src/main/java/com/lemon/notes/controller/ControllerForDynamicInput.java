package com.lemon.notes.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.apptechnosoft.model.HealthPackage;

public class ControllerForDynamicInput {

	@PostMapping("/addHealthPackage")
	private String postHealthPackage(@ModelAttribute HealthPackage healthPackage,@RequestParam MultipartFile image) {

		if(!image.isEmpty()) {
			try {
				Files.copy(image.getInputStream(), 
						Path.of("src/main/resources/static/images/packageImages/"+ image.getOriginalFilename()), 
						StandardCopyOption.REPLACE_EXISTING);
				healthPackage.setImageName(image.getOriginalFilename());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		List<String> testsAndServices = healthPackage.getTestsAndServices().stream().filter(s -> s != null && !s.isEmpty()).collect(Collectors.toList());
		List<String> precautions = healthPackage.getPrecautions().stream().filter(x -> x!= null && !x.isEmpty()).collect(Collectors.toList());
		healthPackage.setTestsAndServices(testsAndServices);
		healthPackage.setPrecautions(precautions);
		healthPackageService.addHealthPackage(healthPackage);

		return"redirect:/addHealthPackage";
	}
}
