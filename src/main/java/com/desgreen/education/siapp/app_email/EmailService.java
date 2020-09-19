package com.desgreen.education.siapp.app_email;

import com.desgreen.education.siapp.AppPublicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.util.Properties;


@Service
public class EmailService {
  
    @Autowired
    public JavaMailSender mailSender;

    public JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();

    public EmailService(){
        try {
            javaMailSender.setUsername("helpdesk1@des-green.com");
//            javaMailSender.setUsername("ppdi@ponpesdahlanikhsan.com");
            javaMailSender.setPassword("Welcome123456789");

            Properties properties = new Properties();
            properties.put("mail.transport.protocol", "smtp");
            properties.put("mail.smtp.ssl.enable", "true");
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.debug", "true");

            properties.put("mail.smtp.connectiontimeout", 6000);
//			properties.put("mail.smtp.timeout", 6000); //sellau bikin error: Jangan pakai ini

//			properties.put("mail.smtp.starttls.enable", "true");

            javaMailSender.setJavaMailProperties(properties);
            javaMailSender.setHost("mail.des-green.com");
//            javaMailSender.setHost("mail.ponpesdahlanikhsan.com");
            javaMailSender.setPort(465);
//		    mailSender.setPort(587); //Not SSL
        }catch (Exception ex) {}

    }

    public void sendSimpleMessage(String to, String subject, String textMesage) {

      
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(AppPublicService.PUBLIC_HOST_EMAIL); //Dummy aslinya ada di setting: Tapi harus ada

        // msg.setTo("bagus.stimata@gmail.com", "des.jatim1@gmail.com", "helpdesk1@des-green.com");
        msg.setTo(to);        
        msg.setSubject(subject);
        
//         msg.setText("Hello World \n Spring Boot Email ari mas bagus winanro");
        msg.setText(textMesage);
        mailSender.send(msg);

    }

    public void sendSimpleHtmlMessage(String to, String subject, String htmlContent){
        MimeMessage mimeMessage = this.mailSender.createMimeMessage();
        MimeMessageHelper message = new MimeMessageHelper(mimeMessage, "UTF-8");
        try {
            message.setFrom(AppPublicService.PUBLIC_HOST_EMAIL); //Dummy aslinya ada di setting: Tapi harus ada

            message.setTo(to);
            message.setSubject(subject);
            message.setText(htmlContent, true);
        }catch (Exception ex){}

        mailSender.send(mimeMessage);
    }




    public void sendSimpleMessageManual(String to, String subject, String textMesage) throws Exception {


        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(AppPublicService.PUBLIC_HOST_EMAIL); //Dummy aslinya ada di setting: Tapi harus ada
//        msg.setFrom("bagus.stimata@gmail.com");

        // msg.setTo("bagus.stimata@gmail.com", "des.jatim1@gmail.com", "helpdesk1@des-green.com");
        msg.setTo(to);
        msg.setSubject(subject);

        msg.setText("Hello World \n Spring Boot Email Boss");
//        msg.setText(textMesage);

        javaMailSender.send(msg);

        System.out.println("Email bos: " + msg.getFrom() + " >> " + msg.getSubject() + " >> " + msg.getTo());

    }


    @Bean
    public void testImplement(){
        System.out.println("hello oke ini");
    }



}