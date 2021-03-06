package com.alkemy.ong.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  private UserDetailsService userDetailsService;

  @Autowired
  private JwtRequestFilter jwtRequestFilter;

  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Autowired
  public void configureGlobal(AuthenticationManagerBuilder managerBuilder) throws Exception {
    managerBuilder.userDetailsService(userDetailsService)
        .passwordEncoder(passwordEncoder());
  }

  @Override
  @Bean
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.csrf()
        .disable()
        .cors()
        .and()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .authorizeRequests()
        .antMatchers(HttpMethod.DELETE, "/categories/**")
        .hasAnyRole(ApplicationRole.ADMIN.getName(), ApplicationRole.USER.getName())
        .antMatchers(HttpMethod.DELETE, "/users/**")
        .hasAnyRole(ApplicationRole.USER.getName())
        .antMatchers(HttpMethod.DELETE, "/testimonials/**")
        .hasAnyRole(ApplicationRole.ADMIN.getName(), ApplicationRole.USER.getName())
        .antMatchers(HttpMethod.DELETE, "/members/**")
        .hasAnyRole(ApplicationRole.ADMIN.getName(), ApplicationRole.USER.getName())
        .antMatchers(HttpMethod.DELETE, "/slides/**")
        .hasAnyRole(ApplicationRole.ADMIN.getName())
        .antMatchers(HttpMethod.DELETE, "/comments/**")
        .hasAnyRole(ApplicationRole.ADMIN.getName(), ApplicationRole.USER.getName())
        .anyRequest()
        .authenticated()
        .and()
        .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
        .exceptionHandling()
        .authenticationEntryPoint(new Http403ForbiddenEntryPoint());
  }

}
