package com.desgreen.education.siapp.security_model;

public enum EnumOrganizationLevel {
    SYS("SYS", "System/Top Level"),
    CORP("CORP", "Corporation Level"),
    DIV("DIV", "Division Level"),
    OTH1("OTH1", "Others 1");
    
    private String stringId;
    private String description;
    
    private EnumOrganizationLevel(String stringId, String description){
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
