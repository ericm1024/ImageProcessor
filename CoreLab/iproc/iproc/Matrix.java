package iproc;

public class Matrix {
	
	/* public data members */
	
	/* private data members */
	float[] matrix_ = null;
	int rows_ = 0;
	int cols_ = 0;
	
	/* public functions */
	
	/**
	 * Constructs an empty matrix.
	 * 
	 * @param rows. the number of rows in the matrix.
	 * @param cols. the number of columns in the matrix.
	 * 
	 * time : O(rows_ * cols_) (because java internally sets everything to 0)
	 * space : O(rows_ * cols_)
	 */
	public Matrix(int rows, int cols) {
		assert (rows > 0 && cols > 0);
		rows_ = rows;
		cols_ = cols;
		matrix_ = new float[rows_ * cols_];
	}
	
	/**
	 * Matrix constructor from primative float array. 
	 * Expects data in row major form, i.e. index by [row][col]
	 * 
	 * @param data. the float array to initialize the matrix from.
	 * 
	 * time : O(rows_ * cols_) (because java internally sets everything to 0)
	 * space : O(rows_ * cols_)
	 */
	public Matrix(float[][] data) {
		rows_ = data.length;
		cols_ = data[0].length;
		matrix_ = new float[rows_ * cols_];
		for (int i = 0; i < data.length; i++) {
			System.arraycopy(data[i], 0, matrix_, i * data[0].length, data[0].length);
		}
	}
	
	/**
	 * Copy constructor. Makes a deep copy.
	 * @param other : matrix to instansiate from
	 */
	public Matrix(Matrix other) {
		rows_ = other.rows_;
		cols_ = other.cols_;
		matrix_ = new float[rows_ * cols_];
		System.arraycopy(other.matrix_, 0, matrix_, 0, other.matrix_.length);
	}
	
	/**
	 * Get entries out of the matrix.
	 * 
	 * @param row
	 * @param col
	 * @return the row,col-entry in the matrix.
	 * 
	 * time : O(1)
	 * space : O(1)
	 */
	public float getItem(int row, int col) {
		return matrix_[row * cols_ + col];
	}
	
	/**
	 * Set entries in the matrix.
	 * 
	 * @param row
	 * @param col
	 * @param value. the value to put in the row,col-entry of the matrix
	 * 
	 * time : O(1)
	 * space : O(1)
	 */
	public void setItem(int row, int col, float value) {
		matrix_[row * cols_ + col] = value;
	}
	
	/**
	 * Multiply by a scalar.
	 * 
	 * @param scalar
	 * @return the matrix with every entry multiplied by the scalar
	 * 
	 * time : O(rows_ * cols_)
	 * space : O(1)
	 */
	public Matrix multiply(float scalar) {
		for (int i = 0; i < matrix_.length; i++) {
			matrix_[i] *= scalar;
		}
		return this;
	}
	
	/**
	 * Right multiply by another matrix, i.e. this * other. Uses the naive 
	 * algorithm.
	 * 
	 * @param other
	 * @return a new matrix
	 * 
	 * time : O(cols_ * rows_ * other.cols_)
	 * space : O(rows_ * other.cols_)
	 */
	public Matrix multiply(Matrix other) {
		assert(this.cols_ == other.rows_);
		Matrix result = new Matrix(rows_, other.cols_);
		for (int row = 0; row < result.rows_; row++) {
			for (int col = 0; col < result.cols_; col++) {
				float accum = 0;
				for (int i = 0; i < this.cols_; i++) {
					accum += this.getItem(row, i) * other.getItem(i, col);
				}
				result.setItem(row, col, accum);
			}
		}
		return result;
	}
	
	/**
	 * Add the summand to every element.
	 * 
	 * @param summand
	 * @return the matrix with the summand added to every element
	 * 
	 * time : O(cols_ * rows_)
	 * space : O(1)
	 */
	public Matrix add(float summand) {
		for (int i = 0; i < matrix_.length; i++) {
			matrix_[i] += summand;
		}
		return this;
	}
	
	/**
	 * Adds to matrices.
	 * 
	 * @param other
	 * @return the matrix with other added to it
	 * 
	 * time : O(cols_ * rows_)
	 * space: O(1)
	 */
	public Matrix add(Matrix other) {
		assert(sameDimension(other));
		for (int i = 0; i < matrix_.length; i++) {
			matrix_[i] += other.matrix_[i];
		}
		return this;
	}
	
	/* private functions */
	
	/**
	 * Checks if this has the same dimensions as other.
	 * 
	 * @param other
	 * @return true or false
	 * 
	 * time : O(1)
	 * space : O(1)
	 */
	private Boolean sameDimension(Matrix other) {
		return this.rows_ == other.rows_ && this.cols_ == other.cols_;
	}
}
