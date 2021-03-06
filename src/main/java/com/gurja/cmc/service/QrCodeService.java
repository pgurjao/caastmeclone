package com.gurja.cmc.service;

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

	public QrCodeDTO save(QrCodeDTO qrCodeDto) {

		if (qrCodeDto == null) {
			System.out.println("[QrCodeService] qrCode == null");
			return null;
		}
		return qrCodeRepository.save(qrCodeDto);
	}
	
	public QrCodeDTO getByIndexKey (String indexKey) {

		QrCodeDTO qrCodeDto = new QrCodeDTO();
		qrCodeDto = qrCodeRepository.findByIndexKey(indexKey);
		return qrCodeDto;
	}
	
	public Optional<QrCodeDTO> getById(Long id) {
		return qrCodeRepository.findById(id);
	}
	
	public List<QrCodeDTO> getAll() {
		return (List<QrCodeDTO>) qrCodeRepository.findAll();
	}

	public void deleteById(long qrCodeId) {		
		qrCodeRepository.deleteById(qrCodeId);		
	}
	
	public void deleteByStatus(String toDelete) {
		qrCodeRepository.deleteByStatus(toDelete);
	}
}
