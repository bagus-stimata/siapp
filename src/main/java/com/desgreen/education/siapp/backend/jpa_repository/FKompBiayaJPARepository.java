package com.desgreen.education.siapp.backend.jpa_repository;


import com.desgreen.education.siapp.backend.model.FDivision;
import com.desgreen.education.siapp.backend.model.FKompBiaya;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;


public interface FKompBiayaJPARepository extends JpaRepository<FKompBiaya, Long> {

    FKompBiaya findById(long id);
    List<FKompBiaya> findByKode1(String kode1);
          
    @Query("SELECT u FROM FKompBiaya u WHERE u.kode1 LIKE :kode1 AND u.description LIKE :description")
    List<FKompBiaya>  findAll(String kode1, String description);


    @Query("SELECT u FROM FKompBiaya u WHERE u.fdivisionBean = :fdivisionBean" )
    Collection<FKompBiaya> findAllByParent(FDivision fdivisionBean);

    @Query("SELECT u FROM FKompBiaya u WHERE u.fdivisionBean IN :collectionFDivisionBean" )
    Collection<FKompBiaya> findAllByParent(Collection<FDivision> collectionFDivisionBean);

    @Query("SELECT u FROM FKompBiaya u WHERE u.fdivisionBean = :fdivisionBean" )
    Page<FKompBiaya> findAllByDivision(FDivision fdivisionBean, Pageable pageable);

}