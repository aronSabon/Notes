package com.apptechnosoft.model;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.apptechnosoft.constant.DoctorStatus;
import com.apptechnosoft.constant.Language;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.ToString;
@Entity
@Data
public class Employee {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int employeeId;

	@OneToOne(cascade = CascadeType.ALL)
	private LoginDetail loginDetail;
	private String fullName;
	private String gender;
	@DateTimeFormat(iso = ISO.DATE)
	private LocalDate dateOfBirth;
	private String contact;
	private String email;
	private String address;
	private String imageName;	
	private String qualification;
	private String experience;
	@ManyToOne
	private Department department;
	private String employeeType;
	private String position;
	private double salary;
    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Salary> salaryList;
	@DateTimeFormat(iso = ISO.DATE)
	private LocalDate joinDate; 
	private String title;
}
