package com.desgreen.education.siapp.security_model;

import javax.persistence.*;


@Entity
@Table(name = "fuser_roles")
public class FUserRoles {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id=0;

    
    private String roleID = Role.GUEST; //as default

    @ManyToOne
    @JoinColumn(name = "fuserBean", referencedColumnName = "ID")
    private FUser fuserBean;
    
    @Transient
    private boolean selected = true;

    @Transient
    private String notes = "";

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
        FUserRoles other = (FUserRoles) obj;
        if (id != other.id)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "FUserRoles [id=" + id + "]";
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRoleID() {
        return roleID;
    }

    public void setRoleID(String roleID) {
        this.roleID = roleID;
    }

    public FUser getFuserBean() {
        return fuserBean;
    }

    public void setFuserBean(FUser fuserBean) {
        this.fuserBean = fuserBean;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}