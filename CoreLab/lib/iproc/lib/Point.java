package iproc.lib;

public class Point extends iproc.Coordinate {
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
	
	@Override
	public int getX() {
		return x_;
	}
	
	@Override
	public int getY() {
		return y_;
	}
	
	@Override
	public iproc.Coordinate makeCopy() {
		return new Point(x_, y_);
	}
}
