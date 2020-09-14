package com.desgreen.education.siapp.ui.views.ppdb_list;

import com.vaadin.flow.component.HasValue;

public interface PpdbListListener {
    void valueChangeListenerSearch(HasValue.ValueChangeEvent e);
    void aksiBtnReloadFromDb();
    void aksiBtnNewForm();
    void aksiBtnDeleteForm();

    void aksiBtnSaveForm();
    void aksiBtnCancelForm();

}
