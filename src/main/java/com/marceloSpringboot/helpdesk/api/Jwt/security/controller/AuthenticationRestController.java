package com.marceloSpringboot.helpdesk.api.Jwt.security.controller;


import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.marceloSpringboot.helpdesk.api.Jwt.security.JwtAuthenticationRequest;
import com.marceloSpringboot.helpdesk.api.Jwt.security.JwtTokenUtil;
import com.marceloSpringboot.helpdesk.api.Jwt.security.model.CurrentUser;
import com.marceloSpringboot.helpdesk.api.Jwt.service.UserService;
import com.marceloSpringboot.helpdesk.api.entity.User;



@RestController
@CrossOrigin(origins="*")  // permitindo o acesso de qualquer ip/porta que vier
public class AuthenticationRestController {

	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private UserService userService;
	
	
	// Método que vai criar o token
	@GetMapping(value="/api/auth")
	public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtAuthenticationRequest authenticationRequest) throws AuthenticationException {
  
		final Authentication authentication = authenticationManager.authenticate(   //aqui requisitamos um token
				new UsernamePasswordAuthenticationToken(
						authenticationRequest.getEmail(),
						authenticationRequest.getPassword())	
				);
				
		SecurityContextHolder.getContext().setAuthentication(authentication);
		final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getEmail());
		final String token = jwtTokenUtil.generateToken(userDetails);
		final User user = userService.findByEmail(authenticationRequest.getEmail());
		user.setPassword(null);	
		
		
    	return ResponseEntity.ok(new CurrentUser(token,user));
	}

	
	// para dar um refresh no nosso token
	public ResponseEntity<?> refreshAndGetAuthenticationToken(HttpServletRequest request) {
		
		String token = request.getHeader("Authorization");
		String username = jwtTokenUtil.getUserNameFromToken(token);
		final User user = userService.findByEmail(username);
		
		if (jwtTokenUtil.canTokenBeRefreshed(token)) {
			String refreshToken = jwtTokenUtil.refreshToken(token);
			return ResponseEntity.ok(new CurrentUser(refreshToken, user));		
		}else {
			return ResponseEntity.badRequest().body(null); 
		}
		
	}
	
	
	
}
