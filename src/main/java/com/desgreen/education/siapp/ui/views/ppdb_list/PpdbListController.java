package com.desgreen.education.siapp.ui.views.ppdb_list;

import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.data.provider.DataProvider;
import org.springframework.beans.factory.annotation.Autowired;

public class PpdbListController implements PpdbListListener {

    protected PpdbListModel model;
    protected PpdbListView view;

    @Autowired
    public PpdbListController(PpdbListModel model, PpdbListView view){
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
