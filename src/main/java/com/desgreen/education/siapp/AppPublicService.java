package com.desgreen.education.siapp;

import com.desgreen.education.siapp.app_email.EmailService;
import com.desgreen.education.siapp.backend.jpa_repository.*;
import com.desgreen.education.siapp.backend.model.FCompany;
import com.desgreen.education.siapp.backend.model.FDivision;
import com.desgreen.education.siapp.security_repository.UserRolesJPARepository;
import com.desgreen.education.siapp.security_repository.UsersJPARepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Service
@Primary
public class AppPublicService implements Serializable {

	private static final Logger logger = LoggerFactory.getLogger(AppPublicService.class);

	@Autowired
	public UsersJPARepository userJPARepository;
	@Autowired
	public UserRolesJPARepository userRolesJPARepository;

	@Autowired
	public EmailService emailService;

	@Autowired
	public FCompanyJPARepository fCompanyJPARepository;
	@Autowired
	public FDivisionJPARepository fDivisionJPARepository;

	@Autowired
	public FKurikulumJPARepository fKurikulumJPARepository;
	@Autowired
	public FPeriodeJPARepository fPeriodeJPARepository;
	@Autowired
	public FMatPelJPARepository fMatPelJPARepository;

	@Autowired
	public FSiswaJPARepository fSiswaJPARepository;
	@Autowired
	public FKompBiayaJPARepository fKompBiayaJPARepository;
	@Autowired
	public FtKrsJPARepository ftKrsJPARepository;

	/**
	 * PUBLIC STATIC FOR ALL USER USING APPLICATION
	 */
	public static Map<Long, FCompany> mapCompanys = new HashMap<>();
	public static Map<Long, FDivision> mapDivisions = new HashMap<>();


	public static final String APP_NAME = "SIAPP";
	public static final String APP_DESC1 = "Sistem Administrasi Pondok Pesantren";
	public static final String APP_DESC2 = "Pondok Pesantren LDII";
	public static final String APP_DESC3 = "Jawa Timur";

//	public static final String FILE_PATH = "/Users/yhawin/AppSourceCode/siapp_file/";

	public static final String FILE_PATH= System.getProperty("user.home") + "/AppSourceCode/siapp_files/";
	public static final String REPORT_PATH= System.getProperty("user.home") + "/AppSourceCode/siapp_reports/";

	public static final String PUBLIC_HOST_EMAIL = "ponpesldii@ponpesdahlanikhsan.com";

	private AppPublicService() {
		// Static methods and fields only
		//Buat Jika tidaka ad
		File fileDir = new File(FILE_PATH);
		if (!fileDir.exists()) {
			fileDir.mkdir();
		}
		File reportFileDir = new File(REPORT_PATH);
		if (!reportFileDir.exists()) {
			reportFileDir.mkdir();
		}
	}

	@PostConstruct
	private void init(){

		if (mapCompanys.size()==0) {
			reloadMapCompany();
		}
		if (mapDivisions.size()==0) {
			reloadMapDivision();
		}

	}


	public void reloadMapCompany(){
		for (FCompany domain: fCompanyJPARepository.findAll()) {
			mapCompanys.put(domain.getId(), domain);
		}

	}

	public void reloadMapDivision(){
		for (FDivision domain: fDivisionJPARepository.findAll()) {
			mapDivisions.put(domain.getId(), domain);
		}

	}



}
