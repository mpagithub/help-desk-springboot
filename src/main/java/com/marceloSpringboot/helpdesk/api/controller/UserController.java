package com.marceloSpringboot.helpdesk.api.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.marceloSpringboot.helpdesk.api.Jwt.service.UserService;
import com.marceloSpringboot.helpdesk.api.entity.User;
import com.marceloSpringboot.helpdesk.api.response.Response;
import com.mongodb.DuplicateKeyException;

@RestController  // Para a classe virar um RestFull
@RequestMapping("/api/user")
@CrossOrigin(origins = "*")   // Liberando o acesso para qualquer porta/servidor
public class UserController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;  // para criptografar nossa senha
	
	@PostMapping
	@PreAuthorize("hasAnyRole('ADMIN')")  // Regra para quem pode criar, de acordo com o perfil
	public ResponseEntity<Response<User>> create(HttpServletRequest request, @RequestBody User user, BindingResult result) { // para salvar um usuátrio no banco

		Response<User> response = new Response<User>();
		
		try {
			
			validateCreateUser(user, result);
			if (result.hasErrors()) {
				result.getAllErrors().forEach(error ->response.getErrors().add(error.getDefaultMessage()));
			}
			
			user.setPassword(passwordEncoder.encode(user.getPassword()));  // criptografando
			User userPersisted = (User) userService.createOrUpdate(user);
			response.setData(userPersisted);
			
		} catch (DuplicateKeyException dE) {
			
			response.getErrors().add("E-mail já registrado!");
			return ResponseEntity.badRequest().body(response);
			
		} catch (Exception e) {
			
			response.getErrors().add(e.getMessage());
			return ResponseEntity.badRequest().body(response);
			
		}
		
		return ResponseEntity.ok(response);
		
	}
	
	// para validar a entrada
	private void validateCreateUser(User user, BindingResult result) {
		
		if (user.getEmail() == null) {
			result.addError(new ObjectError("User", "Email no information"));
		}
	}
	
	@PutMapping
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<Response<User>> update(HttpServletRequest request, @RequestBody User user, BindingResult result) {
		
		Response<User> response = new Response<User>();
		
		try {
			
			validateUpdateUser(user, result);
			if (result.hasErrors()) {
				result.getAllErrors().forEach(error ->response.getErrors().add(error.getDefaultMessage()));
				return ResponseEntity.badRequest().body(response);
			}
			
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			User userPersisted = (User) userService.createOrUpdate(user);
			response.setData(userPersisted);
			
		} catch (Exception e) {
			response.getErrors().add(e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
			
		return ResponseEntity.ok(response);
		
	}
	private void validateUpdateUser(User user, BindingResult result) {
		if (user.getId() == null) {
			result.addError(new ObjectError("User", "Id não Informado"));
		}
		
		if (user.getEmail() == null) {
			result.addError(new ObjectError("User", "Email no information"));
		}
	}

	
	@GetMapping(value = "{id}")
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<Response<User>> findById(@PathVariable("id") String id) {
		
		Response<User> response = new Response<User>();
		User user = userService.findById(id);
		
		if (user == null) {
			response.getErrors().add("Registro não encontrado id: " + id);
			return ResponseEntity.badRequest().body(response);
		}
		response.setData(user);
		return ResponseEntity.ok(response);
		
	}
	
	
	@DeleteMapping(value = "{id}")
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<Response<String>> delete(@PathVariable("id") String id) {
		
		Response<String> response = new Response<String>();
		User user = userService.findById(id);
		if (user == null) {
			response.getErrors().add("Registro não encontrado id: " + id);
			return ResponseEntity.badRequest().body(response);
		}
		userService.delete(id);
		return ResponseEntity.ok(new Response<>());
		
	}
	
	
	@GetMapping(value = "{page}/{count}")     // NA PESQUISA, A PRIMEIRA PÁGINA É A NÚMERO 0(ZERO)
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<Response<Page<User>>> findAll(@PathVariable int page, @PathVariable int count) {
		
		Response<Page<User>> response = new Response<Page<User>>();
		Page<User> users =  userService.findAll(page,count);
		
		response.setData(users);
		return ResponseEntity.ok(response);
		
	}
	
	
	
	
	
	
	
}
