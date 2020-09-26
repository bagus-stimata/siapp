package com.desgreen.education.siapp.ui.views;

import com.desgreen.education.siapp.AppPublicService;
import com.desgreen.education.siapp.backend.jpa_repository.FKurikulumJPARepository;
import com.desgreen.education.siapp.backend.model.EnumStatApproval;
import com.desgreen.education.siapp.backend.model.FDivision;
import com.desgreen.education.siapp.backend.model.FKurikulum;
import com.desgreen.education.siapp.security_config.AuthUserDetailsService;
import com.desgreen.education.siapp.security_config.SecurityUtils;
import com.desgreen.education.siapp.security_model.FUser;
import com.desgreen.education.siapp.ui.MainLayout;
import com.desgreen.education.siapp.ui.components.FlexBoxLayout;
import com.desgreen.education.siapp.ui.layout.size.Horizontal;
import com.desgreen.education.siapp.ui.layout.size.Right;
import com.desgreen.education.siapp.ui.layout.size.Uniform;
import com.desgreen.education.siapp.ui.util.UIUtils;
import com.desgreen.education.siapp.ui.utils.common.CommonFileFactory;
import com.desgreen.education.siapp.ui.utils.common.CommonImageFactory;
import com.github.appreciated.apexcharts.ApexCharts;
import com.github.appreciated.apexcharts.ApexChartsBuilder;
import com.github.appreciated.apexcharts.config.Fill;
import com.github.appreciated.apexcharts.config.builder.*;
import com.github.appreciated.apexcharts.config.chart.Type;
import com.github.appreciated.apexcharts.config.chart.builder.DropShadowBuilder;
import com.github.appreciated.apexcharts.config.chart.builder.ToolbarBuilder;
import com.github.appreciated.apexcharts.config.fill.builder.GradientBuilder;
import com.github.appreciated.apexcharts.config.legend.Position;
import com.github.appreciated.apexcharts.config.plotoptions.builder.RadialBarBuilder;
import com.github.appreciated.apexcharts.config.plotoptions.hollow.HollowPosition;
import com.github.appreciated.apexcharts.config.plotoptions.radialbar.builder.*;
import com.github.appreciated.apexcharts.config.responsive.builder.OptionsBuilder;
import com.github.appreciated.apexcharts.config.stroke.LineCap;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.FlexLayout.FlexDirection;
import com.vaadin.flow.component.orderedlayout.FlexLayout.FlexWrap;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import jdk.javadoc.internal.doclets.toolkit.taglets.SeeTaglet;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@PageTitle("Welcome")
@Route(value = "", layout = MainLayout.class)
@RouteAlias(value = "Home")
public class Home extends ViewFrame {

	@Autowired
	private AppPublicService appPublicService;
	@Autowired
	public AuthUserDetailsService authUserDetailsService;



	protected FKurikulumJPARepository fKurikulumJPARepository;
	protected FUser userActive = new FUser();

	public Home() {
		setId("home");
	}

	@PostConstruct
	private void init() {
		fKurikulumJPARepository = appPublicService.fKurikulumJPARepository;
		userActive = authUserDetailsService.getUserDetail(SecurityUtils.getUsername());
	}
	@Override
	protected void onAttach(AttachEvent attachEvent) {
		super.onAttach(attachEvent);
		setViewContent(createContent());
	}

