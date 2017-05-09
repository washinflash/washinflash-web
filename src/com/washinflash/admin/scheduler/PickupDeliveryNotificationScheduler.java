package com.washinflash.admin.scheduler;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import com.washinflash.admin.businessservice.OrderAdminService;

public class PickupDeliveryNotificationScheduler {

	private static final Logger log = Logger.getLogger(PickupDeliveryNotificationScheduler.class);
	
	@Autowired
	private OrderAdminService orderAdminService;
	
	
    @Scheduled(fixedRate=5*60*1000)
    public void sendPickupDeliveryNotification(){
        
    	try {
    		//orderAdminService.se
    	} catch (Throwable t) {
    		log.error(t.getStackTrace());
    	}
    	
    }

}
