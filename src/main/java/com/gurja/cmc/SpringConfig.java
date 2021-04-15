package com.gurja.cmc;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.gurja.cmc.controller.QrCodeController;

@Configuration
@EnableScheduling
public class SpringConfig {
	
	@Scheduled(cron = "0 0 4 * * *")
	public void scheduleTaskUsingCronExpression() {
		
		QrCodeController qrCodeController = new QrCodeController();
	 
		qrCodeController.cleanOldRecordsFromDataBase();
	}

}
