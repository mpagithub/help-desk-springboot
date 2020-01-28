package com.marceloSpringboot.helpdesk.api.service;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.marceloSpringboot.helpdesk.api.entity.ChangeStatus;
import com.marceloSpringboot.helpdesk.api.entity.Ticket;

@Component
public interface TicketService {

	
	// Vai criar ou alterar um ticket
	Ticket createOrUpdate(Ticket ticket);
	
	// vai detalhar um ticket
	Ticket findById(String id);
	
	// para excluir um ticket 
	void delete(String id);
	
	// para paginação do retorno de uma pesquisa de tickets
	Page<Ticket> listTicket(int page, int count);
	
	// vai retornar o objeto que guarda as alterações feitas no ticket
	ChangeStatus createChangeStatus(ChangeStatus changeStatus);
	
	// retorna a lista das alterações feitas de um ticket
	Iterable<ChangeStatus> listChangeStatus(String ticketId);
	
	// quando o cliente estiver pesquisando, vai retornar para ele apenas os tickts dele e de mais ninguém.
	Page<Ticket> findByCurrentUser(int page, int count, String userId); 
	
	// pesquisa por parâmetros
	Page<Ticket> findByParameters(int page, int count, String title, String status, String priority);

	// pesquisa por parâmetros incluindo o usuário
	Page<Ticket> findByParametersAndCurrentUser(int page, int count, String title, String status, String priority, String userId);

	// pesquisa pelo número do ticket
	Page<Ticket> findByNumber(int page, int count, Integer number);
	
	// para fazer o resumo de todos os tickets
	Iterable<Ticket> findAll();
	
	// quando um técnico está logado e quer listar apenas os tickets atribuido a ele
	Page<Ticket> findByParameterAndAssignedUser(int page, int count, String title, String status, String priority, String assignedUser);
	
	
}
