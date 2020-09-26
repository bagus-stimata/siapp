package com.desgreen.education.siapp.ui.views.master_division;

import com.desgreen.education.siapp.AppPublicService;
import com.desgreen.education.siapp.backend.model.FCompany;
import com.desgreen.education.siapp.backend.model.FDivision;
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
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.radiobutton.RadioGroupVariant;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.SucceededEvent;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.StringToBigDecimalConverter;
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


@Secured({Role.ADMIN, Role.MNU_DIVISION_CABANG})
@UIScope
@SpringComponent
@Route(value = "DivisionView", layout = MainLayout.class)
@PageTitle("DivisionView")
public class DivisionView extends SplitViewFrame {

	protected DivisionModel model;
	protected DivisionController controller;
	protected DivisionListener listener;

	@Autowired
	private AppPublicService appPublicService;
	@Autowired
	public AuthUserDetailsService authUserDetailsService;

	protected Grid<FDivision> grid;
	protected ListDataProvider<FDivision> dataProvider;

	protected DetailsDrawer detailsDrawer;
	protected DetailsDrawerHeader detailsDrawerHeader;

	// Tabs in the drawer
	Tab tabInfoUmum = new Tab("Info Umum");
	Tab tabLogoImage = new Tab("Upload Logo");
	Tabs tabs = new Tabs(tabInfoUmum, tabLogoImage );

	private AppBar appBar;
	private Button btnReloadFromDB;
	private Button btnSearchForm;
	private Button btnNewForm;
	private Button btnDeleteDeleteForm;


	protected Binder<FDivision> binder = new BeanValidationBinder<>(FDivision.class);
	protected boolean isImageChange = false;


	public DivisionView() {
		super();
		/**
		 * Init Dummy
		 */
	}
	@PostConstruct
	private void init(){
		model = new DivisionModel(authUserDetailsService, appPublicService);
		controller = new DivisionController(model, this);
//		model = controller.model;

		listener = controller;


	}

	@Override
	protected void onAttach(AttachEvent attachEvent) {
		super.onAttach(attachEvent);
		initAppBar();
		setViewContent(createContent());
		setViewDetails(createDetailsDrawer());

		filter();
		binder.readBean(new FDivision());

		detailsDrawer.setContent(createDetails_InfoUmum());

	}


	private void initAppBar() {
		appBar = MainLayout.get().getAppBar();
		appBar.searchModeOff();

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
//		dataProvider = DataProvider.ofCollection(DummyData.getFDivisions());
//		dataProvider = DataProvider.ofCollection(new ArrayList<>());
		dataProvider = DataProvider.ofCollection( model.mapHeader.values() );
		
		grid.setDataProvider(dataProvider);
		grid.setHeightFull();

		grid.addColumn(FDivision::getId)
				.setAutoWidth(true)
				.setFlexGrow(0)
				.setFrozen(true)
				.setHeader("ID")
				.setSortable(true);
		grid.addColumn(new ComponentRenderer<>(this::createDetilInfo))
				.setAutoWidth(true)
				.setHeader("Name")
				.setComparator(FDivision::getDescription)
				.setSortable(true);
		grid.addColumn(new ComponentRenderer<>(this::createActive))
				.setAutoWidth(true)
				.setFlexGrow(0)
				.setHeader("Active")
				.setTextAlign(ColumnTextAlign.END);
		grid.addColumn(new ComponentRenderer<>(this::createCompany))
				.setAutoWidth(true)
				.setFlexGrow(0)
				.setHeader("Company")
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

	private Component createDetilInfo(FDivision fDivision) {
		ListItem item = new ListItem(
				new Initials(fDivision.getKode1()), fDivision.getDescription(),
				"");
		item.setPadding(Vertical.XS);
		item.setSpacing(Right.M);
		return item;
	}

	private Component createActive(FDivision fDivision) {
		Icon icon;
		if (fDivision.isStatActive()) {
			icon = UIUtils.createPrimaryIcon(VaadinIcon.CHECK);
		}else {
			icon = UIUtils.createDisabledIcon(VaadinIcon.CLOSE);
		}
		return icon;
	}

	private Component createCompany(FDivision fDivision) {
		return UIUtils.createBoldLabel(fDivision.getFcompanyBean()!=null? fDivision.getFcompanyBean().getDescription(): "");
	}

	private Component createAddresesX(FDivision fDivision) {
		// String address2 = fDivision.getAddress2();
		// if (! fDivision.getPhoneNumber().equals("")) {
		// 	address2 += ", Telp. " + fDivision.getPhoneNumber();
		// }
		// ListItem item = new ListItem( fDivision.getAddress1(),
		// 		address2);
		// item.setPadding(Vertical.XS);
		// item.setSpacing(Right.M);
		// return item;
		return null;
	}

	private Component createDate(FDivision fDivision) {
		return new Span(UIUtils.formatDate(CommonDateFormat.fromDate(fDivision.getModified())));
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
		} else if (selectedTab.equals(tabLogoImage)) {
			detailsDrawer.setContent(createDetails_Photo());

		}
	}

