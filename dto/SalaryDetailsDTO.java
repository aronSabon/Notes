package com.apptechnosoft.dto;

import java.time.LocalDate;
import java.util.List;

import com.apptechnosoft.constant.SalaryStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SalaryDetailsDTO {
    private int salaryId;
    private Double amount;
    private String month;
    private LocalDate payDate;
    private Double amountPaid;
    private SalaryStatus status;
    private String name; // Generic name field (either doctor or employee)
    private String specializationsOrPosition; // Modified to List


    public SalaryDetailsDTO(int salaryId, Double amount, String month, LocalDate payDate, Double amountPaid, SalaryStatus status, 
                            String name,String specializationsOrPosition) {
        this.salaryId = salaryId;
        this.amount = amount;
        this.month = month;
        this.payDate = payDate;
        this.amountPaid = amountPaid;
        this.status = status;
        this.name = name;
        this.specializationsOrPosition = specializationsOrPosition;
    }
}
