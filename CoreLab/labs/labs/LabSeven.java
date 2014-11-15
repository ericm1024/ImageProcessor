package labs;

import iproc.ImageProcessor;
import iproc.ConvolveLib;

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
		float numSigma = 2;
		float sigma = 7f/(2*numSigma);
		File outputFile1 = new File(WORK_DIR+"gauss-kernel-sigma="+sigma+"pixels.png");
		
		proc7.readWorkingImage(baseFile);
		proc7.convolve(ConvolveLib.getGaussKernel(7, numSigma));
		proc7.writeWorkingImage(outputFile1);
	}

	public static void problemThree() {
		int side = 3;
		int thickness = 1;
		File outputFile1 = new File(WORK_DIR+"cross-median-filter-side="+side+",thickness="+thickness+".png");
		
		proc7.readWorkingImage(baseFile);
		proc7.filterImage(ConvolveLib.getCrossFilter(side, thickness));
		proc7.writeWorkingImage(outputFile1);
	}
	
	public static void problemFour() {
		int side = 5;
		int thickness = 1;	
		int gaussSize = 5;
		float numSigma = 2f;
		
		File outputFile1 = new File(WORK_DIR+"best-cleanup.png");
		proc7.readWorkingImage(baseFile);
		proc7.filterImage(ConvolveLib.getCrossFilter(side, thickness));
		proc7.convolve(ConvolveLib.getGaussKernel(gaussSize, numSigma));
		proc7.writeWorkingImage(outputFile1);
	}
}
