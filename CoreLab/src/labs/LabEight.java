package labs;
import iproc.ConvolutionProcessor;
import iproc.ConvolveLib;

import java.io.File;


public class LabEight {
	public static String WORK_DIR = "/Users/eric/Desktop/mudd_fall2014/lab/8/";
	public static File baseFile = new File(WORK_DIR+"lenna.png");
	
	private static ConvolutionProcessor cproc = new ConvolutionProcessor();

	public static void main(String args[]) {
		problemOne();
	}
	
	private static void problemOne() {
		File outputFile1 = new File(WORK_DIR+"w_5-low-pass.png");
		File outputFile2 = new File(WORK_DIR+"w_5-high-pass.png");
	
		double[][] lpFilter = ConvolveLib.getW5Kernel();
		double[][] hpFilter = ConvolveLib.getHPfromLP(lpFilter); 
		
		// low pass filtering
		cproc.readWorkingImage(baseFile);
		cproc.convolveImage(lpFilter);
		cproc.writeWorkingImage(outputFile1);
		
		// high pass filtering
		cproc.readWorkingImage(baseFile);
		cproc.convolveImage(hpFilter);
		cproc.writeWorkingImage(outputFile2);
		
		// boost filtering
	}
}
