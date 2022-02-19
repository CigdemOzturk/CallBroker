package com.ica.model.facade;
import java.util.List;

import com.ica.android.service.Call;
import com.ica.android.service.Caller;

/**
 * 
 * @author Cigdem
 *
 */
public interface CallInterface 
{
	public List<Call> findCall(String Number);
	public boolean insertCall(Call call);
	public void removeFromGroup(String phoneNumber);
	public void addToGroup(String phoneNumber);
}
