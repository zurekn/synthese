package data;

import java.util.concurrent.locks.ReentrantLock;

public abstract class Handler implements Runnable {
	private Thread thread;
	private final ReentrantLock lock = new ReentrantLock(true);

	protected Handler() {
		thread = new Thread(this);
	}

	protected Handler(Thread t) {
		thread = t;
	}

	public abstract void begin();

	/**
	 * Wait for the lock attribute until it's unlocked
	 */
	public void waitLock() {
		lock.lock();
		lock.unlock();
	}

	public void lock() {
		this.lock.lock();
	}

	public void lock(int n) {
		for (int i = 0; i < n; i++)
			this.lock.lock();
	}

	public void unlock() {
		this.lock.unlock();
	}

	public void unlockTemporay(int n) {
		int i;
		for (i = 0; i < lock.getHoldCount() && i < n; i++)
			lock.unlock();

		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		for (int j = 0; j < i; j++)
			lock.lock();

	}

	public void wake(int n) {
		for (int i = 0; i < lock.getHoldCount() && i < n; i++)
			lock.unlock();
	}

	public void wakeAll() {
		for (int i = 0; i < lock.getHoldCount(); i++)
			lock.unlock();
	}

	public Thread getThread() {
		return this.thread;
	}

}
