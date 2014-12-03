package labs;

import iproc.ImageProcessor;
import iproc.Pixel;
import iproc.lib.RawPixel;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;

public class LabTen {
	public static String WORK_DIR = "/Users/eric/Desktop/mudd_fall2014/lab/10/";
	public static String OUT_DIR = WORK_DIR+"out/";
	
	private static File GUMBY = new File(WORK_DIR+"gumby.png");
	
	public static void main(String args[]) {
		traceEdge(GUMBY, "gumby");
	}
	
	private static void traceEdge(File starter, String name) {
		ImageProcessor proc = new ImageProcessor(starter);
		proc.trim(2);
		Pixel edgeMember = null;
		
		// find first pixel
		for (Pixel p : proc) {
			if (p.getGrey() != 0) {
				edgeMember = p;
				break;
			}
		}
		
		// traverse the rest
		HashSet<Pixel> seen = new HashSet<>();
		ArrayList<Pixel> edge = new ArrayList<>();
		while (edgeMember != null) {
			seen.add(edgeMember);
			edge.add(edgeMember);
			edgeMember = newNeighborOf(edgeMember, seen);
		}
		
		// set the edge to red
		for (Pixel p : edge) {
			p.setRed(RawPixel.INT_COLOR_MAX);
		}
		
		proc.writeWorkingImage(new File(OUT_DIR+name+"-edge.png"));
	}
	
	/**
	 * @param pix
	 * @return
	 */
	private static Pixel newNeighborOf(Pixel pix, HashSet<Pixel> seen) {
		
		Pixel neighbor = null;
		int srcX = pix.getX();
		int srcY = pix.getY();
		int window = 3;
		
	search:
		for (int x = -(window/2); x < window/2; x++) {
			for (int y = -(window/2); y < window/2; y++) {
				if (!pix.inImage(srcX + x,  srcY + y)) { 
					continue;
				}
				pix.moveTo(srcX + x, srcY + y);
				if (!seen.contains(pix) && pix.getGrey() != 0) {
					neighbor = new Pixel(pix);
					break search;
				}
			}
		}
	
		// move the pixel back where we found it
		pix.moveTo(srcX, srcY);
		return neighbor;
	}
}
