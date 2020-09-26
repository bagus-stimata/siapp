package com.desgreen.education.siapp.ui.views.master_siswa;

import com.desgreen.education.siapp.AppPublicService;
import com.desgreen.education.siapp.backend.model.EnumStatSiswa;
import com.desgreen.education.siapp.backend.model.FDivision;
import com.desgreen.education.siapp.backend.model.FSiswa;
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
import com.desgreen.education.siapp.ui.util.UIUtils;
import com.desgreen.education.siapp.ui.util.css.BoxSizing;
import com.desgreen.education.siapp.ui.utils.common.CommonImageFactory;
import com.desgreen.education.siapp.ui.views.SplitViewFrame;
import com.desgreen.education.siapp.ui.utils.common.CommonDateFormat;
import com.desgreen.education.siapp.ui.utils.common.CommonFileFactory;
import com.vaadin.componentfactory.ToggleButton;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.radiobutton.RadioGroupVariant;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.component.upload.SucceededEvent;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.StringToBigDecimalConverter;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.data.converter.StringToLongConverter;
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
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Objects;


@Secured({Role.ADMIN, Role.MNU_MASTER_SISWA})
@UIScope
@SpringComponent
@Route(value = "SiswaView", layout = MainLayout.class)
@PageTitle("SiswaView")
public class SiswaView extends SplitViewFrame {

	protected SiswaModel model;
	protected SiswaController controller;
	protected SiswaListener listener;

	@Autowired
	private AppPublicService appPublicService;
	@Autowired
	public AuthUserDetailsService authUserDetailsService;

	protected Grid<FSiswa> grid;
	protected ListDataProvider<FSiswa> dataProvider;

	protected DetailsDrawer detailsDrawer;
	protected DetailsDrawerHeader detailsDrawerHeader;

	// Tabs in the drawer
	Tab tabInfoUmum = new Tab("Info Umum");
	Tab tabOrangTua = new Tab("Orang Tua");
	Tab tabGambar = new Tab("Photo");
	Tab tabSambung = new Tab("Sambung");

	Tabs tabs = new Tabs(tabInfoUmum, tabOrangTua, tabGambar, tabSambung);

	private AppBar appBar;
	private Button btnReloadFromDB;
	private Button btnSearchForm;
	private Button btnNewForm;
	private Button btnDeleteDeleteForm;

	protected Binder<FSiswa> binder = new BeanValidationBinder<>(FSiswa.class);
//	protected FSiswa currentDomain;
//	protected static FSiswa oldDomain;

	protected boolean isImageChange = false;

	public SiswaView() {
		super();
	}
	@PostConstruct
	private void init(){
		model = new SiswaModel(authUserDetailsService, appPublicService);
		controller = new SiswaController(model, this);
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

		/**
		 * Harus dilakukan binding dahulu
		 */
		filter();
		binder.readBean(new FSiswa());
		bindField();
		detailsDrawer.setContent(createDetails_InfoUmum());
//		detailsDrawer.setContent(createDetails_InfoUmum());
//		detailsDrawer.setContent(createDetails_InfoUmum());
	}


