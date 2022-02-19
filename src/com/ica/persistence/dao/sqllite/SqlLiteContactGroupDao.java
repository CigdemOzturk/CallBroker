package com.ica.persistence.dao.sqllite;

import java.util.ArrayList;
import java.util.List;

import com.ica.model.contact.ContactGroup;
import com.ica.persistence.interfaces.ContactGroupDao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 
 * @author Cigdem
 *
 */
public class SqlLiteContactGroupDao extends SqlLiteGenericDao<ContactGroup> implements ContactGroupDao
{
	public SqlLiteContactGroupDao(SQLiteDatabase db)
	{
		super(db);
	}
	
	@Override
	protected String getTableName()
	{
		return DatabaseDefinition.TABLE_CONTACT_GROUP;
	}
	
	@Override
	protected ContactGroup toObject(Cursor cursor)
	{
		Long id = cursor.getLong(cursor.getColumnIndex(DatabaseDefinition.CONTACT_GROUP_COLUMN_ID));
		String name = cursor.getString(cursor.getColumnIndex(DatabaseDefinition.CONTACT_GROUP_COLUMN_NAME));
		ContactGroup contactGroup = new ContactGroup(name);
		contactGroup.setId(id);
		return contactGroup;
	}
	
	public List<ContactGroup> findAllInPolicy(long id)
	{
		List<ContactGroup> res = new ArrayList<ContactGroup>();
		Cursor cursor = db.query(true, DatabaseDefinition.TABLE_CONTACT_GROUP + " JOIN " + DatabaseDefinition.TABLE_POLICY_CONTACT_GROUP + " ON " + DatabaseDefinition.TABLE_CONTACT_GROUP + " . " + DatabaseDefinition.CONTACT_GROUP_COLUMN_ID + " = " + DatabaseDefinition.TABLE_POLICY_CONTACT_GROUP + " . " + DatabaseDefinition.POLICY_CONTACT_GROUP_ID_CONTACT_GROUP, null, DatabaseDefinition.POLICY_CONTACT_GROUP_COLUMN_ID_POLICY + " = " + id, null, null, null, null, null);
		if(cursor.moveToFirst())
		{
			do
			{
				res.add(toObject(cursor));
			}
			while(cursor.moveToNext());
		}
		cursor.close();
		return res;
	}
	
	public boolean linkToPolicy(long idContactGroup, long idPolicy)
	{
		// We fill the Intermediate Table in order to maintain the relation
		ContentValues intermediateTableValues = new ContentValues();
		intermediateTableValues.put(DatabaseDefinition.POLICY_CONTACT_GROUP_COLUMN_ID_POLICY, idPolicy);
		intermediateTableValues.put(DatabaseDefinition.POLICY_CONTACT_GROUP_ID_CONTACT_GROUP, idContactGroup);
		return db.insert(DatabaseDefinition.TABLE_POLICY_CONTACT_GROUP, null, intermediateTableValues) != -1;
	}
	
	// We will keep the unused contacts in the Table Contact because they will be likely used in the future.
	// Moreover whenever we add a contact, if this already exists, we do not do anything
	@Override
	public boolean delete(long id)
	{
		db.delete(DatabaseDefinition.TABLE_CONTACT_CONTACT_GROUP, DatabaseDefinition.CONTACT_CONTACT_GROUP_ID_CONTACT_GROUP + " = " + id, null);
		return super.delete(id);
	}
	
	public boolean unlinkAllFromPolicy(long idPolicy)
	{
		return db.delete(DatabaseDefinition.TABLE_POLICY_CONTACT_GROUP, DatabaseDefinition.POLICY_CONTACT_GROUP_COLUMN_ID_POLICY + " = " + idPolicy, null) != -1;
	}
	
	public void updateFromPolicy(Long id, List<ContactGroup> contactGroups)
	{
		db.delete(DatabaseDefinition.TABLE_POLICY_CONTACT_GROUP, DatabaseDefinition.POLICY_CONTACT_GROUP_COLUMN_ID_POLICY + " = " + id, null);
		for(ContactGroup contactGroup : contactGroups)
		{
			ContentValues values = new ContentValues();
			values.put(DatabaseDefinition.POLICY_CONTACT_GROUP_COLUMN_ID_POLICY, id);
			values.put(DatabaseDefinition.POLICY_CONTACT_GROUP_ID_CONTACT_GROUP, contactGroup.getId());
			db.insert(DatabaseDefinition.TABLE_POLICY_CONTACT_GROUP, null, values);
		}
	}
}
