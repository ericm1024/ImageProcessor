package iproc;

import java.awt.image.BufferedImage;
import java.io.File;

public class ConvolutionProcessor extends ImageProcessor {
	
	
	/* constructors */
	
	/**
	 * Basic constructor for an ImageProcessor.
	 */
	public ConvolutionProcessor() {
		super();
	}

	/**
	 * Basic constructor for an ImageProcessor from a file
	 * 
	 * @param filename the name of the file to read from.
	 */
	public ConvolutionProcessor(File imageFile) {
		super(imageFile);
	}
		
	/**
	 * Basic constructor for an ImageProcessor from a file
	 * 
	 * @param filename the name of the file to read from.
	 */
	public ConvolutionProcessor(BufferedImage image) {
		super(image);
	}
	
	
	/* public methods */
	
	public void convolveImage(double[][] kernel) {
		BufferedImage result = blankCopy();
		Pixel outputPixel = new Pixel(result);
		Pixel workingPixel = new Pixel(result);
		
		while (workingPxm_.hasNext()) {
			convolveOnce(kernel, outputPxm);
			workingPxm_.next();
		}
		workingImage_ = result;
	}
	
	/* low pass filtering kernels  */
	
	public static double[][] getSquareKernel(int width) {
		assert (width%2 == 1); // odd
		double[][] kernel = new double[width][width];

		for (int i = 0; i < kernel.length; i++) {
			for (int j = 0; j < kernel[0].length; j++) {
				kernel[i][j] = 1.0;
			}
		}
		return ConvolveLib.normalizeKernel(kernel);
	}
	
	public static double[][] getW5Kernel() {
		// from assignment
		double[][] kernel = new double[][]{{1, 2, 1}, {2, 4, 2}, {1, 2, 1}};
		
		return normalizeKernel(kernel);
	}
	
	public static double[][] getGaussKernel(int width, double numSigma) {
		assert (width%2 == 1); // odd 
 		double[][] kernel = new double[width][width];
 		double sigma = (double)width/(2*numSigma);
 		int offset = width/2;
		
		for (int x = 0; x < kernel.length; x++) {
			for (int y = 0; y < kernel[0].length; y++) {
				kernel[x][y] = gauss2D(x - offset, y - offset, sigma);
			}
		}
		return normalizeKernel(kernel);
	}
	
	
	/* median filters */
	
	public static Boolean[][] getHFilter(int width) {
		assert (width%2 == 1); // odd
		Boolean[][] filter = new Boolean[width][1];
		for (int i = 0; i < width; i++){
			filter[i][0] = true;
		}
		return filter;
	}
	
	public static Boolean[][] getVFilter(int height) {
		assert (height%2 == 1); // odd
		Boolean[][] filter = new Boolean[1][height];
		for (int i = 0; i < height; i++) {
			filter[0][i] = true;
		}
		return filter;
	}
	
	public static Boolean[][] getSquareFilter(int sideLength) {
		assert (sideLength%2 == 1); // odd
		Boolean[][] filter = new Boolean[sideLength][sideLength];
		for (int i = 0; i < sideLength; i++) {
			for (int j = 0; j < sideLength; j++) {
				filter[i][j] = true;
			}
		}
		return filter;
	}
	
	public static Boolean[][] getCrossFilter(int sideLength, int thickness) {
		assert (sideLength%2 == 1); // odd
		assert (thickness%2 == 1); // odd
		Boolean[][] filter = new Boolean[sideLength][sideLength];
		
		// set all to false
		for (int i = 0; i < filter.length; i++) {
			for (int j = 0; j < filter[0].length; j++) {
				filter[i][j] = false;
			}
		}
		
		// set the middle row
		for (int i = 0; i < filter.length; i++) {
			filter[i][filter[0].length/2] = true;
		}
		
		// set the middle column
		for (int i = 0; i < filter[0].length; i++) {
			filter[filter.length/2][i] = true;
		}
		
		return filter;
	}
	
	/* private methods */
	
	/* generic helpers for creating kernels */
	
	private static double[][] normalizeKernel(double[][] kernel) {
		double areaUnderCurve = ConvolveLib.integrate(kernel);
		
		for (int i = 0; i < kernel.length; i++) {
			for (int j = 0; j < kernel[0].length; j++) {
				kernel[i][j] /= areaUnderCurve;
			}
		}
		
		return kernel;
	}
	
	private static double integrate(double[][] kernel) {
		double result = 0;
		
		for (int i = 0; i < kernel.length; i++) {
			for (int j = 0; j < kernel[0].length; j++) {
				result += kernel[i][j];
			}
		}
		
		return result;
	}
	
	
	/* convolution helpers */
	private void convolveOnce(double[][] kernel, Pixel outputPxm) {
		int xNot = workingPxm_.getX();
		int yNot = workingPxm_.getY();
		int halfXLength = kernel.length/2;
		int halfYLength = kernel[0].length/2;
		int xIndex = 0;
		int yIndex = 0;
		
		RawPixel pixel = new RawPixel(AbstractPixel.Mode.DOUBLE);
		double weight = 0;
		double areaUnderCurve = 0;
		
		for (int kernelX = 0; kernelX < kernel.length; kernelX++) {
			xIndex = (xNot - halfXLength) + kernelX;
			
			for (int kernelY = 0; kernelY < kernel[0].length; kernelY++) {
				yIndex = (yNot - halfYLength) + kernelY;
				if ( !workingPxm_.inRange(xIndex, yIndex)) {
					continue;
				}
				workingPxm_.focus(xIndex, yIndex);
				weight = kernel[kernelX][kernelY];
				areaUnderCurve += weight;
				pixel.add(workingPxm_.getRawPixel().multiply(weight));
			}
		}
		
		/* Ideally the area under the curve (of the kernel) would always be 1.0,
		 * but when we are working near the edge of the image this is not the case
		 * as some of the kernel gets 'cut off.' This manifests as color distortion
		 * around the edges of the image. To fix this, we keep track of the
		 * area under the part of the curve that we actually used and normalize
		 * by it at the end. 
		 */ 
		pixel.divide(areaUnderCurve);
		
		outputPxm.focus(xNot, yNot);
		outputPxm.setRawPixel(pixel);
		
		/* return the pxm to where it was before this function call */
		workingPxm_.focus(xNot, yNot);
	}
	
	
	/* helper for getGaussKernel */
	
	private static double gauss2D(int x, int y, double sigma) {
		double var = sigma * sigma;             // sigma squared
		double a = 1.0/(sigma*Math.sqrt(2*Math.PI));  // coefficient
		double exp = (double)(x*x)/(2.0 * var) + 
					 (double)(y*y)/(2.0 * var);  // exponent
		
		return a * Math.pow(Math.E, - exp);
	}

}
