package com.desgreen.education.siapp.backend.model;

public enum EnumStatApproval {
    APPROVE("APPROVE", "Disetujui"),
    REJECTED("REJECTED", "Tidak Disetujui"),
	OPEN("OPEN", "OPEN")
	;

    private String stringId;
    private String description;

    private EnumStatApproval(String stringId, String description){
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
