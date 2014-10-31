package iproc;

public class ConvolveLib {
	
	/* low pass filtering kernels  */
	
	public static double[][] getSquareKernel(int width) {
		assert (width%2 == 1); // odd
		double[][] kernel = new double[width][width];

		for (int i = 0; i < kernel.length; i++) {
			for (int j = 0; j < kernel[0].length; j++) {
				kernel[i][j] = 1.0;
			}
		}
		return ConvolveLib.normalizeKernel(kernel);
	}
	
	public static double[][] getW5Kernel() {
		// from assignment
		double[][] kernel = new double[][]{{1, 2, 1}, {2, 4, 2}, {1, 2, 1}};
		
		return normalizeKernel(kernel);
	}
	
	public static double[][] getGaussKernel(int width, double numSigma) {
		assert (width%2 == 1); // odd 
 		double[][] kernel = new double[width][width];
 		double sigma = (double)width/(2*numSigma);
 		int offset = width/2;
		
		for (int x = 0; x < kernel.length; x++) {
			for (int y = 0; y < kernel[0].length; y++) {
				kernel[x][y] = gauss2D(x - offset, y - offset, sigma);
			}
		}
		return normalizeKernel(kernel);
	}
	
	
	/* median filters */
	
	public static Boolean[][] getHFilter(int width) {
		assert (width%2 == 1); // odd
		Boolean[][] filter = new Boolean[width][1];
		for (int i = 0; i < width; i++){
			filter[i][0] = true;
		}
		return filter;
	}
	
	public static Boolean[][] getVFilter(int height) {
		assert (height%2 == 1); // odd
		Boolean[][] filter = new Boolean[1][height];
		for (int i = 0; i < height; i++) {
			filter[0][i] = true;
		}
		return filter;
	}
	
	public static Boolean[][] getSquareFilter(int sideLength) {
		assert (sideLength%2 == 1); // odd
		Boolean[][] filter = new Boolean[sideLength][sideLength];
		for (int i = 0; i < sideLength; i++) {
			for (int j = 0; j < sideLength; j++) {
				filter[i][j] = true;
			}
		}
		return filter;
	}
	
	public static Boolean[][] getCrossFilter(int sideLength, int thickness) {
		assert (sideLength%2 == 1); // odd
		assert (thickness%2 == 1); // odd
		Boolean[][] filter = new Boolean[sideLength][sideLength];
		
		// set all to false
		for (int i = 0; i < filter.length; i++) {
			for (int j = 0; j < filter[0].length; j++) {
				filter[i][j] = false;
			}
		}
		
		// set the middle row
		for (int i = 0; i < filter.length; i++) {
			filter[i][filter[0].length/2] = true;
		}
		
		// set the middle column
		for (int i = 0; i < filter[0].length; i++) {
			filter[filter.length/2][i] = true;
		}
		
		return filter;
	}
	
	/* private methods */
	
	/* generic helpers for creating kernels */
	
	private static double[][] normalizeKernel(double[][] kernel) {
		double areaUnderCurve = ConvolveLib.integrate(kernel);
		
		for (int i = 0; i < kernel.length; i++) {
			for (int j = 0; j < kernel[0].length; j++) {
				kernel[i][j] /= areaUnderCurve;
			}
		}
		
		return kernel;
	}
	
	private static double integrate(double[][] kernel) {
		double result = 0;
		
		for (int i = 0; i < kernel.length; i++) {
			for (int j = 0; j < kernel[0].length; j++) {
				result += kernel[i][j];
			}
		}
		
		return result;
	}
	
	/* helper for getGaussKernel */
	
	private static double gauss2D(int x, int y, double sigma) {
		double var = sigma * sigma;             // sigma squared
		double a = 1.0/(sigma*Math.sqrt(2*Math.PI));  // coefficient
		double exp = (double)(x*x)/(2.0 * var) + 
					 (double)(y*y)/(2.0 * var);  // exponent
		
		return a * Math.pow(Math.E, - exp);
	}
}
