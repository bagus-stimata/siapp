package com.desgreen.education.siapp.ui.views.transaksi_krs_validasi;

import com.desgreen.education.siapp.AppPublicService;
import com.desgreen.education.siapp.backend.model.*;
import com.desgreen.education.siapp.security_config.AuthUserDetailsService;
import com.desgreen.education.siapp.security_model.Role;
import com.desgreen.education.siapp.ui.MainLayout;
import com.desgreen.education.siapp.ui.components.FlexBoxLayout;
import com.desgreen.education.siapp.ui.components.Initials;
import com.desgreen.education.siapp.ui.components.ListItem;
import com.desgreen.education.siapp.ui.components.detailsdrawer.DetailsDrawer;
import com.desgreen.education.siapp.ui.components.detailsdrawer.DetailsDrawerFooter;
import com.desgreen.education.siapp.ui.components.detailsdrawer.DetailsDrawerHeader;
import com.desgreen.education.siapp.ui.components.navigation.bar.AppBar;
import com.desgreen.education.siapp.ui.layout.size.Horizontal;
import com.desgreen.education.siapp.ui.layout.size.Right;
import com.desgreen.education.siapp.ui.layout.size.Top;
import com.desgreen.education.siapp.ui.layout.size.Vertical;
import com.desgreen.education.siapp.ui.util.TextColor;
import com.desgreen.education.siapp.ui.util.UIUtils;
import com.desgreen.education.siapp.ui.util.css.BoxSizing;
import com.desgreen.education.siapp.ui.views.SplitViewFrame;
import com.desgreen.education.siapp.ui.views.transaksi_krs_validasi_detail.KrsValidasiDetailView;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;

import javax.annotation.PostConstruct;
import java.util.Locale;
import java.util.Objects;


@Secured({Role.ADMIN, Role.MNU_VALIDASI_KRS})
@UIScope
@SpringComponent
@Route(value = "KrsValidasiView", layout = MainLayout.class)
@PageTitle("KrsValidasiView")
public class KrsValidasiView extends SplitViewFrame {

	protected KrsValidasiModel model;
	protected KrsValidasiController controller;
	protected KrsValidasiListener listener;

	@Autowired
	private AppPublicService appPublicService;
	@Autowired
	public AuthUserDetailsService authUserDetailsService;

	protected Grid<FtKrs> grid;
	protected ListDataProvider<FtKrs> dataProvider;

	protected DetailsDrawer detailsDrawer;
	protected DetailsDrawerHeader detailsDrawerHeader;

	private AppBar appBar;
	private Button btnReloadFromDB;
	private Button btnSearchForm;


	protected Binder<FKurikulum> binder = new BeanValidationBinder<>(FKurikulum.class);
	protected FtKrs currentDomain;



	public KrsValidasiView() {
		super();
		/**
		 * Init Dummy
		 */
	}
	@PostConstruct
	private void init(){
		model = new KrsValidasiModel(authUserDetailsService, appPublicService);
		controller = new KrsValidasiController(model, this);
//		model = controller.model;

		listener = controller;

//		initAppBar();
//		setViewContent(createContent());
//		setViewDetails(createDetailsDrawer());
//		filter();


//		detailsDrawer.setContent(createDetails());

	}


	@Override
	protected void onAttach(AttachEvent attachEvent) {
		super.onAttach(attachEvent);
		initAppBar();

		model.reloadListHeader();
		setViewContent(createContent());
		setViewDetails(createDetailsDrawer());



		filter();
		binder.readBean(new FKurikulum());

	}

	private void initAppBar() {
		appBar = MainLayout.get().getAppBar();
		/**
		 * TAB GRID
		 */

		for (FKurikulum kurikulumBean : model.mapKurikulumExist.values()) {
			int periodeYear = kurikulumBean.getFperiodeBean().getPeriodeFrom().getYear();
			String tabLabel = kurikulumBean.getFmatPelBean().getDescription() + " (" +  periodeYear +")";
			appBar.addTab(tabLabel.toUpperCase()).setId(String.valueOf(kurikulumBean.getId()));

		}//endfor
		appBar.addTabSelectionListener(e -> {
			filter();
			detailsDrawer.hide();
		});
		appBar.centerTabs();


		btnSearchForm = appBar.addActionItem(VaadinIcon.SEARCH);
		btnReloadFromDB = appBar.addActionItem(VaadinIcon.REFRESH);

		btnReloadFromDB.addClickListener(e -> listener.aksiBtnReloadFromDb());
		btnSearchForm.addClickListener(e -> appBar.searchModeOn());

		appBar.addSearchListener( e -> listener.valueChangeListenerSearch(e));

		/**
		 * SAVE DAN CANCEL
		 * berada pada createDetailsDrawer()
		 */
	}
	private Component createContent() {
		FlexBoxLayout content = new FlexBoxLayout(createGrid());
		content.setBoxSizing(BoxSizing.BORDER_BOX);
		content.setHeightFull();
		content.setPadding(Horizontal.RESPONSIVE_X, Top.RESPONSIVE_X);
		return content;
	}

