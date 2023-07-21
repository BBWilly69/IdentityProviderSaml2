package de.imunixx.idpsaml2;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.saml2.provider.service.metadata.OpenSamlMetadataResolver;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.saml.saml2.metadata.IdentityProviderMetadata;
import org.springframework.security.saml2.provider.service.metadata.Saml2MetadataResolver;

@Configuration
@EnableWebSecurity
public class SAMLConfig extends WebSecurityConfigurerAdapter {

    @Value("${idp.metadata.url}")
    private String idpMetadataUrl;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/saml/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .apply(saml())
                .identityProvider()
                .metadataResolver(metadataResolver())
                .and()
                .http()
                .csrf()
                .disable();
    }

    @Bean
    public Saml2MetadataResolver metadataResolver() {
        OpenSamlMetadataResolver resolver = new OpenSamlMetadataResolver();
        resolver.setMetadataURL(idpMetadataUrl);
        return resolver;
    }
}
