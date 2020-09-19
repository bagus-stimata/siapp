package com.desgreen.education.siapp.backend.model;

public enum EnumRotate {
	NORMAL("NORMAL", "Normal"),
    CW_90("CW_90", "Clock Wise 90"),
    CW_180("CW_180", "Clock Wise 180"),
	CW_270("CW_270", "Clock Wise 270")
	;

    private String stringId;
    private String description;

    private EnumRotate(String stringId, String description){
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
