package com.desgreen.education.siapp.ui.views.transaksi_krs_detail;

import com.desgreen.education.siapp.AppPublicService;
import com.desgreen.education.siapp.backend.model.EnumStatApproval;
import com.desgreen.education.siapp.backend.model.FKurikulum;
import com.desgreen.education.siapp.backend.model.FtKrs;
import com.desgreen.education.siapp.backend.model_sample.DummyData;
import com.desgreen.education.siapp.security_config.AuthUserDetailsService;
import com.desgreen.education.siapp.security_model.Role;
import com.desgreen.education.siapp.ui.MainLayout;
import com.desgreen.education.siapp.ui.components.FlexBoxLayout;
import com.desgreen.education.siapp.ui.components.ListItem;
import com.desgreen.education.siapp.ui.components.navigation.bar.AppBar;
import com.desgreen.education.siapp.ui.layout.size.Bottom;
import com.desgreen.education.siapp.ui.layout.size.Horizontal;
import com.desgreen.education.siapp.ui.layout.size.Top;
import com.desgreen.education.siapp.ui.layout.size.Vertical;
import com.desgreen.education.siapp.ui.util.BoxShadowBorders;
import com.desgreen.education.siapp.ui.util.LumoStyles;
import com.desgreen.education.siapp.ui.util.TextColor;
import com.desgreen.education.siapp.ui.util.UIUtils;
import com.desgreen.education.siapp.ui.util.css.BorderRadius;
import com.desgreen.education.siapp.ui.util.css.WhiteSpace;
import com.desgreen.education.siapp.ui.views.ViewFrame;
import com.desgreen.education.siapp.ui.views.transaksi_krs.KrsView;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;

import javax.annotation.PostConstruct;
import java.text.NumberFormat;
import java.time.LocalDate;


@Secured({Role.ADMIN, Role.MNU_PPDB_KRS})
@UIScope
@SpringComponent
@Route(value = "KrsDetailView", layout = MainLayout.class)
@PageTitle("KrsDetailView")
public class KrsDetailView extends ViewFrame implements HasUrlParameter<Long> {

	protected KrsDetailModel model;
	protected KrsDetailController controller;
	protected KrsDetailListener listener;

	@Autowired
	private AppPublicService appPublicService;
	@Autowired
	public AuthUserDetailsService authUserDetailsService;


	protected Binder<FKurikulum> binder = new BeanValidationBinder<>(FKurikulum.class);
	protected FKurikulum currentKurikulum;

	public int RECENT_TRANSACTIONS = 4;

	private ListItem listItemMatPel;
	private ListItem listItemDescription;
	private ListItem listItemPeriode;
	private ListItem listItemBiaya;

//	private BankAccount account;


	public KrsDetailView() {
		super();
		/**
		 * Init Dummy
		 */
	}

	@PostConstruct
	private void init(){
		model = new KrsDetailModel(authUserDetailsService, appPublicService);
		controller = new KrsDetailController(model, this);
//		model = controller.model;

		listener = controller;
	}

	@Override
	public void setParameter(BeforeEvent beforeEvent, Long paramId) {
		currentKurikulum = model.fKurikulumJPARepository.findById(paramId).get();
		setViewContent(createContent());

		listItemMatPel.setPrimaryText(currentKurikulum.getFmatPelBean().getDescription());
		listItemDescription.setPrimaryText(currentKurikulum.getNotes());
		listItemDescription.setSecondaryText("Quota Maksimum " + currentKurikulum.getKuotaMale() + " siswa & " + currentKurikulum.getKuotaFemale() + " siswi");
		listItemPeriode.setPrimaryText(UIUtils.formatDate_ddMMMYYYY(currentKurikulum.getFperiodeBean().getPeriodeFrom()) + " s.d " +
				UIUtils.formatDate_ddMMMYYYY(currentKurikulum.getFperiodeBean().getPeriodeTo()) );

		if (currentKurikulum.getAmountBiaya()==null) currentKurikulum.setAmountBiaya(0.0);
		NumberFormat nf = NumberFormat.getInstance(); nf.setMaximumFractionDigits(0);
		listItemBiaya.setPrimaryText("Rp. " + nf.format(currentKurikulum.getAmountBiaya()) + ",- " + currentKurikulum.getNotesBiaya());

	}


