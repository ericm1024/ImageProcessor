public class HSI extends ThreeTuple {
	
	/* constructors */
	
	public HSI(double h, double s, double i) {
		super(h, s, i);
	}
	
	
	public HSI() {
		super(0.0, 0.0, 0.0);
	}
	
	
	/* getters */
	
	public double H(){
		return super.getFirst();
	}
	
	
	public double S(){
		return super.getSecond();
	}
	
	
	public double I(){
		return super.getThird();
	}
	
	
	/* setters */
	
	public void H(double x) {
		super.setFirst(x);
	}
	
	
	public void S(double x) {
		super.setSecond(x);
	}
	
	
	public void I(double x) {
		super.setThird(x);
	}
}
