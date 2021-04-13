package com.gurja.cmc.dto;

public class QrCodeToDb {
	
	private String indexKey;

	public QrCodeToDb(long qrCodeId, String indexKey) {
		
		QrCodeDTO qDTO = new QrCodeDTO ();
		this.indexKey = qDTO.getIndexKey();
	}

	public String getIndexKey() {
		return indexKey;
	}

	public void setIndexKey(String indexKey) {
		this.indexKey = indexKey;
	}
}
