package com.desgreen.education.siapp.ui.views.ppdb_online;

import com.desgreen.education.siapp.AppPublicService;
import com.desgreen.education.siapp.backend.model.EnumStatSiswa;
import com.desgreen.education.siapp.backend.model.FKurikulum;
import com.desgreen.education.siapp.backend.model.FSiswa;
import com.desgreen.education.siapp.backend.model_sample.Person;
import com.desgreen.education.siapp.security_config.AuthUserDetailsService;
import com.desgreen.education.siapp.security_model.FUser;
import com.desgreen.education.siapp.ui.MainLayout;
import com.desgreen.education.siapp.ui.components.FlexBoxLayout;
import com.desgreen.education.siapp.ui.components.ListItem;
import com.desgreen.education.siapp.ui.components.detailsdrawer.DetailsDrawer;
import com.desgreen.education.siapp.ui.components.detailsdrawer.DetailsDrawerHeader;
import com.desgreen.education.siapp.ui.components.navigation.bar.AppBar;
import com.desgreen.education.siapp.ui.components.navigation.drawer.NaviDrawer;
import com.desgreen.education.siapp.ui.layout.size.*;
import com.desgreen.education.siapp.ui.util.BoxShadowBorders;
import com.desgreen.education.siapp.ui.util.LumoStyles;
import com.desgreen.education.siapp.ui.util.UIUtils;
import com.desgreen.education.siapp.ui.views.Home;
import com.desgreen.education.siapp.ui.views.SplitViewFrame;
import com.desgreen.education.siapp.ui.utils.common.CommonFileFactory;
import com.vaadin.componentfactory.ToggleButton;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.radiobutton.RadioGroupVariant;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.component.upload.SucceededEvent;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.Locale;

@UIScope
@SpringComponent
@Route(value = "PpdbOnlineView", layout = MainLayout.class)
@PageTitle("PpdbOnlineView")
public class PpdbOnlineView extends SplitViewFrame implements HasUrlParameter<Long> {

	protected PpdbOnlineModel model;
	protected PpdbOnlineController controller;
	protected PpdbOnlineListener listener;

	protected Button btnSubmit = new Button("Kirim", new Icon(VaadinIcon.PAPERPLANE));

	private Grid<Person> grid;
	private ListDataProvider<Person> dataProvider;

	protected AppBar appBar;
	protected NaviDrawer naviDrawer;
	protected DetailsDrawer detailsDrawer;
	protected DetailsDrawerHeader detailsDrawerHeader;

	private ListItem availability;
	private ListItem bankAccount;
	private ListItem updated;

	@Autowired
	private AppPublicService appPublicService;
	@Autowired
	public AuthUserDetailsService authUserDetailsService;


	protected Binder<FUser> binderUser = new BeanValidationBinder<>(FUser.class);
	protected FUser currentUser = new FUser();
	protected Binder<FSiswa> binder = new BeanValidationBinder<>(FSiswa.class);
	protected FSiswa currentSiswa = new FSiswa();
	protected FKurikulum currentKurikulum = new FKurikulum();

	protected boolean isImageChange = false;

	public PpdbOnlineView() {
		setViewContent(createContent());
	}

	@Override
	protected void onAttach(AttachEvent attachEvent) {
		super.onAttach(attachEvent);

		initAppBar();
		naviDrawer = MainLayout.get().getNaviDrawer();
		naviDrawer.setVisible(false);

	}
	@PostConstruct
	private void init(){
		model = new PpdbOnlineModel(authUserDetailsService, appPublicService);
		controller = new PpdbOnlineController(model, this);
//		model = controller.model;

		listener = controller;

//		initAppBar();
//		setViewContent(createContent());
//		setViewDetails(createDetailsDrawer());
//		filter();

	}


	@Override
	public void setParameter(BeforeEvent beforeEvent, Long paramId) {
		currentKurikulum = model.fKurikulumJPARepository.findById(paramId).get();
	}

