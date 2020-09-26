package com.desgreen.education.siapp.ui.views.transaksi_krs_validasi;

import ar.com.fdvs.dj.domain.AutoText;
import ar.com.fdvs.dj.domain.Style;
import ar.com.fdvs.dj.domain.builders.ColumnBuilder;
import ar.com.fdvs.dj.domain.builders.GroupBuilder;
import ar.com.fdvs.dj.domain.builders.StyleBuilder;
import ar.com.fdvs.dj.domain.constants.Font;
import ar.com.fdvs.dj.domain.entities.columns.AbstractColumn;
import ar.com.fdvs.dj.domain.entities.columns.PropertyColumn;
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
import com.desgreen.education.siapp.ui.util.LumoStyles;
import com.desgreen.education.siapp.ui.util.TextColor;
import com.desgreen.education.siapp.ui.util.UIUtils;
import com.desgreen.education.siapp.ui.util.css.BoxSizing;
import com.desgreen.education.siapp.ui.utils.common.CommonDateFormat;
import com.desgreen.education.siapp.ui.views.SplitViewFrame;
import com.desgreen.education.siapp.ui.views.transaksi_krs_validasi_detail.KrsValidasiDetailView;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.function.SerializableSupplier;
import com.vaadin.flow.router.*;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.vaadin.reports.PrintPreviewReport;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;


@Secured({Role.ADMIN, Role.MNU_VALIDASI_KRS})
@UIScope
@SpringComponent
@Route(value = "KrsValidasiView", layout = MainLayout.class)
@PageTitle("KrsValidasiView")
public class KrsValidasiView extends SplitViewFrame  implements HasUrlParameter<Long> {

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
	private Button btnPrint;
	private Button btnPrintReportUi;
	private Button btnExcel;
	private Anchor anchor_Print;
	private Anchor anchor_Excel;

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
	public void setParameter(BeforeEvent beforeEvent, @OptionalParameter  Long paramId) {
		if (paramId !=null) {
			FtKrs ftKrs = model.ftKrsJPARepository.findById(paramId).orElse(new FtKrs());
//			model.reloadListHeader(); //Jika dari ValidasiDetail >> maka harus Refresh
			if (ftKrs.getId()>0) {
				selectedTabId = String.valueOf(ftKrs.getFkurikulumBean().getId());
				model.mapHeader.put(ftKrs.getId(), ftKrs);
			}
		}
	}

	@Override
	protected void onAttach(AttachEvent attachEvent) {
		super.onAttach(attachEvent);

		/**
		 * Alternatif bisa mengunakan ini
		 * Artinya akan selalu Reload setiap menuju kesini
		 */
		model.reloadListHeader();

		initAppBar();
		/**
		 * Addition Properties of Tab Bar
		 * TAB GRID
		 * Kelemahan dari Tab Ini adalah selalu harus dipanggil ketika di attach:
		 * Jadi harus ribet
		 */
		for (FKurikulum kurikulumBean : model.mapKurikulumExist.values()) {
			long filledQuotaMale = kurikulumBean.getFtKrsSet().stream().filter(x-> x.getFsiswaBean().isSex()==true && x.getEnumStatApproval().equals(EnumStatApproval.APPROVE)).count();
			long filledQoutaFemale = kurikulumBean.getFtKrsSet().stream().filter(x-> x.getFsiswaBean().isSex()==false && x.getEnumStatApproval().equals(EnumStatApproval.APPROVE)).count();
			int periodeYear = kurikulumBean.getFperiodeBean().getPeriodeFrom().getYear();
			String tabLabel = kurikulumBean.getFmatPelBean().getDescription() + " (" + periodeYear + ")".toUpperCase() + " " +
					String.valueOf(filledQuotaMale) +"/" + String.valueOf(filledQoutaFemale);
			appBar.addTab(tabLabel).setId(String.valueOf(kurikulumBean.getId())); //Selected Tab adalah kurikulumId

			//Select yang pertama kali jika kosong
			if (selectedTabId.equals("")) selectedTabId = String.valueOf(kurikulumBean.getId());

		}//endfor
		appBar.addTabSelectionListener(e -> {
			filter();
			detailsDrawer.hide();
			/**
			 * Oleh karena kelemahan dari metode tab ini
			 */
			selectedTabId = e.getSelectedTab().getId().get();
		});
		appBar.centerTabs();

		try {
			if (! selectedTabId.equals("")) {
				Tab tab1 = appBar.getTab(selectedTabId);
				appBar.setSelectedTab(tab1);
			}
		}catch (Exception ex){}



		setViewContent(createContent());
		setViewDetails(createDetailsDrawer());



		filter();
		binder.readBean(new FKurikulum());


	}