	private Grid createGrid() {
		grid = new Grid<>();
		grid.addSelectionListener(event -> event.getFirstSelectedItem()
				.ifPresent(this::viewDetails));

		/**
		 * CARA YANG SALAH: YANG BENAR ADA PADA SISWA
		 */
		dataProvider = DataProvider.ofCollection( model.mapHeader.values());


		grid.setDataProvider(dataProvider);
		grid.setHeightFull();

		grid.addColumn(FtKrs::getId)
				.setAutoWidth(true)
				.setFlexGrow(0)
				.setFrozen(true)
				.setHeader("ID")
				.setSortable(true);
		grid.addColumn(new ComponentRenderer<>(this::createDetilInfo))
				.setAutoWidth(true)
				.setFlexGrow(0)
				.setHeader("Nama")
				.setComparator((o1, o2) -> o1.getFsiswaBean().getFullName().compareTo(o2.getFsiswaBean().getFullName()))
				.setSortable(true);
		grid.addColumn(new ComponentRenderer<>(this::createStatus))
				.setAutoWidth(true)
				.setFlexGrow(0)
				.setHeader("Stat/Gendr")
				.setComparator((o1, o2) -> Boolean.compare(o1.getFsiswaBean().isSex(), o2.getFsiswaBean().isSex()) )
				.setSortable(true)
				.setTextAlign(ColumnTextAlign.CENTER);
		grid.addColumn(new ComponentRenderer<>(this::createStatusPesetujuan))
				.setAutoWidth(true)
				.setFlexGrow(0)
				.setHeader("STATUS")
				.setTextAlign(ColumnTextAlign.CENTER);
		grid.addColumn(new ComponentRenderer<>(this::createCityInfo))
				.setAutoWidth(true)
				.setFlexGrow(0)
				.setHeader("Kota/Telp")
				.setComparator((o1, o2) -> o1.getFsiswaBean().getCity().compareTo(o2.getFsiswaBean().getCity()))
				.setSortable(true);
		grid.addColumn(new ComponentRenderer<>(this::createAddreses))
				.setAutoWidth(true)
//				.setFlexGrow(0)
				.setHeader("Alamat")
				.setTextAlign(ColumnTextAlign.END);

		return grid;
	}

	private void viewDetails(FtKrs ftKrs) {
		try {
			UI.getCurrent().navigate(KrsValidasiDetailView.class, ftKrs.getId());
		}catch (Exception ex){}
	}

	private Component createDetilInfo(FtKrs ftKrs) {
		ListItem item = new ListItem(
				new Initials(ftKrs.getFsiswaBean().getInitials()), ftKrs.getFsiswaBean().getFullName(),
				"" );
		item.setPadding(Vertical.XS);
		item.setSpacing(Right.S);
//		item.setPrefix(new I, "Logo"));
		return item;
	}
	private Component createCityInfo(FtKrs ftKrs) {
		String telp = "";
		if (! ftKrs.getFsiswaBean().getPhone().equals("")) telp = "Telp. " + ftKrs.getFsiswaBean().getPhone();

		ListItem item = new ListItem(ftKrs.getFsiswaBean().getCity(),
				telp  );
		item.setPadding(Vertical.XS);
		item.setSpacing(Right.S);
		return item;
	}

	private Component createStatusPesetujuan(FtKrs ftKrs) {
		Label labelSatus = new Label();
		if (ftKrs.getEnumStatApproval().equals(EnumStatApproval.APPROVE)) {
			labelSatus = UIUtils.createBoldLabel("DISETUJUI");
			UIUtils.setTextColor(TextColor.SUCCESS, labelSatus);
		}else if (ftKrs.getEnumStatApproval().equals(EnumStatApproval.REJECTED)) {
			labelSatus = UIUtils.createBoldLabel("TIDAK DISETUJUI");
			UIUtils.setTextColor(TextColor.ERROR, labelSatus);
		}else if (ftKrs.isStatSelected()==true && ftKrs.isStatCancel()==false) {
			labelSatus = UIUtils.createBoldLabel("MENDAFTAR");
			UIUtils.setTextColor(TextColor.PRIMARY, labelSatus);
		}else if (ftKrs.isStatSelected()==true && ftKrs.isStatCancel()==true) {
			labelSatus = UIUtils.createBoldLabel("DIBATALKAN");
			UIUtils.setTextColor(TextColor.ERROR, labelSatus);
		}
		return labelSatus;
	}

	private Component createStatus(FtKrs ftKrs) {
		final FSiswa fSiswa = ftKrs.getFsiswaBean();
		Icon icon;
		if (fSiswa.isSex()) {
			if (fSiswa.isMenikah()) {
				icon = UIUtils.createPrimaryIcon(VaadinIcon.FAMILY);

			}else {
				icon = UIUtils.createPrimaryIcon(VaadinIcon.MALE);
			}
		}else {
			if (fSiswa.isMenikah()) {
				icon = UIUtils.createSuccessIcon(VaadinIcon.FAMILY);
			}else {
				icon = UIUtils.createSuccessIcon(VaadinIcon.FEMALE);
			}
		}
		return icon;
	}
	private Component createAddreses(FtKrs ftKrs) {
		final FSiswa fSiswa = ftKrs.getFsiswaBean();
		String address2 = fSiswa.getAddress2();
		if (! fSiswa.getPhone().equals("")) {
			address2 += ", Telp. " + fSiswa.getPhone();
		}
		ListItem item = new ListItem( fSiswa.getAddress1(),
				address2);
		item.setPadding(Vertical.XS);
		item.setSpacing(Right.M);

		return item;
	}


