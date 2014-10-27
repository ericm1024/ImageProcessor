import java.awt.image.BufferedImage;
import java.io.File;


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
	
/* lab two public functions */
	
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
		/* the four pixels we will work with for each interpolation*/
		Pixel a, b, c, d;
		
		/* where our new pixel should come from in the source */
		double sourceX;
		double sourceY;
		
		/* these correspond to u and v, respectively from the equations we were given */
		double u;
		double v;
		
		for (int destX = 0; destX < output.getWidth(); destX++) {
			for (int destY = 0; destY < output.getHeight(); destY++) {
				
				/* find where our source X and Y */
				sourceX = (double)destX*(double)(workingImage_.getWidth() - 1)/
								        (double)(output.getWidth() - 1);
				sourceY = (double)destY*(double)(workingImage_.getHeight() - 1)/
				                        (double)(output.getHeight() - 1);
				
				/* find the remainders */
				u = sourceX % 1;
				v = sourceY % 1;
				
				/* reset all of working pixels. The array indicies are given in the assignment */
				try {
					a = new Pixel(workingImage_.getRGB((int)sourceX, (int)sourceY));
				} catch (ArrayIndexOutOfBoundsException e) {
					a = new Pixel();
				}
				try {
					b = new Pixel(workingImage_.getRGB((int)sourceX+1, (int)sourceY));
				} catch (ArrayIndexOutOfBoundsException e) {
					b = new Pixel();
				}
				try {
					c = new Pixel(workingImage_.getRGB((int)sourceX, (int)sourceY+1));
				} catch (ArrayIndexOutOfBoundsException e) {
					c = new Pixel();
				}
				try {
					d = new Pixel(workingImage_.getRGB((int)sourceX+1, (int)sourceY+1));
				} catch (ArrayIndexOutOfBoundsException e) {
					d = new Pixel();
				}		
				
				/* scale the pixels based on the algorithm we were given */
				a.scale(1 - v - u - u*v);
				b.scale(u - u*v);
				c.scale(v - u*v);
				d.scale(u*v);
				
				/* 
				 * set the corresponding pixel in the output to the sum of all the contributing
				 * components from the original image
				 */
				output.setRGB(destX, destY, a.add(b).add(c).add(d).getRGB());
			}
		}
		workingImage_ = output;
	}
	
	
	/**
	 * @param output the BufferedImage to write output to.
	 */
	private void rotateWorkingTo(BufferedImage output, double theta) {
		/* the four pixels we will work with for each interpolation*/
		Pixel a, b, c, d;
		
		/* where our new pixel should come from in the source */
		double sourceX;
		double sourceY;
		
		/* these correspond to u and v, respectively from the equations we were given */
		double u;
		double v;
		
		/* the center of the image */
		final int xCenter = workingImage_.getWidth()/2;
		final int yCenter = workingImage_.getHeight()/2;
		
		for (int destX = 0; destX < output.getWidth(); destX++) {
			for (int destY = 0; destY < output.getHeight(); destY++) {
				
				/* find where our source X and Y are (algorithm given in assignment) */
				sourceX = ( Math.cos(theta)*(destX - xCenter) +
						    Math.sin(theta)*(destY - yCenter) + xCenter); 
				sourceY = (-Math.sin(theta)*(destX - xCenter) + 
						    Math.cos(theta)*(destY - yCenter) + yCenter); 
				
				/* find the remainders */
				u = sourceX % 1;
				v = sourceY % 1;
				
				/* reset all of working pixels. The array indicies are given in the assignment */
				try {
					a = new Pixel(workingImage_.getRGB((int)sourceX, (int)sourceY));
				} catch (ArrayIndexOutOfBoundsException e) {
					a = new Pixel();
				}
				try {
					b = new Pixel(workingImage_.getRGB((int)sourceX+1, (int)sourceY));
				} catch (ArrayIndexOutOfBoundsException e) {
					b = new Pixel();
				}
				try {
					c = new Pixel(workingImage_.getRGB((int)sourceX, (int)sourceY+1));
				} catch (ArrayIndexOutOfBoundsException e) {
					c = new Pixel();
				}
				try {
					d = new Pixel(workingImage_.getRGB((int)sourceX+1, (int)sourceY+1));
				} catch (ArrayIndexOutOfBoundsException e) {
					d = new Pixel();
				}		
				
				/* scale the pixels based on the algorithm we were given */
				a.scale(1 - v - u - u*v);
				b.scale(u - u*v);
				c.scale(v - u*v);
				d.scale(u*v);
				
				/* 
				 * set the corresponding pixel in the output to the sum of all the contributing
				 * components from the original image
				 */
				output.setRGB(destX, destY, a.add(b).add(c).add(d).getRGB());
			}
		}
		workingImage_ = output;
	}
	
}
