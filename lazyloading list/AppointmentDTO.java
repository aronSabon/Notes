package com.apptechnosoft.dto;

import java.time.Duration;
import java.time.LocalDateTime;

import com.apptechnosoft.model.Contact;
import com.apptechnosoft.model.Notification;

public class AppointmentDTO {
    private int appointmentId;
    private String patientName;
    private String patientMobileNo;
    private String doctorName;
    private String date;
    private String time;
    private String roomNo;
	private String status;

	
	
    public int getAppointmentId() {
		return appointmentId;
	}
	public void setAppointmentId(int appointmentId) {
		this.appointmentId = appointmentId;
	}
	public String getPatientName() {
		return patientName;
	}
	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}
	public String getPatientMobileNo() {
		return patientMobileNo;
	}
	public void setPatientMobileNo(String patientMobileNo) {
		this.patientMobileNo = patientMobileNo;
	}
	public String getDoctorName() {
		return doctorName;
	}
	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getRoomNo() {
		return roomNo;
	}
	public void setRoomNo(String roomNo) {
		this.roomNo = roomNo;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "AppointmentDTO [appointmentId=" + appointmentId + ", patientName=" + patientName + ", patientMobileNo="
				+ patientMobileNo + ", doctorName=" + doctorName + ", date=" + date + ", time=" + time + ", roomNo="
				+ roomNo + ", status=" + status + "]";
	}

    // constructor, getters, setters
}

