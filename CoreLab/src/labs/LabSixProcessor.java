package labs;

import iproc.Pixel;
import iproc.RawPixel;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Iterator;
	
public class LabSixProcessor extends iproc.ImageProcessor{

	private static final double TAU = 2*Math.PI;
	
	/* constructors */
	/**
	 * Basic constructor for an ImageProcessor.
	 */
	public LabSixProcessor() {
		super();
	}

	
	/**
	 * Basic constructor for an ImageProcessor from a file
	 * 
	 * @param filename the name of the file to read from.
	 */
	public LabSixProcessor(File imageFile) {
		super(imageFile);
	}
	
	
	/**
	 * Basic constructor for an ImageProcessor from a file
	 * 
	 * @param filename the name of the file to read from.
	 */
	public LabSixProcessor(BufferedImage image) {
		super(image);
	}
	
	
	/* public functions */
	
	public void rotateHue(double Theta) {
		Pixel localP;
		RGB rgb = new RGB();
		HSI hsi = new HSI();
		
		for (int x = 0; x < workingImage_.getWidth(); x++) {
			for (int y = 0; y < workingImage_.getHeight(); y++) {
				localP = new Pixel(workingImage_, x, y);
				RawPixel localRaw = localP.get();

				// get HSI values
				hsi = RGBtoHSI(localRaw.getColorDouble(RawPixel.ColorField.RED),
							   localRaw.getColorDouble(RawPixel.ColorField.GREEN),
							   localRaw.getColorDouble(RawPixel.ColorField.BLUE));
				
				// rotate the hue
				hsi.h = getEquivClass(hsi.h + Theta);
				
				// transform hsi values back to rgb
				rgb = HSItoRGB(hsi.h, hsi.s, hsi.i);
				
				// update the pixel with the new rgb value
				localRaw.setColorAll(rgb.r, rgb.g, rgb.b, 1.0);
				localP.set(localRaw);
			}
		}
	}
	
	
	public void increaseSaturation(double deltaS) {
		Pixel localP;
		RGB rgb = new RGB();
		HSI hsi = new HSI();
		
		for (int x = 0; x < workingImage_.getWidth(); x++) {
			for (int y = 0; y < workingImage_.getHeight(); y++) {
				localP = new Pixel(workingImage_, x, y);
				RawPixel localRaw = localP.get();

				// get HSI values
				hsi = RGBtoHSI(localRaw.getColorDouble(RawPixel.ColorField.RED),
							   localRaw.getColorDouble(RawPixel.ColorField.GREEN),
							   localRaw.getColorDouble(RawPixel.ColorField.BLUE));
				
				// modify the saturation
				hsi.s = hsi.s + deltaS;
				
				// transform hsi values back to rgb
				rgb = HSItoRGB(hsi.h, hsi.s, hsi.i);
				
				// update the pixel with the new rgb value
				localRaw.setColorAll(rgb.r, rgb.g, rgb.b, 1.0);			
				localP.set(localRaw);
			}
		}
	}
	
	
	public void stretchIntensity() {
		Pixel localP;
		RGB rgb = new RGB();
		HSI hsi = new HSI();
		double minI = getMinI();
		double maxI = getMaxI();
		
		for (int x = 0; x < workingImage_.getWidth(); x++) {
			for (int y = 0; y < workingImage_.getHeight(); y++) {
				localP = new Pixel(workingImage_, x, y);
				RawPixel localRaw = localP.get();

				// get HSI values
				hsi = RGBtoHSI(localRaw.getColorDouble(RawPixel.ColorField.RED),
							   localRaw.getColorDouble(RawPixel.ColorField.GREEN),
							   localRaw.getColorDouble(RawPixel.ColorField.BLUE));
				
				hsi.i = (hsi.i - minI)/(maxI - minI);
				
				// transform hsi values back to rgb
				rgb = HSItoRGB(hsi.h, hsi.s, hsi.i);
				
				// update the pixel with the new rgb value
				localRaw.setColorAll(rgb.r, rgb.g, rgb.b, 1.0);
				localP.set(localRaw);
			}
		}
	}
	
