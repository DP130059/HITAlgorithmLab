package one;

public class Triangle {
	public Point A;
	public Point B;
	public Point C;
	public Triangle(Point A,Point B,Point C) {
		
		this.A=A;
		this.B=B;
		this.C=C;
		
	}
	public double area() {
		double result=0;
		double AB=this.A.distancefrom(this.B);
		double AC=this.A.distancefrom(this.C);
		double BC=this.B.distancefrom(this.C);
		double p=(AB+AC+BC)/2d;
		result=Math.sqrt(p*(p-AC)*(p-AB)*(p-BC));
		return result;
	}
	public boolean contain(Point P) {
		boolean result=false;
		if(P.equals(this.A)||P.equals(this.B)||P.equals(this.C))
			return false;
		Triangle PAB=new Triangle(P, this.A, this.B);
		Triangle PAC=new Triangle(P, this.A, this.C);
		Triangle PBC=new Triangle(P, this.B, this.C);
		double PABarea=PAB.area();
		double PACarea=PAC.area();
		double PBCarea=PBC.area();
		double area=this.area();
		if(Math.abs(PABarea+PACarea+PBCarea-area)<=1E-9)
			result=true;
		return result;
	}
	public Point getA() {
		return A;
	}
	public Point getB() {
		return B;
	}
	public Point getC() {
		return C;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((A == null) ? 0 : A.hashCode());
		result = prime * result + ((B == null) ? 0 : B.hashCode());
		result = prime * result + ((C == null) ? 0 : C.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Triangle))
			return false;
		Triangle other = (Triangle) obj;
		if (A == null) {
			if (other.A != null)
				return false;
		} else if (!A.equals(other.A))
			return false;
		if (B == null) {
			if (other.B != null)
				return false;
		} else if (!B.equals(other.B))
			return false;
		if (C == null) {
			if (other.C != null)
				return false;
		} else if (!C.equals(other.C))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Triangle [A=" + A + ", B=" + B + ", C=" + C + "]";
	}
	

}
