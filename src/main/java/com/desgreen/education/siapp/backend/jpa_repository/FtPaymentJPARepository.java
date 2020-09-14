package com.desgreen.education.siapp.backend.jpa_repository;


import com.desgreen.education.siapp.backend.model.FDivision;
import com.desgreen.education.siapp.backend.model.FSiswa;
import com.desgreen.education.siapp.backend.model.FtAr;
import com.desgreen.education.siapp.backend.model.FtPayment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface FtPaymentJPARepository extends JpaRepository<FtPayment, Long> {

     FtPayment findById(long id);
    List<FtPayment> findByNoRek(String noRek);
          
    @Query("SELECT u FROM  FtPayment u WHERE u.noRek LIKE :noRek")
    List<FtPayment>  findAll(String noRek);

    @Query("SELECT u FROM  FtPayment u WHERE u.fdivisionBean = :fdivisionBean AND u.ftArBean = :ftArBean")
    List<FtPayment>  findAll(FDivision fdivisionBean, FtAr ftArBean);

    @Query("SELECT u FROM  FtPayment u WHERE u.fdivisionBean = :fdivisionBean AND u.fsiswaBean = :fsiswaBean")
    List<FtPayment>  findAll(FDivision fdivisionBean, FSiswa fsiswaBean);

    @Query("SELECT u FROM  FtPayment u WHERE u.fdivisionBean = :fdivisionBean AND u.ftArBean = :ftArBean  AND u.fsiswaBean = :fsiswaBean")
    List<FtPayment>  findAll(FDivision fdivisionBean, FtAr ftArBean, FSiswa fsiswaBean);

    @Query("SELECT u FROM  FtPayment u WHERE u.fdivisionBean = :fdivisionBean" )
    List<FtPayment>  findAllByDivision(FDivision fdivisionBean);

    @Query("SELECT u FROM  FtPayment u WHERE u.fdivisionBean = :fdivisionBean" )
    Page<FtPayment> findAllByDivision(FDivision fdivisionBean, Pageable pageable);

}