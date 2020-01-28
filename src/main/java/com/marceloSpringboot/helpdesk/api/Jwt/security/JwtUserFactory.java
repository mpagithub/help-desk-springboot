package com.marceloSpringboot.helpdesk.api.Jwt.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.marceloSpringboot.helpdesk.api.entity.User;
import com.marceloSpringboot.helpdesk.api.enums.ProfileEnum;

public class JwtUserFactory {

	private JwtUserFactory() {
	}

	// Converte e gera um Jwtuser, com base nos dados de um usuário
	public static JwtUser create(User user) {
		
		return new JwtUser(user.getId(), user.getEmail(), user.getPassword(),
				   mapToGrantedAuthorities(user.getProfile()));
		
	}
	
	
	// Converte o o perfil do usuário para um formato utilizado pelo Spring Security
	private static List<GrantedAuthority> mapToGrantedAuthorities(ProfileEnum profileEnum) {
		
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		authorities.add(new SimpleGrantedAuthority(profileEnum.toString()));
		return authorities;
		
	}
	
}
