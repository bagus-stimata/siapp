package com.desgreen.education.siapp.backend.jpa_repository;


import com.desgreen.education.siapp.backend.model.FDivision;
import com.desgreen.education.siapp.backend.model.FSiswa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface FSiswaJPARepository extends JpaRepository<FSiswa, Long> {

    FSiswa findById(long id);
    List<FSiswa> findByNis(String nis);
          
    @Query("SELECT u FROM FSiswa u WHERE u.nis LIKE :nis AND u.fullName LIKE :fullName")
    List<FSiswa>  findAll(String nis, String fullName);

    @Query("SELECT u FROM FSiswa u WHERE u.nis LIKE :nis AND u.fullName LIKE :fullName AND  u.address1 LIKE :address1  AND  u.city LIKE :city ")
    List<FSiswa>  findAll(String nis, String fullName, String address1, String city);

    @Query("SELECT u FROM FSiswa u WHERE u.fdivisionBean = :fdivisionBean" )
    List<FSiswa>  findAllByDivision(FDivision fdivisionBean);

    @Query("SELECT u FROM FSiswa u WHERE u.fdivisionBean IN :listFdivision" )
    List<FSiswa>  findAllByDivision(List<FDivision> listFdivision);

    @Query("SELECT u FROM FSiswa u WHERE u.fdivisionBean = :fdivisionBean" )
    Page<FSiswa> findAllByDivision(FDivision fdivisionBean, Pageable pageable);

}