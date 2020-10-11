package com.desgreen.education.siapp.ui.views.ppdb_online;

import com.desgreen.education.siapp.AppPublicService;
import com.desgreen.education.siapp.backend.jpa_repository.FDivisionJPARepository;
import com.desgreen.education.siapp.backend.jpa_repository.FKurikulumJPARepository;
import com.desgreen.education.siapp.backend.jpa_repository.FSiswaJPARepository;
import com.desgreen.education.siapp.backend.jpa_repository.FtKrsJPARepository;
import com.desgreen.education.siapp.backend.model.FSiswa;
import com.desgreen.education.siapp.security_config.AuthUserDetailsService;
import com.desgreen.education.siapp.security_config.SecurityUtils;
import com.desgreen.education.siapp.security_model.FUser;
import com.desgreen.education.siapp.security_repository.UsersJPARepository;

import java.util.HashMap;
import java.util.Map;

public class PpdbOnlineModel {
    protected AuthUserDetailsService authUserDetailsService;
    protected AppPublicService appPublicService;

    protected UsersJPARepository usersJPARepository;

    protected FKurikulumJPARepository fKurikulumJPARepository;
    protected FtKrsJPARepository ftKrsJPARepository;
    protected FSiswaJPARepository fSiswaJPARepository;

    protected FDivisionJPARepository fDivisionJPARepository;

    protected FSiswa itemHeader = new FSiswa();
    protected Map<Long, FSiswa> mapHeader = new HashMap<>();

    protected FUser userActive = new FUser();

    public PpdbOnlineModel(AuthUserDetailsService authUserDetailsService, AppPublicService appPublicService){

        this.authUserDetailsService = authUserDetailsService;
        this.appPublicService = appPublicService;

        this.usersJPARepository = appPublicService.userJPARepository;

        this.fKurikulumJPARepository = appPublicService.fKurikulumJPARepository;
        this.ftKrsJPARepository = appPublicService.ftKrsJPARepository;
        this.fSiswaJPARepository = appPublicService.fSiswaJPARepository;

        this.fSiswaJPARepository = fSiswaJPARepository;
        this.fDivisionJPARepository = fDivisionJPARepository;
        this.authUserDetailsService = authUserDetailsService;
        userActive = authUserDetailsService.getUserDetail(SecurityUtils.getUsername());

        initVariable();
        initVariableData();
    }

    protected void initVariable(){
    }

    protected void initVariableData(){
//        reloadListHeader();
    }

    protected void reloadListHeader(){
        for (FSiswa domain: fSiswaJPARepository.findAll()) {
            mapHeader.put(domain.getId(), domain);
        }
    }
    protected boolean isCekEmailAda(String emailValue){
//        System.out.println("Isine Email " + emailValue);
//        for (FUser user: usersJPARepository.findAllByEmail(emailValue.trim())) {
//            System.out.println("adanya " + user.getEmail());
//        }
        FUser fUser = usersJPARepository.findAllByTheEmail(emailValue).orElse(new FUser());
        if (fUser.getId()>0) {
            return true;
        }else {
            return false;
        }
    }

}
