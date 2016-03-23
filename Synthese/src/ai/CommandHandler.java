package ai;

import game.Character;
import game.Mob;
import game.Player;
import game.WindowGame;

import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.event.EventListenerList;

import org.lwjgl.Sys;

import data.Handler;

//TODO add some done signal so the main thread cam make this one sleep
public class CommandHandler extends Handler {
	private static CommandHandler commandHandler;
	private static ArrayList<String> commands = new ArrayList<String>();
	private static ReentrantLock accessLock = new ReentrantLock(true);
	private boolean calculationDone = false;
	private static final int MIN_TIME = 1000; // minimum time between commands
												// action

	private final EventListenerList listeners = new EventListenerList();

	private CommandHandler() {
		super();
	}

	public void run() {
		System.out.println("CommmandHandler : DANS LE RUN");
		this.lock();
		long startTime = System.currentTimeMillis();
		long currentTime;

		AIHandler.getInstance().begin();
		while (true) {
			unlockTemporay(1);
			/*try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}*/
			currentTime = System.currentTimeMillis();

			if ((currentTime - startTime) > MIN_TIME)
				if (hasCommand()) {
					startTime = System.currentTimeMillis();
					// ID useless for this case
					newAction("", getFirstCommand());
				} else if (calculationDone)
					WindowGame.getInstance().getHandler().waitLock();
		}

	}

	public void setCalculationDone(boolean calculationDone) {
		this.calculationDone = calculationDone;
	}

	@Override
	public void begin() {
		System.out.println("Launch the Command Handler Thread");
		if(getThread().isInterrupted())
			getThread().resume();
		else if(getThread().getState().name() == "TIMED_WAITING")
		{
			
		}else
		{
			getThread().start();
		}
	}

	public static CommandHandler getInstance() {
		if (commandHandler == null)
			commandHandler = new CommandHandler();
		return commandHandler;
	}

	public void startCommandsCalculation(Character currentCharacter,
			ArrayList<Player> players, ArrayList<Mob> mobs, int turn) {
		accessLock.lock();
		commands = new ArrayList<String>();
		accessLock.unlock();
		calculationDone = false;
		AIHandler.getInstance().startCommandsCalculation(
				new WindowGameData(players, mobs, turn), currentCharacter);

		// FOR DEBUG !!
		// newAction(currentCharacter.getId(), cmd);
	}

	protected void newAction(String id, String data) {
		ActionEvent event = null;
		for (CommandListener listener : getAIListener()) {
			if (event == null)
				event = new ActionEvent(id, data);
			listener.newAction(event);
		}
	}

	public void addCommandListener(CommandListener listener) {
		listeners.add(CommandListener.class, listener);
	}

	public void removeCommandListener(CommandListener listener) {
		listeners.remove(CommandListener.class, listener);
	}

	public CommandListener[] getAIListener() {
		return listeners.getListeners(CommandListener.class);
	}

	public boolean hasCommand() {
		accessLock.lock();
		boolean b = commands.size() != 0;
		accessLock.unlock();
		return b;
	}

	/**
	 * hasCommand is intended to be called first to ensure that there's a
	 * command ready
	 * 
	 * @return the first command of the list and remove it
	 */
	public String getFirstCommand() {
		accessLock.lock();
		String command = "";
		command = commands.get(0);
		commands.remove(0);
		accessLock.unlock();
		return command;
	}

	public void addCommand(String command) {
		accessLock.lock();
		commands.add(command);
		accessLock.unlock();
	}
}
