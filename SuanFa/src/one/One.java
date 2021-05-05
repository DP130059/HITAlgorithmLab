package one;

import java.io.File;
import java.io.IOException;
import java.util.*;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

public class One {
	public static final int HOW_MANY_POINTS = 6000;
	public static final List<Point> Points = new ArrayList<Point>(HOW_MANY_POINTS);
	public static final List<Point> divideConquerResult = new ArrayList<Point>();
	public static final Map<Point, Boolean> visitedRecord = new HashMap<Point, Boolean>();

	public static void main(String[] args) {
		generatePoints();
		//brutal();
		Graham_Scan();
		divide_Conquer();

	}

	public static void divide_Conquer() {
		List<Point> copy = new ArrayList<Point>();
		copy.addAll(Points);
		for (Point p : copy) {
			visitedRecord.put(p, false);
		}
		long startTime =System.nanoTime();;
		List<Point> sortPoints = sortx(copy);
		Point p_0 = sortPoints.get(0);
		Point p_n = sortPoints.get(1);
		divideConquerResult.add(p_0);
		divideConquerResult.add(p_n);
		visitedRecord.remove(p_0);
		visitedRecord.put(p_0, true);
		visitedRecord.remove(p_n);
		visitedRecord.put(p_n, true);
		copy.remove(p_0);
		copy.remove(p_n);
		List<Point> upPoints = new ArrayList<Point>();
		List<Point> downPoints = new ArrayList<Point>();
		for (Point p : Points) {
			if (p.judge2(p_0, p_n)) 
				upPoints.add(p);
			else
				downPoints.add(p);
		}
		divide(upPoints,p_0,p_n);
		divide(downPoints,p_0,p_n);
		long endTime = System.nanoTime();;
		System.out.println("分治算法用时： " + (endTime - startTime) + "ns");
		//writeTofile("result/1/DivideAndConquerResult.txt", divideConquerResult);
	}

	public static void divide(List<Point> points,Point p_0,Point p_n) {		
		double maxArea=Double.MIN_NORMAL;
		Point p_max=points.get(0);
		for(Point p: points) {
			Triangle tmp=new Triangle(p_0, p_n, p);
			double tmpArea=tmp.area();
			if(tmpArea>maxArea) {
				maxArea=tmpArea;
				p_max=p;
			}
			if(tmpArea==0) {
				visitedRecord.remove(p);
				visitedRecord.put(p, true);
			}
		}
		divideConquerResult.add(p_max);
		visitedRecord.remove(p_max);
		visitedRecord.put(p_max, true);
		points.remove(p_max);
		for(Point p:points) {
			Triangle tmp=new Triangle(p_0, p_n,p_max);
			if(tmp.contain(p)) {
				visitedRecord.remove(p);
				visitedRecord.put(p, true);
			}				
		}
		List<Point>upPoints=new ArrayList<Point>();
		List<Point>downPoints=new ArrayList<Point>();
		for(Point p:points) {
			if(p.judge2(p_0, p_max)&&!visitedRecord.get(p))
				upPoints.add(p);
			else if(p.judge2(p_max, p_n)&&!visitedRecord.get(p))
				downPoints.add(p);
		}
		if(upPoints.size()<=1) {
			Point p=points.get(0);
			divideConquerResult.add(p);
			visitedRecord.remove(p);
			visitedRecord.put(p, true);
			return;
		}
		if(downPoints.size()<=1) {
			Point p=points.get(0);
			divideConquerResult.add(p);
			visitedRecord.remove(p);
			visitedRecord.put(p, true);
			return;
		}
		List<Point>tmp=sortx(upPoints);
		divide(upPoints, tmp.get(0), tmp.get(1));
		tmp=sortx(downPoints);
		divide(downPoints, tmp.get(0),tmp.get(1));		

	}
	public static void Graham_Scan() {
		long startTime = System.nanoTime();;
		List<Point> copy = new ArrayList<Point>();
		copy.addAll(Points);
		List<Point> result = new ArrayList<Point>();
		Point p = sorty(Points);
		copy.remove(0);
		copy.sort(new Comparator<Point>() {
			@Override
			public int compare(Point o1, Point o2) {
				if (o1.judge(o2, p))
					return 1;
				else
					return -1;
			}
		});
		Stack<Point> pointStack = new Stack<Point>();
		pointStack.push(copy.get(0));
		pointStack.push(copy.get(1));
		pointStack.push(copy.get(2));
		for (int i = 3; i < HOW_MANY_POINTS - 1; i++) {
			Point pi = copy.get(i);
			while (true) {
				if(pointStack.size()==1)
					break;
				Point top = pointStack.pop();
				Point nexttotop = pointStack.peek();
				boolean judge = top.judge(pi, nexttotop);
				if (!judge) {
					pointStack.push(top);
					break;
				}
			}
			pointStack.push(pi);

		}
		result.addAll(pointStack);
		long endTime = System.nanoTime();
		System.out.println("Graham_Scan算法用时： " + (endTime - startTime) + "ns");
		//writeTofile("result/1/Graham_ScanResult.txt", result);

	}

