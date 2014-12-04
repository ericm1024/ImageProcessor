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
	public SpaceTree(AdjacencyType adj) {
		adjType = adj;
	}
	
	/**
	 * Default constructor. Defaults adjacency type to CORNERS
	 */
	public SpaceTree() {
		adjType = AdjacencyType.CORNERS;
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
		return contains(elem.getX(), elem.getY());
	}
	
	/**
	 * Determines if an element is adjacent to any elements in the SpaceTree.
	 * @param elem   The element under consideration.
	 * @return True if the element is adjacent to some element in the tree.
	 */
	public Boolean adjacent(T elem) {
		int x = elem.getX();
		int y = elem.getY();
		
		switch(adjType) {
		case CORNERS:
			/* Check corder adjacencies:
			 * |X| |X|
			 * | |E| |
			 * |X| |X|
			 */
			if (contains(x+1, y+1) 
					|| contains(x-1, y+1) 
					|| contains(x+1, y-1) 
					|| contains(x-1, y-1)) {
				return true;
			} // else, fall through
		case NO_CORNERS:
			/* Check linear adjacencies:
			 * | |X| |
			 * |X|E|X|
			 * | |X| |
			 */
			if (contains(x, y+1) 
					|| contains(x, y-1) 
					|| contains(x+1, y) 
					|| contains(x-1, y)) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * 
	 */
	//public T closest 
	
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
			return relativePosition(elem.getX(), elem.getY());
		}
		
		/**
		 * Returns the quadrant that the given position is in,
		 * relative to Node.value
		 */
		private Quadrant relativePosition(int x, int y) {			
			int vx = value.getX();
			int vy = value.getY();
			
			if (vx == x || vy == y) {
				System.out.println("iproc.SpaceTree.Node.relativePosition(): got invalid position.");
			}
			
			if (x > vx && y >= vy) {
				return Quadrant.QUAD_ONE;
			} else if (x <= vx && y > vy) {
				return Quadrant.QUAD_TWO;
			} else if (x < vx && y <= vy) {
				return Quadrant.QUAD_THREE;
			} else { // (x >= vx && y < vy) 
				return Quadrant.QUAD_FOUR;
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
	}
	
	/* private data members of SpaceTree: */
	
	private Node root = null;
	private int size = 0;
	private AdjacencyType adjType = AdjacencyType.CORNERS;
	
	private Boolean contains(int x, int y) {
		if (root == null) {
			return false;
		}
		
		Node current = root;
		Quadrant quad = current.relativePosition(x,y);
		while (current.hasChild(quad)) {
			Node child = current.getChild(quad);
			if (child.value.getX() == x && child.value.getY() == y) {
				return true;
			}
			current = child;
			quad = current.relativePosition(x,y);
		}
		return false;
	}
}
