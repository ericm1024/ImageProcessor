import java.awt.image.BufferedImage;
import java.io.File;


public class ColorHistogramProcessor extends ImageProcessor {
	
	/* constructors */
	/**
	 * Basic constructor for an ImageProcessor.
	 */
	public ColorHistogramProcessor() {
		super();
	}

	
	/**
	 * Basic constructor for an ImageProcessor from a file
	 * 
	 * @param filename the name of the file to read from.
	 */
	public ColorHistogramProcessor(File imageFile) {
		super(imageFile);
	}
	
	
	/**
	 * Basic constructor for an ImageProcessor from a file
	 * 
	 * @param filename the name of the file to read from.
	 */
	public ColorHistogramProcessor(BufferedImage image) {
		super(image);
	}
	
	public BufferedImage histogram() {
		double[] histogramR = getHistoOneColor(Pixel.RED);
		double[] histogramG = getHistoOneColor(Pixel.GREEN);
		double[] histogramB = getHistoOneColor(Pixel.BLUE);
		
		double max = getMaxThreeArray(histogramR, histogramG, histogramB);
		
		int[] normalizedR = normalize(histogramR, max);
		int[] normalizedG = normalize(histogramG, max);
		int[] normalizedB = normalize(histogramB, max);
		
		return histogramFromArray(normalizedR, normalizedG, normalizedB);
	}
	
	public BufferedImage cumulativeHistogram() {
		double[] histogramR = getHistoOneColor(Pixel.RED);
		double[] histogramG = getHistoOneColor(Pixel.GREEN);
		double[] histogramB = getHistoOneColor(Pixel.BLUE);
		
		double[] cumulativeR = getCumulative(histogramR);
		double[] cumulativeG = getCumulative(histogramG);
		double[] cumulativeB = getCumulative(histogramB);
		
		double max = getMaxThreeArray(cumulativeR, cumulativeG, cumulativeB);
		
		int[] normalizedR = normalize(cumulativeR, max);
		int[] normalizedG = normalize(cumulativeG, max);
		int[] normalizedB = normalize(cumulativeB, max);
		
		return histogramFromArray(normalizedR, normalizedG, normalizedB);
	}
	
	
	/* private functions */
	
	private double[] getHistoOneColor(int color) {
		double[] histogram = new double[NUM_COLORS];
		int width = workingImage_.getWidth();
		int height = workingImage_.getHeight();
		double pixelWeight = 1.0/(width*height);
		int localShade;
		
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				localShade = Pixel.getColorFromRGB(color, workingImage_.getRGB(x, y));
				histogram[localShade] += pixelWeight;
			}
		}
		return histogram;
	}
	
	
	private double[] getCumulative(double[] histogram) {
		assert(histogram.length == NUM_COLORS);
		double[] cumulative = new double[NUM_COLORS];
		for (int i = 0; i < NUM_COLORS; i++) {
			for (int j = 0; j <= i; j++) {
				cumulative[i] += histogram[j];
			}
		}
		return cumulative;
	}
	
	
	private int[] normalize(double[] histogram, double max) {
		assert(histogram.length == NUM_COLORS);
		int[] normalized = new int[NUM_COLORS];
		double weight = (double)NUM_COLORS/max;
		
		for (int i = 0; i < NUM_COLORS; i++) {
			normalized[i] = (int)(weight*histogram[i]);
		}
		return normalized;
	}
	
	
	private double maxDoubleArray(double[] array) {
		double max = Double.NEGATIVE_INFINITY;
		for (int i = 0; i < array.length; i++) {
			if (array[i] > max) {
				max = array[i];
			}
		}
		return max;
	}
	
	
	private BufferedImage histogramFromArray(int[] redCounts, int[] greenCounts, int[] blueCounts) {
		BufferedImage histogram = new BufferedImage(NUM_COLORS, NUM_COLORS, imageType_);
		int localHistHeight;
		Pixel localPixel;
		int[] localCounts;
		int localColor;
		
		assert(redCounts.length == NUM_COLORS);
		assert(greenCounts.length == NUM_COLORS);
		assert(blueCounts.length == NUM_COLORS);
		
		for (int color = 0; color < 3; color++) {
			if (color == 0) {
				localCounts = redCounts;
				localColor = Pixel.RED;
			} else if (color == 1) {
				localCounts = greenCounts;
				localColor = Pixel.GREEN;
			} else {
				localCounts = blueCounts;
				localColor = Pixel.BLUE;				
			}
			for (int x = 0; x < histogram.getWidth(); x++) {
				localHistHeight = localCounts[x];
				
				for (int y = 0; y < histogram.getHeight(); y++) {
					if ((histogram.getHeight() - localHistHeight) <= y) {
						localPixel = new Pixel(histogram.getRGB(x,y));
						localPixel.setColor(localColor, Pixel.MAX_COLOR);
						histogram.setRGB(x, y, localPixel.getRGB());
					}
				}
			}
		}
		return histogram;
	}
	
	private double getMaxThreeArray(double[] A, double[] B, double[] C) {
		double max;
		max = Math.max(maxDoubleArray(A), Math.max(maxDoubleArray(B), maxDoubleArray(C)) );
		return max;
	}
}
