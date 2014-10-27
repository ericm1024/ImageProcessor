package iproc;
import java.awt.image.BufferedImage;


public class Pixel {

	/* public data members */
	
	/* private data members */
	private BufferedImage parent_;
	private static int x_;
	private static int y_;
	private RawPixel pixel_;
	
	/* public methods */
	
	/**
	 * This class 'works' on an image. It abstracts away the process
	 * of iterating over an image with hasNext() and next().  
	 * 
	 * Constructor. Takes a parent image.
	 * @param image
	 */
	public Pixel(BufferedImage image, int x, int y) {
		assert (x >= 0 && x < image.getWidth() &&
				y >= 0 && y < image.getHeight());
		parent_ = image;
		pixel_ = new RawPixel();
		pixel_.setColorRGB(parent_.getRGB(x_, y_));
	}

	public int getX() {
		return x_;
	}
	
	public int getY() {
		return y_;
	}
	
	public void setRawPixel(RawPixel pixel) {
		pixel_ = pixel;
		updateImage();
	}
	
	public RawPixel getRawPixel() {
		return pixel_;
	}
	
	
	/* private methods */
	
	/**
	 * Updates the image with the color of the RawPixel
	 */
	private void updateImage() {
		parent_.setRGB(x_, y_, pixel_.getColorRGB());
	}
}
