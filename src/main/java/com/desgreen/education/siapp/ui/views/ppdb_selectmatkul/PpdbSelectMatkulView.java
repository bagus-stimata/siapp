package com.desgreen.education.siapp.ui.views.ppdb_selectmatkul;

import com.desgreen.education.siapp.AppPublicService;
import com.desgreen.education.siapp.backend.model.FKurikulum;
import com.desgreen.education.siapp.backend.model.FtKrs;
import com.desgreen.education.siapp.backend.model_sample.DummyData;
import com.desgreen.education.siapp.security_config.AuthUserDetailsService;
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
import com.desgreen.education.siapp.ui.views.ViewFrame;
import com.desgreen.education.siapp.ui.views.ppdb_list.PpdbListView;
import com.desgreen.education.siapp.ui.views.ppdb_online.PpdbOnlineView;
import com.desgreen.education.siapp.ui.views.transaksi_krs_detail.KrsDetailView;
import com.desgreen.education.siapp.ui.utils.common.CommonFileFactory;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
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

import javax.annotation.PostConstruct;
import java.text.NumberFormat;

@UIScope
@SpringComponent
@Route(value = "PpdbSelectUserView", layout = MainLayout.class)
@PageTitle("PpdbSelectUserView")
public class PpdbSelectMatkulView extends ViewFrame implements HasUrlParameter<Long> {

	protected PpdbSelectMatkulModel model;
	protected PpdbSelectMatkulController controller;
	protected PpdbSelectMatkulListener listener;

	@Autowired
	private AppPublicService appPublicService;
	@Autowired
	public AuthUserDetailsService authUserDetailsService;

	protected Binder<FKurikulum> binder = new BeanValidationBinder<>(FKurikulum.class);
	protected FKurikulum currentKurikulum;

	public int RECENT_TRANSACTIONS = 4;

	private ListItem listItemMatPel;
	private ListItem listItemPeriode;
	private ListItem listItemCompany;
	private ListItem listItemBiaya;

	public PpdbSelectMatkulView() {
		super();
	}

	@PostConstruct
	private void init(){
		model = new PpdbSelectMatkulModel(authUserDetailsService, appPublicService);
		controller = new PpdbSelectMatkulController(model, this);
//		model = controller.model;

		listener = controller;
	}

	@Override
	public void setParameter(BeforeEvent beforeEvent, Long paramId) {
		currentKurikulum = model.fKurikulumJPARepository.findById(paramId).get();
		setViewContent(createContent());

		listItemMatPel.setPrimaryText(currentKurikulum.getFmatPelBean().getDescription());
		listItemPeriode.setPrimaryText(UIUtils.formatDate_ddMMMYYYY(currentKurikulum.getFperiodeBean().getPeriodeFrom()) + " s.d " +
				UIUtils.formatDate_ddMMMYYYY(currentKurikulum.getFperiodeBean().getPeriodeTo()) );
		listItemCompany.setPrimaryText(currentKurikulum.getFdivisionBean().getFcompanyBean().getDescription() );

		NumberFormat nf = NumberFormat.getInstance(); nf.setMaximumFractionDigits(0);
		listItemBiaya.setPrimaryText("Rp. " + nf.format(currentKurikulum.getAmountBiaya()) + ",- " + currentKurikulum.getNotesBiaya());

	}


	@Override
	protected void onAttach(AttachEvent attachEvent) {
		super.onAttach(attachEvent);

		initAppBar();
		UI.getCurrent().getPage().setTitle(currentKurikulum.getFmatPelBean().getDescription());

//		availability.setPrimaryText(UIUtils.formatAmount(account.getAvailability()));
//		bankAccount.setPrimaryText(account.getAccount());
//		bankAccount.setSecondaryText(account.getBank());
//		updated.setPrimaryText(UIUtils.formatDate(account.getUpdated()));

	}

	private AppBar initAppBar() {
		AppBar appBar = MainLayout.get().getAppBar();
		appBar.setNaviMode(AppBar.NaviMode.CONTEXTUAL);
		appBar.getContextIcon().addClickListener(e -> UI.getCurrent().navigate(PpdbListView.class));
		appBar.setTitle(currentKurikulum.getFmatPelBean().getDescription());
		return appBar;
	}

	protected void goToParentView_ToRefresh(){
		UI.getCurrent().navigate(PpdbListView.class);
	}


	protected Component createContent() {
		/**
		 * Kalau Masih kosong maka dia akan panggil sebelumnya
		 */
		model.itemHeader = new FtKrs();
		try {
			model.itemHeader = model.mapKrsCurrentUser.values().stream()
					.filter(x -> x.getFkurikulumBean().equals(currentKurikulum)).findAny().get();
		}catch (Exception ex){}
//		System.out.println("KRS AFTER SELECT: " + model.itemHeader.getId() + " >> " + model.itemHeader.isStatSelected());

		FlexBoxLayout content = new FlexBoxLayout(
				createLogoSection(), createRecentTransactionsHeader(), createRecentTransactionsList(),
				createRecentTransactionsHeader2(), createRecentTransactionsList2()
		);
		content.setFlexDirection(FlexLayout.FlexDirection.COLUMN);
		content.setMargin(Horizontal.AUTO, Vertical.RESPONSIVE_L);
		content.setMaxWidth("840px");

		//Khusus menengahkan tampilan
		content.setAlignItems(FlexComponent.Alignment.CENTER);

		return content;
	}

