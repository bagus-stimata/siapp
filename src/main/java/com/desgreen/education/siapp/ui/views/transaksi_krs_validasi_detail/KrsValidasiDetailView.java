package com.desgreen.education.siapp.ui.views.transaksi_krs_validasi_detail;

import com.desgreen.education.siapp.AppPublicService;
import com.desgreen.education.siapp.backend.model.EnumStatApproval;
import com.desgreen.education.siapp.backend.model.FKurikulum;
import com.desgreen.education.siapp.security_config.AuthUserDetailsService;
import com.desgreen.education.siapp.security_model.Role;
import com.desgreen.education.siapp.ui.MainLayout;
import com.desgreen.education.siapp.ui.components.FlexBoxLayout;
import com.desgreen.education.siapp.ui.components.ListItem;
import com.desgreen.education.siapp.ui.components.navigation.bar.AppBar;
import com.desgreen.education.siapp.ui.layout.size.Bottom;
import com.desgreen.education.siapp.ui.layout.size.Horizontal;
import com.desgreen.education.siapp.ui.layout.size.Top;
import com.desgreen.education.siapp.ui.layout.size.Vertical;
import com.desgreen.education.siapp.ui.util.BoxShadowBorders;
import com.desgreen.education.siapp.ui.util.LumoStyles;
import com.desgreen.education.siapp.ui.util.TextColor;
import com.desgreen.education.siapp.ui.util.UIUtils;
import com.desgreen.education.siapp.ui.util.css.BorderRadius;
import com.desgreen.education.siapp.ui.util.css.WhiteSpace;
import com.desgreen.education.siapp.ui.views.ViewFrame;
import com.desgreen.education.siapp.ui.views.transaksi_krs_validasi.KrsValidasiView;
import com.desgreen.education.siapp.ui.utils.common.CommonFileFactory;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;

import javax.annotation.PostConstruct;
import java.time.LocalDate;


@Secured({Role.ADMIN, Role.MNU_VALIDASI_KRS})
@UIScope
@SpringComponent
@Route(value = "KrsValidasiDetailView", layout = MainLayout.class)
@PageTitle("KrsValidasiDetailView")
public class KrsValidasiDetailView extends ViewFrame implements HasUrlParameter<Long> {

	protected KrsValidasiDetailModel model;
	protected KrsValidasiDetailController controller;
	protected KrsValidasiDetailListener listener;

	@Autowired
	private AppPublicService appPublicService;
	@Autowired
	public AuthUserDetailsService authUserDetailsService;


	protected Binder<FKurikulum> binder = new BeanValidationBinder<>(FKurikulum.class);

	public int RECENT_TRANSACTIONS = 4;

	private ListItem listItemMatPel;
	private ListItem listItemDescription;
	private ListItem listItemPeriode;

	private ListItem listItemAlamat;
	private ListItem listItemSambung;
	private ListItem listItemCatatan;

//	private BankAccount account;


	public KrsValidasiDetailView() {
		super();
		/**
		 * Init Dummy
		 */
	}

	@PostConstruct
	private void init(){
		model = new KrsValidasiDetailModel(authUserDetailsService, appPublicService);
		controller = new KrsValidasiDetailController(model, this);
//		model = controller.model;

		listener = controller;
	}

