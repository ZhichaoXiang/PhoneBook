package com.xiang.os;

import java.util.concurrent.Exchanger;
import java.util.concurrent.TimeUnit;

import com.xiang.utils.Log;

/**
 * 用于线程执行完Runnable后交换数据
 * 
 * @author xiang.zc
 */
public abstract class ExchangeRunnable<T> implements Runnable {
	private final String THIS_FILE = "ExchangeRunnable";
	private static final long TIME_OUT_IN_MILLIS = 4000;
	private final long mTimeoutMs;
	private final Exchanger<T> mExchanger;

	public ExchangeRunnable(long timeoutUs) {
		mExchanger = new Exchanger<T>();
		mTimeoutMs = timeoutUs;
	}

	public ExchangeRunnable() {
		this(TIME_OUT_IN_MILLIS);
	}

	protected abstract T run(Exchanger<T> exchanger);

	@Override
	public void run() {
		try {
			Log.d(THIS_FILE, "run() entering" + this.getClass().getName());
			T t = run(mExchanger);
			exchange(t);
			Log.d(THIS_FILE, "run() leaving " + this.getClass().getName());
		} catch (Exception e) {
			Log.e(THIS_FILE, "Fatal run() failed", e);
			e.printStackTrace();
		}
	}

	public T exchange(T t) {
		try {
			return (mExchanger.exchange(t, mTimeoutMs, TimeUnit.MILLISECONDS));
		} catch (Exception e) {
			Log.e(THIS_FILE, "Fatal exchange() failed", e);
		}
		return (null);
	}
}
