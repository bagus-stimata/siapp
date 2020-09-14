package com.desgreen.education.siapp.backend.model;

public enum EnumStatKelulusan {
    OPEN("OPEN", "Belum"),
    SUCCESS("SUCCESS", "Lulus"),
	FAIL("FAIL", "Tidak Lulus")
	;

    private String stringId;
    private String description;

    private EnumStatKelulusan(String stringId, String description){
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
