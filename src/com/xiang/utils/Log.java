package com.xiang.utils;

public class Log {
	private static int logLevel = 6;
	
	private static long tttt = System.currentTimeMillis();

	private static String appendThreadId(String tag) {
		return (tag + "(tid=" + Thread.currentThread().getId() + ")" +( System.currentTimeMillis() - tttt) + "   ");
	}

	/**
	 * Change current logging level
	 * 
	 * @param level
	 *            new log level 1 <= level <= 6
	 */
	public static void setLogLevel(int level) {
		logLevel = level;
	}

	/**
	 * Get the current log level
	 * 
	 * @return the log level
	 */
	public static int getLogLevel() {
		return logLevel;
	}

	/**
	 * Log verbose
	 * 
	 * @param tag
	 *            Tag for this log
	 * @param msg
	 *            Msg for this log
	 */
	public static void v(String tag, String msg) {
		if (logLevel >= 5) {
			android.util.Log.v(appendThreadId(tag), msg);
		}
	}

	/**
	 * Log verbose
	 * 
	 * @param tag
	 *            Tag for this log
	 * @param msg
	 *            Msg for this log
	 * @param tr
	 *            Error to serialize in log
	 */
	public static void v(String tag, String msg, Throwable tr) {
		if (logLevel >= 5) {
			android.util.Log.v(appendThreadId(tag), msg, tr);
		}
	}

	/**
	 * Log debug
	 * 
	 * @param tag
	 *            Tag for this log
	 * @param msg
	 *            Msg for this log
	 */
	public static void d(String tag, String msg) {
		if (logLevel >= 4) {
			android.util.Log.d(appendThreadId(tag), msg);
		}
	}

	/**
	 * Log debug
	 * 
	 * @param tag
	 *            Tag for this log
	 * @param msg
	 *            Msg for this log
	 * @param tr
	 *            Error to serialize in log
	 */
	public static void d(String tag, String msg, Throwable tr) {
		if (logLevel >= 4) {
			android.util.Log.d(appendThreadId(tag), msg, tr);
		}
	}

	/**
	 * Log info
	 * 
	 * @param tag
	 *            Tag for this log
	 * @param msg
	 *            Msg for this log
	 */
	public static void i(String tag, String msg) {
		if (logLevel >= 3) {
			android.util.Log.i(appendThreadId(tag), msg);
		}
	}

	/**
	 * Log info
	 * 
	 * @param tag
	 *            Tag for this log
	 * @param msg
	 *            Msg for this log
	 * @param tr
	 *            Error to serialize in log
	 */
	static void i(String tag, String msg, Throwable tr) {
		if (logLevel >= 3) {
			android.util.Log.i(appendThreadId(tag), msg, tr);
		}
	}

	/**
	 * Log warning
	 * 
	 * @param tag
	 *            Tag for this log
	 * @param msg
	 *            Msg for this log
	 */
	public static void w(String tag, String msg) {
		if (logLevel >= 2) {
			android.util.Log.w(appendThreadId(tag), msg);
		}
	}

	/**
	 * Log warning
	 * 
	 * @param tag
	 *            Tag for this log
	 * @param msg
	 *            Msg for this log
	 * @param tr
	 *            Error to serialize in log
	 */
	public static void w(String tag, String msg, Throwable tr) {
		if (logLevel >= 2) {
			android.util.Log.w(appendThreadId(tag), msg, tr);
		}
	}

	/**
	 * Log error
	 * 
	 * @param tag
	 *            Tag for this log
	 * @param msg
	 *            Msg for this log
	 */
	public static void e(String tag, String msg) {
		if (logLevel >= 1) {
			android.util.Log.e(appendThreadId(tag), msg);
		}
	}

	/**
	 * Log error
	 * 
	 * @param tag
	 *            Tag for this log
	 * @param msg
	 *            Msg for this log
	 * @param tr
	 *            Error to serialize in log
	 */
	public static void e(String tag, String msg, Throwable tr) {
		if (logLevel >= 1) {
			android.util.Log.e(appendThreadId(tag), msg, tr);
		}
	}

}