	@Override
	public void setParameter(BeforeEvent beforeEvent, Long paramId) {
//		model.currentDomain = model.ftKrsJPARepository.findById(paramId).get();
		model.reloadCurrentDomain(paramId);
		setViewContent(createContent());

		String isCalonSiswaBaru = "";
		try {
			if (model.currentDomain.getFsiswaBean().getFtKrsSet().size() == 1) isCalonSiswaBaru = "Calon Siswa Baru";
		}catch (Exception ex){}

		listItemMatPel.setPrimaryText(model.currentDomain.getFsiswaBean().getFullName() + " #" +isCalonSiswaBaru);
		listItemDescription.setPrimaryText(model.currentDomain.getFkurikulumBean().getFmatPelBean().getDescription());
		listItemDescription.setSecondaryText("Quota Maksimum " + model.currentDomain.getFkurikulumBean().getKuotaMale() + " siswa & " + model.currentDomain.getFkurikulumBean().getKuotaFemale() + " siswi");
		listItemPeriode.setPrimaryText(UIUtils.formatDate_ddMMMYYYY(model.currentDomain.getFkurikulumBean().getFperiodeBean().getPeriodeFrom()) + " s.d " +
				UIUtils.formatDate_ddMMMYYYY(model.currentDomain.getFkurikulumBean().getFperiodeBean().getPeriodeTo()) );

		listItemAlamat.setPrimaryText(model.currentDomain.getFsiswaBean().getAddress1() + " " + model.currentDomain.getFsiswaBean().getAddress2() + " " + model.currentDomain.getFsiswaBean().getAddress3() +
				" " + model.currentDomain.getFsiswaBean().getCity() + " " + model.currentDomain.getFsiswaBean().getState());

		String daerahSambung = "";
		String desaSambung = "";
		String kelompokSambung = "";
		if (! model.currentDomain.getFsiswaBean().getDaerahSmbg().equals("")) daerahSambung= "Daerah: " + model.currentDomain.getFsiswaBean().getDaerahSmbg();
		if (! model.currentDomain.getFsiswaBean().getDesaSmbg().equals("")) desaSambung= "Desa: " + model.currentDomain.getFsiswaBean().getDesaSmbg();
		if (! model.currentDomain.getFsiswaBean().getKelompokSmbg().equals("")) kelompokSambung= "Kelompok: " + model.currentDomain.getFsiswaBean().getKelompokSmbg();
		listItemSambung.setPrimaryText( daerahSambung + " " + desaSambung + " " + kelompokSambung);

		listItemCatatan.setPrimaryText(model.currentDomain.getFsiswaBean().getNotes1() + ", " + model.currentDomain.getFsiswaBean().getNotes2() + ", " + model.currentDomain.getFsiswaBean().getNotes3());

	}


	@Override
	protected void onAttach(AttachEvent attachEvent) {
		super.onAttach(attachEvent);

		initAppBar();
		UI.getCurrent().getPage().setTitle(model.currentDomain.getFkurikulumBean().getDescription() + " | " + model.currentDomain.getFsiswaBean().getFullName());

//		availability.setPrimaryText(UIUtils.formatAmount(account.getAvailability()));
//		bankAccount.setPrimaryText(account.getAccount());
//		bankAccount.setSecondaryText(account.getBank());
//		updated.setPrimaryText(UIUtils.formatDate(account.getUpdated()));

	}

	private AppBar initAppBar() {
		AppBar appBar = MainLayout.get().getAppBar();
		appBar.searchModeOff();

		appBar.setNaviMode(AppBar.NaviMode.CONTEXTUAL);
		appBar.getContextIcon().addClickListener(e -> UI.getCurrent().navigate(KrsValidasiView.class, model.currentDomain.getId()));
		appBar.setTitle(model.currentDomain.getFkurikulumBean().getDescription() + " | " + model.currentDomain.getFsiswaBean().getFullName());
		return appBar;
	}

	protected void goToParentView_ToRefresh(){
		UI.getCurrent().navigate(KrsValidasiView.class, model.currentDomain.getId());
	}


	protected Component createContent() {
		/**
		 * Kalau Masih kosong maka dia akan panggil sebelumnya
		 */
//		model.itemHeader = new FtKrs();
//		try {
//			model.itemHeader = model.mapKrsCurrentUser.values().stream()
//					.filter(x -> x.getFkurikulumBean().equals(model.itemHeader)).findAny().get();
//		}catch (Exception ex){}
//		System.out.println("KRS AFTER SELECT: " + model.itemHeader.getId() + " >> " + model.itemHeader.isStatSelected());

		FlexBoxLayout content = new FlexBoxLayout(
				createLogoSection(),
				createRecentTransactionsHeader(),
				createRecentTransactionsList()
		);
		content.setFlexDirection(FlexLayout.FlexDirection.COLUMN);
		content.setMargin(Horizontal.AUTO, Vertical.RESPONSIVE_L);
		content.setMaxWidth("840px");
		return content;
	}

