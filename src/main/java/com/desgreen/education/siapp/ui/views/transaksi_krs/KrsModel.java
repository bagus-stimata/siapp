package com.desgreen.education.siapp.ui.views.transaksi_krs;

import com.desgreen.education.siapp.AppPublicService;
import com.desgreen.education.siapp.backend.jpa_repository.*;
import com.desgreen.education.siapp.backend.model.*;
import com.desgreen.education.siapp.security_config.AuthUserDetailsService;
import com.desgreen.education.siapp.security_config.SecurityUtils;
import com.desgreen.education.siapp.security_model.EnumOrganizationLevel;
import com.desgreen.education.siapp.security_model.FUser;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class KrsModel {


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
    protected Map<Long, FKurikulum> mapHeader = new HashMap<>();
    protected List<FtKrs> listKrsCurrentUser = new ArrayList<>();

    protected List<FDivision> listFDivision = new ArrayList<>();
    protected List<FPeriode> listFPeriode = new ArrayList<>();
    protected List<FMatPel> listFMatPel = new ArrayList<>();

    protected FUser userActive = new FUser();
    protected FSiswa currentFSiswa = new FSiswa();

    public KrsModel(AuthUserDetailsService authUserDetailsService, AppPublicService appPublicService){
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
        initVariableData();
    }

    protected void initVariable(){
    }

    protected void initVariableData(){
        reloadListHeader();
    }

    protected void reloadListHeader(){
        if (userActive !=null) {
            try {
                currentFSiswa = fSiswaJPARepository.findById(userActive.getIdSiswa()).get();

            } catch (Exception ex) {
            }

            List<FKurikulum> list = new ArrayList<>();

            if (userActive.getOrganizationLevel().equals(EnumOrganizationLevel.DIV)) {
                listFDivision = new ArrayList<FDivision>(appPublicService.mapDivisions.values().stream()
                        .filter(x -> x.equals(userActive.getFdivisionBean()) && x.isStatActive() == true).collect(Collectors.toList()));

                listFPeriode = new ArrayList<>(fPeriodeJPARepository.findAllByParent(userActive.getFdivisionBean()).stream().filter(x -> x.isStatActive() == true).collect(Collectors.toList()));
                listFMatPel = new ArrayList<>(fMatPelJPARepository.findAllByParent(userActive.getFdivisionBean()).stream().filter(x -> x.isStatActive() == true).collect(Collectors.toList()));

                list.addAll(fKurikulumJPARepository.findAll().stream().filter(x -> x.isStatActive() == true && x.getFdivisionBean().equals(userActive.getFdivisionBean())).collect(Collectors.toList()));

            } else if (userActive.getOrganizationLevel().equals(EnumOrganizationLevel.CORP)) {
                listFDivision = new ArrayList<FDivision>(appPublicService.mapDivisions.values().stream()
                        .filter(x -> x.getFcompanyBean().equals(userActive.getFdivisionBean().getFcompanyBean()) && x.isStatActive() == true).collect(Collectors.toList()));

                listFPeriode = new ArrayList<>(fPeriodeJPARepository
                        .findAllByParent(userActive.getFdivisionBean().getFcompanyBean().getFdivisionSet()).stream().filter(x -> x.isStatActive() == true).collect(Collectors.toList()));
                listFMatPel = new ArrayList<>(fMatPelJPARepository
                        .findAllByParent(userActive.getFdivisionBean().getFcompanyBean().getFdivisionSet()).stream().filter(x -> x.isStatActive() == true).collect(Collectors.toList()));

                list.addAll(fKurikulumJPARepository.findAll().stream().filter(x -> x.isStatActive() == true && x.getFdivisionBean().getFcompanyBean().equals(userActive.getFdivisionBean().getFcompanyBean())).collect(Collectors.toList()));

            } else {
                listFDivision = new ArrayList<>(appPublicService.mapDivisions.values().stream().filter(x -> x.isStatActive() == true).collect(Collectors.toList()));

                listFPeriode = new ArrayList<>(fPeriodeJPARepository.findAll()).stream().filter(x -> x.isStatActive() == true).collect(Collectors.toList());
                listFMatPel = new ArrayList<>(fMatPelJPARepository.findAll()).stream().filter(x -> x.isStatActive() == true).collect(Collectors.toList());

                list.addAll(fKurikulumJPARepository.findAll().stream().filter(x -> x.isStatActive() == true).collect(Collectors.toList()));
            }

            for (FKurikulum domain : list.stream().filter(x ->
                    (x.getFperiodeBean().getDaftarOpenFrom().isBefore(LocalDate.now()) || x.getFperiodeBean().getDaftarOpenFrom().isEqual(LocalDate.now())) &&
                            (x.getFperiodeBean().getDaftarCloseTo().isAfter(LocalDate.now()) || x.getFperiodeBean().getDaftarCloseTo().isEqual(LocalDate.now()))
                    ).collect(Collectors.toList())) {
                mapHeader.put(domain.getId(), domain);
            }

            try {
                listKrsCurrentUser = new ArrayList<>(ftKrsJPARepository.findAllBySiswa(currentFSiswa));
            } catch (Exception ex) {
            }
        }else {
            for (FKurikulum domain : fKurikulumJPARepository.findAllActive().stream().filter(x ->
                    (x.getFperiodeBean().getDaftarOpenFrom().isBefore(LocalDate.now()) || x.getFperiodeBean().getDaftarOpenFrom().isEqual(LocalDate.now())) &&
                            (x.getFperiodeBean().getDaftarCloseTo().isAfter(LocalDate.now()) || x.getFperiodeBean().getDaftarCloseTo().isEqual(LocalDate.now()))
                    ).collect(Collectors.toList())) {
                mapHeader.put(domain.getId(), domain);
            }

        }//endif


    }


}
