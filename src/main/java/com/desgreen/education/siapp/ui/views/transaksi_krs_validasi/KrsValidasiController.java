package com.desgreen.education.siapp.ui.views.transaksi_krs_validasi;

import com.desgreen.education.siapp.AppPublicService;
import com.desgreen.education.siapp.backend.model.FKurikulum;
import com.desgreen.education.siapp.backend.model.FtKrs;
import com.desgreen.education.siapp.backend.model.ZLapTemplate2;
import com.desgreen.education.siapp.ui.util.UIUtils;
import com.desgreen.education.siapp.ui.utils.common.CommonDateFormat;
import com.desgreen.education.siapp.ui.utils.common.CommonFileFactory;
import com.desgreen.education.siapp.ui.utils.common.CommonJasperFactory;
import com.helger.commons.csv.CSVWriter;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.server.StreamResource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.commons.io.IOUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class KrsValidasiController implements KrsValidasiListener {

    protected KrsValidasiModel model;
    protected KrsValidasiView view;

    @Autowired
    public KrsValidasiController(KrsValidasiModel model, KrsValidasiView view){
        this.model = model;
        this.view = view;

    }

    @Override
    public void valueChangeListenerSearch(HasValue.ValueChangeEvent e) {
        if (e !=null) {
            view.setFilter(e.getValue().toString());
        }
    }

    @Override
    public void aksiBtnReloadFromDb() {
        view.grid.deselectAll();
        model.initVariableData();
        view.dataProvider = DataProvider.ofCollection(model.mapHeader.values());
        view.dataProvider.refreshAll();
        view.grid.setDataProvider(view.dataProvider);

        view.filter();
    }

    @Override
    public void aksiBtnNewForm() {
    }

    @Override
    public void aksiBtnDeleteForm() {

    }
    public void aksiBtnDeleteProcess(){
    }

    @Override
    public void aksiBtnSaveForm() {

    }

    @Override
    public void aksiBtnCancelForm() {
    }


    public StreamResource getStreamResource_Csv(){
        return  new StreamResource("data_siswa.csv", this::getInputStream_Csv);
    }
    public InputStream getInputStream_Csv() {
        try {
            StringWriter stringWriter = new StringWriter();

            CSVWriter csvWriter = new CSVWriter(stringWriter);
            csvWriter.writeNext(
                    "MatPel", "Periode",
                    "Nama Lengkap",
                    "Jns Kelamin", "Menikah",
                    "Alamat", "Kota",
                    "Telepon", "Email",
                    "Tempat Lahir", "Tanggal Lahir",
                    "Catatan1", "Catatan2"

            );
            view.dataProvider.getItems().stream().filter(ftKrs -> String.valueOf(ftKrs.getFkurikulumBean().getId()).equals(view.selectedTabId))
                    .collect(Collectors.toList()).forEach(c ->{
                        csvWriter.writeNext(
                                c.getFkurikulumBean().getFmatPelBean().getDescription(), UIUtils.formatDate_ddMMMYYYY(c.getFkurikulumBean().getFperiodeBean().getPeriodeFrom()) + " S. D " + UIUtils.formatDate_ddMMMYYYY(c.getFkurikulumBean().getFperiodeBean().getPeriodeTo()),
                                c.getFsiswaBean().getFullName(),
                                c.getFsiswaBean().isSex() ? "Laki-Laki" : "Perempuan", c.getFsiswaBean().isMenikah() ? "Menikah" : "Belum",
                                c.getFsiswaBean().getAddress1() + " " + c.getFsiswaBean().getAddress2() + " " + c.getFsiswaBean().getAddress3(), c.getFsiswaBean().getCity(),
                                c.getFsiswaBean().getPhone(), c.getFsiswaBean().getEmail(),
                                c.getFsiswaBean().getBirthPlace(), UIUtils.formatDate_ddMMMYYYY(c.getFsiswaBean().getBirthDate()),
                                c.getFsiswaBean().getNotes1(), c.getFsiswaBean().getNotes2()
                        );
                    }
            );

            return IOUtils.toInputStream(stringWriter.toString(), "UTF-8");

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public StreamResource getStreamResource_Excel(){
        StreamResource resource = new StreamResource("data_siswa.xlsx", this::getInputStream_Excel);
//        resource.setContentType("application/vnd.ms-excel");

        return  resource;
    }
    public InputStream getInputStream_Excel() {

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("DES Sheet1");

        Map<Integer, Object[]> data = new HashMap<>();
        int lastRow = 1;
        data.put(lastRow++, new Object[]{
                "MatPel", "Periode",
                "Nama Lengkap",
                "Jns Kelamin", "Menikah",
                "Alamat", "Kota",
                "Telepon", "Email",
                "Tempat Lahir", "Tanggal Lahir",
                "Catatan1", "Catatan2"
        });

        for (FtKrs c : view.dataProvider.getItems().stream().filter(ftKrs -> String.valueOf(ftKrs.getFkurikulumBean().getId()).equals(view.selectedTabId))
                .collect(Collectors.toList())) {
            data.put(lastRow++, new Object[]{
                    c.getFkurikulumBean().getFmatPelBean().getDescription(), UIUtils.formatDate_ddMMMYYYY(c.getFkurikulumBean().getFperiodeBean().getPeriodeFrom()) + " S. D " + UIUtils.formatDate_ddMMMYYYY(c.getFkurikulumBean().getFperiodeBean().getPeriodeTo()),
                    c.getFsiswaBean().getFullName(),
                    c.getFsiswaBean().isSex() ? "Laki-Laki" : "Perempuan", c.getFsiswaBean().isMenikah() ? "Menikah" : "Belum",
                    c.getFsiswaBean().getAddress1() + " " + c.getFsiswaBean().getAddress2() + " " + c.getFsiswaBean().getAddress3(), c.getFsiswaBean().getCity(),
                    c.getFsiswaBean().getPhone(), c.getFsiswaBean().getEmail(),
                    c.getFsiswaBean().getBirthPlace(), CommonDateFormat.fromLocalDate(c.getFsiswaBean().getBirthDate()),
                    c.getFsiswaBean().getNotes1(), c.getFsiswaBean().getNotes2()
            });
        }


        sheet.setColumnWidth(2, sheet.getColumnWidth(1) * 3);
        sheet.setColumnWidth(5, sheet.getColumnWidth(1) * 4);
        sheet.setColumnWidth(8, sheet.getColumnWidth(1) * 3);
//        sheet.setColumnWidth(10, sheet.getColumnWidth(10) * 3);
//        sheet.setColumnWidth(12, sheet.getColumnWidth(12) * 2);

        //#Cell Style
        XSSFFont font_title_Bold = workbook.createFont();
        font_title_Bold.setBold(true);
        font_title_Bold.setColor(IndexedColors.BLACK.getIndex());

        CellStyle style_TitleRow_Bold = workbook.createCellStyle();
        style_TitleRow_Bold.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
        style_TitleRow_Bold.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style_TitleRow_Bold.setFont(font_title_Bold);

        CellStyle style_TitleRow = workbook.createCellStyle();
        style_TitleRow.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
        style_TitleRow.setFillPattern(CellStyle.SOLID_FOREGROUND);

        CellStyle cellStyle_Date = workbook.createCellStyle();
        CreationHelper createHelper = workbook.getCreationHelper();
        cellStyle_Date.setDataFormat(
                createHelper.createDataFormat().getFormat("dd MMM yyyy"));

        Set<Integer> keyset = data.keySet();
        int rownum = 0; //TIDAK MAU KALAU BANYAK BANGET
        for (Integer key : keyset) {
            Row row = sheet.createRow(rownum++);
            Object [] objArr = data.get(key);
            int cellnum = 0;
            for (Object obj : objArr) {
                Cell cell = row.createCell(cellnum++);

                //#Style -> can't be here
                if (cell.getRowIndex()==0 &&
                        (cell.getColumnIndex()==2 || cell.getColumnIndex()==5 || cell.getColumnIndex()==10 || cell.getColumnIndex()==12 ||
                                cell.getColumnIndex()==14 || cell.getColumnIndex()==14 )) {
                    cell.setCellStyle(style_TitleRow_Bold);
                }else if (cell.getRowIndex()==0) {
                    cell.setCellStyle(style_TitleRow);
                }

                if(obj instanceof Date) {
                    cell.setCellValue((Date) obj);
                    cell.setCellStyle(cellStyle_Date);
                }else if(obj instanceof Boolean)
                    cell.setCellValue((boolean) obj);
                else if(obj instanceof String)
                    cell.setCellValue((String) obj);
                else if(obj instanceof Double)
                    cell.setCellValue((double) obj);
                else if(obj instanceof Float)
                    cell.setCellValue((double) obj);
                else if(obj instanceof Integer)
                    cell.setCellValue((int) obj);
            }
        }

        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            workbook.write(baos);
            baos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        InputStream is = new ByteArrayInputStream(baos.toByteArray());
        return  is;
    }




    List<ZLapTemplate2> listLapTempate = new ArrayList<>();
    public void fillTemplate(){

    }

    public StreamResource getStreamResource_JasperReport(String jasperFile, String outputFileName) {
        ZLapTemplate2 lapTemplate = new ZLapTemplate2();
        lapTemplate.setId(1);
        lapTemplate.setString1("Hello coba");
        listLapTempate.add(lapTemplate);

        return CommonJasperFactory.getStreamResource_Jrxml(listLapTempate, new HashMap(), jasperFile, outputFileName);
    }
}