	protected void showDetails(FDivision fDivision) {
		if (fDivision == null) {
			fDivision = new FDivision();
		}
//		delete.setVisible(!product.isNewProduct());
		model.currentDomain = fDivision;
		final FDivision tempOldItemHeader = fDivision;
		model.oldDomain = tempOldItemHeader;
		isImageChange= false;

		try {
			binder.readBean(fDivision);
		}catch (Exception ex) {ex.printStackTrace();}

		//delete.setVisible(!product.isNewProduct());

		detailsDrawerHeader.setTitle(fDivision.getDescription());
//		detailsDrawer.setContent(createDetails(fDivision));
		/**
		 * Yang lainnya, ketika dimainkan binder sudah bisa update
		 * untuk case ini di lakukan karena binder nya manual
		 */
		Tab selectedTab = tabs.getSelectedTab();
		if (selectedTab.equals(tabLogoImage)) {
			detailsDrawer.setContent(createDetails_Photo());
		}

		detailsDrawer.show();

	}


	/**
	 * FORM COMPONENT
	 * dipanggil sekali saja
	 */
	private final TextField kode1 = new TextField();
	private final TextField description = new TextField();
	private final TextField address1 = new TextField();
	private final TextField address2 = new TextField();
	private final TextField city = new TextField();
	private final TextField email = new TextField();
	private final TextField phoneNumber = new TextField();
	private final TextField webProfile = new TextField();
	private final TextField webAplikasi = new TextField();

	private final RadioButtonGroup<String> radioButtonStatusActive = new RadioButtonGroup<>();
	private final Checkbox checkStatusActive = new Checkbox();

