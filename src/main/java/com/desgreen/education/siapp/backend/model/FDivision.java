package com.desgreen.education.siapp.backend.model;

import com.desgreen.education.siapp.security_model.FUser;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name="fdivision")
public class FDivision implements Serializable {

	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	private long id =0;
	
	/*
	 * JIKA COPY DARI TEMPAT LAIN: MAKA SEBAGAI LOG TRACK MENINGGALKAN SOURCE_ID = ID sumber asal dia dicopy
	 * keperluan diantaranya:
	 * 1. Clone Database. karena tidak mungkin menggunakan Kode External yang bisa jadi kemungkinan kembar, tapi harus pakai kode internal
	 * 2. 
	 */
	@Column(name="SOURCE_ID", length=9)
	private int sourceID =-1;
	
	@Column(name="KODE1", length=10)
	private String kode1 ="";
	@Column(name="KODE2", length=20)
	private String kode2 ="";
	
	@Column(name="DESCRIPTION", length=100)
	private String description ="";

	private String email ="";
	private String phoneNumber ="";
	private String webProfile ="";
	private String webAplikasi ="";

	//Image Logo
	private String logoImage ="";

	@Column(name="DIFF_COMPANY_ACC")
	private boolean diffCompanyAccount = false;

	@ManyToOne
	@JoinColumn(name="fcompanyBean", referencedColumnName="id")
	private FCompany fcompanyBean;
	
	@XmlTransient
	@OneToMany(mappedBy="fdivisionBean", fetch= FetchType.LAZY)
	@Fetch(FetchMode.JOIN)
	private Set<Sysvar> sysvarSet;

	@OneToMany(mappedBy="fdivisionBean", fetch= FetchType.LAZY)
	@Fetch(FetchMode.JOIN)
	private Set<FKompBiaya> fKompBiayaSet;

	@OneToMany(mappedBy="fdivisionBean", fetch= FetchType.LAZY)
	@Fetch(FetchMode.JOIN)
	private Set<FSiswa> fSiswaSet;

	@OneToMany(mappedBy="fdivisionBean", fetch= FetchType.LAZY)
	@Fetch(FetchMode.JOIN)
	private Set<FtAr> ftArSet;

	@OneToMany(mappedBy="fdivisionBean", fetch= FetchType.LAZY)
	@Fetch(FetchMode.JOIN)
	private Set<FtDonasiOrBonus> ftDonasiOrBonusSet;


	@OneToMany(mappedBy="fdivisionBean", fetch= FetchType.LAZY)
	@Fetch(FetchMode.JOIN)
	private Set<FtKrs> ftKrsSet;

	@OneToMany(mappedBy="fdivisionBean", fetch= FetchType.LAZY)
	@Fetch(FetchMode.JOIN)
	private Set<FKurikulum> fkurikulumSet;

	@OneToMany(mappedBy="fdivisionBean", fetch= FetchType.LAZY)
	@Fetch(FetchMode.JOIN)
	private Set<FMatPel> fmatPelSet;

	@OneToMany(mappedBy="fdivisionBean", fetch= FetchType.LAZY)
	@Fetch(FetchMode.JOIN)
	private Set<FDonatur> fdonaturSet;

	@OneToMany(mappedBy="fdivisionBean", fetch= FetchType.LAZY)
	@Fetch(FetchMode.JOIN)
	private Set<FtPayment> ftPaymentSet;

	@OneToMany(mappedBy="fdivisionBean", fetch= FetchType.LAZY)
	@Fetch(FetchMode.JOIN)
	private Set<FtDataKesehatan> ftDataKesehatanSet;


	private boolean statActive=true;

	@OneToMany(mappedBy="fdivisionBean", fetch= FetchType.LAZY)
	@Fetch(FetchMode.JOIN)
	private Set<FUser> fuserSet;


	@Column(name="CREATED")
	@Temporal(TemporalType.TIMESTAMP)
	private Date created = new Date();
	@Column(name="MODIFIED")
	@Temporal(TemporalType.TIMESTAMP)
	private Date modified = new Date();
	@Column(name="MODIFIED_BY", length=20)
	private String modifiedBy =""; //User ID

	@Transient
	private String tempString1 ="";
	@Transient
	private String tempString2 ="";
	@Transient
	private int tempInt1 =0;
	@Transient
	private int tempInt2 =0;
	@Transient
	private double tempDouble1 =0;
	@Transient
	private double tempDouble2 =0;

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

	public int getSourceID() {
		return sourceID;
	}

