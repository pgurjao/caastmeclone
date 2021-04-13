package com.gurja.cmc.repository;

import org.springframework.data.repository.CrudRepository;

import com.gurja.cmc.dto.QrCodeDTO;


public interface QrCodeRepository extends CrudRepository<QrCodeDTO,Long>{

}