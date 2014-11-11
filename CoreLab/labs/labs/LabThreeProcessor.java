package labs;

import iproc.ImageProcessor;
import iproc.Pixel;
import iproc.RawPixel;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Iterator;


public class LabThreeProcessor extends ImageProcessor {
	
	/* constructors */
	/**
	 * Basic constructor for an ImageProcessor.
	 */
	public LabThreeProcessor() {
		super();
	}

	
	/**
	 * Basic constructor for an ImageProcessor from a file
	 * 
	 * @param filename the name of the file to read from.
	 */
	public LabThreeProcessor(File imageFile) {
		super(imageFile);
	}
	
	
	/**
	 * Basic constructor for an ImageProcessor from a file
	 * 
	 * @param filename the name of the file to read from.
	 */
	public LabThreeProcessor(BufferedImage image) {
		super(image);
	}
	
	
	/* public functions */
	
	
	public BufferedImage greyHisto() {
		int[] colorCounts = countColorsGreyscale();
		double pixelWeight = ((double)NUM_COLORS)/arrayMax(colorCounts);
		normalizeColorCounts(colorCounts, pixelWeight);
		
		return histogramFromArray(colorCounts);
	}
	
	
	public BufferedImage stretchedGreyHisto() {
		int[] colorCounts = countColorsGreyscale();
		int[] stretchedColorCounts;
		double pixelWeight = ((double)NUM_COLORS)/arrayMax(colorCounts);
		normalizeColorCounts(colorCounts, pixelWeight);
		
		int minIndex = firstNonZeroIndex(colorCounts);
		int maxIndex = lastNonZeroIndex(colorCounts);
		
		stretchedColorCounts = stretchArray(colorCounts, minIndex, maxIndex);
		
		return histogramFromArray(stretchedColorCounts);
	}
	
	
	public BufferedImage stretchedInterpGreyHisto() {
		int[] colorCounts = countColorsGreyscale();
		int[] stretchedColorCounts;
		double pixelWeight = ((double)NUM_COLORS)/arrayMax(colorCounts);
		normalizeColorCounts(colorCounts, pixelWeight);
		
		int minIndex = firstNonZeroIndex(colorCounts);
		int maxIndex = lastNonZeroIndex(colorCounts);
		
		stretchedColorCounts = interpolatedStretch(colorCounts, minIndex, maxIndex);
		
		return histogramFromArray(stretchedColorCounts);
	}
	

	public BufferedImage cutoffHisto(double cutoffPercent) {
		assert(0 <= cutoffPercent && 1 >= cutoffPercent);
		
		int[] colorCounts = countColorsGreyscale();
		int[] stretchedColorCounts;
		
		int minIndex = minIndexCuttof(colorCounts, cutoffPercent);
		int maxIndex = maxIndexCuttof(colorCounts, cutoffPercent);
		stretchedColorCounts = stretchArray(colorCounts, minIndex, maxIndex);
		
		double pixelWeight = ((double)NUM_COLORS)/arrayMax(stretchedColorCounts);
		normalizeColorCounts(stretchedColorCounts, pixelWeight);
		
		return histogramFromArray(stretchedColorCounts);
	}
	
	
	public BufferedImage stretchMinMax() {
		int[] colorCounts = countColorsGreyscale();
		int minIndex = firstNonZeroIndex(colorCounts);
		int maxIndex = lastNonZeroIndex(colorCounts);
		return stretchImageMinMax(minIndex, maxIndex);
	}
	
	
	public BufferedImage stretchCutoff(double cutoffPercent) {
		assert(0 <= cutoffPercent && 1 >= cutoffPercent);
		
		int[] colorCounts = countColorsGreyscale();
		int minIndex = minIndexCuttof(colorCounts, cutoffPercent);
		int maxIndex = maxIndexCuttof(colorCounts, cutoffPercent);
		return stretchImageMinMax(minIndex, maxIndex);		
	}
	
	
	/**
	 * Makes the working image greyscale.
	 */
	public void makeGreyscale() {	
		Iterator<Pixel> pixIter = iterator();
		while (pixIter.hasNext()) {
			pixIter.next().greyscale();
		}
	}
	
	
	/* private functions */
	
	
	/**
	 * @return an integer array of NUM_COLORS elements where each element is
	 * the number of times that color occurs
	 */
	private int[] countColorsGreyscale() {
		int[] colorCounts = new int[NUM_COLORS];
		
		Iterator<Pixel> pixIter = iterator();
		while (pixIter.hasNext()) {
			int greyValue = pixIter.next().getGrey();
			colorCounts[greyValue]++;
		}
		
		return colorCounts;
	}
	
	
	/**
	 * Maps the number of occurances of each color to the range 0, NUM_COLORS
	 */
	private void normalizeColorCounts(int[] colorCounts, double weight) {
		
		for (int i=0; i < colorCounts.length; i++) {
			colorCounts[i] = (int)((double)colorCounts[i]*weight);
		}
	}
	
