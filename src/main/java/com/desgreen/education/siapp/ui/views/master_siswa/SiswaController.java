package com.desgreen.education.siapp.ui.views.master_siswa;

import com.desgreen.education.siapp.AppPublicService;
import com.desgreen.education.siapp.backend.model.EnumStatApproval;
import com.desgreen.education.siapp.backend.model.FSiswa;
import com.desgreen.education.siapp.backend.model.FtKrs;
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

public class SiswaController implements SiswaListener {

    protected SiswaModel model;
    protected SiswaView view;


    @Autowired
    public SiswaController(SiswaModel model, SiswaView view){
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
        model.currentDomain = new FSiswa();
        view.showDetails(model.currentDomain);
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

        //siswa belum pernah mendaftar
        boolean isCalonSiswaBaru = true;
        for (FtKrs ftKrsBean: model.currentDomain.getFtKrsSet()) {
            if (ftKrsBean.getEnumStatApproval().equals(EnumStatApproval.APPROVE)) {
                isCalonSiswaBaru=false;
                break;
            }
        }

        if (isCalonSiswaBaru){
            for (FtKrs ftKrsBean: model.currentDomain.getFtKrsSet()) {
                
                model.ftKrsJPARepository.delete(ftKrsBean);
            }

            try {
                File oldFile = new File(AppPublicService.FILE_PATH + model.currentDomain.getImageName());
                if (oldFile.exists()) oldFile.delete();
            }catch (Exception ex){}

            model.fSiswaJPARepository.delete(model.currentDomain);
            model.mapHeader.remove(model.currentDomain.getId());

            view.grid.deselectAll();

            view.dataProvider = DataProvider.ofCollection( model.mapHeader.values() );
            view.dataProvider.refreshAll();
            view.grid.setDataProvider(view.dataProvider);
            view.filter();

            /**
             * Hapus usernya juga
             */
            model.usersJPARepository.deleteByIdSiswa(model.currentDomain.getId());

            UIUtils.showNotification("DATA DELETED!! ");
        }else {
            UIUtils.showNotification("Tidak bisa hapus karena Siswa Sudah Melakukan KRS ");
        }

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

                //Set Nama Image::
                model.currentDomain.setImageName("_" + System.currentTimeMillis() + "_" + view.imageOuput.getTitle() );
            }

//          System.out.println("NAMA IMAGE: " + model.currentDomain.getImageName());

            model.currentDomain = model.fSiswaJPARepository.save(model.currentDomain);
            model.mapHeader.put(model.currentDomain.getId(), model.currentDomain);

            //Lakukan Upload Image to Disk
            if (! model.currentDomain.getImageName().equals("")) {
                CommonFileFactory.writeStreamToFile(view.buffer.getInputStream(), model.currentDomain.getImageName());
            }

            if (newDomain) {
                view.dataProvider = DataProvider.ofCollection( model.mapHeader.values() );
                view.dataProvider.refreshAll();
                view.grid.setDataProvider(view.dataProvider);
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
