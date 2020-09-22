package com.desgreen.education.siapp.ui.views.transaksi_krs_validasi;

import com.helger.commons.csv.CSVWriter;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.VaadinService;
import org.apache.commons.io.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

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

    @Override
    public void aksiBtnPrint() {
        Anchor anchor = new Anchor(new StreamResource("grid.csv", this::getInputStream), "Export as CSV");

        new StreamResource("grid.csv", this::getInputStream);
        System.out.println("Hello sudah print mas");
    }

    public StreamResource getStreamResource(){
        return  new StreamResource("grid.csv", this::getInputStream);
    }
    public InputStream getInputStream() {

//        XSSFWorkbook workbook = new XSSFWorkbook();
//        XSSFSheet sheet = workbook.createSheet("DES Sheet1");
//
//        Map<Integer, Object[]> data = new HashMap<>();
//        data.put(1, new Object[] { "Div",
//                "SpCode", "Salesman"
//
//        });

        try {
            StringWriter stringWriter = new StringWriter();

            CSVWriter csvWriter = new CSVWriter(stringWriter);
            csvWriter.writeNext("id", "name");
            model.mapHeader.values().forEach(c -> csvWriter.writeNext("" + c.getFsiswaBean().getFullName(), c.getFkurikulumBean().getFmatPelBean().getDescription()));

            return IOUtils.toInputStream(stringWriter.toString(), "UTF-8");

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
