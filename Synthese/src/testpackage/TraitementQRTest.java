package testpackage;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.swing.SwingUtilities;
import com.google.zxing.NotFoundException;
import imageprocessing.*;
 
public class TraitementQRTest {

	public static void main(String[] args) throws NotFoundException, IOException {


		SwingUtilities.invokeLater(new WebCamCapture());
		 TraitementImage ti = new TraitementImage();
		  TraitementQRjavapapers tqr = new TraitementQRjavapapers();
		 String QRes="";
		 String QRImage = "QRCapture4.png";
		// ti.makeBinaryImage("webcamCapture.jpg", "vintageWebcamCapture.jpg", "jpg", 110);
		 

		/* ti.compareBinaryImages("vintageWCapture.jpg", "vintageWCapture2.jpg", "vintageWCapture_pixEq120.jpg", 120,0);
		 ti.compareBinaryImages("vintageWCapture.jpg", "vintageWCapture2.jpg", "vintageWCapture_pixSub120.jpg",120,1);
		 ti.compareBinaryImages("vintageWCapture.jpg", "vintageWCapture2.jpg", "vintageWCapture_pixEq20.jpg", 20,0);
		 ti.compareBinaryImages("vintageWCapture.jpg", "vintageWCapture2.jpg", "vintageWCapture_pixSub20.jpg",20,1);
		 */

		// ti.makeBinaryImage("lineTest.jpg", "lineTestVintage.jpg", "jpg",91);
		
		 
		
			QRes = tqr.findAllQR(QRImage,1,null);
		
		 System.out.println("find All - "+QRImage+" : ID de l'action du joueur : "+QRes );
		 
		 QRes = tqr.decodeQR(QRImage);
			
		 System.out.println("simple decode - "+QRImage+" : ID de l'action du joueur : "+QRes );
		 
		 
		
	}

}
		