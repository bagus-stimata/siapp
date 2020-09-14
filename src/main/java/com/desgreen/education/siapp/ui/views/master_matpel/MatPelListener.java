package com.desgreen.education.siapp.ui.views.master_matpel;

import com.vaadin.flow.component.HasValue;

public interface MatPelListener {
    void valueChangeListenerSearch(HasValue.ValueChangeEvent e);
    void aksiBtnReloadFromDb();
    void aksiBtnNewForm();
    void aksiBtnDeleteForm();

    void aksiBtnSaveForm();
    void aksiBtnCancelForm();

}
