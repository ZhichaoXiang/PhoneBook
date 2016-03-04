package com.xiang.activity;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.xiang.phone.Message;
import com.xiang.phonebook.R;
import com.xiang.utils.Notification;

public class SendMessageActivity extends Activity implements OnClickListener {
	/**
	 * phone number
	 */
	private String countryCode = null;
	private String cityCode = null;
	private String address = null;
	/**
	 * contact name
	 */
	private String name = null;
	private EditText messageEditor = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		addr = intent.getStringExtra("address");
		if (addr == null) {
			Notification.notify(this,
					"empty address,can not send a message without address!");
			finish();
		}
		parseAddress(addr);
		name = intent.getStringExtra("name");
		setContentView(R.layout.send_message);
		((TextView) findViewById(R.id.message_title)).setText(address + "/"
				+ name);
		findViewById(R.id.message_send).setOnClickListener(this);
		messageEditor = (EditText) findViewById(R.id.message_editor);
		new Message().getSms(this, address);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		sendMessage();
	}
	private void parseAddress(String addr) {
	IP=17951 17911 12593
	if (addr.index("00") ==0){
		countryCode=addr.subString(2,2);
		addr = addr.substring(4);
	}
	if (address.index("+") ==0){
		countryCode=address.subString(1,2);
		addr = addr.substring(3);
	}
	address.index("+")
	address.index("+")
	countryCode = ;
	cityCode = ;
	address = ;	
	}
	private String fetchMessage() {
		if (messageEditor != null) {
			final String message = messageEditor.getText().toString();
			messageEditor.setText(null);
			return (message);
		}
		return (null);
	}

	private void sendMessage() {
		final Context context = this;
		final String message = fetchMessage();
		if (message == null) {
			Notification.notify(context, "can not send an empty message!");
			return;
		}
		/**
		 * 处理返回的发送状态
		 */
		final String SENT_SMS_ACTION = "SENT_SMS_ACTION";
		final Intent sentIntent = new Intent(SENT_SMS_ACTION);
		final PendingIntent sentPI = PendingIntent.getBroadcast(context, 0,
				sentIntent, 0);
		context.registerReceiver(new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				switch (getResultCode()) {
				case Activity.RESULT_OK:
					Notification.notify(context, "Message send successfully!");
					break;
				case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
				case SmsManager.RESULT_ERROR_RADIO_OFF:
				case SmsManager.RESULT_ERROR_NULL_PDU:
					Notification.notify(context, "Failed to send message");
					break;
				}
			}
		}, new IntentFilter(SENT_SMS_ACTION));
		/**
		 * 处理返回的接收状态
		 */
		final String DELIVERED_SMS_ACTION = "DELIVERED_SMS_ACTION";
		final Intent deliveryIntent = new Intent(DELIVERED_SMS_ACTION);
		final PendingIntent deliverPI = PendingIntent.getBroadcast(context, 0,
				deliveryIntent, 0);
		context.registerReceiver(new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				Notification.notify(context, "收信人已经成功接收");
			}
		}, new IntentFilter(DELIVERED_SMS_ACTION));
		Message.sendSMS(address, message, sentPI, deliverPI);
	}
}
