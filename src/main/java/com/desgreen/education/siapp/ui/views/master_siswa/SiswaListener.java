package com.desgreen.education.siapp.ui.views.master_siswa;

import com.vaadin.flow.component.HasValue;

public interface SiswaListener {
    void valueChangeListenerSearch(HasValue.ValueChangeEvent e);
    void aksiBtnReloadFromDb();
    void aksiBtnNewForm();
    void aksiBtnDeleteForm();

    void aksiBtnSaveForm();
    void aksiBtnCancelForm();

}
