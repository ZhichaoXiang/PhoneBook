package com.xiang.os;

import java.lang.ref.WeakReference;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import com.xiang.utils.Log;

/**
 * Executes immediate runnable in a single thread.
 * 
 * @author xiang.zc
 */
public class ThreadExecutor<T> {
	private final String THIS_FILE = "ThreadExecutor";
	private final HandlerThread mHandlerThread;
	private final Handler mHandler;
	private final WeakReference<T> mWeakReferences;

	public ThreadExecutor(T weakReference) {
		this(weakReference, "Normal.ThreadExecutor");
	}

	public ThreadExecutor(T weakReference, String threadName) {
		mWeakReferences = new WeakReference<T>(weakReference);
		mHandlerThread = new HandlerThread(threadName);
		mHandlerThread.start();
		mHandler = new Handler(mHandlerThread.getLooper()) {
			@Override
			public void handleMessage(Message msg) {
				ThreadExecutor.this.handleMessage(msg);
			}
		};
	}

	/**
	 * 在mHandlerThread中处理Message
	 */
	private void handleMessage(Message msg) {
		Log.d(THIS_FILE, "handleMessage() " + msg.obj);
		if (msg.obj instanceof Runnable) {
			executeRunnable((Runnable) msg.obj);
		} else {
			handleMessage(getReference(), msg);
		}
	}

	/**
	 * 在mHandlerThread中执行 Runnable
	 */
	private void executeRunnable(Runnable runnable) {
		try {
			onPreRun(getReference(), runnable);
			Log.d(THIS_FILE, "executeRunnable() "
					+ runnable.getClass().getName());
			runnable.run();
		} catch (Throwable t) {
			Log.e(THIS_FILE, "Fatal executeRunnable() " + runnable, t);
			t.printStackTrace();
		} finally {
			onPostRun(getReference(), runnable);
		}
	}

	/**
	 * 获取mHandlerThread的Handler
	 */
	public Handler getHandler() {
		return (mHandler);
	}

	/**
	 * can override this function to handle other not runnable message
	 */
	public void handleMessage(T t, Message msg) {
		Log.e(THIS_FILE,
				"Fatal handleMessage() can not handle a non-runnable obj!");
	}

	/**
	 * 发送Message,运行Runnable
	 */
	public void execute(int what, Runnable runnable) {
		if (runnable != null) {
			Log.d(THIS_FILE, "execute() entering "
					+ runnable.getClass().getName() + " in " + getThreadInfo());
			onPreCall(getReference(), runnable);
			boolean send = mHandler.sendMessage(mHandler.obtainMessage(what,
					runnable));
			onPostCall(getReference(), runnable);
			Log.d(THIS_FILE, "execute() leaving "
					+ runnable.getClass().getName() + " in " + getThreadInfo()
					+ " send=" + send);
		} else {
			Log.e(THIS_FILE, "Fatal execute() runnable is not a runnable!");
		}
	}

	/**
	 * 发送Message,运行Runnable
	 */
	public void execute(Runnable runnable) {
		execute(0, runnable);
	}

	/**
	 * 检测调用isSameThread的所在的线程是否就是mHandlerThread线程
	 */
	public boolean isSameThread() {
		return (mHandlerThread.getId() == Thread.currentThread().getId());
	}

	/**
	 * 发送Message,运行Runnable,并return 在mHandlerThread线程中执行完后的返回值
	 * 不能在mHandlerThread线程中调用executeWithReturn在调用线程中执行,否则死锁
	 */
	public synchronized Object executeWithReturn(int what,
			ExchangeRunnable<Object> exchangeRunnable) {
		Log.d(THIS_FILE, "executeWithReturn() entering "
				+ exchangeRunnable.getClass().getName() + " in "
				+ getThreadInfo());
		Object result = null;
		try {
			if (isSameThread()) {
				result = exchangeRunnable.run(null);
			} else {
				execute(what, exchangeRunnable);
				result = exchangeRunnable.exchange(null);
			}
		} catch (Exception e) {
			Log.e(THIS_FILE, "Fatal executeWithReturn() failed", e);
			e.printStackTrace();
		}
		Log.d(THIS_FILE, "executeWithReturn() leaving "
				+ exchangeRunnable.getClass().getName() + " in "
				+ getThreadInfo());
		return (result);
	}

	/**
	 * 发送Message,运行Runnable,并return 在mHandlerThread线程中执行完后的返回值
	 */
	public Object executeWithReturn(ExchangeRunnable<Object> exchangeRunnable) {
		return (executeWithReturn(0, exchangeRunnable));
	}

	/**
	 * 停止线程
	 */
	public void stopThread(boolean wait, long millis) {
		boolean quitted = true;
		try {
			quitted = mHandlerThread.quit();
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(THIS_FILE,
					"Fatal stopThread() Something is wrong with api level declared use fallback method");
		}
		if (!quitted && mHandlerThread.isAlive() && wait) {
			try {
				mHandlerThread.join(millis);
			} catch (Exception e) {
				e.printStackTrace();
				Log.e(THIS_FILE, "Can not finish handler thread", e);
			}
		}
	}

	/**
	 * 停止线程,如果失败,将等待500ms
	 */
	public void stopThread() {
		stopThread(true, 500);
	}

	public T getReference() {
		return (mWeakReferences.get());
	}

	/**
	 * 在发送Message之前要执行操作
	 */
	protected void onPreCall(T weakReference, Runnable runnable) {
	}

	/**
	 * 在发送Message之后要执行操作
	 */
	protected void onPostCall(T weakReference, Runnable runnable) {
	}

	/**
	 * 在mHandlerThread中在运行Runnable之前的操作
	 */
	protected void onPreRun(T weakReference, Runnable runnable) {
	}

	/**
	 * 在mHandlerThread中在运行Runnable之后的操作
	 */
	protected void onPostRun(T weakReference, Runnable runnable) {
	}

	/**
	 * this is for debug,get informations about mHandlerThread
	 */
	private String getThreadInfo() {
		return (mHandlerThread.getName() + "(tid=" + mHandlerThread.getId()
				+ ") state=" + mHandlerThread.getState());
	}
}