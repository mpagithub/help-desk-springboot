package com.marceloSpringboot.helpdesk.api.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.marceloSpringboot.helpdesk.api.entity.Ticket;

public interface TicketRepository extends MongoRepository<Ticket, String> {

	// Vai retornar Page para fazermos paginação
	// Quando usuário acessar a lista de Tickets, deve retornar apenas os tickets abertos por ele
	Page<Ticket> findByUserIdOrderByDateDesc(Pageable pages, String userId);
	
	
	// Pesquisando por títulos
	// O Containing é como se fosse o like '%', você começa a digitar e ele já vai trazendo o que encontrar
	Page<Ticket> findByTitleIgnoreCaseContainingAndStatusAndPriorityOrderByDateDesc(
			     String title, String status, String priority, Pageable pages);
	
	
	// Quando usuário acessar a lista de Tickets, deve retornar apenas os tickets abertos por ele
	Page<Ticket> findByTitleIgnoreCaseContainingAndStatusAndPriorityAndUserIdOrderByDateDesc(
			     String title, String status, String priority, String userId, Pageable pages);
	
	
	// Quando o técnico logar, vai trazer somente os tickets que estão atribuídos a ele
	Page<Ticket> findByTitleIgnoreCaseContainingAndStatusAndPriorityAndAssignedUserIdOrderByDateDesc(
		     String title, String status, String priority, String userId, Pageable pages);
	
	// Pesquisa por número 
	Page<Ticket> findByNumber(Integer number, Pageable pages);
	
	
}
