package com.ica.persistence.dao.sqllite;

import java.util.ArrayList;
import java.util.List;

import com.ica.model.contact.Contact;
import com.ica.persistence.interfaces.ContactDao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 
 * @author Cigdem
 *
 */
public class SqlLiteContactDao extends SqlLiteGenericDao<Contact> implements ContactDao
{
	public SqlLiteContactDao(SQLiteDatabase db)
	{
		super(db);
	}
	
	@Override
	protected String getTableName()
	{
		return DatabaseDefinition.TABLE_CONTACT;
	}
	
	@Override
	protected Contact toObject(Cursor cursor)
	{
		Long id = cursor.getLong(cursor.getColumnIndex(DatabaseDefinition.CONTACT_GROUP_COLUMN_ID));
		String name = cursor.getString(cursor.getColumnIndex(DatabaseDefinition.CONTACT_COLUMN_NAME));
		String phoneNumber = cursor.getString(cursor.getColumnIndex(DatabaseDefinition.CONTACT_PHONE_NUMBER));
		return new Contact(id, name, phoneNumber);
	}
	
	public List<Contact> findAllInContactGroup(long id)
	{
		List<Contact> res = new ArrayList<Contact>();
		Cursor cursor = db.query(true, DatabaseDefinition.TABLE_CONTACT + " JOIN " + DatabaseDefinition.TABLE_CONTACT_CONTACT_GROUP + " ON " + DatabaseDefinition.TABLE_CONTACT + " . " + DatabaseDefinition.CONTACT_COLUMN_ID + " = " + DatabaseDefinition.TABLE_CONTACT_CONTACT_GROUP + " . " + DatabaseDefinition.CONTACT_CONTACT_GROUP_COLUMN_ID_CONTACT, null, DatabaseDefinition.TABLE_CONTACT_CONTACT_GROUP + " . " + DatabaseDefinition.CONTACT_CONTACT_GROUP_ID_CONTACT_GROUP + " = " + id, null, null, null, null, null);
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
	
	public long insert(ContentValues values, long idContactGroup)
	{
		// We just insert the Contact in case it does not exist
		db.insertWithOnConflict(getTableName(), null, values, SQLiteDatabase.CONFLICT_IGNORE);
		// We fill the Intermediate Table in order to maintain the relation
		ContentValues intermediateTableValues = new ContentValues();
		intermediateTableValues.put(DatabaseDefinition.CONTACT_CONTACT_GROUP_COLUMN_ID_CONTACT, values.getAsLong(DatabaseDefinition.CONTACT_COLUMN_ID));
		intermediateTableValues.put(DatabaseDefinition.CONTACT_CONTACT_GROUP_ID_CONTACT_GROUP, idContactGroup);
		return db.insert(DatabaseDefinition.TABLE_CONTACT_CONTACT_GROUP, null, intermediateTableValues);
	}
	
	public void updateFromContactGroup(long id, List<Contact> selectedContacts)
	{
		db.delete(DatabaseDefinition.TABLE_CONTACT_CONTACT_GROUP, DatabaseDefinition.CONTACT_CONTACT_GROUP_ID_CONTACT_GROUP + " = " + id, null);
		for(Contact contact : selectedContacts)
		{
			ContentValues values = new ContentValues();
			values.put(DatabaseDefinition.CONTACT_CONTACT_GROUP_COLUMN_ID_CONTACT, contact.getId());
			values.put(DatabaseDefinition.CONTACT_CONTACT_GROUP_ID_CONTACT_GROUP, id);
			db.insert(DatabaseDefinition.TABLE_CONTACT_CONTACT_GROUP, null, values);
			
			// We just insert the Contact in case it does not exist
			ContentValues contactValues = new ContentValues();
			contactValues.put(DatabaseDefinition.CONTACT_COLUMN_ID, contact.getId());
			contactValues.put(DatabaseDefinition.CONTACT_COLUMN_NAME, contact.getName());
			contactValues.put(DatabaseDefinition.CONTACT_PHONE_NUMBER, contact.getPhoneNumber());
			db.insertWithOnConflict(getTableName(), null, contactValues, SQLiteDatabase.CONFLICT_IGNORE);
		}
	}
}
