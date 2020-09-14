package com.desgreen.education.siapp.ui.views.ppdb_online_2;

import com.vaadin.flow.component.HasValue;

public interface PpdbOnlineListener {
    void valueChangeListenerSearch(HasValue.ValueChangeEvent e);
    void aksiBtnReloadFromDb();
    void aksiBtnNewForm();
    void aksiBtnDeleteForm();

    void aksiBtnSaveForm();
    void aksiBtnCancelForm();

}