	private void initAppBar() {
		appBar = MainLayout.get().getAppBar();
		appBar.searchModeOff();

		/**
		 * TAB GRID
		 */
		for (EnumStatSiswa status : EnumStatSiswa.values()) {
			if (! status.equals(EnumStatSiswa.OTH1)) {
				appBar.addTab(status.getStringId());
			}
		}//endfor
		appBar.addTabSelectionListener(e -> {
			filter();
			detailsDrawer.hide();
		});
		appBar.centerTabs();


		btnSearchForm = appBar.addActionItem(VaadinIcon.SEARCH);
		btnReloadFromDB = appBar.addActionItem(VaadinIcon.REFRESH);
		btnNewForm = appBar.addActionItem(VaadinIcon.PLUS);
		btnDeleteDeleteForm = appBar.addActionItem(VaadinIcon.TRASH);

		btnNewForm.addThemeVariants(ButtonVariant.MATERIAL_OUTLINED);
		btnDeleteDeleteForm.addThemeVariants(ButtonVariant.MATERIAL_OUTLINED, ButtonVariant.LUMO_ERROR);

		btnReloadFromDB.addClickListener(e -> listener.aksiBtnReloadFromDb());
		btnSearchForm.addClickListener(e -> appBar.searchModeOn());
		btnNewForm.addClickListener(e -> listener.aksiBtnNewForm());
		btnDeleteDeleteForm.addClickListener(e -> listener.aksiBtnDeleteForm());

		appBar.addSearchListener( e -> listener.valueChangeListenerSearch(e));

		upload.addSucceededListener(event -> updateImageView(event));

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
//		dataProvider = DataProvider.ofCollection(DummyData.getFSiswas());
//		dataProvider = DataProvider.ofCollection(new ArrayList<>());
		dataProvider = DataProvider.ofCollection( model.mapHeader.values() );

		grid.setDataProvider(dataProvider);
		grid.setHeightFull();

		grid.addColumn(FSiswa::getNis)
				.setAutoWidth(true)
				.setFlexGrow(0)
				.setFrozen(true)
				.setHeader("NIS")
				.setSortable(true);
		grid.addColumn(new ComponentRenderer<>(this::createDetilInfo))
				.setAutoWidth(true)
				.setFrozen(true)
				.setHeader("Nama Siswa")
				.setComparator(FSiswa::getFullName)
				.setSortable(true);
		grid.addColumn(new ComponentRenderer<>(this::createStatus))
				.setAutoWidth(true)
				.setFlexGrow(0)
				.setHeader("Stat/Gendr")
				.setComparator((o1, o2) -> Boolean.compare(o1.isSex(), o2.isSex()) )
				.setSortable(true)
				.setTextAlign(ColumnTextAlign.CENTER);
		grid.addColumn(new ComponentRenderer<>(this::createContact))
				.setAutoWidth(true)
				.setHeader("Telp");
		grid.addColumn(new ComponentRenderer<>(this::createCity))
				.setAutoWidth(true)
				.setFlexGrow(0)
				.setHeader("Kota")
				.setTextAlign(ColumnTextAlign.END)
				.setComparator(( o1, o2) ->
						o1.getCity().compareToIgnoreCase(o2.getCity()))
				.setSortable(true);
		grid.addColumn(new ComponentRenderer<>(this::createAddreses))
				.setAutoWidth(true)
//				.setFlexGrow(0)
				.setHeader("Alamat")
				.setTextAlign(ColumnTextAlign.END);

		return grid;
	}

//	private Component createUserInfo(FSiswa fSiswa) {
//		ListItem item = new ListItem(
//				new Initials(fSiswa.getInitials()), fSiswa.getFullName(),
//				fSiswa.getNis());
//		item.setPadding(Vertical.XS);
//		item.setSpacing(Right.M);
//		return item;
//	}
	private Component createDetilInfo(FSiswa fSiswa) {
		String ortu = "";
		if (fSiswa.isSex()) {
			ortu = "Bin " + fSiswa.getFatherName();
		}else {
			ortu = "Binti " + fSiswa.getFatherName();
		}
		ListItem item = new ListItem(
				new Initials(fSiswa.getInitials()), fSiswa.getFullName(),
				ortu);
		item.setPadding(Vertical.XS);
		item.setSpacing(Right.M);
		return item;
	}

	private Component createContact(FSiswa fSiswa) {
		ListItem item = new ListItem( fSiswa.getPhone());
		item.setPadding(Vertical.XS);
		item.setSpacing(Right.M);

		return item;
	}

	private Component createStatus(FSiswa fSiswa) {
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

	private Component createCity(FSiswa fSiswa) {
		return UIUtils.createBoldLabel(fSiswa.getCity());
	}

	private Component createAddreses(FSiswa fSiswa) {
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

	private Component createDate(FSiswa fSiswa) {
		return new Span(UIUtils.formatDate(CommonDateFormat.fromDate(fSiswa.getModified())));
	}


	private Component createComboDivisionInfo(FDivision fDivision) {
		ListItem item = new ListItem( fDivision.getDescription(),
				fDivision.getFcompanyBean().getDescription());
		item.setPadding(Vertical.XS);
		item.setSpacing(Right.M);

		return item;
	}

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
//			detailsDrawer.hide();//dipindah ke aksiBtnSave
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
//			detailsDrawer.setContent(createDetails(grid.getSelectionModel().getFirstSelectedItem().get()));
			detailsDrawer.setContent(createDetails_InfoUmum());
		} else if (selectedTab.equals(tabOrangTua)) {
			detailsDrawer.setContent(createDetails_OrangTua());
		} else if (selectedTab.equals(tabGambar)) {
			detailsDrawer.setContent(createDetails_Photo());
		} else if (selectedTab.equals(tabSambung)) {
			detailsDrawer.setContent(createDetails_Sambung());
		}
	}

