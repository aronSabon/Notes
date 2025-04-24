package com.apptechnosoft.ticketPrint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apptechnosoft.model.Appointment;
import com.apptechnosoft.service.AppointmentService;
import com.github.anastaciocintra.escpos.EscPos;
import com.github.anastaciocintra.escpos.Style;
import com.github.anastaciocintra.escpos.barcode.QRCode;
import com.github.anastaciocintra.output.PrinterOutputStream;

import java.io.IOException;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;

@Service
public class TicketPrintService {
	
	
@Autowired
AppointmentService appointmentService;

    public void printTicket(int appointmentId) throws IOException {
    	
    	Appointment appointment = appointmentService.getAppointmentById(appointmentId);
        // Step 1: Lookup the printer by name
        PrintService printService = findPrintService("Your Printer Name");
        if (printService == null) {
            throw new RuntimeException("Printer not found.");
        }

        // Step 2: Create EscPos with the print service
        EscPos escpos = new EscPos(new PrinterOutputStream(printService));

        Style titleStyle = new Style().setBold(true).setFontSize(Style.FontSize._2, Style.FontSize._2);

        escpos.writeLF(titleStyle, "Kageshwori Manohora Nagar Hospital");
        escpos.writeLF("Appointment Ticket");
        escpos.writeLF("--------------------------------");
        escpos.writeLF("Patient:    " + appointment.getPatient().getName());
        escpos.writeLF("Patient ID: " + appointment.getPatient().getPatientId());
        escpos.writeLF("Specialization: " + appointment.getDoctor().getSpecialization().getName());
        escpos.writeLF("Visit Date: " + appointment.getDate());
        escpos.writeLF("--------------------------------");
        QRCode qrCode = new QRCode();
        qrCode.setSize(6); // size from 1–16
        qrCode.setErrorCorrectionLevel(QRCode.QRErrorCorrectionLevel.QR_ECLEVEL_L); // low error correction
        escpos.write(qrCode, appointment.getPatient().getPatientId());

        escpos.feed(3);
        escpos.writeLF("Scan for patient info");
        escpos.feed(3);
        escpos.cut(EscPos.CutMode.FULL);
        escpos.close();
    }

    // Helper method to get printer by name
    private PrintService findPrintService(String printerName) {
        for (PrintService service : PrintServiceLookup.lookupPrintServices(null, null)) {
            if (service.getName().equalsIgnoreCase(printerName)) {
                return service;
            }
        }
        return null;
    }
}
