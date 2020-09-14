package com.desgreen.education.siapp.security_model;

import com.desgreen.education.siapp.backend.model.EnumUserType;
import com.desgreen.education.siapp.backend.model.FDivision;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity(name="fuser")
public class FUser implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	// @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="fuser_id_seq")
	// @SequenceGenerator(name="fuser_id_seq", sequenceName="fuser_id_seq", allocationSize=1)
	private long id = -1;

	@NotEmpty
	@Email
	@Size(max = 255)
	@Column(unique = true)
	private String email = "";

	@NotEmpty
	@Size(max = 255)
	@Column(unique = true)
	private String username = "";

	//Image Name
	private String imageName ="";

	//	@NotNull
	// @Size(min = 4, max = 255)
	private String password = "";

	@Transient
	private String passwordConfirm = "";


	@Enumerated(EnumType.STRING)
	private EnumUserType userType = EnumUserType.SYSTEM;
	private Long idSiswa = (long) 0;


	@Column(name = "full_name")
	private String fullName = "";

	@Column(name = "phone")
	private String phone = "";

	@Column(name = "notes")
	private String notes = "";


	@JsonIgnore
	@OneToMany(mappedBy = "fuserBean")
	private List<FUserRoles> fuserRoles = new ArrayList<>();


	@JsonIgnore
	@Transient
	private List<String> tempRoles = new ArrayList<>();
	@JsonIgnore
	@Transient
	private List<FUserRoles> tempUserRoles = new ArrayList<>();


	// @NotBlank
	// @Size(max = 255)
	// private String role;

	private boolean locked = false;

	@Column(name = "created")
	private Date created = new Date();
	@Column(name = "lastmodified")
	private Date lastModified = new Date();
	@Column(name = "modified_by")
	private String modifiedBy = "";

	@Column(length = 5)
	@Enumerated(EnumType.STRING)
	private EnumOrganizationLevel organizationLevel;

	@ManyToOne
	@JoinColumn(name = "fdivisionBean", referencedColumnName = "id")
	private FDivision fdivisionBean;

	@Transient
	public boolean isNewDomain() {
		return getId() == -1;
	}

	public String getInitials() {
		if (fullName != null) {
			if (fullName.trim().length() > 2) {
				return (fullName.trim().substring(0, 2))
						.toUpperCase();
			} else if (fullName.trim().length() == 1) {
				return (fullName.trim().substring(0, 1))
						.toUpperCase();
			} else {
				return "XX";
			}
		} else {
			return "XXX";
		}
	}


	@PrePersist
	@PreUpdate
	private void prepareData() {
		this.email = email == null ? null : email.toLowerCase();
	}


	public FUser() {
		// An empty constructor is needed for all beans
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
		FUser other = (FUser) obj;
		if (id != other.id)
			return false;
		return true;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPasswordConfirm() {
		return passwordConfirm;
	}

	public void setPasswordConfirm(String passwordConfirm) {
		this.passwordConfirm = passwordConfirm;
	}

	public EnumUserType getUserType() {
		return userType;
	}

	public void setUserType(EnumUserType userType) {
		this.userType = userType;
	}

	public Long getIdSiswa() {
		return idSiswa;
	}

	public void setIdSiswa(Long idSiswa) {
		this.idSiswa = idSiswa;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public List<FUserRoles> getFuserRoles() {
		return fuserRoles;
	}

	public void setFuserRoles(List<FUserRoles> fuserRoles) {
		this.fuserRoles = fuserRoles;
	}

	public List<String> getTempRoles() {
		return tempRoles;
	}

	public void setTempRoles(List<String> tempRoles) {
		this.tempRoles = tempRoles;
	}

	public List<FUserRoles> getTempUserRoles() {
		return tempUserRoles;
	}

	public void setTempUserRoles(List<FUserRoles> tempUserRoles) {
		this.tempUserRoles = tempUserRoles;
	}

	public boolean isLocked() {
		return locked;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
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

	public EnumOrganizationLevel getOrganizationLevel() {
		return organizationLevel;
	}

	public void setOrganizationLevel(EnumOrganizationLevel organizationLevel) {
		this.organizationLevel = organizationLevel;
	}

	public FDivision getFdivisionBean() {
		return fdivisionBean;
	}

	public void setFdivisionBean(FDivision fdivisionBean) {
		this.fdivisionBean = fdivisionBean;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}
}
