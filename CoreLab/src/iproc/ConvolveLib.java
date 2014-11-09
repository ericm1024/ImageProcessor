package iproc;

public class ConvolveLib {
	
	/**
	 * creates a high pass kernel from a low pass kernel
	 */
	public static double[][] getHpFromLp(double[][] kernel) {
		double[][] hpFilter = new double[kernel.length][kernel[0].length];
		
		for(int x = 0; x < hpFilter.length; x++) {
			for(int y = 0; y < hpFilter[0].length; y++) {
		
				// if we're in the very middle
				if (x == hpFilter.length/2 && y == hpFilter[0].length/2) {
					hpFilter[x][y] = 1.0 - kernel[x][y];
				} else {
					hpFilter[x][y] = - kernel[x][y];
				}
			}
		}
		return hpFilter;
	}
	
	/**
	 * creates a high boost kernel from a low pass kernel
	 */
	public static double[][] getHbFromHp(double[][] kernel, double multiplier) {
		double[][] hbFilter = new double[kernel.length][kernel[0].length];
		
		for(int x = 0; x < hbFilter.length; x++) {
			for(int y = 0; y < hbFilter[0].length; y++) {
		
				// if we're in the very middle
				if (x == hbFilter.length/2 && y == hbFilter[0].length/2) {
					hbFilter[x][y] = 1.0 + multiplier * kernel[x][y];
				} else {
					hbFilter[x][y] = - multiplier * kernel[x][y];
				}
			}
		}
		return hbFilter;
	}
	
	/**
	 * @return the difference of the two kernels, i.e.k1 - k2
	 */
	public static double[][] kernelDifference(double[][] k1, double[][] k2) {
		assert(k1.length == k2.length && k1[0].length == k2[0].length);
		double[][] result = new double[k1.length][k1[0].length];
		
		for (int x = 0; x < result.length; x++) {
			for (int y = 0; y < result[0].length; y++) {
				result[x][y] = k1[x][y] - k2[x][y];
			}
		}
		return result;
	}
	
	/**
	 * @return the sum of the two kernels, i.e.k1 + k2
	 */
	public static double[][] kernelSum(double[][] k1, double[][] k2) {
		assert(k1.length == k2.length && k1[0].length == k2[0].length);
		double[][] result = new double[k1.length][k1[0].length];
		
		for (int x = 0; x < result.length; x++) {
			for (int y = 0; y < result[0].length; y++) {
				result[x][y] = k1[x][y] + k2[x][y];
			}
		}
		return result;
	}
	
	/**
	 * @return multiplies every element in kernel by c
	 */
	public static double[][] multiplyScalar(double[][] kernel, double c) {
		assert(kernel.length == kernel.length);
		double[][] result = new double[kernel.length][kernel[0].length];
		
		for (int x = 0; x < result.length; x++) {
			for (int y = 0; y < result[0].length; y++) {
				result[x][y] = c * kernel[x][y];
			}
		}
		return result;
	}
	
	/* assignment specific */
	
	// from assignment 7 
	public static double[][] getW5Kernel() {
		
		double[][] kernel = new double[][]{{1, 2, 1}, {2, 4, 2}, {1, 2, 1}};
		
		return normalizeKernel(kernel);
	}
	
	// from assignment 8
	public static double[][] getHbKernel() {
			
		double[][] kernel = new double[][]{{-1, -1, -1}, {-1, 8, -1}, {-1, -1, -1}};
		
		return kernel;
	}
	public static double[][] getHbKernel2() {
		
		double[][] kernel = new double[][]{{0, -1, 0}, {-1, 4, -1}, {0, -1, 0}};
		
		return kernel;
	}
	
	public static double[][] getApKernel(int width) {
		assert (width%2 == 1); // odd
		double[][] kernel = new double[width][width];

		kernel[width/2][width/2] = 1.0; 
		
		return kernel;
	}
	
	
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
	
	public static double[][] normalizeKernel(double[][] kernel) {
		double areaUnderCurve = ConvolveLib.integrate(kernel);
		
		for (int i = 0; i < kernel.length; i++) {
			for (int j = 0; j < kernel[0].length; j++) {
				kernel[i][j] /= areaUnderCurve;
			}
		}
		
		return kernel;
	}
	
	/* gradient kernels */
	
	public static double[][] getRobertsX() {
		return new double[][]{{-1,1},{0,0}};
	}
	
	public static double[][] getRobertsY() {
		return new double[][]{{-1,0},{1,0}};
	}
	
	public static double[][] getSobelX() {
		return new double[][]{{-1,0,1},{-2,0,2},{-1,0,1}};
	}
	
	public static double[][] getSobelY() {
		return new double[][]{{-1,-2,1},{0,0,0},{1,2,1}};
	}
	
	public static double[][] getPrewittX3() {
		return new double[][]{{-1,0,1},{-1,0,1},{-1,0,1}};
	}
	
	public static double[][] getPrewittY3() {
		return new double[][]{{-1,-1,-1},{0,0,0},{1,1,1}};
	}
	
	public static double[][] getPrewittX4() {
		return new double[][]{{-3,-1,1,3},{-3,-1,1,3},{-3,-1,1,3},{-3,-1,1,3}};
	}
	
	public static double[][] getPrewittY4() {
		return new double[][]{{-3,-3,-3,-3},{-1,-1,-1,-1},{1,1,1,1},{3,3,3,3}};
	}
	
	/* compass operators */
	
	public static double[][] getCompassEast() {
		return new double[][]{{-1,0,1},{-1,0,1},{-1,0,1}};
	}
	
	public static double[][] getCompassWest() {
		return new double[][]{{1,0,-1},{1,0,-1},{1,0,-1}};
	}
	
	public static double[][] getCompassNorth() {
		return new double[][]{{1,1,1},{0,0,0},{-1,-1,-1}};
	}
	
	public static double[][] getCompassSouth() {
		return new double[][]{{-1,-1,-1},{0,0,0},{1,1,1}};
	}
	
	/* private methods */
	
	/* generic helpers for creating kernels */
	
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
