package com.boxingregistration.app.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.DefaultSecurityFilterChain
import org.springframework.security.web.server.SecurityWebFilterChain


@Configuration
class SecurityConfig : WebSecurityConfigurerAdapter()
{
    @Bean
    fun passwordEncoder(): PasswordEncoder
    {
        return BCryptPasswordEncoder()
    }

    override fun configure(http: HttpSecurity)
    {
        http.httpBasic()
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

        http.authorizeRequests()
            .antMatchers("/").permitAll()
            .antMatchers("/*.css", "/*.js").permitAll()
            .antMatchers("/api/clubs").permitAll()
            .antMatchers("/api/categories").permitAll()
            .antMatchers("/api/register").permitAll()
            .anyRequest().authenticated()

        http.csrf().disable()
        http.cors().disable()
    }
}