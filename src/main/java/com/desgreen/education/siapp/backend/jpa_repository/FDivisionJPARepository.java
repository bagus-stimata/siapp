package com.desgreen.education.siapp.backend.jpa_repository;


import com.desgreen.education.siapp.backend.model.FCompany;
import com.desgreen.education.siapp.backend.model.FDivision;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;


public interface FDivisionJPARepository extends JpaRepository<FDivision, Long> {

    FDivision findById(long id);
    List<FDivision> findByKode1(String kode1);
          
    @Query("SELECT u FROM FDivision u WHERE u.kode1 LIKE :kode1 and u.description LIKE :description")
    List<FDivision>  findAll(String kode1, String description);


    @Query("SELECT u FROM FDivision u WHERE u.fcompanyBean = :fcompanyBean" )
    Collection<FDivision> findAllByParent(FCompany fcompanyBean);

    @Query("SELECT u FROM FDivision u WHERE u.fcompanyBean IN :listFcompanyBean" )
    Collection<FDivision> findAllByParent(Collection<FCompany> listFcompanyBean);

}