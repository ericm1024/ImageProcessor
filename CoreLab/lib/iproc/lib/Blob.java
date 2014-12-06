package iproc.lib;

import iproc.Pixel;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;

public class Blob {
	public Blob(Pixel root, int thresh) {
		members = new SpaceTree<Pixel>();
		others = new ArrayList<>();
		Deque<Pixel> queue = new ArrayDeque<Pixel>();
		
		members.insert(root);
		queue.addLast(root);
		
		while (!queue.isEmpty()) {
			Pixel pix = queue.pop();
			if (members.adjacent(pix)) {
				members.insert(pix);
				queue = getNewHighNeighbors(queue, pix, thresh);
			}
		}
	}
	
	private Deque<Pixel> getNewHighNeighbors(Deque<Pixel> queue, 
			Pixel center, int thresh) {
		
		Pixel pix = new Pixel(center);
		int window = 3;
		
		for (int i = -(window/2); i < window/2; i++) {
			for (int j = -(window/2); j < window/2; j++) {
				int x = i + center.getX();
				int y = i + center.getY();
				
				if (!pix.inImage(x,  y) || members.contains(pix)) { 
					continue;
				}

				if (pix.moveTo(x,y).getGrey() >= thresh) {
					queue.addLast(new Pixel(pix));
				}
			}
		}
		
		return queue;
	}
	
	public Boolean adjacent(Pixel pix) {
		if (members.adjacent(pix)) {
			return true;
		}
		for (Blob b : others) {
			if (b.members.adjacent(pix)) {
				return true;
			}
		}
		return false;
	}
	
	public Boolean contains(Pixel pix) {
		if (members.contains(pix)) {
			return true;
		}
		for (Blob b : others) {
			if (b.members.contains(pix)) {
				return true;
			}
		}
		return false;
	}
	
	public void insert(Pixel pix) {
		members.insert(pix);
		return;
	}
	
	public void addOther(Blob other) {
		others.add(other);
	}
	
	private SpaceTree<Pixel> members = null;
	private ArrayList<Blob> others = null;
}
