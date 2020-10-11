package com.desgreen.education.siapp.ui;

import com.desgreen.education.siapp.AppPublicService;
import com.desgreen.education.siapp.backend.model.EnumUserType;
import com.desgreen.education.siapp.security_config.AuthUserDetailsService;
import com.desgreen.education.siapp.security_config.SecurityUtils;
import com.desgreen.education.siapp.security_model.FUser;
import com.desgreen.education.siapp.security_utils_ui.AccessDeniedView;
import com.desgreen.education.siapp.ui.components.FlexBoxLayout;
import com.desgreen.education.siapp.ui.components.navigation.bar.AppBar;
import com.desgreen.education.siapp.ui.components.navigation.bar.TabBar;
import com.desgreen.education.siapp.ui.components.navigation.drawer.BrandExpression;
import com.desgreen.education.siapp.ui.components.navigation.drawer.NaviDrawer;
import com.desgreen.education.siapp.ui.components.navigation.drawer.NaviItem;
import com.desgreen.education.siapp.ui.components.navigation.drawer.NaviMenu;
import com.desgreen.education.siapp.ui.util.UIUtils;
import com.desgreen.education.siapp.ui.util.css.Overflow;
import com.desgreen.education.siapp.ui.views.Accounts;
import com.desgreen.education.siapp.ui.views.Home;
import com.desgreen.education.siapp.ui.views.master_company.CompanyView;
import com.desgreen.education.siapp.ui.views.master_division.DivisionView;
import com.desgreen.education.siapp.ui.views.master_komp_biaya.KompBiayaView;
import com.desgreen.education.siapp.ui.views.master_kurikulum.KurikulumView;
import com.desgreen.education.siapp.ui.views.master_matpel.MatPelView;
import com.desgreen.education.siapp.ui.views.master_periode.PeriodeView;
import com.desgreen.education.siapp.ui.views.master_siswa.SiswaView;
import com.desgreen.education.siapp.ui.views.transaksi_krs.KrsView;
import com.desgreen.education.siapp.ui.views.transaksi_krs_validasi.KrsValidasiView;
import com.desgreen.education.siapp.ui.views.users_pengguna.UsersView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasElement;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.page.Viewport;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.server.*;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import com.vaadin.flow.theme.lumo.Lumo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

//Chart ini tidak berhubungan dengan komponen chart vadin
//@CssImport(value = "./styles/components/charts.css", themeFor = "vaadin-chart", include = "vaadin-chart-default-theme")

