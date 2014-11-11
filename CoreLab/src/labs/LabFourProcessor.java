package labs;
import iproc.ImageProcessor;
import iproc.Pixel;
import iproc.RawPixel;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Iterator;


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
		double pixelWeight = 1.0
				/ (workingImage_.getWidth() * workingImage_.getHeight());
		
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
}
