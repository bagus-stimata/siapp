package com.desgreen.education.siapp.ui.views.master_company;

import com.desgreen.education.siapp.AppPublicService;
import com.desgreen.education.siapp.backend.model.FCompany;
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
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
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
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import com.wontlost.ckeditor.EditorType;
import com.wontlost.ckeditor.VaadinCKEditor;
import com.wontlost.ckeditor.VaadinCKEditorBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Locale;
import java.util.Objects;


@Secured({Role.SYS_, Role.ADMIN, Role.MNU_COMPANY_PUSAT})
@UIScope
@SpringComponent
@Route(value = "CompanyView", layout = MainLayout.class)
@PageTitle("CompanyView")
public class CompanyView extends SplitViewFrame {

	protected CompanyModel model;
	protected CompanyController controller;
	protected CompanyListener listener;

	@Autowired
	private AppPublicService appPublicService;
	@Autowired
	public AuthUserDetailsService authUserDetailsService;

	protected Grid<FCompany> grid;
	protected ListDataProvider<FCompany> dataProvider;

	protected DetailsDrawer detailsDrawer;
	protected DetailsDrawerHeader detailsDrawerHeader;

	// Tabs in the drawer
	Tab tabInfoUmum = new Tab("Info Umum");
 	Tab tabLogoImage = new Tab("Logo");
	Tab tabRegDisclaimer = new Tab("Reg Disclaimer");
	Tabs tabs = new Tabs(tabInfoUmum, tabLogoImage, tabRegDisclaimer );

	private AppBar appBar;
	private Button btnReloadFromDB;
	private Button btnSearchForm;
	private Button btnNewForm;
	private Button btnDeleteDeleteForm;

	protected Binder<FCompany> binder = new BeanValidationBinder<>(FCompany.class);

	protected boolean isImageChange = false;

	public CompanyView() {
		super();
		/**
		 * Init Dummy
		 */
	}

	@PostConstruct
	private void init(){
		model = new CompanyModel(authUserDetailsService, appPublicService);
		controller = new CompanyController(model, this);
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
		binder.readBean(new FCompany());

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
//		dataProvider = DataProvider.ofCollection(DummyData.getFCompanys());
//		dataProvider = DataProvider.ofCollection(new ArrayList<>());
		dataProvider = DataProvider.ofCollection( model.mapHeader.values() );

		grid.setDataProvider(dataProvider);
		grid.setHeightFull();

		grid.addColumn(FCompany::getId)
				.setAutoWidth(true)
				.setFlexGrow(0)
				.setFrozen(true)
				.setHeader("ID")
				.setSortable(true);
		grid.addColumn(new ComponentRenderer<>(this::createDetilInfo))
				.setAutoWidth(true)
				.setHeader("Name")
				.setSortable(true);
		grid.addColumn(new ComponentRenderer<>(this::createActive))
				.setAutoWidth(true)
				.setFlexGrow(0)
				.setHeader("Active")
				.setTextAlign(ColumnTextAlign.END);
		grid.addColumn(new ComponentRenderer<>(this::createCity))
				.setAutoWidth(true)
				.setFlexGrow(0)
				.setHeader("Cities")
				.setTextAlign(ColumnTextAlign.END)
				.setSortable(true);
		grid.addColumn(new ComponentRenderer<>(this::createAddreses))
				.setAutoWidth(true)
				.setFlexGrow(0)
				.setHeader("Addresses")
				.setTextAlign(ColumnTextAlign.END);
//		grid.addColumn(new ComponentRenderer<>(this::createDate)).setFlexGrow(0)
//				.setAutoWidth(true)
//				.setFlexGrow(0)
//				.setHeader("Last Report")
//				.setTextAlign(ColumnTextAlign.END);

		return grid;
	}

	private Component createDetilInfo(FCompany fCompany) {
		ListItem item = new ListItem(
				new Initials(fCompany.getInitials()), fCompany.getDescription(),
				fCompany.getKode1());
		item.setPadding(Vertical.XS);
		item.setSpacing(Right.M);
		return item;
	}

	private Component createActive(FCompany fCompany) {
		Icon icon;
		if (fCompany.isStatActive()) {
			icon = UIUtils.createPrimaryIcon(VaadinIcon.CHECK);
		}else {
			icon = UIUtils.createDisabledIcon(VaadinIcon.CLOSE);
		}
		return icon;
	}

	private Component createCity(FCompany fCompany) {
		return UIUtils.createBoldLabel(fCompany.getCity());
	}

	private Component createAddreses(FCompany fCompany) {
		String address2 = fCompany.getAddress2();
		if (! fCompany.getPhoneNumber().equals("")) {
			address2 += ", Telp. " + fCompany.getPhoneNumber();
		}
		ListItem item = new ListItem( fCompany.getAddress1(),
				address2);
		item.setPadding(Vertical.XS);
		item.setSpacing(Right.M);

		return item;
	}