	private Component createContent() {
		setBinderModel();
		Icon iconUser = new Icon(VaadinIcon.USER);
		Icon  iconHealt= new Icon(VaadinIcon.STETHOSCOPE);
		FlexBoxLayout content = new FlexBoxLayout(
				createDetails_Header(iconUser, "User Baru"), createDetails0(),
				createDetails_Header("Data Siswa"), createDetails1(),
				createDetails_Header("Photo (Wajib Diisi)"), createDetails_Photo(),
				createDetails_Header(iconHealt, "Riwayat Kesehatan"), createDetails2() ,
				createDetails_Header("Orang Tua Siswa"), createDetails3());


//		content.setBoxSizing(BoxSizing.BORDER_BOX);
//		content.setHeightFull();
//		content.setPadding(Horizontal.RESPONSIVE_X, Top.RESPONSIVE_X);
		content.setFlexDirection(FlexLayout.FlexDirection.COLUMN);
		content.setMargin(Horizontal.AUTO, Vertical.RESPONSIVE_L);

		return content;
	}


	private AppBar initAppBar() {
		appBar = MainLayout.get().getAppBar();
		appBar.setNaviMode(AppBar.NaviMode.MENU);
//		appBar.getContextIcon().addClickListener(e -> UI.getCurrent().navigate(Accounts.class));
		appBar.setTitle( "PPDB ONLINE #" + AppPublicService.APP_DESC2 );

		try {
			appBar.setTitle("PPDB ONLINE #" + currentKurikulum.getFdivisionBean().getFcompanyBean().getDescription() +
					" #" + currentKurikulum.getFmatPelBean().getDescription());
		}catch (Exception ex) {}

		return appBar;
	}


	/**
	 * FORM COMPONENT
	 * dipanggil sekali saja
	 */
	private final TextField nis = new TextField();
	private final TextField fullName = new TextField();
	private final TextField nickName = new TextField();
	private final TextField birthPlace = new TextField();
	private final DatePicker birthDate = new DatePicker();

//	private final Checkbox checkSex = new Checkbox();
//	private final Checkbox checkMenikah = new Checkbox();
	private final ToggleButton toggleSex = new ToggleButton();
	private final ToggleButton toggleMenikah = new ToggleButton();

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

