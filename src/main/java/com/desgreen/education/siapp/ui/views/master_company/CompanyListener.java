package com.desgreen.education.siapp.ui.views.master_company;

import com.vaadin.flow.component.HasValue;

public interface CompanyListener {
    void valueChangeListenerSearch(HasValue.ValueChangeEvent e);
    void aksiBtnReloadFromDb();
    void aksiBtnNewForm();
    void aksiBtnDeleteForm();

    void aksiBtnSaveForm();
    void aksiBtnCancelForm();

}
