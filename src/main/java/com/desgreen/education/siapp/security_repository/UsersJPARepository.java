package com.desgreen.education.siapp.security_repository;

import com.desgreen.education.siapp.security_model.FUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsersJPARepository extends JpaRepository<FUser, Long> {
    FUser findByEmail(String email);
    // Optional<FUser> findByUserName(String userName);
    FUser findByUsername(String username);

    @Query("SELECT u FROM fuser u WHERE u.email LIKE :email" )
    Optional<FUser> findAllByTheEmail(String email);

    @Query("SELECT u FROM fuser u WHERE u.email LIKE :email" )
    List<FUser> findAllByEmail(String email);

//    @Query("SELECT u FROM FUser u WHERE u.fdivisionBean = :fdivisionBean" )
//    List<FUser> findAllByParent(FDivision fdivisionBean);

//    @Query("SELECT u FROM FUser u WHERE u.fdivisionBean.Id = :fdivisionBeanId" )
//    List<FUser> findAllByParent(Long fdivisionBeanId);

//    @Query("SELECT u FROM FUser u WHERE u.fdivisionBean IN :listFdivisionBean" )
//    List<FUser> findAllByListParent(List<FDivision> listFdivisionBean);


    FUser findByIdSiswa(Long idSiswa);

    //    @Query("DELETE FROM FUser WHERE idSiswa = :idSiswa" )
    long deleteByIdSiswa(Long idSiswa);

}
