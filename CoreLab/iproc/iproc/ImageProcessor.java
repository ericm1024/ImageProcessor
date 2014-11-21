package iproc;

import iproc.lib.RawPixel;
import iproc.lib.RawPixel.ColorField;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.NoSuchElementException;

import javax.imageio.ImageIO;

public class ImageProcessor {
	
	/* public members */
	public static final String OUTPUT_FILE_TYPE = "png";
	public static final int NUM_COLORS = 256;
	
	/* protected members */
	protected static final float TAU = (float)(2*Math.PI);
	protected static final float PI = (float)Math.PI;
	
	/* private members */
	protected BufferedImage workingImage_;
	protected int imageType_;

	
	/* constructors */
	/**
	 * Basic constructor for an ImageProcessor from a file
	 * 
	 * @param filename the name of the file to read from.
	 */
	public ImageProcessor() {
		workingImage_ = null;
		imageType_ = -1;
	}
	
	/**
	 * Basic constructor for an ImageProcessor from a file
	 * 
	 * @param filename the name of the file to read from.
	 */
	public ImageProcessor(File imageFile) {
		readWorkingImage(imageFile);
		imageType_ = workingImage_.getType();
	}
	
	/**
	 * Constructor from an image
	 * 
	 * @param filename the name of the file to read from.
	 */
	public ImageProcessor(BufferedImage image) {
		setImage(image);
	}
	
	/**
	 * Constructor from greyscale array
	 * 
	 * @param image : the image as a 2d array of floats
	 */
	public ImageProcessor(float[][] image, int type) {
		workingImage_ = new BufferedImage(image.length, image[0].length, type);
		setFromGreyscale(image);
	}
	
	/* public functions */
	
	/* readers and writers */
	
	/**
	 * Reads from imageSource into workingImage
	 *
	 * @return 0 on success, 1 on failure
	 */	
	public int readWorkingImage(File imageSource){
		try {
			setImage(ImageIO.read(imageSource));
			return 0;
		} catch (IOException e) {
			e.printStackTrace();
			return 1;
		}
	}
	
	
	/**
	 * Writes the workingImage to outputFile.
	 * 
	 * @param outputFilename the file to write to
	 * @return 0 on success, 1 on failure
	 */
	public int writeWorkingImage(File outputFile){
		try {
			ImageIO.write(workingImage_, OUTPUT_FILE_TYPE, outputFile);
			return 0;
		} catch (IOException e) {
			e.printStackTrace();
			return 1;
		}
	}
	
	
	/* static readers and writers*/
	
	/**
	 * 
	 * @param imageSource: the file to read from.
	 * @return the image in the file.
	 */
	public static BufferedImage readImage(File imageSource){
		ImageProcessor proc = new ImageProcessor(imageSource);
		return proc.getImage();
	}
	
	
	/**
	 * Writes an image to a file.
	 */
	public static int writeImage(BufferedImage image, File outputFile) {
		ImageProcessor proc = new ImageProcessor(image);
		return proc.writeWorkingImage(outputFile);
	}
	
	
	/* setters and getters */
	
	/**
	 * @return returns the local workingImage.
	 */
	public BufferedImage getImage() {
		// TODO: should this function return a copy? (yes)
		return workingImage_;
	}
	
	/**
	 * @return returns the local workingImage.
	 */
	public void setImage(BufferedImage image) {
		workingImage_ = image;
		imageType_ = workingImage_.getType();
	}
	
	public int getType() {
		return imageType_;
	}
	
	/**
	 * @return A BufferedImage with the name dimensions and type as
	 * workingImage_
	 */
	public BufferedImage blankCopy() {
		return new BufferedImage(workingImage_.getWidth(), 
				workingImage_.getHeight(), workingImage_.getType());
	}
	
	/**
	 * TODO: make this a complete deep copy. Needs investigation
	 * @return a (sort of) deep copy of the working image.
	 */
	public BufferedImage deepCopy() {
		 ColorModel cm = workingImage_.getColorModel();
		 boolean isAlphaPremultiplied = workingImage_.isAlphaPremultiplied();
		 WritableRaster raster = workingImage_.copyData(null);
		 
		 return new BufferedImage(cm, raster, isAlphaPremultiplied, null)
		 	.getSubimage(0, 0, workingImage_.getWidth(),
		 		workingImage_.getHeight());
	}
	
	private class PixelIterator implements Iterator<Pixel> {

        private final BufferedImage image_;
        private int x_;
        private int y_;

        PixelIterator(BufferedImage image) {
        	image_ = image;
        	x_ = 0;
        	y_ = 0;
        }

        @Override
        public boolean hasNext() {
            return (y_ < image_.getHeight() &&
            		x_ < image_.getWidth());
        }

        @Override
        public Pixel next() {
            if (! hasNext()) {
            	throw new NoSuchElementException();
            }
            
            Pixel pixel = new Pixel(image_, x_, y_);
            
            if (x_ < image_.getWidth() -1) {
            	x_++;
            } else {
            	x_ = 0;
            	y_++;
            }
            
            return pixel;
        }

        @Override
        public void remove() {
            return;
        }
    }

	/**
	 * Returns an iterator that iterates over the pixels in the image
	 */
    public Iterator<Pixel> iterator() {
        return new PixelIterator(workingImage_);
    }
    
    /* convolution ops */
    
    /**
     * Convolves the working image with the given kernel. Overwrites the
     * working image with the new image
     * @param kernel : the kernel to convolve with
     */
    public void convolve(float[][] kernel) {
		BufferedImage result = blankCopy();

		Iterator<Pixel> itter = iterator();
		Pixel localPixel;
		
		while (itter.hasNext()) {
			localPixel = itter.next();
			convolveOnce(kernel, new Pixel(
					result, localPixel.getX(), localPixel.getY()));
		}
		workingImage_ = result;
	}
	
