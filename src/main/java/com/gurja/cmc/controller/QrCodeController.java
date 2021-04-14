package com.gurja.cmc.controller;

import java.awt.image.BufferedImage;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.BufferedImageHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gurja.cmc.dto.QrCodeDTO;
import com.gurja.cmc.service.QrCodeService;


@RestController
@RequestMapping
public class QrCodeController {
	
	@Autowired
	private QrCodeService qrCodeService;
	
	@GetMapping("/geraraeskey")
	public ResponseEntity<String> gerarQrCode() {
		
		QrCodeDTO qrCode = new QrCodeDTO();
		qrCode.setIndexKey(qrCode.gerarAes256Key() );
		if (qrCode.isDeuBug() == true) {
			System.out.println("[QrCodeController] qrCode.isDeuBug == true");
			return new ResponseEntity<>("HTTP ERROR 500\n\nerror generating AES Key", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		qrCodeService.save(qrCode);
		return new ResponseEntity<>(qrCode.getIndexKey(), HttpStatus.OK);
	}
	
	@GetMapping(value = "/gerarQrCodeImage", produces = MediaType.IMAGE_PNG_VALUE)
	public ResponseEntity<BufferedImage> gerarQrCodeImage() throws Exception {
		
		QrCodeDTO qrCode = new QrCodeDTO();
		qrCode.setIndexKey(qrCode.gerarAes256Key() );
		if (qrCode.isDeuBug() == true) {
			System.out.println("[QrCodeController] qrCode.isDeuBug == true");
			return badResponse(null);
		}
		qrCodeService.save(qrCode);
		return okResponse(qrCode.generateQRCodeImage() );
	}
	
	@RequestMapping(value="/redirectUrl",method = RequestMethod.POST) 
//	@PostMapping("/redirectUrl")
	public ResponseEntity<String> redirectUrl(@RequestBody Map<String,Object> body ) {
		
		QrCodeDTO qDTO = new QrCodeDTO ();
		
		qDTO = qrCodeService.getByIndexKey(body.get("indexKey").toString() );
		
		if (qDTO == null) {
			return new ResponseEntity<>("Chave nao encontrada", HttpStatus.BAD_REQUEST);
		}
		
//		System.out.println("indexKey= " + body.get("indexKey").toString() );
//		System.out.println("urlToRedirect= " + body.get("urlToRedirect").toString() );
		
		qDTO.setUrlToRedirect(body.get("urlToRedirect").toString() );
		
		qrCodeService.save(qDTO);
		
//		return new ResponseEntity<>(Long.toString(qDTO.getQrCodeId() ), HttpStatus.OK); RETORNA ID DA TABELA PARA USUARIO
		return new ResponseEntity<>("'indexKey' found, 'urlToRedirect' updated successfully!", HttpStatus.OK);
	}
	
	@Bean
	public HttpMessageConverter<BufferedImage> createImageHttpMessageConverter() {
	    return new BufferedImageHttpMessageConverter();
	}
	
	private ResponseEntity<BufferedImage> okResponse(BufferedImage image) {
        return new ResponseEntity<>(image, HttpStatus.OK);
	}
	
	private ResponseEntity<BufferedImage> badResponse(BufferedImage image) {
        return new ResponseEntity<>(image, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
