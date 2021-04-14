package com.gurja.cmc.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
	
	public QrCodeDTO getByIndexKey (String indexKey) {
		
		List<QrCodeDTO> listQrCode = new ArrayList<QrCodeDTO>();
		
		listQrCode = this.getAll();
		
//		System.out.println("[QrCodeService (indexKey)]: " + indexKey);
		
		for (QrCodeDTO qDto : listQrCode) {
			if (qDto.getIndexKey().equalsIgnoreCase(indexKey) ) {
				return qDto;
			}
		}
		return null;
	}
	
	public Optional<QrCodeDTO> getById(Long id) {
		return qrCodeRepository.findById(id);
	}
	
	public List<QrCodeDTO> getAll() {
		return (List<QrCodeDTO>) qrCodeRepository.findAll();
	}
}
