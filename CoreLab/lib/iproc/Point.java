package iproc;

public class Point {
	public short x_;
	public short y_;
	
	public Point(short x, short y) {
		x_ = x;
		y_ = y;
	}
	
	public Point(int x, int y) {
		x_ = (short)x;
		y_ = (short)y;
	}
	
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof Point)) {
            return false;
		}
		
		Point rhs = (Point) other;
		return (x_ == rhs.x_ && y_ == rhs.y_);
	}
	
	@Override
	public int hashCode() {
		return (int) x_ | (int) y_ >> 16; 
	}
}