@CssImport(value = "./styles/components/floating-action-button.css", themeFor = "vaadin-button")
@CssImport(value = "./styles/components/grid.css", themeFor = "vaadin-grid")
@CssImport("./styles/lumo/border-radius.css")
@CssImport("./styles/lumo/icon-size.css")
@CssImport("./styles/lumo/margin.css")
@CssImport("./styles/lumo/padding.css")
@CssImport("./styles/lumo/shadow.css")
@CssImport("./styles/lumo/spacing.css")
@CssImport("./styles/lumo/typography.css")
@CssImport("./styles/misc/box-shadow-borders.css")
@CssImport(value = "./styles/styles.css", include = "lumo-badge")
@JsModule("@vaadin/vaadin-lumo-styles/badge")
//@PWA(name = "SIAPP Dahlan Ikhsan", shortName = "SIAPP", iconPath = "images/logos/app_logo.png", backgroundColor = "#233348", themeColor = "#233348")
//@Viewport("width=device-width, minimum-scale=1.0, initial-scale=1.0, user-scalable=yes")
@SpringComponent
@UIScope
public class MainLayout extends FlexBoxLayout
		implements RouterLayout,AfterNavigationObserver {

	private static final Logger log = LoggerFactory.getLogger(MainLayout.class);
	private static final String CLASS_NAME = "root";


	/**
	 * Private parameter for this user only
	 */
	private Div appHeaderOuter;

	private FlexBoxLayout row;
	private NaviDrawer naviDrawer;
	private FlexBoxLayout column;

	private Div appHeaderInner;
	private FlexBoxLayout viewContainer;
	private Div appFooterInner;

	private Div appFooterOuter;

	private TabBar tabBar;
	private boolean navigationTabs = false;
	private AppBar appBar;

	@Autowired
	public AuthUserDetailsService authUserDetailsService;
	protected FUser userActive = new FUser();

	@Autowired
	public MainLayout() {
		VaadinSession.getCurrent()
				.setErrorHandler((ErrorHandler) errorEvent -> {
					log.error("Uncaught UI exception",
							errorEvent.getThrowable());
					Notification.show(
							"We are sorry, but an internal error occurred");
				});

		addClassName(CLASS_NAME);
		setFlexDirection(FlexDirection.COLUMN);
		setSizeFull();

		// Initialise the UI building blocks
		initStructure();

		// Populate the navigation drawer
		initNaviItems();

		// Configure the headers and footers (optional)
		initHeadersAndFooters();

	}

	/**
	 * Initialise the required components and containers.
	 */
	private void initStructure() {
		naviDrawer = new NaviDrawer();

		viewContainer = new FlexBoxLayout();
		viewContainer.addClassName(CLASS_NAME + "__view-container");
		viewContainer.setOverflow(Overflow.HIDDEN);

		column = new FlexBoxLayout(viewContainer);
		column.addClassName(CLASS_NAME + "__column");
		column.setFlexDirection(FlexDirection.COLUMN);
		column.setFlexGrow(1, viewContainer);
		column.setOverflow(Overflow.HIDDEN);

		row = new FlexBoxLayout(naviDrawer, column);
		row.addClassName(CLASS_NAME + "__row");
		row.setFlexGrow(1, column);
		row.setOverflow(Overflow.HIDDEN);
		add(row);
		setFlexGrow(1, row);
	}

	/**
	 * Initialise the navigation items.
	 */
	NaviMenu menu;
	private void initNaviItems() {
		menu = naviDrawer.getMenu();

		menu.addNaviItem(VaadinIcon.HOME, "Home", Home.class);

		NaviItem menuPendaftaran = menu.addNaviItem(VaadinIcon.USER_STAR, "PENDAFTARAN",
				null);
			menu.addNaviItem(menuPendaftaran, "PPDB/KRS", KrsView.class);
			menu.addNaviItem(menuPendaftaran, "Validasi PPDB/KRS", KrsValidasiView.class);
			menu.addNaviItem(menuPendaftaran, "Informasi Penting", Home.class);

		NaviItem menuPayments = menu.addNaviItem(VaadinIcon.CREDIT_CARD, "Biaya Dan Pembayaran",
				null);
			menu.addNaviItem(menuPayments, "Komponen Biaya", KompBiayaView.class);
			menu.addNaviItem(menuPayments, "Biaya & Pembayaran", Accounts.class);

		NaviItem menuSetupAkademis = menu.addNaviItem(VaadinIcon.ACADEMY_CAP, "Setting Akademis",
				null);
			menu.addNaviItem(menuSetupAkademis, "Mata Pelajaran", MatPelView.class);
			menu.addNaviItem(menuSetupAkademis, "Periode Belajar", PeriodeView.class);
			menu.addNaviItem(menuSetupAkademis, "Kurikulum", KurikulumView.class);
			menu.addNaviItem(menuSetupAkademis, "Master Siswa/Santri", SiswaView.class);

		NaviItem menuPoskestren = menu.addNaviItem(VaadinIcon.STETHOSCOPE, "Poskestren",
				KompBiayaView.class);
			menu.addNaviItem(menuPoskestren, "Pemeriksaan", KompBiayaView.class);
			menu.addNaviItem(menuPoskestren, "Pemeriksaan Perbaikan", KompBiayaView.class);


		NaviItem menuDonasi = menu.addNaviItem(VaadinIcon.DIAMOND, "Donasi & Penggunaan",
				null);
			menu.addNaviItem(menuDonasi, "Donatur & Donasi", AccessDeniedView.class);
			menu.addNaviItem(menuDonasi, "Penggunaan", AccessDeniedView.class);

		NaviItem menuSetupApp = menu.addNaviItem(VaadinIcon.WRENCH, "Setup",
				null);
			menu.addNaviItem(menuSetupApp, "Setting Aplikasi", AccessDeniedView.class);
			menu.addNaviItem(menuSetupApp, "Company/Holding", CompanyView.class);
			menu.addNaviItem(menuSetupApp, "Division", DivisionView.class);
			menu.addNaviItem(menuSetupApp, "Pengguna & Otorisasi", UsersView.class);


		/**
		 * MENG HIDE ANAKNY
		 */
		menu.getNaviItems().forEach(naviItem -> {
				boolean matches = ((NaviItem) naviItem).getText().toLowerCase().contains("home") ||
						((NaviItem) naviItem).getText().toLowerCase().contains("poskestren") ||
						((NaviItem) naviItem).getText().toLowerCase().contains("pemeriksaan") ||
						((NaviItem) naviItem).getText().toLowerCase().contains("pemeriksaan Perbaikan") ||
						((NaviItem) naviItem).getText().toLowerCase().contains("pendaftaran") ||
						((NaviItem) naviItem).getText().toLowerCase().contains("ppdb/krs") ||
						((NaviItem) naviItem).getText().toLowerCase().contains("validasi ppdb/krs") ||
						((NaviItem) naviItem).getText().toLowerCase().contains("informasi penting") ||
						((NaviItem) naviItem).getText().toLowerCase().contains("biaya dan pembayaran") ||
						((NaviItem) naviItem).getText().toLowerCase().contains("donasi & penggunaan") ||
						((NaviItem) naviItem).getText().toLowerCase().contains("setting akademis") ||
						((NaviItem) naviItem).getText().toLowerCase().contains("setup") ;
				naviItem.setVisible(matches);
			});


	}

	/**
	 * Configure the app's inner and outer headers and footers.
	 */
	private void initHeadersAndFooters() {
		// setAppHeaderOuter();
		// setAppFooterInner();
		// setAppFooterOuter();

		// Default inner header setup:
		// - When using tabbed navigation the view title, user avatar and main menu button will appear in the TabBar.
		// - When tabbed navigation is turned off they appear in the AppBar.

		appBar = new AppBar("");

		// Tabbed navigation
		if (navigationTabs) {
			tabBar = new TabBar();
			UIUtils.setTheme(Lumo.DARK, tabBar);

			// Shift-click to add a new tab
			for (NaviItem item : naviDrawer.getMenu().getNaviItems()) {
				item.addClickListener(e -> {
					if (e.getButton() == 0 && e.isShiftKey()) {
						tabBar.setSelectedTab(tabBar.addClosableTab(item.getText(), item.getNavigationTarget()));
					}
				});
			}
			appBar.getAvatar().setVisible(false);
			setAppHeaderInner(tabBar, appBar);

			// Default navigation
		} else {
			UIUtils.setTheme(Lumo.DARK, appBar);
			setAppHeaderInner(appBar);
		}
	}

	private void setAppHeaderOuter(Component... components) {
		if (appHeaderOuter == null) {
			appHeaderOuter = new Div();
			appHeaderOuter.addClassName("app-header-outer");
			getElement().insertChild(0, appHeaderOuter.getElement());
		}
		appHeaderOuter.removeAll();
		appHeaderOuter.add(components);
	}

	private void setAppHeaderInner(Component... components) {
		if (appHeaderInner == null) {
			appHeaderInner = new Div();
			appHeaderInner.addClassName("app-header-inner");
			column.getElement().insertChild(0, appHeaderInner.getElement());
		}
		appHeaderInner.removeAll();
		appHeaderInner.add(components);
	}

	private void setAppFooterInner(Component... components) {
		if (appFooterInner == null) {
			appFooterInner = new Div();
			appFooterInner.addClassName("app-footer-inner");
			column.getElement().insertChild(column.getElement().getChildCount(),
					appFooterInner.getElement());
		}
		appFooterInner.removeAll();
		appFooterInner.add(components);
	}

	private void setAppFooterOuter(Component... components) {
		if (appFooterOuter == null) {
			appFooterOuter = new Div();
			appFooterOuter.addClassName("app-footer-outer");
			getElement().insertChild(getElement().getChildCount(),
					appFooterOuter.getElement());
		}
		appFooterOuter.removeAll();
		appFooterOuter.add(components);
	}

//	@Override
//	public void configurePage(InitialPageSettings settings) {
//		settings.addMetaTag("apple-mobile-web-app-capable", "yes");
//		settings.addMetaTag("apple-mobile-web-app-status-bar-style", "black");
//
//		settings.addFavIcon("icon", "frontend/images/favicons/favicon.ico",
//				"256x256");
//	}

	@Override
	public void showRouterLayoutContent(HasElement content) {
		this.viewContainer.getElement().appendChild(content.getElement());
	}

	public NaviDrawer getNaviDrawer() {
		return naviDrawer;
	}

	public static MainLayout get() {
		return (MainLayout) UI.getCurrent().getChildren()
				.filter(component -> component.getClass() == MainLayout.class)
				.findFirst().get();
	}

	public AppBar getAppBar() {
		return appBar;
	}

	@Override
	public void afterNavigation(AfterNavigationEvent event) {
		if (navigationTabs) {
			afterNavigationWithTabs(event);
		} else {
			afterNavigationWithoutTabs(event);
		}
	}

	private void afterNavigationWithTabs(AfterNavigationEvent e) {
		NaviItem active = getActiveItem(e);
		if (active == null) {
			if (tabBar.getTabCount() == 0) {
				tabBar.addClosableTab("", Home.class);
			}
		} else {
			if (tabBar.getTabCount() > 0) {
				tabBar.updateSelectedTab(active.getText(),
						active.getNavigationTarget());
			} else {
				tabBar.addClosableTab(active.getText(),
						active.getNavigationTarget());
			}
		}
		appBar.getMenuIcon().setVisible(false);
	}

	private NaviItem getActiveItem(AfterNavigationEvent e) {
		for (NaviItem item : naviDrawer.getMenu().getNaviItems()) {
			if (item.isHighlighted(e)) {
				return item;
			}
		}
		return null;
	}

	private void afterNavigationWithoutTabs(AfterNavigationEvent e) {
		NaviItem active = getActiveItem(e);
		if (active != null) {
			getAppBar().setTitle(active.getText());
		}
	}

	@PostConstruct
	private void init() {
		/**
		 * Tambahan Sendiri mengganti Brand Logo Sesuai dengan Company
		 */
		String companyLogoPath = "";
		String brandTitle = "SIAPP";
		try {
			userActive = authUserDetailsService.getUserDetail(SecurityUtils.getUsername());
			companyLogoPath = AppPublicService.FILE_PATH + userActive.getFdivisionBean().getFcompanyBean().getLogoImage();
		}catch (Exception ex){}
		try {
			brandTitle = userActive.getFdivisionBean().getFcompanyBean().getKode2();
		}catch (Exception ex){}

		BrandExpression defaBrandExpression = new BrandExpression("SIAPP");
		try {
			defaBrandExpression = new BrandExpression(brandTitle, companyLogoPath);
		}catch (Exception ex){}
		naviDrawer.initHeaderChange(defaBrandExpression);

		/**
		 * SISWA CUMA DILIHATKAN PENDAFTARAN SAJA
		 */
		try {
			if (userActive.getUserType().equals(EnumUserType.SISWA)) {
				menu.getNaviItems().forEach(naviItem -> {
					boolean matches = ((NaviItem) naviItem).getText().toLowerCase().contains("home") ||
							((NaviItem) naviItem).getText().toLowerCase().contains("poskestren") ||
							((NaviItem) naviItem).getText().toLowerCase().contains("pemeriksaan") ||
//							((NaviItem) naviItem).getText().toLowerCase().contains("pendaftaran") ||
//							((NaviItem) naviItem).getText().toLowerCase().contains("ppdb/krs") ||
							((NaviItem) naviItem).getText().toLowerCase().contains("validasi ppdb/krs") ||
							((NaviItem) naviItem).getText().toLowerCase().contains("informasi penting") ||
							((NaviItem) naviItem).getText().toLowerCase().contains("biaya dan pembayaran") ||
							((NaviItem) naviItem).getText().toLowerCase().contains("donasi & penggunaan") ||
							((NaviItem) naviItem).getText().toLowerCase().contains("setting akademis") ||
							((NaviItem) naviItem).getText().toLowerCase().contains("setup") ;
					naviItem.setVisible(false);

					naviDrawer.getSearch().setVisible(false);
				});
			}
		}catch (Exception ex){}

	}


}
