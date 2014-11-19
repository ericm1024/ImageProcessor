package iproc;

import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;

import java.util.Arrays;


// TODO: edit for style
public class AlgSortTest {

	  @Test
	  public void test_heapsort_1() {
	    int[] A = { 6, 2, 9, 3, 4, 8, 0, 1, 7, 5 };
	    int[] Acopy = Arrays.copyOf(A, A.length);
	    Arrays.sort( Acopy );
	    //System.out.println("Before sorting, A is " + Arrays.toString(A));
	    Sort.heapsort( A );
	    //System.out.println("After sorting, A is " + Arrays.toString(A));
	    assertArrayEquals( Acopy, A );
	  }
	  
	  @Test
	  public void test_heapsort_2() {
	    int[] A = { 9, 9, 0, 9, 9, 0, 0, 0, 9, 0, 7 };
	    int[] Acopy = Arrays.copyOf(A, A.length);
	    Arrays.sort( Acopy );
	    //System.out.println("Before sorting, A is " + Arrays.toString(A));
	    Sort.heapsort( A );
	    //System.out.println("After sorting, A is " + Arrays.toString(A));
	    assertArrayEquals( Acopy, A );
	  }
	  
	  @Test
	  public void test_heapsort_3() {
	    int[] A = { 9, 8, 7, 6, 5, 3, 4, 2, 1, 0 };
	    int[] Acopy = Arrays.copyOf(A, A.length);
	    Arrays.sort( Acopy );
	    //System.out.println("Before sorting, A is " + Arrays.toString(A));
	    Sort.heapsort( A );
	    //System.out.println("After sorting, A is " + Arrays.toString(A));
	    assertArrayEquals( Acopy, A );
	  }
	  
	  @Test
	  public void test_heapsort_4() {
	    int[] A = { 42, 42, 43, 41, -42, -42 };
	    int[] Acopy = Arrays.copyOf(A, A.length);
	    Arrays.sort( Acopy );
	    //System.out.println("Before sorting, A is " + Arrays.toString(A));
	    Sort.heapsort( A );
	    //System.out.println("After sorting, A is " + Arrays.toString(A));
	    assertArrayEquals( Acopy, A );
	  }
	  
	  @Test
	  public void test_heapsort_5() {
	    int[] A = { };
	    int[] Acopy = Arrays.copyOf(A, A.length);
	    Arrays.sort( Acopy );
	    //System.out.println("Before sorting, A is " + Arrays.toString(A));
	    Sort.heapsort( A );
	    //System.out.println("After sorting, A is " + Arrays.toString(A));
	    assertArrayEquals( Acopy, A );
	  }
	  
	  @Test
	  public void test_heapsort_6() {
	    int[] A = { 42 };
	    int[] Acopy = Arrays.copyOf(A, A.length);
	    Arrays.sort( Acopy );
	    //System.out.println("Before sorting, A is " + Arrays.toString(A));
	    Sort.heapsort( A );
	    //System.out.println("After sorting, A is " + Arrays.toString(A));
	    assertArrayEquals( Acopy, A );
	  }
	  
	  @Test
	  public void test_heapsort_7_random() {
	    int LEN = 1000;
	    int[] A = new int[LEN];
	    int maxval = 10000;
	    for (int i=0 ; i<A.length ; ++i) {
	      A[i] = (int)(maxval*Math.random());
	    }
	    int[] Acopy = Arrays.copyOf(A, A.length);
	    Arrays.sort( Acopy );
	    //System.out.println("Before sorting, A is " + Arrays.toString(A));
	    Sort.heapsort( A );
	    //System.out.println("After sorting, A is " + Arrays.toString(A));
	    assertArrayEquals( Acopy, A );
	  }
	  
	  
	  @Test
	  public void test_minsort_1() {
	    int[] A = { 6, 2, 9, 3, 4, 8, 0, 1, 7, 5 };
	    int[] Acopy = Arrays.copyOf(A, A.length);
	    Arrays.sort( Acopy );
	    //System.out.println("Before sorting, A is " + Arrays.toString(A));
	    Sort.minsort( A );
	    //System.out.println("After sorting, A is " + Arrays.toString(A));
	    assertArrayEquals( Acopy, A );
	  }
	  
	  @Test
	  public void test_minsort_2() {
	    int[] A = { 9, 9, 0, 9, 9, 0, 0, 0, 9, 0, 7 };
	    int[] Acopy = Arrays.copyOf(A, A.length);
	    Arrays.sort( Acopy );
	    //System.out.println("Before sorting, A is " + Arrays.toString(A));
	    Sort.minsort( A );
	    //System.out.println("After sorting, A is " + Arrays.toString(A));
	    assertArrayEquals( Acopy, A );
	  }
	  
