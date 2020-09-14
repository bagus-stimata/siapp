package com.desgreen.education.siapp.ui.views.master_komp_biaya;

import com.vaadin.flow.component.HasValue;

public interface KompBiayaListener {
    void valueChangeListenerSearch(HasValue.ValueChangeEvent e);
    void aksiBtnReloadFromDb();
    void aksiBtnNewForm();
    void aksiBtnDeleteForm();

    void aksiBtnSaveForm();
    void aksiBtnCancelForm();

}
