package labs;

import iproc.ImageProcessor;
import iproc.Pixel;
import iproc.lib.Point;
import iproc.lib.RawPixel;

import java.io.File;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;

public class LabTen {
	public static String WORK_DIR = "/Users/eric/Desktop/mudd_fall2014/lab/10/";
	public static String OUT_DIR = WORK_DIR+"out/";
	
	private static File GUMBY = new File(WORK_DIR+"gumby2.png");
	
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

		ArrayList<Pixel> edge = trace(edgeMember);
		
		// set the edge to red
		for (Pixel p : edge) {
			p.setRed(RawPixel.INT_COLOR_MAX);
		}
		
		proc.writeWorkingImage(new File(OUT_DIR+name+"-edge.png"));
	}
	
	private static ArrayList<Pixel> trace(Pixel root) {
		ArrayList<Pixel> edge = new ArrayList<>();
		HashSet<Point> seen = new HashSet<>();
		Deque<Pixel> queue = new ArrayDeque<Pixel>();
		
		edge.add(root);
		seen.add(root.where());
		queue.addLast(root);
		
		while (!queue.isEmpty()) {
			Pixel pix = queue.pop();
			assert(!edge.contains(pix));
			edge.add(new Pixel(pix));
			queue = getNewNeighbors(queue, seen, pix);
		}
		return edge;
	}
	
	private static Deque<Pixel> getNewNeighbors(Deque<Pixel> queue, 
			HashSet<Point> seen, Pixel center) {
		
		Pixel pix = new Pixel(center);
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				int x = i + center.getX();
				int y = i + center.getY();
				
				if (!pix.inImage(x, y) || (i==0 && j==0)) { 
					continue;
				}
				
				pix.moveTo(x, y);
				if (!seen.contains(pix.where()) && pix.getGrey() != 0) {
					queue.addLast(new Pixel(pix));
					seen.add(pix.where());
				}
			}
		}	
		return queue;
	}
}
