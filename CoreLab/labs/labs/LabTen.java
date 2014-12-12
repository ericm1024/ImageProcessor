package labs;

import iproc.ImageProcessor;
import iproc.Pixel;
import iproc.lib.Point;
import iproc.lib.RawPixel;

import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;

import org.jtransforms.fft.DoubleFFT_1D;

public class LabTen {
	public static String WORK_DIR = "/Users/eric/Desktop/mudd_fall2014/lab/10/";
	public static String OUT_DIR = WORK_DIR+"out/";
	
	private static File GUMBY = new File(WORK_DIR+"gumby2.png");
	
	private ImageProcessor proc = null;
	
	public static void main(String args[]) {
		LabTen lab = new LabTen();
		lab.proc = new ImageProcessor(GUMBY);
		lab.problemOne("gumby");
		lab.problemTwo("gumby");
		lab.problemThree("gumby");
		lab.problemFour("gumby");
		lab.problemFive("gumby");
	}
	
	// single pixel gumby
	private void problemOne(String name) {
		ArrayList<Pixel> edge = traceEdge(name);
		for (Pixel p : edge) {
			p.setGrey(0);
			p.setRed(255);
		}
		
		RawPixel red = edge.get(0).get();
		for (Pixel p : proc) {
			if (!p.get().equals(red)) {
				p.setGrey(0);
			}
		}
		
		double energy = 0;
		for (Pixel p : edge) {
			energy += Math.hypot(p.getX(), p.getY());
			p.setGrey(RawPixel.INT_COLOR_MAX);
		}		
		
		proc.writeWorkingImage(new File(OUT_DIR+name+"-edge-energy-"+(int)energy+".png"));
	}
	
	// compute edge and save x and y coordinates
	private void problemTwo(String name) {
		ArrayList<Pixel> edge = traceEdge(name);
		
		int[] xVals = new int[edge.size()];
		int[] yVals = new int[edge.size()];
		
		for (int i = 0; i < edge.size(); i++) {
			Pixel pix = edge.get(i);
			xVals[i] = pix.getX();
			yVals[i] = pix.getY();
		}

		writeArrayToCsv(xVals, OUT_DIR+name+"-edge-x-values.csv");
		writeArrayToCsv(yVals, OUT_DIR+name+"-edge-y-values.csv");
	}
	
	// compute and save real and imaginary fourier coefficients
	private void problemThree(String name) {
		ArrayList<Pixel> edge = traceEdge(name);
		Complex[] complexEdge = new Complex[powerOf2Above(edge.size())];
		
		for (int i = 0; i < edge.size(); i++) {
			Pixel pix = edge.get(i);
			complexEdge[i] = new Complex(pix.getX(), pix.getY());
		}
		
		for (int i = edge.size() - 1; i < complexEdge.length; i++) {
			complexEdge[i] = new Complex(0, 0);
		}
		
		FastFourierTransformer fft = new FastFourierTransformer(DftNormalization.STANDARD);
		Complex[] transEdge = fft.transform(complexEdge, TransformType.FORWARD);
		writeArrayToCsv(transEdge, OUT_DIR+name+"-ft-coefficients-.csv");
	}
	
