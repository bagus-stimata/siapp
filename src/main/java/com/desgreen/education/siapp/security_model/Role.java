package com.desgreen.education.siapp.security_model;

public class Role {
	/**
	 * boleh tidak ditambahkan ROLE_ 
	 * misal ADMIN menjadi ROLE_ADMIN
	 * penambahan pada:
	 *  	authList.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
	 * 	oleh karena untuk menyamakan dengan konsep @Secure("ROLE_ADMIN") 
	 *  vaadin maka sebaiknya ditambahkan
	 */
	public static final String SYS_ = "ROLE_SYS_";
	public static final String ADMIN = "ROLE_ADMIN";
	public static final String USER = "ROLE_USER";
	public static final String GUEST = "ROLE_GUEST"; //as default

	//Menu digunakan suatu penanda MNU
	public static final String MNU_PPDB_KRS = "ROLE_0101";
	public static final String MNU_VALIDASI_KRS = "ROLE_0102";
	public static final String MNU_INFO_PENTING = "ROLE_0103";

	public static final String MNU_KOMP_BIAYA = "ROLE_0201";
	public static final String MNU_BIAYA_PEMBAYARAN = "ROLE_0202";

	public static final String MNU_MATA_PELAJARAN = "ROLE_0301";
	public static final String MNU_PERIODE_BELAJAR = "ROLE_0302";
	public static final String MNU_KURIKULUM = "ROLE_0303";
	public static final String MNU_MASTER_SISWA = "ROLE_0304";

	public static final String MNU_DONATUR_DONASI = "ROLE_0401";
	public static final String MNU_PENGGUNAAN_DONASI = "ROLE_0402";

	public static final String MNU_SETTING_APLIKASI = "ROLE_0501";
	public static final String MNU_COMPANY_PUSAT = "ROLE_0502";
	public static final String MNU_DIVISION_CABANG = "ROLE_0503";
	public static final String MNU_PENGGUNA_OTORISASI = "ROLE_0504";

	private Role() {
		// Static methods and fields only
	}

	public static String[] getAuthRoles() {
		return new String[] {  SYS_, ADMIN, USER,GUEST,  MNU_PPDB_KRS, MNU_VALIDASI_KRS, MNU_INFO_PENTING,
			MNU_KOMP_BIAYA, MNU_BIAYA_PEMBAYARAN, MNU_MATA_PELAJARAN, MNU_PERIODE_BELAJAR, MNU_KURIKULUM,
			MNU_MASTER_SISWA, MNU_DONATUR_DONASI, MNU_PENGGUNAAN_DONASI, MNU_SETTING_APLIKASI, MNU_COMPANY_PUSAT,
			MNU_DIVISION_CABANG, MNU_PENGGUNA_OTORISASI};
	}

	public static String[] getAuthRoles_Alias() {
		return new String[] { "ROLE_SYS_", "ROLE_ADMIN", "ROLE_USER", "ROLE_GUEST",
				"ROLE_0101_PPDB_KRS", "ROLE_0102_VALIDASI_KRS", "ROLE_0103_MENU_INFO_PENTING",
				"ROLE_0201_KOMPONEN_BIAYA", "ROLE_0202_BIAYA_DAN_PEMBAYARAN",
				"ROLE_0301_MATA_PEAJARAN", "ROLE_0302_PERIODE_BELAJAR", "ROLE_0303_KURIKULUM",
				"ROLE_0304_MASTER_SISWA", "ROLE_0401_DONATUR_DONASI", "ROLE_0402_PENGGUNAAN_DONASI",
				"ROLE_0501_SETTING_APLIKASI", "ROLE_0502_COMPANY_PUSAT",
				"ROLE_0503_DIVISION_CABANG", "ROLE_0504_PENGGUNA_OTORISASI"};
	}


}

