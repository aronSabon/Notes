package com.apptechnosoft.model;

import java.time.LocalDate;

import com.apptechnosoft.constant.SalaryStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.ToString;

@Entity
@Data
@ToString(exclude = {"doctor", "employee"}) // Avoid infinite recursion by excluding doctor and employee

public class Salary {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int salaryId;
private double amount;
private String month;
private LocalDate payDate;
private double amountPaid;
@Enumerated(EnumType.STRING)
private SalaryStatus status;

@ManyToOne
@JoinColumn(name = "doctor_id", nullable = true) // Ensure correct column name
private Doctor doctor;

@ManyToOne
@JoinColumn(name = "employee_id", nullable = true) // Ensure correct column name
private Employee employee;
}