	// reconstruct with M = {1,2,...,20} components
	private void problemFour(String name) {
		ArrayList<Pixel> edge = traceEdge(name);
		double[] complexEdge = new double[edge.size()*2];
		
		for (int i = 0; i < edge.size(); i++) {
			Pixel pix = edge.get(i);
			complexEdge[2*i] = pix.getX();
			complexEdge[2*i + 1] = pix.getY();
		}
		
		DoubleFFT_1D fft = new DoubleFFT_1D(edge.size());
		fft.complexForward(complexEdge);
		double[] energies = new double[20];
		
		for (int numComponents = 1; numComponents <= 20; numComponents++) {			
			double[] reverse = new double[complexEdge.length];
			reverse[0] = complexEdge[0];
			reverse[1] = complexEdge[1];
			for (int i = 2; i < 2*numComponents + 2; i++) {
				reverse[i] = complexEdge[i];
				reverse[reverse.length - (i-1)] = complexEdge[complexEdge.length - (i-1)];
			}
			fft.complexInverse(reverse, true);
			BufferedImage img = proc.blankCopy();
			
			Pixel worker = new Pixel(img);
			for (int i = 0; i < reverse.length/2; i++) {
				if (worker.inImage((int)reverse[2*i], (int)reverse[2*i + 1]) ) {
					worker.moveTo((int)reverse[2*i], (int)reverse[2*i + 1]);
					worker.setGrey(RawPixel.INT_COLOR_MAX);
				}
			}
			
			double energy = 0;
			for (int i = 0; i < reverse.length/2; i++) {
				energy += Math.hypot(reverse[i*2], reverse[i*2+1]);
			}
			
			energies[numComponents-1] = energy;
			
			ImageProcessor.writeImage(img, new File(OUT_DIR+name+"-"+numComponents
					+"-components-energy-"+(int)energy+".png"));
		}
	}
	
	// reconstruct gumby using all components 
	private void problemFive(String name) {
		ArrayList<Pixel> edge = traceEdge(name);
		double[] complexEdge = new double[edge.size()*2];
		
		for (int i = 0; i < edge.size(); i++) {
			Pixel pix = edge.get(i);
			complexEdge[2*i] = pix.getX();
			complexEdge[2*i + 1] = pix.getY();
		}
		
		DoubleFFT_1D fft = new DoubleFFT_1D(edge.size());
		fft.complexForward(complexEdge);
		fft.complexInverse(complexEdge, true);
		
		BufferedImage img = proc.blankCopy();
		
		Pixel worker = new Pixel(img);
		for (int i = 0; i < complexEdge.length/2; i++) {
			if (worker.inImage((int)complexEdge[2*i], (int)complexEdge[2*i + 1]) ) {
				worker.moveTo((int)complexEdge[2*i], (int)complexEdge[2*i + 1]);
				worker.setGrey(RawPixel.INT_COLOR_MAX);
			}
		}
		
		double energy = 0;
		for (int i = 0; i < complexEdge.length/2; i++) {
			energy += Math.hypot(complexEdge[i*2], complexEdge[i*2+1]);
		}
		
		ImageProcessor.writeImage(img, new File(OUT_DIR+name+"-all-components-energy-"+(int)energy+".png"));
	}
	
	private int powerOf2Above(int num) {
		int logfloor = (int)(Math.log(num)/Math.log(2));
		logfloor += 1;
		return (int)Math.pow(2,logfloor);
	}
	
	private ArrayList<Pixel> traceEdge(String name) {
		proc.trim(2);
		Pixel edgeMember = null;
		
		// find first pixel
		for (Pixel p : proc) {
			if (p.getGrey() != 0) {
				edgeMember = p;
				break;
			}
		}

		return trace(edgeMember);
	}
	
	private enum Cardinal {
		NORTH, NORTH_EAST, EAST, SOUTH_EAST, 
		SOUTH, SOUTH_WEST, WEST, NORTH_WEST}; 
	
	private static ArrayList<Pixel> trace(Pixel root) {
		ArrayList<Pixel> edge = new ArrayList<>();
		Deque<Pixel> queue = new ArrayDeque<Pixel>();
		queue.addLast(root);
		
		while (!queue.isEmpty()) {
			Pixel pix = queue.pop();

			if (pixelCompletesEdge(pix,edge)) {
				edge.add(new Pixel(pix));
				break;
			} else if(onlyAdjacentWithEnd(pix, edge)) {
				edge.add(new Pixel(pix));
				
				Cardinal dir = edge.size() > 1 ? 
						getDirectionBetween(edge.get(edge.size() -2), pix)
						: getStartingDirection(root);
				
				queue = getNextCanidates(pix, dir);
			}
		}
		return edge;
	}
	
