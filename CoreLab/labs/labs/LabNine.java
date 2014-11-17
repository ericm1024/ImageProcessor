package labs;

import iproc.ConvolveLib;
import iproc.ImageProcessor;
import iproc.Pixel;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Iterator;

@SuppressWarnings("unused")
public class LabNine {
	public static String WORK_DIR = "/Users/eric/Desktop/mudd_fall2014/lab/9/";
	public static String OUT_DIR = WORK_DIR+"out/";
	
	private static ImageProcessor cproc = new ImageProcessor();
	
	private static File GUMBY = new File(WORK_DIR+"gumby.png");
	private static File LENNA = new File(WORK_DIR+"lenna.png");
	private static File PANDA = new File(WORK_DIR+"panda.png");
	private static File FUZZY_EDGE = new File(WORK_DIR+"fuzzy_edge.png");
	
	public static void main(String args[]) {
		laplaceOfGauss(PANDA, "panda");
	}
	
	private static void laplace(File starter, final String name) {
		File out1 = new File(OUT_DIR+name+"-laplace-1.png");
		File out2 = new File(OUT_DIR+name+"-laplace-2.png");
		
		float[][] laplace1 = ConvolveLib.KERNEL_LAB9_LAPLACE_1;
		float[][] laplace2 = ConvolveLib.KERNEL_LAB9_LAPLACE_2;
		
		cproc.readWorkingImage(starter);
		cproc.convolve(laplace1);
		cproc.writeWorkingImage(out1);
		
		cproc.readWorkingImage(starter);
		cproc.convolve(laplace2);
		cproc.writeWorkingImage(out2);
	}
	
	private static void  gauss(File starter, final String name) {
		int width = 15;
		float numSigma = 3f;
		float sigma = ((float)width)/(2*numSigma);
		float[][] gauss = ConvolveLib.getGaussKernel(width, numSigma);
		
		int side = 3;
		int thickness = 1;
		Boolean[][] filter = ConvolveLib.getCrossFilter(side, thickness);
		
		float[][] laplace1 = ConvolveLib.KERNEL_LAB9_LAPLACE_1;
		
		File out = new File(OUT_DIR+name+"-gauss-s="+sigma+"-cross-filter-lap1.png");
		
		cproc.readWorkingImage(starter);
		cproc.convolve(gauss);
		cproc.medianFilter(filter);
		cproc.convolve(laplace1);
		cproc.writeWorkingImage(out);
	}
	
	private static void laplaceOfGauss(File starter, final String name) {
		int width = 11;
		float numSigma = 4f;
		float sigma = ((float)width)/(2*numSigma);
		
		File out1 = new File(OUT_DIR+name+"-gauss-s="+sigma+"-laplace1.png");
		File out2 = new File(OUT_DIR+name+"-gauss-s="+sigma+"-laplace2.png");
		File out3 = new File(OUT_DIR+name+"-laplace-of-gauss-5x5-given.png");
		
		float[][] gauss = ConvolveLib.getGaussKernel(width, numSigma);
		/* gaussian kernel specs */
		float[][] gauss2 = ConvolveLib.KERNEL_LAB9_GAUSS;
		float[][] laplace1 = ConvolveLib.KERNEL_LAB9_LAPLACE_1;
		float[][] laplace2 = ConvolveLib.KERNEL_LAB9_LAPLACE_2;
		float[][] lapOfGauss = ConvolveLib.KERNEL_LAB9_LAPLACE_OF_GAUSS_5;
		
		cproc.readWorkingImage(starter);
		cproc.convolve(gauss2);
		cproc.convolve(laplace1);
		cproc.writeWorkingImage(out1);
		
		cproc.readWorkingImage(starter);
		cproc.convolve(gauss2);
		cproc.convolve(laplace2);
		cproc.writeWorkingImage(out2);
	}
	