	protected void showDetails(FSiswa fSiswa) {
		if (fSiswa == null) {
			fSiswa = new FSiswa();
		}
		model.currentDomain = fSiswa;
		model.oldDomain = fSiswa;
		isImageChange= false;

		try {
			binder.readBean(model.currentDomain);
		}catch (Exception ex) {ex.printStackTrace();}

		bindField();
		// delete.setVisible(!product.isNewProduct());
		btnDeleteDeleteForm.setVisible(! model.currentDomain.isNewDomain());


		detailsDrawerHeader.setTitle(fSiswa.getFullName());
//		detailsDrawer.setContent(createDetails(fSiswa)); --> Dipindah ke configureTabs

		/**
		 * Yang lainnya, ketika dimainkan binder sudah bisa update
		 * untuk case ini di lakukan karena binder nya manual
		 */
		Tab selectedTab = tabs.getSelectedTab();
		if (selectedTab.equals(tabGambar)) {
			detailsDrawer.setContent(createDetails_Photo());
		}

		detailsDrawer.show();

	}

	protected void bindField(){
		/**
		 * Re Binding Datata
		 */
		binder.bindInstanceFields(this);
//		binder.forField(city).withConverter(new PriceConverter())
//				.bind("price");
//		binder.forField(kode1).bind(FSiswa::getKode1, FSiswa::setKode1);
//		binder.forField(radioButtonStatusActive)
//				.withConverter(new StringToBooleanConverter("", "Active", "Non-Active"))
//				.bind(FSiswa::isStatActive, FSiswa::setStatActive);

		binder.forField(fieldId).asRequired()
				.withConverter(new StringToLongConverter("Harus Angka"))
				.bind(FSiswa::getId, FSiswa::setId);

		binder.forField(fullName).asRequired()
				.bind(FSiswa::getFullName, FSiswa::setFullName);

		// binder.forField(birthDate).asRequired()
		// 		.asRequired()
		// 		.bind(FSiswa::getBirthDate, FSiswa::setBirthDate);

		binder.forField(fieldAnakKe_)
				.withConverter(new StringToIntegerConverter("Harus Angka"))
				.bind(FSiswa::getAnakKe, FSiswa::setAnakKe);
		binder.forField(fieldDariKe_)
				.withConverter(new StringToIntegerConverter("Harus Angka"))
				.bind(FSiswa::getDariKe, FSiswa::setDariKe);

		binder.forField(comboFDivision)
				.asRequired()
				.bind(FSiswa::getFdivisionBean, FSiswa::setFdivisionBean);

//		binder.forField(checkStatusActive)
//				.bind(FSiswa::isStatActive, FSiswa::setStatActive);
		// binder.forField(checkSex)
		// 		.bind(FSiswa::isSex, FSiswa::setSex);
		// binder.forField(checkMenikah)
		// 		.bind(FSiswa::isMenikah, FSiswa::setMenikah);
		binder.forField(toggleSex)
				.bind(FSiswa::isSex, FSiswa::setSex);
		binder.forField(toggleMenikah)
				.bind(FSiswa::isMenikah, FSiswa::setMenikah);

		binder.forField(comboStatSiswa)
				.bind(FSiswa::getStatSiswa, FSiswa::setStatSiswa);


		/**
		 * binder ke dlam
		 */

		binder.forField(daerahSambung)
				.bind(FSiswa::getDaerahSmbg, FSiswa::setDaerahSmbg);
		binder.forField(desaSambung)
				.bind(FSiswa::getDesaSmbg, FSiswa::setDesaSmbg);
		binder.forField(kelompokSambung)
				.bind(FSiswa::getKelompokSmbg, FSiswa::setKelompokSmbg);
		binder.forField(namaKiKelompok)
				.bind(FSiswa::getKiKelompok, FSiswa::setKiKelompok);
		binder.forField(nomorKiKelompok)
				.bind(FSiswa::getTelpKiKelompok, FSiswa::setTelpKiKelompok);

	}
	/**
	 * FORM COMPONENT
	 * dipanggil sekali saja
	 */
	private final TextField fieldId = new TextField();
	private final ComboBox<FDivision> comboFDivision = new ComboBox<FDivision>();
	private final TextField nis = new TextField();
	private final TextField fullName = new TextField();
	private final TextField nickName = new TextField();
	private final TextField birthPlace = new TextField();
	private final DatePicker birthDate = new DatePicker();

