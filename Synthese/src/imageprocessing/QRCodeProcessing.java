package imageprocessing;
import javax.swing.SwingUtilities;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.ResultPoint;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.multi.qrcode.QRCodeMultiReader;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.qrcode.encoder.ByteMatrix;

import data.Data;


public class QRCodeProcessing {
	
private String data;
private String filePath;
private String charset;
private Map hintMap;
/**
 *  variable that will contain some datas in this form : ID:Orientation:CenterX:CenterY
 */
private String QRDatas;
private QRCodeEvent qrCodeEvent;

	
	public  QRCodeProcessing(){
		super();
		charset = "UTF-8"; // or "ISO-8859-1"
		hintMap = new HashMap();
		hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
		filePath = "Synthese/res/testRes/QRcodes/";
		QRDatas = "";
	    }
	
	
	// encode
	public void generateQR(String destFile, String data,int qrCodeheight, int qrCodewidth)throws WriterException, IOException {
				BitMatrix matrix = new MultiFormatWriter().encode(new String(data.getBytes(charset), charset),BarcodeFormat.QR_CODE, qrCodewidth, qrCodeheight, hintMap);
				MatrixToImageWriter.writeToFile(matrix, filePath.substring(filePath.lastIndexOf('.') + 1), new File(filePath+destFile));
	}
	
	
		