    /**
     * Applies a median filter to the image.
     * @param filter : the filter to use
     */
	public void medianFilter(Boolean[][] filter) {
		BufferedImage result = blankCopy();

		Iterator<Pixel> itter = iterator();
		Pixel localPixel;
		
		while (itter.hasNext()) {
			localPixel = itter.next();
			medianFilterOnce(filter, new Pixel(
					result, localPixel.getX(), localPixel.getY()));
		}
		workingImage_ = result;
	}
	
	/**
	 * @param kernel : the kernel to convolve with
	 * @param source : the data to convolve
	 * @return returns an array with the same dimensions as source containing 
	 *         the data in source convolved with the specified kernel
	 */
	public static float[][] convolveArrayPrimative(final float[][] kernel,
			final float[][] source) {
		float[][] result = new float[source.length][source[0].length];
		
		for (int x = 0; x < result.length; x++) {
			for (int y = 0; y < result[0].length; y++) {
				result[x][y] = convolveOncePrimative(x, y, kernel, source);
			}
		}
		
		return result;
	}
	
	/**
	 * Gets the gradient of the image by convolving with x and y gradient
	 * operators.
	 * @param xKernel : the x gradient operator
	 * @param yKernel : the y gradient operator
	 */
	public void gradient(float[][] xKernel, float[][] yKernel) {
		float[][] greyscale = getGreyscale();
		float[][] magnitude = new float[greyscale.length][greyscale[0].length];
	
		for (int x = 0; x < magnitude.length; x++) {
			for (int y = 0; y < magnitude[0].length; y++) {
				float xmag = convolveOncePrimative(x, y, xKernel, greyscale);
				
				float ymag = convolveOncePrimative(x, y, yKernel, greyscale);
				
				magnitude[x][y] = (float)Math.sqrt(xmag*xmag + ymag*ymag);
			}
		}
		setFromGreyscale(normalizeArray(magnitude));
	}
	
	public void trim(int width) {
		BufferedImage result = new BufferedImage(workingImage_.getWidth() - 2*width, 
				workingImage_.getHeight() - 2*width, workingImage_.getType());
		ImageProcessor proc = new ImageProcessor(result);
		
		Pixel thisPix = new Pixel(this.workingImage_);
		Iterator<Pixel> iter = proc.iterator();
		while(iter.hasNext()) {
			Pixel outPix = iter.next();
			int x = outPix.getX();
			int y = outPix.getY();
			thisPix.moveTo(x + width, y + width);
			outPix.set(thisPix.get());
		}
		workingImage_ = result;
	}
	
	public void detectZeroCrossing(float thresh, int windowSize) {
		float[][] grey = getGreyscale();
		float[][] output = new float[grey.length][grey[0].length];

		for (int x = 0; x < grey.length; x++) {
			for (int y = 0; y < grey[0].length; y++) {
				float min = Float.MAX_VALUE;
				float max = Float.MIN_VALUE;
				
				for (int i = -(windowSize/2); i < windowSize/2; i++) {
					for (int j = -(windowSize/2); j < windowSize/2; j++) {
						
						// bounds check
						if (0 < x+i && x+i < grey.length 
								&& 0 < y +j && y+j <grey[0].length) {
							float g = grey[x+i][y+j];
							if (g < min) {
								min = g;
							}
							if (g > max) {
								max = g;
							}
						}
					}
				}
				float local = grey[x][y];
				if (Math.abs(local - min) > thresh && Math.abs(local - max) > thresh) {
					output[x][y] = max - min;
				} else {
					output[x][y] = RawPixel.FLOAT_COLOR_MIN;
				}
			}
		}
		setFromGreyscale(output);
	}
	
	public void threshold(int thresh) {
		Iterator<Pixel> iter = iterator();
		while (iter.hasNext()) {
			Pixel pix = iter.next();
			if (pix.getGrey() < thresh) {
				pix.setGrey(0);
			}
		}
	}

	public void threshold(float thresh) {
		Iterator<Pixel> iter = iterator();
		while (iter.hasNext()) {
			Pixel pix = iter.next();
			if (pix.getGreyFloat() < thresh) {
				pix.setGrey(0);
			}
		}
	}
	
	public float gradientAngle(float[][] image, int x, int y, float[][] gradX, float[][] gradY) {
		float px = convolveOncePrimative(x,y,gradX,image);
		float py = convolveOncePrimative(x,y,gradY,image);
		return (float)Math.atan2(py, px);
	}
	
	/* convolution helpers */
		
	private void convolveOnce(float[][] kernel, Pixel outputPixel) {
		float redSum = 0;
		float greenSum = 0;
		float blueSum = 0;
		Pixel workingPixel = new Pixel(workingImage_, outputPixel.getX(),
											outputPixel.getY());
		
		for (int kernelX = 0; kernelX < kernel.length; kernelX++) {
			int xIndex = (outputPixel.getX() - kernel.length/2) + kernelX;
			
			for (int kernelY = 0; kernelY < kernel[0].length; kernelY++) {
				int yIndex = (outputPixel.getY() - kernel[0].length/2) + kernelY;
				
				if ( !workingPixel.inImage(xIndex, yIndex)) {
					continue;
				}
				workingPixel.moveTo(xIndex, yIndex);
				float weight = kernel[kernelX][kernelY];
				
				redSum += weight * workingPixel.getRedFloat();
				greenSum += weight * workingPixel.getGreenFloat();
				blueSum += weight * workingPixel.getBlueFloat();
			}
		}
		outputPixel.setRed(redSum);
		outputPixel.setGreen(greenSum);
		outputPixel.setBlue(blueSum);
	}
	