	private FlexBoxLayout createLogoSection() {
		Image image = new Image(DummyData.getImageSource(currentKurikulum.getFmatPelBean().getLogoIndex()),"Company Logo");
		image.addClassName(LumoStyles.Margin.Horizontal.L);
		UIUtils.setBorderRadius(BorderRadius._50, image);
		image.setHeight("200px");
		image.setWidth("200px");

		listItemMatPel = new ListItem(UIUtils.createTertiaryIcon(VaadinIcon.PLUS_CIRCLE), "", "Mata Pelajaran");
		listItemMatPel.getPrimary().addClassName(LumoStyles.Heading.H2);
		listItemMatPel.setDividerVisible(true);
		listItemMatPel.setId("matpel");
		listItemMatPel.setReverse(true);

		listItemBiaya = new ListItem(UIUtils.createTertiaryIcon(VaadinIcon.USER_CHECK), "", "Periode");
		listItemBiaya.setId("biayaId");
		listItemBiaya.setReverse(true);
		listItemBiaya.setDividerVisible(true);

		listItemPeriode = new ListItem(UIUtils.createTertiaryIcon(VaadinIcon.USER_CHECK), "", "Periode");
		listItemPeriode.setId("periode");
		listItemPeriode.setReverse(true);


		Image imageCompany = new Image();
		try {
			imageCompany = CommonFileFactory.generateImage(AppPublicService.FILE_PATH +
					currentKurikulum.getFdivisionBean().getFcompanyBean().getLogoImage());
		}catch (Exception ex){
			ex.printStackTrace();
		}
		listItemCompany = new ListItem(imageCompany, "", "Sekolah/Ponpes");
		listItemCompany.setId("Sekolah/Ponpes");
		listItemCompany.setReverse(true);

		listItemBiaya= new ListItem(UIUtils.createTertiaryIcon(VaadinIcon.MONEY), "", "Biaya");
		listItemBiaya.setId("BiayaId");
		listItemBiaya.setReverse(true);

		FlexBoxLayout listItems = new FlexBoxLayout(listItemMatPel, listItemPeriode, listItemBiaya, listItemCompany);
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

	protected Button btnCalonSiswaBaru = new Button("DAFTAR SISWA/SISWI BARU");
	private Component createRecentTransactionsHeader() {

		Label title = UIUtils.createH3Label("Pendaftaran Calon Siswa/Siswi Baru");

		FlexBoxLayout header = new FlexBoxLayout(title);
		header.setAlignItems(FlexComponent.Alignment.CENTER);
		header.setMargin(Bottom.M, Horizontal.RESPONSIVE_L, Top.L);

		return header;
	}

	private Component createRecentTransactionsList() {

		Div items = new Div();
		items.addClassNames(BoxShadowBorders.BOTTOM, LumoStyles.Padding.Bottom.L);

		Label itemLabel1 = new Label();
		UIUtils.setTextColor(TextColor.PRIMARY, itemLabel1);

		btnCalonSiswaBaru = UIUtils.createSuccessButton("Daftar Disini", VaadinIcon.PENCIL);
		btnCalonSiswaBaru.addThemeVariants(ButtonVariant.LUMO_LARGE, ButtonVariant.LUMO_PRIMARY);
		btnCalonSiswaBaru.setMinWidth("200px");
		items.add(btnCalonSiswaBaru);

		btnCalonSiswaBaru.addClickListener(event -> UI.getCurrent().navigate(PpdbOnlineView.class, currentKurikulum.getId()));

		return items;
	}


	protected Button btnSudahMempunyaiAkun = new Button("Login");
	private Component createRecentTransactionsHeader2() {

		Label title = UIUtils.createH3Label("Atau Jika Sudah Mempunyai Akun");

		FlexBoxLayout header = new FlexBoxLayout(title);
		header.setAlignItems(FlexComponent.Alignment.CENTER);
		header.setMargin(Bottom.M, Horizontal.RESPONSIVE_L, Top.L);

		return header;
	}

	private Component createRecentTransactionsList2() {

		Div items = new Div();
		items.addClassNames(BoxShadowBorders.BOTTOM, LumoStyles.Padding.Bottom.L);

		Label itemLabel1 = new Label();
		UIUtils.setTextColor(TextColor.PRIMARY, itemLabel1);


		btnSudahMempunyaiAkun = UIUtils.createButton("Login", VaadinIcon.USER);
		btnSudahMempunyaiAkun.addThemeVariants(ButtonVariant.LUMO_LARGE, ButtonVariant.LUMO_PRIMARY);
		btnSudahMempunyaiAkun.setMinWidth("200px");
		items.add(btnSudahMempunyaiAkun);

		btnSudahMempunyaiAkun.addClickListener(event -> UI.getCurrent().navigate(KrsDetailView.class, currentKurikulum.getId()));

		return items;
	}

}