	private final ComboBox<FCompany> comboFCompany = new ComboBox<FCompany>();
	private final ToggleButton toggleStatActive = new ToggleButton();


//	private FormLayout createDetails(FDivision fDivision) {
	private FormLayout createDetails_InfoUmum() {
//		System.out.println(fDivision.getAddress1());

		/**
		 * Re Binding Datata
		 */
		binder.bindInstanceFields(this);
		binder.forField(kode1).asRequired().bind(FDivision::getKode1, FDivision::setKode1);
		binder.forField(description).asRequired().bind(FDivision::getDescription, FDivision::setDescription);
//		binder.forField(radioButtonStatusActive)
//				.withConverter(new StringToBooleanConverter("", "Active", "Non-Active"))
//				.bind(FDivision::isStatActive, FDivision::setStatActive);
		binder.forField(comboFCompany)
			.asRequired("tidak boleh kosong")
			.bind(FDivision::getFcompanyBean, FDivision::setFcompanyBean);
		binder.forField(toggleStatActive)
				.bind(FDivision::isStatActive, FDivision::setStatActive);

		/**
		 * Decorate the Form View
		 */
		kode1.setWidthFull();
		description.setWidthFull();
		address1.setWidthFull();
		address2.setWidthFull();
		city.setWidthFull();
		email.setWidthFull();
		phoneNumber.setWidthFull();
		webProfile.setWidthFull();
		webAplikasi.setWidthFull();


		phoneNumber.setPrefixComponent(new Span("+62"));


		// checkStatusActive.setLabel("Active");
//		checkStatusActive.setValue(true);
		toggleStatActive.setLabel("Active");
		toggleStatActive.addValueChangeListener(e -> {
			if (e.getValue()==true) {
				toggleStatActive.setLabel("Active");
			}else {
				toggleStatActive.setLabel("Non-active");
			}
		});


		radioButtonStatusActive.addThemeVariants(RadioGroupVariant.MATERIAL_VERTICAL);
		radioButtonStatusActive.setItems("Active", "Non-Active");
//		radioButtonStatusActive.setValue("Active");

		comboFCompany.setItems(model.listFCompany);
		comboFCompany.setWidthFull();
		comboFCompany.setItemLabelGenerator(p -> p.getDescription());

//		ComboBox company = new ComboBox();
//		company.setItems(DummyData.getCompanies());
//		company.setValue(DummyData.getDivision());
//		company.setWidthFull();

		kode1.setValueChangeMode(ValueChangeMode.EAGER);
//		checkStatusActive.setValueChangeMode(ValueChangeMode.EAGER); //Otomatis EAGER
		description.setValueChangeMode(ValueChangeMode.EAGER);
		address1.setValueChangeMode(ValueChangeMode.EAGER);
		address2.setValueChangeMode(ValueChangeMode.EAGER);
		city.setValueChangeMode(ValueChangeMode.EAGER);
		email.setValueChangeMode(ValueChangeMode.EAGER);
		phoneNumber.setValueChangeMode(ValueChangeMode.EAGER);
		phoneNumber.setValueChangeMode(ValueChangeMode.EAGER);
		webProfile.setValueChangeMode(ValueChangeMode.EAGER);
		webAplikasi.setValueChangeMode(ValueChangeMode.EAGER);

		// Form layout
		FormLayout form = new FormLayout();
		form.addClassNames(LumoStyles.Padding.Bottom.L,
				LumoStyles.Padding.Horizontal.L, LumoStyles.Padding.Top.S);
		form.setResponsiveSteps(
				new FormLayout.ResponsiveStep("0", 1,
						FormLayout.ResponsiveStep.LabelsPosition.TOP),
				new FormLayout.ResponsiveStep("21em", 2,
						FormLayout.ResponsiveStep.LabelsPosition.TOP));

		form.addFormItem(kode1, "Kode1");
//		form.addFormItem(lastName, "Last Name");
		FormLayout.FormItem descriptionItem = form.addFormItem(description, "Nama Sekolah/Pondok");

		FormLayout.FormItem toggleStatusItem = form.addFormItem(toggleStatActive, "Status");

		FormLayout.FormItem comboFCompanyItem = form.addFormItem(comboFCompany, "Company");

		FormLayout.FormItem phoneItem = form.addFormItem(phoneNumber, "Phone");
		FormLayout.FormItem webProfileItem = form.addFormItem(webProfile, "Alamat Web Profile");
		FormLayout.FormItem webAplikasiItem = form.addFormItem(webAplikasi, "Alamat Aplikasi");

		UIUtils.setColSpan(2, descriptionItem, toggleStatusItem);



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



	protected Image imageOuput = new Image();
	protected MemoryBuffer buffer = new MemoryBuffer();
	protected Upload upload = new Upload(buffer);
	Div divImage = new Div();
	private VerticalLayout createDetails_Photo() {

		upload.setAcceptedFileTypes("image/jpeg", "image/png", "image/gif", "image/jpg");
		upload.setMaxFiles(1);
		upload.setAutoUpload(true); //ini bagian penting
		upload.setSizeFull();

		upload.setMaxFileSize( 3* 1500 * 1024);
		upload.setHeight("100px");

		imageOuput = new Image();
		try {
			imageOuput = CommonFileFactory.generateImage(AppPublicService.FILE_PATH + model.currentDomain.getLogoImage());
		}catch (Exception ex){}
		int newWidth = 300;
		int newHeight = 400;
		try {
			BufferedImage bufferedImage = ImageIO.read(new File(AppPublicService.FILE_PATH + model.currentDomain.getLogoImage()));
			newHeight = CommonImageFactory.getMaxScaleHeight(bufferedImage.getWidth(), bufferedImage.getHeight(), newWidth);
		}catch (Exception ex){
			ex.printStackTrace();
		}
		imageOuput.setWidth(newWidth, Unit.PIXELS);
		imageOuput.setHeight(newHeight, Unit.PIXELS);

//		System.out.println("Hello tes: " + model.currentDomain.getLogoImage() + " >> " + imageOuput.getSrc());

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
	}



	private void filter() {
		// dataProvider.setFilterByValue(FDivision::getRole, FDivision.Role.ACCOUNTANT);
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
