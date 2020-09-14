package com.desgreen.education.siapp.backend.jpa_repository;


import com.desgreen.education.siapp.backend.model.FDivision;
import com.desgreen.education.siapp.backend.model.FKurikulum;
import com.desgreen.education.siapp.backend.model.FSiswa;
import com.desgreen.education.siapp.backend.model.FtKrs;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;


public interface FtKrsJPARepository extends JpaRepository<FtKrs, Long> {

    FtKrs findById(long id);
    List<FtKrs> findByNoUrut(String noUrut);
          
    @Query("SELECT u FROM FtKrs u WHERE u.noUrut LIKE :noUrut AND u.noUrut2 LIKE :noUrut2")
    Collection<FtKrs> findAll(String noUrut, String noUrut2);

    @Query("SELECT u FROM FtKrs u WHERE u.fsiswaBean = :fsiswaBean")
    Collection<FtKrs> findAllBySiswa(FSiswa fsiswaBean);

    @Query("SELECT u FROM FtKrs u WHERE u.fsiswaBean IN :collectionSiswa")
    Collection<FtKrs> findAllBySiswa(Collection<FSiswa> collectionSiswa);


    @Query("SELECT u FROM FtKrs u WHERE u.fdivisionBean = :fdivisionBean")
    Collection<FtKrs> findAllByDivision(FDivision fdivisionBean);

    @Query("SELECT u FROM FtKrs u WHERE u.fdivisionBean IN :collectionDivision")
    Collection<FtKrs> findAllByDivision(Collection<FDivision> collectionDivision);


    @Query("SELECT u FROM FtKrs u WHERE u.fdivisionBean = :fdivisionBean AND u.fkurikulumBean = :fkurikulumBean")
    Collection<FtKrs> findAllByDivisionAndKurikulum(FDivision fdivisionBean, FKurikulum fkurikulumBean);

    @Query("SELECT u FROM FtKrs u WHERE u.fdivisionBean = :fdivisionBean AND u.fsiswaBean = :fsiswaBean")
    Collection<FtKrs> findAllByDivisionAndSiswa(FDivision fdivisionBean, FSiswa fsiswaBean);

    @Query("SELECT u FROM FtKrs u WHERE u.fdivisionBean = :fdivisionBean AND u.fkurikulumBean = :fkurikulumBean  AND u.fsiswaBean = :fsiswaBean")
    Collection<FtKrs> findAll(FDivision fdivisionBean, FKurikulum fkurikulumBean, FSiswa fsiswaBean);


    @Query("SELECT u FROM FtKrs u WHERE u.fdivisionBean = :fdivisionBean" )
    Page<FtKrs> findAllByDivision(FDivision fdivisionBean, Pageable pageable);

}