package com.desgreen.education.siapp.ui.views.users_pengguna;

import com.desgreen.education.siapp.AppPublicService;
import com.desgreen.education.siapp.backend.model.EnumUserType;
import com.desgreen.education.siapp.backend.model.FDivision;
import com.desgreen.education.siapp.security_config.AuthUserDetailsService;
import com.desgreen.education.siapp.security_model.EnumOrganizationLevel;
import com.desgreen.education.siapp.security_model.FUser;
import com.desgreen.education.siapp.security_model.FUserRoles;
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
import com.desgreen.education.siapp.ui.views.SplitViewFrame;
import com.desgreen.education.siapp.ui.utils.common.CommonDateFormat;
import com.desgreen.education.siapp.ui.utils.common.CommonFileFactory;
import com.vaadin.componentfactory.ToggleButton;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
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
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.SucceededEvent;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
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
import java.util.Locale;
import java.util.Objects;


@Secured({Role.ADMIN, Role.MNU_PENGGUNA_OTORISASI})
@UIScope
@SpringComponent
@Route(value = "UsersView", layout = MainLayout.class)
@PageTitle("UsersView")
public class UsersView extends SplitViewFrame {

	protected UsersModel model;
	protected UsersController controller;
	protected UsersListener listener;

	@Autowired
	private AppPublicService appPublicService;
	@Autowired
	public AuthUserDetailsService authUserDetailsService;

	protected Grid<FUser> grid;
	protected Grid<FUserRoles> gridDetil1;
	protected ListDataProvider<FUser> dataProvider;
	protected ListDataProvider<FUserRoles> dataProviderDetil1;

	protected DetailsDrawer detailsDrawer;
	protected DetailsDrawerHeader detailsDrawerHeader;

	// Tabs in the drawer
	Tab tabUser = new Tab("User");
	Tab tabOtorisasi = new Tab("Otorisasi");
	Tab tabPhoto = new Tab("Photo");

	Tabs tabs = new Tabs(tabUser, tabOtorisasi, tabPhoto );

	private AppBar appBar;
	private Button btnReloadFromDB;
	private Button btnSearchForm;
	private Button btnNewForm;
	private Button btnDeleteDeleteForm;

	protected Binder<FUser> binder = new BeanValidationBinder<>(FUser.class);

	protected boolean isImageChange = false;

	public UsersView() {
		super();
		/**
		 * Init Dummy
		 */
	}
	@PostConstruct
	private void init(){
		model = new UsersModel(authUserDetailsService, appPublicService);
		controller = new UsersController(model, this);
//		model = controller.model;
		listener = controller;

//		initAppBar();
//		setViewContent(createContent());
//		setViewDetails(createDetailsDrawer());
//		filter();

		/**
		 * Init Data
		 */

//		System.out.println("Ukuran Company : " + appParamService.publicMapCompanys.values().size());
//		for (FCompany fCompany: appParamService.publicMapCompanys.values()) {
//			System.out.println("Ukuran Company : " + fCompany.getDescription());
//		}

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
		try {
			filter();
			binder.readBean(new FUser());
			bindField();
			if (model.currentDomain != null) detailsDrawer.setContent(createDetails_InfoUmum());
		}catch (Exception ex){}

//		detailsDrawer.setContent(createDetails_InfoUmum());
//		detailsDrawer.setContent(createDetails_InfoUmum());

	}


