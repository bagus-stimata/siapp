package com.desgreen.education.siapp.backend.jpa_repository;


import com.desgreen.education.siapp.backend.model.FDivision;
import com.desgreen.education.siapp.backend.model.FtDonasiOrBonus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface FtDonasiOrBonusJPARepository extends JpaRepository<FtDonasiOrBonus, Long> {

    FtDonasiOrBonus findById(long id);
    List<FtDonasiOrBonus> findByNoRek(String noRek);
          
    @Query("SELECT u FROM FtDonasiOrBonus u WHERE u.noRek  LIKE :noRek")
    List<FtDonasiOrBonus>  findAll(String noRek);


    @Query("SELECT u FROM FtDonasiOrBonus u WHERE u.fdivisionBean = :fdivisionBean" )
    List<FtDonasiOrBonus>  findAllByDivision(FDivision fdivisionBean);

    @Query("SELECT u FROM FtDonasiOrBonus u WHERE u.fdivisionBean = :fdivisionBean" )
    Page<FtDonasiOrBonus> findAllByDivision(FDivision fdivisionBean, Pageable pageable);

}