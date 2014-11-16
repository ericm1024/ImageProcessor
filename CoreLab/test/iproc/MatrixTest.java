package iproc;

import static org.junit.Assert.*;

import org.junit.Test;

public class MatrixTest {

	private float[][] M1 = new float[][]{{1}};
	private float[][] M2 = new float[][]{{1,2}, {3,4}, {5,6}};
	private float[][] M3 = new float[][]{{1,2,3}, {4,5,6}, {7,8,9}};
	private float[][] M4 = new float[][]{{10,20,30}, {40,50,60}, {70,80,90}};
	
	// computed with wolfram alpha because I don't trust myself
	private float[][] M3TimesM2 = new float[][]{{22,28}, {49,64}, {76,100}};
	
	private float epsilon = 1e-10f;
	
	// tests constructor and getItem
	@Test
	public void testBasic() {
		Matrix E = new Matrix(1,1);
		// 2nd argument is be zero because java default initializes floats to zero
		assertEquals(E.getItem(0, 0), 0, 0);
		
		Matrix A = new Matrix(M1);
		assertTrue(equal(A, M1, 0));
		
		Matrix B = new Matrix(M2);
		assertTrue(equal(B, M2, 0));
		
		Matrix C = new Matrix(M3);
		assertTrue(equal(C, M3, 0));
	}	
	
	// tests setItem
	@Test
	public void testSetItem() {
		Matrix A = new Matrix(2,3);
		
		// make the matrix:
		//  2 1 6
		//  4 5 9
		A.setItem(0,0,2f);
		A.setItem(0,1,1f);
		A.setItem(0,2,6f);
		A.setItem(1,0,4f);
		A.setItem(1,1,5f);
		A.setItem(1,2,9f);
		
		assertEquals(A.getItem(0,0), 2f, 0);
		assertEquals(A.getItem(0,1), 1f, 0);
		assertEquals(A.getItem(0,2), 6f, 0);
		assertEquals(A.getItem(1,0), 4f, 0);
		assertEquals(A.getItem(1,1), 5f, 0);
		assertEquals(A.getItem(1,2), 9f, 0);
	}
	
	// scalar multiply
	@Test
	public void testMultiplyScalar() {
		float scalar = 3.5f;
		Matrix A = new Matrix(M3);
		
		A.multiply(scalar);
		
		for (int row = 0; row < M3.length; row++) {
			for (int col = 0; col < M3[0].length; col++) {
				assertEquals(A.getItem(row,col), M3[row][col] * scalar, epsilon);
			}
		}
	}
	
	// multiply other
	@Test
	public void testMultiplyMatrix() {
		Matrix A = new Matrix(M3);
		Matrix B = new Matrix(M2);
		
		Matrix AtimesB = A.multiply(B);
		
		assertTrue(equal(AtimesB, M3TimesM2, epsilon));
	}
	
	// add scalar
	@Test
	public void testAddScalar() {
		float scalar = 3.5f;
		Matrix A = new Matrix(M3);
		
		A.add(scalar);
		
		for (int row = 0; row < M3.length; row++) {
			for (int col = 0; col < M3[0].length; col++) {
				assertEquals(A.getItem(row,col), M3[row][col] + scalar, epsilon);
			}
		}
	}
	
	// add other
	@Test
	public void testAddMatrix() {
		Matrix A = new Matrix(M3);
		Matrix B = new Matrix(M4);
		
		A.add(B);
		
		for (int row = 0; row < M3.length; row++) {
			for (int col = 0; col < M3[0].length; col++) {
				assertEquals(A.getItem(row,col), M3[row][col] + M4[row][col], epsilon);
			}
		}
	}
	
	// helpers
	private Boolean equal(Matrix M, float[][] array, float epsilon) {
		for (int row = 0; row < array.length; row++) {
			for (int col = 0; col < array[0].length; col++) {
				if (Math.abs(array[row][col] - M.getItem(row, col)) > epsilon) {
					return false;
				}
			}
		}
		return true;
	}
}
