package com.desgreen.education.siapp.ui.views.master_komp_biaya;

import com.desgreen.education.siapp.AppPublicService;
import com.desgreen.education.siapp.backend.model.FDivision;
import com.desgreen.education.siapp.backend.model.FKompBiaya;
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
import com.desgreen.education.siapp.ui.views.SplitViewFrame;
import com.vaadin.componentfactory.ToggleButton;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
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
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Objects;


@Secured({Role.ADMIN, Role.MNU_KOMP_BIAYA})
@UIScope
@SpringComponent
@Route(value = "KompBiayaView", layout = MainLayout.class)
@PageTitle("KompBiayaView")
public class KompBiayaView extends SplitViewFrame {

	protected KompBiayaModel model;
	protected KompBiayaController controller;
	protected KompBiayaListener listener;

	@Autowired
	private AppPublicService appPublicService;
	@Autowired
	public AuthUserDetailsService authUserDetailsService;

	protected Grid<FKompBiaya> grid;
	protected ListDataProvider<FKompBiaya> dataProvider;

	protected DetailsDrawer detailsDrawer;
	protected DetailsDrawerHeader detailsDrawerHeader;

	private AppBar appBar;
	private Button btnReloadFromDB;
	private Button btnSearchForm;
	private Button btnNewForm;
	private Button btnDeleteDeleteForm;


	protected Binder<FKompBiaya> binder = new BeanValidationBinder<>(FKompBiaya.class);
	protected FKompBiaya currentDomain;



