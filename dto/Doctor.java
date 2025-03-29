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
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.ToString;
@Entity
@Data
//@ToString(exclude = "salary") // Avoid infinite recursion by excluding salary

public class Doctor {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int doctorId;

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

//	@ManyToMany(cascade = CascadeType.ALL)
//	private List<Specialization> specialization;
	@ManyToOne(cascade = CascadeType.ALL)
	private Specialization specialization;
	private String qualification;
	private String experience;
	private int consultationFee;
	private String roomNo;

	@ElementCollection
	@Enumerated(EnumType.STRING)
	@CollectionTable(name="doctor_languages", joinColumns=@JoinColumn(name="doctor_id"))
	@Column(name="language")
	private List<Language> language;
	
	@Enumerated(EnumType.STRING)
	private DoctorStatus status;

	@ManyToOne
	private Department department;

	@OneToMany(mappedBy = "doctor", cascade = CascadeType.REMOVE)
//	@ToString.Exclude

	private List<DoctorSchedule> doctorSchedule;
	private String employeeType;
	private double salary;

	@OneToMany(mappedBy = "doctor",cascade = CascadeType.ALL, orphanRemoval = true)

//	@JoinColumn(nullable = true)
	private List<Salary> salaryList;
	
	private LocalDate joinDate;
	
	private int nmcNo;
}
