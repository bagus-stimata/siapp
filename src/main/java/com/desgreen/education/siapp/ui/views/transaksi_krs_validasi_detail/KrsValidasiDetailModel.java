package com.desgreen.education.siapp.ui.views.transaksi_krs_validasi_detail;

import com.desgreen.education.siapp.AppPublicService;
import com.desgreen.education.siapp.app_email.EmailService;
import com.desgreen.education.siapp.backend.jpa_repository.*;
import com.desgreen.education.siapp.backend.model.FDivision;
import com.desgreen.education.siapp.backend.model.FMatPel;
import com.desgreen.education.siapp.backend.model.FPeriode;
import com.desgreen.education.siapp.backend.model.FtKrs;
import com.desgreen.education.siapp.security_config.AuthUserDetailsService;
import com.desgreen.education.siapp.security_config.SecurityUtils;
import com.desgreen.education.siapp.security_model.EnumOrganizationLevel;
import com.desgreen.education.siapp.security_model.FUser;
import com.desgreen.education.siapp.security_repository.UserRolesJPARepository;
import com.desgreen.education.siapp.security_repository.UsersJPARepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class KrsValidasiDetailModel {


    /**
     * VAADIN TIDAK BISA DI GINIKAN
     */
    protected AuthUserDetailsService authUserDetailsService;
    protected AppPublicService appPublicService;

    protected EmailService emailService;

    protected UsersJPARepository usersJPARepository;
    protected UserRolesJPARepository userRolesJPARepository;

    protected FKurikulumJPARepository fKurikulumJPARepository;
    protected FtKrsJPARepository ftKrsJPARepository;
    protected FSiswaJPARepository fSiswaJPARepository;

    protected FDivisionJPARepository fDivisionJPARepository;
	protected FPeriodeJPARepository fPeriodeJPARepository;
	protected FMatPelJPARepository fMatPelJPARepository;

    protected FtKrs currentDomain = new FtKrs();
//    protected Map<Long, FtKrs> mapHeader = new HashMap<>();
    protected Map<Long, FtKrs> mapKrsCurrentUser = new HashMap<>();
//    protected List<FtKrs> listKrsCurrentUser = new ArrayList<>();

    protected List<FDivision> listFDivision = new ArrayList<>();
    protected List<FPeriode> listFPeriode = new ArrayList<>();
    protected List<FMatPel> listFMatPel = new ArrayList<>();

    protected FUser userActive = new FUser();

    public KrsValidasiDetailModel(AuthUserDetailsService authUserDetailsService, AppPublicService appPublicService){
        this.authUserDetailsService = authUserDetailsService;
        this.appPublicService = appPublicService;

        this.emailService = appPublicService.emailService;

        this.usersJPARepository = appPublicService.userJPARepository;
        this.userRolesJPARepository = appPublicService.userRolesJPARepository;

        this.fKurikulumJPARepository = appPublicService.fKurikulumJPARepository;
        this.ftKrsJPARepository = appPublicService.ftKrsJPARepository;
        this.fSiswaJPARepository = appPublicService.fSiswaJPARepository;

        this.fDivisionJPARepository = appPublicService.fDivisionJPARepository;
        this.fPeriodeJPARepository = appPublicService.fPeriodeJPARepository;
        this.fMatPelJPARepository = appPublicService.fMatPelJPARepository;

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

        if (userActive.getOrganizationLevel().equals(EnumOrganizationLevel.DIV) ) {
            listFDivision = new ArrayList<FDivision>(appPublicService.mapDivisions.values().stream()
                    .filter(x -> x.equals(userActive.getFdivisionBean()) && x.isStatActive()==true).collect(Collectors.toList()));

            listFPeriode = new ArrayList<>(fPeriodeJPARepository.findAllByParent(userActive.getFdivisionBean()).stream().filter(x -> x.isStatActive()==true).collect(Collectors.toList()));
            listFMatPel = new ArrayList<>(fMatPelJPARepository.findAllByParent(userActive.getFdivisionBean()).stream().filter(x -> x.isStatActive()==true).collect(Collectors.toList()));


        }else if (userActive.getOrganizationLevel().equals(EnumOrganizationLevel.CORP) ){
            listFDivision = new ArrayList<FDivision>(appPublicService.mapDivisions.values().stream()
                    .filter(x -> x.getFcompanyBean().equals(userActive.getFdivisionBean().getFcompanyBean()) && x.isStatActive()==true).collect(Collectors.toList()));

            listFPeriode = new ArrayList<>(fPeriodeJPARepository
                    .findAllByParent(userActive.getFdivisionBean().getFcompanyBean().getFdivisionSet()).stream().filter(x -> x.isStatActive()==true).collect(Collectors.toList()));
            listFMatPel = new ArrayList<>(fMatPelJPARepository
                    .findAllByParent(userActive.getFdivisionBean().getFcompanyBean().getFdivisionSet()).stream().filter(x -> x.isStatActive()==true).collect(Collectors.toList()));


        }else {
            listFDivision = new ArrayList<>(appPublicService.mapDivisions.values().stream().filter(x -> x.isStatActive()==true).collect(Collectors.toList()));

            listFPeriode = new ArrayList<>(fPeriodeJPARepository.findAll()).stream().filter(x -> x.isStatActive()==true).collect(Collectors.toList());
            listFMatPel = new ArrayList<>(fMatPelJPARepository.findAll()).stream().filter(x -> x.isStatActive()==true).collect(Collectors.toList());

        }

//        emailService.testImplement();
//        emailService.sendSimpleMessage("bagus.stimata@gmail.com", "Pendaftaran Materi/Asrama", "Ini percobaan yang kedua");

    }

    protected void reloadCurrentDomain(Long id){
        currentDomain = ftKrsJPARepository.findById(id).get();
    }


}
