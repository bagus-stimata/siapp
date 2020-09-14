package com.desgreen.education.siapp.ui.views.transaksi_krs;

import com.vaadin.flow.component.HasValue;

public interface KrsListener {
    void valueChangeListenerSearch(HasValue.ValueChangeEvent e);
    void aksiBtnReloadFromDb();
    void aksiBtnNewForm();
    void aksiBtnDeleteForm();

    void aksiBtnSaveForm();
    void aksiBtnCancelForm();

}