	/**
	 * Finds the set of pixels adjacent to pix in the direction 'last' that 
	 * are possible edge canidates (i.e. are in the bounds of the image and
	 * have a non-zero greyscale value). 
	 * 
	 * Specifically, it considers the pixels in the directions 
	 * 
	 * 		last, last +- pi/4, and last +- pi/2. 
	 * 
	 * For example if 'last' was NORTH, the pixels in the direction NORTH,
	 * NORTH_EAST, NORTH_WEST, EAST, and WEST will be considered. Furthermore,
	 * they will be considered in that order. We use this order so that the
	 * returned pixels will roughly be ordered from most-likely-to-be-an-edge
	 * to least-likely-to-be-an-edge. In essence, we're saying that a pixel
	 * that is in the direction of the edge is more likely to be an edge member
	 * than one that is not.
	 *  
	 * @param pix   The last pixel in the edge
	 * @param last  The direction of pix relative to the pixel before it in the
	 *              edge. This is roughly the local direction of the edge.
	 * @return
	 */
	private static Deque<Pixel> getNextCanidates(Pixel pix, Cardinal last) {
		Deque<Pixel> canidates = new ArrayDeque<Pixel>();
		Cardinal[] cardinals = Cardinal.values();
		
		/* Explanation of the array of magic numbers:
		 * We want the deque we return to contain canidate next-path members
		 * in the direction order last, last +- pi/4, and last +- pi/2.
		 * Adding each of the numbers in the array to the ordinal of 'last'
		 * and modding by 8 (the number of directions) will give us a number
		 * in the range [0,7], as desired. 
		 */
		for (int offset : new int[]{8,9,7,10,6}) {
			Cardinal dir = cardinals[(last.ordinal() + offset) % 8];
			Pixel neighbor = getCardinalNeighbor(pix, dir);
			if (neighbor != null && neighbor.getGrey() != 0) {
				canidates.add(neighbor);
			}
		}
		return canidates;
	}
	
	/**
	 * Gets the neighboring pixel of 'pix' in the cardinal direction 
	 * 'direction'.
	 * @param pix        The pixel whose neighbors will be considered.
	 * @param direction  The cardinal direction of the neighbor to get.
	 * @return The pixel in the direction 'direction' or null if no such
	 * pixel exists (i.e. if it's out of bounds).
	 */
	private static Pixel getCardinalNeighbor(Pixel pix, Cardinal direction) {
		int x = pix.getX();
		int y = pix.getY();
		
		switch(direction) {
		case NORTH:
			y += 1;
			break;
		case NORTH_EAST:
			x += 1;
			y += 1;
			break;
		case EAST:
			x += 1;
			break;
		case SOUTH_EAST:
			x += 1;
			y += -1;
			break;
		case SOUTH:
			y += -1;
			break;
		case SOUTH_WEST:
			x += -1;
			y += -1;
			break;
		case WEST:
			x += -1;
			break;
		case NORTH_WEST:
			x += -1;
			y += 1;
			break;
		}
		Pixel result = new Pixel(pix);
		if (result.inImage(x, y)) {
			return result.moveTo(x, y);
		} else {
			return null;
		}
	}
	
	/**
	 * This function is a bit of a hack -- we need a starting direction
	 * for the edge. This function finds the first neighboring pixel
	 * that is a valid edge canidate and returns its direction
	 * relative to start.
	 * @param start  The first pixel in the edge.
	 * @return The direction of the fisrt viable edge pixel, or null
	 * if no such canidate can be found.
	 */
	private static Cardinal getStartingDirection(Pixel start) {		
		for (Cardinal c : Cardinal.values()) {
			Pixel neighbor = getCardinalNeighbor(start,c);
			if (neighbor != null && neighbor.getGrey() != 0) {
				return c;
			}
		}
		return null;
	}
	
