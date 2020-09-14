package com.desgreen.education.siapp.ui.views.master_division;

import com.vaadin.flow.component.HasValue;

public interface DivisionListener {
    void valueChangeListenerSearch(HasValue.ValueChangeEvent e);
    void aksiBtnReloadFromDb();
    void aksiBtnNewForm();
    void aksiBtnDeleteForm();

    void aksiBtnSaveForm();
    void aksiBtnCancelForm();

}
