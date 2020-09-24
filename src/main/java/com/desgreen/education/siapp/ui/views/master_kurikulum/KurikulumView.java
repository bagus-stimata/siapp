package com.desgreen.education.siapp.ui.views.master_kurikulum;

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
import com.desgreen.education.siapp.ui.views.SplitViewFrame;
import com.vaadin.componentfactory.ToggleButton;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.StringToDoubleConverter;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;

import javax.annotation.PostConstruct;
import java.util.Locale;
import java.util.Objects;


@Secured({Role.ADMIN, Role.MNU_KURIKULUM})
@UIScope
@SpringComponent
@Route(value = "KurikulumView", layout = MainLayout.class)
@PageTitle("KurikulumView")
public class KurikulumView extends SplitViewFrame {

	protected KurikulumModel model;
	protected KurikulumController controller;
	protected KurikulumListener listener;

	@Autowired
	private AppPublicService appPublicService;
	@Autowired
	public AuthUserDetailsService authUserDetailsService;

	protected Grid<FKurikulum> grid;
	protected ListDataProvider<FKurikulum> dataProvider;

	protected DetailsDrawer detailsDrawer;
	protected DetailsDrawerHeader detailsDrawerHeader;

	// Tabs in the drawer
	Tab tabInfoUmum = new Tab("Info");
	Tab tabBiaya = new Tab("Biaya");

	Tabs tabs = new Tabs(tabInfoUmum, tabBiaya);

	private AppBar appBar;
	private Button btnExtractExcel;
	private Button btnReloadFromDB;
	private Button btnSearchForm;
	private Button btnNewForm;
	private Button btnDeleteDeleteForm;


	protected Binder<FKurikulum> binder = new BeanValidationBinder<>(FKurikulum.class);
//	protected FKurikulum currentDomain;



	public KurikulumView() {
		super();
		/**
		 * Init Dummy
		 */
	}
	@PostConstruct
	private void init(){
		model = new KurikulumModel(authUserDetailsService, appPublicService);
		controller = new KurikulumController(model, this);
//		model = controller.model;

		listener = controller;

//		initAppBar();
//		setViewContent(createContent());
//		setViewDetails(createDetailsDrawer());
//		filter();

		/**
		 * Init Data
		 */

	}

	@Override
	protected void onAttach(AttachEvent attachEvent) {
		super.onAttach(attachEvent);
		initAppBar();
		setViewContent(createContent());
		setViewDetails(createDetailsDrawer());

		filter();
		binder.readBean(new FKurikulum());
		detailsDrawer.setContent(createDetails_InfoUmum());

	}


	private void initAppBar() {
		appBar = MainLayout.get().getAppBar();
		appBar.searchModeOff();

		btnExtractExcel = appBar.addActionItem(VaadinIcon.FILE);
		btnSearchForm = appBar.addActionItem(VaadinIcon.SEARCH);
		btnReloadFromDB = appBar.addActionItem(VaadinIcon.REFRESH);
		btnNewForm = appBar.addActionItem(VaadinIcon.PLUS);
		btnDeleteDeleteForm = appBar.addActionItem(VaadinIcon.TRASH);

		btnNewForm.addThemeVariants(ButtonVariant.MATERIAL_OUTLINED);
		btnDeleteDeleteForm.addThemeVariants(ButtonVariant.MATERIAL_OUTLINED, ButtonVariant.LUMO_ERROR);

		btnExtractExcel.addClickListener(e -> listener.aksiBtnExtractExcel());
		btnReloadFromDB.addClickListener(e -> listener.aksiBtnReloadFromDb());
		btnSearchForm.addClickListener(e -> appBar.searchModeOn());
		btnNewForm.addClickListener(e -> listener.aksiBtnNewForm());
		btnDeleteDeleteForm.addClickListener(e -> listener.aksiBtnDeleteForm());

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
				.ifPresent(this::showDetails));


//###### FROM MODEL
//		dataProvider = DataProvider.ofCollection(DummyData.getFKurikulums());
//		dataProvider = DataProvider.ofCollection(new ArrayList<>());
		dataProvider = DataProvider.ofCollection( model.mapHeader.values() );
		
