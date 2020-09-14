package com.desgreen.education.siapp.backend.jpa_repository;


import com.desgreen.education.siapp.backend.model.FDivision;
import com.desgreen.education.siapp.backend.model.FKurikulum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;


public interface FKurikulumJPARepository extends JpaRepository<FKurikulum, Long> {

    FKurikulum findById(long id);
    List<FKurikulum> findByKode1(String kode1);

    @Query("SELECT u FROM FKurikulum u WHERE u.statActive = true" )
    Collection<FKurikulum> findAllActive();

    @Query("SELECT u FROM FKurikulum u WHERE u.kode1 LIKE :kode1")
    List<FKurikulum>  findAll(String kode1);


    @Query("SELECT u FROM FKurikulum u WHERE u.fdivisionBean = :fdivisionBean" )
    Collection<FKurikulum> findAllByParent(FDivision fdivisionBean);

    @Query("SELECT u FROM FKurikulum u WHERE u.fdivisionBean IN :collectionDivision" )
    Collection<FKurikulum> findAllByParent(Collection<FDivision> collectionDivision);

    @Query("SELECT u FROM FKurikulum u WHERE u.fdivisionBean = :fdivisionBean" )
    Page<FKurikulum> findAllByDivision(FDivision fdivisionBean, Pageable pageable);

}