package com.marceloSpringboot.helpdesk.api.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.marceloSpringboot.helpdesk.api.entity.User;

public interface UserRepository extends MongoRepository<User, String> {

	
	// No MongoRepository nem tem este método  'findByEmail', mas se o campo existe na tabela, basta colocar aqui
	// a chamada do método e o Spring data já se encarrega de fazer a pesquisa... simples assim
	User findByEmail(String email);  
	
}