		grid.setDataProvider(dataProvider);
		grid.setHeightFull();

		grid.addColumn(FKurikulum::getId)
				.setAutoWidth(true)
				.setFlexGrow(0)
				.setFrozen(true)
				.setHeader("ID")
				.setSortable(true);
		grid.addColumn(new ComponentRenderer<>(this::createDetilInfo))
				.setAutoWidth(true)
				.setHeader("Name Kurikulum")
				.setComparator(FKurikulum::getDescription)
				.setSortable(true);
		grid.addColumn(new ComponentRenderer<>(this::createPeriode))
				.setAutoWidth(true)
				.setFlexGrow(0)
				.setHeader("Periode")
				.setTextAlign(ColumnTextAlign.START)
				.setComparator((o1, o2) -> o1.getFperiodeBean().getDescription().compareTo(o2.getFperiodeBean().getDescription()))
				.setSortable(true);
		grid.addColumn(new ComponentRenderer<>(this::createMatPel))
				.setAutoWidth(true)
				.setFlexGrow(0)
				.setHeader("Mata Pelajaran")
				.setTextAlign(ColumnTextAlign.START)
				.setComparator((o1, o2) -> o1.getFmatPelBean().getDescription().compareTo(o2.getFmatPelBean().getDescription()))
				.setSortable(true);
		grid.addColumn(new ComponentRenderer<>(this::createKuotaMale))
				.setAutoWidth(true)
				.setHeader("Kuota PA")
				.setTextAlign(ColumnTextAlign.CENTER)
				.setComparator(FKurikulum::getKuotaMale)
				.setSortable(true);
			grid.addColumn(new ComponentRenderer<>(this::createKuotaFemale))
				.setAutoWidth(true)
				.setHeader("Kuota PI")
				.setTextAlign(ColumnTextAlign.CENTER)
				.setComparator(FKurikulum::getKuotaFemale)
				.setSortable(true);
		grid.addColumn(new ComponentRenderer<>(this::createActive))
				.setAutoWidth(true)
				.setFlexGrow(0)
				.setHeader("Active")
				.setTextAlign(ColumnTextAlign.CENTER);
		grid.addColumn(new ComponentRenderer<>(this::createDivision))
				.setAutoWidth(true)
				.setFlexGrow(0)
				.setHeader("Division")
				.setTextAlign(ColumnTextAlign.END)
				.setSortable(true);
		// grid.addColumn(new ComponentRenderer<>(this::createAddreses))
		// 		.setAutoWidth(true)
		// 		.setFlexGrow(0)
		// 		.setHeader("Addresses")
		// 		.setTextAlign(ColumnTextAlign.END);

//		grid.addColumn(new ComponentRenderer<>(this::createDate)).setFlexGrow(0)
//				.setAutoWidth(true)
//				.setFlexGrow(0)
//				.setHeader("Last Report")
//				.setTextAlign(ColumnTextAlign.END);