	private Component createDate(FCompany fCompany) {
		return new Span(UIUtils.formatDate(CommonDateFormat.fromDate(fCompany.getModified())));
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
		} else if (selectedTab.equals(tabRegDisclaimer)) {
			detailsDrawer.setContent(createDetails_RegDisclaimer());
		}

	}

	protected void showDetails(FCompany fCompany) {
		if (fCompany == null) {
			fCompany = new FCompany();
		}
//		delete.setVisible(!product.isNewProduct());
		model.currentDomain = fCompany;

		final FCompany tempOldItemHeader = fCompany;
		model.oldDomain = tempOldItemHeader;
		isImageChange= false;

		try {
			binder.readBean(fCompany);
		}catch (Exception ex) {ex.printStackTrace();}

		//delete.setVisible(!product.isNewProduct());

		detailsDrawerHeader.setTitle(fCompany.getDescription());
//		detailsDrawer.setContent(createDetails(fCompany));
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
	private final TextField kode2 = new TextField();
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

	private FormLayout createDetails_InfoUmum() {
//		System.out.println(fCompany.getAddress1());

		/**
		 * Re Binding Datata
		 */
		binder.bindInstanceFields(this);
		binder.forField(checkStatusActive)
				.bind(FCompany::isStatActive, FCompany::setStatActive);

		/**
		 * CKEditor Binder
		 */
		binder.forField(ckEditorDisclaimer)
				.bind(FCompany::getRegDisclaimer, FCompany::setRegDisclaimer);

		/**
		 * Decorate the Form View
		 */
		kode1.setWidthFull();
		kode2.setWidthFull();
		description.setWidthFull();
		address1.setWidthFull();
		address2.setWidthFull();
		city.setWidthFull();
		email.setWidthFull();
		phoneNumber.setWidthFull();
		webProfile.setWidthFull();
		webAplikasi.setWidthFull();

		phoneNumber.setPrefixComponent(new Span("+62"));


		checkStatusActive.setLabel("Active");
//		checkStatusActive.setValue(true);

		radioButtonStatusActive.addThemeVariants(RadioGroupVariant.MATERIAL_VERTICAL);
		radioButtonStatusActive.setItems("Active", "Non-Active");
//		radioButtonStatusActive.setValue("Active");

		kode1.setValueChangeMode(ValueChangeMode.EAGER);
		kode2.setValueChangeMode(ValueChangeMode.EAGER);
//		checkStatusActive.setValueChangeMode(ValueChangeMode.EAGER); //Otomatis EAGER
		description.setValueChangeMode(ValueChangeMode.EAGER);
		address1.setValueChangeMode(ValueChangeMode.EAGER);
		address2.setValueChangeMode(ValueChangeMode.EAGER);
		city.setValueChangeMode(ValueChangeMode.EAGER);
		email.setValueChangeMode(ValueChangeMode.EAGER);
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
		form.addFormItem(kode2, "Judul Aplikasi");
//		form.addFormItem(lastName, "Last Name");
		FormLayout.FormItem descriptionItem = form.addFormItem(description, "Nama Sekolah/Pondok");

//		FormLayout.FormItem statusItem = form.addFormItem(radioButtonStatusActive, "Status");
		FormLayout.FormItem checkStatusItem = form.addFormItem(checkStatusActive, "Status");
		FormLayout.FormItem address1Item = form.addFormItem(address1, "Address1");
		FormLayout.FormItem address2Item = form.addFormItem(address2, "Address2");
		FormLayout.FormItem cityItem = form.addFormItem(city, "City");

		FormLayout.FormItem phoneItem = form.addFormItem(phoneNumber, "Phone");
		FormLayout.FormItem emailItem = form.addFormItem(email, "Email");
		FormLayout.FormItem webProfileItem = form.addFormItem(webProfile, "Alamat Web Profile");
		FormLayout.FormItem webAplikasiItem = form.addFormItem(webAplikasi, "Alamat Aplikasi");

//		FormLayout.FormItem comboCompanyItem = form.addFormItem(company, "Company");
//		FormLayout.FormItem uploadItem = form.addFormItem(new Upload(),
//				"Image");

		UIUtils.setColSpan(2, descriptionItem, checkStatusItem, address1Item, address2Item, cityItem, phoneItem, emailItem);



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


	protected Image imageOuput = new Image();
	protected MemoryBuffer buffer = new MemoryBuffer();
	protected Upload upload = new Upload(buffer);
	Div divImage = new Div();
	private VerticalLayout createDetails_Photo() {

		upload.setAcceptedFileTypes("image/jpeg", "image/png", "image/gif", "image/jpg");
		upload.setMaxFiles(1);
		upload.setAutoUpload(true); //ini bagian penting
		upload.setSizeFull();

		upload.setMaxFileSize(3 * 1500 * 1024);
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

	protected VaadinCKEditor ckEditorDisclaimer = new VaadinCKEditorBuilder().with(builder->{
		builder.editorType= EditorType.DECOUPLED;
		builder.editorData="Dcoupled Editor";
	}).createVaadinCKEditor();
	private FlexBoxLayout createDetails_RegDisclaimer() {

		/**Document Editor*/
//		ckEditorDisclaimer = new VaadinCKEditorBuilder().with(builder->{
//			builder.editorType= EditorType.DECOUPLED;
//			builder.editorData="Dcoupled Editor";
//		}).createVaadinCKEditor();

		/**Set Editor Content*/
//		ckEditorDisclaimer.setValue("New Content");
//		decoupledEditor.updateValue();
//		decoupledEditor.doSetUpdate("New Content");

		FlexBoxLayout form = new FlexBoxLayout(ckEditorDisclaimer);

//		form.setMargin(true);
//		form.setSpacing(true);
		form.setFlexDirection(FlexLayout.FlexDirection.ROW);
//		form.setMargin(Horizontal.AUTO); //ini yang bikin memanjang bos
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
//		dataProvider.setFilterByValue(FCompany::getRole, FCompany.Role.ACCOUNTANT);
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
				|| passesFilter(domain.getAddress1(), this.filterText)
				|| passesFilter(domain.getCity(), this.filterText));
	}
	private boolean passesFilter(Object object, String filterText) {
		return object != null && object.toString().toLowerCase(Locale.ENGLISH)
				.contains(filterText);
	}


}


