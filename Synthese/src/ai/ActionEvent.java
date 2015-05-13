package ai;

public class ActionEvent {

	private String action;
	private String id;
	public ActionEvent(String id, String data){
		this.id = id;
		this.action = data;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEvent() {
		return action;
	}

	public void setEvent(String event) {
		this.action = event;
	}

	@Override
	public String toString() {
		return "ActionEvent [event=" + action + ", id=" + id + "]";
	}

	
	
	
}
