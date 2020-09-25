package com.desgreen.education.siapp.ui.views.transaksi_krs_validasi;

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

public class KrsValidasiModel {

    /**
     * VAADIN TIDAK BISA DI GINIKAN
     */
    protected AuthUserDetailsService authUserDetailsService;
    protected AppPublicService appPublicService;

    protected FKurikulumJPARepository fKurikulumJPARepository;
    protected FtKrsJPARepository ftKrsJPARepository;
    protected FSiswaJPARepository fSiswaJPARepository;

    protected FDivisionJPARepository fDivisionJPARepository;
	protected FPeriodeJPARepository fPeriodeJPARepository;
	protected FMatPelJPARepository fMatPelJPARepository;

    protected FtKrs itemHeader = new FtKrs();
    protected FKurikulum itemHeaderKurikulum = new FKurikulum();
    protected Map<Long, FtKrs> mapHeader = new HashMap<>();
    protected Map<Long, FKurikulum> mapKurikulumExist = new HashMap<>();

    protected List<FDivision> listFDivision = new ArrayList<>();
    protected List<FPeriode> listFPeriode = new ArrayList<>();
    protected List<FMatPel> listFMatPel = new ArrayList<>();

    protected FUser userActive = new FUser();

    public KrsValidasiModel(AuthUserDetailsService authUserDetailsService, AppPublicService appPublicService){

        this.authUserDetailsService = authUserDetailsService;
        this.appPublicService = appPublicService;

        this.fKurikulumJPARepository = appPublicService.fKurikulumJPARepository;
        this.ftKrsJPARepository = appPublicService.ftKrsJPARepository;
        this.fSiswaJPARepository = appPublicService.fSiswaJPARepository;

        this.fDivisionJPARepository = appPublicService.fDivisionJPARepository;
        this.fPeriodeJPARepository = appPublicService.fPeriodeJPARepository;
        this.fMatPelJPARepository = appPublicService.fMatPelJPARepository;

        userActive = authUserDetailsService.getUserDetail(SecurityUtils.getUsername());
        initVariable();

        try {
            initVariableData();
        }catch (Exception ex){}
    }

    protected void initVariable(){
    }

    protected void initVariableData(){
        reloadListHeaderParent();
//        reloadListHeader();
    }

    protected void reloadListHeaderParent(){
        if (userActive !=null) {
            if (userActive.getOrganizationLevel().equals(EnumOrganizationLevel.DIV)) {
                listFDivision = new ArrayList<FDivision>(appPublicService.mapDivisions.values().stream()
                        .filter(x -> x.equals(userActive.getFdivisionBean()) && x.isStatActive() == true).collect(Collectors.toList()));

                listFPeriode = new ArrayList<>(fPeriodeJPARepository.findAllByParent(userActive.getFdivisionBean()).stream().filter(x -> x.isStatActive() == true).collect(Collectors.toList()));
                listFMatPel = new ArrayList<>(fMatPelJPARepository.findAllByParent(userActive.getFdivisionBean()).stream().filter(x -> x.isStatActive() == true).collect(Collectors.toList()));

            } else if (userActive.getOrganizationLevel().equals(EnumOrganizationLevel.CORP)) {
                listFDivision = new ArrayList<FDivision>(appPublicService.mapDivisions.values().stream()
                        .filter(x -> x.getFcompanyBean().equals(userActive.getFdivisionBean().getFcompanyBean()) && x.isStatActive() == true).collect(Collectors.toList()));

                listFPeriode = new ArrayList<>(fPeriodeJPARepository
                        .findAllByParent(userActive.getFdivisionBean().getFcompanyBean().getFdivisionSet()).stream().filter(x -> x.isStatActive() == true).collect(Collectors.toList()));
                listFMatPel = new ArrayList<>(fMatPelJPARepository
                        .findAllByParent(userActive.getFdivisionBean().getFcompanyBean().getFdivisionSet()).stream().filter(x -> x.isStatActive() == true).collect(Collectors.toList()));

            } else {
                listFDivision = new ArrayList<>(appPublicService.mapDivisions.values().stream().filter(x -> x.isStatActive() == true).collect(Collectors.toList()));

                listFPeriode = new ArrayList<>(fPeriodeJPARepository.findAll()).stream().filter(x -> x.isStatActive() == true).collect(Collectors.toList());
                listFMatPel = new ArrayList<>(fMatPelJPARepository.findAll()).stream().filter(x -> x.isStatActive() == true).collect(Collectors.toList());

            }

        }

    }
    protected void reloadListHeader(){

        List<FtKrs> list = new ArrayList<>();


        if (userActive !=null) {
            if (userActive.getOrganizationLevel().equals(EnumOrganizationLevel.DIV)) {
                list = new ArrayList<>(ftKrsJPARepository.findAllByDivision(userActive.getFdivisionBean()));

            } else if (userActive.getOrganizationLevel().equals(EnumOrganizationLevel.CORP)) {
                list = new ArrayList<>(ftKrsJPARepository.findAllByDivision(userActive.getFdivisionBean().getFcompanyBean().getFdivisionSet()));

            } else {
                list = new ArrayList<>(ftKrsJPARepository.findAll());
            }

            for (FtKrs domain : list.stream().filter(x -> x.isStatSelected() == true).collect(Collectors.toList())) {
                mapHeader.put(domain.getId(), domain);
                mapKurikulumExist.put(domain.getFkurikulumBean().getId(), domain.getFkurikulumBean());
            }
        }

    }




}
