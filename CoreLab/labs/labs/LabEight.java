package labs;

import iproc.ImageProcessor;
import iproc.lib.ConvolveLib;

import java.awt.image.BufferedImage;
import java.io.File;

public class LabEight {
	public static String WORK_DIR = "/Users/eric/Desktop/mudd_fall2014/lab/8/";
	
	private static ImageProcessor cproc = new ImageProcessor();

	public static void main(String args[]) {
		problemOne();
		problemTwo();
		problemThree();
		problemFour();
	}
	
	private static void problemOne() {
		File baseFile = new File(WORK_DIR+"lenna.png");
		File outputFile1 = new File(WORK_DIR+"w_5-low-pass.png");
		File outputFile2 = new File(WORK_DIR+"w_5-high-pass.png");
		File outputFile3 = new File(WORK_DIR+"w_5-high-boost.png");
	
		float[][] lpFilter = ConvolveLib.KERNEL_LAB7_W5;
		float[][] apFilter = ConvolveLib.KERNEL_ALLPASS_3;
		float[][] hpFilter = ConvolveLib.kernelDifference(apFilter, lpFilter);
		float[][] hbFilter = ConvolveLib.kernelSum(apFilter, 
								ConvolveLib.multiplyScalar(
								ConvolveLib.KERNEL_LAB7_W5, 2f/9f));
				
		// low pass filtering
		cproc.readWorkingImage(baseFile);
		cproc.convolve(lpFilter);
		cproc.writeWorkingImage(outputFile1);
		
		// high pass filtering
		cproc.readWorkingImage(baseFile);
		cproc.convolve(hpFilter);
		cproc.writeWorkingImage(outputFile2);
		
		// boost filtering
		cproc.readWorkingImage(baseFile);
		cproc.convolve(hbFilter);
		cproc.writeWorkingImage(outputFile3);
	}
	
	private static void problemTwo() {
		File baseFile = new File(WORK_DIR+"lennaC.png");
		File outputFile1 = new File(WORK_DIR+"intensity-low-pass.png");
		File outputFile2 = new File(WORK_DIR+"intensity-high-pass.png");
		File outputFile3 = new File(WORK_DIR+"intensity-high-boost.png");
	
		float[][] lpFilter = ConvolveLib.KERNEL_LAB7_W5;
		float[][] apFilter = ConvolveLib.KERNEL_ALLPASS_3;
		float[][] hpFilter = ConvolveLib.kernelDifference(apFilter, lpFilter);
		float[][] hbFilter = ConvolveLib.kernelSum(apFilter, 
								ConvolveLib.multiplyScalar(
								ConvolveLib.KERNEL_LAB7_W5, 2f/9f));
		
		// low pass filtering
		cproc.readWorkingImage(baseFile);
		convolveIntensity(cproc.getImage(), lpFilter);
		cproc.writeWorkingImage(outputFile1);
		
		// high pass filtering
		cproc.readWorkingImage(baseFile);
		convolveIntensity(cproc.getImage(), hpFilter);
		cproc.writeWorkingImage(outputFile2);
		
		// boost filtering
		cproc.readWorkingImage(baseFile);
		convolveIntensity(cproc.getImage(), hbFilter);
		cproc.writeWorkingImage(outputFile3);
	}
	
	private static void problemThree() {
		File baseFile = new File(WORK_DIR+"lenna.png");
		File outputFile1 = new File(WORK_DIR+"roberts-gradient.png");
		File outputFile2 = new File(WORK_DIR+"sobel-gradient.png");
		File outputFile3 = new File(WORK_DIR+"prewit-3x3-gradient.png");
		File outputFile4 = new File(WORK_DIR+"prewit-4x4-gradient.png");
	
		// roberts 
		cproc.readWorkingImage(baseFile);
		cproc.gradient(ConvolveLib.GRAD_ROBERTS_X, ConvolveLib.GRAD_ROBERTS_Y);
		cproc.writeWorkingImage(outputFile1);
		
		// sobel
		cproc.readWorkingImage(baseFile);
		cproc.gradient(ConvolveLib.GRAD_SOBEL_X, ConvolveLib.GRAD_SOBEL_Y);
		cproc.writeWorkingImage(outputFile2);
		
		// prewitt 3x3
		cproc.readWorkingImage(baseFile);
		cproc.gradient(ConvolveLib.GRAD_PREWITT_X_3, ConvolveLib.GRAD_PREWITT_Y_3);
		cproc.writeWorkingImage(outputFile3);
		
		// prewitt 4x4
		cproc.readWorkingImage(baseFile);
		cproc.gradient(ConvolveLib.GRAD_PREWITT_X_4, ConvolveLib.GRAD_PREWITT_Y_4);
		cproc.writeWorkingImage(outputFile4);
	}
	
	private static void problemFour() {
		File baseFile = new File(WORK_DIR+"lenna.png");
		File outputFile1 = new File(WORK_DIR+"north-compass.png");
		File outputFile2 = new File(WORK_DIR+"east-compass.png");
		File outputFile3 = new File(WORK_DIR+"south-compass.png");
		File outputFile4 = new File(WORK_DIR+"west-compass.png");
	
		// north 
		cproc.readWorkingImage(baseFile);
		cproc.convolve(ConvolveLib.GRAD_COMP_N_3);
		cproc.writeWorkingImage(outputFile1);
		
		// east
		cproc.readWorkingImage(baseFile);
		cproc.convolve(ConvolveLib.GRAD_COMP_E_3);
		cproc.writeWorkingImage(outputFile2);
		
		// south
		cproc.readWorkingImage(baseFile);
		cproc.convolve(ConvolveLib.GRAD_COMP_S_3);
		cproc.writeWorkingImage(outputFile3);
		
		// west
		cproc.readWorkingImage(baseFile);
		cproc.convolve(ConvolveLib.GRAD_COMP_W_3);
		cproc.writeWorkingImage(outputFile4);
	}
	
	private static void convolveIntensity(BufferedImage img, final float[][] kernel) {
		ImageProcessor colorProc = new ImageProcessor(img); 
		float[][][] hsi = colorProc.getHsi();
		
		hsi[2] = ImageProcessor.convolveArrayPrimative(kernel, hsi[2]);
		
		colorProc.setFromHsi(hsi);
	}
}