	private FlexBoxLayout createLogoSection() {


//		 Image image = new Image(DummyData.getImageSource(model.itemHeader.getFmatPelBean().getLogoIndex()),"Company Logo");
		Image image = new Image();
		try {
			image = CommonFileFactory.generateImage(AppPublicService.FILE_PATH + model.currentDomain.getFsiswaBean().getImageName());
		}catch (Exception ex){}

		image.addClassName(LumoStyles.Margin.Horizontal.L);
		UIUtils.setBorderRadius(BorderRadius._50, image);
		image.setHeight("200px");
		image.setWidth("200px");

		listItemMatPel = new ListItem(UIUtils.createTertiaryIcon(VaadinIcon.USER), "", "Calon Siswa/Siswi");
		listItemMatPel.getPrimary().addClassName(LumoStyles.Heading.H2);
		listItemMatPel.setDividerVisible(true);
		listItemMatPel.setId("itemMatPelId");
		listItemMatPel.setReverse(true);

		listItemDescription = new ListItem(UIUtils.createTertiaryIcon(VaadinIcon.OPEN_BOOK), "", "");
		listItemDescription.setDividerVisible(true);
		listItemDescription.setId("itemDescriptionId");
		listItemDescription.setReverse(true);
		listItemDescription.setWhiteSpace(WhiteSpace.PRE_LINE);

		listItemPeriode = new ListItem(UIUtils.createTertiaryIcon(VaadinIcon.CALENDAR), "", "Periode");
		listItemPeriode.setReverse(true);
		listItemPeriode.setWhiteSpace(WhiteSpace.PRE_LINE);

		listItemAlamat = new ListItem(UIUtils.createTertiaryIcon(VaadinIcon.SITEMAP), "", "Alamat");
		listItemAlamat.setReverse(true);
		listItemAlamat.setWhiteSpace(WhiteSpace.PRE_LINE);

		listItemSambung = new ListItem(UIUtils.createTertiaryIcon(VaadinIcon.HEART), "", "Sambung");
		listItemSambung.setReverse(true);
		listItemSambung.setWhiteSpace(WhiteSpace.PRE_LINE);

		listItemCatatan = new ListItem(UIUtils.createTertiaryIcon(VaadinIcon.CLIPBOARD_CHECK), "", "Catatan");
		listItemCatatan.setReverse(true);
//		listItemCatatan.setWhiteSpace(WhiteSpace.PRE_LINE);

		FlexBoxLayout listItems = new FlexBoxLayout(listItemMatPel, listItemDescription, listItemPeriode,
				listItemAlamat, listItemSambung, listItemCatatan);
		listItems.setFlexDirection(FlexLayout.FlexDirection.COLUMN);

		FlexBoxLayout section = new FlexBoxLayout(image, listItems);
		section.addClassName(BoxShadowBorders.BOTTOM);
		section.setAlignItems(FlexComponent.Alignment.CENTER);
		section.setFlex("1", listItems);
		section.setFlexWrap(FlexLayout.FlexWrap.WRAP);
		section.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
		section.setPadding(Bottom.L);
		return section;
	}

	protected Button btnSetuju;
	protected Button btnTolak;
	private Component createRecentTransactionsHeader() {

		Label title = UIUtils.createH3Label("Progres Pendaftaran");
//		if (model.currentDomain.getEnumStatApproval().equals(EnumStatApproval.APPROVE)) {
//			btnTolak = UIUtils.createErrorButton("TOLAK", VaadinIcon.CLOSE);
//		}else {
//			btnSetuju = UIUtils.createSuccessButton("SETUJU", VaadinIcon.CHECK);
//		}
		btnTolak = UIUtils.createErrorButton("TOLAK", VaadinIcon.CLOSE);
		btnSetuju = UIUtils.createSuccessButton("SETUJU", VaadinIcon.CHECK);

		btnSetuju.addClickListener(e -> {
			listener.aksiBtnSetujuForm();
		});
		btnSetuju.addClassName(LumoStyles.Margin.Left.AUTO);

		btnTolak.addClickListener(e -> {
			listener.aksiBtnTolakForm();
		});
		btnTolak.addClassName(LumoStyles.Margin.Left.AUTO);

		FlexBoxLayout header = new FlexBoxLayout(title, btnSetuju, btnTolak);
		header.setAlignItems(FlexComponent.Alignment.CENTER);
		header.setMargin(Bottom.M, Horizontal.RESPONSIVE_L, Top.L);

		if (model.currentDomain.isStatSelected() && model.currentDomain.getEnumStatApproval().equals(EnumStatApproval.APPROVE)){
			btnSetuju.setEnabled(false);
		}else {
			btnSetuju.setEnabled(true);
		}
		if (model.currentDomain.isStatSelected() && model.currentDomain.getEnumStatApproval().equals(EnumStatApproval.REJECTED)){
			btnTolak.setEnabled(false);
		}else {
			btnTolak.setEnabled(true);
		}

		return header;
	}

