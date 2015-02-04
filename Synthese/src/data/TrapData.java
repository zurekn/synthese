package data;

import java.util.ArrayList;

public class TrapData {
	public static ArrayList<Trap> traps = new ArrayList<Trap>();

	public void addTrap(Trap t) {
		traps.add(t);
	}

	public static Trap getTrapById(String id) {
		for (int i = 0; i < traps.size(); i++) {
			if (traps.get(i).getId().equals(id)) {
				return traps.get(i);
			}
		}
		return null;
	}
}
