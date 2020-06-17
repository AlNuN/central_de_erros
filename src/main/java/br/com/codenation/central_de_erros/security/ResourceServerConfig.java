package br.com.codenation.central_de_erros.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@Configuration
@EnableResourceServer
@Profile({"development", "production"})
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(HttpMethod.POST, "/users").permitAll()
                .antMatchers(HttpMethod.PUT, "/users").authenticated()
                .antMatchers(HttpMethod.GET, "/users").authenticated()
                .antMatchers(HttpMethod.GET, "/events/**").authenticated()
                .antMatchers(HttpMethod.GET, "/events").authenticated()
                .antMatchers(HttpMethod.POST, "/events").authenticated()
                .antMatchers(HttpMethod.PUT, "/events/**").authenticated()
                .antMatchers(HttpMethod.DELETE, "/events/**").authenticated()
                ;
    }
}