		return grid;
	}

	private Component createDetilInfo(FKurikulum fKurikulum) {
		ListItem item = new ListItem(
				new Initials(fKurikulum.getKode1()), fKurikulum.getDescription(),
				"");
		item.setPadding(Vertical.XS);
		item.setSpacing(Right.M);
		return item;
	}
	
	private Component createActive(FKurikulum fKurikulum) {
		Icon icon;
		if (fKurikulum.isStatActive()) {
			icon = UIUtils.createPrimaryIcon(VaadinIcon.CHECK);
		}else {
			icon = UIUtils.createDisabledIcon(VaadinIcon.CLOSE);
		}
		return icon;
	}
	// private Component createPeriode(FKurikulum fKurikulum) {
	// 	return UIUtils.createBoldLabel(fKurikulum.getFperiodeBean()!=null? fKurikulum.getFperiodeBean().getDescription(): "");
	// }
	private Component createPeriode(FKurikulum fKurikulum) {
		String kode1 = "";
		String description = "";
		String notes = "";
		if (fKurikulum.getFperiodeBean() !=null) {
			kode1 = fKurikulum.getFperiodeBean().getKode1();
			description = fKurikulum.getFperiodeBean().getDescription();
			notes = UIUtils.formatDate(fKurikulum.getFperiodeBean().getPeriodeFrom()) + " S.D " +  UIUtils.formatDate(fKurikulum.getFperiodeBean().getPeriodeTo());
		}
		ListItem item = new ListItem(
				new Initials(kode1), description,
				notes);
		item.setPadding(Vertical.XS);
		item.setSpacing(Right.M);
		return item;
	}

	// private Component createMatPel(FKurikulum fKurikulum) {
	// 	return UIUtils.createBoldLabel(fKurikulum.getFmatPelBean()!=null? fKurikulum.getFmatPelBean().getDescription(): "");
	// }
	private Component createMatPel(FKurikulum fKurikulum) {
		String kode1 = "";
		String description = "";
		String notes = "";
		if (fKurikulum.getFmatPelBean() !=null) {
			kode1 = fKurikulum.getFmatPelBean().getKode1();
			description = fKurikulum.getFmatPelBean().getDescription();
		}
		ListItem item = new ListItem(
				new Initials(kode1), description,
				notes);
		item.setPadding(Vertical.XS);
		item.setSpacing(Right.M);
		return item;
	}

	private Component createDivision(FKurikulum fKurikulum) {
		return UIUtils.createBoldLabel(fKurikulum.getFdivisionBean()!=null? fKurikulum.getFdivisionBean().getDescription(): "");
	}

	private Component createComboDivisionInfo(FDivision domain) {
		ListItem item = new ListItem( domain.getDescription(),
				domain.getFcompanyBean().getDescription());
		item.setPadding(Vertical.XS);
		item.setSpacing(Right.M);

		return item;
	}
	private Component createComboKomponentBiaya(FKompBiaya domain) {
		ListItem item = new ListItem( domain.getDescription() + " #" + domain.getKode1(),
				domain.getFdivisionBean().getDescription());
		item.setPadding(Vertical.XS);
		item.setSpacing(Right.M);

		return item;
	}

	private Component createComboPeriodeInfo(FPeriode domain) {
		ListItem item = new ListItem( new Initials(domain.getKode1()) , domain.getDescription(),
				UIUtils.formatDate(domain.getPeriodeFrom()) + " - " + UIUtils.formatDate(domain.getPeriodeTo()) );
		item.setPadding(Vertical.XS);
		item.setSpacing(Right.M);

		return item;
	}
	private Component createComboMatpelInfo(FMatPel domain) {
		ListItem item = new ListItem( new Initials(domain.getKode1()) , domain.getDescription(),
				domain.getNotes() );
		item.setPadding(Vertical.XS);
		item.setSpacing(Right.M);

		return item;
	}

	private Label createKuotaMale(FKurikulum fKurikulum) {
		Label kuota = UIUtils.createBoldLabel(String.valueOf(fKurikulum.getKuotaMale()));
		// Label kuota = UIUtils.createAmountLabel(fKurikulum.getKuotaMale());
		UIUtils.setTextColor(TextColor.PRIMARY, kuota);
		return kuota;
	}
	private Component createKuotaFemale(FKurikulum fKurikulum) {
		Label kuota = UIUtils.createBoldLabel(String.valueOf(fKurikulum.getKuotaFemale()));
		// Label kuota = UIUtils.createAmountLabel(fKurikulum.getKuotaFemale());
		UIUtils.setTextColor(TextColor.SUCCESS, kuota);
		return kuota;
	}
	
	// private Component createKurikulumDateFrom(FKurikulum fKurikulum) {
	// 	return new Span(UIUtils.formatDate(fKurikulum.getKurikulumFrom()));
	// }
	// private Component createKurikulumDateTo(FKurikulum fKurikulum) {
	// 	return new Span(UIUtils.formatDate(fKurikulum.getKurikulumTo()));
	// }

	DetailsDrawerFooter footer = new DetailsDrawerFooter();
	private DetailsDrawer createDetailsDrawer() {
		detailsDrawer = new DetailsDrawer(DetailsDrawer.Position.RIGHT);
		tabs.addThemeVariants(TabsVariant.LUMO_EQUAL_WIDTH_TABS);
		tabs.addSelectedChangeListener(event -> {
			configureTabs();
		});

		// Header
		detailsDrawerHeader = new DetailsDrawerHeader("", tabs);
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

	private void configureTabs() {
		Tab selectedTab = tabs.getSelectedTab();
		if (selectedTab.equals(tabInfoUmum)) {
			detailsDrawer.setContent(createDetails_InfoUmum());
		} else if (selectedTab.equals(tabBiaya)) {
			detailsDrawer.setContent(createDetails_Biaya());
		}
	}

	protected void showDetails(FKurikulum fKurikulum) {
		if (fKurikulum == null) {
			fKurikulum = new FKurikulum();
		}
//		delete.setVisible(!product.isNewProduct());
		model.currentDomain = fKurikulum;
		try {
			binder.readBean(fKurikulum);
		}catch (Exception ex) {ex.printStackTrace();}
		bindField();

		btnDeleteDeleteForm.setVisible(! model.currentDomain.isNewDomain());

		detailsDrawerHeader.setTitle(fKurikulum.getDescription());
//		detailsDrawer.setContent(createDetails(fKurikulum));
		detailsDrawer.show();

	}

	protected void bindField(){
		/**
		 * Re Binding Datata
		 */
		binder.bindInstanceFields(this);
		binder.forField(kode1).asRequired().bind(FKurikulum::getKode1, FKurikulum::setKode1);
		binder.forField(description).asRequired().bind(FKurikulum::getDescription, FKurikulum::setDescription);
//				.withConverter(new StringToBooleanConverter("", "Active", "Non-Active"))
//				.bind(FKurikulum::isStatActive, FKurikulum::setStatActive);

		binder.forField(comboFPeriode)
				.asRequired()
				.bind(FKurikulum::getFperiodeBean, FKurikulum::setFperiodeBean);
		binder.forField(comboFMatpel)
				.asRequired()
				.bind(FKurikulum::getFmatPelBean, FKurikulum::setFmatPelBean);

		binder.forField(comboFDivision)
				.asRequired()
				.bind(FKurikulum::getFdivisionBean, FKurikulum::setFdivisionBean);
		binder.forField(toggleStatActive)
				.bind(FKurikulum::isStatActive, FKurikulum::setStatActive);


		binder.forField(fieldKuotaMale)
				.withConverter(new StringToIntegerConverter("Harus angka"))
				.bind(FKurikulum::getKuotaMale, FKurikulum::setKuotaMale);
		binder.forField(fieldKuotaFemale)
				.withConverter(new StringToIntegerConverter("Harus angka"))
				.bind(FKurikulum::getKuotaFemale, FKurikulum::setKuotaFemale);


		binder.forField(comboFKompBiaya)
				.asRequired()
				.bind(FKurikulum::getFkompBiayaBean, FKurikulum::setFkompBiayaBean);
		binder.forField(fieldAmountRp)
				.withConverter(new StringToDoubleConverter("Harus angka"))
				.bind(FKurikulum::getAmountBiaya, FKurikulum::setAmountBiaya);
		binder.forField(fieldNotesBiaya)
				.bind(FKurikulum::getNotesBiaya, FKurikulum::setNotesBiaya);

	}

	/**
	 * FORM COMPONENT
	 * dipanggil sekali saja
	 */
	private final TextField kode1 = new TextField();
	private final TextField description = new TextField();
	private final TextField fieldKuotaMale = new TextField();
	private final TextField fieldKuotaFemale = new TextField();
	private final DatePicker datePickerKurikulumFrom = new DatePicker();
	private final DatePicker datePickerKurikulumTo = new DatePicker();
	private final ComboBox<FDivision> comboFDivision = new ComboBox<FDivision>();
	private final ToggleButton toggleStatActive = new ToggleButton();
	private final TextArea notes = new TextArea();
	private final ComboBox<FPeriode> comboFPeriode = new ComboBox<FPeriode>();
	private final ComboBox<FMatPel> comboFMatpel = new ComboBox<FMatPel>();

	private final ComboBox<FKompBiaya> comboFKompBiaya = new ComboBox<FKompBiaya>();
	private final TextField fieldAmountRp = new TextField();
	private final TextArea fieldNotesBiaya = new TextArea();

	private FormLayout createDetails_InfoUmum() {
//		System.out.println(fKurikulum.getAddress1());


		/**
		 * Decorate the Form View
		 */
		kode1.setWidthFull();
		description.setWidthFull();
		notes.setWidthFull();

		notes.setMaxHeight("150px");

		toggleStatActive.setLabel("Active");
		toggleStatActive.addValueChangeListener(e -> {
			if (e.getValue()==true) {
				toggleStatActive.setLabel("Active");
			}else {
				toggleStatActive.setLabel("Non-active");
			}
		});

		comboFDivision.setItems(model.listFDivision);
		comboFDivision.setWidthFull();
		comboFDivision.setItemLabelGenerator(p -> p.getKode1() + "  " + p.getDescription());
		comboFDivision.setRenderer(new ComponentRenderer<>(this::createComboDivisionInfo ));

		comboFPeriode.setItems(model.listFPeriode);
		comboFPeriode.setItemLabelGenerator(p -> p.getKode1() + "  " + p.getDescription());
		comboFPeriode.setWidthFull();
		comboFPeriode.setRenderer(new ComponentRenderer<>(this::createComboPeriodeInfo ));

		comboFMatpel.setItems(model.listFMatPel);
		comboFMatpel.setItemLabelGenerator(p -> p.getKode1() + "  " + p.getDescription());
		comboFMatpel.setWidthFull();
		comboFMatpel.setRenderer(new ComponentRenderer<>(this::createComboMatpelInfo ));



		kode1.setValueChangeMode(ValueChangeMode.EAGER);
		description.setValueChangeMode(ValueChangeMode.EAGER);
		notes.setValueChangeMode(ValueChangeMode.EAGER);
		fieldKuotaMale.setValueChangeMode(ValueChangeMode.EAGER);
		fieldKuotaFemale.setValueChangeMode(ValueChangeMode.EAGER);

		fieldKuotaMale.setPattern("[0-9]*");
		fieldKuotaFemale.setPattern("[0-9]*");

		// Form layout
		FormLayout form = new FormLayout();
		form.addClassNames(LumoStyles.Padding.Bottom.L,
				LumoStyles.Padding.Horizontal.L, LumoStyles.Padding.Top.S);
		form.setResponsiveSteps(
				new FormLayout.ResponsiveStep("0", 1,
						FormLayout.ResponsiveStep.LabelsPosition.TOP),
				new FormLayout.ResponsiveStep("21em", 2,
						FormLayout.ResponsiveStep.LabelsPosition.TOP));

		FormLayout.FormItem comboFDivisionItem = form.addFormItem(comboFDivision, "Division");
		form.addFormItem(kode1, "Kode1");
//		form.addFormItem(lastName, "Last Name");
		FormLayout.FormItem descriptionItem = form.addFormItem(description, "Deskripsi");
		FormLayout.FormItem notesItem = form.addFormItem(notes, "Catatan");

		FormLayout.FormItem kuotaMaleItem = form.addFormItem(fieldKuotaMale, "Kuota Laki-Laki");
		FormLayout.FormItem kuotaFemaleItem = form.addFormItem(fieldKuotaFemale, "Kuota Perempuan");

		FormLayout.FormItem toggleStatusItem = form.addFormItem(toggleStatActive, "Status");

//		FormLayout.FormItem datePickerKurikulumFromItem = form.addFormItem(datePickerKurikulumFrom, "Tgl Mulai");
//		FormLayout.FormItem datePickerKurikulumToItem = form.addFormItem(datePickerKurikulumTo, "Tgl Mulai");

		FormLayout.FormItem comboFPeriodeItem = form.addFormItem(comboFPeriode, "Periode");
		FormLayout.FormItem comboFMatpelItem = form.addFormItem(comboFMatpel, "Matpel");

		UIUtils.setColSpan(2, descriptionItem, notesItem, toggleStatusItem, comboFPeriodeItem, comboFMatpelItem, comboFDivisionItem);

		datePickerKurikulumFrom.setClearButtonVisible(true);
		datePickerKurikulumFrom.setLocale(Locale.ITALIAN);

		datePickerKurikulumTo.setClearButtonVisible(true);
		datePickerKurikulumTo.setLocale(Locale.ITALIAN);

		// enable/disable save button while editing
		binder.addStatusChangeListener(event -> {
			final boolean isValid = !event.hasValidationErrors();
			final boolean hasChanges = binder.hasChanges();
//			save.setEnabled(hasChanges && isValid);
//			discard.setEnabled(hasChanges);
			footer.setEnabled(hasChanges && isValid);
		});

		return form;
	}

	private FormLayout createDetails_Biaya() {

		comboFKompBiaya.setItems(model.listFKompBiaya);
		comboFKompBiaya.setItemLabelGenerator(p -> p.getKode1() + "  " + p.getDescription());
//		comboFKompBiaya.setRenderer(new ComponentRenderer<>(this::createComboDivisionInfo ));

		comboFKompBiaya.setWidthFull();
//		fieldAmountRp.setWidthFull();
		fieldNotesBiaya.setWidthFull();
		fieldAmountRp.addThemeVariants(TextFieldVariant.LUMO_ALIGN_RIGHT);
		fieldAmountRp.setPattern("\\d*");

		// Form layout
		FormLayout form = new FormLayout();
		form.addClassNames(LumoStyles.Padding.Bottom.L,
				LumoStyles.Padding.Horizontal.L, LumoStyles.Padding.Top.S);
		form.setResponsiveSteps(
				new FormLayout.ResponsiveStep("0", 1,
						FormLayout.ResponsiveStep.LabelsPosition.TOP),
				new FormLayout.ResponsiveStep("21em", 2,
						FormLayout.ResponsiveStep.LabelsPosition.TOP));

		FormLayout.FormItem comboFKompBiayaItem = form.addFormItem(comboFKompBiaya, "Jenis Biaya");
//		form.addFormItem(comboFKompBiaya, "Jenis Biaya");
		form.addFormItem(fieldAmountRp, "Nilai Biaya");
		FormLayout.FormItem notesBiayaItem = form.addFormItem(fieldNotesBiaya, "Keterangan Biaya");
//		form.addFormItem(fieldNotesBiaya, "Keterangan");
		UIUtils.setColSpan(2, comboFKompBiayaItem, notesBiayaItem);

		return form;
	}

	private void filter() {
		// dataProvider.setFilterByValue(FKurikulum::getRole, FKurikulum.Role.ACCOUNTANT);
	}

	String filterText = "";
	public void setFilter(String filterText) {
		Objects.requireNonNull(filterText, "Filter text cannot be null.");
		if (Objects.equals(this.filterText, filterText.trim())) {
			return;
		}
		this.filterText = filterText.trim().toLowerCase(Locale.ENGLISH);

		dataProvider.setFilter(domain -> passesFilter(domain.getKode1(), this.filterText)
				|| passesFilter(domain.getDescription(), this.filterText)
				);
	}
	private boolean passesFilter(Object object, String filterText) {
		return object != null && object.toString().toLowerCase(Locale.ENGLISH)
				.contains(filterText);
	}


}
