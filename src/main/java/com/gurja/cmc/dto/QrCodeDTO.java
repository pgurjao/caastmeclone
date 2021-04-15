package com.gurja.cmc.dto;

import java.awt.image.BufferedImage;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;

import com.gurja.cmc.controller.QrCodeDTOStatus;

@Entity
@Table(name="QRCODE")
public class QrCodeDTO {
	
	@Id
	@GeneratedValue
	private long qrCodeId;
	
	private String indexKey;
	private String urlToRedirect;
	private String status;
	
	public QrCodeDTO() {
		this.status = QrCodeDTOStatus.OBJ_CREATED.toString();
	}
	
	public String gerarAes256Key() {
			
			KeyGenerator keyGen = null;
			
			try {
				keyGen = KeyGenerator.getInstance("AES");
			} catch (NoSuchAlgorithmException e) {
	//			e.printStackTrace();
				System.out.println("\n\n\n[qrCodeDTO] Exception 'NoSuchAlgorithmException' na geracao da chave AES");
				this.setStatus(QrCodeDTOStatus.ERROR.toString() );
				return null;
			} finally {
				if (keyGen == null) {
					this.status = QrCodeDTOStatus.ERROR.toString();
					return null;
				} else {
					keyGen.init(256); // for example
				}
			}
			SecretKey secretKey = keyGen.generateKey();
			String encodedBase64Key = Base64.getEncoder().encodeToString(secretKey.getEncoded());
			System.out.println("[QrCodeDTO (encodedBase64Key)]: " + encodedBase64Key);
			this.status = QrCodeDTOStatus.KEY_CREATED.toString();
			
			return encodedBase64Key;
		}

	public BufferedImage generateQRCodeImage() throws Exception {
		
		String barcodeText = this.getIndexKey(); 
		
		if (barcodeText == null) {
			return null;
		}
		
		QRCodeWriter barcodeWriter = new QRCodeWriter();
		BitMatrix bitMatrix = barcodeWriter.encode(barcodeText, BarcodeFormat.QR_CODE, 400, 400);
		
		this.status = QrCodeDTOStatus.IMAGE_CREATED.toString();

		return MatrixToImageWriter.toBufferedImage(bitMatrix);
	}

	public long getQrCodeId() {
		return qrCodeId;
	}

	public void setQrCodeId(long qrCodeId) {
		this.qrCodeId = qrCodeId;
	}

	public String getIndexKey() {
		return indexKey;
	}

	public void setIndexKey(String indexKey) {
		this.indexKey = indexKey;
	}

	public String getUrlToRedirect() {
		return urlToRedirect;
	}

	public void setUrlToRedirect(String urlToRedirect) {
		this.urlToRedirect = urlToRedirect;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
