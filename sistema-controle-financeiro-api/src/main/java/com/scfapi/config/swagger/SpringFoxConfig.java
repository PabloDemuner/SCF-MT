package com.scfapi.config.swagger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;

import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.service.Tag;


@Configuration
@Import(BeanValidatorPluginsConfiguration.class)
public class SpringFoxConfig {
	
	@Autowired
	private SpringFoxExceptionsConfig springFoxExceptionsConfig;

    @Bean
    public Docket apiDocket() {
        return new Docket(DocumentationType.OAS_30)
        		.select()
                .apis(RequestHandlerSelectors.basePackage("com.scfapi"))
                .paths(PathSelectors.any())
                .build()
                .useDefaultResponseMessages(false)
				.globalResponses(HttpMethod.GET, springFoxExceptionsConfig.globalGetResponseMessages())
				.globalResponses(HttpMethod.POST, springFoxExceptionsConfig.globalPostPutResponseMessages())
				.globalResponses(HttpMethod.PUT, springFoxExceptionsConfig.globalPostPutResponseMessages())
				.globalResponses(HttpMethod.DELETE, springFoxExceptionsConfig.globalDeleteResponseMessages())
                .apiInfo(apiInfo())
                .tags(
                		new Tag("Categoria", "Controlador de categorias de lançamentos"), 
                		new Tag("Lancamentos", "Controlador de lançamentos financeiros"),
                		new Tag("Pessoa", "Controlador de cadastro de pessoas"));
    }
    
    public ApiInfo apiInfo() {
    	return new ApiInfoBuilder()
    			.title("Sistema de Controle Financeiro")
    			.version("1")
    			.contact(new Contact(
    					"Pablo Demuner", 
    					"https://www.linkedin.com/in/pablo-diego-demuner-costa-31695b1b7/",
    					"pablodiegodemuner@gmail.com"))
    			.build();
    }
}