	private void setBinderModel(){

		if (currentSiswa == null) {
			currentSiswa = new FSiswa();
		}
		if (currentUser == null){
			currentUser = new FUser();
		}

		try {

			currentSiswa.setStatSiswa(EnumStatSiswa.PPDB);
			currentSiswa.setRegDate(LocalDate.now());
			currentSiswa.setBirthDate(LocalDate.now());

			binderUser.readBean(currentUser);
			binder.readBean(currentSiswa);

		}catch (Exception ex) {ex.printStackTrace();}

		/**
		 * Khusus Binder User
		 */
		binderUser.forField(username).asRequired("tidak boleh kosong")
				.withValidator(new EmailValidator("contoh. masjohn@gmail.com"))
				.withValidator(value -> isEmailValid(value), "Email sudah terpakai. Gunakan email lain..!!")
				.bind(FUser::getUsername, FUser::setUsername);
//		binderUser.forField(email)
//				.withValidator(new EmailValidator("contoh. masjohn@gmail.com"))
//				.bind(FUser::getEmail, FUser::setEmail);
		binderUser.forField(password).asRequired("tidak boleh kosong")
				.bind(FUser::getPassword, FUser::setPassword);


		/**
		 * Re Binding Data
		 * Karena menggunakan dua
		 */
//		binder.bindInstanceFields(this);

		binder.forField(fullName).asRequired()
				.bind(FSiswa::getFullName, FSiswa::setFullName);
		binder.forField(birthPlace).asRequired()
				.bind(FSiswa::getBirthPlace, FSiswa::setBirthPlace);
		binder.forField(birthDate).asRequired()
				.bind(FSiswa::getBirthDate, FSiswa::setBirthDate);

		binder.forField(address1).asRequired()
				.bind(FSiswa::getAddress1, FSiswa::setAddress1);
		binder.forField(address2).asRequired()
				.bind(FSiswa::getAddress2, FSiswa::setAddress2);
		binder.forField(address3).asRequired()
				.bind(FSiswa::getAddress3, FSiswa::setAddress3);
		binder.forField(city).asRequired()
				.bind(FSiswa::getCity, FSiswa::setCity);
		binder.forField(state).asRequired()
				.bind(FSiswa::getState, FSiswa::setState);
		binder.forField(phone).asRequired()
				.bind(FSiswa::getPhone, FSiswa::setPhone);


		binder.forField(fatherName).asRequired()
				.bind(FSiswa::getFatherName, FSiswa::setFatherName);
		binder.forField(fatherJob).asRequired()
				.bind(FSiswa::getFatherJob, FSiswa::setFatherJob);
		binder.forField(fatherPhone).asRequired()
				.bind(FSiswa::getFatherPhone, FSiswa::setFatherPhone);
		binder.forField(motherName).asRequired()
				.bind(FSiswa::getMotherName, FSiswa::setMotherName);
		binder.forField(motherJob).asRequired()
				.bind(FSiswa::getMotherJob, FSiswa::setMotherJob);
		binder.forField(parAddress1).asRequired()
				.bind(FSiswa::getParAddress1, FSiswa::setParAddress1);
		binder.forField(parCity).asRequired()
				.bind(FSiswa::getParCity, FSiswa::setParCity);
		binder.forField(parState).asRequired()
				.bind(FSiswa::getParState, FSiswa::setParState);


		binder.forField(notes1).asRequired()
				.bind(FSiswa::getNotes1, FSiswa::setNotes1);
		binder.forField(notes2).asRequired()
				.bind(FSiswa::getNotes2, FSiswa::setNotes2);

		binder.forField(fieldAnakKe_)
				.withConverter(new StringToIntegerConverter("Harus Angka"))
				.bind(FSiswa::getAnakKe, FSiswa::setAnakKe);
		binder.forField(fieldDariKe_)
				.withConverter(new StringToIntegerConverter("Harus Angka"))
				.bind(FSiswa::getDariKe, FSiswa::setDariKe);

		binder.forField(toggleSex)
				.bind(FSiswa::isSex, FSiswa::setSex);
		binder.forField(toggleMenikah)
				.bind(FSiswa::isMenikah, FSiswa::setMenikah);

		binder.forField(comboStatSiswa)
				.bind(FSiswa::getStatSiswa, FSiswa::setStatSiswa);
	}
	private boolean isEmailValid(String emailValue){
		return ! model.isCekEmailAda(emailValue);
	}
	private Component createDetails_Header(String strTitle) {
		Label title = UIUtils.createH3Label(strTitle);
		FlexBoxLayout header = new FlexBoxLayout(title);
		header.setAlignItems(FlexComponent.Alignment.CENTER);
		header.setMargin(Bottom.M, Horizontal.RESPONSIVE_L, Top.L);
		return header;
	}
	private Component createDetails_Header(Icon icon, String strTitle) {
		Label title = UIUtils.createH3Label(strTitle);
		icon.setColor(LumoStyles.Color.Primary._100);
		icon.setSize(LumoStyles.IconSize.S);
		FlexBoxLayout header = new FlexBoxLayout(icon, title);
		header.setAlignItems(FlexComponent.Alignment.CENTER);
		header.setMargin(Bottom.M, Horizontal.RESPONSIVE_L, Top.L);
		header.setSpacing();
		return header;
	}

	private FlexBoxLayout createUserAccount() {
		// Form layout
		FormLayout form = new FormLayout();
		form.addClassNames(LumoStyles.Padding.Bottom.L,
				LumoStyles.Padding.Horizontal.L, LumoStyles.Padding.Top.S);

		form.addFormItem(fatherName, "Nama Bapak");
		form.addFormItem(fatherJob, "Pekerjaan");

		FlexBoxLayout content = new FlexBoxLayout(form);
		content.addClassName(BoxShadowBorders.BOTTOM);
		content.setAlignItems(FlexComponent.Alignment.CENTER);
		content.setFlexWrap(FlexLayout.FlexWrap.WRAP);
		content.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
		content.setPadding(Bottom.L);

		return content;
	}

