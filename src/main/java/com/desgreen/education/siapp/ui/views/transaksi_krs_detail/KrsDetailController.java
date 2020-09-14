package com.desgreen.education.siapp.ui.views.transaksi_krs_detail;

import com.desgreen.education.siapp.backend.model.EnumStatApproval;
import com.desgreen.education.siapp.ui.util.UIUtils;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.claspina.confirmdialog.ButtonOption;
import org.claspina.confirmdialog.ConfirmDialog;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

public class KrsDetailController implements KrsDetailListener{

    protected KrsDetailModel model;
    protected KrsDetailView view;

    @Autowired
    public KrsDetailController(KrsDetailModel model, KrsDetailView view){
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
    public void aksiBtnDaftarOrBatalForm() {

        final boolean isSelected = model.itemHeader.isStatSelected();
        if (model.itemHeader.getEnumStatApproval().equals(EnumStatApproval.APPROVE)) {
            UIUtils.showNotification("Sudah dilakukan Persetujuan, Tidak Bisa dibatalkan");
            return;
        }
        if (isSelected && model.itemHeader.isStatCancel()==false) {
            VerticalLayout messageLayout = new VerticalLayout();
            messageLayout.add(new Label("Apakah Yakin Mau Membatalkan Pendaftaran"));
            messageLayout.add(new Label("Periksa dengan baik"));
            ConfirmDialog.createQuestion()
                    .withCaption("Konfirmasi Pembatalan")

                    .withMessage(messageLayout)
                    // .withMessage("Do you want to continue?, Do you want to continue?, \nDo you want to continue?")

                    .withOkButton(() -> {
                        aksiPembatalan();

                    }, ButtonOption.caption("YES").icon(VaadinIcon.PAPERPLANE))
                    // .withCancelButton(ButtonOption.caption("NO"))
                    .withCancelButton(() -> {
                        //                     System.out.println("No. Implement logic here.");

                    }, ButtonOption.focus(), ButtonOption.caption("No").icon(VaadinIcon.EXIT))
                    .withWidthForAllButtons("150px")
                    .open();

        }else {
            VerticalLayout messageLayout = new VerticalLayout();
            messageLayout.add(new Label("Apakah Yakin Melakukan Pendaftaran"));
            messageLayout.add(new Label("Alhamdulillahi Jaza Kumullohu Khoiron"));
            ConfirmDialog.createQuestion()
                    .withCaption("Konfirmasi Daftar")

                    .withMessage(messageLayout)
                    // .withMessage("Do you want to continue?, Do you want to continue?, \nDo you want to continue?")

                    .withOkButton(() -> {
                        aksiProsesPendaftaran();

                    }, ButtonOption.caption("YES").icon(VaadinIcon.PAPERPLANE))
                    // .withCancelButton(ButtonOption.caption("NO"))
                    .withCancelButton(() -> {
                        //                     System.out.println("No. Implement logic here.");

                    }, ButtonOption.focus(), ButtonOption.caption("No").icon(VaadinIcon.EXIT))
                    .withWidthForAllButtons("150px")
                    .open();
        }

    }

    protected  void aksiProsesPendaftaran(){
        final boolean newDomain = model.itemHeader.isNewDomain();
//        model.itemHeader = new FtKrs();
        model.itemHeader.setStatSelected(true);
        model.itemHeader.setSelectedDate(LocalDate.now());
        model.itemHeader.setStatCancel(false);
        model.itemHeader.setCancelDate(LocalDate.now());

        if (newDomain) {

            model.itemHeader.setFdivisionBean(view.currentKurikulum.getFdivisionBean());
            model.itemHeader.setFsiswaBean(model.currentFSiswa);
            model.itemHeader.setFkurikulumBean(view.currentKurikulum);
            model.itemHeader.setEnumStatApproval(EnumStatApproval.OPEN);
        }
        model.itemHeader = model.ftKrsJPARepository.save(model.itemHeader);

        model.mapKrsCurrentUser.put(model.itemHeader.getId(), model.itemHeader);
//        view.setBtnDaftarCaption(model.itemHeader.isStatSelected());
        view.goToParentView_ToRefresh();
    }

    protected  void aksiPembatalan() {
        model.itemHeader.setStatSelected(true); //tetap true ya
        model.itemHeader.setSelectedDate(LocalDate.now());
        model.itemHeader.setStatCancel(true);
        model.itemHeader.setCancelDate(LocalDate.now());

        model.itemHeader = model.ftKrsJPARepository.save(model.itemHeader);

        model.mapKrsCurrentUser.put(model.itemHeader.getId(), model.itemHeader);
//        view.setBtnDaftarCaption(model.itemHeader.isStatSelected());
        view.goToParentView_ToRefresh();

    }

    @Override
    public void aksiBtnCancelForm() {

    }
}
