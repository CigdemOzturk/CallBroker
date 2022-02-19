package com.ica.persistence.interfaces;

import com.ica.model.policy.Policy;

/**
 * 
 * @author Cigdem
 *
 */
public interface PolicyDao extends GenericDao<Policy>
{
	Policy findActivePolicy();
	void setActivePolicy(long id);
}
