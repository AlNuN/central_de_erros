package br.com.codenation.central_de_erros.swagger;

import com.google.common.base.Predicate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static com.google.common.base.Predicates.or;
import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket apis(){
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(paths())
                .build()
                .apiInfo(apiInfo())
                .useDefaultResponseMessages(false)
                .directModelSubstitute(LocalDateTime.class, java.util.Date.class);
    }

    private ApiInfo apiInfo(){
        return new ApiInfo(
                "Central de erros",
                "API para centralizar todos os erros de sua aplicação",
                "0.0.1",
                null,
                null,
                "Licença",
                "Url da licença",
                new ArrayList<>()
        );
    }

    private Predicate<String> paths(){
        return or(
                regex("/events.*"),
                regex("/oauth/token")
        );
    }
}
