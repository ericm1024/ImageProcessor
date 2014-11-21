package labs;

import iproc.ImageProcessor;
import iproc.lib.ConvolveLib;

import java.io.File;


public class LabSeven {
	public static String WORK_DIR = "/Users/eric/Desktop/mudd_fall2014/lab/7/";
	public static File baseFile = new File(WORK_DIR+"noisy_taxi.png");
	
	private static ImageProcessor proc7 = new ImageProcessor();
	
	public static void main(String args[]) {
		problemOne();
		problemTwo();
		problemThree();
		problemFour();
	}

	public static void problemOne() {
		File outputFile1 = new File(WORK_DIR+"w_5-kernel.png");
		
		proc7.readWorkingImage(baseFile);
		proc7.convolve(ConvolveLib.KERNEL_LAB7_W5);
		proc7.writeWorkingImage(outputFile1);
	}
	
	public static void problemTwo() {
		int width = 7;
		float sigma = 2f;
		File outputFile1 = new File(WORK_DIR+"gauss-kernel-sigma="+sigma+"pixels.png");
		
		proc7.readWorkingImage(baseFile);
		proc7.convolve(ConvolveLib.getGauss(width, sigma));
		proc7.writeWorkingImage(outputFile1);
	}

	public static void problemThree() {
		int side = 5;
		int thickness = 3;
		File outputFile1 = new File(WORK_DIR+"cross-median-filter-side="+side+",thickness="+thickness+".png");
		
		proc7.readWorkingImage(baseFile);
		proc7.medianFilter(ConvolveLib.getCrossFilter(side, thickness));
		proc7.writeWorkingImage(outputFile1);
	}
	
	public static void problemFour() {
		int side = 5;
		int thickness = 1;	
		int gaussSize = 5;
		float sigma = 2f;
		
		File outputFile1 = new File(WORK_DIR+"best-cleanup.png");
		proc7.readWorkingImage(baseFile);
		proc7.medianFilter(ConvolveLib.getCrossFilter(side, thickness));
		proc7.convolve(ConvolveLib.getGauss(gaussSize, sigma));
		proc7.writeWorkingImage(outputFile1);
	}
}
