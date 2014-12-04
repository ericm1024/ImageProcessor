package iproc;

import java.util.EnumMap;

public class SpaceTree <T extends Coordinate > {
		
	/** 
	 * Corner adjacency means something is couted as adjacent even if it
	 * only shares a corner with something else. Ex
	 * if AdjacencyType is CORNERS, the following X's are considered adjacent
	 * to R
	 *     |X|X|X|
	 *     |X|R|X|
	 *     |X|X|X|
	 *     
	 * if the AdjacencyType is NO_CORNERS, the following are considered adjacent
	 *     | |X| |
	 *     |X|R|X|
	 *     | |X| |
	 */
	public enum AdjacencyType {CORNERS, NO_CORNERS};
	
	/**
	 * Constructor that allows adjacency type selection.
	 * @param adj   the adjacency type.
	 */
	SpaceTree(AdjacencyType adj) {
		adjType = adj;
	}
	
	/**
	 * Insert a new element into the SpaceTree
	 * @param elem    The element to insert.
	 */
	public void insert(T elem) {
		if (root == null) {
			root = new Node(elem);
			return;
		}
		
		Node current = root;
		Quadrant quad;
		for (quad = current.relativePosition(elem);
			 current.hasChild(quad);
			 current = current.getChild(quad),
			 quad = current.relativePosition(elem)) {
			;
		}
		current.setChild(quad, elem);
		size++;
	}
	
	/**
	 * Determines if a tree contains an element.
	 * @param elem
	 * @return true if elem exists in the SpaceTree, else false
	 */
	public Boolean contains(T elem) {
		if (root == null) {
			return false;
		}
		
		Node current = root;
		Quadrant quad = current.relativePosition(elem);
		while (current.hasChild(quad)) {
			if (elem.equals(current.getChild(quad))) {
				return true;
			}
			current = current.getChild(quad);
			quad = current.relativePosition(elem);
		}
		return false;
	}
	
	/**
	 * Determines if an element is adjacent to any elements in the SpaceTree.
	 * @param elem   The element under consideration.
	 * @return True if the element is adjacent to some element in the tree.
	 */
	public Boolean adjacent(T elem) {
		if (root == null) {
			return false;
		}
		
		return root.adjacent(elem);
	}
	
	/**
	 * The number of elements in the SpaceTree
	 * @return positive integer size.
	 */
	public int size() {
		return size;
	}
	
	/** 
	 * The quadrant denotes what quadrant a child is in relative to the
	 * parent. i.e. if we constructed a cartesian coordinate plane with
	 * the parent at the center, what quadrant would the child lie in?
	 *  
	 * For reference:
	 *       |
	 *   Q2  |  Q1
	 * ------P------
	 *   Q3  |  Q4
	 *       |
	 * (think of going around a unit circle)
	 */
	private enum Quadrant {QUAD_ONE, QUAD_TWO, QUAD_THREE, QUAD_FOUR}; 
	
	/**
	 * Private node class for SpaceTree. The tree is composed of these
	 */
	private class Node {
		T value = null;
		EnumMap<Quadrant, Node> children = null;
		
		/**
		 * Creates a node whose value is pix.
		 * @param pix
		 */
		@SuppressWarnings("unchecked")
		Node(T elem) {
			children = new EnumMap<Quadrant,Node>(Quadrant.class);
			value = (T)elem.makeCopy();
		}
		
