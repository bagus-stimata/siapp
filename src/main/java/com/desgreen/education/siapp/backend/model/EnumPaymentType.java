package com.desgreen.education.siapp.backend.model;

public enum EnumPaymentType {
    TF("TF", "Transfer"),
    GR("GR", "Giro"),
	CA("CA", "Cash");

    private String stringId;
    private String description;

    private EnumPaymentType(String stringId, String description){
        this.stringId = stringId;
        this.description = description;    
    }

	public String getStringId() {
		return stringId;
	}

	public void setStringId(String stringId) {
		this.stringId = stringId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
    

}
