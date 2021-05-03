package two;

public class Cube {
	public int m;
	public int n;
	public double cost=0;
	public double G_value=0;
	public double H_value=0;
	public double F_value=0;
	public Cube Parent;

	public Cube(int m,int n,double cost) {
		this.m=m;
		this.n=n;
		this.cost=cost;
	}

	public int getM() {
		return m;
	}

	public void setM(int m) {
		this.m = m;
	}

	public int getN() {
		return n;
	}

	public void setN(int n) {
		this.n = n;
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	public double getG_value() {
		return G_value;
	}

	public void setG_value(double g_value) {
		G_value = g_value;
		this.F_value=this.G_value+this.H_value;
	}

	public double getH_value() {
		return H_value;
	}

	public void setH_value(double h_value) {
		H_value = h_value;
		this.F_value=this.G_value+this.H_value;
	}

	public double getF_value() {
		return F_value;
	}

	public void setF_value(double f_value) {
		F_value = f_value;
	}

	public Cube getParent() {
		return Parent;
	}

	public void setParent(Cube parent) {
		Parent = parent;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + m;
		result = prime * result + n;
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
		Cube other = (Cube) obj;
		if (m != other.m)
			return false;
		if (n != other.n)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "£¨" + m + ", " + n + "£©, cost=" + cost + ", G=" + G_value + ", H=" + H_value
				+ ", F=" + F_value + "\n";
	}
	
	public String write() {
		return m+","+n;
	}
	
	public Cube copy() {
		return new Cube(this.m, this.n, 0d);
	}

}