	/**
	 * @param colorCounts
	 * @return
	 */
	private BufferedImage histogramFromArray(int[] colorCounts) {
		BufferedImage histogram = new BufferedImage(NUM_COLORS, NUM_COLORS, imageType_);
		int localHistHeight;
		Pixel localPixel;
		
		assert(colorCounts.length == NUM_COLORS);
		
		for (int x = 0; x < histogram.getWidth(); x++) {
			localHistHeight = colorCounts[x];
			
			for (int y = 0; y < histogram.getHeight(); y++) {
				if ((histogram.getHeight() - localHistHeight) <= y) {
					localPixel = new Pixel(histogram, x, y);
					localPixel.setGrey(RawPixel.INT_COLOR_MAX);;
				}
			}
		}
				
		return histogram;
	}
	
	/**
	 * @param array an array of integers
	 * @return the maximum integer in the array
	 */
	private int arrayMax(int[] array) {
		int max = Integer.MIN_VALUE;
		for (int i=0; i<array.length; i++) {
			if (array[i] > max) {
				max = array[i];
			}
		}
		return max;
	}
	
	/**
	 * @param array
	 * @return the first index into the array at which the value
	 * stored at that index is nonzero 
	 */
	private int firstNonZeroIndex(int[] array) {
		int index = 0;		
		while (array[index] == 0) {
			index++;
		}
		return index;
	}
	
	/**
	 * @param array
	 * @return the last index into the array at which the value
	 * stored at that index is nonzero 
	 */
	private int lastNonZeroIndex(int[] array) {
		int index = array.length - 1;		
		while (array[index] == 0) {
			index--;
		}
		return index;		
	}
	
	/**
	 * Stretches a segment of an array between minIndex and maxIndex over
	 * an array of the same length as the input array.
	 * @param array
	 * @param minIndex
	 * @param maxIndex
	 * @return the stretchedArray
	 */
	private int[] stretchArray(int[] array, int minIndex, int maxIndex) {
		int[] stretchedArray = new int[array.length];
		int outputIndex;
		
		for (int x = minIndex; x < maxIndex; x ++) {
			outputIndex = transform(x, minIndex, maxIndex);
			stretchedArray[outputIndex] = array[x];
		}
		
		return stretchedArray;
	}
	
	
	private int transform(int greyValue, int min, int max) {
		int range = max - min;
		return (int)(NUM_COLORS*(double)(greyValue - min)/range);
	}

	
	/**
	 * Stretches a segment of an array between minIndex and maxIndex over
	 * an array of the same length as the input array. Same as 
	 * stretchArray except this function interpolates so that there aren't
	 * weird gaps.
	 * @param array
	 * @param minIndex
	 * @param maxIndex
	 * @return the stretchedArray
	 */
	private int[] interpolatedStretch(int[] array, int minIndex, int maxIndex) {
		int[] stretchedArray = stretchArray(array, minIndex, maxIndex);
		
		for (int i = 1; i < stretchedArray.length-1; i ++) {
			if(stretchedArray[i] == 0) {
				stretchedArray[i] = (stretchedArray[i-1] + stretchedArray[i+1])/2;
			}
		}
		
		return stretchedArray;
	}
	

	/**
	 * @param colorCounts: int[] where each element is the number of
	 * occurrences of the color value of its index
	 * @param cutoffPercent: double between 0 and 1 inclusive. The amount of
	 * pixels to cut off
	 * @return the lower index into colorCounts at which cutoffPercent of
	 * the points are below that index
	 */
	private int minIndexCuttof(int[] colorCounts, double cutoffPercent) {
		assert(0 <= cutoffPercent && 1 >= cutoffPercent);
		
		int totalPixels = workingImage_.getWidth() * workingImage_.getHeight();
		int pixelsToCuttof = (int)((double)totalPixels*cutoffPercent);
		int pixelsSeen = 0;
		int index = 0;
		
		while (pixelsSeen < pixelsToCuttof) {
			pixelsSeen += colorCounts[index];
			index++;
		}
		
		return index;
	}
	
	/**
	 * @param colorCounts: int[] where each element is the number of
	 * occurrences of the color value of its index
	 * @param cutoffPercent: double between 0 and 1 inclusive. The amount of
	 * pixels to cut off
	 * @return the upper index into colorCounts at which cutoffPercent of
	 * the points are above that index
	 */
	private int maxIndexCuttof(int[] colorCounts, double cutoffPercent) {
		assert(0 <= cutoffPercent && 1 >= cutoffPercent);
		
		int totalPixels = workingImage_.getWidth() * workingImage_.getHeight();
		int pixelsToCuttof = (int)((double)totalPixels*cutoffPercent);
		int pixelsSeen = 0;
		int index = colorCounts.length - 1;
		
		while (pixelsSeen < pixelsToCuttof) {
			pixelsSeen += colorCounts[index];
			index--;
		}
		
		return index;
	}
	
	private BufferedImage stretchImageMinMax(int min, int max) {
		BufferedImage stretched = blankCopy();
		ImageProcessor proc = new ImageProcessor(stretched);

		Iterator<Pixel> iter = proc.iterator();		
		while (iter.hasNext()) {
			Pixel localPixel = iter.next();
			localPixel.setGrey(transform(localPixel.getRed(), min, max));
		}
		return stretched;
	}
}
