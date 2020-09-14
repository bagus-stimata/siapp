package com.desgreen.education.siapp.backend.model;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * Kurikulum adalah semacam Kelas
 */
@Entity
@Table(name="fkurikulum")
public class FKurikulum implements Serializable {

	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	private long id =-1;
	private String kode1 ="";
	private String kode2 ="";

	private int kuotaMale =0;
	private int kuotaFemale =0;
	private String description ="";
	private String notes ="";



	private boolean statActive =true;


	@ManyToOne
	@JoinColumn(name="fkompBiayaBean", referencedColumnName="id")
	private FKompBiaya fkompBiayaBean;

//	@Column( columnDefinition="double precision default '0.00'")
	@Column( columnDefinition="Decimal(10,2) default '0.00'")
	private Double amountBiaya =0.0;
	private String notesBiaya ="";

	@ManyToOne
	@JoinColumn(name="fdivisionBean", referencedColumnName="id")
	private FDivision fdivisionBean;

	@ManyToOne
	@JoinColumn(name="fperiodeBean", referencedColumnName="id")
	private FPeriode fperiodeBean;

	@ManyToOne
	@JoinColumn(name="fmatPelBean", referencedColumnName="id")
	private FMatPel fmatPelBean;

	@OneToMany(mappedBy="fkurikulumBean", fetch= FetchType.LAZY)
	@Fetch(FetchMode.JOIN)
	private Set<FtKrs> ftKrsSet;

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

	private Date created = new Date();
	private Date lastModified = new Date();
	private String modifiedBy ="";

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		FKurikulum that = (FKurikulum) o;

		return id == that.id;
	}

	@Override
	public int hashCode() {
		return (int) (id ^ (id >>> 32));
	}

	@Override
	public String toString() {
		return "FKurikulum{" +
				"kode1='" + kode1 + '\'' +
				'}';
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	
	public int getKuotaMale() {
		return kuotaMale;
	}

	public void setKuotaMale(int kuotaMale) {
		this.kuotaMale = kuotaMale;
	}

	public int getKuotaFemale() {
		return kuotaFemale;
	}

	public void setKuotaFemale(int kuotaFemale) {
		this.kuotaFemale = kuotaFemale;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public FPeriode getFperiodeBean() {
		return fperiodeBean;
	}

	public void setFperiodeBean(FPeriode fperiodeBean) {
		this.fperiodeBean = fperiodeBean;
	}

	public FMatPel getFmatPelBean() {
		return fmatPelBean;
	}

	public void setFmatPelBean(FMatPel fmatPelBean) {
		this.fmatPelBean = fmatPelBean;
	}

	public Set<FtKrs> getFtKrsSet() {
		return ftKrsSet;
	}

	public void setFtKrsSet(Set<FtKrs> ftKrsSet) {
		this.ftKrsSet = ftKrsSet;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
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

	public FKompBiaya getFkompBiayaBean() {
		return fkompBiayaBean;
	}

	public void setFkompBiayaBean(FKompBiaya fkompBiayaBean) {
		this.fkompBiayaBean = fkompBiayaBean;
	}

	public Double getAmountBiaya() {
		return amountBiaya;
	}

	public void setAmountBiaya(Double amountBiaya) {
		this.amountBiaya = amountBiaya;
	}

	public String getNotesBiaya() {
		return notesBiaya;
	}

	public void setNotesBiaya(String notesBiaya) {
		this.notesBiaya = notesBiaya;
	}
}