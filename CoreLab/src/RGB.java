public class RGB extends ThreeTuple {
	
	/* constructors */
	
	public RGB(double r, double g, double b) {
		super(r, g, b);
	}
	
	
	public RGB() {
		super(0.0, 0.0, 0.0);
	}
	
	
	/* getters */
	
	public double R(){
		return super.getFirst();
	}
	
	
	public double G(){
		return super.getSecond();
	}
	
	
	public double B(){
		return super.getThird();
	}
	
	
	/* setters */
	
	public void R(double r) {
		super.setFirst(r);
	}
	
	
	public void G(double g) {
		super.setSecond(g);
	}
	
	
	public void B(double b) {
		super.setThird(b);
	}
}
