package com.apptechnosoft.controller.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import com.apptechnosoft.serviceImpl.ContactServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.apptechnosoft.constant.Shift;
import com.apptechnosoft.controller.backend.ContactController;
import com.apptechnosoft.dto.AppointmentDTO;
import com.apptechnosoft.model.Appointment;
import com.apptechnosoft.model.Patient;
import com.apptechnosoft.service.AppointmentService;
import com.apptechnosoft.service.PatientService;


@RestController
@RequestMapping("/api")
public class ApiController {

	
	 @Autowired
	    private PatientService patientService;

  
	
	//----------------------------------backend api-----------------------------------------------
	
	  @GetMapping("/shift")
	  public List<Map<String, String>> getShifts() {
	      return Arrays.stream(Shift.values())
	                   .map(shift -> {
	                       Map<String, String> map = new HashMap<>();
	                       map.put("id", shift.name());
	                       map.put("name", shift.name()); // Assuming getDisplayName() is part of enum.
	                       return map;
	                   })
	                   .collect(Collectors.toList());
	  }
	  	  
	  @GetMapping("/viewPDF")
	    public ResponseEntity<InputStreamResource> viewPDF(@RequestParam String fileName) {

	        try {
	            // ✅ Load file from classpath (inside src/main/resources/static)
	            Resource resource = new ClassPathResource("static/files/applicantsCV/" + fileName);

	            // ✅ Check if file exists
	            if (!resource.exists()) {
	                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	            }

	            // ✅ Read file as input stream
	            InputStream pdfStream = resource.getInputStream();

	            // ✅ Set headers to force inline display instead of download
	            HttpHeaders headers = new HttpHeaders();
	            headers.setContentType(MediaType.APPLICATION_PDF);
	            headers.add("Content-Disposition", "inline; filename=" + fileName);

	            return new ResponseEntity<>(new InputStreamResource(pdfStream), headers, HttpStatus.OK);
	        } catch (Exception e) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	        }
	    }
	  
	  //-----------------------------------------frontend api ----------------------------------------------
	  @GetMapping("/verify")
	  public ResponseEntity<?> verifyPatient(@RequestParam String contact) {
		  System.out.println("called");
		  System.out.println("called");
	      Patient patient = patientService.getByContactNumber(contact);
	      System.out.println(patient);
	      System.out.println(patient);
	      System.out.println(patient);
	      System.out.println("api verify called");
	      System.out.println("api verify called");
	      System.out.println("api verify called");
	      System.out.println("api verify called");
	      System.out.println("api verify called");
	      System.out.println("api verify called");
	      System.out.println("api verify called");
	      System.out.println("api verify called");
	      System.out.println("api verify called");
	      System.out.println("api verify called");
	      System.out.println("api verify called");
 	      if (patient != null) {
	          Map<String, String> response = new HashMap<>();
	          response.put("name", patient.getName());
	          return ResponseEntity.ok(response);
	      } else {
	          return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Patient not found");
	      }
	  }


	  @GetMapping("/details")
	  public ResponseEntity<Map<String, Object>> getPatientByContact(@RequestParam String contact) {
	      Patient patient = patientService.getByContactNumber(contact);

	      if (patient == null) {
	          return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	      }

	      // Debugging: Convert Patient to a simple JSON map
	      Map<String, Object> response = new HashMap<>();
	      response.put("name", patient.getName());
	      response.put("medicalConcern", patient.getMedicalConcern());
	      response.put("mobileNo", patient.getMobileNo());
	      response.put("email", patient.getEmail());
	      response.put("gender", patient.getGender());
	      response.put("age", patient.getAge());
	      response.put("address", patient.getAddress());
	      response.put("id", patient.getId());

	      return ResponseEntity.ok(response);
	  }

	  
	

	      @Autowired
	      private AppointmentService appointmentService;

//	      @GetMapping("/lazy-appointment-list")
//	      public Page<Appointment> getAppointments(
//	              @RequestParam(defaultValue = "0") int start,
//	              @RequestParam(defaultValue = "10") int length
//	      ) {
//	          int page = start / length;
//	          Pageable pageable = PageRequest.of(page, length);
//	          return appointmentService.getAllOrderByStatusBookedOnTopAndDateDesc(pageable);
//	      }
//	      @GetMapping("/lazy-appointment-list")
//	      public Map<String, Object> getAppointments(
//	              @RequestParam(name = "draw", required = false, defaultValue = "1") int draw,
//	              @RequestParam(name = "start", defaultValue = "0") int start,
//	              @RequestParam(name = "length", defaultValue = "10") int length
//	      ) {
//	          int page = start / length;
//	          Pageable pageable = PageRequest.of(page, length);
//	          Page<Appointment> appointmentPage = appointmentService.getAllOrderByStatusBookedOnTopAndDateDesc(pageable);
//
//	          Map<String, Object> response = new HashMap<>();
//	          response.put("draw", draw);
//	          response.put("recordsTotal", appointmentPage.getTotalElements());
//	          response.put("recordsFiltered", appointmentPage.getTotalElements());
//	          response.put("data", appointmentPage.getContent()); // Not "content" — must be "data"
//
//	          return response;
//	      }
	      
	      //working without searvch functionality
//	      @GetMapping("/lazy-appointment-list")
//	      public Map<String, Object> getAppointments(
//	              @RequestParam(name = "draw", required = false, defaultValue = "1") int draw,
//	              @RequestParam(name = "start", defaultValue = "0") int start,
//	              @RequestParam(name = "length", defaultValue = "10") int length
//	      ) {
//	          int page = start / length;
//	          Pageable pageable = PageRequest.of(page, length);
//	          Page<Appointment> appointmentPage = appointmentService.getAllOrderByStatusBookedOnTopAndDateDesc(pageable);
//
//	          List<AppointmentDTO> dtoList = appointmentService.getAllAppointmentsDTO(appointmentPage.getContent());
//
//	          Map<String, Object> response = new HashMap<>();
//	          response.put("draw", draw);
//	          response.put("recordsTotal", appointmentPage.getTotalElements());
//	          response.put("recordsFiltered", appointmentPage.getTotalElements());
//	          response.put("data", dtoList);
//
//	          return response;
//	      }
	      
	      @GetMapping("/lazy-appointment-list")
	      public Map<String, Object> getAppointments(
	          @RequestParam(name = "draw", required = false, defaultValue = "1") int draw,
	          @RequestParam(name = "start", defaultValue = "0") int start,
	          @RequestParam(name = "length", defaultValue = "10") int length,
	          @RequestParam(name = "search[value]", required = false) String search
	      ) {
	          int page = start / length;
	          Pageable pageable = PageRequest.of(page, length);

	          Page<Appointment> appointmentPage;
	          List<AppointmentDTO> dtoList =new ArrayList<>();

	          if (search != null && !search.isEmpty()) {
	        	  appointmentPage = appointmentService.getAllOrderByStatusBookedOnTopAndDateDescWithSearch(search,pageable);
		          dtoList = appointmentService.getAllAppointmentsDTO(appointmentPage.getContent());
	             

	          } else {
	        	  appointmentPage = appointmentService.getAllOrderByStatusBookedOnTopAndDateDesc(pageable);
		          dtoList = appointmentService.getAllAppointmentsDTO(appointmentPage.getContent());

	          }

	          Map<String, Object> response = new HashMap<>();
	          response.put("draw", draw);
	          response.put("recordsTotal", appointmentPage.getTotalElements());
	          response.put("recordsFiltered", appointmentPage.getTotalElements());
	          response.put("data", dtoList);
	          return response;
	      }



	      
	      

}
