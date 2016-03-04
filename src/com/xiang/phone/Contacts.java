package com.xiang.phone;

import java.util.List;

import android.app.PendingIntent;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.text.TextUtils;

import com.xiang.status.Status;
import com.xiang.status.Status.StatusCode;

public class Contacts {
	private static final Uri SIMCARD_CONTACT_URI = Uri
			.parse("content://icc/adn");
	/**
	 * 获取SIM卡库表字段
	 */
	private static final String[] SIMCARD_CONTACT_PROJECTION = new String[] {
	/**
	 * 
	 */
	"index",
	/**
	 * 
	 */
	"name",
	/**
	 * 
	 */
	"number",
	/**
	 * 
	 */
	"emails",
	/**
	 * 
	 */
	"additionalNumber",
	/**
	 * 
	 */
	"groupIds",
	/**
	 * 
	 */
	"_id",
	/**
	 * 
	 */
	"aas",
	/**
	 * 
	 */
	"sne"
	/**
	 * 
	 */
	};
	/**
	 * 获取手机通讯录库表字段
	 */
	private static final String[] PHONE_CONTACT_PROJECTION = new String[] {
	/**
	 * 
	 */
	Phone.CONTACT_ID,
	/**
	 * 
	 */
	Phone.DISPLAY_NAME,
	/**
	 * 
	 */
	Phone.NUMBER,
	/**
	 * 
	 */
	};
	/**
	 * 联系人名称index
	 */
	private static final int INDEX_CONTACT_NAME = 1;
	/**
	 * 联系人号码index
	 */
	private static final int INDEX_CONTACT_NUMBER = 2;

	private Contacts() {
	}

	public static enum ContactFlags {
		/**
		 * only SIM card
		 */
		CONTACTS_ON_SIMCARD,
		/**
		 * only phone
		 */
		CONTACTS_ON_PHONE,
		/**
		 * SIM card and phone
		 */
		CONTACTS_ON_ALL;
	}

	public static class Contact {
		private final String mName;
		private final String mNumber;
		private final boolean mIsFromSimCard;

		private Contact(String name, String number, boolean isFromSimCard) {
			mName = name;
			mNumber = number;
			mIsFromSimCard = isFromSimCard;
		}

		public String getName() {
			return (mName);
		}

		public String getNumber() {
			return (mNumber);
		}

		public boolean isFromSimCard() {
			return (mIsFromSimCard);
		}

		public Status callPhone(final Context context) {
			return (Call.callPhone(context, getNumber()));
		}

		public Status sendSMS(final String message,
				final PendingIntent sentIntent,
				final PendingIntent deliveryIntent) {
			return (Message.sendSMS(getNumber(), message, sentIntent,
					deliveryIntent));
		}
	}

	/**
	 * 获取SIM卡联系人
	 */
	private static Status queryContacts(final Context context,
			final List<Contact> contacts, final boolean isFromSimCard) {
		final Uri contactsUri = isFromSimCard ? SIMCARD_CONTACT_URI
				: Phone.CONTENT_URI;
		final String[] projection = isFromSimCard ? SIMCARD_CONTACT_PROJECTION
				: PHONE_CONTACT_PROJECTION;
		final Cursor cursor = context.getContentResolver().query(contactsUri,
				projection, null, null, null);
		if (cursor == null) {
			return (new Status(StatusCode.ENOTFOUND));
		}
		try {
			String number;
			while (cursor.moveToNext()) {
				number = cursor.getString(INDEX_CONTACT_NUMBER);
				if (!TextUtils.isEmpty(number)) {
					contacts.add(new Contact(cursor
							.getString(INDEX_CONTACT_NAME), number,
							isFromSimCard));
				}
			}
		} catch (Exception e) {
			return (new Status(StatusCode.EEXCEPTION, e.toString()));
		} finally {
			cursor.close();
		}
		return (new Status(StatusCode.SUCCESS));
	}

	/**
	 * 获取SIM卡联系人
	 */
	private static Status getSimcardContacts(final Context context,
			final List<Contact> contacts) {
		return (queryContacts(context, contacts, true));
	}

	/**
	 * 获取手机通讯录联系人
	 */
	private static Status getPhoneContacts(final Context context,
			final List<Contact> contacts) {
		return (queryContacts(context, contacts, false));
	}

	/**
	 * 获取手机通讯录联系人
	 */
	public static Status getContacts(final Context context,
			final List<Contact> contacts, ContactFlags flags) {
		if (context == null) {
			return (new Status(StatusCode.EINVALIDARGUMENT));
		}
		if (contacts == null) {
			return (new Status(StatusCode.EINVALIDARGUMENT));
		}
		/**
		 * check permission
		 */
		if (PackageManager.PERMISSION_GRANTED != context.getPackageManager()
				.checkPermission("android.permission.READ_CONTACTS",
						context.getPackageName())) {
			return (new Status(StatusCode.ENOPERMISSION,
					"no permission <android.permission.READ_CONTACTS>"));
		}
		if (flags == null) {
			flags = ContactFlags.CONTACTS_ON_ALL;
		}
		switch (flags) {
		case CONTACTS_ON_SIMCARD: {
			return (getSimcardContacts(context, contacts));
		}
		case CONTACTS_ON_PHONE: {
			return (getPhoneContacts(context, contacts));
		}
		case CONTACTS_ON_ALL: {
			Status status1 = getSimcardContacts(context, contacts);
			Status status2 = getPhoneContacts(context, contacts);
			if ((!status1.isSuccessful()) && (!status2.isSuccessful())) {
				return (status1);
			}
			return (new Status(StatusCode.SUCCESS));
		}
		}
		return (new Status(StatusCode.ENOTMATCH));
	}
}
