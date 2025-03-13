package com.apptechnosoft.controller.frontend;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.apptechnosoft.constant.AppointmentStatus;
import com.apptechnosoft.constant.HomeServiceStatus;
import com.apptechnosoft.constant.Language;
import com.apptechnosoft.constant.NotificationStatus;
import com.apptechnosoft.constant.Shift;
import com.apptechnosoft.constant.SlotStatus;
import com.apptechnosoft.controller.backend.NotificationHandler;
import com.apptechnosoft.model.Appointment;
import com.apptechnosoft.model.Department;
import com.apptechnosoft.model.Doctor;
import com.apptechnosoft.model.DoctorSchedule;
import com.apptechnosoft.model.Employee;
import com.apptechnosoft.model.HealthPackage;
import com.apptechnosoft.model.HomeService;
import com.apptechnosoft.model.Notification;
import com.apptechnosoft.model.Patient;
import com.apptechnosoft.model.ShiftDetail;
import com.apptechnosoft.model.Specialization;
import com.apptechnosoft.service.AppointmentService;
import com.apptechnosoft.service.DepartmentService;
import com.apptechnosoft.service.DoctorScheduleService;
import com.apptechnosoft.service.DoctorService;
import com.apptechnosoft.service.EmployeeService;
import com.apptechnosoft.service.HealthPackageService;
import com.apptechnosoft.service.HomeServiceService;
import com.apptechnosoft.service.NotificationService;
import com.apptechnosoft.service.PatientService;
import com.apptechnosoft.service.ShiftDetailService;
import com.apptechnosoft.service.SpecializationService;
@Controller
@RequestMapping("/user")
public class FrontEndController {

	@Autowired
	EmployeeService employeeService;
	@Autowired
	DepartmentService departmentService;
	@Autowired
	DoctorService doctorService;
	@Autowired
	SpecializationService specializationService;
	@Autowired
	HealthPackageService healthPackageService;
	@Autowired
	DoctorScheduleService doctorScheduleService;
	@Autowired
	ShiftDetailService shiftDetailService;
	@Autowired
	PatientService patientService;
	@Autowired
	AppointmentService appointmentService;
	@Autowired
	HomeServiceService homeServiceService;
	@Autowired
	NotificationService notificationService;
	@Autowired
	NotificationHandler notificationHandler;



	@PostMapping("/package-patient-save")
	public String savepatientwithPackage(@ModelAttribute Patient patient,@RequestParam List<Integer> healthPackageId,@RequestParam(required=false) String alreadyAPatient,@RequestParam(required=false) Integer id) {
		//logic for already a patient
		
		if("true".equals(alreadyAPatient) && id != null) {
			Patient patient1 = patientService.getPatientById(id);
			//without health pacakages
			if(patient1.getHealthPackage() == null) {
				patient1.setHealthPackage(new ArrayList<>());
				for(int i : healthPackageId) {
					patient1.getHealthPackage().add(healthPackageService.getHealthPackageById(i));
					patientService.updatePatient(patient1);				
					
					addHomeService(patient,healthPackageService.getHealthPackageById(i));
				}
			}
			else {
				//with healthPackages
				if(patient1.getHealthPackage() !=null) {
					for(int i : healthPackageId) {
						patient1.getHealthPackage().add(healthPackageService.getHealthPackageById(i));
						patientService.updatePatient(patient1);
						addHomeService(patient,healthPackageService.getHealthPackageById(i));
					}
				}
			}
		}
		else {
			//below logic is for new patient
			patientService.addPatient(patient);
			for(Integer i : healthPackageId) {
				addHomeService(patient,healthPackageService.getHealthPackageById(i));
			}
		}
//		clear the session storage after redirecting
		return "redirect:/user/home?bookingSuccess=true";
	}
	
//	helper method for add home Service
	private void addHomeService(Patient patient, HealthPackage healthPackage) {
	    HomeService homeService = new HomeService();
	    homeService.setHealthPackage(healthPackage);
	    homeService.setPatient(patient);
	    homeService.setPurchaseDate(LocalDateTime.now());
	    homeService.setStatus(HomeServiceStatus.PENDING);
	    homeServiceService.addHomeService(homeService);
	    
	    Notification notification = new Notification();
	    notification.setHomeService(homeService);
	    notification.setMessage("A "+ healthPackage.getName() + " package has been purchased. Please assign a team." );
	    notification.setStatus(NotificationStatus.UNSEEN);
	    notificationService.addNotification(notification);
	    //add notification logic here
	    
	    try {
	    	System.out.println("trying broadcast notification");
	        notificationHandler.broadcastNotification(notification.getMessage());
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}

}
