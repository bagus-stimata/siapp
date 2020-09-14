package com.desgreen.education.siapp.ui.views.master_kurikulum;

import com.vaadin.flow.component.HasValue;

public interface KurikulumListener {
    void valueChangeListenerSearch(HasValue.ValueChangeEvent e);
    void aksiBtnReloadFromDb();
    void aksiBtnNewForm();
    void aksiBtnDeleteForm();

    void aksiBtnSaveForm();
    void aksiBtnCancelForm();

    void aksiBtnExtractExcel();


}
