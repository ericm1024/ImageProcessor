package labs;
import HSI;
import ImageProcessor;
import Pixel;
import RGB;

import java.awt.image.BufferedImage;
import java.io.File;
	
public class LabSixProcessor extends ImageProcessor{

	
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
				localP = new Pixel(workingImage_.getRGB(x,y));

				// get HSI values
				hsi = RGBtoHSI((double)localP.getRed()/(double)NUM_COLORS,
						 	   (double)localP.getGreen()/(double)NUM_COLORS, 
						 	   (double)localP.getBlue()/(double)NUM_COLORS);
				
				// rotate the hue
				hsi.H(getEquivClass(hsi.H() + Theta));
				
				// transform hsi values back to rgb
				rgb = HSItoRGB(hsi.H(), hsi.S(), hsi.I());
				
				// update the pixel with the new rgb value
				localP.clear();
				localP.setRGB((int)(rgb.R()*NUM_COLORS), 
						      (int)(rgb.G()*NUM_COLORS), 
						      (int)(rgb.B()*NUM_COLORS));
				
				workingImage_.setRGB(x,y,localP.getRGB());
			}
		}
	}
	
	
	public void increaseSaturation(double deltaS) {
		Pixel localP;
		RGB rgb = new RGB();
		HSI hsi = new HSI();
		
		for (int x = 0; x < workingImage_.getWidth(); x++) {
			for (int y = 0; y < workingImage_.getHeight(); y++) {
				localP = new Pixel(workingImage_.getRGB(x,y));

				// get HSI values
				hsi = RGBtoHSI((double)localP.getRed()/(double)NUM_COLORS,
						 	   (double)localP.getGreen()/(double)NUM_COLORS, 
						 	   (double)localP.getBlue()/(double)NUM_COLORS);
				
				// modify the saturation
				hsi.S(hsi.S() + deltaS);
				
				// transform hsi values back to rgb
				rgb = HSItoRGB(hsi.H(), hsi.S(), hsi.I());
				
				// update the pixel with the new rgb value
				localP.clear();
				localP.setRGB((int)(rgb.R()*NUM_COLORS), 
						      (int)(rgb.G()*NUM_COLORS), 
						      (int)(rgb.B()*NUM_COLORS));
				
				workingImage_.setRGB(x,y,localP.getRGB());
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
				localP = new Pixel(workingImage_.getRGB(x,y));

				// get HSI values
				hsi = RGBtoHSI((double)localP.getRed()/(double)NUM_COLORS,
						 	   (double)localP.getGreen()/(double)NUM_COLORS, 
						 	   (double)localP.getBlue()/(double)NUM_COLORS);
				
				hsi.I((hsi.I() - minI)/(maxI - minI));
				
				// transform hsi values back to rgb
				rgb = HSItoRGB(hsi.H(), hsi.S(), hsi.I());
				
				// update the pixel with the new rgb value
				localP.clear();
				localP.setRGB((int)(rgb.R()*NUM_COLORS), 
						      (int)(rgb.G()*NUM_COLORS), 
						      (int)(rgb.B()*NUM_COLORS));
				
				workingImage_.setRGB(x,y,localP.getRGB());
			}
		}
	}
	
		
	/* RGB to HSI conversion functions */
	
	private HSI RGBtoHSI(double R, double G, double B) {
		
		HSI hsi = new HSI(0,0,0);
		double r, g, b, w, i;
		
		i = R + G + B;
		hsi.I(i/3.0);
		r = R/i;
		g = G/i;
		b = B/i;
		
		if (R == G && G == B){
			hsi.S(0.0);
			hsi.H(0.0);
		} else {
			w = 0.5*(2*R - G - B)/Math.sqrt((R-G)*(R-G) + (R-B)*(G-B));
			if (w > 1) {
				w = 1;
			} else if (w < -1) {
				w = -1;
			}
			hsi.H(Math.acos(w));
			if (hsi.H() < 0) {
				System.err.print("LabSixProcessor.RHBtoHSI: error. got H < 0\n");
			}
			if (B > G) {
				hsi.H(TAU - hsi.H());
			}
			hsi.S(1 - 3*minThree(r,g,b));
		}
		return hsi;
	}
	
	
	private RGB HSItoRGB(double H, double S, double I) {
		RGB rgb = new RGB(0,0,0);
		
		double t = TAU;
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
			rgb.R(I);
			rgb.G(I);
			rgb.B(I);
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
			rgb.R( 3*I*r );
			rgb.G( 3*I*g );
			rgb.B( 3*I*b );
			if (rgb.R() > 1) {
				rgb.R(1.0);
			}
			if (rgb.G() > 1) {
				rgb.G(1.0);
			}
			if (rgb.B() > 1) {
				rgb.B(1.0);
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
				localP = new Pixel(workingImage_.getRGB(x,y));

				// get HSI values
				hsi = RGBtoHSI((double)localP.getRed()/(double)NUM_COLORS,
						 	   (double)localP.getGreen()/(double)NUM_COLORS, 
						 	   (double)localP.getBlue()/(double)NUM_COLORS);
				
				if (hsi.I() < min) {
					min = hsi.I();
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
				localP = new Pixel(workingImage_.getRGB(x,y));

				// get HSI values
				hsi = RGBtoHSI((double)localP.getRed()/(double)NUM_COLORS,
						 	   (double)localP.getGreen()/(double)NUM_COLORS, 
						 	   (double)localP.getBlue()/(double)NUM_COLORS);
				
				if (hsi.I() > max) {
					max = hsi.I();
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
}