	public double[][][] getHsi() {
		double[][][] hsi = new double[3][workingImage_.getWidth()]
				[workingImage_.getHeight()];
		
		double[][][] rgb = getRgb();
		
		for (int x = 0; x < hsi[0].length; x++) {
			for (int y = 0; y < hsi[0][0].length; y++) {
				HSI localHsi = RGBtoHSI(rgb[0][x][y], rgb[1][x][y], rgb[2][x][y]);
				
				hsi[0][x][y] = localHsi.h;
				hsi[1][x][y] = localHsi.s;
				hsi[2][x][y] = localHsi.i;
			}
		}
		return hsi;
	}
	
	public double[][][] getRgb() {
		double[][][] rgb = new double[3][workingImage_.getWidth()]
				[workingImage_.getHeight()];
		
		Iterator<Pixel> pixItter = iterator();
		while (pixItter.hasNext()) {
			Pixel pix = pixItter.next();
			RawPixel rawPix = pix.get();
			
			rgb[0][pix.getX()][pix.getY()] =
					rawPix.getColorDouble(RawPixel.ColorField.RED);
			
			rgb[1][pix.getX()][pix.getY()] =
					rawPix.getColorDouble(RawPixel.ColorField.GREEN);
			
			rgb[2][pix.getX()][pix.getY()] =
					rawPix.getColorDouble(RawPixel.ColorField.BLUE);
		}
		
		return rgb;
	}
	
	public void setFromRgb(double[][][] rgb) {
		Iterator<Pixel> pixItter = iterator();
		while (pixItter.hasNext()) {
			Pixel pix = pixItter.next();
			RawPixel rawPix = pix.get();
			int x = pix.getX();
			int y = pix.getY();
			
			rawPix.setColor(RawPixel.ColorField.RED, rgb[0][x][y]);
			rawPix.setColor(RawPixel.ColorField.GREEN, rgb[1][x][y]);
			rawPix.setColor(RawPixel.ColorField.BLUE, rgb[2][x][y]);
		}
	}
	
	public void setFromHsi(double[][][] hsi) {
		Iterator<Pixel> pixItter = iterator();
		while (pixItter.hasNext()) {
			Pixel pix = pixItter.next();
			RawPixel rawPix = pix.get();
			int x = pix.getX();
			int y = pix.getY();
			
			RGB localRgb = HSItoRGB(hsi[0][x][y], hsi[1][x][y], hsi[2][x][y]);
			
			rawPix.setColor(RawPixel.ColorField.RED, localRgb.r);
			rawPix.setColor(RawPixel.ColorField.GREEN, localRgb.g);
			rawPix.setColor(RawPixel.ColorField.BLUE, localRgb.b);
			
			pix.set(rawPix);
		}
	}
	
		
	/* RGB to HSI conversion functions */
	
	private HSI RGBtoHSI(double R, double G, double B) {
		
		HSI hsi = new HSI(0,0,0);
		double r, g, b, w, i;
		
		i = R + G + B;
		hsi.i = i/3.0;
		r = R/i;
		g = G/i;
		b = B/i;
		
		if (R == G && G == B){
			hsi.s = 0.0;
			hsi.h = 0.0;
		} else {
			w = 0.5*(2*R - G - B)/Math.sqrt((R-G)*(R-G) + (R-B)*(G-B));
			if (w > 1) {
				w = 1;
			} else if (w < -1) {
				w = -1;
			}
			hsi.h = Math.acos(w);
			if (hsi.h < 0) {
				System.err.print("LabSixProcessor.RHBtoHSI: error. got H < 0\n");
			}
			if (B > G) {
				hsi.h = TAU - hsi.h;
			}
			hsi.s = 1 - 3*minThree(r,g,b);
		}
		return hsi;
	}
	
	
	private RGB HSItoRGB(double H, double S, double I) {
		RGB rgb = new RGB(0,0,0);
		
		double r = 0;
		double g = 0;
		double b = 0;
		
		if (S > 1) {
			S = 1;
		}
		if (I > 1) {
			I = 1;
		}
		if (S == 0) {
			rgb.r = I;
			rgb.g = I;
			rgb.b = I;
		} else {
			if ( H < 0 ){
				System.err.print("LabSixProcessor.HSItoRGB: error. got H < 0.\n");
			} else if (H >= 0 && H < TAU/3.0) {
				b = (1-S)/3.0;
				r = (1 + S*Math.cos(H)/Math.cos(Math.PI/3.0 -H ))/3.0;
				g = 1 - b - r;
			} else if (H >= 2*(Math.PI/3.0) && H < 4*(Math.PI/3.0)) {
				H -= TAU/3.0;
				r = (1-S)/3.0;
				g = (1 + S*Math.cos(H)/Math.cos(Math.PI/3.0 -H ))/3.0;
				b = 1 - r - g;
			} else if (H >= 4*(Math.PI/3.0) && H < TAU) {
				H -= 4*Math.PI/3.0;				
				g = (1-S)/3.0;
				b = (1 + S*Math.cos(H)/Math.cos(Math.PI/3.0 -H ))/3.0;
				r = 1 - g - b;
			} else {
				System.err.print("LabSixProcessor.HSItoRGB: error. got H >= 2*pi\n");
			}
			if (r < 0 || g < 0 || b < 0) {
				System.err.print("LabSixProcessor.HSItoRGB: error. r, g, or b was less than zero.\n");
			}
			if (r < 0) {
				r = 0;
			}
			if (g < 0) {
				g = 0;
			}
			if (b < 0) {
				b = 0;
			}
			rgb.r = 3*I*r;
			rgb.g = 3*I*g;
			rgb.b = 3*I*b;
			
			if (rgb.r > 1) {
				rgb.r = 1.0;
			}
			if (rgb.g > 1) {
				rgb.g = 1.0;
			}
			if (rgb.b > 1) {
				rgb.b = 1.0;
			}
		}
		return rgb;
	}
	
