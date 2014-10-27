import java.awt.image.BufferedImage;
import java.io.File;


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
		BufferedImage histogram = new BufferedImage(NUM_COLORS, NUM_COLORS, imageType_);
		
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
	

}
