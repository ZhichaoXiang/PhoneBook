package com.xiang.utils;

/**
 * 
 * @author xiang.zc
 * 
 *         to store value safely under multi threads
 */
public abstract class SafeValue<T> {
	private final Object mValueLock = new Object();
	private T value;

	/**
	 * before value changing
	 */
	protected abstract void onChanging(T oldValue, T newValue);

	/**
	 * after value changed
	 */
	protected abstract void onChanged(T value);

	public SafeValue() {
		value = null;
	}

	protected T getValue() {
		synchronized (mValueLock) {
			return (value);
		}
	}

	protected void setValue(T v) {
		synchronized (mValueLock) {
			if (value != v) {
				onChanging(value, v);
				value = v;
				onChanged(value);
			}
		}
	}
}
