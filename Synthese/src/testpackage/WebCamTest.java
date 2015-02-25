package testpackage;

import imageprocessing.WebCamCapture;

import javax.swing.SwingUtilities;

public class WebCamTest {

	public static void main(String [] args){
		SwingUtilities.invokeLater(new WebCamCapture());
	}
}
