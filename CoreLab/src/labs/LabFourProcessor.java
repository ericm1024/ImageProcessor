package labs;
import iproc.ImageProcessor;
import iproc.Pixel;

import java.awt.image.BufferedImage;
import java.io.File;


public class LabFourProcessor extends ImageProcessor {
	
	/* constructors */
	/**
	 * Basic constructor for an ImageProcessor.
	 */
	public LabFourProcessor() {
		super();
	}

	
	/**
	 * Basic constructor for an ImageProcessor from a file
	 * 
	 * @param filename the name of the file to read from.
	 */
	public LabFourProcessor(File imageFile) {
		super(imageFile);
	}
	
	
	/**
	 * Basic constructor for an ImageProcessor from a file
	 * 
	 * @param filename the name of the file to read from.
	 */
	public LabFourProcessor(BufferedImage image) {
		super(image);
	}
	
	
	public BufferedImage equalize() {
		double[] histogram = getHistogram();
		double[] lookupTable = getLookupTable(histogram);
		return mapLookup(lookupTable);
	}
	

	
	/* private functions */
	
	
	private double[] getHistogram() {
		double[] histogram = new double[NUM_COLORS];
		int width = workingImage_.getWidth();
		int height = workingImage_.getHeight();
		double pixelWeight = 1.0/(width*height);
		int localShade;
		
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				localShade = Pixel.getGreyFromRGB(workingImage_.getRGB(x, y));
				histogram[localShade] += pixelWeight;
			}
		}
		return histogram;
	}
	
	
	private double[] getLookupTable(double[] histogram) {
		assert(histogram.length == NUM_COLORS);
		double[] lookupTable = new double[NUM_COLORS];
		double sum = 0.0;
		
		for(int i = 0; i < NUM_COLORS; i++) {
			sum += histogram[i];
			lookupTable[i] = sum*(NUM_COLORS - 1);
		}

		return lookupTable;
	}
	
	
	private BufferedImage mapLookup(double[] lookupTable) {
		int width = workingImage_.getWidth();
		int height = workingImage_.getHeight();
		BufferedImage output = new BufferedImage(width, height, imageType_);
		int localGrey;
		int localRGB;
		
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				localGrey = Pixel.getGreyFromRGB(workingImage_.getRGB(x, y));
				localRGB = Pixel.getRgbFromGrey((int)lookupTable[localGrey]);
				output.setRGB(x, y, localRGB);
			}
		}
		
		return output;
	}	
}
