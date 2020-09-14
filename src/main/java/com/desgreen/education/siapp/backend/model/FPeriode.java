package com.desgreen.education.siapp.backend.model;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name="fperiode")
public class FPeriode implements Serializable{

	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	private long id = -1;

	@Column(name="KODE1", length=20)
	private String kode1 ="";
	@Column(name="KODE2", length=20)
	private String kode2 ="";
	
	@Column(name="DESCRIPTION", length=100)
	private String description ="";

	private LocalDate daftarOpenFrom =LocalDate.now();
	private LocalDate daftarCloseTo =LocalDate.now();

	private LocalDate periodeFrom =LocalDate.now();
	private LocalDate periodeTo =LocalDate.now();
	private int tahun=0;

	private String notes = "";

	private boolean statActive=true;

	private Date created = new Date();
	private Date lastModified = new Date();
	private String modifiedBy ="";

	@ManyToOne
	@JoinColumn(name="fdivisionBean", referencedColumnName="id")
	private FDivision fdivisionBean;

	@OneToMany(mappedBy="fperiodeBean", fetch= FetchType.LAZY)
	@Fetch(FetchMode.JOIN)
	private Set<FKurikulum> fkurikulumSet;

	@OneToMany(mappedBy="fperiodeBean", fetch= FetchType.LAZY)
	@Fetch(FetchMode.JOIN)
	private Set<FtAr> ftArSet;

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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FPeriode other = (FPeriode) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "FPeriode [description=" + description + ", kode1=" + kode1 + "]";
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

	public LocalDate getPeriodeFrom() {
		return periodeFrom;
	}

	public void setPeriodeFrom(LocalDate periodeFrom) {
		this.periodeFrom = periodeFrom;
	}

	public LocalDate getPeriodeTo() {
		return periodeTo;
	}

	public void setPeriodeTo(LocalDate periodeTo) {
		this.periodeTo = periodeTo;
	}

	public int getTahun() {
		return tahun;
	}

	public void setTahun(int tahun) {
		this.tahun = tahun;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public boolean isStatActive() {
		return statActive;
	}

	public void setStatActive(boolean statActive) {
		this.statActive = statActive;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getLastModified() {
		return lastModified;
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public FDivision getFdivisionBean() {
		return fdivisionBean;
	}

	public void setFdivisionBean(FDivision fdivisionBean) {
		this.fdivisionBean = fdivisionBean;
	}

	public Set<FKurikulum> getFkurikulumSet() {
		return fkurikulumSet;
	}

	public void setFkurikulumSet(Set<FKurikulum> fkurikulumSet) {
		this.fkurikulumSet = fkurikulumSet;
	}

	public Set<FtAr> getFtArSet() {
		return ftArSet;
	}

	public void setFtArSet(Set<FtAr> ftArSet) {
		this.ftArSet = ftArSet;
	}

	public LocalDate getDaftarOpenFrom() {
		return daftarOpenFrom;
	}

	public void setDaftarOpenFrom(LocalDate daftarOpenFrom) {
		this.daftarOpenFrom = daftarOpenFrom;
	}

	public LocalDate getDaftarCloseTo() {
		return daftarCloseTo;
	}

	public void setDaftarCloseTo(LocalDate daftarCloseTo) {
		this.daftarCloseTo = daftarCloseTo;
	}
}