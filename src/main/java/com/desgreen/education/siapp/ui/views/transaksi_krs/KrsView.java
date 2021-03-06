package com.desgreen.education.siapp.ui.views.transaksi_krs;

import com.desgreen.education.siapp.AppPublicService;
import com.desgreen.education.siapp.backend.model.*;
import com.desgreen.education.siapp.backend.model_sample.DummyData;
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
import com.desgreen.education.siapp.ui.views.transaksi_krs_detail.KrsDetailView;
import com.desgreen.education.siapp.ui.utils.common.CommonFileFactory;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
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

@Secured({Role.ADMIN, Role.MNU_PPDB_KRS})
@UIScope
@SpringComponent
@Route(value = "KrsView", layout = MainLayout.class)
@PageTitle("KrsView")
public class KrsView extends SplitViewFrame {

	protected KrsModel model;
	protected KrsController controller;
	protected KrsListener listener;

	@Autowired
	private AppPublicService appPublicService;
	@Autowired
	public AuthUserDetailsService authUserDetailsService;

	protected Grid<FKurikulum> grid;
	protected ListDataProvider<FKurikulum> dataProvider;

	protected DetailsDrawer detailsDrawer;
	protected DetailsDrawerHeader detailsDrawerHeader;

	private AppBar appBar;
	private Button btnReloadFromDB;
	private Button btnSearchForm;
	private Button btnNewForm;
	private Button btnDeleteDeleteForm;


	protected Binder<FKurikulum> binder = new BeanValidationBinder<>(FKurikulum.class);
	protected FtKrs currentDomain;



	public KrsView() {
		super();
		/**
		 * Init Dummy
		 */
	}
	@PostConstruct
	private void init(){
		model = new KrsModel(authUserDetailsService, appPublicService);
		controller = new KrsController(model, this);
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
		/**
		 * Alternatif bisa mengunakan ini
		 * Artinya akan selalu Reload setiap menuju kesini
		 */
		model.reloadListHeader();

		initAppBar();

		setViewContent(createContent());
		setViewDetails(createDetailsDrawer());


		filter();
		binder.readBean(new FKurikulum());

	}

	private void initAppBar() {
		appBar = MainLayout.get().getAppBar();

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
				.ifPresent(this::viewDetails));


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
				.setFlexGrow(0)
				.setHeader("MatPel/Kurikulum")
				.setComparator(FKurikulum::getDescription)
				.setSortable(true);
		grid.addColumn(new ComponentRenderer<>(this::createStatusPesetujuan))
				.setAutoWidth(true)
				.setFlexGrow(0)
				.setHeader("STATUS")
				.setTextAlign(ColumnTextAlign.CENTER);

		grid.addColumn(new ComponentRenderer<>(this::createPeriode))
				.setAutoWidth(true)
				.setFlexGrow(0)
				.setHeader("Periode")
				.setTextAlign(ColumnTextAlign.CENTER)
				.setComparator((o1, o2) -> o1.getFperiodeBean().getDescription().compareTo(o2.getFperiodeBean().getDescription()))
				.setSortable(true);
		grid.addColumn(new ComponentRenderer<>(this::createKuotaMale))
				.setAutoWidth(true)
				.setFlexGrow(0)
				.setHeader("Kuota")
				.setTextAlign(ColumnTextAlign.CENTER)
				.setComparator(FKurikulum::getKuotaMale)
				.setSortable(false);
			grid.addColumn(new ComponentRenderer<>(this::createKuotaFemale))
				.setAutoWidth(true)
				.setFlexGrow(0)
				.setTextAlign(ColumnTextAlign.CENTER)
				.setComparator(FKurikulum::getKuotaFemale)
				.setSortable(false);
		grid.addColumn(new ComponentRenderer<>(this::createStatusBuka))
				.setAutoWidth(true)
				.setFlexGrow(0)
				.setHeader("STATUS")
				.setTextAlign(ColumnTextAlign.CENTER);
		grid.addColumn(new ComponentRenderer<>(this::createCompany))
				.setAutoWidth(true)
				.setFlexGrow(0)
				.setHeader("Sekolah/Pondok")
				.setTextAlign(ColumnTextAlign.START)
				.setSortable(true);


