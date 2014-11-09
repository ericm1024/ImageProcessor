package labs;
import iproc.ConvolutionProcessor;
import iproc.ConvolveLib;

import java.io.File;


public class LabSeven {
	public static String WORK_DIR = "/Users/eric/Desktop/mudd_fall2014/lab/7/";
	public static File baseFile = new File(WORK_DIR+"noisy_taxi.png");
	
	private static ConvolutionProcessor proc7 = new ConvolutionProcessor();
	
	
	public static void main(String args[]) {
		problemOne();
		problemTwo();
		problemThree();
		problemFour();
	}

	public static void problemOne() {
		File outputFile1 = new File(WORK_DIR+"w_5-kernel.png");
		
		proc7.readWorkingImage(baseFile);
		proc7.convolve(ConvolveLib.getW5Kernel());
		proc7.writeWorkingImage(outputFile1);
	}
	
	public static void problemTwo() {
		double numSigma = 2.0;
		double sigma = 7.0/(2*numSigma);
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
		double numSigma = 2.0;
		
		File outputFile1 = new File(WORK_DIR+"best-cleanup.png");
		proc7.readWorkingImage(baseFile);
		proc7.filterImage(ConvolveLib.getCrossFilter(side, thickness));
		proc7.convolve(ConvolveLib.getGaussKernel(gaussSize, numSigma));
		proc7.writeWorkingImage(outputFile1);
	}
}
