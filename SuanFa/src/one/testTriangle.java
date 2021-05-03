package one;

import static org.junit.Assert.*;

import org.junit.Test;

public class testTriangle {

	@Test
	public void test() {
		Point A=new Point(0d,0d);
		Point B=new Point(10d,0d);
		Point C=new Point(10d,10d);
		Point P=new Point(1d,0d);
		Triangle triangle=new Triangle(A, B, C);
		assertTrue(C.judge2(A, B));
	}

}
