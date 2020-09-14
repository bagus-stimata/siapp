package com.desgreen.education.siapp.ui.views.transaksi_krs_detail;

import com.vaadin.flow.component.HasValue;

public interface KrsDetailListener {
    void valueChangeListenerSearch(HasValue.ValueChangeEvent e);
    void aksiBtnReloadFromDb();
    void aksiBtnNewForm();
    void aksiBtnDeleteForm();

    void aksiBtnDaftarOrBatalForm();
    void aksiBtnCancelForm();

}
