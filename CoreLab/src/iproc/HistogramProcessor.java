package iproc;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import static iproc.RawPixel.*;
import static iproc.RawPixel.ColorField.*;


public class HistogramProcessor extends ImageProcessor {
	
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
	 * Returns a normalized 256x256 rgb histogram of the processor's working
	 * image.
	 */
	public BufferedImage histogramRGB() {

		
		generateHistogram(histogram);
		
		return histogram;
	}

	
	/**
	 * Returns a normalized 256x256 greyscale histogram of the processor's
	 * working image.
	 */
	public BufferedImage histogramGrey() {
		
	}
	
	
	/**
	 * Returns a normalized 256x256 cumulative histogram of the processor's
	 * working image.
	 */
	public BufferedImage cumulativeHistogramRGB() {
		
	}

	
	/**
	 * Returns a normalized 256x256 cumulative greyscale histogram of the
	 * processor's working image.
	 */
	public BufferedImage cumulativeHistogramGrey() {
		
	}
	
	
	/* private functions */
	
	private HashMap<ColorField, int[]> getShadeCounts() {
		HashMap<ColorField, int[]> shades = getShadeHashMap();
		RawPixel localRawPixel;
		Iterator<Pixel> pixelItter = iterator();
		
		while (pixelItter.hasNext()) {
			localRawPixel = pixelItter.next().getRawPixel();
			Iterator<ColorField> colorItter = RawPixel.iterator();
			
			while (colorItter.hasNext()) {
				ColorField localColor = colorItter.next();
				shades.get(localColor)[localRawPixel.getColorInt(localColor)]++;
			}
		}
		return shades;
	}
	
	private void normalizeCounts(HashMap<ColorField, int[]> shadeCounts) {
		int max = getMaxShadeCount(shadeCounts);
		Iterator<ColorField> colorItter = RawPixel.iterator();
		
		while (colorItter.hasNext()) {
			int[] counts = shadeCounts.get(colorItter.next());
			
			for (int i = 0; i < counts.length; i++) {
				counts[i] *= (double)NUM_COLORS/(double)max;
			}
		}
	}
	
	private void makeCumulative(HashMap<ColorField, int[]> shadeCounts) {
		Iterator<ColorField> colorItter = RawPixel.iterator();
		
		while (colorItter.hasNext()) {
			int[] counts = shadeCounts.get(colorItter.next());
			
			for (int i = 1; i < counts.length; i++) {
				counts[i] += counts[i-1];
			}
		}
	}
	
	private BufferedImage makeHistogram(HashMap<ColorField, int[]> shadeCounts) {
		BufferedImage histogram = new BufferedImage(NUM_COLORS, NUM_COLORS, imageType_);
		ImageProcessor proc = new ImageProcessor(histogram);	
		Pixel localPixel;
		RawPixel localRawPixel;
		
		Iterator<Pixel> itter = proc.iterator();
		while (itter.hasNext()) {
			localPixel = itter.next();
			localRawPixel = localPixel.getRawPixel();
			
			Iterator<ColorField> colorItter = RawPixel.iterator();
			while (colorItter.hasNext()) {
				ColorField color = colorItter.next();
				if (shadeCounts.get(color)[localPixel.getX()] <= 
						localPixel.getY()) {
					localRawPixel.setColor(color, RawPixel.INT_COLOR_MAX);
					localPixel.setRawPixel(localRawPixel);
				}
			}
			
		}
		
		return histogram;
	}
	
	private HashMap<ColorField, int[]> getShadeHashMap() {
		HashMap<ColorField,int[]> colors = new HashMap<ColorField, int[]>();
		
		colors.put(RED, new int[NUM_COLORS]);
		colors.put(GREEN, new int[NUM_COLORS]);
		colors.put(BLUE, new int[NUM_COLORS]);
		colors.put(ALPHA, new int[NUM_COLORS]);
		
		return colors;
	}
	
	private int getMaxShadeCount(final HashMap<ColorField, int[]> shadeCounts) {
		Iterator<ColorField> colorItter = RawPixel.iterator();
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
}
