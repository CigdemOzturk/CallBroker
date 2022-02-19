package com.ica.util.contact;

import android.telephony.PhoneNumberUtils;

/**
 * 
 * @author Cigdem
 *
 */
public abstract class ContactUtils 
{
	public static boolean comparePhoneNumbers(String phone1, String phone2)
	{
		return PhoneNumberUtils.compare(phone1,  phone2);
	}
}
