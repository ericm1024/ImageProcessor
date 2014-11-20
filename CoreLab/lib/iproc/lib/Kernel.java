package iproc.lib;

public class Kernel {
	
	/* public data members */
	public static String name_ = "empty kernel";
	
	/* private data members */
	private Matrix kernel_ = null;
	
	/* constructors */
	public Kernel(String name, float[][] kernel) {
		name_ = name;
		kernel_ = new Matrix(kernel);
	}
	
	public Kernel(String name, Matrix kernel) {
		name_ = name;
		kernel_ = new Matrix(kernel);
	}
}
