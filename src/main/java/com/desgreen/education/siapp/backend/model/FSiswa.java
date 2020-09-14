package com.desgreen.education.siapp.backend.model;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name="fsiswa")
public class FSiswa implements Serializable {

	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	private long id =-1;
	private LocalDate regDate = LocalDate.now();
	private String nis ="";
	private String fullName ="";
	private String nickName ="";
	private String birthPlace ="";
	private LocalDate birthDate = LocalDate.now();
	private boolean sex = true;
	private boolean menikah = false;
	private Integer anakKe = 1;
	private Integer dariKe = 0;

	private String address1 ="";
	private String address2 ="";
	private String address3 ="";
	private String city ="";
	private String state ="";
	private String phone ="";
	private String email ="";

//	private Boolean ckm = false; //calon mubaleg
//	private Boolean hb = false; //hadis besar



	private String fatherName ="";
	private String fatherJob ="";
	private String fatherPhone ="";

	private String motherName ="";
	private String motherJob ="";
	private String motherPhone ="";

	private String parAddress1 ="";
	private String parAddress2 ="";
	private String parCity ="";
	private String parState ="";

	private String imageName ="";
	private String imageIdentity ="";
	private String notes1 ="";
	private String notes2 ="";
	private String notes3 ="";

	/**
	 * INFORMASI KEDALAM
	 */
	private String daerahSmbg ="";
	private String desaSmbg ="";
	private String kelompokSmbg ="";
	private String kiKelompok ="";
	private String telpKiKelompok ="";

	private boolean statActive=true;
	@Enumerated(EnumType.STRING)
	private EnumStatSiswa statSiswa = EnumStatSiswa.OTH1;

	@ManyToOne
	@JoinColumn(name="fdivisionBean", referencedColumnName="id")
	private FDivision fdivisionBean;


	@OneToMany(mappedBy="fsiswaBean", fetch= FetchType.LAZY)
	@Fetch(FetchMode.JOIN)
	private Set<FtKrs> ftKrsSet;

	@OneToMany(mappedBy="fsiswaBean", fetch= FetchType.LAZY)
	@Fetch(FetchMode.JOIN)
	private Set<FtPayment> ftPaymentSet;

	@OneToMany(mappedBy="fsiswaBean", fetch= FetchType.LAZY)
	@Fetch(FetchMode.JOIN)
	private Set<FtDataKesehatan> ftDataKesehatanSet;

	@Transient
	public boolean isNewDomain() {
		return getId() == -1;
	}

	public String getInitials() {
		if (fullName !=null) {
			if (fullName.trim().length() > 2) {
				return (fullName.trim().substring(0, 2))
						.toUpperCase();
			}else if (fullName.trim().length() == 1) {
				return (fullName.trim().substring(0, 1))
						.toUpperCase();
			} else {
				return "XX";
			}
		}else {
			return "XXX";
		}
	}

	@Column(name="CREATED")
	@Temporal(TemporalType.TIMESTAMP)
	private Date created = new Date();
	@Column(name="MODIFIED")
	@Temporal(TemporalType.TIMESTAMP)
	private Date modified = new Date();
	@Column(name="MODIFIED_BY", length=20)
	private String modifiedBy =""; //User ID


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		FSiswa fSiswa = (FSiswa) o;

