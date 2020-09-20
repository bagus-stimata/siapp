package com.desgreen.education.siapp;

import com.desgreen.education.siapp.backend.jpa_repository.FCompanyJPARepository;
import com.desgreen.education.siapp.backend.jpa_repository.FDivisionJPARepository;
import com.desgreen.education.siapp.backend.model.FCompany;
import com.desgreen.education.siapp.backend.model.FDivision;
import com.desgreen.education.siapp.security_config.SecurityConfiguration;
import com.desgreen.education.siapp.security_model.FUser;
import com.desgreen.education.siapp.security_repository.UsersJPARepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.unit.DataSize;

import javax.servlet.MultipartConfigElement;

/**
 * The entry point of the Spring Boot application.
 */
//@SpringBootApplication
@SpringBootApplication(scanBasePackageClasses = { AppPublicService.class, SecurityConfiguration.class,
        PasswordEncoder.class }, exclude = ErrorMvcAutoConfiguration.class)
@EnableJpaRepositories(basePackageClasses = { UsersJPARepository.class, FCompanyJPARepository.class,
        FDivisionJPARepository.class})
@EntityScan(basePackageClasses = { FUser.class, FCompany.class, FDivision.class })
@EnableAsync //untuk asyn email
public class Application extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    /**
     * Untuk mengatur Limit File Upload Secara Keseluruhan
     * @return
     */
    @Bean
    MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(DataSize.ofBytes(512000000L));
        factory.setMaxRequestSize(DataSize.ofBytes(512000000L));
        return factory.createMultipartConfig();
    }
}
