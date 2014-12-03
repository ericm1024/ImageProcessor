package iproc;

import java.util.EnumMap;

public class SpaceTree {
		
	/** 
	 * Corner adjacency means something is couted as adjacent even if it
	 * only shares a corner with something else. Ex
	 * if AdjacencyType is CORNERS, the following X's are considered adjacent
	 * to Y
	 *     |X|X|X|
	 *     |X|Y|X|
	 *     |X|X|X|
	 *     
	 * if the AdjacencyType is NO_CORNERS, the following are considered adjacent
	 *     | |X| |
	 *     |X|Y|X|
	 *     | |X| |
	 */
	public enum AdjacencyType {CORNERS, NO_CORNERS};
	
	public void insert(Pixel pix) {
		if (root == null) {
			root = new Node(pix);
			return;
		}
		
		Node current = root;
		Quadrant quad;
		for (quad = current.relativePosition(pix);
			 current.hasChild(quad);
			 current = current.getChild(quad),
			 quad = current.relativePosition(pix)) {
			;
		}
		current.setChild(quad, pix);
	}
	
	public Boolean contains(Pixel pix) {
		if (root == null) {
			return false;
		}
		
		Node current = root;
		Quadrant quad = current.relativePosition(pix);
		while (current.hasChild(quad)) {
			if (pix.equals(current.getChild(quad))) {
				return true;
			}
			current = current.getChild(quad);
			quad = current.relativePosition(pix);
		}
		return false;
	}
	
	public Boolean adjacent(Pixel pix) {
		if (root == null) {
			return false;
		}
		
		Node current = root;
		Quadrant quad;
		do {
			if (adjacent(current.value, pix)) {
				return true;
			}
			quad = current.relativePosition(pix);
			current = current.getChild(quad);
		} while (current != null);
		
		return false;
	}
	
	/* For reference:
	 *       |
	 *   Q2  |  Q1
	 * ------|------
	 *   Q3  |  Q4
	 *       |
	 * (think of going around a unit circle)
	 */
	private enum Quadrant {QUAD_ONE, QUAD_TWO, QUAD_THREE, QUAD_FOUR}; 
	
	/**
	 * Private node class for Blob. The entries in the blob's tree are 
	 * composed of these. 
	 * @author eric
	 */
	private class Node {
		Pixel value = null;
		EnumMap<Quadrant, Node> children = null;
		
		/**
		 * Creates a node whose value is pix.
		 * @param pix
		 */
		Node(Pixel pix) {
			children = new EnumMap<Quadrant,Node>(Quadrant.class);
			value = new Pixel(pix);
		}
		
		private Quadrant relativePosition(Pixel insertee) {
			double theta = Math.atan2(insertee.getY() - value.getY(), 
					insertee.getX() - value.getX());
			
			// theta ranges from -pi to pi (see the docs)
			if (theta < -Math.PI/2 || theta == Math.PI) { // fuck floats
				return Quadrant.QUAD_THREE;
			} else if (theta < Math.PI) {
				return Quadrant.QUAD_FOUR;
			} else if (theta < Math.PI/2) {
				return Quadrant.QUAD_ONE;
			} else {
				return Quadrant.QUAD_TWO;
			}
		}
		
		private Node getChild(Quadrant quad) {
			return children.get(quad);
		}
		
		private void setChild(Quadrant quad, Pixel pix) {
			if (!hasChild(quad)) {
				children.put(quad, new Node(pix));
			}
		}
		
		private Boolean hasChild(Quadrant quad) {
			return children.containsKey(quad);
		}
	}
	
	private Node root = null;
	private int size = 0;
	private AdjacencyType adjType = AdjacencyType.CORNERS;
	
	/**
	 * Determines if two pixels are adjacent. The paramaters for Adjacency 
	 * are determined by the adjacency type specified at construction.
	 * @param a the first pixel
	 * @param b the second pixel
	 * @return True if the two pixels are adjacent, false otherwise.
	 */
	private Boolean adjacent(Pixel a, Pixel b) {
		if (adjType == AdjacencyType.CORNERS) {
			return Math.abs(a.getX() - b.getX()) <= 1 
			       &&  Math.abs(a.getY() - b.getY()) <= 1; 
		} else if (adjType == AdjacencyType.NO_CORNERS) {
			return (a.getX() == b.getX() && Math.abs(a.getY() - b.getY()) <= 1)
				   ||  (a.getY() == b.getY() && Math.abs(a.getX() - b.getX()) <= 1);
		} else {
			System.err.println("SpaceTree: adjacent: got unknown adjacency type.");
			return false;
		}
	}
}