	public KompBiayaView() {
		super();
		/**
		 * Init Dummy
		 */
	}
	@PostConstruct
	private void init(){
		model = new KompBiayaModel(authUserDetailsService, appPublicService);
		controller = new KompBiayaController(model, this);
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
		binder.readBean(new FKompBiaya());
		detailsDrawer.setContent(createDetails());

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
//		dataProvider = DataProvider.ofCollection(DummyData.getFKompBiayas());
//		dataProvider = DataProvider.ofCollection(new ArrayList<>());
		dataProvider = DataProvider.ofCollection( model.mapHeader.values() );
		
		grid.setDataProvider(dataProvider);
		grid.setHeightFull();

		grid.addColumn(FKompBiaya::getId)
				.setAutoWidth(true)
				.setFlexGrow(0)
				.setFrozen(true)
				.setHeader("ID")
				.setSortable(true);
		grid.addColumn(new ComponentRenderer<>(this::createDetilInfo))
				.setAutoWidth(true)
				.setHeader("Name MatPel")
				.setComparator(FKompBiaya::getDescription)
				.setSortable(true);
		// grid.addColumn(new ComponentRenderer<>(this::createMatPelDateFrom))
		// 		.setAutoWidth(true)
		// 		.setHeader("Mulai")
		// 		.setComparator(FKompBiaya::getMatPelFrom)
		// 		.setSortable(true);
		// grid.addColumn(new ComponentRenderer<>(this::createMatPelDateTo))
		// 		.setAutoWidth(true)
		// 		.setHeader("Selesai")
		// 		.setComparator(FKompBiaya::getMatPelFrom)
		// 		.setSortable(true);
		grid.addColumn(new ComponentRenderer<>(this::createActive))
				.setAutoWidth(true)
				.setFlexGrow(0)
				.setHeader("Active")
				.setTextAlign(ColumnTextAlign.END);
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

	private Component createDetilInfo(FKompBiaya fMatPel) {
		ListItem item = new ListItem(
				new Initials(fMatPel.getKode1()), fMatPel.getDescription(),
				"");
		item.setPadding(Vertical.XS);
		item.setSpacing(Right.M);
		return item;
	}
	
	private Component createActive(FKompBiaya fMatPel) {
		Icon icon;
		if (fMatPel.isStatActive()) {
			icon = UIUtils.createPrimaryIcon(VaadinIcon.CHECK);
		}else {
			icon = UIUtils.createDisabledIcon(VaadinIcon.CLOSE);
		}
		return icon;
	}

	private Component createDivision(FKompBiaya fMatPel) {
		return UIUtils.createBoldLabel(fMatPel.getFdivisionBean()!=null? fMatPel.getFdivisionBean().getDescription(): "");
	}

	private Component createComboDivisionInfo(FDivision fDivision) {
		ListItem item = new ListItem( fDivision.getDescription(),
				fDivision.getFcompanyBean().getDescription());
		item.setPadding(Vertical.XS);
		item.setSpacing(Right.M);

		return item;
	}


	// private Component createMatPelDateFrom(FKompBiaya fMatPel) {
	// 	return new Span(UIUtils.formatDate(fMatPel.getMatPelFrom()));
	// }
	// private Component createMatPelDateTo(FKompBiaya fMatPel) {
	// 	return new Span(UIUtils.formatDate(fMatPel.getMatPelTo()));
	// }

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

	protected void showDetails(FKompBiaya fMatPel) {
		if (fMatPel == null) {
			fMatPel = new FKompBiaya();
		}
//		delete.setVisible(!product.isNewProduct());
		currentDomain = fMatPel;
		try {
			binder.readBean(fMatPel);
		}catch (Exception ex) {ex.printStackTrace();}

		//delete.setVisible(!product.isNewProduct());

		detailsDrawerHeader.setTitle(fMatPel.getDescription());
//		detailsDrawer.setContent(createDetails(fMatPel));
		detailsDrawer.show();

	}


	/**
	 * FORM COMPONENT
	 * dipanggil sekali saja
	 */
	private final TextField kode1 = new TextField();
	private final TextField description = new TextField();
	private final TextArea notes = new TextArea();
	private final ComboBox<FDivision> comboFDivision = new ComboBox<FDivision>();
	private final ToggleButton toggleStatActive = new ToggleButton();


//	private FormLayout createDetails(FKompBiaya fMatPel) {
	private FormLayout createDetails() {
//		System.out.println(fMatPel.getAddress1());

		/**
		 * Re Binding Datata
		 */
		binder.bindInstanceFields(this);
		binder.forField(kode1).asRequired().bind(FKompBiaya::getKode1, FKompBiaya::setKode1);
		binder.forField(description).asRequired().bind(FKompBiaya::getDescription, FKompBiaya::setDescription);
//				.withConverter(new StringToBooleanConverter("", "Active", "Non-Active"))
//				.bind(FKompBiaya::isStatActive, FKompBiaya::setStatActive);
		binder.forField(comboFDivision)
			.asRequired()
			.bind(FKompBiaya::getFdivisionBean, FKompBiaya::setFdivisionBean);
		binder.forField(toggleStatActive)
				.bind(FKompBiaya::isStatActive, FKompBiaya::setStatActive);



		/**
		 * Decorate the Form View
		 */
		kode1.setWidthFull();
		description.setWidthFull();

		notes.setWidthFull();
		
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


		comboFDivision.setItems(model.listFDivision);
		comboFDivision.setWidthFull();
		comboFDivision.setItemLabelGenerator(p -> p.getKode1() + "  " + p.getDescription());
//		comboFDivision.setRenderer(new ComponentRenderer<>(item -> new Span(item.getDescription())));
		comboFDivision.setRenderer(new ComponentRenderer<>(this::createComboDivisionInfo ));

		kode1.setValueChangeMode(ValueChangeMode.EAGER);
		description.setValueChangeMode(ValueChangeMode.EAGER);

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
		FormLayout.FormItem descriptionItem = form.addFormItem(description, "Deskripsi");
		FormLayout.FormItem notesItem = form.addFormItem(notes, "Catatan");

		FormLayout.FormItem toggleStatusItem = form.addFormItem(toggleStatActive, "Status");

		FormLayout.FormItem comboFDivisionItem = form.addFormItem(comboFDivision, "Division");

		UIUtils.setColSpan(2, descriptionItem, notesItem, comboFDivisionItem, toggleStatusItem);

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

	private void filter() {
		// dataProvider.setFilterByValue(FKompBiaya::getRole, FKompBiaya.Role.ACCOUNTANT);
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
