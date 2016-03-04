package com.xiang.phone;

import java.util.List;

import android.app.PendingIntent;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.telephony.SmsManager;
import android.text.TextUtils;

import com.xiang.status.Status;
import com.xiang.status.Status.StatusCode;
import com.xiang.utils.Log;

public class Message {
	/**
	 * 
	 * 所有的短信
	 */
	public static final Uri SMS_URI_ALL = Uri.parse("content://sms/");
	/**
	 * 
	 * 收件箱短信
	 */
	public static final String SMS_URI_INBOX = "content://sms/inbox";
	/**
	 * 
	 * 已发送短信
	 */
	public static final String SMS_URI_SEND = "content://sms/sent";
	/**
	 * 
	 * 草稿箱短信
	 */
	public static final String SMS_URI_DRAFT = "content://sms/draft";

	/**
	 * 
	 * Role:获取短信的各种信息 <BR>
	 * 
	 * Date:2012-3-19 <BR>
	 * 
	 * 
	 * 
	 * @author CODYY)peijiangping
	 */
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

	public void getSmsInfo(final Context context) {
		String[] projection = new String[] { "address", "body", "date", "type" };
		Cursor cusor = context.getContentResolver().query(SMS_URI_ALL,
				projection, null, null, "date desc");
		int phoneNumberColumn = cusor.getColumnIndex("address");
		int smsbodyColumn = cusor.getColumnIndex("body");
		int dateColumn = cusor.getColumnIndex("date");
		int typeColumn = cusor.getColumnIndex("type");
		if (cusor != null) {
			while (cusor.moveToNext()) {
				Log.e("==================",
						"body1==" + cusor.getString(smsbodyColumn));
			}
			cusor.close();
		}
	}

	// "_id",
	// "thread_id ",
	// "address",
	// "m_size ",
	// "person ",
	// "date",
	// "date_sent",
	// "protocol ",
	// "read",
	// "status ",
	// "type",
	// "reply_path_present",
	// "subject",
	// "body",
	// "service_center",
	// "locked ",
	// "sim_id ",
	// "error_code ",
	// "seen",
	// "ipmsg_id ",
	// "ref_id ",
	// "total_len",
	// "rec_len"

	public void getSms(final Context context, final String address) {
		String[] projection = null;// new String[] { "body", "date", "type" };
		String selection = "thread_id = '" + 68 + "'";
		Cursor cusor = context.getContentResolver().query(SMS_URI_ALL,
				projection, selection, null, "date desc");
		int nameColumn = cusor.getColumnIndex("person");
		int phoneNumberColumn = cusor.getColumnIndex("address");
		int smsbodyColumn = cusor.getColumnIndex("body");
		int dateColumn = cusor.getColumnIndex("date");
		int typeColumn = cusor.getColumnIndex("type");
		String[] sss = cusor.getColumnNames();
		for (String ss : sss) {
			Log.e("", "sss==========================" + ss);
		}
		if (cusor != null) {
			while (cusor.moveToNext()) {
				String msg = "";
				for (String ss : sss) {
					if(!"body".equals(ss)){
					msg = msg + ";" + ss + "="
							+ cusor.getString(cusor.getColumnIndex(ss));}
				}
				
					msg = msg + ";" + "body" + "="
							+ cusor.getString(cusor.getColumnIndex("body"));
				Log.e("==================", msg);
			}
			cusor.close();
		}
	}

	/**
	 * 调用短信接口发短信
	 */
	public static Status sendSMS(final String number, final String message,
			final PendingIntent sentIntent, final PendingIntent deliveryIntent) {
		if (TextUtils.isEmpty(number)) {
			return (new Status(StatusCode.EINVALIDARGUMENT));
		}
		if (TextUtils.isEmpty(message)) {
			return (new Status(StatusCode.EINVALIDARGUMENT));
		}
		/**
		 * 获取短信管理器
		 */
		final SmsManager smsManager = SmsManager.getDefault();
		/**
		 * 拆分短信内容（手机短信长度限制）
		 */
		final List<String> messages = smsManager.divideMessage(message);
		for (String msg : messages) {
			smsManager.sendTextMessage(number, null, msg, sentIntent,
					deliveryIntent);
		}
		return (new Status(StatusCode.SUCCESS));
	}
}
