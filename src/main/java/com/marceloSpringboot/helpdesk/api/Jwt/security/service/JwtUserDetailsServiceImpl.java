package com.marceloSpringboot.helpdesk.api.Jwt.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.marceloSpringboot.helpdesk.api.Jwt.security.JwtUserFactory;
import com.marceloSpringboot.helpdesk.api.Jwt.service.UserService;
import com.marceloSpringboot.helpdesk.api.entity.User;



@Service
public class JwtUserDetailsServiceImpl implements UserDetailsService{

	@Autowired
	private UserService userService;
	
	// Metodo para carregar o usuário
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		
		User user = userService.findByEmail(email);
		if (user == null) {
			throw new UsernameNotFoundException(String.format("Nenhum usuário encontrado com username '%s'.", email));
		}else {
			return JwtUserFactory.create(user);
		}
				
	}
	
	
}
