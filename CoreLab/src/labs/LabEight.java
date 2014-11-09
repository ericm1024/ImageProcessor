package labs;

import iproc.ColorspaceProcessor;
import iproc.ConvolutionProcessor;
import iproc.ConvolveLib;

import java.awt.image.BufferedImage;
import java.io.File;

public class LabEight {
	public static String WORK_DIR = "/Users/eric/Desktop/mudd_fall2014/lab/8/";
	
	private static ConvolutionProcessor cproc = new ConvolutionProcessor();

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
	
		double[][] lpFilter = ConvolveLib.getW5Kernel();
		double[][] apFilter = ConvolveLib.getApKernel(3);
		double[][] hpFilter = ConvolveLib.kernelDifference(apFilter, lpFilter);
		double[][] hbFilter = ConvolveLib.kernelSum(apFilter, 
								ConvolveLib.multiplyScalar(
								ConvolveLib.getW5Kernel(), 2.0/9.0));
				
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
	
		double[][] lpFilter = ConvolveLib.getW5Kernel();
		double[][] apFilter = ConvolveLib.getApKernel(3);
		double[][] hpFilter = ConvolveLib.kernelDifference(apFilter, lpFilter);
		double[][] hbFilter = ConvolveLib.kernelSum(apFilter, 
								ConvolveLib.multiplyScalar(
								ConvolveLib.getW5Kernel(), 2.0/9.0));
		
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
		cproc.gradient(ConvolveLib.getRobertsX(), ConvolveLib.getRobertsY());
		cproc.writeWorkingImage(outputFile1);
		
		// sobel
		cproc.readWorkingImage(baseFile);
		cproc.gradient(ConvolveLib.getSobelX(), ConvolveLib.getSobelY());
		cproc.writeWorkingImage(outputFile2);
		
		// prewitt 3x3
		cproc.readWorkingImage(baseFile);
		cproc.gradient(ConvolveLib.getPrewittX3(), ConvolveLib.getPrewittY3());
		cproc.writeWorkingImage(outputFile3);
		
		// prewitt 4x4
		cproc.readWorkingImage(baseFile);
		cproc.gradient(ConvolveLib.getPrewittX4(), ConvolveLib.getPrewittY4());
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
		cproc.convolve(ConvolveLib.getCompassNorth());
		cproc.writeWorkingImage(outputFile1);
		
		// east
		cproc.readWorkingImage(baseFile);
		cproc.convolve(ConvolveLib.getCompassEast());
		cproc.writeWorkingImage(outputFile2);
		
		// south
		cproc.readWorkingImage(baseFile);
		cproc.convolve(ConvolveLib.getCompassSouth());
		cproc.writeWorkingImage(outputFile3);
		
		// west
		cproc.readWorkingImage(baseFile);
		cproc.convolve(ConvolveLib.getCompassWest());
		cproc.writeWorkingImage(outputFile4);
	}
	
	private static void convolveIntensity(BufferedImage img, double[][] kernel) {
		ColorspaceProcessor colorProc = new ColorspaceProcessor(img); 
		double[][][] hsi = colorProc.getHsi();
		
		hsi[2] = ConvolutionProcessor
				.convolveArrayPrimative(kernel, hsi[2]);
		
		colorProc.setFromHsi(hsi);
	}
}
