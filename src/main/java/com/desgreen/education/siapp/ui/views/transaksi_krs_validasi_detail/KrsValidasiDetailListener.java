package com.desgreen.education.siapp.ui.views.transaksi_krs_validasi_detail;

import com.vaadin.flow.component.HasValue;

public interface KrsValidasiDetailListener {
    void valueChangeListenerSearch(HasValue.ValueChangeEvent e);
    void aksiBtnReloadFromDb();
    void aksiBtnNewForm();
    void aksiBtnDeleteForm();

    void aksiBtnSetujuForm();
    void aksiBtnTolakForm();
    void aksiBtnCancelForm();

}