	protected final TextField username = new TextField();
	protected final TextField email = new TextField();
	private final PasswordField password = new PasswordField();
	private FlexBoxLayout createDetails0() {
		// Form layout
		FormLayout form = new FormLayout();
		form.addClassNames(LumoStyles.Padding.Bottom.L,
				LumoStyles.Padding.Horizontal.L, LumoStyles.Padding.Top.S);

//		form.addClassNames(LumoStyles.Padding.Bottom.L,
//				LumoStyles.Padding.Horizontal.L, LumoStyles.Padding.Top.S);
//		form.setResponsiveSteps(
//				new FormLayout.ResponsiveStep("0", 1,
//						FormLayout.ResponsiveStep.LabelsPosition.ASIDE),
//				new FormLayout.ResponsiveStep("21em", 1,
//						FormLayout.ResponsiveStep.LabelsPosition.ASIDE));
		form.setResponsiveSteps(
				new FormLayout.ResponsiveStep("21em", 1,
						FormLayout.ResponsiveStep.LabelsPosition.ASIDE));

//		form.addFormItem(username, "Email Aktif"); //Kita hanya akan menggunakan email
////		form.addFormItem(email, "email"); //User Name adalah email
//		form.addFormItem(password, "Password");
		FormLayout.FormItem comboUsername = form.addFormItem(username, "Email Aktif");
		FormLayout.FormItem comboPassword = form.addFormItem(password, "Password");

		username.addBlurListener(e -> {
			binderUser.validate();
		});

		username.setWidth("220px");
		email.setWidth("220px");
		password.setWidth("200px");

		/**
		 * dilakukan validate manual saat on blur username
		 */
//		username.setValueChangeMode(ValueChangeMode.EAGER);
//		email.setValueChangeMode(ValueChangeMode.EAGER);
//		password.setValueChangeMode(ValueChangeMode.EAGER);

		FlexBoxLayout content = new FlexBoxLayout(form);
		content.addClassName(BoxShadowBorders.BOTTOM);
		content.setAlignItems(FlexComponent.Alignment.CENTER);
		content.setFlexWrap(FlexLayout.FlexWrap.WRAP);
		content.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
		content.setPadding(Bottom.L);

//		UIUtils.setColSpan(2, username, password); //2 Kolom dijadikan satu

		return content;
	}

	private FlexBoxLayout createDetails1() {

		/**
		 * Decorate the Form View
		 */
		fullName.setWidthFull();
		nickName.setWidth("150px");
		address1.setWidthFull();
		email.setWidthFull();

		comboStatSiswa.setItems(EnumStatSiswa.values());
		comboStatSiswa.setValue(EnumStatSiswa.OTH1);

		fatherName.setWidthFull();
		motherName.setWidthFull();

		parAddress1.setWidthFull();

		notes1.setWidthFull();
		notes2.setWidthFull();

		phone.setPrefixComponent(new Span("+62"));
		fatherPhone.setPrefixComponent(new Span("+62"));
		motherPhone.setPrefixComponent(new Span("+62"));


		checkStatusActive.setLabel("Aktif");

		birthDate.setClearButtonVisible(true);
		birthDate.setLocale(Locale.ITALIAN);

		radioButtonStatusActive.addThemeVariants(RadioGroupVariant.MATERIAL_VERTICAL);
		radioButtonStatusActive.setItems("Active", "Non-Active");


		nis.setValueChangeMode(ValueChangeMode.EAGER);
//		checkStatusActive.setValueChangeMode(ValueChangeMode.EAGER); //Otomatis EAGER
		fullName.setValueChangeMode(ValueChangeMode.EAGER);
		address1.setValueChangeMode(ValueChangeMode.EAGER);
		address2.setValueChangeMode(ValueChangeMode.EAGER);
		city.setValueChangeMode(ValueChangeMode.EAGER);
		phone.setValueChangeMode(ValueChangeMode.EAGER);

		// Form layout
		FormLayout form = new FormLayout();
		form.addClassNames(LumoStyles.Padding.Bottom.L,
				LumoStyles.Padding.Horizontal.L, LumoStyles.Padding.Top.S);

//		FormLayout.FormItem nisItem = form.addFormItem(nis, "NIS");

		FormLayout.FormItem fullNameItem = form.addFormItem(fullName, "Nama Siswa/Siswi");
		FormLayout.FormItem nickNameItem = form.addFormItem(nickName, "Nama Panggilan");

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
//		form.addFormItem(email, "Email");

		FlexBoxLayout content = new FlexBoxLayout(form);
		content.addClassName(BoxShadowBorders.BOTTOM);
		content.setAlignItems(FlexComponent.Alignment.CENTER);
		content.setFlexWrap(FlexLayout.FlexWrap.WRAP);
		content.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
		content.setPadding(Bottom.L);

		return content;
	}

