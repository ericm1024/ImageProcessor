package iproc;

public class ConvolveLib {

	/* lab specific kernels */
	
	/* lab 7 */ 
	
	public static final float[][] KERNEL_LAB7_W5 = {{1f/16f,2f/16f,1f/16f},
		{2f/16f,4f/16f,2f/16f}, {1f/16f,2f/16f,1f/16f}};
	
	/* lab 8 */
	
	public static final float[][] KERNEL_LAB8_HB1 =
		{{-1,-1,-1}, {-1,8,-1}, {-1,-1,-1}};
	
	public static final float[][] KERNEL_LAB8_HB2 =
		{{0,-1,0}, {-1,4,-1}, {0,-1,0}};
	
	/* lab 9 */
	
	public static final float[][] KERNEL_LAB9_LAPLACE_1 = 
		{{0,1,0}, {1,-4,1}, {0,1,0}};
	
	public static final float[][] KERNEL_LAB9_LAPLACE_2 =
		{{1,1,1}, {1,-8,1}, {1,1,1}};
	
	public static final float[][] KERNEL_LAB9_LAPLACE_3 =
		{{0.5f,1,0.5f}, {1,-6,1}, {0.5f,1,0.5f}};
	
	public static final float[][] KERNEL_LAB9_LAPLACE_OF_GAUSS_5 =
		{{0,0,1,0,0}, {0,1,2,1,0}, {1,2,-16,2,1}, {0,1,2,1,0}, {0,0,1,0,0}};
	
	/**
	 * source: 
	 * http://en.wikipedia.org/wiki/Canny_edge_detector#Noise_reduction
	 */
	public static final float[][] KERNEL_LAB9_GAUSS =
		{{2f/159f, 4f/159f, 5f/159f, 4f/159f, 2f/159f},
		 {4f/159f, 9f/159f, 12f/159f, 9f/159f, 4f/159f},
		 {5f/159f, 12f/159f, 15f/159f, 12f/159f, 5f/159f},
		 {4f/159f, 9f/159f, 12f/159f, 9f/159f, 4f/159f},
		 {2f/159f, 4f/159f, 5f/159f, 4f/159f, 2f/159f}};
	
	/* gradient kernels */
	
	public static final float[][] GRAD_ROBERTS_X = {{-1,1}, {0,0}};
	
	public static final float[][] GRAD_ROBERTS_Y = {{-1,0}, {1,0}};
	
	public static final float[][] GRAD_SOBEL_X =
		{{-1,0,1}, {-2,0,2}, {-1,0,1}};
	
	public static final float[][] GRAD_SOBEL_Y =
		{{-1,-2,1}, {0,0,0}, {1,2,1}};
	
	/**
	 * source: 
	 * http://en.wikipedia.org/wiki/Sobel_operator#Alternative_operators
	 * 
	 * The Scharr operators are similar the Sobel operators, but they are
	 * rotationally symmetric. 
	 */
	public static final float[][] GRAD_SCHARR_X =
		{{3,0,-3}, {10,0,-10}, {3,0,-3}};
			
	public static final float[][] GRAD_SCHARR_Y =
		{{3,10,3}, {0,0,0}, {-3,-10,-3}};
	
	public static final float[][] GRAD_PREWITT_X_3 =
		{{-1,0,1}, {-1,0,1}, {-1,0,1}};
	
	public static final float[][] GRAD_PREWITT_Y_3 =
		{{-1,-1,-1}, {0,0,0}, {1,1,1}}; 
	
	public static final float[][] GRAD_PREWITT_X_4 =
		{{-3,-1,1,3}, {-3,-1,1,3}, {-3,-1,1,3}, {-3,-1,1,3}};
	
	public static final float[][] GRAD_PREWITT_Y_4 =
		{{-3,-3,-3,-3}, {-1,-1,-1,-1}, {1,1,1,1}, {3,3,3,3}};
	
	/* compass operators */
	
	public static final float[][] GRAD_COMP_E_3 =
		{{-1,0,1}, {-1,0,1}, {-1,0,1}};
	
	public static final float[][] GRAD_COMP_W_3 = 
		{{1,0,-1}, {1,0,-1}, {1,0,-1}};
	
	public static final float[][] GRAD_COMP_N_3 =
		{{1,1,1}, {0,0,0}, {-1,-1,-1}};
	
	public static final float[][] GRAD_COMP_S_3 = 
		{{-1,-1,-1}, {0,0,0}, {1,1,1}};
	
	/* all pass kernel */
	
	public static final float[][] KERNEL_ALLPASS_3 =
		{{0,0,0}, {0,1,0}, {0,0,0}};
	
	public static final float[][] KERNEL_ALLPASS_5 =
		{{0,0,0,0,0}, {0,0,0,0,0}, {0,0,1,0,0}, {0,0,0,0,0}, {0,0,0,0,0}};
	
