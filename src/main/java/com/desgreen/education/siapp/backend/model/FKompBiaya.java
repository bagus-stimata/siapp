package com.desgreen.education.siapp.backend.model;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name="fkompbiaya")
public class FKompBiaya {

	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	private long id=-1;
	private String kode1 ="";
	private String kode2 ="";
	private String description ="";
	private String notes ="";

	private boolean statActive=true;

	@ManyToOne
	@JoinColumn(name="fdivisionBean", referencedColumnName="id")
	private FDivision fdivisionBean;

	@OneToMany(mappedBy="fkompBiayaBean", fetch= FetchType.LAZY)
	@Fetch(FetchMode.JOIN)
	private Set<FtAr> ftArSet;

	/**
	 * HANYA TRANSIENT UNTUK SETTING SAJA
	 */
	@OneToMany(mappedBy="fkompBiayaBean", fetch= FetchType.LAZY)
	@Fetch(FetchMode.JOIN)
	private Set<FKurikulum> fKurikulumSet;

	private Date created = new Date();
	private Date lastModified = new Date();
	private String modifiedBy ="";
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
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		FKompBiaya that = (FKompBiaya) o;

		return id == that.id;
	}

	@Override
	public int hashCode() {
		return (int) (id ^ (id >>> 32));
	}

	@Override
	public String toString() {
		return "FKompBiaya{" +
				"kode1='" + kode1 + '\'' +
				", description='" + description + '\'' +
				'}';
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

	public FDivision getFdivisionBean() {
		return fdivisionBean;
	}

	public void setFdivisionBean(FDivision fdivisionBean) {
		this.fdivisionBean = fdivisionBean;
	}

	public Set<FtAr> getFtArSet() {
		return ftArSet;
	}

	public void setFtArSet(Set<FtAr> ftArSet) {
		this.ftArSet = ftArSet;
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
}