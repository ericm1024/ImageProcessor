package alg;

import iproc.lib.Array;

import java.util.ArrayList;

/**
 * a class for several sorting algorithms
 * 
 * @author 
 *
 */
// TODO: style... so much style
public class Sort {
   
	/**
	 * minsort - O(N**2) sorting (also: selection sort)
	 * @param A, an array of ints, to be sorted in place
	 * @note in place
	 */
	public static void minsort(int[] A) {
		// for every element E in the list
		for (int i = 0; i < A.length; i++) {
			// find the min
			int minIndex = findMinIndex(A,i);
			int min = A[minIndex];
			// swap it with the element at i
			A[minIndex] = A[i];
			A[i] = min;
		}
	}
  
	private static int findMinIndex(int[] array, int startingIndex) {
		int minIndex = startingIndex;
		for (int i = startingIndex + 1 ; i < array.length; i++) {
			if (array[i] < array[minIndex]) {
				minIndex = i;
			}
		}
		return minIndex;
	}
  
	/**
	 * bucketsort - O(N) sorting, sort of...
	 * @param A, an array of ints, to be sorted in place
	 */
	public static void bucketsort(int[] A) {
		if (A.length < 2) {
			return;
		}
		
		int min = Array.min(A);
		int max = Array.max(A);
		int[] buckets = new int[max - min + 1];
		
		// fill the buckets
		for (int n : A) {
			buckets[n-min]++;
		}
		
		// empty the buckets in order
		int j = 0;
		for (int i = 0; i < buckets.length; i++) {
			for ( ; buckets[i] > 0; buckets[i]--) {
				A[j++] = i + min;
			}
		}
	}
  
	/**
	 * this Heap class implements a binary min-heap
	 * it uses Java's ArrayList<Integer> as the storage for
	 *    its data, named this.heapdata
	 *                          
	 * At all times (except just before calling percolate_up and percolate_down),
	 * the heap property should be true:
	 *   Every node is less than (or equal to) both its children
	 */
	static class Heap {
		/**
		 * this.heapdata is the storage for our heap
		 * 
		 * indexing reminders:
		 * 
		 * for an element at index i:
		 *    its parent is at index (i-1)/2
		 *    its leftchild is at index 2*i+1
		 *    its rightchild is at index 2*i+2
		 */
		public ArrayList<Integer> heapdata;
    
		/**
		 * constructor for our Heap class
		 */
		public Heap() {
			this.heapdata = new ArrayList<Integer>(); // construct our heapdata
		}
    
		/**
		 * our toString method, delegating to the ArrayList toString method
		 * @return the string representation of this.heapdata
		 */
		@Override
		public String toString() {
			return "" + this.heapdata; // Uses ArrayList's toString method
		}
    
		/**
		 * this method also delegates to the ArrayList size method
		 * @return the number of nodes currently in the Heap
		 */
		public int size() {
			return this.heapdata.size(); // Uses ArrayList's size method
		}

	    
	    /**
	     * inserts an element to the heap and maintains the heap property
	     * @param val, the value to be inserted to the heap
	     */
	    public void insert(int val) {
		      this.heapdata.add(val); // adds to the back of the heapdata
		      this.percolateUp();  // fixes the heap up!
	    }
	
		/**
		 * removes and returns the minimum value in the heap
		 *   it makes sure the heap property is maintained, too
		 * @return the smallest value that was in the heap
		 */
	    public int removeMin() {
		      if (this.size() <= 0)  {
		        System.out.println("Error: can not remove from an empty heap.");
		        return -1;
		      }
		      // get the min, remove the last element
		      int min = heapdata.get(0);
		      int end = heapdata.remove(heapdata.size()-1);
		      // percolate if necessary
		      if (heapdata.size() > 0) {
		        this.heapdata.set(0, end);
		        this.percolateDown(); 
		      }
		      // now, we return that min
		      return min;
	    }	
    
