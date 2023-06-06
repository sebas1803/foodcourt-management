package com.pragma.powerup.infrastructure.security.config;

import com.pragma.powerup.infrastructure.security.jwt.JwtEntryPoint;
import com.pragma.powerup.infrastructure.security.jwt.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private JwtEntryPoint jwtEntryPoint;
    @Autowired
    private JwtFilter jwtFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeRequests()
                .antMatchers(HttpMethod.POST, "/api/v1/restaurants").hasAuthority("ROLE_ADMIN")
                .antMatchers(HttpMethod.POST, "/api/v1/dishes").hasAuthority("ROLE_OWNER")
                .antMatchers(HttpMethod.PUT, "/api/v1/dishes/{id}").hasAuthority("ROLE_OWNER")
                .antMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .exceptionHandling().authenticationEntryPoint(jwtEntryPoint)
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
