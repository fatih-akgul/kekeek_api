package com.kekeek.api.config;

import com.google.common.base.Predicates;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collection;
import java.util.Collections;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    private final static String TITLE = "Kekeek Travel API";
    private final static String DESCRIPTION = "API Endpoints for Kekeek Travel Website";
    private final static String VERSION = "1.0";
    private final static String TOS_URL = "http://www.kekeek.com/api-tos";
    private final static Contact CONTACT = new Contact("Kekeek Ltd", "http://www.kekeek.com", "kekeek@gmail.com");
    private final static String LICENSE = "API License";
    private final static String LICENSE_URL = "http://www.kekeek.com/license";
    private final static Collection<VendorExtension> VENDOR_EXTENSIONS = Collections.emptyList();

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.kekeek.api.controller"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo())
                ;
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(TITLE, DESCRIPTION, VERSION, TOS_URL, CONTACT, LICENSE, LICENSE_URL, VENDOR_EXTENSIONS);
    }
}
