package com.xiang.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.xiang.os.ThreadExecutor;
import com.xiang.phone.Contacts;
import com.xiang.phone.Contacts.Contact;
import com.xiang.phone.Contacts.ContactFlags;
import com.xiang.phonebook.R;
import com.xiang.status.Status;
import com.xiang.utils.Log;
import com.xiang.utils.Notification;

public class ContactFragment extends ListAdapterFragment<Contact> {
	private ThreadExecutor<Fragment> mThreadExecutor;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = View.inflate(inflater.getContext(), R.layout.contact_list,
				null);
		return (v);
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
	}

	@Override
	public void onStart() {
		super.onStart();
		mThreadExecutor = new ThreadExecutor<Fragment>(this, "request contacts");
		// mListView.setAdapter(getListAdapter());
		// mListView.setFastScrollAlwaysVisible(true);
		// mListView.setOnItemClickListener(this);
		requestContacts();
	}

	@Override
	public void onStop() {
		mThreadExecutor.stopThread();
		super.onStop();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	private void requestContacts() {
		mThreadExecutor.execute(new Runnable() {
			@Override
			public void run() {
				List<Contact> contacts = new ArrayList<Contact>();
				Status status = Contacts.getContacts(getActivity(), contacts,
						ContactFlags.CONTACTS_ON_PHONE);
				Log.e("testcontact", "contacts=" + contacts.size());
				if (status.isSuccessful()) {
					getListAdapter().resetData(contacts);
					contacts.clear();
				} else {
					Notification.notify(getActivity(), status);
				}
				status = Contacts.getContacts(getActivity(), contacts,
						ContactFlags.CONTACTS_ON_SIMCARD);
				Log.e("testcontact", "1contacts=" + contacts.size());
				if (status.isSuccessful()) {
					getListAdapter().addData(contacts);
				} else {
					Notification.notify(getActivity(), status);
				}
			}
		});
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Contact contact = (Contact) getListAdapter().getItem(position);
		if (contact != null) {
			Intent intent = new Intent(getActivity(),SendMessageActivity.class);
			intent.putExtra("address", contact.getNumber());
			intent.putExtra("name", contact.getName());
			getActivity().startActivity(intent);
//			Status status = contact.callPhone(view.getContext());
//			if (!status.isSuccessful()) {
//				Notification.notify(getActivity(), status);
//			}
//
//			Context context = view.getContext();
//			/**
//			 * 处理返回的发送状态
//			 */
//			final String SENT_SMS_ACTION = "SENT_SMS_ACTION";
//			final Intent sentIntent = new Intent(SENT_SMS_ACTION);
//			final PendingIntent sentPI = PendingIntent.getBroadcast(context, 0,
//					sentIntent, 0);
//			context.registerReceiver(new BroadcastReceiver() {
//				@Override
//				public void onReceive(Context context, Intent intent) {
//					switch (getResultCode()) {
//					case Activity.RESULT_OK:
//						Toast.makeText(context, "短信发送成功", Toast.LENGTH_SHORT)
//								.show();
//						break;
//					case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
//						break;
//					case SmsManager.RESULT_ERROR_RADIO_OFF:
//						break;
//					case SmsManager.RESULT_ERROR_NULL_PDU:
//						break;
//					}
//				}
//			}, new IntentFilter(SENT_SMS_ACTION));
//			/**
//			 * 处理返回的接收状态
//			 */
//			final String DELIVERED_SMS_ACTION = "DELIVERED_SMS_ACTION";
//			final Intent deliveryIntent = new Intent(DELIVERED_SMS_ACTION);
//			final PendingIntent deliverPI = PendingIntent.getBroadcast(context,
//					0, deliveryIntent, 0);
//			context.registerReceiver(new BroadcastReceiver() {
//				@Override
//				public void onReceive(Context context, Intent intent) {
//					Toast.makeText(context, "收信人已经成功接收", Toast.LENGTH_SHORT)
//							.show();
//				}
//			}, new IntentFilter(DELIVERED_SMS_ACTION));
//			// contact.sendSMS("sdfadsfasdfasdfa", sentPI, deliverPI);
		} else {
		}
	}

	@Override
	protected View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(getActivity()).inflate(
					R.layout.contact, null);
		}
		TextView contact_index = (TextView) convertView
				.findViewById(R.id.contact_index);
		TextView contactName = (TextView) convertView
				.findViewById(R.id.contact_name);
		TextView contactNumber = (TextView) convertView
				.findViewById(R.id.contact_number);
		Contact contact = (Contact) getListAdapter().getItem(position);
		if (contact != null) {
			contact_index.setText(String.valueOf(position + 1) + "/"
					+ String.valueOf(getListAdapter().getCount()) + "/"
					+ contact.isFromSimCard());
			contactName.setText(contact.getName());
			contactNumber.setText(contact.getNumber());
		}
		return (convertView);
	}
}
