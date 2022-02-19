package com.ica.persistence.interfaces;

import java.util.List;

import com.ica.model.contact.ContactGroup;

/**
 * 
 * @author Cigdem
 *
 */
public interface ContactGroupDao extends GenericDao<ContactGroup>
{
	List<ContactGroup> findAllInPolicy(long id);
	boolean linkToPolicy(long idContactGroup, long idPolicy);
	boolean unlinkAllFromPolicy(long idPolicy);
	void updateFromPolicy(Long id, List<ContactGroup> contactGroups);
}
