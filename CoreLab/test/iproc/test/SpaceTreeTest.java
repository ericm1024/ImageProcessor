package iproc.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.util.ArrayList;
import java.util.HashSet;

import iproc.lib.Point;
import iproc.lib.SpaceTree;

import org.junit.Test;

public class SpaceTreeTest {
	
	// insert tests
	 @Test
	 public void test_insert_contains_1() {
		SpaceTree<Point> test = new SpaceTree<>();
		
		test.insert(new Point(1,2));
		assertEquals(test.size(), 1);
		assertTrue(test.contains(new Point(1,2)));
	 }
	 
	 @Test
	 public void test_insert_contains_multi() {
		 SpaceTree<Point> test = new SpaceTree<>();
		 ArrayList<Point> points = new ArrayList<>();
		 
		 for (int i = 0; i < 100; i++) {
			 points.add(new Point((int)(Math.random()*10000),
					 (int)(Math.random() * 10000)));
		 }
		 
		 for (int i = 0; i < 100; i++) {
			 assertEquals(test.size(), i);
			 test.insert(points.get(i));
		 }
		 
		 for (int i = 0; i < 100; i++) {
			 assertTrue(test.contains(points.get(i)));
		 }
	 }
	
	// adjacent tests
	@Test
	public void test_adjacent_1() {
		SpaceTree<Point> test = new SpaceTree<>();
		
		test.insert(new Point(1,2));
		assertTrue(test.adjacent(new Point(2,2)));
	}
	
	@Test
	public void test_adjacent_corner() {
		SpaceTree<Point> test = new SpaceTree<>(SpaceTree.AdjacencyType.CORNERS);
		ArrayList<Point> points = new ArrayList<>();
		 
		// construct a tree
		for (int i = 0; i < 100; i++) {
			Point p = new Point((int)(Math.random()*10000),
					 (int)(Math.random() * 10000));
			points.add(p);
			test.insert(p);
		}
		 
		// check adjacent points for each point
		for (Point p : points) {
			int x = p.getX();
			int y = p.getY();
			int window = 3;
			for (int i = -(window/2); i < window/2; i++) {
				for (int j = -(window/2); j < window/2; j++) {
					assertTrue(test.adjacent(new Point(x + i, y+j)));
				}
			}
		}
	}
	
	@Test
	public void test_adjacent_no_corner() {
		SpaceTree<Point> test = new SpaceTree<>(SpaceTree.AdjacencyType.NO_CORNERS);
		HashSet<Point> points = new HashSet<>();
		 
		 // construct a tree
		for (int i = 0; i < 10000000; i++) {
			Point p = new Point((int)(Math.random()*100)*3,
					 (int)(Math.random()*100)*3);
			if (!points.contains(p)) {
				points.add(p);
				test.insert(p);
			}
		}
		 
		 // check adjacent points for each point
		 for (Point p : points) {
			int x = p.getX();
			int y = p.getY();

			assertTrue(test.contains(p));
			assertTrue(test.adjacent(new Point(x + 1,y)));
			assertTrue(test.adjacent(new Point(x - 1,y)));
			assertTrue(test.adjacent(new Point(x,y + 1)));
			assertTrue(test.adjacent(new Point(x,y - 1)));
			
			assertFalse(test.adjacent(new Point(x + 1,y + 1)));
			assertFalse(test.adjacent(new Point(x + 1,y - 1)));
			assertFalse(test.adjacent(new Point(x - 1,y + 1)));
			assertFalse(test.adjacent(new Point(x - 1,y - 1)));
		}
	}
}
