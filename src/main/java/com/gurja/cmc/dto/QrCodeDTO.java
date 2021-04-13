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

@Entity
@Table(name="QRCODE")
public class QrCodeDTO {
	
	@Id
	@GeneratedValue
	private long qrCodeId;
	
	private String indexKey;
//	private String urlToPost;
	private String urlToRedirect;
	private boolean deuBug;
	
	public QrCodeDTO() {
		
//		this.urlToPost = "http://localhost:8080/enviarurl";
		this.urlToRedirect = null;
		this.indexKey = gerarAes256Key();
		
		if (this.indexKey == null) {
			this.deuBug = true;
			System.out.println("[QrCodeDTO] indexKey == null");
		} else {
			this.deuBug = false;
		}
		gerarQrCode(indexKey);
	}
	
	public static BufferedImage generateQRCodeImage(String barcodeText) throws Exception {
		
		QRCodeWriter barcodeWriter = new QRCodeWriter();
		BitMatrix bitMatrix = barcodeWriter.encode(barcodeText, BarcodeFormat.QR_CODE, 200, 200);

		return MatrixToImageWriter.toBufferedImage(bitMatrix);
	}
	
	private String gerarAes256Key() {
		
		KeyGenerator keyGen = null;
		
		try {
			keyGen = KeyGenerator.getInstance("AES");
		} catch (NoSuchAlgorithmException e) {
//			e.printStackTrace();
			System.out.println("\n\n\n[qrCodeDTO] Exception 'NoSuchAlgorithmException' na geracao da chave AES");
			return null;
		} finally {
			if (keyGen == null) {
				return null;
			} else {
				keyGen.init(256); // for example
			}
		}
		SecretKey secretKey = keyGen.generateKey();
		
		String encodedBase64Key = Base64.getEncoder().encodeToString(secretKey.getEncoded());
		System.out.println("[qrCodeDTO]encodedBase64Key =" + encodedBase64Key);
		
		byte[] decodedBase64Key = Base64.getDecoder().decode(encodedBase64Key);
		System.out.println("[qrCodeDTO]DEcodedBase64Key =" + decodedBase64Key);
		
		return encodedBase64Key;
	}
	
	private void gerarQrCode(String indexKey) {
		
		String barCodeText = indexKey;
		System.out.println("[qrCodeDTO] barCodeText= " + barCodeText);
		
		try {
			generateQRCodeImage(barCodeText);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("\n\n\n[qrCodeDTO] Exception na geracao do QR CODE");
		}
	}
	
//	private void salvarNaDb() {
//		
//	}

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

//	public String getUrlToPost() {
//		return urlToPost;
//	}

//	public void setUrlToPost(String urlToPost) {
//		this.urlToPost = urlToPost;
//	}

	public String getUrlToRedirect() {
		return urlToRedirect;
	}

	public void setUrlToRedirect(String urlToRedirect) {
		this.urlToRedirect = urlToRedirect;
	}

	public boolean isDeuBug() {
		return deuBug;
	}
}
