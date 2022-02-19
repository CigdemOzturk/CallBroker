package com.ica.persistence.interfaces;

import java.util.List;

import com.ica.model.command.Command;

/**
 * 
 * @author Cigdem
 *
 */
public interface CommandDao extends GenericDao<Command>
{
	List<Command> findAllInContactGroup(long id);
	
	void updateFromContactGroup(Long idContactGroup, List<Command> commands);
	void deleteAllFromContactGroup(long id);
}