	public static Point sorty(List<Point> copy) {
		copy.sort(new Comparator<Point>() {
			@Override
			public int compare(Point o1, Point o2) {
				if (o1.y < o2.y)
					return -1;
				else {
					return 1;
				}
			}
		});
		return copy.get(0);
	}

	public static List<Point> sortx(List<Point> points) {
		List<Point> result = new ArrayList<Point>();
		points.sort(new Comparator<Point>() {

			@Override
			public int compare(Point o1, Point o2) {
				if (o1.x < o2.x)
					return -1;
				else {
					return 1;
				}
			}
		});
		result.add(points.get(0));
		result.add(points.get(points.size() - 1));
		return result;
	}

	public static void generatePoints() {
		int k = 0;
		while (k < HOW_MANY_POINTS) {
			double X = 100d * Math.random(), Y = 100d * Math.random();
			Point point = new Point(X, Y);
			if (Points.contains(point))
				continue;
			else {
				Points.add(point);
				k += 1;
			}
		}
		System.out.println("点对生成完成");
		writeTofile("result/1/points.txt", Points);
	}

	public static void writeTofile(String filePath, List<Point> points) {
		try {
			File file = new File(filePath);
			if (!file.exists())
				file.createNewFile();
			StringBuffer sb = new StringBuffer();
			Iterator<Point> pointsIterator = points.iterator();
			while (pointsIterator.hasNext()) {
				Point point = (Point) pointsIterator.next();
				sb.append(point.toString());
				sb.append("\n");
			}
			Files.write(sb, file, Charsets.UTF_8);
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}

	}

	public static void brutal() {
		long startTime = System.currentTimeMillis();
		List<Point> deletelist = new ArrayList<Point>();
		for (int i = 0; i < Points.size(); i++) {
			for (int j = 0; j < Points.size(); j++) {
				for (int k = 0; k < Points.size(); k++) {
					if (i != j && j != k && i != k) {
						Point A = Points.get(i);
						Point B = Points.get(j);
						Point C = Points.get(k);
						Triangle triangle = new Triangle(A, B, C);
						for (int m = 0; m < Points.size(); m++) {
							Point P = Points.get(m);
							if (m != i && m != j && m != k) {
								if (triangle.contain(P))
									deletelist.add(P);
							}
						}
					}
					//System.gc();
				}

			}
		}
		List<Point> copy = new ArrayList<Point>();
		for (Point m : Points) {
			if (!deletelist.contains(m))
				copy.add(m);
		}
		long endTime = System.currentTimeMillis();
		System.out.println("暴力遍历算法用时： " + (endTime - startTime) + "ms");
		//writeTofile("result/1/brutal.txt", copy);
	}

}
