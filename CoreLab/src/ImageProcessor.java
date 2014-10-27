import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;



public class ImageProcessor {
	
	/* public members */
	public static final String OUTPUT_FILE_TYPE = "png";
	public static final int NUM_COLORS = 256;
	
	
	/* protected members */
	protected static final double TAU = 2*Math.PI;
	protected static final double PI = Math.PI;
	
	
	/* private members */
	protected BufferedImage workingImage_;
	protected PixelManipulator workingPxm_;
	protected int imageType_;

	
	/* constructors */
	/**
	 * Basic constructor for an ImageProcessor from a file
	 * 
	 * @param filename the name of the file to read from.
	 */
	public ImageProcessor() {
		workingImage_ = null;
		imageType_ = -1;
		workingPxm_ = null;
	}
	
	
	/**
	 * Basic constructor for an ImageProcessor from a file
	 * 
	 * @param filename the name of the file to read from.
	 */
	public ImageProcessor(File imageFile) {
		readWorkingImage(imageFile);
		imageType_ = workingImage_.getType();
		workingPxm_ = new PixelManipulator(workingImage_);
	}
	
	/**
	 * Constructor from an image
	 * 
	 * @param filename the name of the file to read from.
	 */
	public ImageProcessor(BufferedImage image) {
		setImage(image);
		workingPxm_ = new PixelManipulator(workingImage_);
	}
	
	
	/* public functions */
	
	/* readers and writers */
	
	/**
	 * Reads from imageSource into workingImage
	 *
	 * @return 0 on success, 1 on failure
	 */	
	public int readWorkingImage(File imageSource){
		try {
			workingImage_ = ImageIO.read(imageSource);
			return 0;
		} catch (IOException e) {
			e.printStackTrace();
			return 1;
		}
	}
	
	
	/**
	 * Writes the workingImage to outputFile.
	 * 
	 * @param outputFilename the file to write to
	 * @return 0 on success, 1 on failure
	 */
	public int writeWorkingImage(File outputFile){
		try {
			ImageIO.write(workingImage_, OUTPUT_FILE_TYPE, outputFile);
			return 0;
		} catch (IOException e) {
			e.printStackTrace();
			return 1;
		}
	}
	
	
	/* static readers and writers*/
	
	/**
	 * 
	 * @param imageSource: the file to read from.
	 * @return the image in the file.
	 */
	public static BufferedImage readImage(File imageSource){
		ImageProcessor proc = new ImageProcessor(imageSource);
		return proc.getImage();
	}
	
	
	/**
	 * Writes an image to a file.
	 */
	public static int writeImage(BufferedImage image, File outputFile) {
		ImageProcessor proc = new ImageProcessor(image);
		return proc.writeWorkingImage(outputFile);
	}
	
	
	/* setters and getters */
	
	/**
	 * @return returns the local workingImage.
	 */
	public BufferedImage getImage() {
		// TODO: should this function return a copy?
		return workingImage_;
	}
	
	
	/**
	 * @return returns the local workingImage.
	 */
	public void setImage(BufferedImage image) {
		workingImage_ = image;
	}
	
	
	/**
	 * @return A BufferedImage with the name dimensions and type as
	 * workingImage_
	 */
	public BufferedImage blankCopy() {
		return new BufferedImage(workingImage_.getWidth(), 
				workingImage_.getHeight(), workingImage_.getType());
	}

	
	/* private functions */
	
	/**
	 * 
	 */
	private Iterator<RawPixel> pixelIterator() {
		
	}
	
	
	/* public sub classes */
	public class Itterator<RawPixel> 
}