	private double minThree(double x, double y, double z) {
		return Math.min(x, Math.min(y,z));
	}
	
	private double getMinI() {
		double min = Double.POSITIVE_INFINITY;
		Pixel localP;
		HSI hsi = new HSI();
		
		for (int x = 0; x < workingImage_.getWidth(); x++) {
			for (int y = 0; y < workingImage_.getHeight(); y++) {
				localP = new Pixel(workingImage_, x, y);
				RawPixel localRaw = localP.get();

				// get HSI values
				hsi = RGBtoHSI(localRaw.getColorDouble(RawPixel.ColorField.RED),
							   localRaw.getColorDouble(RawPixel.ColorField.GREEN),
							   localRaw.getColorDouble(RawPixel.ColorField.BLUE));
				
				if (hsi.i < min) {
					min = hsi.i;
				}
			}
		}
		
		return min;
	}
	
	private double getMaxI() {
		double max = Double.NEGATIVE_INFINITY;
		Pixel localP;
		HSI hsi = new HSI();
		
		for (int x = 0; x < workingImage_.getWidth(); x++) {
			for (int y = 0; y < workingImage_.getHeight(); y++) {
				localP = new Pixel(workingImage_, x, y);
				RawPixel localRaw = localP.get();

				// get HSI values
				hsi = RGBtoHSI(localRaw.getColorDouble(RawPixel.ColorField.RED),
						   localRaw.getColorDouble(RawPixel.ColorField.GREEN),
						   localRaw.getColorDouble(RawPixel.ColorField.BLUE));
				
				if (hsi.i > max) {
					max = hsi.i;
				}
			}
		}
		return max;
	}
	
	/**
	 * @param theta an angle in radians 
	 * @return the equivalent angle in the range 0 <= x < tau
	 */
	private double getEquivClass(double theta) {
		double newTheta;
		if (theta < 0) {
			newTheta = theta + TAU*(1 + (int)(-theta/TAU) );
		} else if (theta >= TAU) {
			newTheta = theta - TAU*(int)(theta/TAU);
		} else {
			newTheta = theta;
		}
		
		// fuck floats
		if (newTheta == TAU) {
			newTheta = 0;
		}
		
		return newTheta; 
	}
	
	
 	
	private class RGB {
		public double r = 0.0;
		public double g = 0.0;
		public double b = 0.0;
		
		public RGB(double red, double green, double blue) {
			r = red;
			g = green;
			b = blue;
		}
		
		public RGB() {
			r = 0.0;
			g = 0.0;
			b = 0.0;
		}
	}
	
	private class HSI {
		public double h = 0.0;
		public double s = 0.0;
		public double i = 0.0;
		
		public HSI(double hue, double sat, double intensity) {
			h = hue;
			s = sat;
			i = intensity;
		}
		
		public HSI() {
			h = 0.0;
			s = 0.0;
			i = 0.0;
		}
	}
}
