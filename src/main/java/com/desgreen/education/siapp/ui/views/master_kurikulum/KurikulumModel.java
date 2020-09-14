package com.desgreen.education.siapp.ui.views.master_kurikulum;

import com.desgreen.education.siapp.AppPublicService;
import com.desgreen.education.siapp.backend.jpa_repository.*;
import com.desgreen.education.siapp.backend.model.*;
import com.desgreen.education.siapp.security_config.AuthUserDetailsService;
import com.desgreen.education.siapp.security_config.SecurityUtils;
import com.desgreen.education.siapp.security_model.EnumOrganizationLevel;
import com.desgreen.education.siapp.security_model.FUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class KurikulumModel {


    /**
     * VAADIN TIDAK BISA DI GINIKAN
     */
    protected AuthUserDetailsService authUserDetailsService;
    protected AppPublicService appPublicService;

    protected FKurikulumJPARepository fKurikulumJPARepository;

    protected FDivisionJPARepository fDivisionJPARepository;
	protected FPeriodeJPARepository fPeriodeJPARepository;
	protected FMatPelJPARepository fMatPelJPARepository;
    protected FKompBiayaJPARepository fKompBiayaJPARepository;

//    protected FKurikulum itemHeader = new FKurikulum();
    protected Map<Long, FKurikulum> mapHeader = new HashMap<>();
	protected FKurikulum currentDomain = new FKurikulum();
    protected FSiswa oldDomain = new FSiswa();

    protected List<FDivision> listFDivision = new ArrayList<>(); 
    protected List<FPeriode> listFPeriode = new ArrayList<>(); 
    protected List<FMatPel> listFMatPel = new ArrayList<>();
    protected List<FKompBiaya> listFKompBiaya = new ArrayList<>();

    protected FUser userActive = new FUser();

    public KurikulumModel(AuthUserDetailsService authUserDetailsService, AppPublicService appPublicService){
        this.authUserDetailsService = authUserDetailsService;
        this.appPublicService = appPublicService;

        this.fKurikulumJPARepository = appPublicService.fKurikulumJPARepository;
        this.fDivisionJPARepository = appPublicService.fDivisionJPARepository;
        this.fPeriodeJPARepository = appPublicService.fPeriodeJPARepository;
        this.fMatPelJPARepository = appPublicService.fMatPelJPARepository;
        this.fKompBiayaJPARepository = appPublicService.fKompBiayaJPARepository;

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

        List<FKurikulum> list = new ArrayList<>();
        if (userActive.getOrganizationLevel().equals(EnumOrganizationLevel.DIV) ) {
            listFDivision = new ArrayList<FDivision>(appPublicService.mapDivisions.values().stream()
                    .filter(x -> x.equals(userActive.getFdivisionBean()) && x.isStatActive()==true).collect(Collectors.toList()));

            listFPeriode = new ArrayList<>(fPeriodeJPARepository.findAllByParent(userActive.getFdivisionBean()).stream().filter(x -> x.isStatActive()==true).collect(Collectors.toList()));
            listFMatPel = new ArrayList<>(fMatPelJPARepository.findAllByParent(userActive.getFdivisionBean()).stream().filter(x -> x.isStatActive()==true).collect(Collectors.toList()));
            listFKompBiaya = new ArrayList<>(fKompBiayaJPARepository.findAllByParent(userActive.getFdivisionBean()).stream().filter(x -> x.isStatActive()==true).collect(Collectors.toList()));

//            list.addAll(fKurikulumJPARepository.findAll().stream().filter(x -> x.getFdivisionBean().equals(userActive.getFdivisionBean())).collect(Collectors.toList()));
            list = new ArrayList<>(fKurikulumJPARepository.findAllByParent(userActive.getFdivisionBean()));

        }else if (userActive.getOrganizationLevel().equals(EnumOrganizationLevel.CORP) ){
            listFDivision = new ArrayList<FDivision>(appPublicService.mapDivisions.values().stream()
                    .filter(x -> x.getFcompanyBean().equals(userActive.getFdivisionBean().getFcompanyBean()) && x.isStatActive()==true).collect(Collectors.toList()));

            listFPeriode = new ArrayList<>(fPeriodeJPARepository
                    .findAllByParent(userActive.getFdivisionBean().getFcompanyBean().getFdivisionSet()).stream().filter(x -> x.isStatActive()==true).collect(Collectors.toList()));
            listFMatPel = new ArrayList<>(fMatPelJPARepository
                    .findAllByParent(userActive.getFdivisionBean().getFcompanyBean().getFdivisionSet()).stream().filter(x -> x.isStatActive()==true).collect(Collectors.toList()));
            listFKompBiaya = new ArrayList<>(fKompBiayaJPARepository
                    .findAllByParent(userActive.getFdivisionBean().getFcompanyBean().getFdivisionSet()).stream().filter(x -> x.isStatActive()==true).collect(Collectors.toList()));

//            list.addAll(fKurikulumJPARepository.findAll().stream().filter(x -> x.getFdivisionBean().getFcompanyBean().equals(userActive.getFdivisionBean().getFcompanyBean())).collect(Collectors.toList()));
            list = new ArrayList<>(fKurikulumJPARepository.findAllByParent(userActive.getFdivisionBean().getFcompanyBean().getFdivisionSet()));

        }else {
            listFDivision = new ArrayList<>(appPublicService.mapDivisions.values().stream().filter(x -> x.isStatActive()==true).collect(Collectors.toList()));

            listFPeriode = new ArrayList<>(fPeriodeJPARepository.findAll()).stream().filter(x -> x.isStatActive()==true).collect(Collectors.toList());
            listFMatPel = new ArrayList<>(fMatPelJPARepository.findAll()).stream().filter(x -> x.isStatActive()==true).collect(Collectors.toList());
            listFKompBiaya = new ArrayList<>(fKompBiayaJPARepository.findAll()).stream().filter(x -> x.isStatActive()==true).collect(Collectors.toList());

            list.addAll(fKurikulumJPARepository.findAll());
        }

        for (FKurikulum domain : list) {
            mapHeader.put(domain.getId(), domain);
        }

    }


}