	private Component createDivision(FtKrs fKurikulum) {
		return UIUtils.createBoldLabel(fKurikulum.getFdivisionBean()!=null? fKurikulum.getFdivisionBean().getDescription(): "");
	}

	private Component createComboDivisionInfo(FDivision domain) {
		ListItem item = new ListItem( domain.getDescription(),
				domain.getFcompanyBean().getDescription());
		item.setPadding(Vertical.XS);
		item.setSpacing(Right.M);

		return item;
	}

	private Component createComboPeriodeInfo(FPeriode domain) {
		ListItem item = new ListItem( new Initials(domain.getKode1()) , domain.getDescription(),
				UIUtils.formatDate(domain.getPeriodeFrom()) + " - " + UIUtils.formatDate(domain.getPeriodeTo()) );
		item.setPadding(Vertical.XS);
		item.setSpacing(Right.S);

		return item;
	}
	private Component createComboMatpelInfo(FMatPel domain) {
		ListItem item = new ListItem( new Initials(domain.getKode1()) , domain.getDescription(),
				domain.getNotes() );
		item.setPadding(Vertical.XS);
		item.setSpacing(Right.M);

		return item;
	}

//	private Component createKuotaMale(FtKrs ftKrs) {
//		ListItem item = new ListItem(String.valueOf(ftKrs.getKuotaMale()));
//		item.setPadding(Vertical.XS);
//		item.setSpacing(Right.S);
//
//		Icon icon = UIUtils.createPrimaryIcon(VaadinIcon.MALE);
//		item.setPrefix(icon);
//
//		return item;
//	}
//	private Component createKuotaFemale(FKurikulum fKurikulum) {
//		ListItem item = new ListItem(String.valueOf(fKurikulum.getKuotaFemale()));
//		item.setPadding(Vertical.XS);
//		item.setSpacing(Right.S);
//
//		Icon icon = UIUtils.createSuccessIcon(VaadinIcon.FEMALE);
//		item.setPrefix(icon);
//		return item;
//	}


	DetailsDrawerFooter footer = new DetailsDrawerFooter();
	private DetailsDrawer createDetailsDrawer() {
		detailsDrawer = new DetailsDrawer(DetailsDrawer.Position.RIGHT);

		// Header
		detailsDrawerHeader = new DetailsDrawerHeader("");
		detailsDrawerHeader.addCloseListener(buttonClickEvent -> detailsDrawer.hide());
		detailsDrawer.setHeader(detailsDrawerHeader);

		// Footer
		footer.addSaveListener(e -> {
			listener.aksiBtnSaveForm();
//			UIUtils.showNotification("Changes saved.");
		});
		footer.addCancelListener(e -> {
			detailsDrawer.hide();
			listener.aksiBtnCancelForm();
		});
		detailsDrawer.setFooter(footer);

		return detailsDrawer;
	}


	private void filter() {
		Tab selectedTab = MainLayout.get().getAppBar().getSelectedTab();
		if (selectedTab != null) {
//			System.out.println("Masuk filter: " + selectedTab.getLabel() + " >> " + selectedTab.getId().toString());
			dataProvider.setFilterByValue(p -> String.valueOf(p.getFkurikulumBean().getId()), selectedTab.getId().get());
		}
	}


	String filterText = "";
	public void setFilter(String filterText) {
		Objects.requireNonNull(filterText, "Filter teks tidak boleh kosong");
		if (Objects.equals(this.filterText, filterText.trim())) {
			return;
		}
		this.filterText = filterText.trim().toLowerCase(Locale.ENGLISH);

		dataProvider.setFilter(domain -> passesFilter(domain.getFsiswaBean()!=null?(domain.getFsiswaBean().getFullName()):"", this.filterText)
				|| passesFilter(domain.getFsiswaBean()!=null?(domain.getFsiswaBean().isSex()? "Laki": "Perempu"):"", this.filterText)
//				|| passesFilter(domain.getAddress1(), this.filterText)
				|| passesFilter(domain.getFsiswaBean()!=null?(domain.getFsiswaBean().getCity()):"", this.filterText));

//		Tab selectedTab = MainLayout.get().getAppBar().getSelectedTab();
//		if (selectedTab != null) {
////			System.out.println("Masuk filter: " + selectedTab.getLabel() + " >> " + selectedTab.getId().toString());
//			dataProvider.addFilterByValue(p -> String.valueOf(p.getFkurikulumBean().getId()), selectedTab.getId().get());
//		}

	}
	private boolean passesFilter(Object object, String filterText) {
		return object != null && object.toString().toLowerCase(Locale.ENGLISH)
				.contains(filterText);
	}

}
