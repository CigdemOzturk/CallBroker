package com.ica.model.facade;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;

import com.ica.model.command.Command;
import com.ica.model.command.CommandContent;
import com.ica.model.command.CommandType;
import com.ica.model.contact.Contact;
import com.ica.model.contact.ContactGroup;
import com.ica.model.policy.Policy;
import com.ica.model.policy.time.TimeStamp;
import com.ica.persistence.dao.sqllite.DatabaseDefinition;
import com.ica.persistence.factory.AbstractDaoFactory;
import com.ica.persistence.factory.DaoType;
import com.ica.persistence.interfaces.CommandDao;
import com.ica.persistence.interfaces.ContactDao;
import com.ica.persistence.interfaces.ContactGroupDao;
import com.ica.persistence.interfaces.PolicyDao;
import com.ica.persistence.interfaces.TimeStampDao;
import com.ica.util.time.TimeUtils.DAYS;

/**
 * 
 * @author Cigdem
 *
 */
public class PolicyFacadeImpl implements PolicyFacadeInterface
{
	private static PolicyFacadeInterface instance = null;
	
	private PolicyDao policyDao = null;
	private ContactGroupDao contactGroupDao = null;
	private TimeStampDao timeStampDao = null;
	private ContactDao contactDao;
	private CommandDao commandDao;
	
	private PolicyFacadeImpl(Context context)
	{
		AbstractDaoFactory factory = AbstractDaoFactory.getFactory(DaoType.SQL_LITE, context);
		factory.createConnection();
		policyDao = factory.getPolicyDao();
		contactGroupDao = factory.getContactGroupDao();
		contactDao = factory.getContactDao();
		commandDao = factory.getAbstractCommandDao();
		timeStampDao = factory.getTimeStampDao();
	}
	
	// @return instance
	public static PolicyFacadeInterface getInstance(Context context)
	{
		if(instance == null)
		{
			instance = new PolicyFacadeImpl(context);
		}
		return instance;
	}
	/* ContactGroups will be a proxy because we do not need to work with its contacts.
	 * TimeFilter will NOT be a proxy because we need its content.
	 */
	public Policy getActivePolicy()
	{
		return policyDao.findActivePolicy();
	}
	
	public void setActivePolicy(long id)
	{
		policyDao.setActivePolicy(id);
	}
	
	/* ContactGroups will NOT be a proxy because we need to know if the phoneNumber belongs to one of its contacts.
	 * Every Contact in ContactGroups will NOT be a proxy for the same reason.
	 * Every Command in ContactGroups will NOT be a proxy for the same reason.
	 * TimeFilter will NOT be a proxy because we need its content.
	 */
	public List<ContactGroup> test(String phoneNumber)
	{
		Policy policy = getActivePolicy();
		if(policy == null)
		{
			return new ArrayList<ContactGroup>();
		}
		List<ContactGroup> contactGroups = contactGroupDao.findAllInPolicy(policy.getId());
		for(ContactGroup contactGroup : contactGroups)
		{
			contactGroup.setContacts(contactDao.findAllInContactGroup(contactGroup.getId()));
			List<Command> commands = commandDao.findAllInContactGroup(contactGroup.getId());
			for(Command command : commands)
			{
				if(command.getType() == CommandType.SEND_SMS)
				{
					ContactGroup cg = (ContactGroup)command.getContent().get(CommandContent.CONTACT_GROUP);
					List<Contact> contacts = contactDao.findAllInContactGroup(cg.getId());
					ContactGroup newContactGroup = new ContactGroup(cg.getName());
					newContactGroup.setId(contactGroup.getId());
					newContactGroup.setContacts(contacts);
					command.getContent().put(CommandContent.CONTACT_GROUP, newContactGroup);	
				}
			}
			contactGroup.setCommands(commands);
		}
		policy.setContacts(contactGroups);
		HashMap<DAYS, List<TimeStamp>> timeFilters = timeStampDao.findAllInPolicy(policy.getId());
		policy.setTimeStamps(timeFilters);
		return policy.test(phoneNumber);
	}
	
