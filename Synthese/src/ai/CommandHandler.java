package ai;

import game.Character;
import game.Mob;
import game.Player;
import game.WindowGame;

import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.event.EventListenerList;

import data.Handler;

public class CommandHandler extends Handler {
	private static CommandHandler commandHandler;
	private static ArrayList<String> commands = new ArrayList<String>();
	private static ReentrantLock accessLock = new ReentrantLock(true);
	private boolean calculationDone = false;

	private final EventListenerList listeners = new EventListenerList();

	private CommandHandler() {
		super();
	}

	public void run() {
		System.out.println("CommmandHandler : DANS LE RUN");
		this.lock();
		AIHandler.getInstance().begin();
		while (true) {
			unlockTemporay(1);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(hasCommand()){
				newAction("",getFirstCommand());
				//TODO check if ID is used
			}
			if(calculationDone){
				WindowGame.getInstance().getHandler().waitLock();
			}
		}

	}

	@Override
	public void begin() {
		System.out.println("Launch the Command Handler Thread");
		getThread().start();
	}

	public static CommandHandler getInstance() {
		if (commandHandler == null)
			commandHandler = new CommandHandler();
		return commandHandler;
	}

	public void startCommandsCalculation(Character currentCharacter,
			ArrayList<Player> players, ArrayList<Mob> mobs, int turn) {
		// TODO mettre en place le systeme de recolte d'action et les faire dans
		// l'ordre
		accessLock.lock();
		commands = new ArrayList<String>();
		accessLock.unlock();
		AIHandler.getInstance().startCommandsCalculation(
				new WindowGameData(players, mobs, turn), currentCharacter);

		// FOR DEBUG !!
		//newAction(currentCharacter.getId(), cmd);

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