	private Component createContent() {

		Set<FDivision> fDivisions = userActive.getFdivisionBean().getFcompanyBean().getFdivisionSet();
		List<FKurikulum> list = new ArrayList<>(fKurikulumJPARepository.findAllByParent( fDivisions).stream().filter(x -> x.isStatActive() == true && x.getFdivisionBean().getFcompanyBean().equals(userActive.getFdivisionBean().getFcompanyBean())).collect(Collectors.toList()));


		int counter = 0;
		int max =4;
		FlexBoxLayout content = new FlexBoxLayout();
		for (FKurikulum kurikulumBean : list.stream().filter(x ->
				(x.getFperiodeBean().getDaftarOpenFrom().isBefore(LocalDate.now()) || x.getFperiodeBean().getDaftarOpenFrom().isEqual(LocalDate.now())) &&
						(x.getFperiodeBean().getDaftarCloseTo().isAfter(LocalDate.now()) || x.getFperiodeBean().getDaftarCloseTo().isEqual(LocalDate.now()))
			).sorted(Comparator.comparing(FKurikulum::getId).reversed()).collect(Collectors.toList())) {

			double filledQuotaMale = (double) kurikulumBean.getFtKrsSet().stream().filter(x-> x.getFsiswaBean().isSex()==true && x.getEnumStatApproval().equals(EnumStatApproval.APPROVE)).count();
			double filledQuotaFemale = (double) kurikulumBean.getFtKrsSet().stream().filter(x-> x.getFsiswaBean().isSex()==false && x.getEnumStatApproval().equals(EnumStatApproval.APPROVE)).count();

			double persenMale = 0;
			double persenFemale = 0;
			try {
				persenMale = filledQuotaMale/(double) kurikulumBean.getKuotaMale() * 100.0;
			}catch (Exception ex){}
			try {
				persenFemale = filledQuotaFemale/(double) kurikulumBean.getKuotaFemale() * 100.0;
			}catch (Exception ex){}

			int periodeYear = kurikulumBean.getFperiodeBean().getPeriodeFrom().getYear();
			String title = kurikulumBean.getDescription()  + " (" + periodeYear + ")".toUpperCase() + " ";
			title += "Quota L= " + (kurikulumBean.getKuotaMale()) + ", P= " + kurikulumBean.getKuotaFemale();

//			ApexCharts chart1 = multiRadialBarChartExample(BigDecimal.valueOf(persenMale).setScale(2, RoundingMode.HALF_DOWN).doubleValue(),
//					BigDecimal.valueOf(persenFemale).setScale(2, RoundingMode.HALF_DOWN).doubleValue(), kurikulumBean.getDescription());
			ApexCharts chart1 = gradientRadialBarChartExample(BigDecimal.valueOf(persenMale).setScale(2, RoundingMode.HALF_DOWN).doubleValue(),
					BigDecimal.valueOf(persenFemale).setScale(2, RoundingMode.HALF_DOWN).doubleValue(),
					(int) filledQuotaMale, (int) filledQuotaFemale,
					title);
			content.add(chart1);

			counter++;
			if (counter==max) break;
		}
		content.setFlexDirection(FlexDirection.ROW);
//		content.setMargin(Horizontal.AUTO);
		content.setMaxWidth("840px");
		content.setPadding(Uniform.RESPONSIVE_L);

		content.setAlignContent(FlexLayout.ContentAlignment.CENTER);


		return content;
	}
	private Component createContentX() {
		Html intro = new Html("<p>A responsive application template with some dummy data. Loosely based on " +
				"the <b>responsive layout grid</b> guidelines set by " +
				"<a href=\"https://material.io/design/layout/responsive-layout-grid.html\">Material Design</a>. " +
				"Utilises the <a href=\"https://vaadin.com/themes/lumo\">Lumo</a> theme.</p>");

		Html productivity = new Html("<p>The starter gives you a productivity boost and a head start. " +
				"You get an app shell with a typical hierarchical left-hand menu. The shell, the views and the " +
				"components are all responsive and touch friendly, which makes them great for desktop and mobile" +
				"use. The views are built with Java, which enhances Java developers' productivity by allowing them to" +
				"do all in one language.</p>");


		Anchor documentation = new Anchor("https://vaadin.com/docs/business-app/overview.html", UIUtils.createButton("Read the documentation", VaadinIcon.EXTERNAL_LINK));
		Anchor starter = new Anchor("https://vaadin.com/start/latest/business-app", UIUtils.createButton("Start a new project with Business App", VaadinIcon.EXTERNAL_LINK));

		FlexBoxLayout links = new FlexBoxLayout(documentation, starter);
		links.setFlexWrap(FlexWrap.WRAP);
		links.setSpacing(Right.S);

		FlexBoxLayout content = new FlexBoxLayout(intro, productivity, links);
		content.setFlexDirection(FlexDirection.COLUMN);
		content.setMargin(Horizontal.AUTO);
		content.setMaxWidth("840px");
		content.setPadding(Uniform.RESPONSIVE_L);

		return content;
	}


	public Component  pieChartExample() {
		TitleSubtitleBuilder subTitleBuilder = TitleSubtitleBuilder.get().withText("Pie Chart Bos");
		ApexChartsBuilder chartBuilder = new ApexChartsBuilder();
		chartBuilder.withChart(ChartBuilder.get().withType(Type.pie).build())
				.withLabels("Team A", "Team B", "Team C", "Team D", "Team E")
				.withTitle(subTitleBuilder.build())
				.withLegend(LegendBuilder.get()
						.withPosition(Position.right)
						.build())
				.withSeries(44.0, 55.0, 13.0, 43.0, 22.0)
				.withResponsive(ResponsiveBuilder.get()
						.withBreakpoint(480.0)
						.withOptions(OptionsBuilder.get()
								.withLegend(LegendBuilder.get()
										.withPosition(Position.bottom)
										.build())
								.build())
						.build());

		return  chartBuilder.build();
	}

