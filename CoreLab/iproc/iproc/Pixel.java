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
		return new RawPixel(pixel_);
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
	
	public int getRed() {
		return pixel_.getColorInt(RawPixel.ColorField.RED);
	}
	
	public int getGreen() {
		return pixel_.getColorInt(RawPixel.ColorField.GREEN);
	}
	
	public int getBlue() {
		return pixel_.getColorInt(RawPixel.ColorField.BLUE);
	}
	
	public int getGrey() {
		return (pixel_.getColorInt(RawPixel.ColorField.RED)
				+ pixel_.getColorInt(RawPixel.ColorField.GREEN)
				+ pixel_.getColorInt(RawPixel.ColorField.BLUE)
				/3);
	}
	
	public float getRedFloat() {
		return pixel_.getColorFloat(RawPixel.ColorField.RED);
	}
	
	public float getGreenFloat() {
		return pixel_.getColorFloat(RawPixel.ColorField.GREEN);
	}
	
	public float getBlueFloat() {
		return pixel_.getColorFloat(RawPixel.ColorField.BLUE);
	}
	
	public float getGreyFloat() {
		return (pixel_.getColorFloat(RawPixel.ColorField.RED)
				+ pixel_.getColorFloat(RawPixel.ColorField.GREEN)
				+ pixel_.getColorFloat(RawPixel.ColorField.BLUE)
				/3f);
	}
	
	public void greyscale() {
		int grey = getGrey();
		pixel_.setColorAll(grey, grey, grey, 
				pixel_.getColorInt(RawPixel.ColorField.ALPHA));
		updateImage();
	}
	
	public void setRed(int red) {
		pixel_.setColor(RawPixel.ColorField.RED, red);
		updateImage();
	}
	
	public void setGreen(int green) {
		pixel_.setColor(RawPixel.ColorField.RED, green);
		updateImage();
	}
	
	public void setBlue(int blue) {
		pixel_.setColor(RawPixel.ColorField.RED, blue);
		updateImage();
	}
	
	public void setGrey(int grey) {
		pixel_.setColorAll(grey, grey, grey,
			pixel_.getColorInt(RawPixel.ColorField.ALPHA));
		updateImage();
	}
	
	public void setRed(float red) {
		pixel_.setColor(RawPixel.ColorField.RED, red);
		updateImage();
	}
	
	public void setGreen(float green) {
		pixel_.setColor(RawPixel.ColorField.RED, green);
		updateImage();
	}
	
	public void setBlue(float blue) {
		pixel_.setColor(RawPixel.ColorField.RED, blue);
		updateImage();
	}
	
	public void setGrey(float grey) {
		pixel_.setColorAll(grey, grey, grey,
			pixel_.getColorInt(RawPixel.ColorField.ALPHA));
		updateImage();
	}
	
	/* private methods */
	
	/**
	 * Updates the image with the color of the RawPixel
	 */
	private void updateImage() {
		parent_.setRGB(x_, y_, pixel_.getColorRgb());
	}
}
