package labs;

import iproc.ConvolveLib;
import iproc.ImageProcessor;
import iproc.Pixel;
import iproc.RawPixel;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

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
		canny(FUZZY_EDGE, "fuzzy-edge");
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
		File outf1 = new File(OUT_DIR+name+"-m2-step1-gauss.png");
		File outf2 = new File(OUT_DIR+name+"-m2-step2-grad.png");
		File outf3 = new File(OUT_DIR+name+"-m2-step3-thresh.png");
		File outf4 = new File(OUT_DIR+name+"-m2-step4-supress.png");
		File outf5 = new File(OUT_DIR+name+"-m2-step5-hysteresis.png");
		
		proc.readWorkingImage(starter);
		
		/* step 1: smooth with a Gaussian kernel */
		/* gaussian */
		int width = 7;
		float sigma = 2f;
		float[][] gauss = ConvolveLib.KERNEL_LAB9_GAUSS;
		proc.convolve(gauss);
		proc.writeWorkingImage(outf1);
		
		/* step 2: compute the gradient */
		float[][] gradientX = ConvolveLib.GRAD_SCHARR_X;
		float[][] gradientY = ConvolveLib.GRAD_SCHARR_Y;
		proc.gradient(gradientX, gradientY);
		proc.writeWorkingImage(outf2);
		
		/* step 3: threshold by the magnitude of the gradient */
		/* threshold value (should be between 0 and 1) */
		float threshold = 0.1f;
		proc.threshold(threshold);
		proc.writeWorkingImage(outf3);	
		
		/* step 4: supress non-maximum pixels */
		/* trim the mess off of the edges because convolution sucks */
		proc.trim(5);		

		float[][] greyscale = proc.getGreyscale();
		float[][] supressed = proc.getGreyscale();
		
		for (int x = 0; x < greyscale.length; x++) {
			for (int y = 0; y < greyscale[0].length; y++) {
				if (greyscale[x][y] != 0) {
					float theta = proc.gradientAngle(greyscale, x, y, gradientX, gradientY);
					
					float n1 = getNeighbor(greyscale, x, y, theta, 1);
					float n2 = getNeighbor(greyscale, x, y, theta, -1);
					//float n3 = getNeighbor(greyscale, x, y, theta, 2);
					//float n4 = getNeighbor(greyscale, x, y, theta, -2);
					
					if (greyscale[x][y] < Math.max(n1, n2)) {
						supressed[x][y] = 0.0f;
					} else {
						supressed[x][y] = greyscale[x][y]; 
					}
				}
			}
		}
		
		proc.setFromGreyscale(supressed);
		proc.writeWorkingImage(outf4);
		
		/* step 5: hysteresis */
		int lower = 80;
		int upper = 100;
		
		/*
		Iterator<Pixel> iter = proc.iterator();
		while(iter.hasNext()) {
			Pixel pix = iter.next();
			if (pix.getGrey() < lower) {
				pix.setGrey(RawPixel.INT_COLOR_MIN);
			} else if (pix.getGrey() >= upper) {
				pix.setGrey(RawPixel.INT_COLOR_MAX);
			} else {
				Iterator<Pixel> localIter = getNewNeighbors(pix).iterator();
				Boolean found = false;
				while (localIter.hasNext() && !found) {
					if (localIter.next().getGrey() >= upper) {
						pix.setGrey(RawPixel.INT_COLOR_MAX);
						found = true;
					}
				}
				if (!found) {
					pix.setGrey(RawPixel.INT_COLOR_MIN);
				}
			}
		} */
		
		Iterator<Pixel> iter = proc.iterator();
		while(iter.hasNext()) {
			Pixel pix = iter.next();
			if (pix.getGrey() < lower) { // if its below the lower threshold, we trash it
				pix.setGrey(RawPixel.INT_COLOR_MIN);
			} else if (pix.getGrey() >= upper) { // if its above the upper threshold, we keep it
				pix.setGrey(RawPixel.INT_COLOR_MAX);
			} else {
				HashSet<int[]> seen = new HashSet<>();
				// get the pixel's neighbors that are above the minthresh
				
				// if it has no neighboring pixels above the top threshold, continue
				
				// else, while the neighbors list is not empty and we haven't exceded our
				// max path length
				// get the next neighbor
				// store its coordinates in a hash table (if we've seen it before, skip)
				// add its neighbors that are above the minthresh to the neighbors list 
			}
		}
		
		proc.writeWorkingImage(outf5);
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
	
	/**
	 * @param pix
	 * @return
	 */
	private static ArrayList<Pixel> newNeighborsOf(Pixel pix, HashSet<int[]> seen, int thresh) {
		
		ArrayList<Pixel> neighbors = new ArrayList<>();
		int srcX = pix.getX();
		int srcY = pix.getY();
		int window = 3;
		
		for (int x = -(window/2); x < window/2; x++) {
			for (int y = -(window/2); y < window/2; y++) {
				if (!pix.inImage(srcX + x,  srcY + y) || (x == 0 && y == 0)) { 
					continue;
				}
				pix.moveTo(srcX + x, srcY + y);
				if (seen.equals({))
				neighbors.add(new Pixel(pix));
			}
		}
		// move the pixel back where we found it
		pix.moveTo(srcX, srcY);
		return neighbors;
	}
}