	private final ToggleButton toggleSex = new ToggleButton();
	private final ToggleButton toggleMenikah = new ToggleButton();
	private final ToggleButton toggleStatusActive = new ToggleButton();

	private final Checkbox checkSex = new Checkbox();
	private final Checkbox checkMenikah = new Checkbox();
	private final Checkbox checkStatusActive = new Checkbox();
	private final RadioButtonGroup<String> radioButtonStatusActive = new RadioButtonGroup<>();

	private TextField fieldAnakKe_ = new TextField();
	private TextField fieldDariKe_ = new TextField();

	private final TextField address1 = new TextField();
	private final TextField address2 = new TextField();
	private final TextField address3 = new TextField();
	private final TextField city = new TextField();
	private final TextField state = new TextField();
	private final TextField phone = new TextField();
	private final TextField email = new TextField();

	private final ComboBox<EnumStatSiswa> comboStatSiswa = new ComboBox<EnumStatSiswa>();

	private final TextField notes1 = new TextField();
	private final TextField notes2 = new TextField();


	private final TextField fatherName = new TextField();
	private final TextField fatherJob = new TextField();
	private final TextField fatherPhone = new TextField();

	private final TextField motherName = new TextField();
	private final TextField motherJob = new TextField();
	private final TextField motherPhone = new TextField();


	private final TextField parAddress1 = new TextField();
	private final TextField parAddress2 = new TextField();
	private final TextField parCity = new TextField();
	private final TextField parState = new TextField();
	private final TextField parPhone = new TextField();
	private final TextField parEmail = new TextField();

	private final TextField contact = new TextField();