	    /**
	     * percolate_up should adjust the last element of the heap
	     *    by swapping it with its parent, grandparent, ...
	     *    until the heap property is satisfied
	     */
		private void percolateUp() {
			int childIndex = heapdata.size() - 1; 
			int parentIndex;
			
			while (hasParent(childIndex) && heapdata.get(childIndex) 
					< heapdata.get(parentIndex = parentOf(childIndex)) ) {
				swap(parentIndex, childIndex);
				childIndex = parentIndex;
			}
		}
    
	    /**
	     * percolate_down should adjust the first element of the heap
	     *    by swapping it with its smaller child, grandchild, ...
	     *    until the heap property is satisfied
	     */
	    private void percolateDown() {
	    	int parentIndex = 0;
	    	int minChildIndex;
	    	
	    	while (hasLChild(parentIndex) && heapdata.get(parentIndex) 
	    			> heapdata.get(minChildIndex = minChild(parentIndex))) {
	    		swap(parentIndex, minChildIndex);
	    		parentIndex = minChildIndex;
	    	}
	    }
	    
	    /**
	     * Helper function for both percolateUp and percolateDown. Swaps the
	     * values at indexA and indexB in heapdata
	     * @param indexA
	     * @param indexB
	     */
	    private void swap(int indexA, int indexB) {
	    	int valA = heapdata.get(indexA);
	    	heapdata.set(indexA, heapdata.get(indexB));
	    	heapdata.set(indexB, valA);
	    }
	    
	    /**
	     * Helper function for percolateUp.
	     * @param childIndex
	     * @return true if the index has a valid parent index, false otherwise
	     */
	    private boolean hasParent(int childIndex) {
	    	return childIndex > 0;
	    }
	    
	    /**
	     * Helper function for percolateUp
	     * @param childIndex
	     * @return The index of the parent of childIndex
	     */
	    private int parentOf(int childIndex) {
	    	return (childIndex - 1)/2;
	    }
	    
	    /**
	     * Helper function for percolateDown. It is not valid to call this function if
	     * no children exist. In other words, it assumes at least one child (the left one)
	     * exists.
	     * @param parentIndex 
	     * @return the index of the smallest child of the value at parentIndex 
	     */
	    private int minChild(int parentIndex) {
	    	int left = heapdata.get(lChild(parentIndex));
	    	int right = hasRChild(parentIndex) ? heapdata
	    			.get(rChild(parentIndex)) : Integer.MAX_VALUE;
	    			
	    	return left < right ? lChild(parentIndex) : rChild(parentIndex);
	    }
	    
	    /**
	     * Helper function for percolateDown.
	     * @param parentIndex
	     * @return true if the index has a valid right child index, false otherwise
	     */
	    private boolean hasRChild(int parentIndex) {
	    	return parentIndex*2 + 2 < size();
	    }
	    
	    /**
	     * Helper function for percolateDown.
	     * @param parentIndex
	     * @return true if the index has a valid right child index, false otherwise
	     */
	    private boolean hasLChild(int parentIndex) {
	    	return parentIndex*2 + 1 < size();
	    }
	    
	    /**
	     * Helper function for percolateDown.
	     * @param parentIndex
	     * @return the index of the left child
	     */
	    private int lChild(int parentIndex) {
	    	return parentIndex * 2 + 1;
	    }
	    
	    /**
	     * Helper function for percolateDown.
	     * @param parentIndex
	     * @return the index of the right child
	     */
	    private int rChild(int parentIndex) {
	    	return parentIndex * 2 + 2;
	    }
	}
  
	/**
	 * heapsort sorts by creating a min-heap and then removing
	 *     consecutive minimum elements until it's done!
	 * @param A, the array to be sorted
	 */
	public static void heapsort(int[] A) {
		Heap h = new Sort.Heap();
		// construct the heap
		for (int n:A) { 
			h.insert(n);
		}

		for (int i=0 ; i<A.length ; ++i) {
			A[i] = h.removeMin();
		}
		return;
	}
  
  
  
	/**
	 * extra credit #2 - quicksort
	 *   sorts in place - and without add'l arrays!
	 * @param A, an array of ints
	 */
	public static void quicksort(int[] A) {
		
	}
	
	private static void quicksortIteration(int[] A, int low, int high) {
		
	}
}



