package com.desgreen.education.siapp.backend.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="ftdonasiorbonus")
public class FtDonasiOrBonus {

	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	private long id;
	private String noRek;
	private Date trDate;
	private int paymentType;
	private String bank;
	private String transferNumber;
	private double paidRp;
	private String notes;

	private boolean statActive=true;

	@ManyToOne
	@JoinColumn(name="fdivisionBean", referencedColumnName="ID")
	private FDivision fdivisionBean;

}