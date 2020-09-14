package com.desgreen.education.siapp.ui.views.transaksi_krs_validasi;

import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.data.provider.DataProvider;
import org.springframework.beans.factory.annotation.Autowired;

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
}