	public boolean insertPolicy(Policy policy)
	{
		// Policy Table
		ContentValues valuesPolicy = new ContentValues();
		valuesPolicy.put(DatabaseDefinition.POLICY_COLUMN_NAME, policy.getName());
		valuesPolicy.put(DatabaseDefinition.POLICY_ACTIVE, policy.isActive());
		long policyId = policyDao.insert(valuesPolicy);
		
		// ContactGroup Table
		for(ContactGroup cg : policy.getContacts())
		{
			contactGroupDao.linkToPolicy(cg.getId(), policyId);
		}
		
		// TimeStamp Table
		for(DAYS day : DAYS.values())
		{
			ContentValues valuesTimeFilter = new ContentValues();
			List<TimeStamp> timeStamps = policy.getTimeStamps(day);
			for(TimeStamp timeStamp : timeStamps)
			{
				valuesTimeFilter.put(DatabaseDefinition.TIME_STAMP_COLUMN_DAY, day.ordinal());
				valuesTimeFilter.put(DatabaseDefinition.TIME_STAMP_COLUMN_FROM_HOUR, timeStamp.getFromHour());
				valuesTimeFilter.put(DatabaseDefinition.TIME_STAMP_COLUMN_FROM_MINUTE, timeStamp.getFromMinute());
				valuesTimeFilter.put(DatabaseDefinition.TIME_STAMP_COLUMN_TO_HOUR, timeStamp.getToHour());
				valuesTimeFilter.put(DatabaseDefinition.TIME_STAMP_COLUMN_TO_MINUTE, timeStamp.getToMinute());
				valuesTimeFilter.put(DatabaseDefinition.TIME_STAMP_COLUMN_ID_POLICY, policyId);
				timeStampDao.insert(valuesTimeFilter);
			}
		}
		return true;
	}
	
	// We will delete all its timestamps
	public boolean deletePolicy(long id)
	{
		timeStampDao.deleteAllFromPolicy(id);
		contactGroupDao.unlinkAllFromPolicy(id);
		policyDao.delete(id);
		return true;
	}
	
	/* Every find* method will get the object in a "lazy" way. It means all its members will be set to null.
	 * This will improve the performance due to the fact that, according to the activity which is running,
	 * just some of its members will be retrieved.
	 */
	public Policy findPolicy(long id)
	{
		return policyDao.find(id);
	}
	
	public List<ContactGroup> findContactGroupsOfPolicy(long id)
	{
		return contactGroupDao.findAllInPolicy(id);
	}
	
	public HashMap<DAYS, List<TimeStamp>> findTimeStampsOfPolicy(long id)
	{
		return timeStampDao.findAllInPolicy(id);
	}
	
	public List<Policy> findAllPolicies()
	{
		return policyDao.findAll();
	}
	
	public void updatePolicy(Long id, String newName, List<ContactGroup> contactGroups, HashMap<DAYS, List<TimeStamp>> timeStamps)
	{
		ContentValues values = new ContentValues();
		values.put(DatabaseDefinition.POLICY_COLUMN_NAME, newName);
		policyDao.update(id, values);
		contactGroupDao.updateFromPolicy(id, contactGroups);
		timeStampDao.deleteAllFromPolicy(id);
		for(DAYS day : DAYS.values())
		{
			ContentValues valuesTimeFilter = new ContentValues();
			List<TimeStamp> ts = timeStamps.get(day);
			for(TimeStamp timeStamp : ts)
			{
				valuesTimeFilter.put(DatabaseDefinition.TIME_STAMP_COLUMN_DAY, day.ordinal());
				valuesTimeFilter.put(DatabaseDefinition.TIME_STAMP_COLUMN_FROM_HOUR, timeStamp.getFromHour());
				valuesTimeFilter.put(DatabaseDefinition.TIME_STAMP_COLUMN_FROM_MINUTE, timeStamp.getFromMinute());
				valuesTimeFilter.put(DatabaseDefinition.TIME_STAMP_COLUMN_TO_HOUR, timeStamp.getToHour());
				valuesTimeFilter.put(DatabaseDefinition.TIME_STAMP_COLUMN_TO_MINUTE, timeStamp.getToMinute());
				valuesTimeFilter.put(DatabaseDefinition.TIME_STAMP_COLUMN_ID_POLICY, id);
				timeStampDao.insert(valuesTimeFilter);
			}
		}
		
	}
}
