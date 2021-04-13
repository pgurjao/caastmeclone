package com.gurja.cmc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gurja.cmc.dto.QrCodeDTO;
import com.gurja.cmc.service.QrCodeService;


@RestController
@RequestMapping("/gerarQrCode")
public class QrCodeController {
	
	@Autowired
	private QrCodeService qrCodeService;
	
	@GetMapping
	public QrCodeDTO gerarQrCode () {
		
		QrCodeDTO qrCode = new QrCodeDTO();
		
		if (qrCode.isDeuBug() == true) {
			System.out.println("[QrCodeController] qrCode.isDeuBug == null");
			return null;
		}
		
		return qrCodeService.save(qrCode);
		
	}
	
	

}
