package labs;
import AbstractPixel;
import ImageProcessor;
import RawPixel;

import java.awt.image.BufferedImage;
import java.io.File;

import AbstractPixel.ColorField;


public class LabOneProcessor extends ImageProcessor {
	
	/* used for the HIST_TYPE argument of histogramC */
	public static final int DENSITY = 0;
	public static final int CUMULATIVE = 1;
	

	/* constructors */
	/**
	 * Basic constructor for an ImageProcessor.
	 */
	public LabOneProcessor() {
		super();
	}
	
	/**
	 * Basic constructor for an ImageProcessor from a file
	 * 
	 * @param filename the name of the file to read from.
	 */
	public LabOneProcessor(File imageFile) {
		super(imageFile);
	}
	
	
	/**
	 * Basic constructor for an ImageProcessor from a file
	 * 
	 * @param filename the name of the file to read from.
	 */
	public LabOneProcessor(BufferedImage image) {
		super(image);
	}
	
	
	
	/* lab one public functions */
	
	/**
	 * Negates the workingImage
	 */
	public void negateImage() {	
		workingPxm_.reset();
		RawPixel localPixel;
		while (workingPxm_.hasNext()) {
			localPixel = workingPxm_.getRawPixel();
			negate(localPixel);
			workingPxm_.setRawPixel(localPixel);
			workingPxm_.next();
		}
	}
	
	/**
	 * Negates the workingImage
	 */
	public void makeGreyscale() {	
		workingPxm_.reset();
		RawPixel localPixel;
		while (workingPxm_.hasNext()) {
			localPixel = workingPxm_.getRawPixel();
			greyscale(localPixel);
			workingPxm_.setRawPixel(localPixel);
			workingPxm_.next();
		}
	}

	
	/* lab one private functions */
	
	private void negate(RawPixel pixel) {
		pixel.setColor(AbstractPixel.ColorField.RED, 
				AbstractPixel.INT_COLOR_MAX - pixel.getColorInt(AbstractPixel.ColorField.RED));
		
		pixel.setColor(AbstractPixel.ColorField.GREEN, 
				AbstractPixel.INT_COLOR_MAX - pixel.getColorInt(AbstractPixel.ColorField.GREEN));
		
		pixel.setColor(AbstractPixel.ColorField.BLUE, 
				AbstractPixel.INT_COLOR_MAX - pixel.getColorInt(AbstractPixel.ColorField.BLUE));
		
		pixel.setColor(AbstractPixel.ColorField.ALPHA, 
				AbstractPixel.INT_COLOR_MAX - pixel.getColorInt(AbstractPixel.ColorField.ALPHA));
	}
	
	private void greyscale(RawPixel pixel) {
		int r = pixel.getColorInt(AbstractPixel.ColorField.RED);
		int g = pixel.getColorInt(AbstractPixel.ColorField.GREEN);
		int b = pixel.getColorInt(AbstractPixel.ColorField.BLUE);
		
		int grey = (r+g+b)/3;
		
		pixel.setColor(AbstractPixel.ColorField.RED, grey);
		pixel.setColor(AbstractPixel.ColorField.GREEN, grey);
		pixel.setColor(AbstractPixel.ColorField.BLUE, grey);
		pixel.setColor(AbstractPixel.ColorField.ALPHA, grey);
	}
}
