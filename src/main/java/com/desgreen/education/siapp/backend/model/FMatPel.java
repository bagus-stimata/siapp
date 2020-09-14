package com.desgreen.education.siapp.backend.model;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name="fmatpel")
public class FMatPel implements Serializable {

	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	private long id =-1;
	private String kode1 ="";
	private String kode2 ="";
	private String description ="";
	private String notes ="";

	private Integer logoIndex =0;

	private boolean statActive=true;

	private Date created = new Date();
	private Date lastModified = new Date();
	private String modifiedBy ="";

	@ManyToOne
	@JoinColumn(name="fdivisionBean", referencedColumnName="id")
	private FDivision fdivisionBean;

	@OneToMany(mappedBy="fmatPelBean", fetch= FetchType.LAZY)
	@Fetch(FetchMode.JOIN)
	private Set<FKurikulum> fkurikulumSet;

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

	private boolean randomBoolean;
	
	public enum Role {
		DESIGNER, DEVELOPER, MANAGER, TRADER, PAYMENT_HANDLER, ACCOUNTANT
	}
	public Role role;
	
//
//	public String getInitials() {
//		return (kode1.substring(0, 1) + description.substring(0, 1))
//				.toUpperCase();
//	}

	public boolean getRandomBoolean() {
		return randomBoolean;
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

	public boolean isRandomBoolean() {
		return randomBoolean;
	}

	public void setRandomBoolean(boolean randomBoolean) {
		this.randomBoolean = randomBoolean;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		FMatPel fMatPel = (FMatPel) o;

		return id == fMatPel.id;
	}

	@Override
	public int hashCode() {
		return (int) (id ^ (id >>> 32));
	}

	@Override
	public String toString() {
		return "FMatPel{" +
				"kode1='" + kode1 + '\'' +
				", description='" + description + '\'' +
				'}';
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

	public Set<FKurikulum> getFkurikulumSet() {
		return fkurikulumSet;
	}

	public void setFkurikulumSet(Set<FKurikulum> fkurikulumSet) {
		this.fkurikulumSet = fkurikulumSet;
	}

	public Integer getLogoIndex() {
		return logoIndex;
	}

	public void setLogoIndex(Integer logoIndex) {
		this.logoIndex = logoIndex;
	}
}