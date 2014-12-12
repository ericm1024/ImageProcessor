package labs;

import iproc.ImageProcessor;
import iproc.Pixel;

import java.awt.image.BufferedImage;
import java.io.File;

import org.jtransforms.fft.DoubleFFT_2D;

public class LabEleven {
	public static String WORK_DIR = "/Users/eric/Desktop/mudd_fall2014/lab/11/";
	public static String OUT_DIR = WORK_DIR+"out/";
	private static File PANDA = new File(WORK_DIR+"panda.png");
	
	private ImageProcessor proc = null;
	private String name = "panda";
	
	public static void main(String args[]) {
		LabEleven lab = new LabEleven();
		lab.proc = new ImageProcessor(PANDA);
		lab.problemOne();
		lab.problemTwo();
	}
	
	// write original panda & spectrum to images
	private void problemOne() {
		double[][] transPanda = fftShift(pandaFft());
		BufferedImage spectrum = proc.blankCopy();
		
		Pixel worker = new Pixel(spectrum);
		for (int y = 0; y < transPanda.length; y++) {
			for (int x = 0; x <transPanda[0].length; x += 2) {
				worker.moveTo(y, x/2);
				double val = Math.hypot(transPanda[y][x], transPanda[y][x+1]);
				worker.setGrey((int)Math.pow(val, 0.4));
			}
		}
		
		proc.writeWorkingImage(new File(OUT_DIR+name+"-original.png"));
		ImageProcessor.writeImage(spectrum, new File(OUT_DIR+name+"-spectrum.png"));
		System.out.println("done.");
	}
	
	// idealLP, idealHP, butterworthLP, butterworthHP
	private void problemTwo() {
		double[][] idealLp = fftShift(pandaFft());
		double[][] idealHp = fftShift(pandaFft());
		double[][] butterLp = fftShift(pandaFft());
		double[][] butterHp = fftShift(pandaFft());
		
		// do filtering
		idealLpFilter(idealLp, 5);
		idealHpFilter(idealHp, 5);
		butterworthLpFilter(butterLp, 5, 5);
		butterworthHpFilter(butterHp, 5, 5);
		
		// reverse the transform
		reverseFft(fftShift(idealLp));
		reverseFft(fftShift(idealHp));
		reverseFft(fftShift(butterLp));
		reverseFft(fftShift(butterHp));
		
		// write the images to files
		ImageProcessor.writeImage(imgFromComplex(idealLp), new File(
				OUT_DIR+name+"ideal-low-pass.png"));
		ImageProcessor.writeImage(imgFromComplex(idealHp), new File(
				OUT_DIR+name+"ideal-high-pass.png"));
		ImageProcessor.writeImage(imgFromComplex(butterLp), new File(
				OUT_DIR+name+"butterworth-low-pass.png"));
		ImageProcessor.writeImage(imgFromComplex(butterHp), new File(
				OUT_DIR+name+"butterworth-high-pass.png"));
		System.out.println("done.");
	}

	private double[][] pandaFft() { /* http://imgur.com/mih8b */
		int h = proc.getImage().getHeight();
		int w = proc.getImage().getWidth();
		DoubleFFT_2D fft = new DoubleFFT_2D(h,w);
		double[][] imageData = new double[h][2*w];
		
		for (Pixel p : proc) {
			imageData[p.getY()][p.getX()*2] = p.getGrey();
		}
		fft.complexForward(imageData);
		return imageData;
	}
	
	private double[][] reverseFft(double[][] spectrum) {
		int h = spectrum.length;
		int w = spectrum[0].length/2;
		DoubleFFT_2D fft = new DoubleFFT_2D(h,w);
		fft.complexInverse(spectrum, true);
		return spectrum;
	}
	
	// emulate the matlab function (in place)
	private double[][] fftShift(double[][] data) {
		assert(data.length%2 == 0);
		assert(data[0].length%2 == 0);
	
		int halfHeight = data.length/2;
		int halfWidth = data[0].length/2;
		// swap upper left quadrant with bottom right
		for (int r = 0; r < halfHeight; r++) {
			for (int c = 0; c < halfWidth; c++) {
				double tmp = data[r][c];
				data[r][c] = data[r + halfHeight][c + halfWidth];
				data[r + halfHeight][c + halfWidth] = tmp;
			}
		}
		
		// swap the upper right quadrand with the bottom left
		for (int r = 0; r < halfHeight; r++) {
			for (int c = halfWidth; c < halfWidth*2; c++) {
				double tmp = data[r][c];
				data[r][c] = data[r + halfHeight][c - halfWidth];
				data[r + halfHeight][c - halfWidth] = tmp;
			}
		}
		return data;
	}
	
	private void idealLpFilter(double[][] data, double thresh) {
		int height = data.length;
		int width = data[0].length;
		
		for (int r = 0; r < height; r++) {
			for (int c = 0; c < width; c+=2) {
				int u = r - height/2;
				int v = (c - width/2)/2;
				if (Math.hypot(u,v) >= thresh) {
					data[r][c] = 0;
					data[r][c+1] = 0;
				}
			}
		}
	}
	
	private void idealHpFilter(double[][] data, double thresh) {
		int height = data.length;
		int width = data[0].length;
		
		for (int r = 0; r < height; r++) {
			for (int c = 0; c < width; c+=2) {
				int u = r - height/2;
				int v = (c - width/2)/2;
				if (Math.hypot(u,v) < thresh) {
					data[r][c] = 0;
					data[r][c+1] = 0;
				}
			}
		}
	}
	
	private void butterworthLpFilter(double[][] data, double thresh, int n) {
		int height = data.length;
		int width = data[0].length;
		
		for (int r = 0; r < height; r++) {
			for (int c = 0; c < width; c+=2) {
				int u = r - height/2;
				int v = (c - width/2)/2;
				
				double butter = 1.0/(1.0 + Math.pow((u*u + v*v)/(thresh*thresh), n));
				
				data[r][c] *= butter;
				data[r][c+1] *= butter;
			}
		}
	}
	
	private void butterworthHpFilter(double[][] data, double thresh, int n) {
		int height = data.length;
		int width = data[0].length;
		
		for (int r = 0; r < height; r++) {
			for (int c = 0; c < width; c+=2) {
				int u = r - height/2;
				int v = (c - width/2)/2;
				
				double butter = 1.0/(1.0 + Math.pow((u*u + v*v)/(thresh*thresh), n));
				
				data[r][c] *= (1.0 - butter);
				data[r][c+1] *= (1.0 - butter);
			}
		}
	}
	
	private BufferedImage imgFromComplex(double[][] data) {
		BufferedImage img = proc.blankCopy();
		Pixel worker = new Pixel(img);
		for (int y = 0; y < data.length; y++) {
			for (int x = 0; x < data[0].length; x += 2) {
				worker.moveTo(y, x/2);
				double val = Math.hypot(data[y][x], data[y][x+1]);
				worker.setGrey((int)val);
			}
		}
		return img;
	}
}
