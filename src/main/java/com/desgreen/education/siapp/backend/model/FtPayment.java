package com.desgreen.education.siapp.backend.model;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * Jenis Pembayaran EnumPaymentType
 * 1. Potongan (Elemintasi Biaya tanpa dibebankan)
 * 2. Cash/Tunai
 * 3. Transfer
 */

@Entity
@Table(name="ftpayment")
public class FtPayment {

	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	private long id =-1;
	private String noRek ="";
	private LocalDate trDate =LocalDate.now();
	private EnumPaymentType paymentType;
	private String bank ="";
	private String transferNumber ="";
	private String notes ="";

	@Column( columnDefinition="Decimal(10,2) default '0.00'")
	private Double paidRp =0.0;

	@ManyToOne
	@JoinColumn(name="fdivisionBean", referencedColumnName="id")
	private FDivision fdivisionBean;

	@ManyToOne
	@JoinColumn(name="fsiswaBean", referencedColumnName="id")
	private FSiswa fsiswaBean;

	@ManyToOne
	@JoinColumn(name="ftArBean", referencedColumnName="id")
	private FtAr ftArBean;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		FtPayment ftPayment = (FtPayment) o;

		return id == ftPayment.id;
	}

	@Override
	public int hashCode() {
		return (int) (id ^ (id >>> 32));
	}

	@Override
	public String toString() {
		return "FtPayment{" +
				"id=" + id +
				", noRek='" + noRek + '\'' +
				'}';
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNoRek() {
		return noRek;
	}

	public void setNoRek(String noRek) {
		this.noRek = noRek;
	}

	public LocalDate getTrDate() {
		return trDate;
	}

	public void setTrDate(LocalDate trDate) {
		this.trDate = trDate;
	}

	public EnumPaymentType getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(EnumPaymentType paymentType) {
		this.paymentType = paymentType;
	}

	public String getBank() {
		return bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public String getTransferNumber() {
		return transferNumber;
	}

	public void setTransferNumber(String transferNumber) {
		this.transferNumber = transferNumber;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
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

	public FtAr getFtArBean() {
		return ftArBean;
	}

	public void setFtArBean(FtAr ftArBean) {
		this.ftArBean = ftArBean;
	}

	public Double getPaidRp() {
		return paidRp;
	}

	public void setPaidRp(Double paidRp) {
		this.paidRp = paidRp;
	}
}