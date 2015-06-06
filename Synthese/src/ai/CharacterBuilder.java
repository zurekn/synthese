package ai;

import java.util.ArrayList;

public abstract class CharacterBuilder {

	public static ArrayList<CharacterData> build(ArrayList<CharacterData> characters){
		ArrayList<CharacterData> list = new ArrayList<CharacterData>();
		for(CharacterData c : characters)
			list.add(new CharacterData(c));
		
		return list;
	}

}
