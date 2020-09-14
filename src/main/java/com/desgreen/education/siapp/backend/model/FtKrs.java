package com.desgreen.education.siapp.backend.model;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * nomorUrut = adalah nomor urut per kurikulum
 * misal siswa mengambil matapelajaran/kurikulum bahasa indonesia dan bahasa enggris maka
 * 1. Bahasa Indonesia
 * 2. Bahasa Enggris
 * 
 * 
 * nomorUrut2 = bisa dipakai untuk keperluan lain misal
 * nomor urut bangku
 * 
 * 
 */

@Entity
@Table(name="ftkrs")
public class FtKrs implements Serializable {

	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	private long id = -1;
	private int noUrut = 0;
	private int noUrut2 =0;

	@Column(length = 20)
	private String kode1 ="";

	private String notes = "";

	//Berarti daftar
	private boolean statSelected =false;
	private LocalDate selectedDate =LocalDate.now();

	private boolean statCancel =false;
	private LocalDate cancelDate =LocalDate.now();

	@Enumerated(EnumType.STRING)
	private EnumStatApproval enumStatApproval = EnumStatApproval.OPEN;
	private LocalDate approveDate =LocalDate.now();
	@Column(length = 60)
	private String approveBy = "";

	@Enumerated(EnumType.STRING)
	private EnumStatKelulusan enumStatKelulusan = EnumStatKelulusan.OPEN;
	private LocalDate kelulusanDate =LocalDate.now();
	private Integer nilaiKelulusan =0;
	@Column(length = 60)
	private String diluluskanOleh = "";





	@ManyToOne
	@JoinColumn(name="fdivisionBean", referencedColumnName="id")
	private FDivision fdivisionBean;

	@ManyToOne
	@JoinColumn(name="fkurikulumBean", referencedColumnName="id")
	private FKurikulum fkurikulumBean;

	@ManyToOne
	@JoinColumn(name="fsiswaBean", referencedColumnName="id")
	private FSiswa fsiswaBean;

	@Transient
	public boolean isNewDomain() {
		return getId() == -1;
	}

	public String getInitials() {
		if (kode1 !=null) {
			if (kode1.trim().length() > 2) {
				return (kode1.trim().substring(0, 2))
						.toUpperCase();
			}
			if (kode1.trim().length() == 1) {
				return (kode1.trim().substring(0, 1))
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

		FtKrs ftKrs = (FtKrs) o;

		return id == ftKrs.id;
	}

	@Override
	public int hashCode() {
		return (int) (id ^ (id >>> 32));
	}

	@Override
	public String toString() {
		return "FtKrs{" +
				"noUrut=" + noUrut +
				", notes='" + notes + '\'' +
				'}';
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getNoUrut() {
		return noUrut;
	}

	public void setNoUrut(int noUrut) {
		this.noUrut = noUrut;
	}

	public int getNoUrut2() {
		return noUrut2;
	}

	public void setNoUrut2(int noUrut2) {
		this.noUrut2 = noUrut2;
	}

	public String getKode1() {
		return kode1;
	}

	public void setKode1(String kode1) {
		this.kode1 = kode1;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public boolean isStatSelected() {
		return statSelected;
	}

	public void setStatSelected(boolean statSelected) {
		this.statSelected = statSelected;
	}

	public LocalDate getSelectedDate() {
		return selectedDate;
	}

	public void setSelectedDate(LocalDate selectedDate) {
		this.selectedDate = selectedDate;
	}

	public boolean isStatCancel() {
		return statCancel;
	}

	public void setStatCancel(boolean statCancel) {
		this.statCancel = statCancel;
	}

	public LocalDate getCancelDate() {
		return cancelDate;
	}

	public void setCancelDate(LocalDate cancelDate) {
		this.cancelDate = cancelDate;
	}

	public EnumStatApproval getEnumStatApproval() {
		return enumStatApproval;
	}

	public void setEnumStatApproval(EnumStatApproval enumStatApproval) {
		this.enumStatApproval = enumStatApproval;
	}

	public LocalDate getApproveDate() {
		return approveDate;
	}

	public void setApproveDate(LocalDate approveDate) {
		this.approveDate = approveDate;
	}

	public String getApproveBy() {
		return approveBy;
	}

	public void setApproveBy(String approveBy) {
		this.approveBy = approveBy;
	}

	public FDivision getFdivisionBean() {
		return fdivisionBean;
	}

	public void setFdivisionBean(FDivision fdivisionBean) {
		this.fdivisionBean = fdivisionBean;
	}

	public FKurikulum getFkurikulumBean() {
		return fkurikulumBean;
	}

	public void setFkurikulumBean(FKurikulum fkurikulumBean) {
		this.fkurikulumBean = fkurikulumBean;
	}

	public FSiswa getFsiswaBean() {
		return fsiswaBean;
	}

	public void setFsiswaBean(FSiswa fsiswaBean) {
		this.fsiswaBean = fsiswaBean;
	}

	public EnumStatKelulusan getEnumStatKelulusan() {
		return enumStatKelulusan;
	}

	public void setEnumStatKelulusan(EnumStatKelulusan enumStatKelulusan) {
		this.enumStatKelulusan = enumStatKelulusan;
	}

	public LocalDate getKelulusanDate() {
		return kelulusanDate;
	}

	public void setKelulusanDate(LocalDate kelulusanDate) {
		this.kelulusanDate = kelulusanDate;
	}

	public Integer getNilaiKelulusan() {
		return nilaiKelulusan;
	}

	public void setNilaiKelulusan(Integer nilaiKelulusan) {
		this.nilaiKelulusan = nilaiKelulusan;
	}

	public String getDiluluskanOleh() {
		return diluluskanOleh;
	}

	public void setDiluluskanOleh(String diluluskanOleh) {
		this.diluluskanOleh = diluluskanOleh;
	}
}