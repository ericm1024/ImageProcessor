package labs;

import java.io.File;
import java.awt.image.BufferedImage;
import java.util.Iterator;

import iproc.ImageProcessor;
import iproc.Pixel;
import iproc.RawPixel;
import iproc.RawPixel.ColorField;

public class LabOne {
	
	public static String WORK_DIR = "/Users/eric/Desktop/mudd_fall2014/lab/1/";
	public static File baseFile = new File(WORK_DIR+"nature.jpg");
	
	private static ImageProcessor proc = new ImageProcessor();
	
	public static void main(String args[]) {
		problemOne();
		problemTwo();
		problemThree();
		problemFour();
		problemFive();
		problemSix();
	}
	
	/**
	 * problem statement: 1. Any color image of your choice
	 */
	public static void problemOne() {
		File outputFile = new File(WORK_DIR+"original.png");
		proc.readWorkingImage(baseFile);
		
		proc.writeWorkingImage(outputFile);
	}
	
	/**
	 * problem statement: 2. The negative version of the image
	 */
	public static void problemTwo() {
		File outputFile = new File(WORK_DIR+"negated.png");
		proc.readWorkingImage(baseFile);
		
		ImageProcessor.writeImage(negate(proc.getImage()), outputFile);	
	}
	
	/**
	 * problem statement: 3. The density histogram image of the original
	 * image
	 */
	public static void problemThree() {
		File outputFile = new File(WORK_DIR+"original-histo.png");
		proc.readWorkingImage(baseFile);
		
		ImageProcessor.writeImage(proc.histogramRgb(), outputFile);
	}
	
	/**
	 * problem statement: 4. The cumulative histogram image of the original
	 * image
	 */
	public static void problemFour() {
		File outputFile = new File(WORK_DIR+"original-cumulative-histo.png");
		proc.readWorkingImage(baseFile);
		
		ImageProcessor.writeImage(proc.cumulativeHistogramRgb(), outputFile);
	}
	
	/**
	 * problem statement: 5. The density histogram image of the negative
	 * image
	 */
	public static void problemFive() {
		File outputFile = new File(WORK_DIR+"negated-histo.png");
		proc.readWorkingImage(baseFile);
		
		proc.setImage(negate(proc.getImage()));
		ImageProcessor.writeImage(proc.histogramRgb(), outputFile);
	}
	
	/**
	 * problem statement: 6. The cumulative histogram image of the negative
	 * image
	 */
	public static void problemSix() {
		File outputFile = new File(WORK_DIR+"negated-cumulative-histo.png");
		proc.readWorkingImage(baseFile);
		
		proc.setImage(negate(proc.getImage()));
		ImageProcessor.writeImage(proc.cumulativeHistogramRgb(), outputFile);
	}
	
	private static BufferedImage negate(BufferedImage img) {
		ImageProcessor localProc = new ImageProcessor(img);
		Iterator<Pixel> pixelItter = localProc.iterator();
		
		// iterate over the pixels in the image and negate the color of
		// each one
		while (pixelItter.hasNext()) {
			Pixel pixel = pixelItter.next();
			RawPixel rawPixel = pixel.get();
			Iterator<ColorField> colorItter = 
					RawPixel.iterator(false); // no alpha
			
			while (colorItter.hasNext()) {
				ColorField color = colorItter.next();
				rawPixel.setColor(color, RawPixel.INT_COLOR_MAX 
						- rawPixel.getColorInt(color));
			}
			pixel.set(rawPixel);
		}
		return img;
	}
}
