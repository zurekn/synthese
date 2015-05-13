package ai;

import java.util.EventListener;

public interface AIListener extends EventListener{
	
	void newAction(ActionEvent e);
}
