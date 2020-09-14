package com.desgreen.education.siapp.backend.model;

public enum EnumStatSiswa {
//	ACTIVE("ACTIVE", "Active"),
    HB("HB", "Hadis Besar"),
	CKM("CKM", "CKM"),
    PPDB("PPDB", "PPDB"),
	NON_ACT("NON_ACT", "Non Active"),
    OTH1("OTH1", "Others 1");

    private String stringId;
    private String description;

    private EnumStatSiswa(String stringId, String description){
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
