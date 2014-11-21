package iproc.lib;

import java.awt.Color;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class RawPixel {
	
	/* public members */
	
	public static final float FLOAT_COLOR_MIN = 0;
	public static final float FLOAT_COLOR_MAX = 1;
	
	public static final int INT_COLOR_MIN = 0;
	public static final int INT_COLOR_MAX = 255;
	
	public static enum Mode {FLOAT, INT};
	public static enum ColorField {RED, GREEN, BLUE, ALPHA};
	
	/* private members */
	
	private Number red_;
	private Number green_;
	private Number blue_;
	private Number alpha_;
	
	private Mode mode_;
	
	/* public functions */
	
	/**
	 * Mode defaults to Mode.INT
	 */
	public RawPixel() {
		mode_ = Mode.INT;
		red_ = (int)INT_COLOR_MIN;
		green_ = (int)INT_COLOR_MIN;
		blue_ = (int)INT_COLOR_MIN;
		alpha_ = (int)INT_COLOR_MIN;
	}	
	
	/**
	 * standard RawPixel constructor
	 * @param mode: the numerical mode to operate under (float or integer).
	 * Under float mode, the colors range from FLOAT_COLOR_MIN to
	 * FLOAT_COLOR_MAX. Similarly with integer mode and corresponding
	 * constants.
	 */
	public RawPixel(Mode mode) {
		mode_ = mode;
		if (mode == Mode.FLOAT) {
			red_ = (float)FLOAT_COLOR_MIN;
			green_ = (float)FLOAT_COLOR_MIN;
			blue_ = (float)FLOAT_COLOR_MIN;
			alpha_ = (float)FLOAT_COLOR_MIN;
		} else if (mode == Mode.INT) {
			red_ = (int)INT_COLOR_MIN;
			green_ = (int)INT_COLOR_MIN;
			blue_ = (int)INT_COLOR_MIN;
			alpha_ = (int)INT_COLOR_MIN;
		}
	}
	
	/**
	 * copy constructor 
	 */
	public RawPixel(RawPixel other) {
		this.mode_ = other.mode_;
		this.setColorRgb(other.getColorRgb());
	}
	
	/**
	 * Set one of the color fields of the pixel with a float
	 */
	public void setColor(ColorField color, float value) {
		Mode oldMode = mode_;
		assertMode(Mode.FLOAT);

		switch (color) {
			case RED:
				red_ = validate(value);
				break;
			case GREEN:
				green_ = validate(value);
				break;
			case BLUE:
				blue_ = validate(value);
				break;
			case ALPHA:
				alpha_ = validate(value);
				break;
			default:
				System.err.println(
						"RawPixel.setColor (float): got unknown color.");
		}
		assertMode(oldMode);
	}
	
	/**
	 * Set one of the color fields of the pixel with an int.
	 */
	public void setColor(ColorField color, int value) {
		Mode oldMode = mode_;
		assertMode(Mode.INT);
		
		switch (color) {
			case RED:
				red_ = validate(value);
				break;
			case GREEN:
				green_ = validate(value);
				break;
			case BLUE:
				blue_ = validate(value);
				break;
			case ALPHA:
				alpha_ = validate(value);
				break;
			default:
				System.err.println(
						"RawPixel.setColor (int): got unknown color.");
		}
		assertMode(oldMode);
	}
	
	public void setColorAll(float red, float green, 
			float blue, float alpha) {
		setColor(ColorField.RED, red);
		setColor(ColorField.GREEN, green);
		setColor(ColorField.BLUE, blue);
		setColor(ColorField.ALPHA, alpha);
	}
	
	public void setColorAll(int red, int green, int blue, int alpha) {
		setColor(ColorField.RED, red);
		setColor(ColorField.GREEN, green);
		setColor(ColorField.BLUE, blue);
		setColor(ColorField.ALPHA, alpha);
	}
	
	/**
	 * Set the color of the RawPixel from an rgb value, i.e. each color is an
	 * octet
	 * @param rgb integer color value
	 */
	public void setColorRgb(int rgb) {
		Color color = new Color(rgb, true);
		setColorAll(color.getRed(), color.getGreen(), 
				color.getBlue(), color.getAlpha());
	}
	
	/**
	 * @return the color of the pixel as an rgb integer, where 
	 *         each color is an octet.
	 */
	public int getColorRgb() {
		Mode oldMode = mode_;
		assertMode(Mode.INT);
		
		Color color = new Color(red_.intValue(), green_.intValue(), 
							blue_.intValue(), alpha_.intValue());
		
		assertMode(oldMode);
		return color.getRGB();
	}
	
	/**
	 * Get one of the color fields as an int.
	 */
	public int getColorInt(ColorField color) {
		Mode oldMode = mode_;
		assertMode(Mode.INT);
		
		int value;
		switch (color) {
			case RED:
				value = red_.intValue();
				break;
			case GREEN:
				value = green_.intValue();
				break;
			case BLUE:
				value = blue_.intValue();
				break;
			case ALPHA:
				value = alpha_.intValue();
				break;
			default:
				System.err.println(
						"RawPixel.getColorInt: got unknown color.");
				value = INT_COLOR_MIN;
		}
		assertMode(oldMode);
		return value;
	}
	
	/**
	 * Get one of the color fields as a float
	 */
	public float getColorFloat(ColorField color) {
		Mode oldMode = mode_;
		assertMode(Mode.FLOAT);
		
		float value;
		switch (color) {
			case RED:
				value = red_.floatValue();
				break;
			case GREEN:
				value = green_.floatValue();
				break;
			case BLUE:
				value = blue_.floatValue();
				break;
			case ALPHA:
				value = alpha_.floatValue();
				break;
			default:
				System.err.println(
						"RawPixel.getColorFloat: got unknown color.");
				value = FLOAT_COLOR_MIN;
		}
		
		assertMode(oldMode);
		return value;
	}
	
	/**
	 * Adds to the value of one of the color fields. 
	 * @param color : color field to add to
	 * @param x : the amount to add
	 * @return this
	 */
	public RawPixel add(ColorField color, float x) {
		Mode oldMode = mode_;
		assertMode(Mode.FLOAT);
		
		switch (color) {
			case RED:
				setColor(ColorField.RED, red_.floatValue() + x);
				break;
			case GREEN:
				setColor(ColorField.GREEN, green_.floatValue() + x);
				break;
			case BLUE:
				setColor(ColorField.BLUE, blue_.floatValue() + x);
				break;
			case ALPHA:
				setColor(ColorField.ALPHA, alpha_.floatValue() + x);
				break;
			default:
				System.err.println("RawPixel.add: got unknown color.");
		}
		
		assertMode(oldMode);
		return this;
	}
	
	/**
	 * Adds to the value of all of the color fields.
	 * @param x : the amount to add
	 * @return this
	 */
	public RawPixel add(float x) {
		Mode oldMode = mode_;
		assertMode(Mode.FLOAT);
		
		setColor(ColorField.RED, red_.floatValue() + x);
		setColor(ColorField.GREEN, green_.floatValue() + x);
		setColor(ColorField.BLUE, blue_.floatValue() + x);
		setColor(ColorField.ALPHA, alpha_.floatValue() + x);

		assertMode(oldMode);
		return this;
	}
	
	/**
	 * Adds to color values from rhs to this.
	 * @param rhs : the RawPixel to add color values from
	 * @return this
	 */
	public RawPixel add(RawPixel rhs) {
		Mode oldMode = rhs.mode_;
		rhs.assertMode(this.mode_);
		
		add(ColorField.RED, rhs.getColorFloat(ColorField.RED));
		add(ColorField.GREEN, rhs.getColorFloat(ColorField.GREEN));
		add(ColorField.BLUE, rhs.getColorFloat(ColorField.BLUE));
		add(ColorField.ALPHA, rhs.getColorFloat(ColorField.ALPHA));
		
		rhs.assertMode(oldMode);
		return this;
	}
	
	/**
	 * Subtracts from the value of one of the color fields. 
	 * @param color : color field to subtract from
	 * @param x : the amount to subtract
	 * @return this
	 */
	public RawPixel subtract(ColorField color, float x) {
		return add(color, -x);
	}
	
	public RawPixel subtract(float x) {
		return add(-x);
	}
	
	public RawPixel subtract(RawPixel rhs) {
		Mode oldMode = rhs.mode_;
		rhs.assertMode(this.mode_);
		
		subtract(ColorField.RED, rhs.getColorFloat(ColorField.RED));
		subtract(ColorField.GREEN, rhs.getColorFloat(ColorField.GREEN));
		subtract(ColorField.BLUE, rhs.getColorFloat(ColorField.BLUE));
		subtract(ColorField.ALPHA, rhs.getColorFloat(ColorField.ALPHA));
		
		rhs.assertMode(oldMode);
		return this;
	}
	
	public RawPixel multiply(ColorField color, float x) {
		Mode oldMode = mode_;
		assertMode(Mode.FLOAT);
		
		switch (color) {
			case RED:
				setColor(ColorField.RED, red_.floatValue() * x);
				break;
			case GREEN:
				setColor(ColorField.GREEN, green_.floatValue() * x);
				break;
			case BLUE:
				setColor(ColorField.BLUE, blue_.floatValue() * x);
				break;
			case ALPHA:
				setColor(ColorField.ALPHA, alpha_.floatValue() * x);
				break;
			default:
				System.err.println("RawPixel.multiply: got unknown color.");
		}
		
		assertMode(oldMode);
		return this;
	}
	
	public RawPixel multiply(float x) {
		Mode oldMode = mode_;
		assertMode(Mode.FLOAT);
		
		setColor(ColorField.RED, red_.floatValue() * x);
		setColor(ColorField.GREEN, green_.floatValue() * x);
		setColor(ColorField.BLUE, blue_.floatValue() * x);
		setColor(ColorField.ALPHA, alpha_.floatValue() * x);
		
		assertMode(oldMode);
		return this;
	}
	
	public RawPixel divide(ColorField color, float x) {
		Mode oldMode = mode_;
		assertMode(Mode.FLOAT);
		
		switch (color) {
			case RED:
				setColor(ColorField.RED, red_.floatValue() / x);
				break;
			case GREEN:
				setColor(ColorField.GREEN, green_.floatValue() / x);
				break;
			case BLUE:
				setColor(ColorField.BLUE, blue_.floatValue() / x);
				break;
			case ALPHA:
				setColor(ColorField.ALPHA, alpha_.floatValue() / x);
				break;
			default:
				System.err.println("RawPixel.divide: got unknown color.");
		}
		
		assertMode(oldMode);
		return this;
	}
	
	public RawPixel divide(float x) {
		Mode oldMode = mode_;
		assertMode(Mode.FLOAT);
		
		setColor(ColorField.RED, red_.floatValue() / x);
		setColor(ColorField.GREEN, green_.floatValue() / x);
		setColor(ColorField.BLUE, blue_.floatValue() / x);
		setColor(ColorField.ALPHA, alpha_.floatValue() / x);
		
		assertMode(oldMode);
		return this;
	}

	/* private functions */
	
	/**
	 * This is the workhorse of this class. Changes between integer and float
	 * modes. 
	 * @param mode: The numerical mode to operate under. See the constructor
	 * for more details.
	 */
	private void assertMode(Mode mode) {
		if (mode_ == mode) {
			return;
		}
		if (mode_ == Mode.FLOAT && mode == Mode.INT) {
			red_ = (int)(red_.floatValue() * INT_COLOR_MAX);
			green_ = (int)(green_.floatValue() * INT_COLOR_MAX);
			blue_ = (int)(blue_.floatValue() * INT_COLOR_MAX);
			alpha_ = (int)(alpha_.floatValue() * INT_COLOR_MAX);
			mode_ = mode;
		} else if (mode_ == Mode.INT && mode == Mode.FLOAT) {
			red_ = red_.floatValue() / (float)INT_COLOR_MAX;
			green_ = green_.floatValue() / (float)INT_COLOR_MAX;
			blue_ = blue_.floatValue() / (float)INT_COLOR_MAX;
			alpha_ = alpha_.floatValue() / (float)INT_COLOR_MAX;
			mode_ = mode;
		} else {
			System.err.println("RawPixel.assertMode: got unknown mode.");
		}
	}
	
	private static float validate(float color) {
		if (color < FLOAT_COLOR_MIN) {
			return FLOAT_COLOR_MIN;
		} else if (color > FLOAT_COLOR_MAX) {
			return FLOAT_COLOR_MAX;
		} else {
			return color;
		}
	}
	
	private static int validate(int color) {
		if (color < INT_COLOR_MIN) {
			return INT_COLOR_MIN;
		} else if (color > INT_COLOR_MAX) {
			return INT_COLOR_MAX;
		} else {
			return color;
		}
	}
	
	private static class ColorIterator implements Iterator<ColorField> {

		private int count_;
		private int max_;
		
		ColorIterator(Boolean hasAlpha) {
        	count_ = 0;
        	if (hasAlpha) {
        		max_ = 4;
        	} else {
        		max_ = 3;
        	}
        }

        @Override
        public boolean hasNext() {
            return count_ < max_;
        }

        @Override
        public ColorField next() {
        	if (!hasNext()) {
        		throw new NoSuchElementException();
        	}
        	ColorField color;
        	switch (count_) {
				case 0:
					color = ColorField.RED;
					break;
				case 1:
					color = ColorField.GREEN;
					break;
				case 2:
					color = ColorField.BLUE;
					break;
				case 3:
					color = ColorField.ALPHA;
				default:
		            throw new NoSuchElementException();
        	}
        	count_++;
        	return color;
        }

        @Override
        public void remove() {
            return;
        }
    }
	
	public static Iterator<ColorField> iterator(Boolean hasAlpha) {
        return new ColorIterator(hasAlpha);
    }
}
