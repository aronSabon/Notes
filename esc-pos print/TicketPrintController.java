package com.apptechnosoft.ticketPrint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
@Controller
public class TicketPrintController {
	 @Autowired
	    private TicketPrintService ticketPrintService;

	    @GetMapping("/print-ticket")
	    public String printTicket(@RequestParam("appointmentId") int appointmentId, @RequestParam int doctorId) {
	        try {
	            // You can fetch the patient details from the database using the appointmentId
	            // Example: Using a service to get patient data
//	            String patientName = "Sabin Basi"; // This would be fetched based on appointmentId
//	            String patientId = "OPD-001245"; // This would be fetched based on appointmentId
	            
	            // Call the printer service to print the ticket
	            ticketPrintService.printTicket(appointmentId);
	            
//	            return "Ticket printed successfully"; // You can show this message in a view
	        } catch (Exception e) {
	            e.printStackTrace();
//	            return "Failed to print ticket: " + e.getMessage();
	        }
            return "redirect:/today-appointment-list?doctorId=" + doctorId;

	    }
	}