	public ApexCharts multiRadialBarChartExample(double sumPria, double sumWanita, String title) {
		List<String> fillColors = new ArrayList<>();
		fillColors.add("#1b4fde");
		fillColors.add("#ed45d1");
		FillBuilder fillBuilder = FillBuilder.get().withColors(fillColors);

		TitleSubtitleBuilder subTitleBuilder = TitleSubtitleBuilder.get().withText(title);
		ApexChartsBuilder chartBuilder = new ApexChartsBuilder();
		chartBuilder.withChart(ChartBuilder.get()
				.withType(Type.radialBar)
				.withWidth("100px")
				.build())
				.withPlotOptions(PlotOptionsBuilder.get()
						.withRadialBar(RadialBarBuilder.get()
								.withHollow(HollowBuilder.get()
										.withSize("55%")
										.build())
								.build())
						.build())
				.withSeries(sumPria, sumWanita)
//				.withSeries(sumPria)
				.withColors("#1b4fde", "#ed45d1")
//				.withColors("#1b4fde")
				.withFill(fillBuilder.build())
				.withLabels("Laki-laki", "Perempuan")
//				.withLabels("Laki-laki")
				.withTitle(subTitleBuilder.build())
				.withLegend(LegendBuilder.get()
						.withPosition(Position.right)
						.build());

		return  chartBuilder.build();
	}


	public class RadialBarChartExample extends Div {
		public RadialBarChartExample() {
			ApexCharts radialBarChart = ApexChartsBuilder.get()
					.withChart(ChartBuilder.get()
							.withType(Type.radialBar)
							.build())
					.withPlotOptions(PlotOptionsBuilder.get()
							.withRadialBar(RadialBarBuilder.get()
									.withHollow(HollowBuilder.get()
											.withSize("70%")
											.build())
									.build())
							.build())
					.withSeries(70.0)
					.withLabels("Cricket")
					.build();
			add(radialBarChart);
			setWidth("100%");
		}
	}

	public ApexCharts gradientRadialBarChartExample(double persenPria, double persenWanita, int sumMale, int sumFemale, String title) {
		TitleSubtitleBuilder subTitleBuilder = TitleSubtitleBuilder.get().withText(title);
		ApexCharts gradientRadialBarChart = ApexChartsBuilder.get()
				.withChart(ChartBuilder.get()
						.withType(Type.radialBar)
						.withToolbar(ToolbarBuilder.get().withShow(true).build())
						.build())
				.withPlotOptions(PlotOptionsBuilder.get().withRadialBar(RadialBarBuilder.get()
//						.withStartAngle(-135.0)
//						.withEndAngle(225.0)
						.withHollow(HollowBuilder.get()
								.withMargin(0.0)
//								.withSize("70%")
								.withSize("60%")
								.withBackground("#fff")
								.withPosition(HollowPosition.front)
								.withDropShadow(DropShadowBuilder.get()
										.withEnabled(true)
										.withTop(3.0)
										.withBlur(4.0)
										.withOpacity(0.24)
										.build())
								.build())
						.withTrack(TrackBuilder.get()
								.withBackground("#fff")
								.withStrokeWidth("67%")
								.withDropShadow(DropShadowBuilder.get()
										.withTop(-3.0)
										.withLeft(0.0)
										.withBlur(4.0)
										.withOpacity(0.35)
										.build())
								.build())
						.withDataLabels(RadialBarDataLabelsBuilder.get()
								.withShow(true)
								.withName(NameBuilder.get()
										.withOffsetY(-10.0)
										.withShow(true)
										.withColor("#888")
										.withFontSize("17px")
										.build())
								.withValue(ValueBuilder
										.get()
										.withColor("#111")
										.withFontSize("36px")
										.withShow(true)
										.build())
								.build())
						.build())
						.build())
				.withFill(FillBuilder.get()
						.withType("gradient")
						.withGradient(GradientBuilder.get()
								.withShade("dark")
								.withType("horizontal")
								.withShadeIntensity(0.5)
								.withGradientToColors("#ABE5A1")
								.withInverseColors(true)
								.withOpacityFrom(1.0)
								.withOpacityTo(1.0)
								.withStops(0.0, 100.0)
								.build())
						.build())
//				.withSeries(75.0)
				.withSeries(persenPria, persenWanita)
				.withStroke(StrokeBuilder.get()
						.withLineCap(LineCap.round)
						.build())
//				.withLabels("Percent")
				.withLabels("Laki-laki " + sumMale +  " orang", "Perempuan " + sumFemale + " orang")
				.withTitle(subTitleBuilder.build())
				.build();
//			add(gradientRadialBarChart);
//			setWidth("100%");

		return gradientRadialBarChart;
	}


}
