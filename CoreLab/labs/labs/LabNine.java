package labs;

import iproc.ConvolveLib;
import iproc.ImageProcessor;
import iproc.Pixel;
import iproc.RawPixel;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
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
		//laplaceOfGauss(PANDA, "panda");
		//laplace(PANDA, "panda");
		canny(PANDA, "panda");
	}
	
	private static void laplace(File starter, final String name) {
		File out1 = new File(OUT_DIR+name+"-laplace-1.png");
		File out2 = new File(OUT_DIR+name+"-laplace-2.png");
		
		float[][] laplace = ConvolveLib.KERNEL_LAB9_LAPLACE_1;
		
		float thresh = 0.05f;
		int windowSize = 3;
		
		cproc.readWorkingImage(starter);
		cproc.convolve(laplace);
		cproc.detectZeroCrossing(thresh, windowSize);
		cproc.writeWorkingImage(out1);
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
		
		cproc.readWorkingImage(starter);
		cproc.convolve(gauss);
		cproc.writeWorkingImage(out1);
		cproc.convolve(laplace);
		cproc.writeWorkingImage(out2);
		cproc.detectZeroCrossing(thresh, windowSize);
		cproc.writeWorkingImage(out3);
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
		float[][] gauss2 = ConvolveLib.getGauss(width, sigma);
		
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
	private static void canny(File starter, final String name) {
		File outf1 = new File(OUT_DIR+name+"-m2-step1-gauss.png");
		File outf2 = new File(OUT_DIR+name+"-m2-step2-grad.png");
		File outf3 = new File(OUT_DIR+name+"-m2-step3-thresh.png");
		File outf4 = new File(OUT_DIR+name+"-m2-step4-supress.png");
		File outf5t1 = new File(OUT_DIR+name+"-m2-step5-t1.png");
		File outf5t2 = new File(OUT_DIR+name+"-m2-step5-t2.png");
		
		cproc.readWorkingImage(starter);
		
		/* step 1: smooth with a Gaussian kernel */
		/* gaussian */
		int width = 7;
		float sigma = 2f;
		float[][] gauss = ConvolveLib.getGauss(width, sigma);
		cproc.convolve(gauss);
		cproc.writeWorkingImage(outf1);
		
		/* step 2: compute the gradient */
		float[][] gradientX = ConvolveLib.GRAD_SCHARR_X;
		float[][] gradientY = ConvolveLib.GRAD_SCHARR_Y;
		cproc.gradient(gradientX, gradientY);
		cproc.writeWorkingImage(outf2);
		
		/* step 3: threshold by the magnitude of the gradient */
		/* threshold value (should be between 0 and 1) */
		float threshold = 0.1f;
		cproc.threshold(threshold);
		cproc.writeWorkingImage(outf3);	
		
		/* step 4: supress non-maximum pixels */
		/* trim the mess off of the edges because convolution sucks */
		cproc.trim(5);		

		float[][] greyscale = cproc.getGreyscale();
		float[][] supressed = cproc.getGreyscale();
		
		for (int x = 0; x < greyscale.length; x++) {
			for (int y = 0; y < greyscale[0].length; y++) {
				if (greyscale[x][y] != 0) {
					float theta = cproc.gradientAngle(greyscale, x, y, gradientX, gradientY);
					
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
		
		cproc.setFromGreyscale(supressed);
		cproc.writeWorkingImage(outf4);
		
		/* step 5: two thresholds */
		int lower = 120;
		int upper = 175;
		ImageProcessor t1 = new ImageProcessor(supressed, cproc.getType());
		ImageProcessor t2 = new ImageProcessor(supressed, cproc.getType());
		
		t1.threshold(lower);
		t2.threshold(upper);
		
		t1.writeWorkingImage(outf5t1);
		t2.writeWorkingImage(outf5t2);
		
		/* step 6: connect the edges */
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
			//System.err.println("labNine.getNeighbor: caught index out of bounds exception");
			return 0f;
		}
	}
	
	private static ImageProcessor connect(ImageProcessor upper,
			ImageProcessor lower, int length) {
		
		float[][] gUpper = upper.getGreyscale();
		float[][] gLower = upper.getGreyscale();
		Iterator<Pixel> iter = upper.iterator();
		
		while (iter.hasNext()) {
			Pixel pix = iter.next();
			if (pix.getGrey() == 0) {
				continue;
			}
			
			// if has non-zero neighbor, look for other neighbors that are in lower
			// but not upper
		}
		
		return new ImageProcessor(gUpper, upper.getType());
	}
	
	/**
	 * Finds neighbors of upper in lower (a different image)
	 * @param upper
	 * @param lower
	 * @return
	 */
	private static ArrayList<Pixel> getNewNeighbors(Pixel upper, Pixel lower) {
		assert(upper.getX() == lower.getX() && upper.getY() == lower.getY());
		
		// It makes no sense to call this method on two of the same image
		// and I don't want to think about the edge cases that would
		// bring up, so we're just going to say you can't unless I find
		// a reason to do otherwise
		assert(upper.getParentHash() != lower.getParentHash());
		
		ArrayList<Pixel> neighbors = new ArrayList<>();
		
		for (int x = 0; x < 3; x++) {
			for (int y = 0; y < 3; y++) {
				if ()
			}
		}
		
		return neighbors;
	}
}
