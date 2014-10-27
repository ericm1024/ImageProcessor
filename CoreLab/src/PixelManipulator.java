import java.awt.image.BufferedImage;


public class PixelManipulator extends AbstractPixel {

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
	 * Constructor. Takes a parent image.
	 * @param image
	 */
	public PixelManipulator(BufferedImage image) {
		parent_ = image;
		reset();
	}
	
	
	/* movement methods */
	
	public void focus(int x, int y) {
		assert(inRange(x,y));
		x_ = x;
		y_ = y;
	}
	
	public int getX() {
		return x_;
	}
	
	public int getY() {
		return y_;
	}
	
	public Boolean inRange(int x, int y) {
		return (x >= 0 && x < parent_.getWidth() && 
				y >= 0 && y < parent_.getHeight());
	}

	/**
	 * @return true if the pxm has not traversed the entire image,
	 * false otherwise
	 */
	public Boolean hasNext() {
		if (x_ == (parent_.getWidth() -1) &&
		    y_ == (parent_.getHeight() -1)) {
			return false;
		}
		return true;
	}
	
	/**
	 * Moves the pxm to the next pixel in the image
	 */
	public void next() {
		if (! hasNext()) {
			return;
		}
		if (x_ + 1 < parent_.getWidth()) {
			x_++;
		} else {
			x_ = 0;
			y_++;
		}
		updatePixel();
	}
	
	public void reset() {
		x_ = 0;
		y_ = 0;
		updatePixel();
	}
	
	
	/* color manipulation methods */

	public void setColor(ColorField color, double value) {
		pixel_.setColor(color, value);
		updateImage();
	}

	public void setColor(ColorField color, int value) {
		pixel_.setColor(color, value);
		updateImage();
	}
	
	public void setColorAll(double red, double green, double blue, double alpha) {
		pixel_.setColorAll(red, green, blue, alpha);
		updateImage();
	}
	
	public void setColorAll(int red, int green, int blue, int alpha) {
		pixel_.setColorAll(red, green, blue, alpha);
		updateImage();
	}
	
	public void setRawPixel(RawPixel pixel) {
		pixel_ = pixel;
		updateImage();
	}
	
	public void setColorRGB(int rgb) {
		pixel_.setColorRGB(rgb);
		updateImage();
	}
	
	public int getColorRGB() {
		return pixel_.getColorRGB();
	}
	
	public double getColorDouble(ColorField color) {
		return pixel_.getColorDouble(color);
	}
	
	public int getColorInt(ColorField color) {
		return pixel_.getColorInt(color);
	}

	public RawPixel getRawPixel() {
		return new RawPixel(pixel_);
	}
	
	
	/* color arithmetic methods */
	
	public RawPixel add(ColorField color, double x){
		pixel_.add(color, x);
		updateImage();
		return getRawPixel();
	}
	
	public RawPixel subtract(ColorField color, double x){
		pixel_.subtract(color, x);
		updateImage();
		return getRawPixel();
	}
	
	public RawPixel multiply(ColorField color, double x){
		pixel_.multiply(color, x);
		updateImage();
		return getRawPixel();
	}
	
	public RawPixel divide(ColorField color, double x){
		pixel_.divide(color, x);
		updateImage();
		return getRawPixel();
	}
	
	public RawPixel add(double x){
		pixel_.add(x);
		updateImage();
		return getRawPixel();
	}
	
	public RawPixel subtract(double x){
		pixel_.subtract(x);
		updateImage();
		return getRawPixel();
	}
	
	public RawPixel multiply(double x){
		pixel_.multiply(x);
		updateImage();
		return getRawPixel();
	}
	
	public RawPixel divide(double x){
		pixel_.divide(x);
		updateImage();
		return getRawPixel();
	}
	
	public RawPixel add(RawPixel rhs) {
		pixel_.add(rhs);
		updateImage();
		return getRawPixel();
	}
	
	public RawPixel subtract(RawPixel rhs) {
		pixel_.subtract(rhs);
		updateImage();
		return getRawPixel();
	}
	
	/* private methods */
	
	private void updatePixel() {
		pixel_.setColorRGB(parent_.getRGB(x_, y_));
	}
	
	/**
	 * Updates the image with the color of the RawPixel
	 */
	private void updateImage() {
		parent_.setRGB(x_, y_, pixel_.getColorRGB());
	}
}
