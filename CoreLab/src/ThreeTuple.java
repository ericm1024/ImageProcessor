public class ThreeTuple {
	private double first_;
	private double second_;
	private double third_;
	
	public ThreeTuple(double a, double b, double c) {
		first_ = a;
		second_ = b;
		third_ = c;
	}
	
	public double getFirst(){
		return first_;
	}
	public double getSecond(){
		return second_;
	}
	public double getThird(){
		return third_;
	}
	
	public void setFirst(double x) {
		first_ = x;
	}
	public void setSecond(double x) {
		second_ = x;
	}
	public void setThird(double x) {
		third_ = x;
	}
}
