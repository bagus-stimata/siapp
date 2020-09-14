package com.desgreen.education.siapp.backend.jpa_repository;


import com.desgreen.education.siapp.backend.model.FDivision;
import com.desgreen.education.siapp.backend.model.FPeriode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;


public interface FPeriodeJPARepository extends JpaRepository<FPeriode, Long> {

    FPeriode findById(long id);
    List<FPeriode> findByKode1(String kode1);
          
    @Query("SELECT u FROM FPeriode u WHERE u.kode1 LIKE :kode1 and u.description LIKE :description")
    Collection<FPeriode>  findAll(String kode1, String description);


    @Query("SELECT u FROM FPeriode u WHERE u.fdivisionBean = :fdivisionBean" )
    Collection<FPeriode> findAllByParent(FDivision fdivisionBean);

    @Query("SELECT u FROM FPeriode u WHERE u.fdivisionBean IN :collectionFDivisionBean" )
    Collection<FPeriode> findAllByParent(Collection<FDivision> collectionFDivisionBean);

}