	/** Find a QR code at any position in an image
	 * 
	 * @param srcQR, String
	 * @return the ID coded into the QRCode, String
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws NotFoundException (if no QR Code was found)
	 */
	public String decodeQR(String srcQR)throws FileNotFoundException, IOException, NotFoundException {
		BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(ImageIO.read(new FileInputStream(filePath+srcQR)))));
		Result qrCodeResult = new MultiFormatReader().decode(binaryBitmap,hintMap);
		return qrCodeResult.getText();
	}
	
	/** Find all QR codes in an image
	 * 
	 * @param srcImgName : The name of the image we want to test and decode if there is a QR Code in it, String
	 * @param seuil : the variation in pixel that filter the orientation of the QRCode, float
	 * @param imweb : the buffered image to decode, BufferedImage
	 * @throws IOException
	 * @throws NotFoundException (if no QR Code was found)
	 */
	public void findAllQR(String srcImgName,float seuil, BufferedImage imweb) throws IOException, NotFoundException{
		System.out.println(" Lancement de : Détection QR Codes");
		QRDatas = ""; // reset final object
		String res="";
		
		// Read Image
		BufferedImage img;
		// Will contain all QRCode results
		Result[] result = null;
		// Used to detect position of the QRCode
		ResultPoint[] rp = null;
		// Multi reader permit to detect multiple QR Codes
		QRCodeMultiReader mdr = new QRCodeMultiReader();
		// Count the number of read QRCodes
		int i= 1;
	
		// initialisation of the hint of type try harder
		Hashtable<DecodeHintType, Object> hints = new Hashtable<DecodeHintType, Object>();
	    hints.put(DecodeHintType.TRY_HARDER, true);
	    if(imweb == null){
		img = ImageIO.read(new File(filePath + srcImgName));
	}else{
		img = imweb;
	}
		LuminanceSource source = new BufferedImageLuminanceSource(img);
		BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
			
			result = mdr.decodeMultiple(bitmap,hints);

			
			for(Result rs : result){
				rp = rs.getResultPoints();
				QRDatas += checkOrientation(seuil, rp, rs.getText())+"\n";
				qrCodeEvent = createQrCodeEvent(seuil, rp, rs.getText()+"\n");
			}
	}
	
	/**
	 * Method that put datas of QR Codes in events
	 * @param seuil - Area of authorized variation of position, float
	 * @param rp - passed from another method, ResultPoint[]
	 * @param QRContent - ID of the QRCode (used in final object), String
	 * @return
	 */
	public QRCodeEvent createQrCodeEvent(float seuil,ResultPoint[] rp,String QRContent){
		int direction;
		String id = QRContent;
		// Count each eye in the QR Code
		int j= 1;
		float xeye1 = 0;
		float xeye2 = 0;
		float xeye3 = 0;
		float xeye4 = 0;
		float yeye1 = 0;
		float yeye2 = 0;
		float yeye3 = 0;
		float yeye4 = 0;
		float ecartXlarg;
		float ecartXhoriz;
		float ecartYhaut;
		float ecartYlarg;
		// to calculate the center of QRCode
		float xmid = 0, ymid = 0;
		
		// Calcul des positions des yeux
		for(ResultPoint resp : rp){
			if(j==1){
				xeye1 = resp.getX();
				yeye1 = resp.getY();
			}
			if(j==2){
				xeye2 = resp.getX();
				yeye2 = resp.getY();
			}
			if(j==3){
				xeye3 = resp.getX();
				yeye3 = resp.getY();
			}
			if(j==4){
				xeye4 = resp.getX();
				yeye4 = resp.getY();
			}
			j++;
		}
		j=1;
		ecartXlarg=(xeye1 - xeye2);
		ecartYlarg=(yeye2 - yeye3);
		
		ecartYhaut=(yeye1 - yeye2);
		ecartXhoriz=(xeye2 - xeye3);

		
		// RIght
		if(ecartXlarg < (0-seuil) ){
			if(ecartYlarg < (0-seuil)){
				
				if(ecartXhoriz > seuil || ecartXhoriz < (0-seuil)){
					// Top
					if(ecartYhaut > seuil){
						direction = Data.NORTH_EAST;
						
					// Bottom
					}else{
						direction = Data.SOUTH_EAST;
					}
					
				}else{
					direction = Data.EAST;
				}
			}else{
				direction = Data.NORTH;
			}
			// if X larg > 0  => Left
		}else if(ecartXlarg > seuil){
			if(ecartYlarg < (0-seuil)){
				direction = Data.NORTH;
			}else{
				
				if(ecartXhoriz > seuil || ecartXhoriz < (0-seuil)){
					// Top
					if(ecartYhaut > seuil){
						direction = Data.NORTH_WEST;
						
					// Bottom
					}else{
						direction = Data.SOUTH_WEST;
					}
					
				}else{
					direction = Data.WEST;
				}
				
			}
			//  Bottom
		}else{				
				if(ecartXhoriz > seuil || ecartXhoriz < (0-seuil)){
					// Top
					if(ecartYhaut > seuil){
						direction = Data.NORTH;
					// Bottom
					}else{
						direction = Data.SOUTH;
					}
					// Left
				}else{
					direction = Data.WEST;
				}
				
		
		}
		
		// Calcul of middle point : 
		xmid = ((xeye3 - xeye2)/2) + xeye2;
		ymid = ((yeye1 - yeye2)/2) + yeye2;
		xmid = xmid < 0 ? -xmid : xmid;
		ymid = ymid < 0 ? -ymid : ymid;
		return new QRCodeEvent(id, direction, (int)xmid,(int)ymid);
	}
	
	/**
	 * Method that calculate the datas of a given QRCode and put them in a variable
	 * @param seuil - Area of authorized variation of position, float
	 * @param rp - passed from another method, ResultPoint[]
	 * @param QRContent - ID of the QRCode (used in final object), String
	 * @return result - All the datas put between ":", String
	 */
	public String checkOrientation(float seuil,ResultPoint[] rp,String QRContent){
		
		String result=QRContent+":";
		// Count each eye in the QR Code
		int j= 1;
		float xeye1 = 0;
		float xeye2 = 0;
		float xeye3 = 0;
		float xeye4 = 0;
		float yeye1 = 0;
		float yeye2 = 0;
		float yeye3 = 0;
		float yeye4 = 0;
		float ecartXlarg;
		float ecartXhoriz;
		float ecartYhaut;
		float ecartYlarg;
		// to calculate the center of QRCode
		float xmid = 0, ymid = 0;
		
		// Calcul des positions des yeux
		for(ResultPoint resp : rp){
			if(j==1){
				xeye1 = resp.getX();
				yeye1 = resp.getY();
			}
			if(j==2){
				xeye2 = resp.getX();
				yeye2 = resp.getY();
			}
			if(j==3){
				xeye3 = resp.getX();
				yeye3 = resp.getY();
			}
			if(j==4){
				xeye4 = resp.getX();
				yeye4 = resp.getY();
			}
			j++;
		}
		j=1;
		ecartXlarg=(xeye1 - xeye2);
		ecartYlarg=(yeye2 - yeye3);
		
		ecartYhaut=(yeye1 - yeye2);
		ecartXhoriz=(xeye2 - xeye3);
		
		// RIght
		if(ecartXlarg < (0-seuil) ){
			if(ecartYlarg < (0-seuil)){
				
				if(ecartXhoriz > seuil || ecartXhoriz < (0-seuil)){
					// Top
					if(ecartYhaut > seuil){
						result += "haut-droite" ;
						
					// Bottom
					}else{
						result += "bas-droite" ;
					}
					
				}else{
					result += "droite";
				}
			}else{
				result += "haut" ;
			}
		}else if(ecartXlarg > seuil){
			if(ecartYlarg < (0-seuil)){
				result += "haut" ;
			}else{
				
				if(ecartXhoriz > seuil || ecartXhoriz < (0-seuil)){
					// Top
					if(ecartYhaut > seuil){
						result += "haut-gauche" ;
						
					// Bottom
					}else{
						result += "bas-gauche" ;
					}
					
				}else{
					result += "gauche" ;
				}
				
			}
			//  Bottom
		}else{				
				if(ecartXhoriz > seuil || ecartXhoriz < (0-seuil)){
					// Top
					if(ecartYhaut > seuil){
						result += "haut" ;
					// Bottom
					}else{
						result += "bas" ;
					}
					// Left
				}else{
					result += "gauche" ;
				}
				
		
		}
		
		// Calcul of middle point : 
		xmid = ((xeye3 - xeye2)/2) + xeye2;
		ymid = ((yeye1 - yeye2)/2) + yeye2;
		xmid = xmid < 0 ? -xmid : xmid;
		ymid = ymid < 0 ? -ymid : ymid;
		result+=":"+xmid+":"+ymid;
		return result;
	}
	
	
	public String getQRDatas(){
		return QRDatas;
	}
	
	public QRCodeEvent getQRCodeEvent(){
		return qrCodeEvent;
	}
	
}// End of class
