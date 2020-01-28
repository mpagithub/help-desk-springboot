package com.marceloSpringboot.helpdesk.api.Jwt.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.marceloSpringboot.helpdesk.api.Jwt.service.UserService;
import com.marceloSpringboot.helpdesk.api.entity.User;
import com.marceloSpringboot.helpdesk.api.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService  {

	@Autowired
	private UserRepository userRepository;
	
	
	
	@Override
	public User findByEmail(String email) {
		return this.userRepository.findByEmail(email);
	}

	@Override
	public User createOrUpdate(User user) {
		return this.userRepository.save(user);
	}

	@Override
	public User findById(String id) {
		
		Optional<User> optional = this.userRepository.findById(id);
		
		if (optional.isPresent()) return optional.get();
		else return null;
		
	}
	
	@Override
	public void delete(String id) {
		this.userRepository.deleteById(id);
	}

	@Override
	public Page<User> findAll(int page, int count) {
		
		/*  Deprecated
			Pageable pages = new PageRequest(page,count);
		*/

		Pageable pages =   PageRequest.of(page, count);
		return this.userRepository.findAll(pages);
		
	}

	
	
	
	
	
}
