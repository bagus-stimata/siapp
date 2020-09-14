package com.desgreen.education.siapp.backend.model;

import javax.persistence.*;

@Entity
@Table(name="fdonatur")
public class FDonatur {

	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	private long id;
	private String noRek;
	private String fullName;
	private String address1;
	private String address2;
	private String city;
	private String state;
	private String inAddress1;
	private String inAddress2;
	private String inCity1;
	private String phone;
	private boolean waPhoneEnable;

	private boolean statActive=true;

	@ManyToOne
	@JoinColumn(name="fdivisionBean", referencedColumnName="id")
	private FDivision fdivisionBean;

}