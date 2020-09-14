package com.desgreen.education.siapp.backend.model;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name="ftaccountreceivable")
public class FtAr {

	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	private long id =-1;
	private String noRek ="";
	private String description ="";
	private LocalDate trDate = LocalDate.now();

	@Column( columnDefinition="Decimal(10,2) default '0.00'")
	private Double amountRp =0.0;

	@Transient
	private double totalPaidRp =0;

	private boolean statusLunas =false;

	@ManyToOne
	@JoinColumn(name="fdivisionBean", referencedColumnName="ID")
	private FDivision fdivisionBean;

	@ManyToOne
	@JoinColumn(name="fperiodeBean", referencedColumnName="id")
	private FPeriode fperiodeBean;

	@ManyToOne
	@JoinColumn(name="fkompBiayaBean", referencedColumnName="id")
	private FKompBiaya fkompBiayaBean;

	@OneToMany(mappedBy="ftArBean", fetch= FetchType.LAZY)
	@Fetch(FetchMode.JOIN)
	private Set<FtPayment> ftPaymentSet;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		FtAr ftAr = (FtAr) o;

		return id == ftAr.id;
	}

	@Override
	public int hashCode() {
		return (int) (id ^ (id >>> 32));
	}

	@Override
	public String toString() {
		return "FtAr{" +
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDate getTrDate() {
		return trDate;
	}

	public void setTrDate(LocalDate trDate) {
		this.trDate = trDate;
	}


	public double getTotalPaidRp() {
		return totalPaidRp;
	}

	public void setTotalPaidRp(double totalPaidRp) {
		this.totalPaidRp = totalPaidRp;
	}

	public boolean isStatusLunas() {
		return statusLunas;
	}

	public void setStatusLunas(boolean statusLunas) {
		this.statusLunas = statusLunas;
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

	public FKompBiaya getFkompBiayaBean() {
		return fkompBiayaBean;
	}

	public void setFkompBiayaBean(FKompBiaya fkompBiayaBean) {
		this.fkompBiayaBean = fkompBiayaBean;
	}

	public Set<FtPayment> getFtPaymentSet() {
		return ftPaymentSet;
	}

	public void setFtPaymentSet(Set<FtPayment> ftPaymentSet) {
		this.ftPaymentSet = ftPaymentSet;
	}

	public Double getAmountRp() {
		return amountRp;
	}

	public void setAmountRp(Double amountRp) {
		this.amountRp = amountRp;
	}
}