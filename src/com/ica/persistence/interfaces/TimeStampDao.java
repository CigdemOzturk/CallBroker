package com.ica.persistence.interfaces;

import java.util.HashMap;
import java.util.List;

import com.ica.model.policy.time.TimeStamp;
import com.ica.util.time.TimeUtils.DAYS;

/**
 * 
 * @author Cigdem
 *
 */
public interface TimeStampDao extends GenericDao<TimeStamp>
{
	HashMap<DAYS, List<TimeStamp>> findAllInPolicy(long id);
	boolean deleteAllFromPolicy(long id);
}
