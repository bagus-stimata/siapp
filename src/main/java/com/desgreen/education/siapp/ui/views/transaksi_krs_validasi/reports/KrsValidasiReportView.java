package com.desgreen.education.siapp.ui.views.transaksi_krs_validasi.reports;

import com.desgreen.education.siapp.AppPublicService;
import com.desgreen.education.siapp.backend.model.EnumStatApproval;
import com.desgreen.education.siapp.backend.model.FKurikulum;
import com.desgreen.education.siapp.backend.model.ZLapTemplate2;
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
import com.desgreen.education.siapp.ui.utils.common.CommonFileFactory;
import com.desgreen.education.siapp.ui.views.ViewFrame;
import com.desgreen.education.siapp.ui.views.transaksi_krs_validasi.KrsValidasiView;
import com.desgreen.education.siapp.ui.views.transaksi_krs_validasi_detail.KrsValidasiDetailController;
import com.desgreen.education.siapp.ui.views.transaksi_krs_validasi_detail.KrsValidasiDetailListener;
import com.desgreen.education.siapp.ui.views.transaksi_krs_validasi_detail.KrsValidasiDetailModel;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.function.SerializableSupplier;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.vaadin.reports.PrintPreviewReport;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Secured({Role.ADMIN, Role.MNU_VALIDASI_KRS})
@UIScope
@SpringComponent
@Route(value = "KrsValidasiReportView", layout = MainLayout.class)
@PageTitle("KrsValidasiReportView")
public class KrsValidasiReportView extends ViewFrame implements HasUrlParameter<Long> {

	@Autowired
	private AppPublicService appPublicService;
	@Autowired
	public AuthUserDetailsService authUserDetailsService;


	protected Binder<FKurikulum> binder = new BeanValidationBinder<>(FKurikulum.class);

	public int RECENT_TRANSACTIONS = 4;

	private ListItem listItemMatPel;
	private ListItem listItemDescription;
	private ListItem listItemPeriode;

	private ListItem listItemAlamat;
	private ListItem listItemSambung;
	private ListItem listItemCatatan;

//	private BankAccount account;


	public KrsValidasiReportView() {
		super();
		/**
		 * Init Dummy
		 */
	}

	@PostConstruct
	private void init(){
	}

	@Override
	public void setParameter(BeforeEvent beforeEvent, Long paramId) {
//		model.reloadCurrentDomain(paramId);
		setViewContent(createContent());

	}


	@Override
	protected void onAttach(AttachEvent attachEvent) {
		super.onAttach(attachEvent);

		initAppBar();
//		UI.getCurrent().getPage().setTitle(model.currentDomain.getFkurikulumBean().getDescription() + " | " + model.currentDomain.getFsiswaBean().getFullName());

	}

	private AppBar initAppBar() {
		AppBar appBar = MainLayout.get().getAppBar();
		appBar.searchModeOff();

		appBar.setNaviMode(AppBar.NaviMode.CONTEXTUAL);
		appBar.getContextIcon().addClickListener(e -> UI.getCurrent().navigate(KrsValidasiView.class, (long) 0));
//		appBar.setTitle(model.currentDomain.getFkurikulumBean().getDescription() + " | " + model.currentDomain.getFsiswaBean().getFullName());
		return appBar;
	}

	protected void goToParentView_ToRefresh(){
		UI.getCurrent().navigate(KrsValidasiView.class, (long) 0);
	}


	protected Component createContent() {
		/**
		 * Kalau Masih kosong maka dia akan panggil sebelumnya
		 */
//		model.itemHeader = new FtKrs();
//		try {
//			model.itemHeader = model.mapKrsCurrentUser.values().stream()
//					.filter(x -> x.getFkurikulumBean().equals(model.itemHeader)).findAny().get();
//		}catch (Exception ex){}
//		System.out.println("KRS AFTER SELECT: " + model.itemHeader.getId() + " >> " + model.itemHeader.isStatSelected());

		List<ZLapTemplate2> list = new ArrayList<>();

		PrintPreviewReport<ZLapTemplate2> report = new PrintPreviewReport<>(ZLapTemplate2.class, "client", "city", "phoneNumber", "startTime", "duration", "status");

		SerializableSupplier<List<? extends ZLapTemplate2>> itemsCall = () -> list;

		report.setItems(itemsCall.get());

		HorizontalLayout anchors = new HorizontalLayout();
		for (PrintPreviewReport.Format format : PrintPreviewReport.Format.values()) {
			Anchor anchor = new Anchor(report.getStreamResource("call-report." + format.name().toLowerCase(), itemsCall, format),
					format.name());
			anchor.getElement().setAttribute("download", true);
			anchors.add(anchor);
		}
//		add(anchors, report);
//		setPadding(false);


		FlexBoxLayout content = new FlexBoxLayout(
			anchors, report
		);
		content.setFlexDirection(FlexLayout.FlexDirection.COLUMN);
		content.setMargin(Horizontal.AUTO, Vertical.RESPONSIVE_L);
		content.setMaxWidth("840px");
		return content;
	}


}
