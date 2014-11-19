package iproc;

public class Array {
	
	public static byte min(byte[] array) {
		byte min = array[0];
		for (byte n : array) {
			if (n < min) {
				min = n;
			}
		}
		return min;
	}
	
	public static int min(short[] array) {
		short min = array[0];
		for (short n : array) {
			if (n < min) {
				min = n;
			}
		}
		return min;
	}
	
	public static int min(int[] array) {
		int min = array[0];
		for (int n : array) {
			if (n < min) {
				min = n;
			}
		}
		return min;
	}
	
	public static long min(long[] array) {
		long min = array[0];
		for (long n : array) {
			if (n < min) {
				min = n;
			}
		}
		return min;
	}
	
	public static float min(float[] array) {
		float min = array[0];
		for (float n : array) {
			if (n < min) {
				min = n;
			}
		}
		return min;
	}
	
	public static double min(double[] array) {
		double min = array[0];
		for (double n : array) {
			if (n < min) {
				min = n;
			}
		}
		return min;
	}
	
	public static <T extends Comparable<T>> T min(T[] array) {
		T min = array[0];
		for (T n : array) {
			if (n.compareTo(min) < 0) {
				min = n;
			}
		}
		return min;
	}
	
	public static byte max(byte[] array) {
		byte max = array[0];
		for (byte n : array) {
			if (n > max) {
				max = n;
			}
		}
		return max;
	}
	
	public static int max(short[] array) {
		short max = array[0];
		for (short n : array) {
			if (n > max) {
				max = n;
			}
		}
		return max;
	}
	
	public static int max(int[] array) {
		int max = array[0];
		for (int n : array) {
			if (n > max) {
				max = n;
			}
		}
		return max;
	}
	
	public static long max(long[] array) {
		long max = array[0];
		for (long n : array) {
			if (n > max) {
				max = n;
			}
		}
		return max;
	}
	
	public static float max(float[] array) {
		float max = array[0];
		for (float n : array) {
			if (n > max) {
				max = n;
			}
		}
		return max;
	}
	
	public static double max(double[] array) {
		double max = array[0];
		for (double n : array) {
			if (n > max) {
				max = n;
			}
		}
		return max;
	}
	
	public static <T extends Comparable<T>> T max(T[] array) {
		T max = array[0];
		for (T n : array) {
			if (n.compareTo(max) < 0) {
				max = n;
			}
		}
		return max;
	}
}