	private final TextField daerahSambung = new TextField();
	private final TextField desaSambung = new TextField();
	private final TextField kelompokSambung = new TextField();
	private final TextField namaKiKelompok = new TextField();
	private final TextField nomorKiKelompok = new TextField();

//	private FormLayout createDetails(FSiswa fSiswa) {
	private FormLayout createDetails_InfoUmum() {

		fullName.setWidthFull();
		nickName.setWidth("150px");
		address1.setWidthFull();
		email.setWidthFull();

		comboFDivision.setItems(model.listFDivision);
		comboFDivision.setWidthFull();
		comboFDivision.setItemLabelGenerator(p -> p.getKode1() + "  " + p.getDescription());
//		comboFDivision.setRenderer(new ComponentRenderer<>(item -> new Span(item.getDescription())));
		comboFDivision.setRenderer(new ComponentRenderer<>(this::createComboDivisionInfo ));

		if (model.currentDomain!=null) comboFDivision.setValue(model.currentDomain.getFdivisionBean());


		comboStatSiswa.setItems(EnumStatSiswa.values());
		if (model.currentDomain!=null) comboStatSiswa.setValue(model.currentDomain.getStatSiswa());


		notes1.setWidthFull();
		notes2.setWidthFull();


		toggleSex.setLabel("Perempuan");
		toggleSex.addValueChangeListener( e -> {
			if (e.getValue()==true) {
				toggleSex.setLabel("Laki-laki");
			}else {
				toggleSex.setLabel("Perempuan");
			}
		});

		toggleMenikah.setLabel("Belum Menikah");
		toggleMenikah.addValueChangeListener( e -> {
			if (e.getValue()==true) {
				toggleMenikah.setLabel("Menikah");				
			}else {
				toggleMenikah.setLabel("Belum Menikah");
			}
		});

		checkStatusActive.setLabel("Aktif");
//		checkStatusActive.setValue(true);

//		checkSex.setLabel("Laki-Laki");
		birthDate.setClearButtonVisible(true);
		birthDate.setLocale(Locale.ITALIAN);

		radioButtonStatusActive.addThemeVariants(RadioGroupVariant.MATERIAL_VERTICAL);
		radioButtonStatusActive.setItems("Active", "Non-Active");
//		radioButtonStatusActive.setValue("Active");


//		ComboBox company = new ComboBox();
//		company.setItems(DummyData.getCompanies());
//		company.setValue(DummyData.getCompany());
//		company.setWidthFull();

		fieldId.setValueChangeMode(ValueChangeMode.EAGER);
		nis.setValueChangeMode(ValueChangeMode.EAGER);
//		checkStatusActive.setValueChangeMode(ValueChangeMode.EAGER); //Otomatis EAGER
		fullName.setValueChangeMode(ValueChangeMode.EAGER);
		address1.setValueChangeMode(ValueChangeMode.EAGER);
		address2.setValueChangeMode(ValueChangeMode.EAGER);
		city.setValueChangeMode(ValueChangeMode.EAGER);
		phone.setValueChangeMode(ValueChangeMode.EAGER);
		email.setValueChangeMode(ValueChangeMode.EAGER);


		// Form layout
		FormLayout form = new FormLayout();
		form.addClassNames(LumoStyles.Padding.Bottom.L,
				LumoStyles.Padding.Horizontal.L, LumoStyles.Padding.Top.S);
//		form.setResponsiveSteps(
//				new FormLayout.ResponsiveStep("0", 1,
//						FormLayout.ResponsiveStep.LabelsPosition.ASIDE),
//				new FormLayout.ResponsiveStep("21em", 2,
//						FormLayout.ResponsiveStep.LabelsPosition.ASIDE));


		FormLayout.FormItem idItem = form.addFormItem(fieldId, "ID");
		FormLayout.FormItem comboFDivisionItem = form.addFormItem(comboFDivision, "Division");
		FormLayout.FormItem nisItem = form.addFormItem(nis, "NIS");
		FormLayout.FormItem fullNameItem = form.addFormItem(fullName, "Nama Siswa/Siswi");
		FormLayout.FormItem nickNameItem = form.addFormItem(nickName, "Alias");

		// form.addFormItem(checkSex, "Laki-Laki");
		// form.addFormItem(checkMenikah, "Menikah");
//		form.addFormItem(checkStatusActive, "Aktif");
		form.addFormItem(comboStatSiswa, "Status Siswa/Maba");

		form.addFormItem(toggleSex, "Jenis Kelamin");
		form.addFormItem(toggleMenikah, "Status Pernikahan");


		form.addFormItem(birthPlace, "Tempat Lahir");
		form.addFormItem(birthDate, "Tanggal Lahir");

		fieldAnakKe_.setWidth("30px");
		fieldDariKe_.setPrefixComponent(new Label(" dari "));
		fieldDariKe_.setSuffixComponent(new Label(" bersaudara"));
		FlexBoxLayout layoutAnakKe = new FlexBoxLayout(this.fieldAnakKe_, fieldDariKe_);
		layoutAnakKe.setFlexGrow(1, this.fieldAnakKe_);
		layoutAnakKe.setSpacing(Right.S);

		fieldAnakKe_.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER);
		fieldDariKe_.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER);
		fieldAnakKe_.setPattern("[0-9]*");
		fieldDariKe_.setPattern("[0-9]*");

		form.addFormItem(layoutAnakKe, "Anak Ke");

		FormLayout.FormItem address1Item = form.addFormItem(address1, "Jalan RT/RW");
		FormLayout.FormItem address2Item = form.addFormItem(address2, "Desa");
		FormLayout.FormItem address3Item = form.addFormItem(address3, "Kecamatan");

		FormLayout.FormItem cityItem = form.addFormItem(city, "Kota/Kab");
		FormLayout.FormItem stateItem = form.addFormItem(state, "Propinsi");

		form.addFormItem(phone, "Telp/HP");
		form.addFormItem(email, "Email");

		Label labelSectionSpace1 = new Label(); labelSectionSpace1.setHeight("20px");
		form.add(labelSectionSpace1);
		Label labelSectionNotes = UIUtils.createH5Label("# Keterangan Lain");
		form.add(labelSectionNotes);

		form.addFormItem(notes1, "Penyakit Bawaan");
		form.addFormItem(notes2, "Alergi Obat");


		// enable/disable save button while editing
