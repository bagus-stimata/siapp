package com.desgreen.education.siapp.backend.jpa_repository;


import com.desgreen.education.siapp.backend.model.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface FtAccountReceivableJPARepository extends JpaRepository<FtAr, Long> {

    FtAr findById(long id);
    List<FtAr> findByNoRek(String noRek);
          
    @Query("SELECT u FROM FtAr u WHERE u.noRek LIKE :noRek AND u.description LIKE :description")
    List<FtAr>  findAll(String noRek, String description);

    @Query("SELECT u FROM  FtAr u WHERE u.fdivisionBean = :fdivisionBean AND u.fperiodeBean = :fperiodeBean")
    List<FtPayment>  findAll(FDivision fdivisionBean, FPeriode fperiodeBean);

    @Query("SELECT u FROM  FtAr u WHERE u.fdivisionBean = :fdivisionBean AND u.fkompBiayaBean = :fkompBiayaBean")
    List<FtPayment>  findAll(FDivision fdivisionBean, FKompBiaya fkompBiayaBean);

    @Query("SELECT u FROM  FtAr u WHERE u.fdivisionBean = :fdivisionBean AND u.fperiodeBean = :fperiodeBean  AND u.fkompBiayaBean = :fkompBiayaBean")
    List<FtPayment>  findAll(FDivision fdivisionBean, FPeriode fperiodeBean, FKompBiaya fkompBiayaBean);

    @Query("SELECT u FROM FtAr u WHERE u.fdivisionBean = :fdivisionBean" )
    List<FSiswa>  findAllByDivision(FDivision fdivisionBean);

    @Query("SELECT u FROM FtAr u WHERE u.fdivisionBean = :fdivisionBean" )
    Page<FtAr> findAllByDivision(FDivision fdivisionBean, Pageable pageable);

}