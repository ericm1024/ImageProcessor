package iproc;

import iproc.RawPixel.ColorField;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.HashMap;

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
	
	public void convolve(double[][] kernel) {
		BufferedImage result = blankCopy();

		Iterator<Pixel> itter = iterator();
		Pixel localPixel;
		
		while (itter.hasNext()) {
			localPixel = itter.next();
			convolveOnce(kernel, new Pixel(
					result, localPixel.getX(), localPixel.getY()));
		}
		workingImage_ = result;
	}
	
	public void filterImage(Boolean[][] filter) {
		BufferedImage result = blankCopy();

		Iterator<Pixel> itter = iterator();
		Pixel localPixel;
		
		while (itter.hasNext()) {
			localPixel = itter.next();
			medianFilterOnce(filter, new Pixel(
					result, localPixel.getX(), localPixel.getY()));
		}
		workingImage_ = result;
	}
	
	/**
	 * @param kernel : the kernel to convolve with
	 * @param source : the data to convolve
	 * @return returns an array with the same dimensions as source containing 
	 *         the data in source convolved with the specified kernel
	 */
	public static double[][] convolveArrayPrimative(final double[][] kernel,
			final double[][] source) {
		double[][] result = new double[source.length][source[0].length];
		
		for (int x = 0; x < result.length; x++) {
			for (int y = 0; y < result[0].length; y++) {
				ConvolutionProcessor.convolveOncePrimative(x, y, kernel, 
						source, result);
			}
		}
		
		return result;
	}
	
	public void gradient(double[][] xKernel, double[][] yKernel) {
		
	}
		
	/* convolution helpers */
		
	private void convolveOnce(double[][] kernel, Pixel outputPixel) {
		double weight = 0.0;
		double redSum = 0.0;
		double greenSum = 0.0;
		double blueSum = 0.0;
		
		Pixel workingPixel = new Pixel(workingImage_, outputPixel.getX(),
											outputPixel.getY());
		
		for (int kernelX = 0; kernelX < kernel.length; kernelX++) {
			int xIndex = (outputPixel.getX() - kernel.length/2) + kernelX;
			
			for (int kernelY = 0; kernelY < kernel[0].length; kernelY++) {
				int yIndex = (outputPixel.getY() - kernel[0].length/2) + kernelY;
				
				if ( !workingPixel.inImage(xIndex, yIndex)) {
					continue;
				}
				workingPixel.moveTo(xIndex, yIndex);
				weight = kernel[kernelX][kernelY];
				
				RawPixel pixel = workingPixel.get();
				
				redSum += weight * pixel.getColorDouble(ColorField.RED);
				greenSum += weight * pixel.getColorDouble(ColorField.GREEN);
				blueSum += weight * pixel.getColorDouble(ColorField.BLUE);
			}
		}
		RawPixel output = new RawPixel(RawPixel.Mode.DOUBLE);
		
		output.setColor(ColorField.RED, redSum);
		output.setColor(ColorField.GREEN, greenSum);
		output.setColor(ColorField.BLUE, blueSum);
		
		outputPixel.set(output);
	}
	
	private void medianFilterOnce(Boolean[][] filter, Pixel outputPixel) {
		Pixel workingPixel = new Pixel(workingImage_, outputPixel.getX(),
										outputPixel.getY());
		
		ArrayList<RawPixel> pixels = new ArrayList<RawPixel>();
		
		for (int filterX = 0; filterX < filter.length; filterX++) {
			int xIndex = (outputPixel.getX() - filter.length/2) + filterX;
			
			for (int filterY = 0; filterY < filter[0].length; filterY++) {
				int yIndex = (outputPixel.getY() - filter[0].length/2) + filterY;
				
				if (workingPixel.inImage(xIndex, yIndex)
					&& filter[filterX][filterY]) {
					workingPixel.moveTo(xIndex, yIndex);
					pixels.add(workingPixel.get());
				}
			}
		}

		outputPixel.set(getMedianPixel(pixels));
	}
	
	private RawPixel getMedianPixel(ArrayList<RawPixel> pixels) {
		RawPixel median = new RawPixel();
		HashMap<ColorField,int[]> colors = new HashMap<ColorField, int[]>();
		
		Iterator<ColorField> colorItter = RawPixel.iterator(false);
		while (colorItter.hasNext()) {
			ColorField localColor = colorItter.next();
			int[] localCounts =  new int[pixels.size()];
			
			for (int i = 0; i < pixels.size(); i++) {
				localCounts[i] = pixels.get(i).getColorInt(localColor);
			}
			colors.put(localColor, localCounts);
		}
		
		// reset the iterator -- we're looping over the colors again
		colorItter = RawPixel.iterator(false);
		while (colorItter.hasNext()) {
			ColorField localColor = colorItter.next();
			int[] localCounts = colors.get(localColor);
			Arrays.sort(localCounts);
			
			// get the middle value
			median.setColor(localColor, localCounts[localCounts.length/2]);
		}
		
		return median;
	}
	
	/**
	 * @param x : the x coordinate to center the kernel at
	 * @param y : the y coordinate to center the kernel at
	 * @param kernel : kernel to convolve with
	 * @param source : data to convolve from
	 * @param target : output gets written to target[x][y] 
	 */
	private static void convolveOncePrimative(final int x, final int y,
			final double[][] kernel, final double[][] source, 
			double[][] target) {
		double sum = 0.0;
				
		for (int kernelX = 0; kernelX < kernel.length; kernelX++) {
			int xIndex = (x - kernel.length/2) + kernelX;
			for (int kernelY = 0; kernelY < kernel[0].length; kernelY++) {
				int yIndex = (y - kernel[0].length/2) + kernelY;
				
				// ensure the indices are in bounds
				if ( 0 <= xIndex && xIndex < source.length 
						&& 0 <= yIndex && yIndex < source[0].length) {
					sum += kernel[kernelX][kernelY] * source[xIndex][yIndex];
				}
			}
		}		
		target[x][y] = sum;
	}
}
