package iproc;

import java.awt.image.BufferedImage;

public class Pixel {

	/* public data members */
	
	/* private data members */
	private BufferedImage parent_;
	private int x_;
	private int y_;
	private RawPixel pixel_;
	
	/* public methods */
	
	/**
	 * This class 'works' on an image. It abstracts away the process
	 * of iterating over an image with hasNext() and next().  
	 * 
	 * Constructor. Takes a parent image and starting coordinates.
	 * @param image
	 */
	public Pixel(BufferedImage image, int x, int y) {
		parent_ = image;
		assert(inImage(x,y));
		x_ = x;
		y_ = y;
		pixel_ = new RawPixel();
		pixel_.setColorRgb(parent_.getRGB(x_, y_));
	}
	
	/**
	 * This class 'works' on an image. It abstracts away the process
	 * of iterating over an image with hasNext() and next().  
	 * 
	 * Constructor. Takes a parent image.
	 * @param image
	 */
	public Pixel(BufferedImage image) {
		this(image, 0, 0);
	}

	public int getX() {
		return x_;
	}
	
	public int getY() {
		return y_;
	}
	
	public void set(RawPixel pixel) {
		pixel_ = pixel;
		updateImage();
	}
	
	public RawPixel get() {
		return pixel_;
	}
	
	public Pixel moveTo(int x, int y) {
		assert(inImage(x,y));
		x_ = x;
		y_ = y;
		pixel_ = new RawPixel();
		pixel_.setColorRgb(parent_.getRGB(x_, y_));
		return this;
	}
	
	public Boolean inImage(int x, int y) {
		return (x >= 0 && x < parent_.getWidth() &&
				y >= 0 && y < parent_.getHeight());
	}
	
	/* private methods */
	
	/**
	 * Updates the image with the color of the RawPixel
	 */
	private void updateImage() {
		parent_.setRGB(x_, y_, pixel_.getColorRgb());
	}
}