		return id == fSiswa.id;
	}

	@Override
	public int hashCode() {
		return (int) (id ^ (id >>> 32));
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public LocalDate getRegDate() {
		return regDate;
	}

	public void setRegDate(LocalDate regDate) {
		this.regDate = regDate;
	}

	public LocalDate getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}

	public String getNis() {
		return nis;
	}

	public void setNis(String nis) {
		this.nis = nis;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getBirthPlace() {
		return birthPlace;
	}

	public void setBirthPlace(String birthPlace) {
		this.birthPlace = birthPlace;
	}



	public boolean isSex() {
		return sex;
	}

	public void setSex(boolean sex) {
		this.sex = sex;
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

	public String getAddress3() {
		return address3;
	}

	public void setAddress3(String address3) {
		this.address3 = address3;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFatherName() {
		return fatherName;
	}

	public void setFatherName(String fatherName) {
		this.fatherName = fatherName;
	}

	public String getFatherJob() {
		return fatherJob;
	}

	public void setFatherJob(String fatherJob) {
		this.fatherJob = fatherJob;
	}

	public String getFatherPhone() {
		return fatherPhone;
	}

	public void setFatherPhone(String fatherPhone) {
		this.fatherPhone = fatherPhone;
	}

	public String getMotherName() {
		return motherName;
	}

	public void setMotherName(String motherName) {
		this.motherName = motherName;
	}

	public String getMotherJob() {
		return motherJob;
	}

	public void setMotherJob(String motherJob) {
		this.motherJob = motherJob;
	}

	public String getMotherPhone() {
		return motherPhone;
	}

	public void setMotherPhone(String motherPhone) {
		this.motherPhone = motherPhone;
	}

	public String getParAddress1() {
		return parAddress1;
	}

	public void setParAddress1(String parAddress1) {
		this.parAddress1 = parAddress1;
	}

	public String getParAddress2() {
		return parAddress2;
	}

	public void setParAddress2(String parAddress2) {
		this.parAddress2 = parAddress2;
	}

	public String getParCity() {
		return parCity;
	}

	public void setParCity(String parCity) {
		this.parCity = parCity;
	}

	public String getParState() {
		return parState;
	}

	public void setParState(String parState) {
		this.parState = parState;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public String getNotes1() {
		return notes1;
	}

	public void setNotes1(String notes1) {
		this.notes1 = notes1;
	}

	public String getNotes2() {
		return notes2;
	}

	public void setNotes2(String notes2) {
		this.notes2 = notes2;
	}

	public String getNotes3() {
		return notes3;
	}

	public void setNotes3(String notes3) {
		this.notes3 = notes3;
	}

	public boolean isStatActive() {
		return statActive;
	}

	public void setStatActive(boolean statActive) {
		this.statActive = statActive;
	}

	public FDivision getFdivisionBean() {
		return fdivisionBean;
	}

	public void setFdivisionBean(FDivision fdivisionBean) {
		this.fdivisionBean = fdivisionBean;
	}

	public Set<FtKrs> getFtKrsSet() {
		return ftKrsSet;
	}

	public void setFtKrsSet(Set<FtKrs> ftKrsSet) {
		this.ftKrsSet = ftKrsSet;
	}

	public Set<FtPayment> getFtPaymentSet() {
		return ftPaymentSet;
	}

	public void setFtPaymentSet(Set<FtPayment> ftPaymentSet) {
		this.ftPaymentSet = ftPaymentSet;
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

	public boolean isMenikah() {
		return menikah;
	}

	public void setMenikah(boolean menikah) {
		this.menikah = menikah;
	}

	public Integer getAnakKe() {
		return anakKe;
	}

	public void setAnakKe(Integer anakKe) {
		this.anakKe = anakKe;
	}

	public Integer getDariKe() {
		return dariKe;
	}

	public void setDariKe(Integer dariKe) {
		this.dariKe = dariKe;
	}

	public EnumStatSiswa getStatSiswa() {
		return statSiswa;
	}

	public void setStatSiswa(EnumStatSiswa statSiswa) {
		this.statSiswa = statSiswa;
	}

	public String getImageIdentity() {
		return imageIdentity;
	}

	public void setImageIdentity(String imageIdentity) {
		this.imageIdentity = imageIdentity;
	}

	public Set<FtDataKesehatan> getFtDataKesehatanSet() {
		return ftDataKesehatanSet;
	}

	public void setFtDataKesehatanSet(Set<FtDataKesehatan> ftDataKesehatanSet) {
		this.ftDataKesehatanSet = ftDataKesehatanSet;
	}

	public String getDaerahSmbg() {
		return daerahSmbg;
	}

	public void setDaerahSmbg(String daerahSmbg) {
		this.daerahSmbg = daerahSmbg;
	}

	public String getDesaSmbg() {
		return desaSmbg;
	}

	public void setDesaSmbg(String desaSmbg) {
		this.desaSmbg = desaSmbg;
	}

	public String getKelompokSmbg() {
		return kelompokSmbg;
	}

	public void setKelompokSmbg(String kelompokSmbg) {
		this.kelompokSmbg = kelompokSmbg;
	}

	public String getKiKelompok() {
		return kiKelompok;
	}

	public void setKiKelompok(String kiKelompok) {
		this.kiKelompok = kiKelompok;
	}

	public String getTelpKiKelompok() {
		return telpKiKelompok;
	}

	public void setTelpKiKelompok(String telpKiKelompok) {
		this.telpKiKelompok = telpKiKelompok;
	}
}