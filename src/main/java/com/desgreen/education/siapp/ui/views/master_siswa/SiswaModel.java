package com.desgreen.education.siapp.ui.views.master_siswa;

import com.desgreen.education.siapp.AppPublicService;
import com.desgreen.education.siapp.backend.jpa_repository.FSiswaJPARepository;
import com.desgreen.education.siapp.backend.jpa_repository.FtKrsJPARepository;
import com.desgreen.education.siapp.backend.model.FDivision;
import com.desgreen.education.siapp.backend.model.FSiswa;
import com.desgreen.education.siapp.security_config.AuthUserDetailsService;
import com.desgreen.education.siapp.security_config.SecurityUtils;
import com.desgreen.education.siapp.security_model.EnumOrganizationLevel;
import com.desgreen.education.siapp.security_model.FUser;
import com.desgreen.education.siapp.security_repository.UsersJPARepository;
import com.vaadin.flow.router.Route;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Route(value = "CompanyModel")
public class SiswaModel {

    /**
     * VAADIN TIDAK BISA DI GINIKAN
     */
    //1. DAO SERVICE: Utama
    protected AuthUserDetailsService authUserDetailsService;
    protected AppPublicService appPublicService;

    protected UsersJPARepository usersJPARepository;
    protected FSiswaJPARepository fSiswaJPARepository;
    protected FtKrsJPARepository ftKrsJPARepository;

    protected List<FDivision> listFDivision = new ArrayList<>();

//    protected FSiswa itemHeader = new FSiswa(); //belum kepakai
    protected FSiswa currentDomain = new FSiswa();
    protected FSiswa oldDomain = new FSiswa();
    protected Map<Long, FSiswa> mapHeader = new HashMap<>();

    protected FUser userActive = new FUser();

    public SiswaModel(AuthUserDetailsService authUserDetailsService, AppPublicService appPublicService){
        this.authUserDetailsService = authUserDetailsService;
        this.appPublicService = appPublicService;
        this.usersJPARepository = appPublicService.userJPARepository;

        this.fSiswaJPARepository = appPublicService.fSiswaJPARepository;
        this.ftKrsJPARepository = appPublicService.ftKrsJPARepository;

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

//        System.out.println("User: " + userActive.getFullName());

        List<FSiswa> list = new ArrayList<>();
        if (userActive.getOrganizationLevel().equals(EnumOrganizationLevel.DIV) ) {
            listFDivision = new ArrayList<FDivision>(appPublicService.mapDivisions.values().stream()
                    .filter(x -> x.equals(userActive.getFdivisionBean()) && x.isStatActive()==true).collect(Collectors.toList()));

            list.addAll(fSiswaJPARepository.findAllByDivision(userActive.getFdivisionBean()));

        }else if (userActive.getOrganizationLevel().equals(EnumOrganizationLevel.CORP) ){

            listFDivision = new ArrayList<FDivision>(appPublicService.mapDivisions.values().stream()
                    .filter(x -> x.getFcompanyBean().equals(userActive.getFdivisionBean().getFcompanyBean()) && x.isStatActive()==true).collect(Collectors.toList()));

            list.addAll(fSiswaJPARepository.findAllByDivision(new ArrayList<>(userActive.getFdivisionBean().getFcompanyBean().getFdivisionSet())));

        }else {
            listFDivision = new ArrayList<>(appPublicService.mapDivisions.values().stream().filter(x -> x.isStatActive()==true).collect(Collectors.toList()));

            list.addAll(fSiswaJPARepository.findAll());
        }

        for (FSiswa domain : list) {
            mapHeader.put(domain.getId(), domain);
        }


    }

}