		return grid;
	}

	private void viewDetails(FKurikulum fKurikulum) {
		UI.getCurrent().navigate(KrsDetailView.class, fKurikulum.getId());
	}

	private Component createDetilInfo(FKurikulum fKurikulum) {
		ListItem item = new ListItem(
				new Initials(fKurikulum.getKode1()), fKurikulum.getFmatPelBean().getDescription(),
				fKurikulum.getDescription());
		item.setPadding(Vertical.XS);
		item.setSpacing(Right.S);

		item.setPrefix(new Image(DummyData.getImageSource(fKurikulum.getFmatPelBean().getLogoIndex()), "Logo"));


		return item;
	}

	private Component createActive(FKurikulum fKurikulum) {
		FtKrs ftKrs = new FtKrs();
		try{
			ftKrs = model.listKrsCurrentUser.stream().filter(x -> x.getFkurikulumBean().equals(fKurikulum)).findAny().get();
//			System.out.println("KRS: " + ftKrs.getId() + " >> " + fKurikulum.getId() + " >>" + ftKrs.getFkurikulumBean().getId() + " >> " + ftKrs.isStatSelected());
		}catch (Exception ex){}

		Icon icon;
		if (ftKrs.isStatSelected()) {
			icon = UIUtils.createPrimaryIcon(VaadinIcon.CHECK);
		}else {
			icon = UIUtils.createDisabledIcon(VaadinIcon.CLOSE);
		}
		return icon;
	}

	private Component createStatusPesetujuan(FKurikulum fKurikulum) {
		FtKrs ftKrs = new FtKrs();
		try{
			ftKrs = model.listKrsCurrentUser.stream().filter(x -> x.getFkurikulumBean().equals(fKurikulum)).findAny().get();
//			System.out.println("KRS: " + ftKrs.getId() + " >> " + fKurikulum.getId() + " >>" + ftKrs.getFkurikulumBean().getId() + " >> " + ftKrs.isStatSelected());
		}catch (Exception ex){}

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


	private Component createStatusBuka(FKurikulum fKurikulum) {
		Label labelSatus = new Label();
		labelSatus = UIUtils.createBoldLabel("BUKA");
		UIUtils.setTextColor(TextColor.SUCCESS, labelSatus);
		return labelSatus;
	}

	private Component createPeriode(FKurikulum fKurikulum) {
		String periodeStart = "Start: ";
		String periodeEnd = "End: ";

		if (fKurikulum.getFperiodeBean() !=null) {
			periodeStart += UIUtils.formatDate_ddMMMYYYY(fKurikulum.getFperiodeBean().getPeriodeFrom());
			periodeEnd += UIUtils.formatDate_ddMMMYYYY(fKurikulum.getFperiodeBean().getPeriodeTo());
		}

		ListItem item = new ListItem(periodeStart, periodeEnd);
		item.setPadding(Vertical.XS);
		item.setSpacing(Right.S);

		return item;
	}


	private Component createCompany(FKurikulum fKurikulum) {
		ListItem item = new ListItem(fKurikulum.getFdivisionBean().getFcompanyBean().getDescription(),
				fKurikulum.getFmatPelBean().getFdivisionBean().getDescription());
		item.setPadding(Vertical.XS);
		item.setSpacing(Right.S);

		Image image = new Image();
		try {
			image = CommonFileFactory.generateImage(AppPublicService.FILE_PATH +
					fKurikulum.getFdivisionBean().getFcompanyBean().getLogoImage());
		}catch (Exception ex){
			ex.printStackTrace();
		}

		item.setPrefix(image);

		return item;
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

	private Component createKuotaMale(FKurikulum fKurikulum) {
		long qoutaMale = fKurikulum.getFtKrsSet().stream().filter(x-> x.getFsiswaBean().isSex()==true && x.getEnumStatApproval().equals(EnumStatApproval.APPROVE)).count();
		ListItem item = new ListItem(String.valueOf(qoutaMale),
				"of " + String.valueOf(fKurikulum.getKuotaMale()) );
		item.setPadding(Vertical.XS);
		item.setSpacing(Right.S);

		Icon icon = UIUtils.createPrimaryIcon(VaadinIcon.MALE);
		item.setPrefix(icon);

		return item;
	}
	private Component createKuotaFemale(FKurikulum fKurikulum) {
		long qoutaFemale = fKurikulum.getFtKrsSet().stream().filter(x-> x.getFsiswaBean().isSex()==false && x.getEnumStatApproval().equals(EnumStatApproval.APPROVE)).count();
		ListItem item = new ListItem(String.valueOf(qoutaFemale),
				"of " + String.valueOf(fKurikulum.getKuotaFemale()) );
		item.setPadding(Vertical.XS);
		item.setSpacing(Right.S);

		Icon icon = UIUtils.createSuccessIcon(VaadinIcon.FEMALE);
		item.setPrefix(icon);
		return item;
	}

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
