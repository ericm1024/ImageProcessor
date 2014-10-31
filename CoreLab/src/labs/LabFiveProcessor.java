package labs;
import ImageProcessor;
import iproc.Pixel;

import java.awt.image.BufferedImage;
import java.io.File;
	
public class LabFiveProcessor extends ImageProcessor{

	
	/* constructors */
	/**
	 * Basic constructor for an ImageProcessor.
	 */
	public LabFiveProcessor() {
		super();
	}

	
	/**
	 * Basic constructor for an ImageProcessor from a file
	 * 
	 * @param filename the name of the file to read from.
	 */
	public LabFiveProcessor(File imageFile) {
		super(imageFile);
	}
	
	
	/**
	 * Basic constructor for an ImageProcessor from a file
	 * 
	 * @param filename the name of the file to read from.
	 */
	public LabFiveProcessor(BufferedImage image) {
		super(image);
	}
	
	
	public BufferedImage equalize() {
		double[] lookupTable = getEqualizeArray();
		return mapLookup(lookupTable);
	}
	
	
	public BufferedImage histogramSpec(double[] histogram) {
		assert(histogram.length == NUM_COLORS);
		
		double[] outputLT = getLookupTable(histogram);
		double[] inputLT = getEqualizeArray();
		int[] totalLT = getMapping(inputLT, outputLT); 
		return mapLookup(totalLT);
	}
	
	
	public static void normalize(double[] array) {
		double volume = 0;
		double normalFactor;
		for (int i = 0; i < array.length; i++) {
			volume += array[i];
		}
		
		normalFactor = 1.0/volume;
		
		for (int i = 0; i < array.length; i++) {
			array[i] *= normalFactor;
		}
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
	
	
	private BufferedImage mapLookup(int[] lookupTable) {
		int width = workingImage_.getWidth();
		int height = workingImage_.getHeight();
		BufferedImage output = new BufferedImage(width, height, imageType_);
		int localGrey;
		int localRGB;
		
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				localGrey = Pixel.getGreyFromRGB(workingImage_.getRGB(x, y));
				localRGB = Pixel.getRgbFromGrey(lookupTable[localGrey]);
				output.setRGB(x, y, localRGB);
			}
		}	
		return output;
	}
	
	
	private double[] getEqualizeArray() {
		double[] histogram = getHistogram();
		double[] lookupTable = getLookupTable(histogram);
		return lookupTable;
	}
	
	
	private int[] getMapping(double[] inputLT, double[] outputLT) {
		assert(inputLT.length == outputLT.length);
		int[] maping = new int[inputLT.length];
		
		for (int i = 0; i < maping.length; i++) {
			maping[i] = closestIndex(outputLT, inputLT[i]);
		}

		return maping;
	}
	
	
	private int closestIndex(double[] array, double value) {
		double closest = array[0];
		int closestIndex = 0;
		for (int i = 0; i < array.length; i++) {
			if (Math.abs(array[i] - value) < Math.abs(array[i] - closest)) {
				closest = array[i];
				closestIndex = i;
			}
		}
		return closestIndex;
	}
}
