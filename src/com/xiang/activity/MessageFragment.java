package com.xiang.activity;

import com.xiang.phone.Message;
import com.xiang.phonebook.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

public class MessageFragment extends ListAdapterFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// View v = View.inflate(inflater.getContext(), R.layout.contact_list,
		// null);
		View v = inflater.inflate(R.layout.send_message, null, false);
		// ListView sss = new ListView(inflater.getContext());
		// new Message().getSmsFromPhone(getActivity());
		new Message().getSmsInfo(getActivity());
		return (v);
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	@Override
	protected View getView(int position, View convertView, ViewGroup parent) {
		return (null);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
	}
}
