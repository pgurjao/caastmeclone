package com.gurja.cmc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gurja.cmc.dto.QrCodeDTO;
import com.gurja.cmc.repository.QrCodeRepository;

@Service
public class QrCodeService {
	
	@Autowired
	public QrCodeRepository qrCodeRepository;

	public QrCodeDTO save(QrCodeDTO qrCode) {

		if (qrCode == null) {
			System.out.println("[QrCodeService] qrCode == null");
			return null;
		}

		return qrCodeRepository.save(qrCode);
	}
	

}
