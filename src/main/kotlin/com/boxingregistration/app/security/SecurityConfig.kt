package com.boxingregistration.app.security

import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter


@Configuration
class SecurityConfig : WebSecurityConfigurerAdapter()
{
    override fun configure(http: HttpSecurity)
    {
        http.authorizeRequests()
            .antMatchers("/*").permitAll()
            .antMatchers("/").permitAll()
            .antMatchers("/api/clubs").permitAll()
            .antMatchers("/api/categories").permitAll()
            .antMatchers("/api/register").permitAll()
            .anyRequest().authenticated()
            .and()
            .logout()
            .permitAll()

        http.csrf().disable()
    }
}