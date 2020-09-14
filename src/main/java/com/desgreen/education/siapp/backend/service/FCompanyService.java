package com.desgreen.education.siapp.backend.service;

import com.desgreen.education.siapp.backend.model.FCompany;

import java.util.List;

/**
 * The UserService interface
 *
 * @author ibrahim KARAYEL
 * @version 1.0
 * Date 4/27/2018.
 */
public interface FCompanyService {

    FCompany save(FCompany user);

    Boolean delete(Long id);

    FCompany update(FCompany user);

    FCompany findById(Long id);

    List<FCompany> findAll();

    List<FCompany> findAll(String kode1, String description);

}