	String selectedTabId = "";
	private void initAppBar() {
		appBar = MainLayout.get().getAppBar();
		appBar.searchModeOff();

		btnSearchForm = appBar.addActionItem(VaadinIcon.SEARCH);
		btnReloadFromDB = appBar.addActionItem(VaadinIcon.REFRESH);

		btnReloadFromDB.addClickListener(e -> listener.aksiBtnReloadFromDb());
		btnSearchForm.addClickListener(e -> appBar.searchModeOn());

		appBar.addSearchListener( e -> listener.valueChangeListenerSearch(e));

		/**
		 * BUAT BUTTON PRINT MANUAL
		 */
//		btnPrint = appBar.addActionItem(VaadinIcon.PRINT);
//		btnPrint = new Button("Print");
//		btnPrint.addClickListener(e -> listener.aksiBtnPrint());
		btnPrint = UIUtils.createButton(VaadinIcon.PRINT, ButtonVariant.LUMO_SMALL,
				ButtonVariant.LUMO_TERTIARY);
		btnExcel = UIUtils.createButton(VaadinIcon.DOWNLOAD, ButtonVariant.LUMO_SMALL,
				ButtonVariant.LUMO_TERTIARY);

		btnPrintReportUi = UIUtils.createButton(VaadinIcon.PRINT, ButtonVariant.LUMO_SMALL,
				ButtonVariant.LUMO_TERTIARY);

//		Icon iconExcel = IcoMoon.FILE_EXCEL.create();
//		btnExcel = new Button(iconExcel);
//		btnExcel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

		appBar.addActionItem(btnPrintReportUi);
		btnPrintReportUi.addClickListener( e -> {
			setViewContent(createContentReportUi());
		});

		try {
			anchor_Print = new Anchor(controller.getStreamResource_JasperReport("LapTemplate1Ds.jrxml", "file.pdf"), null);
			anchor_Print.setTarget("_blank");
			anchor_Print.add(btnPrint);
//			appBar.addActionItem(anchor_Print);

		}catch (Exception ex){}

		try {
			anchor_Excel = new Anchor(controller.getStreamResource_Excel(), null);
			anchor_Excel.add(btnExcel);
			appBar.addActionItem(anchor_Excel);
		}catch (Exception ex){}

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
				.setComparator((o1, o2) -> o1.getEnumStatApproval().getStringId().compareTo(o2.getEnumStatApproval().getStringId()))
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

	private Component createContentReportUi(){
		FlexBoxLayout content = new FlexBoxLayout(createReportUi());
		content.setBoxSizing(BoxSizing.BORDER_BOX);
		content.setHeightFull();
		content.setPadding(Horizontal.RESPONSIVE_X, Top.RESPONSIVE_X);
		return content;
	}

	List<ZLapTemplate2> listLapTemplate = new ArrayList<>();
	HorizontalLayout exportButtons = new HorizontalLayout();
	Button btnBack;
	PrintPreviewReport<ZLapTemplate2> report = new PrintPreviewReport<>(ZLapTemplate2.class, "string1", "string2", "string3", "string4", "string5", "string6");
	private Div createReportUi(){
		listLapTemplate.clear();
		for (FtKrs c : dataProvider.getItems().stream().filter(ftKrs -> String.valueOf(ftKrs.getFkurikulumBean().getId()).equals(selectedTabId))
				.collect(Collectors.toList())) {
			ZLapTemplate2 domain = new ZLapTemplate2();
			domain.setId(c.getId());
			domain.setString1(c.getFsiswaBean().getFullName());
			domain.setString2(c.getFsiswaBean().isSex() ? "Laki-Laki" : "Perempuan");
			domain.setString3(c.getFsiswaBean().getAddress1() + " " + c.getFsiswaBean().getAddress2() + " " + c.getFsiswaBean().getAddress3());
			domain.setString4(c.getFsiswaBean().getCity());
			domain.setString5(c.getFsiswaBean().getPhone());

			listLapTemplate.add(domain);
		}


		Div content = new Div();
		Style headerStyle = new StyleBuilder(true).setFont(Font.ARIAL_MEDIUM).build();
		Style groupStyle = new StyleBuilder(true).setFont(Font.ARIAL_MEDIUM_BOLD).build();

		AbstractColumn columnNama;

		PrintPreviewReport<ZLapTemplate2> report = new PrintPreviewReport<>();
		report.getReportBuilder()
				.setMargins(20, 20, 40, 40)
				.setTitle(appBar.getSelectedTab().getLabel())
				.addAutoText(model.userActive.getFdivisionBean().getFcompanyBean().getDescription(), AutoText.POSITION_HEADER, AutoText.ALIGMENT_LEFT, 200, headerStyle)
				.addAutoText(LocalDateTime.now().toString(), AutoText.POSITION_HEADER, AutoText.ALIGNMENT_RIGHT, 200, headerStyle)
				.addAutoText(AutoText.AUTOTEXT_PAGE_X_OF_Y, AutoText.POSITION_HEADER, AutoText.ALIGNMENT_RIGHT, 200, 10, headerStyle)
//                .setPrintBackgroundOnOddRows(true) //Selang-Seling Row

				.addColumn(columnNama = ColumnBuilder.getNew()
						.setColumnProperty("string1", String.class)
						.setTitle("Nama")
						.setWidth(130).setFixedWidth(true)
//						.setStyle(groupStyle)
						.build())
//				.addGroup(new GroupBuilder()
//						.setCriteriaColumn((PropertyColumn) columnNama)
//						.build())

				.addColumn(ColumnBuilder.getNew()
						.setColumnProperty("string2", String.class)
						.setTitle("Jkel")
						.build())
				.addColumn(ColumnBuilder.getNew()
						.setColumnProperty("string3", String.class)
						.setTitle("Alamat")
						.build())
//				.addColumn(ColumnBuilder.getNew()
//						.setColumnProperty("startTime", LocalDateTime.class)
//						.setTextFormatter(DateTimeFormatter.ISO_LOCAL_TIME.toFormat())
//						.setTitle("Start time")
//						.build())
//				.addColumn(ColumnBuilder.getNew()
//						.setColumnProperty("duration", Integer.class)
//						.setTitle("Duration (seconds)")
//						.build())
				.addColumn(ColumnBuilder.getNew()
						.setColumnProperty("string4", String.class)
						.setTitle("Kota")
						.build())
				.addColumn(ColumnBuilder.getNew()
						.setColumnProperty("string5", String.class)
						.setTitle("Telp").build());

		SerializableSupplier<List<? extends ZLapTemplate2>> itemsSupplier = () -> listLapTemplate;
		report.setItems(itemsSupplier.get());

		exportButtons.removeAll();
		for (PrintPreviewReport.Format format : PrintPreviewReport.Format.values()) {
			Anchor anchor = new Anchor(report.getStreamResource("call-report." + format.name().toLowerCase(), itemsSupplier, format),
					format.name());
			anchor.getElement().setAttribute("download", true);

			exportButtons.add(anchor);
		}

		btnBack = UIUtils.createTertiaryInlineButton(VaadinIcon.CLOSE);
		btnBack.addClickListener(e -> {
			setViewContent(createContent());
		});

		Icon icon = new Icon(VaadinIcon.CLOSE);
		icon.setColor("red");
		btnBack.setIcon(icon);
		btnBack.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.MATERIAL_OUTLINED);
		exportButtons.addComponentAsFirst(btnBack);
		content.add(exportButtons, report);
		return  content;
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

	protected void filter() {
		Tab selectedTab = MainLayout.get().getAppBar().getSelectedTab();
		if (selectedTab != null) {
			dataProvider.setFilterByValue(p -> p.getFkurikulumBean()!=null?String.valueOf(p.getFkurikulumBean().getId()):"", selectedTab.getId().get());
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
				|| passesFilter(domain.getFsiswaBean()!=null?(domain.getFsiswaBean().getCity()):"", this.filterText));

		Tab selectedTab = MainLayout.get().getAppBar().getSelectedTab();
		if (selectedTab != null && selectedTab.getId().isPresent()) {
			try {
				dataProvider.addFilterByValue(p -> p.getFkurikulumBean() != null ? String.valueOf(p.getFkurikulumBean().getId()) : "", selectedTab.getId().get());
			}catch (Exception ex){}
		}

	}

	private boolean passesFilter(Object object, String filterText) {
		return object != null && object.toString().toLowerCase(Locale.ENGLISH)
				.contains(filterText);
	}

}
