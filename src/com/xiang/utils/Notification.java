package com.xiang.utils;

import android.content.Context;
import android.widget.Toast;

import com.xiang.status.Status;

public class Notification {
	public static void notify(Context context, String msg) {
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}

	public static void notify(Context context, Status status) {
		notify(context, status.getDescription());
	}
}
