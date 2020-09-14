package com.desgreen.education.siapp.ui.views.master_matpel;

import com.desgreen.education.siapp.AppPublicService;
import com.desgreen.education.siapp.backend.jpa_repository.FDivisionJPARepository;
import com.desgreen.education.siapp.backend.jpa_repository.FMatPelJPARepository;
import com.desgreen.education.siapp.backend.model.FDivision;
import com.desgreen.education.siapp.backend.model.FMatPel;
import com.desgreen.education.siapp.security_config.AuthUserDetailsService;
import com.desgreen.education.siapp.security_config.SecurityUtils;
import com.desgreen.education.siapp.security_model.EnumOrganizationLevel;
import com.desgreen.education.siapp.security_model.FUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MatPelModel {


    /**
     * VAADIN TIDAK BISA DI GINIKAN
     */
    //1. DAO SERVICE: Utama
    protected AppPublicService appPublicService;
    protected AuthUserDetailsService authUserDetailsService;

    protected FMatPelJPARepository fMatPelJPARepository;

    protected FDivisionJPARepository fDivisionJPARepository;

    protected FMatPel itemHeader = new FMatPel();
    protected Map<Long, FMatPel> mapHeader = new HashMap<>();

    protected List<FDivision> listFDivision = new ArrayList<>();
    protected List<Integer> listLogoIndex = new ArrayList<>();

    protected FUser userActive = new FUser();

    public MatPelModel(AuthUserDetailsService authUserDetailsService, AppPublicService appPublicService){
        this.authUserDetailsService = authUserDetailsService;
        this.appPublicService = appPublicService;

        this.fMatPelJPARepository = appPublicService.fMatPelJPARepository;
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

        List<FMatPel> list = new ArrayList<>();
        if (userActive.getOrganizationLevel().equals(EnumOrganizationLevel.DIV) ) {
            listFDivision = new ArrayList<FDivision>(appPublicService.mapDivisions.values().stream()
                    .filter(x -> x.equals(userActive.getFdivisionBean()) && x.isStatActive()==true).collect(Collectors.toList()));

            list.addAll(fMatPelJPARepository.findAll().stream().filter(x -> x.getFdivisionBean()
                    .equals(userActive.getFdivisionBean())).collect(Collectors.toList()));

        }else if (userActive.getOrganizationLevel().equals(EnumOrganizationLevel.CORP) ){
            listFDivision = new ArrayList<FDivision>(appPublicService.mapDivisions.values().stream()
                    .filter(x -> x.getFcompanyBean().equals(userActive.getFdivisionBean().getFcompanyBean()) && x.isStatActive()==true).collect(Collectors.toList()));

            list.addAll(fMatPelJPARepository.findAll().stream().filter(x -> x.getFdivisionBean().getFcompanyBean().equals(userActive.getFdivisionBean().getFcompanyBean())).collect(Collectors.toList()));

        }else {
            listFDivision = new ArrayList<>(appPublicService.mapDivisions.values());
            list.addAll(fMatPelJPARepository.findAll());
        }

        for (FMatPel domain : list) {
            mapHeader.put(domain.getId(), domain);
        }

        for (int i=1; i<40; i++) listLogoIndex.add(i);
    }


}