	@Override
	protected void onAttach(AttachEvent attachEvent) {
		super.onAttach(attachEvent);

		initAppBar();
		UI.getCurrent().getPage().setTitle(currentKurikulum.getFmatPelBean().getDescription());

//		availability.setPrimaryText(UIUtils.formatAmount(account.getAvailability()));
//		bankAccount.setPrimaryText(account.getAccount());
//		bankAccount.setSecondaryText(account.getBank());
//		updated.setPrimaryText(UIUtils.formatDate(account.getUpdated()));

	}

	private AppBar initAppBar() {
		AppBar appBar = MainLayout.get().getAppBar();
		appBar.setNaviMode(AppBar.NaviMode.CONTEXTUAL);
		appBar.getContextIcon().addClickListener(e -> UI.getCurrent().navigate(KrsView.class));
		appBar.setTitle(currentKurikulum.getFmatPelBean().getDescription());
		return appBar;
	}

	protected void goToParentView_ToRefresh(){
		UI.getCurrent().navigate(KrsView.class);
	}


	protected Component createContent() {
		/**
		 * Kalau Masih kosong maka dia akan panggil sebelumnya
		 */
		model.itemHeader = new FtKrs();
		try {
			model.itemHeader = model.mapKrsCurrentUser.values().stream()
					.filter(x -> x.getFkurikulumBean().equals(currentKurikulum)).findAny().get();
		}catch (Exception ex){}
//		System.out.println("KRS AFTER SELECT: " + model.itemHeader.getId() + " >> " + model.itemHeader.isStatSelected());

		FlexBoxLayout content = new FlexBoxLayout(
				createLogoSection(),
				createRecentTransactionsHeader(),
				createRecentTransactionsList()
		);
		content.setFlexDirection(FlexLayout.FlexDirection.COLUMN);
		content.setMargin(Horizontal.AUTO, Vertical.RESPONSIVE_L);
		content.setMaxWidth("840px");


		return content;
	}

	private FlexBoxLayout createLogoSection() {
		Image image = new Image(DummyData.getImageSource(currentKurikulum.getFmatPelBean().getLogoIndex()),"Company Logo");
		image.addClassName(LumoStyles.Margin.Horizontal.L);
		UIUtils.setBorderRadius(BorderRadius._50, image);
		image.setHeight("200px");
		image.setWidth("200px");

		listItemMatPel = new ListItem(UIUtils.createTertiaryIcon(VaadinIcon.OPEN_BOOK), "", "Materi Pelajaran");
		listItemMatPel.getPrimary().addClassName(LumoStyles.Heading.H2);
		listItemMatPel.setDividerVisible(true);
		listItemMatPel.setId("availability");
		listItemMatPel.setReverse(true);

		listItemDescription = new ListItem(UIUtils.createTertiaryIcon(VaadinIcon.INSTITUTION), "", "");
		listItemDescription.setDividerVisible(true);
		listItemDescription.setId("bankAccount");
		listItemDescription.setReverse(true);
		listItemDescription.setWhiteSpace(WhiteSpace.PRE_LINE);


		listItemPeriode = new ListItem(UIUtils.createTertiaryIcon(VaadinIcon.CALENDAR), "", "Periode");
		listItemPeriode.setReverse(true);
		listItemPeriode.setDividerVisible(true);

		listItemBiaya = new ListItem(UIUtils.createTertiaryIcon(VaadinIcon.USER_CHECK), "", "Periode");
		listItemBiaya.setId("biayaId");
		listItemBiaya.setReverse(true);

		FlexBoxLayout listItems = new FlexBoxLayout(listItemMatPel, listItemDescription, listItemPeriode, listItemBiaya);
		listItems.setFlexDirection(FlexLayout.FlexDirection.COLUMN);

		FlexBoxLayout section = new FlexBoxLayout(image, listItems);
		section.addClassName(BoxShadowBorders.BOTTOM);
		section.setAlignItems(FlexComponent.Alignment.CENTER);
		section.setFlex("1", listItems);
		section.setFlexWrap(FlexLayout.FlexWrap.WRAP);
		section.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
		section.setPadding(Bottom.L);
		return section;
	}

	protected Button btnDaftarOrBatal;
	private Component createRecentTransactionsHeader() {

		Label title = UIUtils.createH3Label("Progres Pendaftaran");
		if (model.itemHeader.isStatSelected() && model.itemHeader.isStatCancel()==false) {
			btnDaftarOrBatal = UIUtils.createErrorButton("BATALKAN", VaadinIcon.CLOSE);
		}else {
			btnDaftarOrBatal = UIUtils.createPrimaryButton("DAFTAR", VaadinIcon.PAPERPLANE);
		}

		btnDaftarOrBatal.addClickListener(e -> {
			listener.aksiBtnDaftarOrBatalForm();
		});
		btnDaftarOrBatal.addClassName(LumoStyles.Margin.Left.AUTO);

		FlexBoxLayout header = new FlexBoxLayout(title, btnDaftarOrBatal);
		header.setAlignItems(FlexComponent.Alignment.CENTER);
		header.setMargin(Bottom.M, Horizontal.RESPONSIVE_L, Top.L);
		return header;
	}