	public void setSourceID(int sourceID) {
		this.sourceID = sourceID;
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

	public boolean isDiffCompanyAccount() {
		return diffCompanyAccount;
	}

	public void setDiffCompanyAccount(boolean diffCompanyAccount) {
		this.diffCompanyAccount = diffCompanyAccount;
	}

	public FCompany getFcompanyBean() {
		return fcompanyBean;
	}

	public void setFcompanyBean(FCompany fcompanyBean) {
		this.fcompanyBean = fcompanyBean;
	}

	public Set<Sysvar> getSysvarSet() {
		return sysvarSet;
	}

	public void setSysvarSet(Set<Sysvar> sysvarSet) {
		this.sysvarSet = sysvarSet;
	}

	public Set<FKompBiaya> getfKompBiayaSet() {
		return fKompBiayaSet;
	}

	public void setfKompBiayaSet(Set<FKompBiaya> fKompBiayaSet) {
		this.fKompBiayaSet = fKompBiayaSet;
	}

	public Set<FSiswa> getfSiswaSet() {
		return fSiswaSet;
	}

	public void setfSiswaSet(Set<FSiswa> fSiswaSet) {
		this.fSiswaSet = fSiswaSet;
	}

	public Set<FtAr> getFtArSet() {
		return ftArSet;
	}

	public void setFtArSet(Set<FtAr> ftArSet) {
		this.ftArSet = ftArSet;
	}

	public Set<FtDonasiOrBonus> getFtDonasiOrBonusSet() {
		return ftDonasiOrBonusSet;
	}

	public void setFtDonasiOrBonusSet(Set<FtDonasiOrBonus> ftDonasiOrBonusSet) {
		this.ftDonasiOrBonusSet = ftDonasiOrBonusSet;
	}

	public Set<FtKrs> getFtKrsSet() {
		return ftKrsSet;
	}

	public void setFtKrsSet(Set<FtKrs> ftKrsSet) {
		this.ftKrsSet = ftKrsSet;
	}

	public Set<FKurikulum> getFkurikulumSet() {
		return fkurikulumSet;
	}

	public void setFkurikulumSet(Set<FKurikulum> fkurikulumSet) {
		this.fkurikulumSet = fkurikulumSet;
	}

	public Set<FMatPel> getFmatPelSet() {
		return fmatPelSet;
	}

	public void setFmatPelSet(Set<FMatPel> fmatPelSet) {
		this.fmatPelSet = fmatPelSet;
	}

	public Set<FDonatur> getFdonaturSet() {
		return fdonaturSet;
	}

	public void setFdonaturSet(Set<FDonatur> fdonaturSet) {
		this.fdonaturSet = fdonaturSet;
	}

	public Set<FtPayment> getFtPaymentSet() {
		return ftPaymentSet;
	}

	public void setFtPaymentSet(Set<FtPayment> ftPaymentSet) {
		this.ftPaymentSet = ftPaymentSet;
	}

	public boolean isStatActive() {
		return statActive;
	}

	public void setStatActive(boolean statActive) {
		this.statActive = statActive;
	}

	public Set<FUser> getFuserSet() {
		return fuserSet;
	}

	public void setFuserSet(Set<FUser> fuserSet) {
		this.fuserSet = fuserSet;
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

	public String getTempString1() {
		return tempString1;
	}

	public void setTempString1(String tempString1) {
		this.tempString1 = tempString1;
	}

	public String getTempString2() {
		return tempString2;
	}

	public void setTempString2(String tempString2) {
		this.tempString2 = tempString2;
	}

	public int getTempInt1() {
		return tempInt1;
	}

	public void setTempInt1(int tempInt1) {
		this.tempInt1 = tempInt1;
	}

	public int getTempInt2() {
		return tempInt2;
	}

	public void setTempInt2(int tempInt2) {
		this.tempInt2 = tempInt2;
	}

	public double getTempDouble1() {
		return tempDouble1;
	}

	public void setTempDouble1(double tempDouble1) {
		this.tempDouble1 = tempDouble1;
	}

	public double getTempDouble2() {
		return tempDouble2;
	}

	public void setTempDouble2(double tempDouble2) {
		this.tempDouble2 = tempDouble2;
	}

	public Set<FtDataKesehatan> getFtDataKesehatanSet() {
		return ftDataKesehatanSet;
	}

	public void setFtDataKesehatanSet(Set<FtDataKesehatan> ftDataKesehatanSet) {
		this.ftDataKesehatanSet = ftDataKesehatanSet;
	}

	public String getLogoImage() {
		return logoImage;
	}

	public void setLogoImage(String logoImage) {
		this.logoImage = logoImage;
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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	@Override
	public String toString() {
		return "FDivision [description=" + description + ", kode1=" + kode1 + "]";
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		FDivision fDivision = (FDivision) o;

		return id == fDivision.id;
	}
}