	private Component createRecentTransactionsList() {

		Div items = new Div();
		items.addClassNames(BoxShadowBorders.BOTTOM, LumoStyles.Padding.Bottom.L);

		Label itemLabel1 = new Label();
		UIUtils.setTextColor(TextColor.PRIMARY, itemLabel1);
		if (model.currentDomain.isStatSelected() && model.currentDomain.isStatCancel()==false) {
			itemLabel1 = UIUtils.createBoldLabel("DAFTAR");
			UIUtils.setTextColor(TextColor.PRIMARY, itemLabel1);
		}else if (model.currentDomain.isStatSelected() && model.currentDomain.isStatCancel()) {
			itemLabel1 = UIUtils.createBoldLabel("DIBATALKAN");
			UIUtils.setTextColor(TextColor.ERROR, itemLabel1);
		}else {
			itemLabel1 = UIUtils.createBoldLabel("BELUM");
			UIUtils.setTextColor(TextColor.SECONDARY, itemLabel1);
		}

		Icon itemIcon1 = new Icon(VaadinIcon.FILE_ADD);
		itemIcon1.setColor("blue");
		ListItem item1 = new ListItem(itemIcon1,
				"Melakukan Pendaftaran",
				UIUtils.formatDate(LocalDate.now()),
				itemLabel1
		);
		item1.setDividerVisible(true);
		items.add(item1);

		Label itemLabel2 = UIUtils.createBoldLabel("-");
		UIUtils.setTextColor(TextColor.SECONDARY, itemLabel2);
		if (model.currentDomain.getEnumStatApproval().equals(EnumStatApproval.APPROVE)) {
			itemLabel2 = UIUtils.createBoldLabel("DISETUJUI");
			UIUtils.setTextColor(TextColor.SUCCESS, itemLabel2);
		}else if (model.currentDomain.getEnumStatApproval().equals(EnumStatApproval.REJECTED)) {
			itemLabel2 = UIUtils.createBoldLabel("TIDAK DISETUJUI");
			UIUtils.setTextColor(TextColor.ERROR, itemLabel2);
		}
		Icon itemIcon2 = new Icon(VaadinIcon.CHECK);
		itemIcon2.setColor("green");
		ListItem item2 = new ListItem(itemIcon2,
				"Validasi/Approval",
				UIUtils.formatDate(LocalDate.now()),
				itemLabel2
		);
		item2.setDividerVisible(true);
		items.add(item2);

		Label itemLabel3 = UIUtils.createBoldLabel("-");
		UIUtils.setTextColor(TextColor.SECONDARY, itemLabel3);
		Icon itemIcon3 = new Icon(VaadinIcon.MONEY);
		itemIcon3.setColor("#bd2db1");
		ListItem item3 = new ListItem(itemIcon3,
				"Pembayaran",
				UIUtils.formatDate(LocalDate.now()),
				itemLabel3
		);
//		item1.setDividerVisible(true);
		items.add(item3);


		return items;
	}

	private Component createMonthlyOverviewHeader() {
		Label header = UIUtils.createH3Label("Monthly Overview");
		header.addClassNames(LumoStyles.Margin.Vertical.L, LumoStyles.Margin.Responsive.Horizontal.L);
		return header;
	}

	private Component createRecentTransactionsHeader_InfoSiswa() {

		Label title = UIUtils.createH3Label("Informasi Siswa");

		FlexBoxLayout header = new FlexBoxLayout(title);
		header.setAlignItems(FlexComponent.Alignment.CENTER);
		header.setMargin(Bottom.M, Horizontal.RESPONSIVE_L, Top.L);


		return header;
	}


	private Component createRecentTransactionsList_InfoSiswa() {

		Div items = new Div();
		items.addClassNames(BoxShadowBorders.BOTTOM, LumoStyles.Padding.Bottom.L);


		Label itemLabel1 = new Label();
		UIUtils.setTextColor(TextColor.PRIMARY, itemLabel1);

		itemLabel1 = UIUtils.createBoldLabel("DAFTAR");
		UIUtils.setTextColor(TextColor.PRIMARY, itemLabel1);

		Icon itemIcon1 = new Icon(VaadinIcon.FILE_ADD);
		itemIcon1.setColor("blue");
		ListItem item1 = new ListItem(itemIcon1,
				"Melakukan Pendaftaran",
				UIUtils.formatDate(LocalDate.now()),
				itemLabel1
		);
//		item1.setDividerVisible(true);
		items.add(item1);


		return items;
	}

}
