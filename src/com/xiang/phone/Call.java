package com.xiang.phone;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.TextUtils;

import com.xiang.status.Status;
import com.xiang.status.Status.StatusCode;

public class Call {
	public static Status callPhone(final Context context, final String number) {
		if (context == null) {
			return (new Status(StatusCode.EINVALIDARGUMENT));
		}
		if (TextUtils.isEmpty(number)) {
			return (new Status(StatusCode.EINVALIDARGUMENT));
		}
		if (PackageManager.PERMISSION_GRANTED != context.getPackageManager()
				.checkPermission("android.permission.CALL_PHONE",
						context.getPackageName())) {
			return (new Status(StatusCode.ENOPERMISSION,
					"no permission <android.permission.CALL_PHONE>"));
		}
		/**
		 * 调用系统方法拨打电话
		 */
		Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
				+ number));
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_SINGLE_TOP);
		context.startActivity(intent);
		return (new Status(StatusCode.SUCCESS));
	}
}
