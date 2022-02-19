package com.ica.persistence.interfaces;

import java.util.List;

import com.ica.android.service.Call;
import com.ica.android.service.Caller;

import android.content.ContentValues;

/**
 * 
 * @author Cigdem
 *
 */
public interface CallDao extends GenericDao<Call>
{
	List<Call> findCall(String number);
	long insert(ContentValues values);
	void removeFromGroup(String phoneNumber);
	void addToGroup(String phoneNumber, String groupName);
}