	  @Test
	  public void test_minsort_3() {
	    int[] A = { 9, 8, 7, 6, 5, 3, 4, 2, 1, 0 };
	    int[] Acopy = Arrays.copyOf(A, A.length);
	    Arrays.sort( Acopy );
	    //System.out.println("Before sorting, A is " + Arrays.toString(A));
	    Sort.minsort( A );
	    //System.out.println("After sorting, A is " + Arrays.toString(A));
	    assertArrayEquals( Acopy, A );
	  }
	  
	  @Test
	  public void test_minsort_4() {
	    int[] A = { 42, 42, 43, 41, -42, -42 };
	    int[] Acopy = Arrays.copyOf(A, A.length);
	    Arrays.sort( Acopy );
	    //System.out.println("Before sorting, A is " + Arrays.toString(A));
	    Sort.minsort( A );
	    //System.out.println("After sorting, A is " + Arrays.toString(A));
	    assertArrayEquals( Acopy, A );
	  }
	  
	  @Test
	  public void test_minsort_5() {
	    int[] A = { };
	    int[] Acopy = Arrays.copyOf(A, A.length);
	    Arrays.sort( Acopy );
	    //System.out.println("Before sorting, A is " + Arrays.toString(A));
	    Sort.minsort( A );
	    //System.out.println("After sorting, A is " + Arrays.toString(A));
	    assertArrayEquals( Acopy, A );
	  }
	  
	  @Test
	  public void test_minsort_6() {
	    int[] A = { 42 };
	    int[] Acopy = Arrays.copyOf(A, A.length);
	    Arrays.sort( Acopy );
	    //System.out.println("Before sorting, A is " + Arrays.toString(A));
	    Sort.minsort( A );
	    //System.out.println("After sorting, A is " + Arrays.toString(A));
	    assertArrayEquals( Acopy, A );
	  }
	  
	  @Test
	  public void test_minsort_7_random() {
	    int LEN = 1000;
	    int[] A = new int[LEN];
	    int maxval = 10000;
	    for (int i=0 ; i<A.length ; ++i) {
	      A[i] = (int)(maxval*Math.random());
	    }
	    int[] Acopy = Arrays.copyOf(A, A.length);
	    Arrays.sort( Acopy );
	    //System.out.println("Before sorting, A is " + Arrays.toString(A));
	    Sort.minsort( A );
	    //System.out.println("After sorting, A is " + Arrays.toString(A));
	    assertArrayEquals( Acopy, A );
	  }
	  
	  
	  @Test
	  public void test_bucketsort_1() {
	    int[] A = { 6, 2, 9, 3, 4, 8, 0, 1, 7, 5 };
	    int[] Acopy = Arrays.copyOf(A, A.length);
	    Arrays.sort( Acopy );
	    //System.out.println("Before sorting, A is " + Arrays.toString(A));
	    Sort.bucketsort( A );
	    //System.out.println("After sorting, A is " + Arrays.toString(A));
	    assertArrayEquals( Acopy, A );
	  }
	  
	  @Test
	  public void test_bucketsort_2() {
	    int[] A = { 9, 9, 0, 9, 9, 0, 0, 0, 9, 0, 7 };
	    int[] Acopy = Arrays.copyOf(A, A.length);
	    Arrays.sort( Acopy );
	    //System.out.println("Before sorting, A is " + Arrays.toString(A));
	    Sort.bucketsort( A );
	    //System.out.println("After sorting, A is " + Arrays.toString(A));
	    assertArrayEquals( Acopy, A );
	  }
	  
	  @Test
	  public void test_bucketsort_3() {
	    int[] A = { 9, 8, 7, 6, 5, 3, 4, 2, 1, 0 };
	    int[] Acopy = Arrays.copyOf(A, A.length);
	    Arrays.sort( Acopy );
	    //System.out.println("Before sorting, A is " + Arrays.toString(A));
	    Sort.bucketsort( A );
	    //System.out.println("After sorting, A is " + Arrays.toString(A));
	    assertArrayEquals( Acopy, A );
	  }
	  
