package labs;

import iproc.ImageProcessor;
import iproc.Pixel;
import iproc.lib.ConvolveLib;
import iproc.lib.Point;
import iproc.lib.RawPixel;
import iproc.lib.SpaceTree;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

@SuppressWarnings("unused")
public class LabNine {
	public static String WORK_DIR = "/Users/eric/Desktop/mudd_fall2014/lab/9/";
	public static String OUT_DIR = WORK_DIR+"out/";
	
	private static ImageProcessor proc = new ImageProcessor();
	
	private static File GUMBY = new File(WORK_DIR+"gumby.png");
	private static File LENNA = new File(WORK_DIR+"lenna.png");
	private static File PANDA = new File(WORK_DIR+"panda.png");
	private static File FUZZY_EDGE = new File(WORK_DIR+"fuzzy_edge.png");
	
	public static void main(String args[]) {
		canny(GUMBY, "gumby");
	}
	
	private static void laplace(File starter, final String name) {
		File out1 = new File(OUT_DIR+name+"-laplace-1.png");
		File out2 = new File(OUT_DIR+name+"-laplace-2.png");
		
		float[][] laplace = ConvolveLib.KERNEL_LAB9_LAPLACE_1;
		
		float thresh = 0.05f;
		int windowSize = 3;
		
		proc.readWorkingImage(starter);
		proc.convolve(laplace);
		proc.detectZeroCrossing(thresh, windowSize);
		proc.writeWorkingImage(out1);
	}
	
	private static void laplaceOfGauss(File starter, final String name) {
		int width = 5;
		float sigma = 3f;
		
		File out1 = new File(OUT_DIR+name+"LoG-step1.png");
		File out2 = new File(OUT_DIR+name+"LoG-step2.png");
		File out3 = new File(OUT_DIR+name+"-LoG-step3.png");
		
		float[][] gauss = ConvolveLib.getGauss(width, sigma);
		float[][] laplace = ConvolveLib.KERNEL_LAB9_LAPLACE_3;
		
		int windowSize = 3;
		float thresh = 0.08f;
		
		proc.readWorkingImage(starter);
		proc.convolve(gauss);
		proc.writeWorkingImage(out1);
		proc.convolve(laplace);
		proc.writeWorkingImage(out2);
		proc.detectZeroCrossing(thresh, windowSize);
		proc.writeWorkingImage(out3);
	}
	
	/* canny algorithm */
	private static void canny(File starter, final String name) {	
		proc.readWorkingImage(starter);
		
		/* step 1: smooth with a Gaussian kernel */
		proc.convolve(ConvolveLib.KERNEL_LAB9_GAUSS);
		proc.writeWorkingImage(new File(OUT_DIR+name+"-step-1-gauss-filter.png"));
		
		/* step 2: compute the gradient */
		float[][] gradientX = ConvolveLib.GRAD_SCHARR_X;
		float[][] gradientY = ConvolveLib.GRAD_SCHARR_Y;
		proc.gradient(gradientX, gradientY);
		proc.writeWorkingImage(new File(OUT_DIR+name+"-step-2-gradient.png"));
		
		/* step 3: threshold the magnitude of the gradient */
		/* threshold value (should be between 0 and 1) */
		float threshold = 0.45f;
		proc.threshold(threshold);
		proc.writeWorkingImage(new File(OUT_DIR+name+"-step-3-thresh.png"));
		
		/* step 4: supress non-maximum pixels */
		/* trim the mess off of the edges because convolution sucks */
		/*
		proc.trim(5);
		
		float[][] greyscale = proc.getGreyscale();
		float[][] supressed = proc.getGreyscale();
		
		for (int x = 0; x < greyscale.length; x++) {
			for (int y = 0; y < greyscale[0].length; y++) {
				if (greyscale[x][y] != 0) {
					float theta = proc.gradientAngle(greyscale, x, y, gradientX, gradientY);	
					float n1 = getNeighbor(greyscale, x, y, theta, 1);
					float n2 = getNeighbor(greyscale, x, y, theta, -1);
					
					if (greyscale[x][y] < Math.max(n1, n2)) {
						supressed[x][y] = 0.0f;
					} else {
						supressed[x][y] = greyscale[x][y]; 
					}
				}
			}
		}
		proc.setFromGreyscale(supressed);
		proc.writeWorkingImage(new File(OUT_DIR+name+"-step-4-nm-thresh.png"));
		*/
		
		
		/* step 5: hysteresis */
		int lower = 50;
		int upper = 145;
		int maxpath = 4;
		proc.hysteresisThresh(lower, upper, maxpath);
		proc.writeWorkingImage(new File(OUT_DIR+name+"-canny-edge.png"));
	}
	
	private static float magnitude(float x, float y) {
		return (float)Math.sqrt(x*x + y*y);
	}
	
	private static float getNeighbor(float[][] image, int x, int y,
			float theta, int distance) {
		
		theta %= (2.0*Math.PI);
		int neighborX = x;
		int neighborY = y;
		
		if (- Math.PI/8 <= theta && theta <= Math.PI/8 ) { // theta ~= 0
			neighborX = x + distance;
			neighborY = y;
		} else if (theta <= 3*Math.PI/8) { // theta ~= pi/4
			neighborX = x + distance;
			neighborY = y + distance;
		} else if (theta <= 5*Math.PI/8) { // theta ~= pi/2
			neighborX = x;
			neighborY = y + distance;
		} else if (theta <= 7*Math.PI/8) { // theta ~= 3pi/4
			neighborX = x - distance;
			neighborY = y + distance;
		} else if (theta <= 9*Math.PI/8) { // theta ~= pi
			neighborX = x - distance;
			neighborY = y;
		} else if (theta <= 11*Math.PI/8) { // theta ~= 5pi/4
			neighborX = x - distance;
			neighborY = y - distance;
		} else if (theta <= 13*Math.PI/8) { // theta ~= 3pi/2
			neighborX = x;
			neighborY = y - distance;
		} else if (theta <= 15*Math.PI/8) { // theta ~= 7pi/4
			neighborX = x + distance;
			neighborY = y - distance;
		}
		
		try {
			return image[neighborX][neighborY];
		} catch (ArrayIndexOutOfBoundsException e) {
			return 0f;
		}
	}	
}
