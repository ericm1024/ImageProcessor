import java.awt.Color;


public class Pixel {
	
	/* public members */
	
	//TODO: change these to enums
	public static final int RED = 0;
	public static final int GREEN = 1;
	public static final int BLUE = 2;
	public static final int ALPHA = 3;
	
	public static int MAX_COLOR = 255;
	
	
	/* private members */
	
	private Color color;
	
	
	/* constructors */
	
	/**
	 * @param rgb a 32bit int rgb value.
	 */
	public Pixel(int rgb) {
		color = new Color(rgb, true);
	}
	
	/**
	 * Constructs a pixel with all colors set to zero
	 */
	public Pixel() {
		//initialize rgb to 0, ie all black
		color = new Color(0, true);
	}
	
	
	/* public functions */
	
	/* getters */
	
	public int getRed() {
		return color.getRed();
	}
	
	
	public int getGreen() {
		return color.getGreen();
	}
	
	
	public int getBlue() {
		return color.getBlue();
	}
	
	
	public int getAlpha() {
		return color.getAlpha();
	}
	
	
	public int getRGB(){
		return color.getRGB();
	}
	
	
	/**
	 * @param field one of of either RED, GREEN, BLUE, or ALPHA
	 * @return the value of that color component of the current pixel.
	 */
	public int getColor(int field) {
		int fieldValue;
		switch (field) {
			case RED:
				fieldValue = getRed();
				break;
			case GREEN:
				fieldValue = getGreen();
				break;
			case BLUE:
				fieldValue = getBlue();
				break;				
			case ALPHA:
				fieldValue = getAlpha();
				break;
			default:
				System.err.println("Pixel.getColor: got unexpected color.");
				fieldValue = 0;
				break;
		}
		return fieldValue;
	}
	
	
	/**
	 * @param color one of of either RED, GREEN, BLUE, or ALPHA
	 * @param RGB an rgb value (as an int)
	 * @return the specified color component (as an int) from
	 * the given RGB value
	 */
	public static int getColorFromRGB(int color, int rgb){
		Pixel localPixel = new Pixel(rgb);
		return localPixel.getColor(color);
	}

	
	/**
	 * @param color one of of either RED, GREEN, BLUE, or ALPHA
	 * @param RGB an rgb value (as an int)
	 * @return the specified color component (as an int) from
	 * the given RGB value
	 */
	public static int getGreyFromRGB(int rgb){
		Pixel localPixel = new Pixel(rgb);
		localPixel.greyscale();
		
		// since all the colors are the same, it doesn't matter
		// which one we return
		return localPixel.getRed();
	}
	
	
	public static int getRgbFromGrey(int grey) {
		Pixel localPixel = new Pixel();
		localPixel.setRed(grey);
		localPixel.setBlue(grey);
		localPixel.setGreen(grey);
		
		return localPixel.getRGB();
	}
	
	
	/* setters */
	
	public void setRed(int redValue){
		color = new Color(validateColorValue(redValue),
						  getGreen(), getBlue(), getAlpha());
	}
	
	
	public void setGreen(int greenValue){
		color = new Color(getRed(), validateColorValue(greenValue),
						  getBlue(), getAlpha());
	}
	
	
	public void setBlue(int blueValue){
		color = new Color(getRed(), getGreen(),
					validateColorValue(blueValue), getAlpha());
						
	}
	
	
	public void setRGB(int r, int g, int b){
		setRed(r);
		setGreen(g);
		setBlue(b);
	}
	
	
	public void setGrey(int greyValue){
		setRed(greyValue);
		setGreen(greyValue);
		setBlue(greyValue);
	}
	
	
	public void setAlpha(int alphaValue){
		color = new Color(getRed(), getGreen(),
				  		  getBlue(), validateColorValue(alphaValue));
	}
	
	
	public void setColor(int field, int value) {
		switch (field) {
			case RED:
				setRed(value);
				break;
			case GREEN:
				setGreen(value);
				break;
			case BLUE:
				setBlue(value);
				break;				
			case ALPHA:
				setAlpha(value);
				break;
			default:
				System.err.println("Pixel.getColor: got unexpected color.");
				break;
		}
	}
	
	
	public void clear() {
		color = new Color(0, true);
	}
	
	
	public static int negate(int rgb){
		Pixel localPixel = new Pixel(rgb);
		localPixel.negate();
		return localPixel.getRGB();
	}
	
	
	public static int greyscale(int rgb){
		Pixel localPixel = new Pixel(rgb);
		localPixel.greyscale();
		return localPixel.getRGB();
	}
	
	
	public void scale(double scaleFactor){
		setRed((int)(getRed()*scaleFactor));
		setGreen((int)(getGreen()*scaleFactor));
		setBlue((int)(getBlue()*scaleFactor));
	}
	
	public Pixel add(Pixel pixel) {
		Pixel result = new Pixel();
		
		result.setRed(validateColorValue(getRed() + pixel.getRed()));
		result.setGreen(validateColorValue(getGreen() + pixel.getGreen()));
		result.setBlue(validateColorValue(getBlue() + pixel.getBlue()));
		result.setAlpha(validateColorValue(getAlpha() + pixel.getAlpha()));
		
		return result;
	}
	
	/* private functions */
	
	/**
	 * This function is used for validating inputs.
	 * 
	 * @param color the input color as an integer. 
	 * @return an integer that will always be between 0 and 255 inclusive
	 */
	private int validateColorValue(int color){
		if (color < 0){
			return 0;
		} else if (color > MAX_COLOR){
			return MAX_COLOR;
		} else {
			return color;
		}
	}
	
	
	private void negate() {
		color = new Color(MAX_COLOR - getRed(), MAX_COLOR - getGreen(),
						  MAX_COLOR - getBlue(), MAX_COLOR - getAlpha());
	}
	
	
	private void greyscale() {
		int grey = (getGreen()+getBlue()+getRed())/3;
		color = new Color(grey, grey, grey, getAlpha());
	}
}
