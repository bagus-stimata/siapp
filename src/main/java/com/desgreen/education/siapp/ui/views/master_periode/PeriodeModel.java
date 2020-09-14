package com.desgreen.education.siapp.ui.views.master_periode;

import com.desgreen.education.siapp.AppPublicService;
import com.desgreen.education.siapp.backend.jpa_repository.FDivisionJPARepository;
import com.desgreen.education.siapp.backend.jpa_repository.FPeriodeJPARepository;
import com.desgreen.education.siapp.backend.model.FDivision;
import com.desgreen.education.siapp.backend.model.FPeriode;
import com.desgreen.education.siapp.security_config.AuthUserDetailsService;
import com.desgreen.education.siapp.security_config.SecurityUtils;
import com.desgreen.education.siapp.security_model.EnumOrganizationLevel;
import com.desgreen.education.siapp.security_model.FUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PeriodeModel {


    /**
     * VAADIN TIDAK BISA DI GINIKAN
     */
    //1. DAO SERVICE: Utama
    protected AuthUserDetailsService authUserDetailsService;
    protected AppPublicService appPublicService;

    protected FPeriodeJPARepository fPeriodeJPARepository;

    protected FDivisionJPARepository fDivisionJPARepository;

    protected FPeriode itemHeader = new FPeriode();
    protected Map<Long, FPeriode> mapHeader = new HashMap<>();

    protected List<FDivision> listFDivision = new ArrayList<>();

    protected FUser userActive = new FUser();

    public PeriodeModel(AuthUserDetailsService authUserDetailsService, AppPublicService appPublicService){
        this.authUserDetailsService = authUserDetailsService;
        this.appPublicService = appPublicService;

        this.fPeriodeJPARepository = appPublicService.fPeriodeJPARepository;
        this.fDivisionJPARepository = appPublicService.fDivisionJPARepository;

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
        List<FPeriode> list = new ArrayList<>();
        if (userActive.getOrganizationLevel().equals(EnumOrganizationLevel.DIV) ) {
            listFDivision = new ArrayList<FDivision>(appPublicService.mapDivisions.values().stream()
                    .filter(x -> x.equals(userActive.getFdivisionBean()) && x.isStatActive()==true).collect(Collectors.toList()));

            list.addAll(fPeriodeJPARepository.findAll().stream().filter(x -> x.getFdivisionBean().equals(userActive.getFdivisionBean())).collect(Collectors.toList()));

        }else if (userActive.getOrganizationLevel().equals(EnumOrganizationLevel.CORP) ){
            listFDivision = new ArrayList<FDivision>(appPublicService.mapDivisions.values().stream()
                    .filter(x -> x.getFcompanyBean().equals(userActive.getFdivisionBean().getFcompanyBean()) && x.isStatActive()==true).collect(Collectors.toList()));

            list.addAll(fPeriodeJPARepository.findAll().stream().filter(x -> x.getFdivisionBean().getFcompanyBean().equals(userActive.getFdivisionBean().getFcompanyBean())).collect(Collectors.toList()));

        }else {
            listFDivision = new ArrayList<>(appPublicService.mapDivisions.values().stream().filter(x -> x.isStatActive()==true).collect(Collectors.toList()));

            list.addAll(fPeriodeJPARepository.findAll());
        }

        for (FPeriode domain : list) {
            mapHeader.put(domain.getId(), domain);
        }

    }


}
