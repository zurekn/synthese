package testpackage;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.swing.SwingUtilities;
import com.google.zxing.NotFoundException;
import imageprocessing.*;
 
public class TraitementQRTest {

	public static void main(String[] args) throws NotFoundException, IOException {


		// SwingUtilities.invokeLater(new WebCamCapture());
		 TraitementImage ti = new TraitementImage();
		 QRCodeProcessing tqr = new  QRCodeProcessing();
		 String QRes="";
		 String QRImage = "Aero.png";
		 String QRList[] = {"Aero.png","Attaque.png","Shuriken.png","Heal.png","Potion.png","Ice crush.png","Fire Ball.png","Lightning Bolt.png","Water Wave.png","Key.png","Fur.png","Meteor.png","Defense.png"}; 
		// error with 
		 
		 // ti.makeBinaryImage("webcamCapture.jpg", "vintageWebcamCapture.jpg", "jpg", 110);
		 

		/* ti.compareBinaryImages("vintageWCapture.jpg", "vintageWCapture2.jpg", "vintageWCapture_pixEq120.jpg", 120,0);
		 ti.compareBinaryImages("vintageWCapture.jpg", "vintageWCapture2.jpg", "vintageWCapture_pixSub120.jpg",120,1);
		 ti.compareBinaryImages("vintageWCapture.jpg", "vintageWCapture2.jpg", "vintageWCapture_pixEq20.jpg", 20,0);
		 ti.compareBinaryImages("vintageWCapture.jpg", "vintageWCapture2.jpg", "vintageWCapture_pixSub20.jpg",20,1);
		 */
		 
		// ti.makeBinaryImage("lineTest.jpg", "lineTestVintage.jpg", "jpg",91);
		
			//tqr.findAllQR(QRImage,1,null);
		//QRes = tqr.getQRDatas();
		//System.out.println("QRCODE Name - "+QRImage+" - ID QRCODE : "+QRes );
			if(QRList != null){
				for(String img : QRList){
					System.out.println(img);
					tqr.findAllQR(img,1,null);
					QRes = tqr.getQRDatas();
					System.out.println("QRCODE Name - "+QRImage+" - ID QRCODE : "+QRes );
				}
			}
		 
		 
		 
		
	}

}
		