package labs;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Iterator;

import iproc.ImageProcessor;
import iproc.Pixel;
import iproc.RawPixel;

public class LabTwoProcessor extends ImageProcessor {
	
	/* constructors */
	
	/**
	 * Basic constructor for an ImageProcessor.
	 */
	public LabTwoProcessor() {
		super();
	}
	
	/**
	 * Basic constructor for an ImageProcessor from a file
	 * 
	 * @param filename the name of the file to read from.
	 */
	public LabTwoProcessor(File imageFile) {
		super(imageFile);
	}
	
	/**
	 * Basic constructor for an ImageProcessor from a file
	 * 
	 * @param filename the name of the file to read from.
	 */
	public LabTwoProcessor(BufferedImage image) {
		super(image);
	}
	
	/* public functions */
	
	public void resize(double xRatio, double yRatio) {
		int newWidth = (int)((double)workingImage_.getWidth()*xRatio); 
		int newHeight = (int)((double)workingImage_.getHeight()*yRatio);
		BufferedImage result = new BufferedImage(newWidth, newHeight, imageType_);
		
		resizeWorkingTo(result);
	}
	
	public void rotate(double theta) {
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
			double sourceX = (double)destX
							* (double)(workingImage_.getWidth() - 1)
							/ (double)(output.getWidth() - 1);
			
			double sourceY = (double)destY
							* (double)(workingImage_.getHeight() - 1)
							/ (double)(output.getHeight() - 1);
			
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
			double u = sourceX % 1.0;
			double v = sourceY % 1.0;
			
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
	private void rotateWorkingTo(BufferedImage output, double theta) {
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
			double sourceX = ( Math.cos(theta)*(destX - xCenter) +
					    Math.sin(theta)*(destY - yCenter) + xCenter); 
			double sourceY = (-Math.sin(theta)*(destX - xCenter) + 
					    Math.cos(theta)*(destY - yCenter) + yCenter); 
			
			/* find the remainders */
			double u = sourceX % 1;
			double v = sourceY % 1;
			
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
}
