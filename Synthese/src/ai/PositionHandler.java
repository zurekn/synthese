package ai;

public class PositionHandler implements Runnable {

	private Thread thread;
	private static PositionHandler positionHandler;

	private PositionHandler() {
		thread = new Thread(this);
	}

	public void run() {
		System.out.println("PHandler : DANS LE RUN");
		Thread aiThread = AIHandler.getInstance().getThread();

		synchronized (aiThread) {
			while (true) {
				//Waiting ai thread to start working
				try {
					aiThread.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				//Calculation
			}
		}
	}

	public void begin() {
		System.out.println("Launch the Position Handler Thread");
		thread.start();
	}

	public Thread getThread() {
		return thread;
	}

	public static PositionHandler getInstance() {
		if (positionHandler == null)
			positionHandler = new PositionHandler();
		return positionHandler;
	}

}
