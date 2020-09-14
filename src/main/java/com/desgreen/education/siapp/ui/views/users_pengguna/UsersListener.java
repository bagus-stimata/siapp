package com.desgreen.education.siapp.ui.views.users_pengguna;

import com.vaadin.flow.component.HasValue;

public interface UsersListener {
    void valueChangeListenerSearch(HasValue.ValueChangeEvent e);
    void aksiBtnReloadFromDb();
    void aksiBtnNewForm();
    void aksiBtnDeleteForm();

    void aksiBtnSaveForm();
    void aksiBtnCancelForm();

}
