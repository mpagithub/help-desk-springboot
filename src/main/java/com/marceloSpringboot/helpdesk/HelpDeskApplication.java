package com.marceloSpringboot.helpdesk;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.marceloSpringboot.helpdesk.api.entity.User;
import com.marceloSpringboot.helpdesk.api.enums.ProfileEnum;
import com.marceloSpringboot.helpdesk.api.repository.UserRepository;

@SpringBootApplication
public class HelpDeskApplication {

	public static void main(String[] args) {
		SpringApplication.run(HelpDeskApplication.class, args);
	}
	
	@Bean
	CommandLineRunner init(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		return args -> {
			initUsers(userRepository, passwordEncoder);
		};
	}
	
	
	
	public void initUsers(UserRepository userRepository, PasswordEncoder passwordEncoder) {
	
		// Ao iniciar a aplicação, já vai criar o usuário com suas credenciais
		User admin = new User();
		admin.setEmail("admin@helpdesk.com");
		admin.setPassword(passwordEncoder.encode("123456"));
		admin.setProfile(ProfileEnum.ROLE_ADMIN);
		
		// Salvando no banco
		User find = userRepository.findByEmail("admin@helpdesk.com");
		if (find == null) {
			userRepository.save(admin);
		}
	}

}
