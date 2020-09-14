package com.desgreen.education.siapp.ui.views.users_pengguna;

import com.desgreen.education.siapp.AppPublicService;
import com.desgreen.education.siapp.backend.model.FDivision;
import com.desgreen.education.siapp.security_config.AuthUserDetailsService;
import com.desgreen.education.siapp.security_config.SecurityUtils;
import com.desgreen.education.siapp.security_model.EnumOrganizationLevel;
import com.desgreen.education.siapp.security_model.FUser;
import com.desgreen.education.siapp.security_model.FUserRoles;
import com.desgreen.education.siapp.security_model.Role;
import com.desgreen.education.siapp.security_repository.UserRolesJPARepository;
import com.desgreen.education.siapp.security_repository.UsersJPARepository;
import com.vaadin.flow.router.Route;

import java.util.*;
import java.util.stream.Collectors;

@Route(value = "CompanyModel")
public class UsersModel {


    /**
     * VAADIN TIDAK BISA DI GINIKAN
     */
    //1. DAO SERVICE: Utama
    private AppPublicService appPublicService;
    protected AuthUserDetailsService authUserDetailsService;

    protected UsersJPARepository userJPARepository;

    protected UserRolesJPARepository userRolesJPARepository;

    protected Map<Long, FUser> mapHeader = new HashMap<>();
    protected Map<String, FUserRoles> mapTempUserRoles = new HashMap<>();

    protected List<FDivision> listFDivision = new ArrayList<>();

    protected FUser currentDomain;
	protected FUser oldDomain;
    protected FUser userActive = new FUser();

    public UsersModel(AuthUserDetailsService authUserDetailsService, AppPublicService appPublicService){
        this.authUserDetailsService = authUserDetailsService;
        this.appPublicService = appPublicService;
        this.userJPARepository = appPublicService.userJPARepository;
        this.userRolesJPARepository = appPublicService.userRolesJPARepository;

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

        List<FUser> list = new ArrayList<>();
        if (userActive.getOrganizationLevel().equals(EnumOrganizationLevel.DIV) ) {
            listFDivision = new ArrayList<FDivision>(appPublicService.mapDivisions.values().stream()
                            .filter(x -> x.equals(userActive.getFdivisionBean())).collect(Collectors.toList()));

            list.addAll(userJPARepository.findAll().stream().filter(x -> x.getFdivisionBean().equals(userActive.getFdivisionBean())).collect(Collectors.toList()));

        }else if (userActive.getOrganizationLevel().equals(EnumOrganizationLevel.CORP) ){
            listFDivision = new ArrayList<FDivision>(appPublicService.mapDivisions.values().stream()
                    .filter(x -> x.getFcompanyBean().equals(userActive.getFdivisionBean().getFcompanyBean())).collect(Collectors.toList()));

            list.addAll(userJPARepository.findAll().stream().filter(x -> x.getFdivisionBean().getFcompanyBean().equals(userActive.getFdivisionBean().getFcompanyBean())).collect(Collectors.toList()));

        }else {
            listFDivision = new ArrayList<>(appPublicService.mapDivisions.values());
            list.addAll(userJPARepository.findAll());
        }

        for (FUser domain : list) {
            mapHeader.put(domain.getId(), domain);
        }
    }
    protected  void setMapTempUserRoles(FUser fuser) {

        mapTempUserRoles = new HashMap<>();
        long tempId = 1;
        for (String role : Role.getAuthRoles() ) {
            FUserRoles tempUserRole = new FUserRoles();
            tempUserRole.setId(tempId++);
            tempUserRole.setFuserBean(fuser);
            tempUserRole.setRoleID(role);
            tempUserRole.setSelected(false);
            for (String alias: Role.getAuthRoles_Alias()) {
                if (alias.contains(role)) {
                    tempUserRole.setNotes(alias);
                    break;
                }
            }
            mapTempUserRoles.put(role, tempUserRole);
        }
        try {
            for (FUserRoles fUserRole : fuser.getFuserRoles()) {
                try {
                    FUserRoles tempUserRole = fUserRole;
                    tempUserRole.setSelected(true);
                    for (String alias: Role.getAuthRoles_Alias()) {
                        if (alias.contains(fUserRole.getRoleID())) {
                            tempUserRole.setNotes(alias);
                            break;
                        }
                    }
                    mapTempUserRoles.put(tempUserRole.getRoleID(), tempUserRole);
                } catch (Exception ex) {
//                    ex.printStackTrace();
                }
            }
        }catch (Exception ex) {
            ex.printStackTrace();
        }



    }
    protected void deleteCurrentRole(List<FUserRoles> fUserRoles) {
//        userRolesJPARepository.deleteAllByListUser(fUserRoles);
        userRolesJPARepository.deleteAll(fUserRoles);
        for (FUserRoles userRole: fUserRoles) {
            try {
                userRolesJPARepository.delete(userRole);
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }
    protected void saveUpdateUserRoles(Collection<FUserRoles> collectionUserRoles) {
        for (FUserRoles userRole: collectionUserRoles) {
            if (userRole.isSelected()) {
                userRole.setId(0);
                userRole.setFuserBean(currentDomain);
                userRolesJPARepository.save(userRole);
            }
        }//endfor
    }

    protected FUser getFUserFromDb(long id) {
        return userJPARepository.findById(id).get();
    }



}
