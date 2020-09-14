package com.desgreen.education.siapp.ui.views.ppdb_selectmatkul;

import com.vaadin.flow.component.HasValue;

public interface PpdbSelectMatkulListener {
    void valueChangeListenerSearch(HasValue.ValueChangeEvent e);
    void aksiBtnReloadFromDb();
    void aksiBtnNewForm();
    void aksiBtnDeleteForm();

    void aksiBtnDaftarOrBatalForm();
    void aksiBtnCancelForm();

}
