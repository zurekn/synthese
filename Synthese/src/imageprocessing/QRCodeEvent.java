package imageprocessing;

public class QRCodeEvent {
	
	private String data;
	
	QRCodeEvent(String data){
		this.data = data;
	}
	
	public String getData(){
		return data;
	}
}
