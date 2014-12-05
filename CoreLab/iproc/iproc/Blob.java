package iproc;

import java.util.ArrayDeque;
import java.util.Deque;

public class Blob {
	public Blob(Pixel root, int thresh) {
		members = new SpaceTree<Pixel>();
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
		return members.adjacent(pix);
	}
	
	public Boolean contains(Pixel pix) {
		return members.contains(pix);
	}
	
	public void insert(Pixel pix) {
		members.insert(pix);
		return;
	}
	
	private SpaceTree<Pixel> members = null;
}
