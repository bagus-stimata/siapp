package com.desgreen.education.siapp.backend.service;

import com.desgreen.education.siapp.backend.jpa_repository.FCompanyJPARepository;
import com.desgreen.education.siapp.backend.model.FCompany;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * The UserServiceImpl class
 *
 * @author ibrahim KARAYEL
 * @version 1.0
 * Date 4/27/2018.
 */
@Service
@Transactional
public class FCompanyServiceImpl implements FCompanyService {

    @Autowired
    private FCompanyJPARepository fCompanyJPARepository;

    @Autowired
    public void setfCompanyJPARepository(FCompanyJPARepository fCompanyJPARepository) {
        this.fCompanyJPARepository = fCompanyJPARepository;
    }

    @Override
    public FCompany save(FCompany fCompany) {
        return fCompanyJPARepository.save(fCompany);
    }

    @Override
    public Boolean delete(Long id) {
        if (fCompanyJPARepository.existsById(id)) {
            fCompanyJPARepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public FCompany update(FCompany fCompany) {
        return fCompanyJPARepository.save(fCompany);
    }

    @Override
    public FCompany findById(Long id) {
        return fCompanyJPARepository.findById(id).get();
    }

    @Override
    public List<FCompany> findAll() {
        return fCompanyJPARepository.findAll();
    }

    @Override
    public List<FCompany> findAll(String kode1, String description) {
        return fCompanyJPARepository.findAll( kode1, description );
    }

}