	private void medianFilterOnce(Boolean[][] filter, Pixel outputPixel) {
		Pixel workingPixel = new Pixel(workingImage_, outputPixel.getX(),
										outputPixel.getY());
		
		ArrayList<RawPixel> pixels = new ArrayList<RawPixel>();
		
		for (int filterX = 0; filterX < filter.length; filterX++) {
			int xIndex = (outputPixel.getX() - filter.length/2) + filterX;
			
			for (int filterY = 0; filterY < filter[0].length; filterY++) {
				int yIndex = (outputPixel.getY() - filter[0].length/2) + filterY;
				
				if (workingPixel.inImage(xIndex, yIndex)
					&& filter[filterX][filterY]) {
					workingPixel.moveTo(xIndex, yIndex);
					pixels.add(workingPixel.get());
				}
			}
		}
		outputPixel.set(getMedianPixel(pixels));
	}
	
	private RawPixel getMedianPixel(ArrayList<RawPixel> pixels) {
		RawPixel median = new RawPixel();
		HashMap<ColorField,int[]> colors = new HashMap<ColorField, int[]>();
		
		Iterator<ColorField> colorItter = RawPixel.iterator(false);
		while (colorItter.hasNext()) {
			ColorField localColor = colorItter.next();
			int[] localCounts =  new int[pixels.size()];
			
			for (int i = 0; i < pixels.size(); i++) {
				localCounts[i] = pixels.get(i).getColorInt(localColor);
			}
			colors.put(localColor, localCounts);
		}
		
		// reset the iterator -- we're looping over the colors again
		colorItter = RawPixel.iterator(false);
		while (colorItter.hasNext()) {
			ColorField localColor = colorItter.next();
			int[] localCounts = colors.get(localColor);
			Arrays.sort(localCounts);
			
			// get the middle value
			median.setColor(localColor, localCounts[localCounts.length/2]);
		}
		median.setColor(ColorField.ALPHA, RawPixel.INT_COLOR_MAX);
		return median;
	}
	
	/**
	 * @param x : the x coordinate to center the kernel at
	 * @param y : the y coordinate to center the kernel at
	 * @param kernel : kernel to convolve with
	 * @param source : data to convolve from
	 * @return the new value at x,y after convolution
	 */
	private static float convolveOncePrimative(final int x, final int y,
			final float[][] kernel, final float[][] source) {
		float sum = 0;
				
		for (int kernelX = 0; kernelX < kernel.length; kernelX++) {
			int xIndex = (x - kernel.length/2) + kernelX;
			for (int kernelY = 0; kernelY < kernel[0].length; kernelY++) {
				int yIndex = (y - kernel[0].length/2) + kernelY;
				
				// ensure the indices are in bounds
				if ( 0 <= xIndex && xIndex < source.length 
						&& 0 <= yIndex && yIndex < source[0].length) {
					sum += kernel[kernelX][kernelY] * source[xIndex][yIndex];
				}
			}
		}		
		return sum;
	}
	
	/* colorspace ops */
	
	/* public functions */
	
	public void rotateHue(float Theta) {
		Iterator<Pixel> iter = iterator();
		while (iter.hasNext()) {
			Pixel pix = iter.next();
			
			// get HSI values
			HSI hsi = RGBtoHSI(pix.getRedFloat(), pix.getGreenFloat(),
					pix.getBlueFloat());
			
			// rotate the hue
			hsi.h = getEquivClass(hsi.h + Theta);
			
			// transform hsi values back to rgb
			RGB rgb = HSItoRGB(hsi.h, hsi.s, hsi.i);
			
			pix.setRed(rgb.r);
			pix.setGreen(rgb.g);
			pix.setBlue(rgb.b);
		}
	}
	
	public void increaseSaturation(float deltaS) {		
		Iterator<Pixel> iter = iterator();
		while (iter.hasNext()) {
			Pixel pix = iter.next();
			
			// get HSI values
			HSI hsi = RGBtoHSI(pix.getRedFloat(), pix.getGreenFloat(),
					pix.getBlueFloat());
			
			// modify the saturation
			hsi.s = hsi.s + deltaS;

			// transform hsi values back to rgb
			RGB rgb = HSItoRGB(hsi.h, hsi.s, hsi.i);
			
			pix.setRed(rgb.r);
			pix.setGreen(rgb.g);
			pix.setBlue(rgb.b);
		}
	}
	
	public void stretchIntensity() {
		float minI = getMinI();
		float maxI = getMaxI();
		
		Iterator<Pixel> iter = iterator();
		while (iter.hasNext()) {
			Pixel pix = iter.next();
			
			// get HSI values
			HSI hsi = RGBtoHSI(pix.getRedFloat(), pix.getGreenFloat(),
					pix.getBlueFloat());
			
			// stretch the intensity
			hsi.i = (hsi.i - minI)/(maxI - minI);

			// transform hsi values back to rgb
			RGB rgb = HSItoRGB(hsi.h, hsi.s, hsi.i);
			
			pix.setRed(rgb.r);
			pix.setGreen(rgb.g);
			pix.setBlue(rgb.b);
		}
	}
	
