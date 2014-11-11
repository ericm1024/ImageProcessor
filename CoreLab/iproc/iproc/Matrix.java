package iproc;

public class Matrix {
	
	/* public data members */
	
	/* private data members */
	float[][] matrix_ = null;
	
	/* public functions */
	public Matrix(int width, int height) {
		matrix_ = new float[width][height];
	}
	
	public Matrix(float[][] data) {
		matrix_ = new float[data.length][data[0].length];
		for (int i = 0; i < data.length; i++) {
			System.arraycopy(data[i], 0, matrix_[i], 0, matrix_[i].length);
		}
	}
	
	public float getItem(int x, int y) {
		return matrix_[x][y];
	}
	
	public float setItem(int x, int y, float value) {
		return matrix_[x][y];
	}
	
	public Matrix multiply(float scaleFactor) {
		for (int x = 0; x < matrix_.length; x++) {
			for (int y = 0; y < matrix_[0].length; y++) {
				matrix_[x][y] *= scaleFactor;
			}
		}
		return this;
	}
	
	public Matrix multiply(Matrix other) {
		/*  The number of rows in this must be equal to the number of columns  
		 *  in other.
		 */
		
		return this;
	}
	
	public Matrix add(float summand) {
		for (int x = 0; x < matrix_.length; x++) {
			for (int y = 0; y < matrix_[0].length; y++) {
				matrix_[x][y] += summand;
			}
		}
		return this;
	}
	
	public Matrix add(Matrix other) {
		assert(other.matrix_.length == matrix_.length 
				&& other.matrix_[0].length == matrix_[0].length);
		for (int x = 0; x < matrix_.length; x++) {
			for (int y = 0; y < matrix_[0].length; y++) {
				matrix_[x][y] += other.matrix_[x][y];
			}
		}
		return this;
	}
	
	/* private functions */
}
