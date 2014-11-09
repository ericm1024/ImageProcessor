package labs;
import iproc.ImageProcessor;
import iproc.Pixel;
import iproc.RawPixel;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Iterator;
	
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
		double pixelWeight = 1.0/(workingImage_.getWidth() 
								* workingImage_.getHeight());

		Iterator<Pixel> pixItter = iterator();
		while (pixItter.hasNext()) {
			RawPixel next = pixItter.next().get();
			int localShade = (next.getColorInt(RawPixel.ColorField.RED)
							+ next.getColorInt(RawPixel.ColorField.GREEN)
							+ next.getColorInt(RawPixel.ColorField.BLUE)
							) / 3;
			histogram[localShade] += pixelWeight;
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
		BufferedImage output = blankCopy();
		
		Iterator<Pixel> pixItter = iterator();
		while(pixItter.hasNext()) {
			Pixel pix = pixItter.next();
			RawPixel rawPix = pix.get();
			int localShade = (rawPix.getColorInt(RawPixel.ColorField.RED)
							+ rawPix.getColorInt(RawPixel.ColorField.GREEN)
							+ rawPix.getColorInt(RawPixel.ColorField.BLUE)
							) / 3;
			RawPixel result = new RawPixel();
			result.setColorAll((int)lookupTable[localShade], 
					(int)lookupTable[localShade], (int)lookupTable[localShade],
					RawPixel.INT_COLOR_MAX);
			pix.set(result);
		}
		
		return output;
	}
	
	
	private BufferedImage mapLookup(int[] lookupTable) {
BufferedImage output = blankCopy();
		
		Iterator<Pixel> pixItter = iterator();
		while(pixItter.hasNext()) {
			Pixel pix = pixItter.next();
			RawPixel rawPix = pix.get();
			int localShade = (rawPix.getColorInt(RawPixel.ColorField.RED)
							+ rawPix.getColorInt(RawPixel.ColorField.GREEN)
							+ rawPix.getColorInt(RawPixel.ColorField.BLUE)
							) / 3;
			RawPixel result = new RawPixel();
			result.setColorAll(lookupTable[localShade], 
					lookupTable[localShade], lookupTable[localShade],
					RawPixel.INT_COLOR_MAX);
			pix.set(result);
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
