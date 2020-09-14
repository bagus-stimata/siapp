package com.desgreen.education.siapp.security_repository;

import com.desgreen.education.siapp.security_model.FUser;
import com.desgreen.education.siapp.security_model.FUserRoles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRolesJPARepository extends JpaRepository<FUserRoles, Long> {
    FUserRoles findById(long id);

    @Query("SELECT u FROM FUserRoles u WHERE u.fuserBean = :fuserBean" )
    List<FUserRoles>  findAllByUse(FUser fuserBean);

    @Query("DELETE FROM FUserRoles u WHERE u.fuserBean = :fuserBean" )
    void  deleteAllByUser(FUser fuserBean);

    @Query("DELETE FROM FUserRoles u WHERE u IN :listFUserRoles" )
    void  deleteAllByListUser(List<FUserRoles> listFUserRoles);

}