	private void initAppBar() {
		appBar = MainLayout.get().getAppBar();
		appBar.searchModeOff();

		/**
		 * TAB GRID
		 */
		for (EnumUserType status : EnumUserType.values()) {
			// if (! status.equals(EnumUserType.OTH1)) {
			// 	appBar.addTab(status.getStringId());
			// }
			appBar.addTab(status.getStringId());
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
//		dataProvider = DataProvider.ofCollection(DummyData.getFUsers());
//		dataProvider = DataProvider.ofCollection(new ArrayList<>());
		dataProvider = DataProvider.ofCollection( model.mapHeader.values() );

		grid.setDataProvider(dataProvider);
		grid.setHeightFull();

		grid.addColumn(FUser::getId)
				.setAutoWidth(true)
				.setFlexGrow(0)
				.setFrozen(true)
				.setHeader("ID")
				.setSortable(true);
		grid.addColumn(new ComponentRenderer<>(this::createDetilInfo))
				.setAutoWidth(true)
				.setFrozen(true)
				.setHeader("User Name")
				.setComparator(FUser::getFullName)
				.setSortable(true);
		grid.addColumn(new ComponentRenderer<>(this::createStatus))
				.setAutoWidth(true)
				.setFlexGrow(0)
				.setHeader("Locked")
				.setComparator((o1, o2) -> Boolean.compare(o1.isLocked(), o2.isLocked()) )
				.setSortable(true)
				.setTextAlign(ColumnTextAlign.CENTER);
		grid.addColumn(FUser::getFullName)
				.setAutoWidth(true)
				.setHeader("Nama Lengkap");
		grid.addColumn(new ComponentRenderer<>(this::createContact))
				.setAutoWidth(true)
				.setHeader("Telp");

		return grid;
	}


//	private Component createUserInfo(FUser fUser) {
//		ListItem item = new ListItem(
//				new Initials(fUser.getInitials()), fUser.getFullName(),
//				fUser.getNis());
//		item.setPadding(Vertical.XS);
//		item.setSpacing(Right.M);
//		return item;
//	}
	private int rowIndex = 0;
	private String getRowIndex(){
		return String.valueOf(++this.rowIndex); // increments the rowIndex for each row
	}
	private Component createDetilInfo(FUser fUser) {
		ListItem item = new ListItem(
				new Initials(fUser.getInitials()), fUser.getUsername(),
				fUser.getEmail() );
		item.setPadding(Vertical.XS);
		item.setSpacing(Right.M);
		return item;
	}

	private Component createContact(FUser fUser) {
		ListItem item = new ListItem( fUser.getPhone());
		item.setPadding(Vertical.XS);
		item.setSpacing(Right.M);

		return item;
	}

	private Component createStatus(FUser fUser) {
		Icon icon;
		if (fUser.isLocked()) {
			icon = UIUtils.createErrorIcon(VaadinIcon.LOCK);
		}else {
			icon = UIUtils.createPrimaryIcon(VaadinIcon.UNLOCK);
		}
		return icon;
	}

	private Component createDate(FUser fUser) {
		return new Span(UIUtils.formatDate(CommonDateFormat.fromDate(fUser.getLastModified())));
	}

	private Component createComboDivisionInfo(FDivision fDivision) {
		ListItem item = new ListItem( fDivision.getDescription(), fDivision.getFcompanyBean().getDescription());
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
//			detailsDrawer.hide(); //dipindah di aksiBtn Save
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
		if (selectedTab.equals(tabUser)) {
//			detailsDrawer.setContent(createDetails(grid.getSelectionModel().getFirstSelectedItem().get()));
			detailsDrawer.setContent(createDetails_InfoUmum());
		} else if (selectedTab.equals(tabOtorisasi)) {
			detailsDrawer.setContent(createDetails_Otorisasi());
		} else if (selectedTab.equals(tabPhoto)) {
			detailsDrawer.setContent(createDetails_Photo());
		}
	}

	protected void showDetails(FUser fUser) {
		if (fUser == null) {
			fUser = new FUser();
		}
		model.currentDomain = fUser;
		final FUser tempOldDomain = fUser;
		model.oldDomain = tempOldDomain;
		isImageChange= false;

		try {
			binder.readBean(model.currentDomain);
		}catch (Exception ex) {ex.printStackTrace();}

		bindField();
		// delete.setVisible(!product.isNewProduct());
		btnDeleteDeleteForm.setVisible(! model.currentDomain.isNewDomain());


		detailsDrawerHeader.setTitle(fUser.getFullName());

		password_manual.setValue("");

//		detailsDrawer.setContent(createDetails(fUser)); --> Dipindah ke configureTabs
		/**
		 * Yang lainnya, ketika dimainkan binder sudah bisa update
		 * untuk case ini di lakukan karena binder nya manual
		 */
		// Tab selectedTab = tabs.getSelectedTab();
		// if (selectedTab.equals(tabGambar)) {
		// 	// detailsDrawer.setContent(createDetails_Photo());
		// }
/**
		 * Manual Bind
		 */
		if ( model.currentDomain !=null){
			model.setMapTempUserRoles( model.currentDomain);
//			dataProviderDetil1.refreshAll();
		}
		configureTabs();

		Tab selectedTab = tabs.getSelectedTab();
		if (selectedTab.equals( tabPhoto)) {
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

		binder.forField(username).asRequired()
				.bind(FUser::getUsername, FUser::setUsername);
		binder.forField(fullName).asRequired()
				.bind(FUser::getFullName, FUser::setFullName);


		binder.forField(fieldIdSiswa)
				.withConverter(new StringToLongConverter("Harus Angka"))
				.bind(FUser::getIdSiswa, FUser::setIdSiswa);

		binder.forField(comboOrganizationLevel).asRequired()
				.bind(FUser::getOrganizationLevel, FUser::setOrganizationLevel);

//		binder.forField(fieldDivision).asRequired()
//				.bind(FUser::getFdivisionBean, FUser::setFdivisionBean);
		binder.forField(comboFDivision).asRequired()
				.bind(FUser::getFdivisionBean, FUser::setFdivisionBean);

		binder.forField(comboStatUser)
				.bind(FUser::getUserType, FUser::setUserType);

		binder.forField(toggleLocked)
				.bind(FUser::isLocked, FUser::setLocked);


		
		
	}
	/**
	 * FORM COMPONENT
	 * dipanggil sekali saja
	 */
	protected final TextField username = new TextField();
	protected final TextField email = new TextField();
	private final TextField fullName = new TextField();

	protected final PasswordField password_manual = new PasswordField();
	protected final PasswordField passwordConfirm_manual = new PasswordField();

	private final TextField birthPlace = new TextField();
	private final DatePicker birthDate = new DatePicker();

	private final ToggleButton toggleLocked = new ToggleButton();
	private final ToggleButton toggleStatusActive = new ToggleButton();


	private ComboBox<FDivision> comboFDivision = new ComboBox<FDivision>();
	private ComboBox<EnumOrganizationLevel> comboOrganizationLevel = new ComboBox<EnumOrganizationLevel>();
	private ComboBox<EnumUserType> comboStatUser = new ComboBox<EnumUserType>();
	private final TextField fieldIdSiswa = new TextField();

	private final TextField address1 = new TextField();
	private final TextField address2 = new TextField();
	private final TextField address3 = new TextField();
	private final TextField city = new TextField();
	private final TextField state = new TextField();
	private final TextField phone = new TextField();


	private final TextField notes1 = new TextField();
	private final TextField notes2 = new TextField();


	private FormLayout createDetails_InfoUmum() {

		username.setWidthFull();
		passwordConfirm_manual.setWidthFull();
		password_manual.setWidthFull();

		fullName.setWidthFull();
		address1.setWidthFull();
		email.setWidthFull();

		comboFDivision.setItems(model.listFDivision);
		comboFDivision.setItemLabelGenerator(p -> p.getDescription() + "  [" + p.getFcompanyBean().getDescription() + "]");
//		comboFDivision.setRenderer(new ComponentRenderer<>(item -> new Span(item.getDescription())));
		comboFDivision.setRenderer(new ComponentRenderer<>(this::createComboDivisionInfo));
		/**
		 * Terpakaksa karena menggunakan Bind Tidak bisa
		 */
		if (model.currentDomain!=null) comboFDivision.setValue(model.currentDomain.getFdivisionBean());
		comboFDivision.setWidthFull();

		comboOrganizationLevel.setItems(EnumOrganizationLevel.values());
		comboOrganizationLevel.setItemLabelGenerator(EnumOrganizationLevel::getDescription);
		if (model.currentDomain!=null) comboOrganizationLevel.setValue(model.currentDomain.getOrganizationLevel());
		comboStatUser.setItems(EnumUserType.values());
		comboStatUser.setItemLabelGenerator(EnumUserType::getDescription);
		if (model.currentDomain!=null) comboStatUser.setValue(model.currentDomain.getUserType());


		notes1.setWidthFull();
		notes2.setWidthFull();

		phone.setPrefixComponent(new Span("+62"));
		toggleLocked.setLabel("Active");
		toggleLocked.addValueChangeListener( e -> {
			if (e.getValue()==true) {
				toggleLocked.setLabel("Locked");
			}else {
				toggleLocked.setLabel("Active");
			}
		});


//		checkSex.setLabel("Laki-Laki");
		birthDate.setClearButtonVisible(true);
		birthDate.setLocale(Locale.ITALIAN);


//		ComboBox company = new ComboBox();
//		company.setItems(DummyData.getCompanies());
//		company.setValue(DummyData.getCompany());
//		company.setWidthFull();

		username.setValueChangeMode(ValueChangeMode.EAGER);
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


		FormLayout.FormItem usernameItem = form.addFormItem(username, "UserName");
		FormLayout.FormItem emailItem = form.addFormItem(email, "Email");
	
		FormLayout.FormItem passwordItem = form.addFormItem(password_manual, "Ganti Password Password");
//		FormLayout.FormItem confirmPasswordItem = form.addFormItem(passwordConfirm_manual, "Confirm Password");

		form.addFormItem(comboFDivision, "DIVISI");
		form.addFormItem(comboOrganizationLevel, "LEVEL ORGANISASI");
		form.addFormItem(comboStatUser, "TIPE USER");

		FormLayout.FormItem fullNameItem = form.addFormItem(fullName, "Nama Lengkap");
		FormLayout.FormItem phoneItem = form.addFormItem(phone, "Telpon");
		form.addFormItem(toggleLocked, "Locked");

		fieldIdSiswa.setPattern("[0-9]*");

		form.addFormItem(fieldIdSiswa, "Id Siswa (Jangan dirubah)");


		// form.addFormItem(phone, "Telp/HP");

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

	private VerticalLayout createDetails_Otorisasi() {


		gridDetil1 = new Grid<>();
		// gridDetil1.addSelectionListener(event -> event.getFirstSelectedItem()
		// 		.ifPresent(this::showDetails));

		//###### FROM MODEL
		//		dataProviderDetil1 = DataProvider.ofCollection(DummyData.getFUsers());
		// dataProviderDetil1 = DataProvider.ofCollection(new ArrayList<>());
		dataProviderDetil1 = DataProvider.ofCollection( model.mapTempUserRoles.values());

		gridDetil1.setDataProvider(dataProviderDetil1);
		gridDetil1.setHeightFull();

		gridDetil1.addColumn(FUserRoles::getNotes)
				.setAutoWidth(true)
				.setFlexGrow(0)
				.setFrozen(true)
				.setHeader("Nama Otorisasi")
				.setSortable(true);

		gridDetil1.addColumn(new ComponentRenderer<>(this::createStatus_Edit))
				.setAutoWidth(true)
				.setFrozen(true)
				.setHeader("Selected")
				.setTextAlign(ColumnTextAlign.CENTER)
				.setComparator(FUserRoles::isSelected)
				.setSortable(true);

		VerticalLayout form = new VerticalLayout(gridDetil1);
		form.setSizeFull();
		form.setMargin(true);
		form.setSpacing(true);
		form.setAlignItems(FlexComponent.Alignment.CENTER);


		return form;
	}

	private Component createStatus(FUserRoles fUserRoles) {
		Icon icon;
		if (fUserRoles.isSelected()) {
			icon = UIUtils.createPrimaryIcon(VaadinIcon.CHECK);
		}else {
			icon = UIUtils.createDisabledIcon(VaadinIcon.CLOSE);
		}
		icon.addClassNames(LumoStyles.IconSize.S);
		return icon;
	}
	private Component createStatus_Edit(FUserRoles fUserRoles) {
		// Icon icon;
		// if (fUserRoles.isSelected()) {
		// 	icon = UIUtils.createPrimaryIcon(VaadinIcon.CHECK);
		// }else {
		// 	icon = UIUtils.createDisabledIcon(VaadinIcon.CLOSE);
		// }
		// icon.addClassNames(LumoStyles.IconSize.S);
		Checkbox checkSelected = new Checkbox();
		checkSelected.setValue(Boolean.valueOf(fUserRoles.isSelected()));
		checkSelected.addValueChangeListener(e -> {
			fUserRoles.setSelected(e.getValue());
			model.mapTempUserRoles.put(fUserRoles.getRoleID(), fUserRoles);
		});
		return checkSelected;
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

		upload.setMaxFileSize(1500 * 1024);
		upload.setHeight("100px");

		imageOuput = new Image();
		try {
			imageOuput = CommonFileFactory.generateImage(AppPublicService.FILE_PATH + model.currentDomain.getImageName() );
		}catch (Exception ex){}
		imageOuput.setMaxHeight("400px");
		imageOuput.setMaxWidth("300px");

		divImage.removeAll();
		divImage.addComponentAsFirst(imageOuput);

		VerticalLayout form = new VerticalLayout(upload, divImage);

		form.setMargin(true);
		form.setSpacing(true);
		form.setAlignItems(FlexComponent.Alignment.CENTER);

		return form;
	}

	private void updateImageView(SucceededEvent event){
//		UIUtils.showNotification("CREATE AND SHOW MESSAGE");
		isImageChange =true;

		//Agar Photo langsung terlihat
		Component component = UIUtils.createComponentFromFile(event.getMIMEType(),
				event.getFileName(), buffer.getInputStream());

		imageOuput = (Image) component;
		imageOuput.setMaxHeight("400px");
		imageOuput.setMaxWidth("300px");

		divImage.removeAll();
		divImage.addComponentAsFirst(imageOuput);

		footer.setEnabled(true);
	}

	protected void filter() {
		Tab selectedTab = MainLayout.get().getAppBar().getSelectedTab();
		if (selectedTab != null)
//			dataProvider.refreshAll();
			dataProvider.setFilterByValue(FUser::getUserType, EnumUserType.valueOf(selectedTab.getLabel().toUpperCase()));
	}

	String filterText = "";
	public void setFilter(String filterText) {
		Objects.requireNonNull(filterText, "Filter teks tidak boleh kosong");
		if (Objects.equals(this.filterText, filterText.trim())) {
			return;
		}
		this.filterText = filterText.trim().toLowerCase(Locale.ENGLISH);

		dataProvider.setFilter(domain -> passesFilter(domain.getUsername(), this.filterText)
				|| passesFilter(domain.getEmail(), this.filterText)
				|| passesFilter(domain.getFullName(), this.filterText)
				);
	}
	private boolean passesFilter(Object object, String filterText) {
		return object != null && object.toString().toLowerCase(Locale.ENGLISH)
				.contains(filterText);
	}




}