	private Component createRecentTransactionsListX() {
		Div items = new Div();
		items.addClassNames(BoxShadowBorders.BOTTOM, LumoStyles.Padding.Bottom.L);

		for (int i = 0; i < RECENT_TRANSACTIONS; i++) {
			Double amount = DummyData.getAmount();
			Label amountLabel = UIUtils.createAmountLabel(amount);
			if (amount > 0) {
				UIUtils.setTextColor(TextColor.SUCCESS, amountLabel);
			} else {
				UIUtils.setTextColor(TextColor.ERROR, amountLabel);
			}
			ListItem item = new ListItem(
					DummyData.getLogo(),
					DummyData.getCompany(),
					UIUtils.formatDate(LocalDate.now().minusDays(i)),
					amountLabel
			);
			// Dividers for all but the last item
			item.setDividerVisible(i < RECENT_TRANSACTIONS - 1);
			items.add(item);
		}

		return items;
	}
	private Component createRecentTransactionsList() {

		Div items = new Div();
		items.addClassNames(BoxShadowBorders.BOTTOM, LumoStyles.Padding.Bottom.L);

		Label itemLabel1 = new Label();
		UIUtils.setTextColor(TextColor.PRIMARY, itemLabel1);
		if (model.itemHeader.isStatSelected() && model.itemHeader.isStatCancel()==false) {
			itemLabel1 = UIUtils.createBoldLabel("DAFTAR");
			UIUtils.setTextColor(TextColor.PRIMARY, itemLabel1);
		}else if (model.itemHeader.isStatSelected() && model.itemHeader.isStatCancel()==true) {
			itemLabel1 = UIUtils.createBoldLabel("DIBATALKAN");
			UIUtils.setTextColor(TextColor.ERROR, itemLabel1);
		}else {
			itemLabel1 = UIUtils.createBoldLabel("BELUM");
			UIUtils.setTextColor(TextColor.SECONDARY, itemLabel1);
		}

		Icon itemIcon1 = new Icon(VaadinIcon.FILE_ADD);
		itemIcon1.setColor("blue");
		ListItem item1 = new ListItem(itemIcon1,
				"Melakukan Pendaftaran",
				UIUtils.formatDate(LocalDate.now()),
				itemLabel1
		);
		item1.setDividerVisible(true);
		items.add(item1);

		Label itemLabel2 = UIUtils.createBoldLabel("-");
		UIUtils.setTextColor(TextColor.SECONDARY, itemLabel2);
		if (model.itemHeader.getEnumStatApproval().equals(EnumStatApproval.APPROVE)) {
			itemLabel2 = UIUtils.createBoldLabel("DISETUJUI");
			UIUtils.setTextColor(TextColor.SUCCESS, itemLabel2);
		}else if (model.itemHeader.getEnumStatApproval().equals(EnumStatApproval.APPROVE)) {
			itemLabel2 = UIUtils.createBoldLabel("TIDAK DISETUJUI");
			UIUtils.setTextColor(TextColor.ERROR, itemLabel2);
		}
		Icon itemIcon2 = new Icon(VaadinIcon.CHECK);
		itemIcon2.setColor("green");
		ListItem item2 = new ListItem(itemIcon2,
				"Validasi/Approval",
				UIUtils.formatDate(LocalDate.now()),
				itemLabel2
		);
		item2.setDividerVisible(true);
		items.add(item2);

		Label itemLabel3 = UIUtils.createBoldLabel("-");
		UIUtils.setTextColor(TextColor.SECONDARY, itemLabel3);
		Icon itemIcon3 = new Icon(VaadinIcon.MONEY);
		itemIcon3.setColor("#bd2db1");
		ListItem item3 = new ListItem(itemIcon3,
				"Pembayaran",
				UIUtils.formatDate(LocalDate.now()),
				itemLabel3
		);
//		item1.setDividerVisible(true);
		items.add(item3);


		return items;
	}

	private Component createMonthlyOverviewHeader() {
		Label header = UIUtils.createH3Label("Monthly Overview");
		header.addClassNames(LumoStyles.Margin.Vertical.L, LumoStyles.Margin.Responsive.Horizontal.L);
		return header;
	}



}
