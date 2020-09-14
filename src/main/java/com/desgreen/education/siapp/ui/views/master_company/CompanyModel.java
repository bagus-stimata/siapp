package com.desgreen.education.siapp.ui.views.master_company;

import com.desgreen.education.siapp.AppPublicService;
import com.desgreen.education.siapp.backend.jpa_repository.FCompanyJPARepository;
import com.desgreen.education.siapp.backend.model.FCompany;
import com.desgreen.education.siapp.security_config.AuthUserDetailsService;
import com.desgreen.education.siapp.security_config.SecurityUtils;
import com.desgreen.education.siapp.security_model.EnumOrganizationLevel;
import com.desgreen.education.siapp.security_model.FUser;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@UIScope
@SpringComponent
public class CompanyModel {


    /**
     * VAADIN TIDAK BISA DI GINIKAN
     */
    //1. DAO SERVICE: Utama

    protected AppPublicService appPublicService;
    protected AuthUserDetailsService authUserDetailsService;

    protected FCompanyJPARepository fCompanyJPARepository;

    protected FCompany currentDomain = new FCompany();
    protected FCompany oldDomain = new FCompany();
    protected Map<Long, FCompany> mapHeader = new HashMap<>();

    protected FUser userActive = new FUser();

    public CompanyModel(AuthUserDetailsService authUserDetailsService, AppPublicService appPublicService){
        this.authUserDetailsService = authUserDetailsService;
        this.appPublicService = appPublicService;

        this.fCompanyJPARepository = appPublicService.fCompanyJPARepository;

        userActive = authUserDetailsService.getUserDetail(SecurityUtils.getUsername());

        initVariable();
        initVariableData();
    }

    protected void initVariable(){
    }

    protected void initVariableData(){
        reloadListHeader();
    }

    protected void reloadListHeader(){
        List<FCompany> list = new ArrayList<>();
        if (userActive.getOrganizationLevel().equals(EnumOrganizationLevel.CORP) ||
                userActive.getOrganizationLevel().equals(EnumOrganizationLevel.DIV) ) {

            list.add(fCompanyJPARepository.findById(userActive.getFdivisionBean().getFcompanyBean().getId()));
        }else {
            list.addAll(fCompanyJPARepository.findAll());
        }

//        list.addAll(fCompanyJPARepository.findAll());

        for (FCompany domain : list) {
            mapHeader.put(domain.getId(), domain);
        }

    }

}