	public float[][][] getHsi() {
		float[][][] hsi = new float[3][workingImage_.getWidth()]
				[workingImage_.getHeight()];
		
		float[][][] rgb = getRgb();
		
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
	
	public float[][][] getRgb() {
		float[][][] rgb = new float[3][workingImage_.getWidth()]
				[workingImage_.getHeight()];
		
		Iterator<Pixel> pixItter = iterator();
		while (pixItter.hasNext()) {
			Pixel pix = pixItter.next();
			
			rgb[0][pix.getX()][pix.getY()] = pix.getRedFloat();			
			rgb[1][pix.getX()][pix.getY()] = pix.getGreenFloat();
			rgb[2][pix.getX()][pix.getY()] = pix.getBlueFloat();
		}	
		return rgb;
	}
	
	public float[][] getGreyscale() {
		float[][] greyscale = new float[workingImage_.getWidth()]
				[workingImage_.getHeight()];
		
		Iterator<Pixel> pixItter = iterator();
		while (pixItter.hasNext()) {
			Pixel pix = pixItter.next();
			greyscale[pix.getX()][pix.getY()] = pix.getGreyFloat();
		}		
		return greyscale;
	}
	
	public void setFromRgb(float[][][] rgb) {
		Iterator<Pixel> pixItter = iterator();
		while (pixItter.hasNext()) {
			Pixel pix = pixItter.next();
			int x = pix.getX();
			int y = pix.getY();
			
			pix.setRed(rgb[0][x][y]);
			pix.setGreen(rgb[1][x][y]);
			pix.setBlue(rgb[2][x][y]);
		}
	}
	
	public void setFromGreyscale(float[][] greyscale) {
		workingImage_ = new BufferedImage(greyscale.length, 
				greyscale[0].length, workingImage_.getType());
		
		Iterator<Pixel> pixItter = iterator();
		
		while (pixItter.hasNext()) {
			Pixel pix = pixItter.next();
			pix.setGrey(greyscale[pix.getX()][pix.getY()]);
		}
	}
	
	public void setFromHsi(float[][][] hsi) {
		Iterator<Pixel> pixItter = iterator();
		while (pixItter.hasNext()) {
			Pixel pix = pixItter.next();
			int x = pix.getX();
			int y = pix.getY();
			
			RGB localRgb = HSItoRGB(hsi[0][x][y], hsi[1][x][y], hsi[2][x][y]);
			
			pix.setRed(localRgb.r);
			pix.setGreen(localRgb.g);
			pix.setBlue(localRgb.b);
		}
	}
	
	/* RGB to HSI conversion functions */
	
	// DO NOT TOUCH THIS, IT WORKS, DON'T ASK WHY
	private HSI RGBtoHSI(float R, float G, float B) {
		
		HSI hsi = new HSI(0,0,0);
		float r, g, b, w, i;
		
		i = R + G + B;
		hsi.i = i/3;
		r = R/i;
		g = G/i;
		b = B/i;
		
		if (R == G && G == B){
			hsi.s = 0;
			hsi.h = 0;
		} else {
			w = (float)0.5*(2*R - G - B)/(float)Math.sqrt((R-G)*(R-G) + (R-B)*(G-B));
			if (w > 1) {
				w = 1;
			} else if (w < -1) {
				w = -1;
			}
			hsi.h = (float)Math.acos(w);
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
	
	// DO NOT TOUCH THIS, IT WORKS, DON'T ASK WHY
	private RGB HSItoRGB(float H, float S, float I) {
		RGB rgb = new RGB(0,0,0);
		
		float r = 0;
		float g = 0;
		float b = 0;
		
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
				b = (1-S)/3f;
				r = (1 + S*(float)Math.cos(H)/(float)Math.cos(Math.PI/3.0 -H ))/3f;
				g = 1 - b - r;
			} else if (H >= 2*(Math.PI/3.0) && H < 4*(Math.PI/3.0)) {
				H -= TAU/3.0;
				r = (1-S)/3f;
				g = (1 + S*(float)Math.cos(H)/(float)Math.cos(Math.PI/3.0 -H ))/3f;
				b = 1 - r - g;
			} else if (H >= 4*(Math.PI/3.0) && H < TAU) {
				H -= 4*Math.PI/3.0;				
				g = (1-S)/3f;
				b = (1 + S*(float)Math.cos(H)/(float)Math.cos(Math.PI/3.0 -H ))/3f;
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
				rgb.r = 1;
			}
			if (rgb.g > 1) {
				rgb.g = 1;
			}
			if (rgb.b > 1) {
				rgb.b = 1;
			}
		}
		return rgb;
	}
	
	private float minThree(float x, float y, float z) {
		return Math.min(x, Math.min(y,z));
	}
	
	private float getMinI() {
		float min = Float.POSITIVE_INFINITY;
		
		Iterator<Pixel> iter = iterator();
		while (iter.hasNext()) {
			Pixel pix = iter.next();
			
			// get HSI values
			HSI hsi = RGBtoHSI(pix.getRedFloat(), pix.getGreenFloat(), 
					pix.getBlueFloat());
			
			if (hsi.i < min) {
				min = hsi.i;
			}
		}
		return min;
	}
	
	private float getMaxI() {
		float max = Float.NEGATIVE_INFINITY;
		
		Iterator<Pixel> iter = iterator();
		while (iter.hasNext()) {
			Pixel pix = iter.next();
			
			// get HSI values
			HSI hsi = RGBtoHSI(pix.getRedFloat(), pix.getGreenFloat(), 
					pix.getBlueFloat());
			
			if (hsi.i > max) {
				max = hsi.i;
			}
		}
		return max;
	}
	
	/**
	 * @param theta an angle in radians 
	 * @return the equivalent angle in the range 0 <= x < tau
	 */
	private float getEquivClass(float theta) {
		float newTheta;
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
		public float r = 0;
		public float g = 0;
		public float b = 0;
		
		public RGB(float red, float green, float blue) {
			r = red;
			g = green;
			b = blue;
		}
	}
	
	private class HSI {
		public float h = 0;
		public float s = 0;
		public float i = 0;
		
		public HSI(float hue, float sat, float intensity) {
			h = hue;
			s = sat;
			i = intensity;
		}
	}
	
	/**
	 * Returns a normalized 256x256 rgb histogram of the processor's working
	 * image.
	 */
	public BufferedImage histogramRgb() {
		
		HashMap<ColorField, int[]> colorCounts = getShadeCounts();
		normalizeCounts(colorCounts);
		
		return makeHistogram(colorCounts);
	}
	
	/**
	 * Returns a normalized 256x256 greyscale histogram of the processor's
	 * working image.
	 */
	public BufferedImage histogramGrey() {
		makeGreyscale();
		return histogramRgb();
	}
		
	/**
	 * Returns a normalized 256x256 cumulative histogram of the processor's
	 * working image.
	 */
	public BufferedImage cumulativeHistogramRgb() {
		
		HashMap<ColorField, int[]> colorCounts = getShadeCounts();
		makeCumulative(colorCounts);
		normalizeCounts(colorCounts);
		
		return makeHistogram(colorCounts);
	}

	/**
	 * Returns a normalized 256x256 cumulative greyscale histogram of the
	 * processor's working image.
	 */
	public BufferedImage cumulativeHistogramGrey() {	
		makeGreyscale();
		return cumulativeHistogramRgb();
	}
	
	/* private histogram functions */
	
	/**
	 * Counts the number of occurrences of each shade of each color. 
	 * @return The hash map : ColorFields -> int[] counting each color
	 */
	private HashMap<ColorField, int[]> getShadeCounts() {
		HashMap<ColorField, int[]> shades = getShadeHashMap();
		RawPixel localRawPixel;
		Iterator<Pixel> pixelItter = iterator();
		
		while (pixelItter.hasNext()) {
			localRawPixel = pixelItter.next().get();
			Iterator<ColorField> colorItter = RawPixel.iterator(false);
			
			while (colorItter.hasNext()) {
				ColorField localColor = colorItter.next();
				shades.get(localColor)[localRawPixel.getColorInt(localColor)]++;
			}
		}
		return shades;
	}
	
	/**
	 * Normalizes each value in shadeCounts to [0,255]
	 * @param shadeCounts the hash map : ColorFields -> int[] counting each color
	 */
	private void normalizeCounts(HashMap<ColorField, int[]> shadeCounts) {
		int max = getMaxShadeCount(shadeCounts);
		Iterator<ColorField> colorItter = RawPixel.iterator(false);
		
		while (colorItter.hasNext()) {
			int[] counts = shadeCounts.get(colorItter.next());
			
			for (int i = 0; i < counts.length; i++) {
				counts[i] *= (float)NUM_COLORS/(float)max;
			}
		}
	}
	
	/**
	 * Makes each array cumulative, so each element is the sum of all the
	 * previous elements.
	 * @param shadeCounts the hash map : ColorFields -> int[] counting
	 * each color.
	 */
	private void makeCumulative(HashMap<ColorField, int[]> shadeCounts) {
		Iterator<ColorField> colorItter = RawPixel.iterator(false);
		
		while (colorItter.hasNext()) {
			int[] counts = shadeCounts.get(colorItter.next());
			
			for (int i = 1; i < counts.length; i++) {
				counts[i] += counts[i-1];
			}
		}
	}
	
	/**
	 * Makes a histogram from shadeCounts 
	 * @param shadeCounts
	 * @return
	 */
	private BufferedImage makeHistogram(HashMap<ColorField, int[]> shadeCounts) {
		BufferedImage histogram = new BufferedImage(NUM_COLORS, NUM_COLORS, imageType_);
		ImageProcessor proc = new ImageProcessor(histogram);	
		Pixel localPixel;
		RawPixel localRawPixel;
		
		Iterator<Pixel> itter = proc.iterator();
		while (itter.hasNext()) {
			localPixel = itter.next();
			localRawPixel = localPixel.get();
			
			Iterator<ColorField> colorItter = RawPixel.iterator(false);
			while (colorItter.hasNext()) {
				ColorField color = colorItter.next();
				
				// we want to color the histogram up to the value of the
				// shade in the map, so  if the histogram color value is >=
				// to our current y value, we set the color
				if (shadeCounts.get(color)[localPixel.getX()] >=  
						histogram.getHeight() - localPixel.getY()) {
					localRawPixel.setColor(color, RawPixel.INT_COLOR_MAX);
					localPixel.set(localRawPixel);
				}
			}
			
		}
		return histogram;
	}
	
	/**
	 * Returns an empty hash map with 3 or 4 elements (depending on the
	 * includeAlpha_ flag), each a int array of size NUM_COLORS.
	 * @return Hash map : ColorFields -> int[] 
	 */
	private HashMap<ColorField, int[]> getShadeHashMap() {
		HashMap<ColorField,int[]> colors = new HashMap<ColorField, int[]>();	
		Iterator<ColorField> colorItter = RawPixel.iterator(false);
		
		while (colorItter.hasNext()) {
			colors.put(colorItter.next(), new int[NUM_COLORS]);
		}
		return colors;
	}
	
	/**
	 * Returns the max r, g, or b value out of all the pixels in the
	 * workingImage_ 
	 * @return the max shades
	 */
	private int getMaxShadeCount(final HashMap<ColorField, int[]> shadeCounts) {
		Iterator<ColorField> colorItter = RawPixel.iterator(false);
		int max = 0;
		
		while (colorItter.hasNext()) {
			for (int x : shadeCounts.get(colorItter.next())) {
				if (x > max) {
					max = x;
				}
			}
		}
		return max;
	}
	
	/* lab 2: public functions */
	
	public void resize(float xRatio, float yRatio) {
		int newWidth = (int)((float)workingImage_.getWidth()*xRatio); 
		int newHeight = (int)((float)workingImage_.getHeight()*yRatio);
		BufferedImage result = new BufferedImage(newWidth, newHeight, imageType_);
		
		resizeWorkingTo(result);
	}
	
	public void rotate(float theta) {
		BufferedImage result = new BufferedImage(workingImage_.getWidth(), 
												 workingImage_.getHeight(),
												 imageType_);	
		rotateWorkingTo(result, theta);
	}
	
	/**
	 * @param output the BufferedImage to write output to.
	 */
	private void resizeWorkingTo(BufferedImage output) {
		ImageProcessor proc = new ImageProcessor(output);
		
		/* the four pixels we will work with for each interpolation*/
		Pixel a = new Pixel(workingImage_);
		Pixel b = new Pixel(workingImage_);
		Pixel c = new Pixel(workingImage_);
		Pixel d = new Pixel(workingImage_);
		
		Iterator<Pixel> pixelItter = proc.iterator();
		while (pixelItter.hasNext()) {
			Pixel localPixel = pixelItter.next();
			
			int destX = localPixel.getX();
			int destY = localPixel.getY();
		
			/* find where our source X and Y */
			float sourceX = (float)destX
							* (float)(workingImage_.getWidth() - 1)
							/ (float)(output.getWidth() - 1);
			
			float sourceY = (float)destY
							* (float)(workingImage_.getHeight() - 1)
							/ (float)(output.getHeight() - 1);
			
			/* incoming ternary operators */
			a = a.inImage((int)sourceX, (int)sourceY)
					? a.moveTo((int)sourceX, (int)sourceY)
					/* if it's out of bounds, we want a blank pixel
					 * (no we don't, see todo) */
				    : new Pixel(workingImage_); // TODO: fix this.
					
			b = b.inImage((int)sourceX + 1, (int)sourceY)
					? b.moveTo((int)sourceX + 1, (int)sourceY)
					: new Pixel(workingImage_);
					
			c = c.inImage((int)sourceX, (int)sourceY + 1)
					? c.moveTo((int)sourceX, (int)sourceY + 1)
					: new Pixel(workingImage_);
			
			d = d.inImage((int)sourceX + 1, (int)sourceY + 1)
					? d.moveTo((int)sourceX + 1, (int)sourceY + 1)
					: new Pixel(workingImage_);
					
			/* find the remainders */
			float u = sourceX % 1;
			float v = sourceY % 1;
			
			a.set(a.get().multiply(1 - v - u - u*v));
			b.set(b.get().multiply(u - u*v));
			c.set(c.get().multiply(v - u*v));
			d.set(d.get().multiply(u*v));
			
			RawPixel sum = a.get().add(b.get().add(c.get().add(d.get())));	
			
			/* 
			 * set the corresponding pixel in the output to the sum of all the contributing
			 * components from the original image
			 */
			output.setRGB(destX, destY, sum.getColorRgb());
		}
		workingImage_ = output;
	}
	
	
	/**
	 * @param output the BufferedImage to write output to.
	 */
	private void rotateWorkingTo(BufferedImage output, float theta) {
		ImageProcessor proc = new ImageProcessor(output);
		
		/* the four pixels we will work with for each interpolation*/
		Pixel a = new Pixel(workingImage_);
		Pixel b = new Pixel(workingImage_);
		Pixel c = new Pixel(workingImage_);
		Pixel d = new Pixel(workingImage_);
		
		/* the center of the image */
		int xCenter = workingImage_.getWidth()/2;
		int yCenter = workingImage_.getHeight()/2;
		
		Iterator<Pixel> pixelItter = proc.iterator();
		while (pixelItter.hasNext()) {
			Pixel localPixel = pixelItter.next();
			
			int destX = localPixel.getX();
			int destY = localPixel.getY();
				
			/* find where our source X and Y are (algorithm given in assignment) */
			float sourceX = ( (float)Math.cos(theta)*(destX - xCenter) +
					(float)Math.sin(theta)*(destY - yCenter) + xCenter); 
			float sourceY = (-(float)Math.sin(theta)*(destX - xCenter) + 
					(float)Math.cos(theta)*(destY - yCenter) + yCenter); 
			
			/* find the remainders */
			float u = sourceX % 1;
			float v = sourceY % 1;
			
			/* incoming ternary operators */
			a = a.inImage((int)sourceX, (int)sourceY)
					? a.moveTo((int)sourceX, (int)sourceY)
					/* if it's out of bounds, we want a blank pixel
					 * (no we don't, see todo) */
				    : new Pixel(workingImage_); // TODO: fix this.
					
			b = b.inImage((int)sourceX + 1, (int)sourceY)
					? b.moveTo((int)sourceX + 1, (int)sourceY)
					: new Pixel(workingImage_);
					
			c = c.inImage((int)sourceX, (int)sourceY + 1)
					? c.moveTo((int)sourceX, (int)sourceY + 1)
					: new Pixel(workingImage_);
			
			d = d.inImage((int)sourceX + 1, (int)sourceY + 1)
					? d.moveTo((int)sourceX + 1, (int)sourceY + 1)
					: new Pixel(workingImage_);
			
			/* scale the pixels based on the algorithm we were given */
			a.set(a.get().multiply(1 - v - u - u*v));
			b.set(b.get().multiply(u - u*v));
			c.set(c.get().multiply(v - u*v));
			d.set(d.get().multiply(u*v));
			
			RawPixel sum = a.get().add(b.get().add(c.get().add(d.get())));
			
			/* 
			 * set the corresponding pixel in the output to the sum of all the contributing
			 * components from the original image
			 */
			output.setRGB(destX, destY, sum.getColorRgb());
		}
		workingImage_ = output;
	}
	
	/* lab 3 public functions */
	
	public BufferedImage stretchedGreyHisto() {
		int[] colorCounts = countColorsGreyscale();
		float pixelWeight = ((float)NUM_COLORS)/arrayMax(colorCounts);
		normalizeColorCounts(colorCounts, pixelWeight);
		
		int minIndex = firstNonZeroIndex(colorCounts);
		int maxIndex = lastNonZeroIndex(colorCounts);
		
		int[] stretchedColorCounts = stretchArray(colorCounts, minIndex, maxIndex);
		
		return histogramFromArray(stretchedColorCounts);
	}
	
	public BufferedImage stretchedInterpGreyHisto() {
		int[] colorCounts = countColorsGreyscale();
		int[] stretchedColorCounts;
		float pixelWeight = ((float)NUM_COLORS)/arrayMax(colorCounts);
		normalizeColorCounts(colorCounts, pixelWeight);
		
		int minIndex = firstNonZeroIndex(colorCounts);
		int maxIndex = lastNonZeroIndex(colorCounts);
		
		stretchedColorCounts = interpolatedStretch(colorCounts, minIndex, maxIndex);
		
		return histogramFromArray(stretchedColorCounts);
	}
	
	public BufferedImage cutoffHisto(float cutoffPercent) {
		assert(0 <= cutoffPercent && 1 >= cutoffPercent);
		
		int[] colorCounts = countColorsGreyscale();
		int[] stretchedColorCounts;
		
		int minIndex = minIndexCuttof(colorCounts, cutoffPercent);
		int maxIndex = maxIndexCuttof(colorCounts, cutoffPercent);
		stretchedColorCounts = stretchArray(colorCounts, minIndex, maxIndex);
		
		float pixelWeight = ((float)NUM_COLORS)/arrayMax(stretchedColorCounts);
		normalizeColorCounts(stretchedColorCounts, pixelWeight);
		
		return histogramFromArray(stretchedColorCounts);
	}
	
	public BufferedImage stretchMinMax() {
		int[] colorCounts = countColorsGreyscale();
		int minIndex = firstNonZeroIndex(colorCounts);
		int maxIndex = lastNonZeroIndex(colorCounts);
		return stretchImageMinMax(minIndex, maxIndex);
	}
	
	public BufferedImage stretchCutoff(float cutoffPercent) {
		assert(0 <= cutoffPercent && 1 >= cutoffPercent);
		
		int[] colorCounts = countColorsGreyscale();
		int minIndex = minIndexCuttof(colorCounts, cutoffPercent);
		int maxIndex = maxIndexCuttof(colorCounts, cutoffPercent);
		return stretchImageMinMax(minIndex, maxIndex);		
	}
	
	/**
	 * Makes the working image greyscale.
	 */
	public void makeGreyscale() {	
		Iterator<Pixel> pixIter = iterator();
		while (pixIter.hasNext()) {
			pixIter.next().greyscale();
		}
	}
	
	/* private functions */
	
	/**
	 * @return an integer array of NUM_COLORS elements where each element is
	 * the number of times that color occurs
	 */
	private int[] countColorsGreyscale() {
		int[] colorCounts = new int[NUM_COLORS];
		
		Iterator<Pixel> pixIter = iterator();
		while (pixIter.hasNext()) {
			int greyValue = pixIter.next().getGrey();
			colorCounts[greyValue]++;
		}
		
		return colorCounts;
	}
	
	/**
	 * Maps the number of occurances of each color to the range 0, NUM_COLORS
	 */
	private void normalizeColorCounts(int[] colorCounts, float weight) {
		
		for (int i=0; i < colorCounts.length; i++) {
			colorCounts[i] = (int)((float)colorCounts[i]*weight);
		}
	}
	
	/**
	 * @param colorCounts
	 * @return
	 */
	private BufferedImage histogramFromArray(int[] colorCounts) {
		BufferedImage histogram = new BufferedImage(NUM_COLORS, NUM_COLORS, imageType_);
		int localHistHeight;
		Pixel localPixel;
		
		assert(colorCounts.length == NUM_COLORS);
		
		for (int x = 0; x < histogram.getWidth(); x++) {
			localHistHeight = colorCounts[x];
			
			for (int y = 0; y < histogram.getHeight(); y++) {
				if ((histogram.getHeight() - localHistHeight) <= y) {
					localPixel = new Pixel(histogram, x, y);
					localPixel.setGrey(RawPixel.INT_COLOR_MAX);;
				}
			}
		}		
		return histogram;
	}
	
	/**
	 * @param array an array of integers
	 * @return the maximum integer in the array
	 */
	private int arrayMax(int[] array) {
		int max = Integer.MIN_VALUE;
		for (int i=0; i<array.length; i++) {
			if (array[i] > max) {
				max = array[i];
			}
		}
		return max;
	}
	
	/**
	 * @param array
	 * @return the first index into the array at which the value
	 * stored at that index is nonzero 
	 */
	private int firstNonZeroIndex(int[] array) {
		int index = 0;		
		while (array[index] == 0) {
			index++;
		}
		return index;
	}
	
	/**
	 * @param array
	 * @return the last index into the array at which the value
	 * stored at that index is nonzero 
	 */
	private int lastNonZeroIndex(int[] array) {
		int index = array.length - 1;		
		while (array[index] == 0) {
			index--;
		}
		return index;		
	}
	
	/**
	 * Stretches a segment of an array between minIndex and maxIndex over
	 * an array of the same length as the input array.
	 * @param array
	 * @param minIndex
	 * @param maxIndex
	 * @return the stretchedArray
	 */
	private int[] stretchArray(int[] array, int minIndex, int maxIndex) {
		int[] stretchedArray = new int[array.length];
		int outputIndex;
		
		for (int x = minIndex; x < maxIndex; x ++) {
			outputIndex = transform(x, minIndex, maxIndex);
			stretchedArray[outputIndex] = array[x];
		}
		
		return stretchedArray;
	}
	
	private int transform(int greyValue, int min, int max) {
		int range = max - min;
		return (int)(NUM_COLORS*(float)(greyValue - min)/range);
	}
	
	/**
	 * Stretches a segment of an array between minIndex and maxIndex over
	 * an array of the same length as the input array. Same as 
	 * stretchArray except this function interpolates so that there aren't
	 * weird gaps.
	 * @param array
	 * @param minIndex
	 * @param maxIndex
	 * @return the stretchedArray
	 */
	private int[] interpolatedStretch(int[] array, int minIndex, int maxIndex) {
		int[] stretchedArray = stretchArray(array, minIndex, maxIndex);
		
		for (int i = 1; i < stretchedArray.length-1; i ++) {
			if(stretchedArray[i] == 0) {
				stretchedArray[i] = (stretchedArray[i-1] + stretchedArray[i+1])/2;
			}
		}
		
		return stretchedArray;
	}

	/**
	 * @param colorCounts: int[] where each element is the number of
	 * occurrences of the color value of its index
	 * @param cutoffPercent: float between 0 and 1 inclusive. The amount of
	 * pixels to cut off
	 * @return the lower index into colorCounts at which cutoffPercent of
	 * the points are below that index
	 */
	private int minIndexCuttof(int[] colorCounts, float cutoffPercent) {
		assert(0 <= cutoffPercent && 1 >= cutoffPercent);
		
		int totalPixels = workingImage_.getWidth() * workingImage_.getHeight();
		int pixelsToCuttof = (int)((float)totalPixels*cutoffPercent);
		int pixelsSeen = 0;
		int index = 0;
		
		while (pixelsSeen < pixelsToCuttof) {
			pixelsSeen += colorCounts[index];
			index++;
		}
		
		return index;
	}
	
	/**
	 * @param colorCounts: int[] where each element is the number of
	 * occurrences of the color value of its index
	 * @param cutoffPercent: float between 0 and 1 inclusive. The amount of
	 * pixels to cut off
	 * @return the upper index into colorCounts at which cutoffPercent of
	 * the points are above that index
	 */
	private int maxIndexCuttof(int[] colorCounts, float cutoffPercent) {
		assert(0 <= cutoffPercent && 1 >= cutoffPercent);
		
		int totalPixels = workingImage_.getWidth() * workingImage_.getHeight();
		int pixelsToCuttof = (int)((float)totalPixels*cutoffPercent);
		int pixelsSeen = 0;
		int index = colorCounts.length - 1;
		
		while (pixelsSeen < pixelsToCuttof) {
			pixelsSeen += colorCounts[index];
			index--;
		}
		
		return index;
	}
	
	private BufferedImage stretchImageMinMax(int min, int max) {
		BufferedImage stretched = blankCopy();
		ImageProcessor proc = new ImageProcessor(stretched);

		Iterator<Pixel> iter = proc.iterator();		
		while (iter.hasNext()) {
			Pixel localPixel = iter.next();
			localPixel.setGrey(transform(localPixel.getRed(), min, max));
		}
		return stretched;
	}
	
	/* lab 5 public functions */
	
	public BufferedImage equalize() {
		float[] lookupTable = getEqualizeArray();
		return mapLookup(lookupTable);
	}
	
	public BufferedImage histogramSpec(float[] histogram) {
		assert(histogram.length == NUM_COLORS);
		
		float[] outputLT = getLookupTable(histogram);
		float[] inputLT = getEqualizeArray();
		int[] totalLT = getMapping(inputLT, outputLT); 
		return mapLookup(totalLT);
	}
	
	public static void normalize(float[] array) {
		float volume = 0;
		for (int i = 0; i < array.length; i++) {
			volume += array[i];
		}
		
		float normalFactor = 1f/volume;
		
		for (int i = 0; i < array.length; i++) {
			array[i] *= normalFactor;
		}
	}
	
	/* private functions */	
	
	private float[] getHistogram() {
		float[] histogram = new float[NUM_COLORS];
		float pixelWeight = 1f/(workingImage_.getWidth() 
								* workingImage_.getHeight());

		Iterator<Pixel> pixItter = iterator();
		while (pixItter.hasNext()) {
			int localShade = pixItter.next().getGrey();
			histogram[localShade] += pixelWeight;
		}
		return histogram;
	}
	
	private float[] getLookupTable(float[] histogram) {
		assert(histogram.length == NUM_COLORS);
		float[] lookupTable = new float[NUM_COLORS];
		float sum = 0;
		
		for(int i = 0; i < NUM_COLORS; i++) {
			sum += histogram[i];
			lookupTable[i] = sum*(NUM_COLORS - 1);
		}

		return lookupTable;
	}
	
	private BufferedImage mapLookup(float[] lookupTable) {
		BufferedImage output = blankCopy();
		
		Iterator<Pixel> iter = iterator();
		while(iter.hasNext()) {
			Pixel pix = iter.next();
			int localShade = pix.getGrey();
			Pixel result = new Pixel(output, pix.getX(), pix.getY());
			result.setGrey((int)lookupTable[localShade]);
		}
		return output;
	}
	
	private BufferedImage mapLookup(int[] lookupTable) {
		BufferedImage output = blankCopy();
		
		Iterator<Pixel> iter = iterator();
		while(iter.hasNext()) {
			Pixel pix = iter.next();
			int localShade = pix.getGrey();
			Pixel result = new Pixel(output, pix.getX(), pix.getY());
			result.setGrey(lookupTable[localShade]);
		}
		return output;
	}
	
	private float[] getEqualizeArray() {
		float[] histogram = getHistogram();
		float[] lookupTable = getLookupTable(histogram);
		return lookupTable;
	}
	
	private int[] getMapping(float[] inputLT, float[] outputLT) {
		assert(inputLT.length == outputLT.length);
		int[] maping = new int[inputLT.length];
		
		for (int i = 0; i < maping.length; i++) {
			maping[i] = closestIndex(outputLT, inputLT[i]);
		}
		return maping;
	}
	
	private int closestIndex(float[] array, float value) {
		float closest = array[0];
		int closestIndex = 0;
		for (int i = 0; i < array.length; i++) {
			if (Math.abs(array[i] - value) < Math.abs(array[i] - closest)) {
				closest = array[i];
				closestIndex = i;
			}
		}
		return closestIndex;
	}
	
	/**
	 * Normalize every element in the array to the range [0,1]
	 * @param data. the array to normalize
	 * @return the array, normalized 
	 */
	private float[][] normalizeArray(float[][] data) {
		float min = Float.MAX_VALUE;
		float max = Float.MIN_VALUE;
		
		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < data[0].length; j++) {
				float x = data[i][j];
				if (x < min) {
					min = x;
				}
				if (x > max) {
					max = x;
				}
			}
		}
		
		float offset = 0 - min;
		float multiplier = 1f/(max-min);
		
		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < data[0].length; j++) {
				data[i][j] = (data[i][j] + offset) * multiplier;
			}
		}
		return data;
	}
}