	private FlexBoxLayout createDetails2() {
		/**
		 * Decorate the Form View
		 */
		notes1.setWidthFull();
		notes2.setWidthFull();

		// Form layout
		FormLayout form = new FormLayout();
		form.addClassNames(LumoStyles.Padding.Bottom.L,
				LumoStyles.Padding.Horizontal.L, LumoStyles.Padding.Top.S);

		form.addFormItem(notes1, "Penyakit Bawaan");
		form.addFormItem(notes2, "Alergi Obat");

		FlexBoxLayout content = new FlexBoxLayout(form);
		content.addClassName(BoxShadowBorders.BOTTOM);
		content.setAlignItems(FlexComponent.Alignment.CENTER);
		content.setFlexWrap(FlexLayout.FlexWrap.WRAP);
		content.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
		content.setPadding(Bottom.L);

		return content;
	}



	private FlexBoxLayout createDetails3() {

		phone.setPrefixComponent(new Span("+62"));
		fatherPhone.setPrefixComponent(new Span("+62"));
		motherPhone.setPrefixComponent(new Span("+62"));


		// Form layout
		FormLayout form = new FormLayout();
		form.addClassNames(LumoStyles.Padding.Bottom.L,
				LumoStyles.Padding.Horizontal.L, LumoStyles.Padding.Top.S);

		form.addFormItem(fatherName, "Nama Bapak");
		form.addFormItem(fatherJob, "Pekerjaan Ayah");
		form.addFormItem(fatherPhone, "Telp/HP");

		form.addFormItem(motherName, "Nama Ibu");
		form.addFormItem(motherJob, "Pekerjaan Ibu");
		form.addFormItem(motherPhone, "Telp/HP");

		form.addFormItem(parAddress1, "Alamat");
		form.addFormItem(parCity, "Kota");
		form.addFormItem(parState, "Propinsi");




		btnSubmit.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		btnSubmit.setWidth("150px");
		btnSubmit.setIconAfterText(true);
		form.addFormItem(btnSubmit, "");

		btnSubmit.addClickListener(e -> listener.aksiBtnSaveForm());


		FlexBoxLayout content = new FlexBoxLayout(form);
		content.addClassName(BoxShadowBorders.BOTTOM);
		content.setAlignItems(FlexComponent.Alignment.CENTER);
		content.setFlexWrap(FlexLayout.FlexWrap.WRAP);
		content.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
		content.setPadding(Bottom.L);

		return content;
	}


	protected Image imageOuput = new Image();
	protected MemoryBuffer buffer = new MemoryBuffer();
	protected Upload upload = new Upload(buffer);
	Div divImage = new Div();
	private FlexLayout createDetails_Photo() {

		upload.setAcceptedFileTypes("image/jpeg", "image/png", "image/gif", "image/jpg");
		upload.setMaxFiles(1);
		upload.setAutoUpload(true); //ini bagian penting
		upload.setSizeFull();

		upload.setMaxFileSize(4 *1500 * 1024);
		upload.setHeight("100px");
		upload.addSucceededListener(event -> updateImageView(event));

		imageOuput = new Image();
		try {
			imageOuput = CommonFileFactory.generateImage(AppPublicService.FILE_PATH + currentSiswa.getImageName());
		}catch (Exception ex){}
		imageOuput.setMaxHeight("300px");
		imageOuput.setMaxWidth("230px");

		divImage.removeAll();
		divImage.addComponentAsFirst(imageOuput);

		FlexBoxLayout form = new FlexBoxLayout(upload, divImage);

		form.setFlexDirection(FlexLayout.FlexDirection.COLUMN);
		form.setMargin(Horizontal.AUTO, Vertical.RESPONSIVE_L);
		form.setAlignItems(FlexComponent.Alignment.CENTER);

		form.addClassName(BoxShadowBorders.BOTTOM);
		form.setFlexWrap(FlexLayout.FlexWrap.WRAP);
		form.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
		form.setPadding(Bottom.L);

		return form;
	}

	private void updateImageView(SucceededEvent event){
		isImageChange =true;

		//Agar Photo langsung terlihat
		Component component = UIUtils.createComponentFromFile(event.getMIMEType(),
				event.getFileName(), buffer.getInputStream());

		imageOuput = (Image) component;
		imageOuput.setMaxHeight("400px");
		imageOuput.setMaxWidth("300px");

		divImage.removeAll();
		divImage.addComponentAsFirst(imageOuput);

//		footer.setEnabled(true);
	}

	protected void navigateToSuccessPage(){
		appBar.getContextIcon().addClickListener(e -> UI.getCurrent().navigate(Home.class));
	}

}
