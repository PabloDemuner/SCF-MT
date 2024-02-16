package com.scfapi.config.swagger;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpMethod;

import com.fasterxml.classmate.TypeResolver;
import com.scfapi.config.swagger.paginacao.PageableSwaggerLancamentos;
import com.scfapi.config.swagger.paginacao.PageableSwaggerPessoas;
import com.scfapi.config.swagger.paginacao.PageableSwaggerResumoLancamentos;
import com.scfapi.controller.filter.ResumoLancamento;
import com.scfapi.model.Lancamento;
import com.scfapi.model.Pessoa;

import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.OAuthBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.AlternateTypeRules;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.GrantType;
import springfox.documentation.service.HttpAuthenticationScheme;
import springfox.documentation.service.OAuth;
import springfox.documentation.service.ResourceOwnerPasswordCredentialsGrant;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;


@Configuration
@Import(BeanValidatorPluginsConfiguration.class)
public class SpringFoxConfig {
	
	@Autowired
	private TypeResolver typeResolver;
	
	@Autowired
	private SpringFoxExceptionsConfig springFoxExceptionsConfig;

    @Bean
    public Docket apiDocket() {
    	
    	//var typeResolver = new TypeResolver();
    	
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
				/*Não esta funcionando corretamente na versão 3 do SpringFox
				.securitySchemes(Arrays.asList(securityScheme()))*/
				.securityContexts(Arrays.asList(securityContext()))
		        .securitySchemes(List.of(authenticationScheme()))
		        .securityContexts(List.of(securityContext()))
				//.directModelSubstitute(LocalDate.class, Date.class)
				.alternateTypeRules(AlternateTypeRules.newRule(typeResolver.resolve(
						Page.class, Lancamento.class), 
						PageableSwaggerLancamentos.class))
				.alternateTypeRules(AlternateTypeRules.newRule(typeResolver.resolve(
						Page.class, ResumoLancamento.class), 
						PageableSwaggerResumoLancamentos.class))
				.alternateTypeRules(AlternateTypeRules.newRule(typeResolver.resolve(
						Page.class, Pessoa.class), 
						PageableSwaggerPessoas.class))
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
    /*Não esta funcionando corretamente na versão 3 do SpringFox
	private OAuth securityScheme() {
		return new OAuthBuilder()
				.name("SCF")
				.grantTypes(grantTypes())
				.scopes(scopes())
				.build();
	}

	private List<GrantType> grantTypes() {
		return Arrays.asList(new ResourceOwnerPasswordCredentialsGrant("/oauth/token"));
	}

	private List<AuthorizationScope> scopes() {
		return Arrays.asList(new AuthorizationScope("READ", "Acesso de leitura"),
				new AuthorizationScope("WRITE", "Acesso de leitura"));
	}*/

	private SecurityContext securityContext() {
		return SecurityContext.builder()
				.securityReferences(securityReference())
				.build();
	}

	private List<SecurityReference> securityReference() {
		AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
		AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
		authorizationScopes[0] = authorizationScope;
		return List.of(new SecurityReference("Authorization", authorizationScopes));
	}

	private HttpAuthenticationScheme authenticationScheme() {
		return HttpAuthenticationScheme.JWT_BEARER_BUILDER.name("Authorization").build();
	}
}
