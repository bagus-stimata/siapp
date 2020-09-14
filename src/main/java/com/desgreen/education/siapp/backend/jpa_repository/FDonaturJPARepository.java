package com.desgreen.education.siapp.backend.jpa_repository;


import com.desgreen.education.siapp.backend.model.FDivision;
import com.desgreen.education.siapp.backend.model.FDonatur;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface FDonaturJPARepository extends JpaRepository<FDonatur, Long> {

    FDonatur findById(long id);
    List<FDonatur> findByNoRek(String noRek);
          
    @Query("SELECT u FROM  FDonatur u WHERE u.noRek LIKE :noRek AND u.fullName LIKE :fullName")
    List<FDonatur>  findAll(String noRek, String fullName);

    @Query("SELECT u FROM  FDonatur u WHERE u.noRek LIKE :noRek AND u.fullName LIKE :fullName AND  u.address1 LIKE :address1  AND  u.city LIKE :city ")
    List<FDonatur>  findAll(String noRek, String fullName, String address1, String city);

    @Query("SELECT u FROM  FDonatur u WHERE u.fdivisionBean = :fdivisionBean" )
    List<FDonatur>  findAllByDivision(FDivision fdivisionBean);

    @Query("SELECT u FROM  FDonatur u WHERE u.fdivisionBean = :fdivisionBean" )
    Page<FDonatur> findAllByDivision(FDivision fdivisionBean, Pageable pageable);

}