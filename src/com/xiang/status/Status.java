package com.xiang.status;

import android.text.TextUtils;

public class Status {
	private final StatusCode mStatus;
	private final String mDescription;

	public Status(StatusCode status) {
		this(status, null);
	}

	public Status(StatusCode status, String desc) {
		mStatus = status;
		mDescription = desc;
	}

	public boolean isSuccessful() {
		return (mStatus == StatusCode.SUCCESS);
	}

	public String getDescription() {
		return (mStatus.getDescription() + (TextUtils.isEmpty(mDescription) ? ""
				: "," + mDescription));
	}

	/******************************************************************************/
	public static enum StatusCode {
		/**
		 * Success
		 */
		SUCCESS,
		/**
		 * Unknown error
		 */
		EUNKNOWN,
		/**
		 * Catch exception
		 */
		EEXCEPTION,
		/**
		 * Invalid argument
		 */
		EINVALIDARGUMENT,
		/**
		 * No permission
		 */
		ENOPERMISSION,
		/**
		 * Not found
		 */
		ENOTFOUND,
		/**
		 * Not support
		 */
		ENOTSUPPORT,
		/**
		 * Not match
		 */
		ENOTMATCH
		/**
		 * Finished
		 */
		;
		/**
		 * status value
		 */
		private final StatusValue mValue;

		private static class StatusValue {
			private static int next = 0;
			private final int mValue;
			private final String mDescription;

			public StatusValue() {
				this(next, null);
			}

			public StatusValue(final int value) {
				this(value, null);
			}

			public StatusValue(final String description) {
				this(next, description);
			}

			public StatusValue(final int value, final String description) {
				next = value;
				mValue = next++;
				mDescription = description;
			}

			public int getValue() {
				return (mValue);
			}

			public String getDescription() {
				String desc = "";
				if (!TextUtils.isEmpty(mDescription)) {
					desc = mDescription;
				}
				desc = ("status=" + String.valueOf(getValue()))
						+ (TextUtils.isEmpty(desc) ? "" : ("," + desc));
				return (desc);
			}
		}

		/**
		 * status code
		 */
		private StatusCode() {
			mValue = new StatusValue();
		}

		private StatusCode(final int value) {
			mValue = new StatusValue(value);
		}

		private StatusCode(final String description) {
			mValue = new StatusValue(description);
		}

		private StatusCode(final int value, final String description) {
			mValue = new StatusValue(value, description);
		}

		public int toInt() {
			return (mValue.getValue());
		}

		public String getDescription() {
			return (mValue.getDescription());
		}

		public static StatusCode toEnum(final int value) {
			// Status.class.getEnumConstants();
			StatusCode[] codes = StatusCode.values();
			if (codes != null) {
				if (
				/**
				 * a right index
				 */
				(value >= 0) &&
				/**
				 * not overflow
				 */
				(value < codes.length) &&
				/**
				 * value matching
				 */
				codes[value].toInt() == value
				/**
				 * matched
				 */
				) {
					return codes[value];
				}
				for (StatusCode code : codes) {
					if (value == code.mValue.getValue()) {
						return (code);
					}
				}
			}
			return (EUNKNOWN);
		}
	}
}