package com.desgreen.education.siapp.ui.views.transaksi_krs_validasi_detail;

import com.desgreen.education.siapp.backend.model.EnumStatApproval;
import com.desgreen.education.siapp.security_model.FUser;
import com.desgreen.education.siapp.security_model.FUserRoles;
import com.desgreen.education.siapp.security_model.Role;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.claspina.confirmdialog.ButtonOption;
import org.claspina.confirmdialog.ConfirmDialog;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

public class KrsValidasiDetailController implements KrsValidasiDetailListener {

    protected KrsValidasiDetailModel model;
    protected KrsValidasiDetailView view;

    @Autowired
    public KrsValidasiDetailController(KrsValidasiDetailModel model, KrsValidasiDetailView view){
//        this.model = new tKrsModel();
        this.model = model;
        this.view = view;

    }

    @Override
    public void valueChangeListenerSearch(HasValue.ValueChangeEvent e) {
        if (e !=null) {
        }
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
    public void aksiBtnTolakForm() {

        final boolean isSelected = model.currentDomain.isStatSelected();

        VerticalLayout messageLayout = new VerticalLayout();
        messageLayout.add(new Label("Apakah Yakin Mau Membatalkan Persetujuan"));
        messageLayout.add(new Label("Periksa dengan baik"));
        ConfirmDialog.createQuestion()
                .withCaption("Konfirmasi Pembatalan")

                .withMessage(messageLayout)
                // .withMessage("Do you want to continue?, Do you want to continue?, \nDo you want to continue?")

                .withOkButton(() -> {
                    aksiProsesBatal();

                }, ButtonOption.caption("YES").icon(VaadinIcon.PAPERPLANE))
                // .withCancelButton(ButtonOption.caption("NO"))
                .withCancelButton(() -> {
                    //                     System.out.println("No. Implement logic here.");

                }, ButtonOption.focus(), ButtonOption.caption("No").icon(VaadinIcon.EXIT))
                .withWidthForAllButtons("150px")
                .open();


    }

    @Override
    public void aksiBtnSetujuForm() {
        VerticalLayout messageLayout = new VerticalLayout();
        messageLayout.add(new Label("Apakah Yakin Melakukan Persetujuan"));
        messageLayout.add(new Label(""));
        ConfirmDialog.createQuestion()
                .withCaption("Konfirmasi Daftar")

                .withMessage(messageLayout)
                // .withMessage("Do you want to continue?, Do you want to continue?, \nDo you want to continue?")

                .withOkButton(() -> {
                    aksiProsesSetuju();

                }, ButtonOption.caption("YES").icon(VaadinIcon.PAPERPLANE))
                // .withCancelButton(ButtonOption.caption("NO"))
                .withCancelButton(() -> {
                    //                     System.out.println("No. Implement logic here.");

                }, ButtonOption.focus(), ButtonOption.caption("No").icon(VaadinIcon.EXIT))
                .withWidthForAllButtons("150px")
                .open();

    }

    protected  void aksiProsesSetuju(){

        model.currentDomain.setEnumStatApproval(EnumStatApproval.APPROVE);
        model.currentDomain = model.ftKrsJPARepository.save(model.currentDomain);

        model.mapKrsCurrentUser.put(model.currentDomain.getId(), model.currentDomain);
        view.goToParentView_ToRefresh();

        FUser userBean = new FUser();
        try {
            userBean = model.usersJPARepository.findByIdSiswa(model.currentDomain.getFsiswaBean().getId());
            aktifkanUser(userBean);
        }catch (Exception ex){}
        try {
            String mailTo = model.currentDomain.getFsiswaBean().getEmail();
            if (mailTo.equals("")) {
                try {
                    mailTo = userBean.getEmail();
                }catch (Exception ex){}

            }
            String subject = "Pendaftaran Materi/Asrama";
            String textMessage = "Assalamualaikum Wr. Wb\n" +
                    "Kami Pengurus Pondok/Sekolah " + model.currentDomain.getFdivisionBean().getFcompanyBean().getDescription() +
                    " " + model.currentDomain.getFdivisionBean().getDescription() + ", Menerima Pendaftaran Anda\n" +
                    " Untuk Informasi Lebih Lanjut Silahkan Melakukan Login dan Melihat Informasi-informasi penting dalam aplikasi\n\n" +
                    "Alhamdulillahi Jaza Kumullohu Khoiron, Assalamualaikum Wr.Wb" ;
            model.emailService.sendSimpleMessage(mailTo, subject, textMessage);
        }catch (Exception ex){}


    }

    protected  void aksiProsesBatal() {

        model.currentDomain.setEnumStatApproval(EnumStatApproval.REJECTED);
        model.currentDomain = model.ftKrsJPARepository.save(model.currentDomain);

        model.mapKrsCurrentUser.put(model.currentDomain.getId(), model.currentDomain);
        view.goToParentView_ToRefresh();

        try {
            String mailTo = model.currentDomain.getFsiswaBean().getEmail();
            if (mailTo.equals("")) {
                try {
                    FUser userBean = model.usersJPARepository.findByIdSiswa(model.currentDomain.getFsiswaBean().getId());
                    mailTo = userBean.getEmail();
                }catch (Exception ex){}

            }
            String subject = "Pendaftaran Materi/Asrama";
            String textMessage = "Assalamualaikum Wr. Wb\n" +
                    "Kami Pengurus Pondok/Sekolah " + model.currentDomain.getFdivisionBean().getFcompanyBean().getDescription() +
                    " " + model.currentDomain.getFdivisionBean().getDescription() + ", Dengan Sangat Menyesal Belum Dapat Menerima Pendaftaran Anda\n" +
                    " Untuk Informasi Lebih Lanjut Silahkan Hubungi Telp +62" + model.currentDomain.getFdivisionBean().getFcompanyBean().getPhoneNumber()+ " \n\n" +
                    "Alhamdulillahi Jaza Kumullohu Khoiron, Assalamualaikum Wr.Wb" ;
            model.emailService.sendSimpleMessage(mailTo, subject, textMessage);
        }catch (Exception ex){}

    }

    @Override
    public void aksiBtnCancelForm() {

    }

    public void aktifkanUser(FUser userBean){
        if (userBean !=null ) {
            List<FUserRoles> listFUserRoles = userBean.getFuserRoles();
            if (listFUserRoles.stream().filter(x -> x.getRoleID().equals(Role.MNU_PPDB_KRS)).collect(Collectors.toList()).size()==0) {
                FUserRoles newFUserRoles = new FUserRoles();
                newFUserRoles.setFuserBean(userBean);
                newFUserRoles.setSelected(true);
                newFUserRoles.setRoleID(Role.MNU_PPDB_KRS);

                model.userRolesJPARepository.save(newFUserRoles);
            }

        }

    }

}
