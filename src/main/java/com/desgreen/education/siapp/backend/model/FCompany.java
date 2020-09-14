package com.desgreen.education.siapp.backend.model;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name="fcompany")
public class FCompany implements Serializable {

	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	private long id =-1; //sebetulnya nol juga bisa
	
	private String kode1 ="";
	private String kode2 ="";
	
	private String description ="";
	private String address1 ="";
	private String address2 ="";
	private String city ="";

	private String email ="";
	private String phoneNumber ="";
	private String webProfile ="";
	private String webAplikasi ="";

	//Image Logo
	private String logoImage ="";


	private boolean statActive=true;

	@OneToMany(mappedBy="fcompanyBean", fetch= FetchType.LAZY)
	@Fetch(FetchMode.JOIN)
	private Set<Sysvar> sysvarSet;
	
	@OneToMany(mappedBy="fcompanyBean", fetch= FetchType.LAZY)
	@Fetch(FetchMode.JOIN)
	private Set<FDivision> fdivisionSet;


	/*
	 * SETTING YANG BERLAKU UNTUK SEMUA DIVISI
	 * JIKA KOSONG MAKA MENGGUNAKAN PRIORITAS ATASNYA
	 * 1. Parameter System
	 * 2. Corporation
	 * 3. Division 
	 */


	@Column(name="CREATED")
	@Temporal(TemporalType.TIMESTAMP)
	private Date created = new Date();
	@Column(name="MODIFIED")
	@Temporal(TemporalType.TIMESTAMP)
	private Date modified = new Date();
	@Column(name="MODIFIED_BY", length=20)
	private String modifiedBy =""; //User ID

	@Transient
	public boolean isNewDomain() {
		return getId() == -1;
	}

	public String getInitials() {
		if (description !=null) {
			if (description.trim().length() > 2) {
				return (description.trim().substring(0, 2))
						.toUpperCase();
			}
			if (description.trim().length() == 1) {
				return (description.trim().substring(0, 1))
						.toUpperCase();
			} else {
				return "XX";
			}
		}else {
			return "XX";
		}
	}


	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getKode1() {
		return kode1;
	}

	public void setKode1(String kode1) {
		this.kode1 = kode1;
	}

	public String getKode2() {
		return kode2;
	}

	public void setKode2(String kode2) {
		this.kode2 = kode2;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<Sysvar> getSysvarSet() {
		return sysvarSet;
	}

	public void setSysvarSet(Set<Sysvar> sysvarSet) {
		this.sysvarSet = sysvarSet;
	}

	public Set<FDivision> getFdivisionSet() {
		return fdivisionSet;
	}

	public boolean isStatActive() {
		return statActive;
	}

	public void setStatActive(boolean statActive) {
		this.statActive = statActive;
	}

	public void setFdivisionSet(Set<FDivision> fdivisionSet) {
		this.fdivisionSet = fdivisionSet;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getModified() {
		return modified;
	}

	public void setModified(Date modified) {
		this.modified = modified;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}


	public String getLogoImage() {
		return logoImage;
	}

	public void setLogoImage(String logoImage) {
		this.logoImage = logoImage;
	}

	public String getWebProfile() {
		return webProfile;
	}

	public void setWebProfile(String webProfile) {
		this.webProfile = webProfile;
	}

	public String getWebAplikasi() {
		return webAplikasi;
	}

	public void setWebAplikasi(String webAplikasi) {
		this.webAplikasi = webAplikasi;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		FCompany fCompany = (FCompany) o;

		return id == fCompany.id;
	}

	@Override
	public int hashCode() {
		return (int) (id ^ (id >>> 32));
	}

	@Override
	public String toString() {
		return "FCompany{" +
				"id=" + id +
				", kode1='" + kode1 + '\'' +
				", description='" + description + '\'' +
				'}';
	}
}