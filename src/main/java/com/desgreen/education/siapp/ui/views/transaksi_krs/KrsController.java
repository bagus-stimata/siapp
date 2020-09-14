package com.desgreen.education.siapp.ui.views.transaksi_krs;

import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.data.provider.DataProvider;
import org.springframework.beans.factory.annotation.Autowired;

public class KrsController implements KrsListener {

    protected KrsModel model;
    protected KrsView view;

    @Autowired
    public KrsController(KrsModel model, KrsView view){
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

        if (view.currentDomain != null) {
        }

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
