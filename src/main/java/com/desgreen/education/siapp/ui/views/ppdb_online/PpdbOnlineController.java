package com.desgreen.education.siapp.ui.views.ppdb_online;

import com.desgreen.education.siapp.backend.model.*;
import com.desgreen.education.siapp.security_config.PassEncoding;
import com.desgreen.education.siapp.security_model.EnumOrganizationLevel;
import com.desgreen.education.siapp.ui.util.UIUtils;
import com.desgreen.education.siapp.ui.utils.common.CommonFileFactory;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.claspina.confirmdialog.ButtonOption;
import org.claspina.confirmdialog.ConfirmDialog;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

public class PpdbOnlineController implements PpdbOnlineListener {

    protected PpdbOnlineModel model;
    protected PpdbOnlineView view;


    @Autowired
    public PpdbOnlineController(PpdbOnlineModel model, PpdbOnlineView view){
//        this.model = new CompanyModel();
        this.model = model;
        this.view = view;

    }

    @Override
    public void valueChangeListenerSearch(HasValue.ValueChangeEvent e) {
    }

    @Override
    public void aksiBtnReloadFromDb() {
    }

    @Override
    public void aksiBtnNewForm() {
    }

    @Override
    public void aksiBtnDeleteForm() {
    }
    public void aksiBtnDeleteProcess(){
    }

    @Override
    public void aksiBtnSaveForm() {

        if (view.currentSiswa != null) {
            VerticalLayout messageLayout = new VerticalLayout();
            messageLayout.add(new Label("Yakin Kirim Pendaftaran"));
            ConfirmDialog.createQuestion()
                    .withCaption("Konfirmasi Pengiriman")

                    .withMessage(messageLayout)
                    // .withMessage("Do you want to continue?, Do you want to continue?, \nDo you want to continue?")
                    .withOkButton(() -> {

                        aksiSimpanProses();

                    }, ButtonOption.caption("YES").icon(VaadinIcon.CHECK))
                    // .withCancelButton(ButtonOption.caption("NO"))
                    .withCancelButton(() -> {
                        //                     System.out.println("No. Implement logic here.");

                    }, ButtonOption.focus(), ButtonOption.caption("No").icon(VaadinIcon.EXIT))
                    .withWidthForAllButtons("150px")
                    .open();
        }



    }

    public void aksiSimpanProses() {
        //Pasti New Domain
//        final boolean newDomain = view.currentDomain.isNewDomain();
        if (view.currentSiswa != null && view.isImageChange
                && view.binder.writeBeanIfValid(view.currentSiswa) && view.binderUser.writeBeanIfValid(view.currentUser) ) {

            try {
//                FDivision fDivisionBean = model.fDivisionJPARepository.findAll().get(0);
                FDivision fDivisionBean = view.currentKurikulum.getFdivisionBean();

                view.currentSiswa.setFdivisionBean(fDivisionBean);
            }catch (Exception ex){}

            /**
             * 1. Tambah Calon Siswa
             */
            view.currentSiswa.setStatSiswa(EnumStatSiswa.PPDB);
           view.currentSiswa.setEmail(view.email.getValue());

            view.currentSiswa.setImageName(view.currentSiswa.getFdivisionBean().getFcompanyBean().getId()
                    + "_" + System.currentTimeMillis() + "_" + view.imageOuput.getTitle() );

            //Save File
            view.currentSiswa = model.fSiswaJPARepository.save(view.currentSiswa);
            model.mapHeader.put(view.currentSiswa.getId(), view.currentSiswa);


            //Lakukan Upload Image to Disk
            if ( ! view.currentSiswa.getImageName().equals("") && view.isImageChange) {
                CommonFileFactory.writeStreamToFile(view.buffer.getInputStream(), view.currentSiswa.getImageName());
            }

//            //Lakukan File  Upload
//            if (! view.fileName_FileUploaded.equals("")) {
//                view.createFileComponent(view.currentSiswa.getImageName(), view.inputStream_FileUploaded);
//            }


            /**
             * 2. Tambah User
             */
            final String strPassword = view.currentUser.getPassword();

            view.currentUser.setIdSiswa(view.currentSiswa.getId());
            view.currentUser.setOrganizationLevel(EnumOrganizationLevel.DIV);
            view.currentUser.setUserType(EnumUserType.SISWA);
            view.currentUser.setFullName(view.currentSiswa.getFullName());

            view.currentUser.setPassword(PassEncoding.getInstance().passwordEncoder.encode(strPassword));
            if (view.currentUser.getFdivisionBean() ==null){
                view.currentUser.setFdivisionBean(view.currentSiswa.getFdivisionBean());
            }
            try {
                view.currentUser = model.usersJPARepository.save(view.currentUser);

            }catch (Exception ex){
                ex.printStackTrace();
            }

            /**
             * 3. Tambah KRS
             */
            FtKrs newFtKrs = new FtKrs();
            newFtKrs.setEnumStatApproval(EnumStatApproval.OPEN);
            newFtKrs.setFdivisionBean(view.currentSiswa.getFdivisionBean());
            newFtKrs.setStatSelected(true);
            newFtKrs.setSelectedDate(LocalDate.now());
            newFtKrs.setFsiswaBean(view.currentSiswa);
            newFtKrs.setFkurikulumBean(view.currentKurikulum);

            try {
                newFtKrs = model.ftKrsJPARepository.save(newFtKrs);
            }catch (Exception ex){
                ex.printStackTrace();
            }




            UIUtils.showNotification("PENDAFTARAN SUKSES");
            view.btnSubmit.setEnabled(false);
            view.btnSubmit.setText("Pendaftaran Sukses");
            view.btnSubmit.setIcon(new Icon(VaadinIcon.THUMBS_UP));

        }else {
            UIUtils.showNotification("Masih terdapat isian data yang belum lengkap!!");

        }
    }

    @Override
    public void aksiBtnCancelForm() {

    }
}
