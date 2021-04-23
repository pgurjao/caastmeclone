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
//import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;
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
		
		QrCodeDTO qrCodeDTO = new QrCodeDTO();
		qrCodeDTO.setIndexKey(qrCodeDTO.gerarAes256Key() );
		if (qrCodeDTO.getStatus() == QrStatus.ERROR.toString() ) {
			System.out.println("[QrCodeController] qrCodeDTO.status == ERROR");
			return new ResponseEntity<>("HTTP ERROR 500\n\nerror generating AES Key", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		qrCodeService.save(qrCodeDTO);
		return new ResponseEntity<>(qrCodeDTO.getIndexKey(), HttpStatus.OK);
	}
	
	@GetMapping(value = "/gerarQrCodeImage", produces = MediaType.IMAGE_PNG_VALUE)
	public ResponseEntity<BufferedImage> gerarQrCodeImage() throws Exception {
		
		QrCodeDTO qrCodeDTO = new QrCodeDTO();
		qrCodeDTO.setIndexKey(qrCodeDTO.gerarAes256Key() );
		if (qrCodeDTO.getStatus() == QrStatus.ERROR.toString() ) {
			System.out.println("[QrCodeController] qrCodeDTO.status == ERROR");
			return badResponse(null);
		}
		qrCodeService.save(qrCodeDTO);
		return okResponse(qrCodeDTO.generateQRCodeImage() );
	}
	
	@RequestMapping(value="/redirectUrl",method = RequestMethod.POST) 
//	@PostMapping("/redirectUrl")
	public ResponseEntity<String> redirectUrl(@RequestBody Map<String,String> body ) throws InterruptedException {
		
		QrCodeDTO qrCodeDTO = new QrCodeDTO ();
		
		if ( body.isEmpty() || body.size() == 0 ) {
//			System.out.println("[QrCodeController (body.isEmpty())] body.isEmpty() or size = 0");
			return new ResponseEntity<>("", HttpStatus.NOT_ACCEPTABLE);
		}
		if ( !body.containsKey("indexKey") || body.get("indexKey") == null ) {
//			System.out.println("[QrCodeController (indexKey)] body does not contain indexKey it OR is null");
			return new ResponseEntity<>("", HttpStatus.NOT_ACCEPTABLE);
		}
		if ( !body.containsKey("urlToRedirect") || body.get("urlToRedirect") == null ) {
//			System.out.println("[QrCodeController (urlToRedirect)] body does not contain urlToRedirect OR is null");
			return new ResponseEntity<>("", HttpStatus.NOT_ACCEPTABLE);
		}
		
		qrCodeDTO = qrCodeService.getByIndexKey(body.get("indexKey").toString() );
		
		if (qrCodeDTO == null) {
//			return ResponseEntity.noContent().build();
			return new ResponseEntity<>("", HttpStatus.BAD_REQUEST);
		}
		if (qrCodeDTO.getStatus().equalsIgnoreCase(QrStatus.TO_DELETE.toString() ) ) {
			return new ResponseEntity<>("", HttpStatus.GONE);
		}
		
		qrCodeDTO.setStatus(QrStatus.WAITING_URLTOREDIRECT.toString() );
		
		//DEBUG
//		System.out.println("indexKey= " + body.get("indexKey").toString() );
//		System.out.println("urlToRedirect= " + body.get("urlToRedirect").toString() );
		
		String urlToValidate = body.get("urlToRedirect").toString();
		
		// ADICIONAR LOGICA DE VALIDACAO DE URL
		// if (isUrlValid(urlToValidate) == true) {
		
		qrCodeDTO.setUrlToRedirect(urlToValidate);
		qrCodeDTO.setStatus(QrStatus.TO_DELETE.toString());
		qrCodeService.save(qrCodeDTO);

		// } else {
		// System.out.println("Invalid URL");
		// }
		
//		System.out.println("[QrCodeController (qDTO.Id)]: " + qDTO.getQrCodeId());
//		System.out.println("[QrCodeController (qDTO.Key)]: " + qDTO.getIndexKey());
//		System.out.println("[QrCodeController (qDTO.redirect)]: " + qDTO.getUrlToRedirect());
		
//		qrCodeService.deleteById(qrCodeDTO.getQrCodeId() );
		
//		return new ResponseEntity<>(Long.toString(qDTO.getQrCodeId() ), HttpStatus.OK); RETORNA ID DA TABELA PARA USUARIO
//		return new ResponseEntity<>(qrCodeDTO.toString(), HttpStatus.OK);
		
		return new ResponseEntity<>("URL pushed successfully!", HttpStatus.OK);
	}
	
	public void cleanOldRecordsFromDataBase() {
		qrCodeService.deleteByStatus(QrStatus.TO_DELETE.toString() );
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
