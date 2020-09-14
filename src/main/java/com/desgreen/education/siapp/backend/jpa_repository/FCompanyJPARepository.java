package com.desgreen.education.siapp.backend.jpa_repository;


import com.desgreen.education.siapp.backend.model.FCompany;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface FCompanyJPARepository extends JpaRepository<FCompany, Long> {

    FCompany findById(long id);
    List<FCompany> findByKode1(String kode1);
          
    @Query("SELECT u FROM FCompany u WHERE u.kode1 LIKE :kode1 and u.description LIKE :description")
    List<FCompany>  findAll(String kode1, String description);

}