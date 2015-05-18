package ai;

public class PositionHandler implements Runnable {

	private Thread thread;
	private static PositionHandler positionHandler;

	private PositionHandler() {
		thread = new Thread(this);
	}

	public synchronized void run() {
		System.out.println("PHandler : DANS LE RUN");
		AIHandler ai = AIHandler.getInstance();
			while (true) {
				System.out.println("Pos : j'attends");
				//aiThread.wait();
				ai.waitLock();
				for(int i=0;i<10;i++){
					System.out.println("Pos :"+i);
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				//Calculation
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
