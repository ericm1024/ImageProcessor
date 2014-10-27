package iproc;
import java.awt.Color;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class RawPixel {
	
	/* public members */
	
	public static final double DOUBLE_COLOR_MIN = 0.0;
	public static final double DOUBLE_COLOR_MAX = 1.0;
	public static final int INT_COLOR_MIN = 0;
	public static final int INT_COLOR_MAX = 255;
	public static enum Mode {DOUBLE, INT};
	public static enum ColorField {RED, GREEN, BLUE, ALPHA};
	
	
	/* private members */
	
	private Number red_;
	private Number green_;
	private Number blue_;
	private Number alpha_;
	
	private Mode mode_;
	
	
	/* public functions */
	
	/**
	 * Default constructor. Mode defaults to Mode.INT
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
	 * @param mode: the numerical mode to operate under (double or integer).
	 * Under double mode, the colors range from DOUBLE_COLOR_MIN to
	 * DOUBLE_COLOR_MAX. Similarly with integer mode and corresponding
	 * constants.
	 */
	public RawPixel(Mode mode) {
		mode_ = mode;
		if (mode == Mode.DOUBLE) {
			red_ = (double)DOUBLE_COLOR_MIN;
			green_ = (double)DOUBLE_COLOR_MIN;
			blue_ = (double)DOUBLE_COLOR_MIN;
			alpha_ = (double)DOUBLE_COLOR_MIN;
		} else if (mode == Mode.INT) {
			red_ = (int)INT_COLOR_MIN;
			green_ = (int)INT_COLOR_MIN;
			blue_ = (int)INT_COLOR_MIN;
			alpha_ = (int)INT_COLOR_MIN;
		}
	}
	
	/**
	 * copy constructor 
	 * @param other: RawPixel to copy
	 */
	public RawPixel(RawPixel other) {
		RawPixel copy = new RawPixel(mode_);
		copy.setColorRGB(getColorRGB());
	}
	
	
	/**
	 * Set one of the color fields of the pixel with a double
	 */
	public void setColor(ColorField color, double value) {
		assertMode(Mode.DOUBLE);
		switch (color) {
			case RED:
				red_ = validate(value);
				return;
			case GREEN:
				green_ = validate(value);
				return;
			case BLUE:
				blue_ = validate(value);
				return;
			case ALPHA:
				alpha_ = validate(value);
				return;
			default:
				System.err.println("RawPixel.setColor: got unknown color.");
				return;
		}		
	}
	
	/**
	 * Set one of the color fields of the pixel with an int.
	 */
	public void setColor(ColorField color, int value) {
		assertMode(Mode.INT);
		switch (color) {
			case RED:
				red_ = validate(value);
				return;
			case GREEN:
				green_ = validate(value);
				return;
			case BLUE:
				blue_ = validate(value);
				return;
			case ALPHA:
				alpha_ = validate(value);
				return;
			default:
				System.err.println("RawPixel.setColor: got unknown color.");
				return;
		}		
	}
	
	public void setColorAll(double red, double green, double blue, double alpha) {
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
	
	public void setColorRGB(int rgb) {
		Color color = new Color(rgb, true);
		setColorAll(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
	}
	
	public int getColorRGB() {
		assertMode(Mode.INT);
		Color color = new Color(red_.intValue(), green_.intValue(), 
						        blue_.intValue(), alpha_.intValue());
		return color.getRGB();
	}
	
	/**
	 * Get one of the color fields as an int.
	 */
	public int getColorInt(ColorField color) {
		assertMode(Mode.INT);
		switch (color) {
			case RED:
				return red_.intValue();
			case GREEN:
				return green_.intValue();
			case BLUE:
				return blue_.intValue();
			case ALPHA:
				return alpha_.intValue();
			default:
				System.err.println("RawPixel.setColor: got unknown color.");
				return INT_COLOR_MIN;
		}	
	}
	
	/**
	 * Get one of the color fields as a double
	 */
	public double getColorDouble(ColorField color) {
		assertMode(Mode.DOUBLE);
		switch (color) {
			case RED:
				return red_.doubleValue();
			case GREEN:
				return green_.doubleValue();
			case BLUE:
				return blue_.doubleValue();
			case ALPHA:
				return alpha_.doubleValue();
			default:
				System.err.println("RawPixel.setColor: got unknown color.");
				return INT_COLOR_MIN;
		}		
	}
	
	public RawPixel add(ColorField color, double x) {
		assertMode(Mode.DOUBLE);
		switch (color) {
			case RED:
				setColor(ColorField.RED, red_.doubleValue() + x);
			case GREEN:
				setColor(ColorField.GREEN, green_.doubleValue() + x);
			case BLUE:
				setColor(ColorField.BLUE, blue_.doubleValue() + x);
			case ALPHA:
				setColor(ColorField.ALPHA, alpha_.doubleValue() + x);
			default:
				System.err.println("RawPixel.setColor: got unknown color.");
		}
		return this;
	}
	
	public RawPixel subtract(ColorField color, double x) {
		assertMode(Mode.DOUBLE);
		switch (color) {
			case RED:
				setColor(ColorField.RED, red_.doubleValue() - x);
			case GREEN:
				setColor(ColorField.GREEN, green_.doubleValue() - x);
			case BLUE:
				setColor(ColorField.BLUE, blue_.doubleValue() - x);
			case ALPHA:
				setColor(ColorField.ALPHA, alpha_.doubleValue() - x);
			default:
				System.err.println("RawPixel.setColor: got unknown color.");
		}
		return this;
	}
	
	public RawPixel multiply(ColorField color, double x) {
		assertMode(Mode.DOUBLE);
		switch (color) {
			case RED:
				setColor(ColorField.RED, red_.doubleValue() * x);
			case GREEN:
				setColor(ColorField.GREEN, green_.doubleValue() * x);
			case BLUE:
				setColor(ColorField.BLUE, blue_.doubleValue() * x);
			case ALPHA:
				setColor(ColorField.ALPHA, alpha_.doubleValue() * x);
			default:
				System.err.println("RawPixel.setColor: got unknown color.");
		}
		return this;
	}
	
	public RawPixel divide(ColorField color, double x) {
		assertMode(Mode.DOUBLE);
		switch (color) {
			case RED:
				setColor(ColorField.RED, red_.doubleValue() / x);
			case GREEN:
				setColor(ColorField.GREEN, green_.doubleValue() / x);
			case BLUE:
				setColor(ColorField.BLUE, blue_.doubleValue() / x);
			case ALPHA:
				setColor(ColorField.ALPHA, alpha_.doubleValue() / x);
			default:
				System.err.println("RawPixel.setColor: got unknown color.");
		}
		return this;
	}
	
	public RawPixel add(double x) {
		setColor(ColorField.RED, red_.doubleValue() + x);
		setColor(ColorField.GREEN, green_.doubleValue() + x);
		setColor(ColorField.BLUE, blue_.doubleValue() + x);
		setColor(ColorField.ALPHA, alpha_.doubleValue() + x);
		return this;
	}
	
	public RawPixel subtract(double x) {
		setColor(ColorField.RED, red_.doubleValue() - x);
		setColor(ColorField.GREEN, green_.doubleValue() - x);
		setColor(ColorField.BLUE, blue_.doubleValue() - x);
		setColor(ColorField.ALPHA, alpha_.doubleValue() - x);
		return this;
	}
	
	public RawPixel multiply(double x) {
		setColor(ColorField.RED, red_.doubleValue() * x);
		setColor(ColorField.GREEN, green_.doubleValue() * x);
		setColor(ColorField.BLUE, blue_.doubleValue() * x);
		setColor(ColorField.ALPHA, alpha_.doubleValue() * x);
		return this;
	}
	
	public RawPixel divide(double x) {
		setColor(ColorField.RED, red_.doubleValue() / x);
		setColor(ColorField.GREEN, green_.doubleValue() / x);
		setColor(ColorField.BLUE, blue_.doubleValue() / x);
		setColor(ColorField.ALPHA, alpha_.doubleValue() / x);
		return this;
	}
	
	public RawPixel add(RawPixel rhs) {
		add(ColorField.RED, rhs.getColorInt(ColorField.RED));
		add(ColorField.GREEN, rhs.getColorInt(ColorField.GREEN));
		add(ColorField.BLUE, rhs.getColorInt(ColorField.BLUE));
		add(ColorField.ALPHA, rhs.getColorInt(ColorField.ALPHA));
		return this;
	}
	
	public RawPixel subtract(RawPixel rhs) {
		subtract(ColorField.RED, rhs.getColorInt(ColorField.RED));
		subtract(ColorField.GREEN, rhs.getColorInt(ColorField.GREEN));
		subtract(ColorField.BLUE, rhs.getColorInt(ColorField.BLUE));
		subtract(ColorField.ALPHA, rhs.getColorInt(ColorField.ALPHA));
		return this;
	}

	
	/* private functions */
	
	/**
	 * This is the workhorse of this class. Changes between integer and double modes. 
	 * @param mode: The numerical mode to operate under. See the constructor
	 * for more details.
	 */
	private void assertMode(Mode mode) {
		if (mode_ == mode) {
			return;
		}
		if (mode_ == Mode.DOUBLE && mode == Mode.INT) {
			red_ = (int)(red_.doubleValue() * INT_COLOR_MAX);
			green_ = (int)(green_.doubleValue() * INT_COLOR_MAX);
			blue_ = (int)(blue_.doubleValue() * INT_COLOR_MAX);
			alpha_ = (int)(alpha_.doubleValue() * INT_COLOR_MAX);
			mode_ = mode;
		} else if (mode_ == Mode.INT && mode == Mode.DOUBLE) {
			red_ = red_.doubleValue() / (double)INT_COLOR_MAX;
			green_ = green_.doubleValue() / (double)INT_COLOR_MAX;
			blue_ = blue_.doubleValue() / (double)INT_COLOR_MAX;
			alpha_ = alpha_.doubleValue() / (double)INT_COLOR_MAX;
			mode_ = mode;
		} else {
			System.err.println("RawPixel.assertMode: got unknown mode.");
		}
	}
	
	private static double validate(double color) {
		if (color < DOUBLE_COLOR_MIN) {
			return DOUBLE_COLOR_MIN;
		} else if (color > DOUBLE_COLOR_MAX) {
			return DOUBLE_COLOR_MAX;
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
		
        ColorIterator() {
        	count_ = 0;
        }

        @Override
        public boolean hasNext() {
            return count_ < 4;
        }

        @Override
        public ColorField next() {
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

    public static Iterator<ColorField> iterator() {
        return new ColorIterator();
    }
}
