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
@Table(name="ftdatakesehatan")
public class FtDataKesehatan implements Serializable {

	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	private long id = -1;
	private int noUrut = 0;

	@Column(length = 20)
	private String kode1 ="";

	private String notes = "";

	private LocalDate trDate =LocalDate.now();

	@Column(length = 500)
	private String keluhan = "";
	@Column(length = 500)
	private String terapiTindakan = "";
	@Column(length = 500)
	private String terapiObat = "";


	@ManyToOne
	@JoinColumn(name="fdivisionBean", referencedColumnName="id")
	private FDivision fdivisionBean;

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

		FtDataKesehatan ftKrs = (FtDataKesehatan) o;

		return id == ftKrs.id;
	}

	@Override
	public int hashCode() {
		return (int) (id ^ (id >>> 32));
	}

	@Override
	public String toString() {
		return "dataKesehatan{" +
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

	public LocalDate getTrDate() {
		return trDate;
	}

	public void setTrDate(LocalDate trDate) {
		this.trDate = trDate;
	}

	public String getKeluhan() {
		return keluhan;
	}

	public void setKeluhan(String keluhan) {
		this.keluhan = keluhan;
	}

	public String getTerapiTindakan() {
		return terapiTindakan;
	}

	public void setTerapiTindakan(String terapiTindakan) {
		this.terapiTindakan = terapiTindakan;
	}

	public String getTerapiObat() {
		return terapiObat;
	}

	public void setTerapiObat(String terapiObat) {
		this.terapiObat = terapiObat;
	}

	public FDivision getFdivisionBean() {
		return fdivisionBean;
	}

	public void setFdivisionBean(FDivision fdivisionBean) {
		this.fdivisionBean = fdivisionBean;
	}

	public FSiswa getFsiswaBean() {
		return fsiswaBean;
	}

	public void setFsiswaBean(FSiswa fsiswaBean) {
		this.fsiswaBean = fsiswaBean;
	}
}