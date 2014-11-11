package iproc;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import iproc.RawPixel.ColorField;


public class HistogramProcessor extends ImageProcessor {
	
	/* private data members */
	
	/**
	 * Whether or not we should include alpha values in our histogram
	 */
	private Boolean includeAlpha_ = false; 
	
	/* constructors */
	
	/**
	 * Basic constructor for an ImageProcessor.
	 */
	public HistogramProcessor() {
		super();
	}

	/**
	 * Basic constructor for an ImageProcessor from a file
	 * 
	 * @param filename the name of the file to read from.
	 */
	public HistogramProcessor(File imageFile) {
		super(imageFile);
	}
		
	/**
	 * Basic constructor for an ImageProcessor from a file
	 * 
	 * @param filename the name of the file to read from.
	 */
	public HistogramProcessor(BufferedImage image) {
		super(image);
	}
		
	/**
	 * Sets the alphaUsage_ private variable
	 */
	public void setAlphaUsage(Boolean b) {
		includeAlpha_ = b;
	}
	
	/**
	 * Sets the alphaUsage_ private variable
	 */
	public Boolean getAlphaUsage() {
		return includeAlpha_;
	}
		
	/**
	 * Returns a normalized 256x256 rgb histogram of the processor's working
	 * image.
	 */
	public BufferedImage histogramRgb() {
		
		HashMap<ColorField, int[]> colorCounts = getShadeCounts();
		normalizeCounts(colorCounts);
		
		return makeHistogram(colorCounts);
	}
	
	/**
	 * Returns a normalized 256x256 greyscale histogram of the processor's
	 * working image.
	 */
	public BufferedImage histogramGrey() {
		makeGreyscale();
		return histogramRgb();
	}
		
	/**
	 * Returns a normalized 256x256 cumulative histogram of the processor's
	 * working image.
	 */
	public BufferedImage cumulativeHistogramRgb() {
		
		HashMap<ColorField, int[]> colorCounts = getShadeCounts();
		makeCumulative(colorCounts);
		normalizeCounts(colorCounts);
		
		return makeHistogram(colorCounts);
	}

	/**
	 * Returns a normalized 256x256 cumulative greyscale histogram of the
	 * processor's working image.
	 */
	public BufferedImage cumulativeHistogramGrey() {	
		makeGreyscale();
		return cumulativeHistogramRgb();
	}
	
	
	/* private functions */
	
	/**
	 * Counts the number of occurrences of each shade of each color. 
	 * @return The hash map : ColorFields -> int[] counting each color
	 */
	private HashMap<ColorField, int[]> getShadeCounts() {
		HashMap<ColorField, int[]> shades = getShadeHashMap();
		RawPixel localRawPixel;
		Iterator<Pixel> pixelItter = iterator();
		
		while (pixelItter.hasNext()) {
			localRawPixel = pixelItter.next().get();
			Iterator<ColorField> colorItter = RawPixel.iterator(includeAlpha_);
			
			while (colorItter.hasNext()) {
				ColorField localColor = colorItter.next();
				shades.get(localColor)[localRawPixel.getColorInt(localColor)]++;
			}
		}
		return shades;
	}
	
	/**
	 * Normalizes each value in shadeCounts to [0,255]
	 * @param shadeCounts the hash map : ColorFields -> int[] counting each color
	 */
	private void normalizeCounts(HashMap<ColorField, int[]> shadeCounts) {
		int max = getMaxShadeCount(shadeCounts);
		Iterator<ColorField> colorItter = RawPixel.iterator(includeAlpha_);
		
		while (colorItter.hasNext()) {
			int[] counts = shadeCounts.get(colorItter.next());
			
			for (int i = 0; i < counts.length; i++) {
				counts[i] *= (double)NUM_COLORS/(double)max;
			}
		}
	}
	
	/**
	 * Makes each array cumulative, so each element is the sum of all the
	 * previous elements.
	 * @param shadeCounts the hash map : ColorFields -> int[] counting
	 * each color.
	 */
	private void makeCumulative(HashMap<ColorField, int[]> shadeCounts) {
		Iterator<ColorField> colorItter = RawPixel.iterator(includeAlpha_);
		
		while (colorItter.hasNext()) {
			int[] counts = shadeCounts.get(colorItter.next());
			
			for (int i = 1; i < counts.length; i++) {
				counts[i] += counts[i-1];
			}
		}
	}
	
	/**
	 * Makes a histogram from shadeCounts 
	 * @param shadeCounts
	 * @return
	 */
	private BufferedImage makeHistogram(HashMap<ColorField, int[]> shadeCounts) {
		BufferedImage histogram = new BufferedImage(NUM_COLORS, NUM_COLORS, imageType_);
		ImageProcessor proc = new ImageProcessor(histogram);	
		Pixel localPixel;
		RawPixel localRawPixel;
		
		Iterator<Pixel> itter = proc.iterator();
		while (itter.hasNext()) {
			localPixel = itter.next();
			localRawPixel = localPixel.get();
			
			Iterator<ColorField> colorItter = RawPixel.iterator(includeAlpha_);
			while (colorItter.hasNext()) {
				ColorField color = colorItter.next();
				
				// we want to color the histogram up to the value of the
				// shade in the map, so  if the histogram color value is >=
				// to our current y value, we set the color
				if (shadeCounts.get(color)[localPixel.getX()] >=  
						histogram.getHeight() - localPixel.getY()) {
					localRawPixel.setColor(color, RawPixel.INT_COLOR_MAX);
					localPixel.set(localRawPixel);
				}
			}
			
		}
		
		return histogram;
	}
	
	/**
	 * Returns an empty hash map with 3 or 4 elements (depending on the
	 * includeAlpha_ flag), each a int array of size NUM_COLORS.
	 * @return Hash map : ColorFields -> int[] 
	 */
	private HashMap<ColorField, int[]> getShadeHashMap() {
		HashMap<ColorField,int[]> colors = new HashMap<ColorField, int[]>();	
		Iterator<ColorField> colorItter = RawPixel.iterator(includeAlpha_);
		
		while (colorItter.hasNext()) {
			colors.put(colorItter.next(), new int[NUM_COLORS]);
		}
		
		return colors;
	}
	
	/**
	 * Returns the max r, g, or b value out of all the pixels in the
	 * workingImage_ 
	 * @return the max shades
	 */
	private int getMaxShadeCount(final HashMap<ColorField, int[]> shadeCounts) {
		Iterator<ColorField> colorItter = RawPixel.iterator(false);
		int max = 0;
		
		while (colorItter.hasNext()) {
			for (int x : shadeCounts.get(colorItter.next())) {
				if (x > max) {
					max = x;
				}
			}
		}
		return max;
	}
	
	/**
	 * Makes the workingImage_ greyscale, i.e. averages the rgb values for
	 * each pixel.
	 */
	private void makeGreyscale() {
		Iterator<Pixel> pixelItter = iterator();
		Pixel localPixel;
		RawPixel localRawPixel;
		
		while (pixelItter.hasNext()) {
			localPixel = pixelItter.next();
			localRawPixel = localPixel.get();
			
			// we don't want to include the alpha value when we transform
			// to greyscale
			Iterator<ColorField> colorItter = RawPixel.iterator(false);
			
			int sum = 0;
			while (colorItter.hasNext()) {
				sum += localRawPixel.getColorInt(colorItter.next());
			}
			// we're averaging three color fields
			sum /= 3; 
			localRawPixel.setColorAll(sum, sum, sum, 
					localRawPixel.getColorInt(ColorField.ALPHA));
			localPixel.set(localRawPixel);
		}
	}
}