//		binder.addStatusChangeListener(event -> {
//			final boolean isValid = !event.hasValidationErrors();
//			final boolean hasChanges = binder.hasChanges();
////			save.setEnabled(hasChanges && isValid);
////			discard.setEnabled(hasChanges);
//			footer.setEnabled(hasChanges && isValid);
//		});


		return form;
	}

	private FormLayout createDetails_OrangTua() {

		fatherName.setWidthFull();
		motherName.setWidthFull();
		parAddress1.setWidthFull();

		phone.setPrefixComponent(new Span("+62"));
		fatherPhone.setPrefixComponent(new Span("+62"));
		motherPhone.setPrefixComponent(new Span("+62"));


//		ComboBox company = new ComboBox();
//		company.setItems(DummyData.getCompanies());
//		company.setValue(DummyData.getCompany());
//		company.setWidthFull();


		// Form layout
		FormLayout form = new FormLayout();
		form.addClassNames(LumoStyles.Padding.Bottom.L,
				LumoStyles.Padding.Horizontal.L, LumoStyles.Padding.Top.S);
//		form.setResponsiveSteps(
//				new FormLayout.ResponsiveStep("0", 1,
//						FormLayout.ResponsiveStep.LabelsPosition.ASIDE),
//				new FormLayout.ResponsiveStep("21em", 2,
//						FormLayout.ResponsiveStep.LabelsPosition.ASIDE));


		form.addFormItem(fatherName, "Nama Bapak");
		form.addFormItem(fatherJob, "Pekerjaan Ayah");
		form.addFormItem(fatherPhone, "Telp/HP");

		form.addFormItem(motherName, "Nama Ibu");
		form.addFormItem(motherJob, "Pekerjaan Ibu");
		form.addFormItem(motherPhone, "Telp/HP");

		form.addFormItem(parAddress1, "Alamat");
		form.addFormItem(parCity, "Kota");
		form.addFormItem(parState, "Propinsi");


		// enable/disable save button while editing
//		binder.addStatusChangeListener(event -> {
//			final boolean isValid = !event.hasValidationErrors();
//			final boolean hasChanges = binder.hasChanges();
////			save.setEnabled(hasChanges && isValid);
////			discard.setEnabled(hasChanges);
//			footer.setEnabled(hasChanges && isValid);
//		});


		return form;
	}

	private FormLayout createDetails_Sambung() {

		daerahSambung.setWidthFull();
		desaSambung.setWidthFull();
		kelompokSambung.setWidthFull();
		namaKiKelompok.setWidthFull();
		nomorKiKelompok.setWidthFull();

		nomorKiKelompok.setPrefixComponent(new Span("+62"));


		// Form layout
		FormLayout form = new FormLayout();
		form.addClassNames(LumoStyles.Padding.Bottom.L,
				LumoStyles.Padding.Horizontal.L, LumoStyles.Padding.Top.S);

		form.addFormItem(daerahSambung, "Daerah");
		form.addFormItem(desaSambung, "Desa");
		form.addFormItem(kelompokSambung, "Kelompok");
		form.addFormItem(namaKiKelompok, "KI Kelompok");
		form.addFormItem(nomorKiKelompok, "Telp Ki Klp");


		return form;
	}

	protected Image imageOuput = new Image();
	protected MemoryBuffer buffer = new MemoryBuffer();
	protected Upload upload = new Upload(buffer);
	Div divImage = new Div();
	private VerticalLayout createDetails_Photo() {

		upload.setAcceptedFileTypes("image/jpeg", "image/png", "image/gif", "image/jpg");
		upload.setMaxFiles(1);
		upload.setAutoUpload(true); //ini bagian penting
		upload.setSizeFull();

//		upload.setMaxFileSize(1500 * 1024);
		upload.setMaxFileSize(3 * 1500 * 1024);
		upload.setHeight("100px");

		imageOuput = new Image();
		try {
			imageOuput = CommonFileFactory.generateImage(AppPublicService.FILE_PATH + model.currentDomain.getImageName());
		}catch (Exception ex){}
		int newWidth = 300;
		int newHeight = 400;
		try {
			BufferedImage bufferedImage = ImageIO.read(new File(AppPublicService.FILE_PATH + model.currentDomain.getImageName()));
			newHeight = CommonImageFactory.getMaxScaleHeight(bufferedImage.getWidth(), bufferedImage.getHeight(), newWidth);
		}catch (Exception ex){}
		imageOuput.setWidth(newWidth, Unit.PIXELS);
		imageOuput.setHeight(newHeight, Unit.PIXELS);

		divImage.removeAll();
		divImage.addComponentAsFirst(imageOuput);

		VerticalLayout form = new VerticalLayout(upload, divImage);

		form.setMargin(true);
		form.setSpacing(true);
		form.setAlignItems(FlexComponent.Alignment.CENTER);

		return form;

	}

	private void updateImageView(SucceededEvent event){
		isImageChange =true;

		//Agar Photo langsung terlihat
		Component component = UIUtils.createComponentFromFile(event.getMIMEType(),
				event.getFileName(), buffer.getInputStream());

		int newWidth = 300;
		int newHeight = 400;
		try {
			BufferedImage buffImage = ImageIO.read(buffer.getInputStream());
			buffImage = CommonImageFactory.autoRotateImage(buffImage,
					CommonImageFactory.getImageRotationSuggestion(buffer.getInputStream()));
			newHeight = CommonImageFactory.getMaxScaleHeight(buffImage, newWidth);
		}catch (Exception ex){}

		imageOuput = (Image) component;
		imageOuput.setWidth(newWidth, Unit.PIXELS);
		imageOuput.setHeight(newHeight, Unit.PIXELS);


		divImage.removeAll();
		divImage.addComponentAsFirst(imageOuput);

		footer.setEnabled(true);

//		System.out.println("Ukuran: " + buffer);
	}


	private static class PriceConverter extends StringToBigDecimalConverter {

		public PriceConverter() {
			super(BigDecimal.ZERO, "Cannot convert value to a number.");
		}

		@Override
		protected NumberFormat getFormat(Locale locale) {
			// Always display currency with two decimals
			final NumberFormat format = super.getFormat(locale);
			if (format instanceof DecimalFormat) {
				format.setMaximumFractionDigits(2);
				format.setMinimumFractionDigits(2);
			}
			return format;
		}
	}

	protected void filter() {
		Tab selectedTab = MainLayout.get().getAppBar().getSelectedTab();
		if (selectedTab != null)
			dataProvider.setFilterByValue(p -> p.getStatSiswa()!=null? p.getStatSiswa():"", EnumStatSiswa.valueOf(selectedTab.getLabel().toUpperCase()));
//		dataProvider.setFilterByValue(FSiswa::getStatSiswa, EnumStatSiswa.valueOf(selectedTab.getLabel().toUpperCase()));
	}

	String filterText = "";
	public void setFilter(String filterText) {
		Objects.requireNonNull(filterText, "Filter teks tidak boleh kosong");
		if (Objects.equals(this.filterText, filterText.trim())) {
			return;
		}
		this.filterText = filterText.trim().toLowerCase(Locale.ENGLISH);

		dataProvider.setFilter(domain -> passesFilter(domain.getNis(), this.filterText)
				|| passesFilter(domain.getFatherName(), this.filterText)
				|| passesFilter(domain.isSex()? "Laki": "Perempu", this.filterText)
				|| passesFilter(domain.getFullName(), this.filterText)
				|| passesFilter(domain.getAddress1(), this.filterText)
				|| passesFilter(domain.getCity(), this.filterText));

		Tab selectedTab = MainLayout.get().getAppBar().getSelectedTab();
		if (selectedTab != null  &&  selectedTab.getId().isPresent()) {
			try {
				dataProvider.addFilterByValue(p -> p.getStatSiswa() != null ? p.getStatSiswa() : "", EnumStatSiswa.valueOf(selectedTab.getLabel().toUpperCase()));
			}catch (Exception ex){}
		}

	}
	private boolean passesFilter(Object object, String filterText) {
		return object != null && object.toString().toLowerCase(Locale.ENGLISH)
				.contains(filterText);
	}




}
