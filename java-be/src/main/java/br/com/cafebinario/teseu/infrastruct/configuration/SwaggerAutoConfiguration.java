package br.com.cafebinario.teseu.infrastruct.configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.data.rest.configuration.SpringDataRestConfiguration;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

@EnableSwagger2WebMvc
@Import(SpringDataRestConfiguration.class)
@Configuration
@EnableConfigurationProperties
public class SwaggerAutoConfiguration {

	@Value("${br.com.cafebinario.teseu.swagger.title:Teseu regressive tests}")
	private String title;
	
	@Value("${br.com.cafebinario.teseu.swagger.description: engine for regresive tests of APIs restful}")
	private String description;
	
	@Value("${br.com.cafebinario.teseu.swagger.contactName: Alexandre Or Jane}")
	private String contactName;
	
	@Value("${br.com.cafebinario.teseu.swagger.contactUrl: http://www.robotservice.com.br}")
	private String contactUrl;
	
	@Value("${br.com.cafebinario.teseu.swagger.contactEmail: comercial@cafebinario.com.br}")
	private String contactEmail;
	
	@Value("${br.com.cafebinario.teseu.swagger.license: Apache 2.0}")
	private String license;
	
	@Value("${br.com.cafebinario.teseu.swagger.licenseUrl: https://www.apache.org/licenses/LICENSE-2.0}")
	private String licenseUrl;
	
	@Value("${br.com.cafebinario.teseu.swagger.version: 0.0.1-SNAPSHOT}")
	private String version;
	
	@Bean
	public Docket api() {
		
		return new Docket(DocumentationType.SWAGGER_2) //
				.select() //
				.paths(PathSelectors.regex("/.*")) //
				.build() //
				.apiInfo(apiEndPointsInfo());

	}

	private ApiInfo apiEndPointsInfo() {

		return new ApiInfoBuilder() //
				.title(title) //
				.description(description) //
				.contact(createContact()) //
				.license(license) //
				.licenseUrl(licenseUrl) //
				.version(version) //
				.build();

	}

	private Contact createContact() {
		
		return new Contact( //
				contactName, //
				contactUrl, //
				contactEmail);
	}
}