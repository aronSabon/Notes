 package com.apptechnosoft.serviceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apptechnosoft.constant.NotificationStatus;
import com.apptechnosoft.controller.backend.AutoNotificationController;
import com.apptechnosoft.controller.backend.NotificationController;
import com.apptechnosoft.dto.NotificationDTO;
import com.apptechnosoft.model.Notification;
import com.apptechnosoft.repository.NotificationRepository;
import com.apptechnosoft.service.NotificationService;
@Service
public class NotificationServiceImpl implements NotificationService {
	@Autowired
	NotificationRepository notificationRepository;
	

    @Autowired
    private AutoNotificationController autoNotificationController;

	    
	    public void addNotification(Notification notification) {
	        // Save the notification to the database
	        notificationRepository.save(notification);

	        // Send the notification to all connected clients
	        autoNotificationController.sendNotification(notification.getMessage());

	    }
	

//	@Override
//	public void addNotification(Notification notification) {
//		// TODO Auto-generated method stub
//		notificationRepository.save(notification);
//	}

	@Override
	public List<Notification> getAllNotification() {
		// TODO Auto-generated method stub
		return notificationRepository.findAll();
	}

	@Override
	public void deleteNotificationById(int notificationId) {
		// TODO Auto-generated method stub
		notificationRepository.deleteById(notificationId);
	}

	@Override
	public Notification getNotificationById(int notificationId) {
		// TODO Auto-generated method stub
		return notificationRepository.findById(notificationId).get();
	}

	@Override
	public void updateNotification(Notification notification) {
		// TODO Auto-generated method stub
		notificationRepository.save(notification);
	}

	@Override
	public List<Notification> getAllByStatus(NotificationStatus status) {
		// TODO Auto-generated method stub
		return notificationRepository.findByStatus(status);
	}

	@Override
	public List<NotificationDTO> getAllDTOByStatus(NotificationStatus status) {
		// TODO Auto-generated method stub
		return notificationRepository.findByStatusOrderByHomeServicePurchaseDateDesc(status)
				.stream()
				.map(n -> new NotificationDTO(n))
				.collect(Collectors.toList());
	}

	@Override
	public List<Notification> getAllByHomeServicePurchaseDateDescendingOrder() {
		// TODO Auto-generated method stub
		return notificationRepository.findAllByDescendingOrder();
	}
	


	  
	
}
