package com.desgreen.education.siapp.ui.views.users_pengguna;

import com.desgreen.education.siapp.AppPublicService;
import com.desgreen.education.siapp.security_config.PassEncoding;
import com.desgreen.education.siapp.security_model.FUser;
import com.desgreen.education.siapp.ui.util.UIUtils;
import com.desgreen.education.siapp.ui.utils.common.CommonFileFactory;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.DataProvider;
import org.claspina.confirmdialog.ButtonOption;
import org.claspina.confirmdialog.ConfirmDialog;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.util.HashMap;
import java.util.stream.Collectors;

public class UsersController implements UsersListener {

    protected UsersModel model;
    protected UsersView view;


    @Autowired
    public UsersController(UsersModel model, UsersView view){
//        this.model = new CompanyModel();
        this.model = model;
        this.view = view;

    }

    @Override
    public void valueChangeListenerSearch(HasValue.ValueChangeEvent e) {
        if (e !=null) {
            view.setFilter(e.getValue().toString());
        }
    }

    @Override
    public void aksiBtnReloadFromDb() {
        view.grid.deselectAll();
        model.initVariableData();
        view.dataProvider = DataProvider.ofCollection(model.mapHeader.values());
        view.dataProvider.refreshAll();
        view.grid.setDataProvider(view.dataProvider);
    }

    @Override
    public void aksiBtnNewForm() {
        view.grid.deselectAll();
        model.currentDomain = new FUser();
        view.showDetails(model.currentDomain);
        view.username.focus();
    }


    @Override
    public void aksiBtnDeleteForm() {

        if (model.currentDomain != null) {
            VerticalLayout messageLayout = new VerticalLayout();
            messageLayout.add(new Label("Apakah akan Menghapus Data"));
            messageLayout.add(new Label(model.currentDomain.getFullName()));
             ConfirmDialog.createQuestion()
                 .withCaption("KONFIRMASI HAPUS")

                 .withMessage(messageLayout)
                 // .withMessage("Do you want to continue?, Do you want to continue?, \nDo you want to continue?")

                 .withOkButton(() -> {
                        aksiBtnDeleteProcess();

                     }, ButtonOption.caption("YES").icon(VaadinIcon.TRASH))
                 // .withCancelButton(ButtonOption.caption("NO"))
                 .withCancelButton(() -> {
    //                     System.out.println("No. Implement logic here.");

                      }, ButtonOption.focus(), ButtonOption.caption("No").icon(VaadinIcon.EXIT))
                 .withWidthForAllButtons("150px")
                 .open();

        }

    }
    public void aksiBtnDeleteProcess(){

        try {
            File oldFile = new File(AppPublicService.FILE_PATH + model.currentDomain.getImageName());
            if (oldFile.exists()) oldFile.delete();
        }catch (Exception ex){}

        view.grid.deselectAll();
//            viewLogic.deleteProduct(currentProduct);
        model.deleteCurrentRole(model.currentDomain.getFuserRoles());
        model.mapTempUserRoles = new HashMap<>();
        model.userJPARepository.delete(model.currentDomain);
        model.mapHeader.remove(model.currentDomain.getId());

        view.dataProvider = DataProvider.ofCollection( model.mapHeader.values() );
        view.dataProvider.refreshAll();
        view.grid.setDataProvider(view.dataProvider);

        view.filter();

        UIUtils.showNotification("DATA DELETED!! ");

    }


    @Override
    public void aksiBtnSaveForm() {

        final boolean newDomain = model.currentDomain.isNewDomain();
        if (model.currentDomain != null
                && view.binder.writeBeanIfValid(model.currentDomain)) {

            if (newDomain) {
                model.currentDomain.setFdivisionBean(model.userActive.getFdivisionBean());
            }

            if ( view.isImageChange && ! newDomain) {
                //Hapus dahulu file Lama
                File oldFile = new File(AppPublicService.FILE_PATH + model.currentDomain.getImageName());
                if (oldFile.exists()) oldFile.delete();

                //Set Nama Image
                model.currentDomain.setImageName(model.userActive.getFdivisionBean().getFcompanyBean().getId()
                        + "_" + System.currentTimeMillis() + "_" + view.imageOuput.getTitle().get() );
            }

            if (view.password_manual.getValue() != null) {
                if (! view.password_manual.getValue().equals("")) {
                    model.currentDomain.setPassword(PassEncoding.getInstance().passwordEncoder.encode(view.password_manual.getValue().trim() ));
                }
            }

            model.currentDomain = model.userJPARepository.save(model.currentDomain);

            /**
             * UPDATE ROLES
             */
            model.deleteCurrentRole(model.currentDomain.getFuserRoles());
            model.saveUpdateUserRoles(model.mapTempUserRoles.values());

            model.currentDomain.setFuserRoles(model.mapTempUserRoles.values().stream().collect(Collectors.toList()));

            model.currentDomain = model.getFUserFromDb(model.currentDomain.getId());
            model.mapHeader.put(model.currentDomain.getId(), model.currentDomain);

            //Lakukan Upload Image to Disk
            if ( ! model.currentDomain.getImageName().equals("") && view.isImageChange) {
                CommonFileFactory.writeStreamToFile(view.buffer.getInputStream(), model.currentDomain.getImageName());
            }

            if (newDomain) {
                view.dataProvider = DataProvider.ofCollection( model.mapHeader.values() );
                view.dataProvider.refreshAll();
                view.grid.setDataProvider(view.dataProvider);
                view.filter();
            } else {
                view.dataProvider.refreshItem(model.currentDomain);
            }

            UIUtils.showNotification("DATA SAVED!! ");
            view.detailsDrawer.hide();
        }


    }

    @Override
    public void aksiBtnCancelForm() {

    }
}
