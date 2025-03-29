package com.apptechnosoft.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.apptechnosoft.dto.SalaryDetailsDTO;
import com.apptechnosoft.model.Doctor;
import com.apptechnosoft.model.Salary;

public interface SalaryRepository extends JpaRepository<Salary, Integer>{
	
//	  @Query("SELECT new com.apptechnosoft.dto.SalaryDetailsDTO(s.salaryId, s.amount, s.month, s.payDate, s.amountPaid, s.status, " +
//	           "d.fullName, e.fullName) " +
//	           "FROM Salary s " +
//	           "LEFT JOIN s.doctor d " +
//	           "LEFT JOIN s.employee e")
//	    List<SalaryDetailsDTO> findAllSalariesWithDoctorOrEmployee();
//	@Query("SELECT new com.apptechnosoft.dto.SalaryDetailsDTO(s.salaryId, s.amount, s.month, s.payDate, " +
//		       "s.amountPaid, s.status, " +
//		       "CASE WHEN d IS NOT NULL THEN d.fullName ELSE NULL END, " +
//		       "CASE WHEN e IS NOT NULL THEN e.fullName ELSE NULL END) " +
//		       "FROM Salary s " +
//		       "LEFT JOIN s.doctor d " +
//		       "LEFT JOIN s.employee e")
//		List<SalaryDetailsDTO> findAllSalariesWithDoctorOrEmployee();
//	@Query("SELECT new com.apptechnosoft.dto.SalaryDetailsDTO(s.salaryId, s.amount, s.month, s.payDate, " +
//		       "s.amountPaid, s.status, " +
//		       "COALESCE(d.fullName, '') , " +  // Ensures null-safe conversion
//		       "COALESCE(e.fullName, '') ) " +
//		       "FROM Salary s " +
//		       "LEFT JOIN s.doctor d " +
//		       "LEFT JOIN s.employee e")
//		List<SalaryDetailsDTO> findAllSalariesWithDoctorOrEmployee();
//	@Query("SELECT new com.apptechnosoft.dto.SalaryDetailsDTO(s.salaryId, s.amount, s.month, s.payDate, s.amountPaid, s.status, " +
//		       "COALESCE(d.fullName, e.fullName), " +
//			"COALESCE(d.specialization, e.position))" +
//		       "FROM Salary s " +
//		       "LEFT JOIN s.doctor d " +
//		       "LEFT JOIN s.employee e")
//		List<SalaryDetailsDTO> findAllSalariesWithDoctorOrEmployee();
//	@Query("SELECT new com.apptechnosoft.dto.SalaryDetailsDTO(s.salaryId, s.amount, s.month, s.payDate, s.amountPaid, s.status, " +
//		       "COALESCE(d.fullName, e.fullName), " +
//		       "(SELECT STRING_AGG(sp.name, ', ') FROM Specialization sp WHERE sp IN (d.specialization)), " +
//		       "e.position) " +
//		       "FROM Salary s " +
//		       "LEFT JOIN s.doctor d " +
//		       "LEFT JOIN s.employee e")
//		List<SalaryDetailsDTO> findAllSalariesWithDoctorOrEmployee();
//	@Query("SELECT new com.apptechnosoft.dto.SalaryDetailsDTO(s.salaryId, s.amount, s.month, s.payDate, s.amountPaid, s.status, " +
//		       "COALESCE(d.fullName, e.fullName), " +
//		       "(SELECT STRING_AGG(sp.name, ', ') FROM Specialization sp WHERE sp MEMBER OF d.specialization), " +
//		       "e.position) " +
//		       "FROM Salary s " +
//		       "LEFT JOIN s.doctor d " +
//		       "LEFT JOIN s.employee e " +
//		       "LEFT JOIN d.specialization sp")
//		List<SalaryDetailsDTO> findAllSalariesWithDoctorOrEmployee();
	@Query(value = "SELECT s.salary_id, s.amount, s.month, s.pay_date, s.amount_paid, s.status, " +
		       "COALESCE(d.full_name, e.full_name) AS fullName, " +
		       "COALESCE(GROUP_CONCAT(sp.name SEPARATOR ', '), e.position) AS specializationOrPosition " +
		       "FROM salary s " +
		       "LEFT JOIN doctor d ON s.doctor_id = d.doctor_id " +
		       "LEFT JOIN employee e ON s.employee_id = e.employee_id " +
		       "LEFT JOIN doctor_specialization ds ON d.doctor_id = ds.doctor_id " +
		       "LEFT JOIN specialization sp ON ds.specialization_id = sp.id " +
		       "GROUP BY s.salary_id, d.full_name, e.full_name, e.position", nativeQuery = true)
		List<SalaryDetailsDTO> findAllSalariesWithDoctorOrEmployee();





	
}
