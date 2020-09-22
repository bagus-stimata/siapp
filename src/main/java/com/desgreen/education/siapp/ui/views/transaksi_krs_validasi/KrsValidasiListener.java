package com.desgreen.education.siapp.ui.views.transaksi_krs_validasi;

import com.vaadin.flow.component.HasValue;

public interface KrsValidasiListener {
    void valueChangeListenerSearch(HasValue.ValueChangeEvent e);
    void aksiBtnReloadFromDb();
    void aksiBtnPrint();
    void aksiBtnNewForm();
    void aksiBtnDeleteForm();

    void aksiBtnSaveForm();
    void aksiBtnCancelForm();

}
