import java.awt.image.BufferedImage;

import java.io.File;
import java.util.ArrayList;
	
public class LabSevenProcessor extends ImageProcessor{

	
	/* constructors */
	/**
	 * Basic constructor for an ImageProcessor.
	 */
	public LabSevenProcessor() {
		super();
	}

	
	/**
	 * Basic constructor for an ImageProcessor from a file
	 * 
	 * @param filename the name of the file to read from.
	 */
	public LabSevenProcessor(File imageFile) {
		super(imageFile);
	}
	
	
	/**
	 * Basic constructor for an ImageProcessor from a file
	 * 
	 * @param filename the name of the file to read from.
	 */
	public LabSevenProcessor(BufferedImage image) {
		super(image);
	}
	
	
	public void convolveSquare(int size) {
		double[][] kernel = getSquareKernel(size);
		convolveImage(kernel);
	}
	
	
	public void convolveW5() {
		double[][] kernel = getW5Kernel();
		convolveImage(kernel);
	}
	
	
	/**
	 * @param size: the size of the kernel array
	 * @param numSigma: the number of standards of deviation that the
	 * range encompanses
	 */
	public void convolveGaussian(int size, double numSigma) {
		double[][] kernel = getGaussKernel(size, numSigma);
		convolveImage(kernel);
	}
	
	public void filterHorizontal(int width) {
		Boolean[][] filter = getHFilter(width);
		filterImage(filter);
	}
	
	public void filterVertical(int height) {
		Boolean[][] filter = getVFilter(height);
		filterImage(filter);
	}
	
	public void filterSquare(int width) {
		Boolean[][] filter = getSquareFilter(width);
		filterImage(filter);
	}
	
	public void filterCross(int sideLength, int thickness) {
		Boolean[][] filter = getCrossFilter(sideLength, thickness);
		filterImage(filter);
	}
		
	
	/* private helpers */
	
	private double[][] getSquareKernel(int width) {
		assert (width%2 == 1); // odd
		double[][] kernel = new double[width][width];

		for (int i = 0; i < kernel.length; i++) {
			for (int j = 0; j < kernel[0].length; j++) {
				kernel[i][j] = 1.0;
			}
		}
		return normalizeKernel(kernel);
	}
	
	private double[][] getW5Kernel() {
		// from assignment
		double[][] kernel = new double[][]{{1, 2, 1}, {2, 4, 2}, {1, 2, 1}};
		
		return normalizeKernel(kernel);
	}
	
	private double[][] getGaussKernel(int width, double numSigma) {
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
	
	private double gauss2D(int x, int y, double sigma) {
		double var = sigma * sigma;             // sigma squared
		double a = 1.0/(sigma*Math.sqrt(TAU));  // coefficient
		double exp = (double)(x*x)/(2.0 * var) + 
					 (double)(y*y)/(2.0 * var);  // exponent
		
		return a * Math.pow(Math.E, - exp);
	}
	
	private Boolean[][] getHFilter(int width) {
		assert (width%2 == 1); // odd
		Boolean[][] filter = new Boolean[width][1];
		for (int i = 0; i < width; i++){
			filter[i][0] = true;
		}
		return filter;
	}
	
	private Boolean[][] getVFilter(int height) {
		assert (height%2 == 1); // odd
		Boolean[][] filter = new Boolean[1][height];
		for (int i = 0; i < height; i++) {
			filter[0][i] = true;
		}
		return filter;
	}
	
	private Boolean[][] getSquareFilter(int sideLength) {
		assert (sideLength%2 == 1); // odd
		Boolean[][] filter = new Boolean[sideLength][sideLength];
		for (int i = 0; i < sideLength; i++) {
			for (int j = 0; j < sideLength; j++) {
				filter[i][j] = true;
			}
		}
		return filter;
	}
	
	private Boolean[][] getCrossFilter(int sideLength, int thickness) {
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
	
	private double[][] normalizeKernel(double[][] kernel) {
		double areaUnderCurve = integrate(kernel);
		
		for (int i = 0; i < kernel.length; i++) {
			for (int j = 0; j < kernel[0].length; j++) {
				kernel[i][j] /= areaUnderCurve;
			}
		}
		
		return kernel;
	}
	
	private double integrate(double[][] kernel) {
		double result = 0;
		
		for (int i = 0; i < kernel.length; i++) {
			for (int j = 0; j < kernel[0].length; j++) {
				result += kernel[i][j];
			}
		}
		
		return result;
	}
	
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
	
	private void convolveImage(double[][] kernel) {
		workingPxm_.reset();
		BufferedImage result = blankCopy();
		Pixel outputPxm = new Pixel(result);
		
		while (workingPxm_.hasNext()) {
			convolveOnce(kernel, outputPxm);
			workingPxm_.next();
		}
		workingImage_ = result;
	}
	
	private void medianFilterOnce(Boolean[][] filter, Pixel outputPxm) {
		int xNot = workingPxm_.getX();
		int yNot = workingPxm_.getY();
		int halfXLength = filter.length/2;
		int halfYLength = filter[0].length/2;
		int xIndex = 0;
		int yIndex = 0;
		
		ArrayList<RawPixel> pixels = new ArrayList<RawPixel>();
		
		for (int filterX = 0; filterX < filter.length; filterX++) {
			xIndex = (xNot - halfXLength) + filterX;
			
			for (int filterY = 0; filterY < filter[0].length; filterY++) {
				yIndex = (yNot - halfYLength) + filterY;
				if ( !workingPxm_.inRange(xIndex, yIndex)) {
					continue;
				}
				workingPxm_.focus(xIndex, yIndex);
				pixels.add(workingPxm_.getRawPixel());
			}
		}
		
		RawPixel median = getMedianPixel(pixels);
		outputPxm.focus(xNot, yNot);
		outputPxm.setRawPixel(median);
		
		workingPxm_.focus(xNot, yNot);
	}
	
	private void filterImage(Boolean[][] filter) {
		workingPxm_.reset();
		BufferedImage result = blankCopy();
		Pixel outputPxm = new Pixel(result);
		
		while (workingPxm_.hasNext()) {
			medianFilterOnce(filter, outputPxm);
			workingPxm_.next();
		}
		workingImage_ = result;
	}
	
	private RawPixel getMedianPixel(ArrayList<RawPixel> pixels) {
		RawPixel median = new RawPixel();
		// TODO: write me
		return median;
	}
}