	private static void method1(File starter, final String name) {		
		File outf1 = new File(OUT_DIR+name+"-m1-step1-gauss.png");
		File outf2 = new File(OUT_DIR+name+"-m1-step2-grad.png");
		File outf3 = new File(OUT_DIR+name+"-m1-step3-thresh.png");
		File outf4 = new File(OUT_DIR+name+"-m1-step4-filter.png");
		File outf5 = new File(OUT_DIR+name+"-m1-step5-compass.png");
		
		cproc.readWorkingImage(starter);
		
		/* gaussian kernel specs */
		float[][] gauss = ConvolveLib.KERNEL_LAB9_GAUSS;
		
		/* alternate gaussian */
		int width = 5;
		float sigma = 2.0f;
		float numSigma = (float)width/(2*sigma);
		float[][] gauss2 = ConvolveLib.getGaussKernel(width, numSigma);
		
		/* gradient operators */
		float[][] gradientX = ConvolveLib.GRAD_SCHARR_X;
		float[][] gradientY = ConvolveLib.GRAD_SCHARR_Y;
		
		/* threshold value (should be between 0 and 1) */
		float threshold = 0.41f;
		
		/* step 1: smooth with a Gaussian kernel */
		
		cproc.convolve(gauss2);
		cproc.writeWorkingImage(outf1);
		
		/* step 2: compute the gradient */
		
		ImageProcessor colorProc = new ImageProcessor(cproc.getImage());
		float[][] greyscale = colorProc.getGreyscale();
		
		float[][] xPartial = ImageProcessor
				.convolveArrayPrimative(gradientX, greyscale);
		float[][] yPartial = ImageProcessor
				.convolveArrayPrimative(gradientY, greyscale);
		
		cproc.gradient(gradientX, gradientY);
		cproc.writeWorkingImage(outf2);
		
		/* step 3: threshold by the magnitude of the gradient */
		
		float[][] greyscaleThreshold = 
				new float[greyscale.length][greyscale[0].length];
		
		for (int x = 0; x < greyscale.length; x++) {
			for (int y = 0; y < greyscale[0].length; y++) {
				float grad = magnitude(xPartial[x][y], yPartial[x][y]);
				if (grad < threshold) {
					greyscaleThreshold[x][y] = 0f;
				} else {
					greyscaleThreshold[x][y] = grad;
				}
			}
		}
		
		colorProc.setFromGreyscale(greyscaleThreshold);
		colorProc.writeWorkingImage(outf3);	
		
		/* step 4: median filter the image */
		
		/* trim the mess off of the edges because convolution sucks */
		cproc.setImage(colorProc.getImage());
		cproc.trim(5);
		
		cproc.medianFilter(ConvolveLib.getCrossFilter(7, 3));
		cproc.writeWorkingImage(outf4);
		
		/* step 5: convolve with compass operators */
		
		float[][] NE = ConvolveLib.kernelSum(ConvolveLib.GRAD_COMP_N_3, 
				ConvolveLib.GRAD_COMP_E_3);
		
		cproc.convolve(NE);
		cproc.writeWorkingImage(outf5);
	}
	
	
	/* canny algorithm */
	private static void method2(File starter, final String name) {
		File outf1 = new File(OUT_DIR+name+"-m2-step1-gauss.png");
		File outf2 = new File(OUT_DIR+name+"-m2-step2-grad.png");
		File outf3 = new File(OUT_DIR+name+"-m2-step3-thresh.png");
		File outf4 = new File(OUT_DIR+name+"-m2-step4.png");
		File outf5t1 = new File(OUT_DIR+name+"-m2-step5-t1.png");
		File outf5t2 = new File(OUT_DIR+name+"-m2-step5-t2.png");
		
		cproc.readWorkingImage(starter);
		
		/* alternate gaussian */
		int width = 11;
		float sigma = 2f;
		float numSigma = (float)width/(2*sigma);
		float[][] gauss2 = ConvolveLib.getGaussKernel(width, numSigma);
		
		/* gradient operators */
		float[][] gradientX = ConvolveLib.GRAD_SCHARR_X;
		float[][] gradientY = ConvolveLib.GRAD_SCHARR_Y;
		
		/* threshold value (should be between 0 and 1) */
		float threshold = 0.25f;
		
		/* step 1: smooth with a Gaussian kernel */
		
		cproc.convolve(gauss2);
		cproc.writeWorkingImage(outf1);
		
		/* step 2: compute the gradient */
		
		ImageProcessor colorProc = new ImageProcessor(cproc.getImage());
		float[][] greyscale = colorProc.getGreyscale();
		
		float[][] xPartial = ImageProcessor
				.convolveArrayPrimative(gradientX, greyscale);
		float[][] yPartial = ImageProcessor
				.convolveArrayPrimative(gradientY, greyscale);
		
		cproc.gradient(gradientX, gradientY);
		cproc.writeWorkingImage(outf2);
		
		/* step 3: threshold by the magnitude of the gradient */
		
		float[][] greyscaleThreshold = 
				new float[greyscale.length][greyscale[0].length];
		
		for (int x = 0; x < greyscale.length; x++) {
			for (int y = 0; y < greyscale[0].length; y++) {
				float grad = magnitude(xPartial[x][y], yPartial[x][y]);
				if (grad < threshold) {
					greyscaleThreshold[x][y] = 0.0f;
				} else {
					greyscaleThreshold[x][y] = grad;
				}
			}
		}
		
		colorProc.setFromGreyscale(greyscaleThreshold);
		colorProc.writeWorkingImage(outf3);	
		
		/* step 4: supress non-maximum pixels */
		
		/* trim the mess off of the edges because convolution sucks */
		cproc.setImage(colorProc.getImage());
		cproc.trim(5);		

		colorProc.setImage(cproc.getImage());
		greyscaleThreshold = colorProc.getGreyscale();
		
		float[][] supressedThreshold = new float[greyscaleThreshold.length]
				[greyscaleThreshold[0].length];
		
		for (int x = 0; x < greyscaleThreshold.length; x++) {
			for (int y = 0; y < greyscaleThreshold[0].length; y++) {
				if (greyscaleThreshold[x][y] != 0) {
					float theta = (float)Math
							.atan2(yPartial[x][y], xPartial[x][y]);
					
					float neighborOne =
							getNeighbor(greyscaleThreshold, x, y, theta, 1);
					float neighborTwo = 
							getNeighbor(greyscaleThreshold, x, y, theta, -1);
					
					if (greyscaleThreshold[x][y]
						< Math.max(neighborOne, neighborTwo)) {
						supressedThreshold[x][y] = 0.0f;
					} else {
						supressedThreshold[x][y] = greyscaleThreshold[x][y]; 
					}
				}
			}
		}
		
		colorProc.setFromGreyscale(supressedThreshold);
		colorProc.writeWorkingImage(outf4);
		
		/* step 5: two thresholds */
		
		float[][] t1 = new float[supressedThreshold.length][supressedThreshold[0].length];
		float[][] t2 = new float[supressedThreshold.length][supressedThreshold[0].length];
		
		for (int x = 0; x < greyscale.length; x++) {
			for (int y = 0; y < greyscale[0].length; y++) {
				float grad = magnitude(xPartial[x][y], yPartial[x][y]);
				if (grad < threshold) {
					greyscaleThreshold[x][y] = 0.0f;
				} else {
					greyscaleThreshold[x][y] = grad;
				}
			}
		}
		
		colorProc.setFromGreyscale(greyscaleThreshold);
		colorProc.writeWorkingImage(outf3);	
	}
	
	private static float magnitude(float x, float y) {
		return (float)Math.sqrt(x*x + y*y);
	}
	
	private static float getNeighbor(float[][] image, int x, int y,
			float theta, int distance) {
		int neighborX = x + (int)(Math.cos(theta)*distance); 
		int neighborY = y + (int)(Math.sin(theta)*distance);
		
		try {
			return image[neighborX][neighborY];
		} catch (ArrayIndexOutOfBoundsException e) {
			//System.err.println("labNine.getNeighbor: caught index out of bounds exception");
			return 1.0f;
		}
	}
}
