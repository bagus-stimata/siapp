package com.desgreen.education.siapp.backend.jpa_repository;


import com.desgreen.education.siapp.backend.model.FDivision;
import com.desgreen.education.siapp.backend.model.FMatPel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;


public interface FMatPelJPARepository extends JpaRepository<FMatPel, Long> {

    FMatPel findById(long id);
    List<FMatPel> findByKode1(String kode1);
          
    @Query("SELECT u FROM FMatPel u WHERE u.kode1 LIKE :kode1 AND u.description LIKE :description")
    List<FMatPel>  findAll(String kode1, String description);


    @Query("SELECT u FROM FMatPel u WHERE u.fdivisionBean = :fdivisionBean" )
    Collection<FMatPel> findAllByParent(FDivision fdivisionBean);

    @Query("SELECT u FROM FMatPel u WHERE u.fdivisionBean IN :collectionFDivisionBean" )
    Collection<FMatPel> findAllByParent(Collection<FDivision> collectionFDivisionBean);

    @Query("SELECT u FROM FMatPel u WHERE u.fdivisionBean = :fdivisionBean" )
    Page<FMatPel> findAllByDivision(FDivision fdivisionBean, Pageable pageable);

}