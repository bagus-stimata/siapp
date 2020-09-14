package com.desgreen.education.siapp.ui.views.master_division;

import com.desgreen.education.siapp.AppPublicService;
import com.desgreen.education.siapp.backend.jpa_repository.FCompanyJPARepository;
import com.desgreen.education.siapp.backend.jpa_repository.FDivisionJPARepository;
import com.desgreen.education.siapp.backend.model.FCompany;
import com.desgreen.education.siapp.backend.model.FDivision;
import com.desgreen.education.siapp.security_config.AuthUserDetailsService;
import com.desgreen.education.siapp.security_config.SecurityUtils;
import com.desgreen.education.siapp.security_model.EnumOrganizationLevel;
import com.desgreen.education.siapp.security_model.FUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DivisionModel {


    /**
     * VAADIN TIDAK BISA DI GINIKAN
     */
    //1. DAO SERVICE: Utama
    protected AppPublicService appPublicService;
    protected AuthUserDetailsService authUserDetailsService;

    protected FDivisionJPARepository fDivisionJPARepository;

    protected FCompanyJPARepository fCompanyJPARepository;

    protected FDivision currentDomain = new FDivision();
    protected FDivision oldDomain = new FDivision();
    protected Map<Long, FDivision> mapHeader = new HashMap<>();

    protected List<FCompany> listFCompany = new ArrayList<>();

    protected FUser userActive = new FUser();

    public DivisionModel(AuthUserDetailsService authUserDetailsService, AppPublicService appPublicService) {
        this.authUserDetailsService = authUserDetailsService;
        this.appPublicService = appPublicService;
        this.fDivisionJPARepository = appPublicService.fDivisionJPARepository;
        this.fCompanyJPARepository = appPublicService.fCompanyJPARepository;

        userActive = authUserDetailsService.getUserDetail(SecurityUtils.getUsername());

        initVariable();
        initVariableData();
    }

    protected void initVariable() {
    }

    protected void initVariableData() {
        reloadListHeader();
    }

    protected void reloadListHeader() {
        List<FDivision> list = new ArrayList<>();
        if (userActive.getOrganizationLevel().equals(EnumOrganizationLevel.DIV) || userActive.getOrganizationLevel().equals(EnumOrganizationLevel.CORP)) {

            listFCompany = new ArrayList<FCompany>(appPublicService.mapCompanys.values().stream()
                    .filter(x -> x.equals(userActive.getFdivisionBean().getFcompanyBean())).collect(Collectors.toList()));

            list = new ArrayList<>(fDivisionJPARepository.findAllByParent(userActive.getFdivisionBean().getFcompanyBean()));

        } else {
            listFCompany = new ArrayList<FCompany>(appPublicService.mapCompanys.values().stream().collect(Collectors.toList()));

            list = new ArrayList<>(fDivisionJPARepository.findAll());

        }

        for (FDivision domain : list) {
            mapHeader.put(domain.getId(), domain);
        }
    }


}