		/**
		 * Returns the quadrant that elem lies in with respect to parent.
		 * @param elem  the element being considered 
		 * @return The quadrant that elem lies in relative to the
		 * member variable 'value'.
		 */
		private Quadrant relativePosition(T elem) {
			double theta = Math.atan2(elem.getY() - value.getY(), 
					elem.getX() - value.getX());
			
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
		
		/**
		 * Gets the child node in the given quadrant.
		 * @param quad   The quadrant of the desired child node.
		 * @return The child node in quadrant 'quad' relative to the member
		 * variable 'value'
		 */
		private Node getChild(Quadrant quad) {
			return children.get(quad);
		}
		
		/**
		 * Sets the child node in a given quadrant.
		 * @param quad   The quadrant of the new element.
		 * @param elem   The element to isnert
		 */
		private void setChild(Quadrant quad, T elem) {
			if (!hasChild(quad)) {
				children.put(quad, new Node(elem));
			}
		}
		
		/**
		 * Determines if a node has a child in a given quadrant.
		 * @param quad   The quadrant under consideration.
		 * @return true if Node has a child in the given quadrant, else false
		 */
		private Boolean hasChild(Quadrant quad) {
			return children.containsKey(quad);
		}
		
		/**
		 * The meat of the adjacency algorithm. This algorithm is pure recursive.
		 * @param elem
		 * @return true if the elem is adjacent to an element in this's subtree,
		 * otherwise false.
		 * 
		 * There are a number of special cases. We would like to just recurse 
		 * into the quadrant that elem would go in, but it is possible that 
		 * elem is on the border of two quadrants, or, depending on the type of
		 * adjacency under consideration (AdjacencyType), three. This is 
		 * obviously a little messy, but this function is the key to this data
		 * structure.
		 *   
		 * Graphically, the quadrants around a node (labeled R) looke like this:
		 *   
 		 *  |2|2|2|2|1|1|1|
 		 *  |2|2|2|2|1|1|1|
 		 *  |2|2|2|2|1|1|1|
 		 *  |3|3|3|R|1|1|1|
 		 *  |3|3|3|4|4|4|4|
 		 *  |3|3|3|4|4|4|4|
 		 *  |3|3|3|4|4|4|4|
 		 * 
 		 * If we're using NO_CORNERS adjacency, this gives the following edges:
 		 * 
 		 *  | | | |2|1| | |
 		 *  | | | |2|1| | |
 		 *  |2|2|2|2|1| | |
 		 *  |3|3|3|R|1|1|1|
 		 *  | | |3|4|4|4|4|
 		 *  | | |3|4| | | |
 		 *  | | |3|4| | | |
 		 * 
 		 *  If R is the value of the current node and E is the elemnt under
 		 *  consideration, the edges are determined by the following 
 		 *  conditions:
 		 *  
 		 *  Q1-Q2 edge: (R.x <= E.x && E.x <= R.x + 1 && R.y < E.y)
 		 *  
 		 *  Q2-Q3 edge: (R.x > E.x  && R.y <= E.y && E.y <= R.y + 1) 
 		 *  
 		 *  Q3-Q4 edge: (R.x - 1 <= E.x && E.x <= R.x && R.y > E.y)
 		 *  
 		 *  Q4-Q1 edge: (R.x < E.x && R.y - 1 <= E.y && E.y <= R.y)
 		 *  
 		 *  If we are using CORNERS adjacency, we have the following
 		 *  special cases where the element under consideration could be
 		 *  adjacent to a node in one of three quadrants. (Denoted with *'s)
 		 *  
 		 *  | | | |2|1| | |
 		 *  | | | |2|1| | |
 		 *  |2|2|2|*|1| | |
 		 *  |3|3|*|R|*|1|1|
 		 *  | | |3|*|4|4|4|
 		 *  | | |3|4| | | |
 		 *  | | |3|4| | | | 
 		 *  
 		 *  However, note that all of these are adjacent to R, so we don't
 		 *  actually need to consider these cases separately.
		 */
		private Boolean adjacent(T elem) {
			// base cases
			if (value.equals(elem) && adjacentNodes(value, elem)) {
				return true;
			} else if (children.size() == 0) {
				return false;
			}
			
			int rx = value.getX(); // root's x
			int ry = value.getY(); // root's y
			int ex = elem.getX();  // elem's x
			int ey = elem.getY();  // elem's y
			
			if (rx <= ex && ex <= rx + 1 && ry < ey) {
				// Q1 - Q2 edge
				return getChild(Quadrant.QUAD_ONE).adjacent(elem)
						|| getChild(Quadrant.QUAD_TWO).adjacent(elem);
			} else if (rx > ex  && ry <= ey && ey <= ry + 1) {
				// Q2 - Q3 edge
				return getChild(Quadrant.QUAD_TWO).adjacent(elem)
						|| getChild(Quadrant.QUAD_THREE).adjacent(elem);
			} else if (rx - 1 <= ex && ex <= rx && ry > ey) {
				// Q3 - Q4 edge
				return getChild(Quadrant.QUAD_THREE).adjacent(elem)
						|| getChild(Quadrant.QUAD_FOUR).adjacent(elem);
			} else if (rx < ex && ry - 1 <= ey && ey <= ry) {
				// Q4 - Q1 edge
				return getChild(Quadrant.QUAD_FOUR).adjacent(elem)
						|| getChild(Quadrant.QUAD_ONE).adjacent(elem);
			} else {
				return getChild(relativePosition(elem)).adjacent(elem);
			}
		}
		
		/**
		 * Determines if two pixels are adjacent. The paramaters for Adjacency 
		 * are determined by the adjacency type specified at construction.
		 * @param a the first pixel
		 * @param b the second pixel
		 * @return True if the two pixels are adjacent, false otherwise.
		 */
		private Boolean adjacentNodes(T a, T b) {
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
	
	/* private data members of SpaceTree: */
	
	private Node root = null;
	private int size = 0;
	private AdjacencyType adjType = AdjacencyType.CORNERS;
}