	  @Test
	  public void test_bucketsort_4() {
	    int[] A = { 42, 42, 43, 41, -42, -42 };
	    int[] Acopy = Arrays.copyOf(A, A.length);
	    Arrays.sort( Acopy );
	    //System.out.println("Before sorting, A is " + Arrays.toString(A));
	    Sort.bucketsort( A );
	    //System.out.println("After sorting, A is " + Arrays.toString(A));
	    assertArrayEquals( Acopy, A );
	  }
	  
	  @Test
	  public void test_bucketsort_5() {
	    int[] A = { };
	    int[] Acopy = Arrays.copyOf(A, A.length);
	    Arrays.sort( Acopy );
	    //System.out.println("Before sorting, A is " + Arrays.toString(A));
	    Sort.bucketsort( A );
	    //System.out.println("After sorting, A is " + Arrays.toString(A));
	    assertArrayEquals( Acopy, A );
	  }
	  
	  @Test
	  public void test_bucketsort_6() {
	    int[] A = { 42 };
	    int[] Acopy = Arrays.copyOf(A, A.length);
	    Arrays.sort( Acopy );
	    //System.out.println("Before sorting, A is " + Arrays.toString(A));
	    Sort.bucketsort( A );
	    //System.out.println("After sorting, A is " + Arrays.toString(A));
	    assertArrayEquals( Acopy, A );
	  }
	  
	  @Test
	  public void test_bucketsort_7_random() {
	    int LEN = 1000;
	    int[] A = new int[LEN];
	    int maxval = 10000;
	    for (int i=0 ; i<A.length ; ++i) {
	      A[i] = (int)(maxval*Math.random());
	    }
	    int[] Acopy = Arrays.copyOf(A, A.length);
	    Arrays.sort( Acopy );
	    //System.out.println("Before sorting, A is " + Arrays.toString(A));
	    Sort.bucketsort( A );
	    //System.out.println("After sorting, A is " + Arrays.toString(A));
	    assertArrayEquals( Acopy, A );
	  }
	  
	  @Test
	  public void test_quicksort_1() {
	    int[] A = { 6, 2, 9, 3, 4, 8, 0, 1, 7, 5 };
	    int[] Acopy = Arrays.copyOf(A, A.length);
	    Arrays.sort( Acopy );
	    //System.out.println("Before sorting, A is " + Arrays.toString(A));
	    Sort.quicksort( A );
	    //System.out.println("After sorting, A is " + Arrays.toString(A));
	    assertArrayEquals( Acopy, A );
	  }
	  
	  @Test
	  public void test_quicksort_2() {
	    int[] A = { 9, 9, 0, 9, 9, 0, 0, 0, 9, 0, 7 };
	    int[] Acopy = Arrays.copyOf(A, A.length);
	    Arrays.sort( Acopy );
	    //System.out.println("Before sorting, A is " + Arrays.toString(A));
	    Sort.quicksort( A );
	    //System.out.println("After sorting, A is " + Arrays.toString(A));
	    assertArrayEquals( Acopy, A );
	  }
	  
	  @Test
	  public void test_quicksort_3() {
	    int[] A = { 9, 8, 7, 6, 5, 3, 4, 2, 1, 0 };
	    int[] Acopy = Arrays.copyOf(A, A.length);
	    Arrays.sort( Acopy );
	    //System.out.println("Before sorting, A is " + Arrays.toString(A));
	    Sort.quicksort( A );
	    //System.out.println("After sorting, A is " + Arrays.toString(A));
	    assertArrayEquals( Acopy, A );
	  }
	  
	  @Test
	  public void test_quicksort_4() {
	    int[] A = { 42, 42, 43, 41, -42, -42 };
	    int[] Acopy = Arrays.copyOf(A, A.length);
	    Arrays.sort( Acopy );
	    //System.out.println("Before sorting, A is " + Arrays.toString(A));
	    Sort.quicksort( A );
	    //System.out.println("After sorting, A is " + Arrays.toString(A));
	    assertArrayEquals( Acopy, A );
	  }
	  
	  
	  @Test
	  public void test_quicksort_5_random() {
	    int LEN = 1000;
	    int[] A = new int[LEN];
	    int maxval = 10000;
	    for (int i=0 ; i<A.length ; ++i) {
	      A[i] = (int)(maxval*Math.random());
	    }
	    int[] Acopy = Arrays.copyOf(A, A.length);
	    Arrays.sort( Acopy );
	    //System.out.println("Before sorting, A is " + Arrays.toString(A));
	    Sort.quicksort( A );
	    //System.out.println("After sorting, A is " + Arrays.toString(A));
	    assertArrayEquals( Acopy, A );
	  }
}
