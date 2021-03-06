package com.relay.iot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter
{
    private static String ADMIN_ROLE = "admin";
    private static String AUTH_USER = "root";
    private static String AUTH_PASSWORD = "{noop}123";

    @Override
    protected void configure(HttpSecurity http) throws Exception
    {
        http.httpBasic()
            .and()
            .authorizeRequests()
            .antMatchers(HttpMethod.POST, "/api/v1/sensor/*").hasRole(ADMIN_ROLE)
            .and()
            .csrf().disable()
            .formLogin().disable();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception
    {
        auth.inMemoryAuthentication()
                .withUser(AUTH_USER)
                .password(AUTH_PASSWORD)
                .roles(ADMIN_ROLE);
    }
}
