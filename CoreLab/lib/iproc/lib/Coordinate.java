package iproc.lib;

/**
 * A coordinate is any generic object that has an x and y position.
 * @author eric
 */
public abstract class Coordinate {
	/**
	 * Gets the x value of the object.
	 * @return the x value of the objec.
	 */
	public abstract int getX();
	
	/**
	 * Gets the y value of the object.
	 * @return the y value of the objec.
	 */
	public abstract int getY();
	
	/**
	 * 'Virtual' copy constructor
	 */
	public abstract Coordinate makeCopy();
}
