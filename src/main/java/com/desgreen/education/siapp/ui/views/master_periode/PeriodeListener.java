package com.desgreen.education.siapp.ui.views.master_periode;

import com.vaadin.flow.component.HasValue;

public interface PeriodeListener {
    void valueChangeListenerSearch(HasValue.ValueChangeEvent e);
    void aksiBtnReloadFromDb();
    void aksiBtnNewForm();
    void aksiBtnDeleteForm();

    void aksiBtnSaveForm();
    void aksiBtnCancelForm();

}
