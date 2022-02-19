package com.ica.model.facade;

import java.util.HashMap;
import java.util.List;

import com.ica.model.contact.ContactGroup;
import com.ica.model.policy.Policy;
import com.ica.model.policy.time.TimeStamp;
import com.ica.util.time.TimeUtils.DAYS;

/**
 * 
 * @author Cigdem
 *
 */
public interface PolicyFacadeInterface 
{
	public boolean insertPolicy(Policy policy);
	public boolean deletePolicy(long id);
	public Policy findPolicy(long id);
	public List<ContactGroup> findContactGroupsOfPolicy(long id);
	public HashMap<DAYS, List<TimeStamp>> findTimeStampsOfPolicy(long id);
	public List<Policy> findAllPolicies();
	public List<ContactGroup> test(String phoneNumber);
	public Policy getActivePolicy();
	public void setActivePolicy(long id);
	public void updatePolicy(Long idPolicy, String newName, List<ContactGroup> contactGroups, HashMap<DAYS, List<TimeStamp>> timeStamps);
}
