package com.desgreen.education.siapp.app_email;

import com.desgreen.education.siapp.AppPublicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
public class EmailService {
  
    @Autowired
    public JavaMailSender javaMailSender;

    public void sendSimpleMessage(String to, String subject, String textMesage) {

      
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(AppPublicService.PUBLIC_HOST_EMAIL); //Dummy aslinya ada di setting: Tapi harus ada

        // msg.setTo("bagus.stimata@gmail.com", "des.jatim1@gmail.com", "helpdesk1@des-green.com");
        msg.setTo(to);        
        msg.setSubject(subject);
        
        // msg.setText("Hello World \n Spring Boot Email");
        msg.setText(textMesage);

        javaMailSender.send(msg);

    }

    @Bean
    public void testImplement(){
        System.out.println("hello oke ini");
    }



}