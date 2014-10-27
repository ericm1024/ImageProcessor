
public abstract class AbstractPixel {
	
	
	/* public members */
	
	public static final double DOUBLE_COLOR_MIN = 0.0;
	public static final double DOUBLE_COLOR_MAX = 1.0;
	public static final int INT_COLOR_MIN = 0;
	public static final int INT_COLOR_MAX = 255;
	public static enum Mode {DOUBLE, INT};
	public static enum ColorField {RED, GREEN, BLUE, ALPHA};
	
	
	/* public functions */

	public abstract void setColor(ColorField color, double value);

	public abstract void setColor(ColorField color, int value);
	
	public abstract void setColorAll(double red, double green, double blue, double alpha);
	
	public abstract void setColorAll(int red, int green, int blue, int alpha);
	
	public abstract void setColorRGB(int rgb);
	
	public abstract double getColorDouble(ColorField color);
	
	public abstract int getColorInt(ColorField color);
	
	public abstract int getColorRGB();
	
	/* protected functions */
	
	protected static double validate(double color) {
		if (color < DOUBLE_COLOR_MIN) {
			return DOUBLE_COLOR_MIN;
		} else if (color > DOUBLE_COLOR_MAX) {
			return DOUBLE_COLOR_MAX;
		} else {
			return color;
		}
	}
	
	protected static int validate(int color) {
		if (color < INT_COLOR_MIN) {
			return INT_COLOR_MIN;
		} else if (color > INT_COLOR_MAX) {
			return INT_COLOR_MAX;
		} else {
			return color;
		}
	}
}
