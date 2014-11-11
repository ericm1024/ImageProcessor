package labs;

import iproc.ConvolutionProcessor;
import iproc.ConvolveLib;

import java.awt.image.BufferedImage;
import java.io.File;

@SuppressWarnings("unused")
public class LabNine {
	public static String WORK_DIR = "/Users/eric/Desktop/mudd_fall2014/lab/9/";
	public static String OUT_DIR = WORK_DIR+"out/";
	
	private static ConvolutionProcessor cproc = new ConvolutionProcessor();
	
	private static File GUMBY = new File(WORK_DIR+"gumby.png");
	private static File LENNA = new File(WORK_DIR+"lenna.png");
	private static File PANDA = new File(WORK_DIR+"panda.png");
	private static File FUZZY_EDGE = new File(WORK_DIR+"fuzzy_edge.png");
	
	public static void main(String args[]) {
		laplaceOfGauss(GUMBY, "gumby");
		
		laplaceOfGauss(LENNA, "lenna");
		
		laplaceOfGauss(PANDA, "panda");
		
		laplaceOfGauss(FUZZY_EDGE, "fuzzy_edge");
		
		//gauss(FUZZY_EDGE, "fuzzy_edge");
	}
	
	private static void laplace(File starter, final String name) {
		File out1 = new File(OUT_DIR+name+"-laplace-1.png");
		File out2 = new File(OUT_DIR+name+"-laplace-2.png");
		
		double[][] laplace1 = ConvolveLib.KERNEL_LAB9_LAPLACE_1;
		double[][] laplace2 = ConvolveLib.KERNEL_LAB9_LAPLACE_2;
		
		cproc.readWorkingImage(starter);
		cproc.convolve(laplace1);
		cproc.writeWorkingImage(out1);
		
		cproc.readWorkingImage(starter);
		cproc.convolve(laplace2);
		cproc.writeWorkingImage(out2);
	}
	
	private static void  gauss(File starter, final String name) {
		int width = 15;
		double numSigma = 3.0;
		double sigma = ((double)width)/(2*numSigma);
		double[][] gauss = ConvolveLib.getGaussKernel(width, numSigma);
		
		int side = 3;
		int thickness = 1;
		Boolean[][] filter = ConvolveLib.getCrossFilter(side, thickness);
		
		double[][] laplace1 = ConvolveLib.KERNEL_LAB9_LAPLACE_1;
		
		File out = new File(OUT_DIR+name+"-gauss-s="+sigma+"-cross-filter-lap1.png");
		
		cproc.readWorkingImage(starter);
		cproc.convolve(gauss);
		cproc.filterImage(filter);
		cproc.convolve(laplace1);
		cproc.writeWorkingImage(out);
	}
	
	private static void laplaceOfGauss(File starter, final String name) {
		int width = 11;
		double numSigma = 4.0;
		double sigma = ((double)width)/(2*numSigma);
		
		File out1 = new File(OUT_DIR+name+"-gauss-s="+sigma+"-laplace1.png");
		File out2 = new File(OUT_DIR+name+"-gauss-s="+sigma+"-laplace2.png");
		File out3 = new File(OUT_DIR+name+"-laplace-of-gauss-5x5-given.png");
		
		double[][] gauss = ConvolveLib.getGaussKernel(width, numSigma);
		double[][] laplace1 = ConvolveLib.KERNEL_LAB9_LAPLACE_1;
		double[][] laplace2 = ConvolveLib.KERNEL_LAB9_LAPLACE_2;
		double[][] lapOfGauss = ConvolveLib.KERNEL_LAB9_LAPLACE_OF_GAUSS_5;
		
		cproc.readWorkingImage(starter);
		cproc.convolve(gauss);
		cproc.convolve(laplace1);
		cproc.writeWorkingImage(out1);
		
		cproc.readWorkingImage(starter);
		cproc.convolve(gauss);
		cproc.convolve(laplace2);
		cproc.writeWorkingImage(out2);
		
		/*
		cproc.readWorkingImage(starter);
		cproc.convolve(lapOfGauss);
		cproc.writeWorkingImage(out1); */
	}
}
