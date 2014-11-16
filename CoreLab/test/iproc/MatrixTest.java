package iproc;

import static org.junit.Assert.*;

import org.junit.Test;

public class MatrixTest {

	private float[][] M1 = new float[][]{{1}};
	private float[][] M2 = new float[][]{{1,2}, {3,4}, {5,6}};
	private float[][] M3 = new float[][]{{1,2,3}, {4,5,6}, {7,8,9}};
	
	// tests constructor and getItem
	@Test
	public void testBasic() {
		Matrix A = new Matrix(M1);
		assertTrue(equal(A, M1));
		
		Matrix B = new Matrix(M2);
		assertTrue(equal(B, M2));
		
		Matrix C = new Matrix(M3);
		assertTrue(equal(C, M3));
		
		Matrix E = new Matrix(1,1);
		// 2nd argument is be zero because java default initializes floats to zero
		assertEquals(E.getItem(0, 0), 0, 0);
	}	
	
	// set item tests
	@Test
	public void testSetItem() {
		
	}
	
	// scalar multiply
	
	// multiply other
	
	// add scalar
	
	// add other
	
	// helpers
	private Boolean equal(Matrix M, float[][] array) {
		for (int row = 0; row < array.length; row++) {
			for (int col = 0; col < array[0].length; col++) {
				if (array[row][col] != M.getItem(row, col)) {
					return false;
				}
			}
		}
		return true;
	}
}
