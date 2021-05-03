package one;

public class Point {
	double x;
	double y;
	public Point(double x,double y) {
		this.x=x;
		this.y=y;
	}
	public double getX() {
		return x;
	}
	public double getY() {
		return y;
	}
	public double distancefrom(Point another) {
		double result=0;
		double tmpx=another.x-this.x,tmpy=another.y-this.y;
		result=Math.sqrt(Math.pow(tmpx, 2d)+Math.pow(tmpy, 2d));
		return result;
	}
	public boolean judge2(Point A, Point B) {
		boolean result=false;
		double tmp=A.x*B.y+this.x*A.y+B.x*this.y-this.x*B.y-B.x*A.y-A.x*this.y;
		if(tmp>0)
			result=true;
		return result;
	}
	public boolean judge(Point another,Point P) {		
		double x1=this.x-P.x,y1=this.y-P.y;
		double x2=another.x-P.x,y2=another.y-P.y;
		double tmp=x1*y2-x2*y1;
		return tmp<0;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(x);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(y);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Point other = (Point) obj;
		if (x!= other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "" + x + ", " + y + "";
	}
	

}