	/**
	 * Gets the cardinal direction that describes the relative location
	 * of head with respect to tail. i.e. the direction of a vector starting
	 * at tail and ending at head. Head and tail must be adjacent.
	 * @param tail   
	 * @param head
	 * @return Cardinal direction that describes the relative location
	 * of head with respect to tail. 
	 */
	private static Cardinal getDirectionBetween(Pixel tail, Pixel head) {
		assert(areAdjacent(tail.where(), head.where()));
		
		int dx = head.getX() - tail.getX();
		int dy = head.getY() - tail.getY();
		
		if (dx == 0) {
			if (dy == 0) {
				return null; // pixel and head are the same thing, caller fucked up
			} else if (dy == 1) {
				return Cardinal.NORTH;
			} else { // dy == -1
				return Cardinal.SOUTH;
			}
		} else if (dx == 1) {
			if (dy == 0) {
				return Cardinal.EAST;
			} else if (dy == 1) {
				return Cardinal.NORTH_EAST;
			} else { // dy == -1
				return Cardinal.SOUTH_EAST;
			}
		} else { // dx == -1
			if (dy == 0) {
				return Cardinal.WEST;
			} else if (dy == 1) {
				return Cardinal.NORTH_WEST;
			} else { // dy == -1
				return Cardinal.SOUTH_WEST;
			}
		}
	}
	
	/**
	 * Determines if the given pixel is only adjacent with pixels at the end
	 * of the edge. The end of the edge is considered to be the last two pixels
	 * in the edge. Returns true if the edge is empty, false if the pixel is
	 * not adjacent with the last pixel in the edge, and false if the pixel
	 * is adjacent with any pixels other than the last two pixels in the edge.
	 * @param pix   The canidate pixel check adjacency with.
	 * @param edge  The pixels currently in the edge.
	 * @return True if pix is only adjacent with pixels at the end of the edge.
	 */
	private static boolean onlyAdjacentWithEnd(Pixel pix, ArrayList<Pixel> edge) {
		// if it's not adjacent with the end pixel, return false.
		if (edge.size() > 0 && !areAdjacent(pix.where()
				, edge.get(edge.size()-1).where())) {
			return false;
		}
		
		for (int i = 0; i < edge.size() - 2; i++) {
			if (areAdjacent(pix.where(),edge.get(i).where())) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Determines if a pixel completes an edge. It is necessary to check this
	 * because we would otherwise throw out a pixel that was adjacent to 
	 * anything but the end of the edge. Thus function checks that pix is
	 * adjacent to the first and last pixel in the edge.
	 * @param pix   The canidate pixel to complete the edge.
	 * @param edge  The edge so far.
	 * @return True if the supplied pixel is adjacent to the first and last
	 * pixels in the edge.
	 */
	private static boolean pixelCompletesEdge(Pixel pix, ArrayList<Pixel> edge) {
		if (edge.size() < 5) { // totally not a hack to avoid weird cases with short edges...
			return false;
		}
		
		// adjacent to the first and last pixel in the edge
		return areAdjacent(pix.where(), edge.get(0).where())
				&& areAdjacent(pix.where(), edge.get(edge.size()-1).where());
	}
	
	/**
	 * Determines adjacency of two pixels. Two pixels are considered adjacent
	 * if they share an edge or a corner.
	 * @param center            one pixel
	 * @param possibleNeighbor  a possible neighbor
	 * @return True if center and possibleNeighbor are adjacent
	 */
	private static boolean areAdjacent(Point center, Point possibleNeighbor) {
		return Math.abs(center.getX() - possibleNeighbor.getX()) <= 1
			   && Math.abs(center.getY() - possibleNeighbor.getY()) <= 1;
	}
	
	private void writeArrayToCsv(int[] array, String filename) {
		StringBuilder sb = new StringBuilder();
		
		try (BufferedWriter br = new BufferedWriter(new FileWriter(filename))) {
			for (int i = 0; i < array.length; i++) {
				sb.append(i);
				sb.append(",");
				sb.append(array[i]);
				sb.append('\n');
			}
			br.write(sb.toString());
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void writeArrayToCsv(Complex[] array, String filename) {
		StringBuilder sb = new StringBuilder();
		
		try (BufferedWriter br = new BufferedWriter(new FileWriter(filename))) {
			for (int i = 0; i < array.length; i++) {
				sb.append(i);
				sb.append(",");
				sb.append(array[i].getReal());
				sb.append(",");
				sb.append(array[i].getImaginary());
				sb.append('\n');
			}
			br.write(sb.toString());
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