	/**
	 * creates a high pass kernel from a low pass kernel
	 */
	public static float[][] getHpFromLp(float[][] kernel) {
		float[][] hpFilter = new float[kernel.length][kernel[0].length];
		
		for(int x = 0; x < hpFilter.length; x++) {
			for(int y = 0; y < hpFilter[0].length; y++) {
		
				// if we're in the very middle
				if (x == hpFilter.length/2 && y == hpFilter[0].length/2) {
					hpFilter[x][y] = 1 - kernel[x][y];
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
	public static float[][] getHbFromHp(float[][] kernel, float multiplier) {
		float[][] hbFilter = new float[kernel.length][kernel[0].length];
		
		for(int x = 0; x < hbFilter.length; x++) {
			for(int y = 0; y < hbFilter[0].length; y++) {
		
				// if we're in the very middle
				if (x == hbFilter.length/2 && y == hbFilter[0].length/2) {
					hbFilter[x][y] = 1 + multiplier * kernel[x][y];
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
	public static float[][] kernelDifference(float[][] k1, float[][] k2) {
		assert(k1.length == k2.length && k1[0].length == k2[0].length);
		float[][] result = new float[k1.length][k1[0].length];
		
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
	public static float[][] kernelSum(float[][] k1, float[][] k2) {
		assert(k1.length == k2.length && k1[0].length == k2[0].length);
		float[][] result = new float[k1.length][k1[0].length];
		
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
	public static float[][] multiplyScalar(float[][] kernel, float c) {
		assert(kernel.length == kernel.length);
		float[][] result = new float[kernel.length][kernel[0].length];
		
		for (int x = 0; x < result.length; x++) {
			for (int y = 0; y < result[0].length; y++) {
				result[x][y] = c * kernel[x][y];
			}
		}
		return result;
	}
	
	
	/* low pass filtering kernels  */
	
	public static float[][] getSquareKernel(int width) {
		assert (width%2 == 1); // odd
		float[][] kernel = new float[width][width];

		for (int i = 0; i < kernel.length; i++) {
			for (int j = 0; j < kernel[0].length; j++) {
				kernel[i][j] = 1;
			}
		}
		return ConvolveLib.normalizeKernel(kernel);
	}
	
	@Deprecated
	public static float[][] getGaussKernel(int width, float numSigma) {
		assert (width%2 == 1); // odd 
 		float[][] kernel = new float[width][width];
 		float sigma = (float)width/(2*numSigma);
 		int offset = width/2;
		
		for (int x = 0; x < kernel.length; x++) {
			for (int y = 0; y < kernel[0].length; y++) {
				kernel[x][y] = gauss2D(x - offset, y - offset, sigma);
			}
		}
		return normalizeKernel(kernel);
	}
	
	public static float[][] getGauss(int width, float sigma) {
		assert (width%2 == 1); // odd 
 		float[][] kernel = new float[width][width];
 		int offset = width/2;
		
		for (int x = 0; x < kernel.length; x++) {
			for (int y = 0; y < kernel[0].length; y++) {
				kernel[x][y] = gauss2DV2(x - offset, y - offset, sigma);
			}
		}
		return normalizeKernel(kernel);
	}
	
	public static float[][] getApKernel(int width) {
		assert (width%2 == 1); // odd
		float[][] kernel = new float[width][width];

		kernel[width/2][width/2] = 1; 
		
		return kernel;
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
	
	public static float[][] normalizeKernel(float[][] kernel) {
		float areaUnderCurve = ConvolveLib.integrate(kernel);
		
		for (int i = 0; i < kernel.length; i++) {
			for (int j = 0; j < kernel[0].length; j++) {
				kernel[i][j] /= areaUnderCurve;
			}
		}
		
		return kernel;
	}

	/* private methods */
	
	/* generic helpers for creating kernels */
	
	private static float integrate(float[][] kernel) {
		float result = 0;
		
		for (int i = 0; i < kernel.length; i++) {
			for (int j = 0; j < kernel[0].length; j++) {
				result += kernel[i][j];
			}
		}
		
		return result;
	}
	
	/* helper for getGaussKernel */
	
	@Deprecated
	private static float gauss2D(int x, int y, float sigma) {
		float var = sigma * sigma;             // sigma squared
		float a = 1/(sigma*(float)Math.sqrt(2*Math.PI));  // coefficient
		float exp = (float)(x*x)/(2 * var) + 
					 (float)(y*y)/(2 * var);  // exponent
		
		return a * (float)Math.pow(Math.E, - exp);
	}
	
	private static float gauss2DV2(int x, int y, float sigma) {
		// 2 sigma^2
		float t = 2f * sigma * sigma; 
		// coefficient
		float a = 1f/(t*(float)Math.PI);
		// exponent
		float exp = -(float)(x*x + y*y)/t;
		
		return a * (float)Math.pow(Math.E, exp);
	}
}
