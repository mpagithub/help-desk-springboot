package com.marceloSpringboot.helpdesk.api.Jwt.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.marceloSpringboot.helpdesk.api.Jwt.security.JwtAuthenticationEntryPoint;
import com.marceloSpringboot.helpdesk.api.Jwt.security.JwtAuthenticationTokenFilter;

@Configuration  // Para informar para o spring que será uma classe de configuração
@EnableWebSecurity  // habilita essa classe para ela configurar o spring security pra nós
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private JwtAuthenticationEntryPoint unauthorizedHandler;
	
	@Autowired
	private UserDetailsService userDetailsService; 
	
	@Autowired
	public void configureAuthentication(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
		authenticationManagerBuilder.userDetailsService(this.userDetailsService).passwordEncoder(passwordEncoder());
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public JwtAuthenticationTokenFilter authenticationTokenFilterBean() throws Exception {
		return new JwtAuthenticationTokenFilter();
	}

	
	@Bean
	public AuthenticationManager customAuthenticationManager() throws Exception {

	  return authenticationManager();

	}
	
	protected void configure(HttpSecurity httpSecurity) throws Exception {
			
		httpSecurity.csrf().disable()
		            .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
		            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
		            .authorizeRequests()
		            .antMatchers(
		            		HttpMethod.GET,   // tudo abaixo tem livre acesso, o que não está aí será validado e precisará de um token válido para executar qualquer ação na aplicação 
		            		"/",
		            		"/*.html",
		            		"/favicon.ico",
		            		"/**/*.css",
		            		"/**/*.js"
		            ).permitAll()
                    .antMatchers("/api/auth/**").permitAll()
                    .anyRequest().authenticated();
		
		httpSecurity.addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);
		httpSecurity.headers().cacheControl();
		
	}
	
	
}
