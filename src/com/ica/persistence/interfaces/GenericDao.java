package com.ica.persistence.interfaces;

import java.util.List;

import android.content.ContentValues;

/**
 * 
 * @author Cigdem
 *
 */
public interface GenericDao <T>
{
	public long insert(ContentValues values);
	public boolean delete(long id);
	public T find(long id);
	public List<T> findAll();
	public boolean update(long id, ContentValues values);
}
