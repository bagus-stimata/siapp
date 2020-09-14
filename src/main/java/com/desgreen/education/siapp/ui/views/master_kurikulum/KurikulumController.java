package com.desgreen.education.siapp.ui.views.master_kurikulum;

import com.desgreen.education.siapp.AppPublicService;
import com.desgreen.education.siapp.backend.model.FKurikulum;
import com.desgreen.education.siapp.backend.model.FtKrs;
import com.desgreen.education.siapp.ui.util.UIUtils;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.DataProvider;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.claspina.confirmdialog.ButtonOption;
import org.claspina.confirmdialog.ConfirmDialog;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class KurikulumController implements KurikulumListener {

    protected KurikulumModel model;
    protected KurikulumView view;

    @Autowired
    public KurikulumController(KurikulumModel model, KurikulumView view){
//        this.model = new KurikulumModel();
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
    }

    @Override
    public void aksiBtnNewForm() {
        view.grid.deselectAll();
        model.currentDomain = new FKurikulum();
        view.showDetails(model.currentDomain);
    }

    @Override
    public void aksiBtnDeleteForm() {

        if (model.currentDomain != null) {
            VerticalLayout messageLayout = new VerticalLayout();
            messageLayout.add(new Label("Apakah akan Menghapus Data"));
            messageLayout.add(new Label(model.currentDomain.getDescription()));
             ConfirmDialog.createQuestion()
                 .withCaption("KONFIRMASI HAPUS")

                 .withMessage(messageLayout)
                 // .withMessage("Do you want to continue?, Do you want to continue?, \nDo you want to continue?")

                 .withOkButton(() -> {
                        aksiBtnDeleteProcess();

                     }, ButtonOption.caption("YES").icon(VaadinIcon.TRASH))
                 // .withCancelButton(ButtonOption.caption("NO"))
                 .withCancelButton(() -> {
    //                     System.out.println("No. Implement logic here.");

                      }, ButtonOption.focus(), ButtonOption.caption("No").icon(VaadinIcon.EXIT))
                 .withWidthForAllButtons("150px")
                 .open();

        }

    }
    public void aksiBtnDeleteProcess(){
        view.grid.deselectAll();
//            viewLogic.deleteProduct(currentProduct);
        model.fKurikulumJPARepository.delete(model.currentDomain);
        model.mapHeader.remove(model.currentDomain.getId());

        view.dataProvider = DataProvider.ofCollection( model.mapHeader.values() );
        view.dataProvider.refreshAll();
        view.grid.setDataProvider(view.dataProvider);

        UIUtils.showNotification("DATA DELETED!! ");

    }

    @Override
    public void aksiBtnSaveForm() {

        final boolean newDomain = model.currentDomain.isNewDomain();
        if (model.currentDomain != null
                && view.binder.writeBeanIfValid(model.currentDomain)) {

            model.currentDomain = model.fKurikulumJPARepository.save(model.currentDomain);
            model.mapHeader.put(model.currentDomain.getId(), model.currentDomain);

            if (newDomain) {
                view.dataProvider = DataProvider.ofCollection( model.mapHeader.values() );
                view.dataProvider.refreshAll();
                view.grid.setDataProvider(view.dataProvider);
            } else {
                view.dataProvider.refreshItem(model.currentDomain);
            }

            UIUtils.showNotification("DATA SAVED!! ");
            view.detailsDrawer.hide();

        }



    }

    @Override
    public void aksiBtnCancelForm() {

    }

    @Override
    public void aksiBtnExtractExcel() {
        //1. ISI DATABASE UNTUK TEMP
//        fillDatabaseReportLengkap();

        //####CREATE FILE EXEL####
//        String basepath = VaadinService.getCurrent()
//                .getBaseDirectory().getAbsolutePath();
        String filePathDestination = AppPublicService.FILE_PATH + "/Peserta.xlsx";

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("SIAPP Sheet1");

        Map<Integer, Object[]> data = new HashMap<>();
        data.put(1, new Object[] {

                "NIS", "Nama Siswa",
        });

        int lastRow = 1;
        for (FtKrs domain: model.currentDomain.getFtKrsSet()) {
            lastRow++;
            try{
                data.put(lastRow, new Object[] {

                        domain.getFsiswaBean().getNis(), domain.getFsiswaBean().getFullName(),

                });
            } catch(Exception ex){}
        }

        Set<Integer> keyset = data.keySet();
        int rownum = 0; //TIDAK MAU KALAU BANYAK BANGET
        for (Integer key : keyset) {
            Row row = sheet.createRow(rownum++);
            Object [] objArr = data.get(key);
            int cellnum = 0;
            for (Object obj : objArr) {
                Cell cell = row.createCell(cellnum++);

                if(obj instanceof Date)
                    cell.setCellValue((Date) obj);
                else if(obj instanceof Boolean)
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

        //OUTPUT KE FILE
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            workbook.write(out);
            out.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

//        StreamResource.StreamSource source = new StreamSource() {
//            @Override
//            public InputStream getStream() {
//                byte[] b = null;
//                b = out.toByteArray();
//                return new ByteArrayInputStream(b);
//            }
//        };
//
//        String fileName = "kurikulum_siswa"  +System.currentTimeMillis() +".xlsx";
//        StreamResource resource = new StreamResource( source, fileName);
//        resource.setMIMEType("application/vnd.ms-excel");
//        resource.getStream().setParameter("Content-Disposition","attachment; filename="+fileName);
//
//        view.getUI().get().getPage().open(resource, "_new_" , false);

    }


}
