package com.desgreen.education.siapp.ui.views.transaksi_krs_validasi_detail;

import com.desgreen.education.siapp.AppPublicService;
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
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.text.NumberFormat;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class KrsValidasiDetailController implements KrsValidasiDetailListener {

    protected KrsValidasiDetailModel model;
    protected KrsValidasiDetailView view;

    @Autowired
    public KrsValidasiDetailController(KrsValidasiDetailModel model, KrsValidasiDetailView view) {
//        this.model = new tKrsModel();
        this.model = model;
        this.view = view;

    }

    @Override
    public void valueChangeListenerSearch(HasValue.ValueChangeEvent e) {
        if (e != null) {
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

    public void aksiBtnDeleteProcess() {
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

    protected void aksiProsesSetuju() {

        model.currentDomain.setEnumStatApproval(EnumStatApproval.APPROVE);
        model.currentDomain = model.ftKrsJPARepository.save(model.currentDomain);

        model.mapKrsCurrentUser.put(model.currentDomain.getId(), model.currentDomain);
        view.goToParentView_ToRefresh();

        FUser userBean = new FUser();
        try {
            userBean = model.usersJPARepository.findByIdSiswa(model.currentDomain.getFsiswaBean().getId());
            aktifkanUser(userBean);
        } catch (Exception ex) {
        }
        NumberFormat nf = NumberFormat.getInstance(); nf.setMaximumFractionDigits(0);
        try {
            String mailFrom = model.currentDomain.getFdivisionBean().getFcompanyBean().getEmail();
            String mailTo = model.currentDomain.getFsiswaBean().getEmail();
            if (mailTo.equals("")) {
                try {
                    mailTo = userBean.getEmail();
                } catch (Exception ex) {
                }

            }
            String subject = "Pendaftaran Materi/Asrama";
            String textMessage = "Assalamualaikum Wr. Wb\n" +
                    "Kami Pengurus Pondok/Sekolah " + model.currentDomain.getFdivisionBean().getFcompanyBean().getDescription() +
                    " " + model.currentDomain.getFdivisionBean().getDescription() + ", Menerima Pendaftaran Anda\n" +
                    " Untuk Informasi Lebih Lanjut Silahkan Melakukan Login dan Melihat Informasi-informasi penting dalam aplikasi\n\n" +
                    "Alhamdulillahi Jaza Kumullohu Khoiron, Assalamualaikum Wr.Wb";
//            model.emailService.sendSimpleMessage(mailFrom, mailTo, subject, textMessage);
            model.emailService.sendSimpleMessage( mailTo, subject, textMessage);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    protected void aksiProsesBatal() {

        model.currentDomain.setEnumStatApproval(EnumStatApproval.REJECTED);
        model.currentDomain = model.ftKrsJPARepository.save(model.currentDomain);

        model.mapKrsCurrentUser.put(model.currentDomain.getId(), model.currentDomain);
        view.goToParentView_ToRefresh();

        try {
            String mailFrom = model.currentDomain.getFdivisionBean().getFcompanyBean().getEmail();
            String mailTo = model.currentDomain.getFsiswaBean().getEmail();
            if (mailTo.equals("")) {
                try {
                    FUser userBean = model.usersJPARepository.findByIdSiswa(model.currentDomain.getFsiswaBean().getId());
                    mailTo = userBean.getEmail();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
            String subject = "Pendaftaran Materi/Asrama";
            String textMessage = "Assalamualaikum Wr. Wb\n" +
                    "Kami Pengurus Pondok/Sekolah " + model.currentDomain.getFdivisionBean().getFcompanyBean().getDescription() +
                    " " + model.currentDomain.getFdivisionBean().getDescription() + ", Dengan Sangat Menyesal Belum Dapat Menerima Pendaftaran Anda\n" +
                    " Untuk Informasi Lebih Lanjut Silahkan Hubungi Telp +62" + model.currentDomain.getFdivisionBean().getFcompanyBean().getPhoneNumber()+ " \n\n" +
                    "Alhamdulillahi Jaza Kumullohu Khoiron, Assalamualaikum Wr.Wb" ;
//            model.emailService.sendSimpleMessage(mailFrom, mailTo, subject, textMessage);
            model.emailService.sendSimpleMessage( mailTo, subject, textMessage);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public void aksiBtnCancelForm() {

    }

    public void aktifkanUser(FUser userBean) {
        if (userBean != null) {
            List<FUserRoles> listFUserRoles = userBean.getFuserRoles();
            if (listFUserRoles.stream().filter(x -> x.getRoleID().equals(Role.MNU_PPDB_KRS)).collect(Collectors.toList()).size() == 0) {
                FUserRoles newFUserRoles = new FUserRoles();
                newFUserRoles.setFuserBean(userBean);
                newFUserRoles.setSelected(true);
                newFUserRoles.setRoleID(Role.MNU_PPDB_KRS);

                model.userRolesJPARepository.save(newFUserRoles);
            }

        }

    }


    public void sendMail() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        try {
            mailSender.setUsername("helpdesk1@des-green.com");
            mailSender.setPassword("Welcome123456789");

            Properties properties = new Properties();
            properties.put("mail.transport.protocol", "smtp");
            properties.put("mail.smtp.ssl.enable", "true");
//            properties.put("mail.smtp.ssl.enable", "false");//Not SSL
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.debug", "true");

            properties.put("mail.smtp.connectiontimeout", 6000);
//			properties.put("mail.smtp.timeout", 6000); //sellau bikin error: Jangan pakai ini

//			properties.put("mail.smtp.starttls.enable", "true");

            mailSender.setJavaMailProperties(properties);
            mailSender.setHost("mail.des-green.com");
            mailSender.setPort(465);
//		    mailSender.setPort(587); //Not SSL

        } catch (Exception e) {
            e.printStackTrace();
        }

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(AppPublicService.PUBLIC_HOST_EMAIL); //Dummy aslinya ada di setting: Tapi harus ada

        // msg.setTo("bagus.stimata@gmail.com", "des.jatim1@gmail.com", "helpdesk1@des-green.com");
        msg.setTo("bagus.stimata@gmail.com");
        msg.setSubject("PAH");

        msg.setText("Hello World \n Spring Boot Email mas bagus winanro");
//        msg.setText(textMesage);

        mailSender.send(msg);

//        MimeMessage message = mailSender.createMimeMessage();
//        try {
//            MimeMessageHelper helper = new MimeMessageHelper(message);
////            helper = new MimeMessageHelper(message, false);
////            helper = new MimeMessageHelper(message, true); //To Active Multipart: Yaitu jika ada Attachment
//
//            helper.setFrom("helpdesk1@des-green.com");
//            helper.setSubject("PAH " );
//
//            String htmlTitleText = "<h2><font color=\"blue\"><b>REPORT PROSES AKHIR HARI </b></font></h2>";
//            htmlTitleText += "<p style=\"font-size:25px;  line-height: 0.6; \"><b></b></p>";
//            htmlTitleText += "<p style=\"font-size:12px; line-height: 1.2; \">PIC: " +
//                    " (" + model.userActive.getFullName() + ") " +
//                    "<small>finish at  " +
//                    " from " + "</small></p>";
//
//            String htmlContentText = "";
//
//
//            String htmlFooter = "<br><br>";
//            helper.setText("", htmlTitleText + htmlContentText + htmlFooter);
//
////			helper.addTo("des.jatim1@gmail.com");
////			helper.addTo("bagus.stimata@gmail.com");
//			helper.setTo("bagus.stimata@gmail.com");
//
//
//			try {
//                FileSystemResource file
//                        = new FileSystemResource(new File(AppPublicService.FILE_PATH));
//                helper.addAttachment("lap.pdf", file);
//            }catch (Exception ex){}
//
//            mailSender.send(message);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }



    }
}
