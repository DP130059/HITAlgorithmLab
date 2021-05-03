package two;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.common.base.Charsets;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.common.io.Files;

public class Maze {
	public Table<Integer, Integer, Cube> MAZE;
	public int M;
	public int N;

	public Maze(int m, int n, String filePath) {
		this.M = m;
		this.N = n;
		MAZE = HashBasedTable.create();
		File file = new File(filePath);
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				Cube a=new Cube(i, j, 0);
				MAZE.put(i, j, a);
			}
		}

		try {
			int f = 0;
			List<String> readLines = Files.readLines(file, Charsets.UTF_8);
			for (int i = f; i < readLines.size(); i++) {
				String tmp = readLines.get(i);
				if(tmp.equals(""))
					break;
				int p = Integer.parseInt(tmp.split(",")[0]);
				int q = Integer.parseInt(tmp.split(",")[1]);
				if (p >= M || q >= N || p < 0 || q < 0) {
					String err = "第" + f + "行文件出现错误";
					throw new IndexOutOfBoundsException(err);
				}
				Cube cube = new Cube(p, q, Integer.MAX_VALUE);
				MAZE.remove(p, q);
				MAZE.put(p, q, cube);
				f += 1;
			}
			f+=1;
			for (int i = f ; i < readLines.size(); i++) {
				String tmp = readLines.get(i);
				if(tmp.equals(""))
					break;
				int p = Integer.parseInt(tmp.split(",")[0]);
				int q = Integer.parseInt(tmp.split(",")[1]);
				if (p >= M || q >= N || p < 0 || q < 0) {
					String err = "第" + f + "行文件出现错误";
					throw new IndexOutOfBoundsException(err);
				}
				Cube cube = new Cube(p, q, 15);
				MAZE.remove(p, q);
				MAZE.put(p, q, cube);
				f += 1;
			}
			f+=1;
			for (int i = f; i < readLines.size(); i++) {
				String tmp = readLines.get(i);
				if(tmp.equals(""))
					break;
				int p = Integer.parseInt(tmp.split(",")[0]);
				int q = Integer.parseInt(tmp.split(",")[1]);
				if (p >= M || q >= N || p < 0 || q < 0) {
					String err = "第" + f + "行文件出现错误";
					throw new IndexOutOfBoundsException(err);
				}
				Cube cube = new Cube(p, q, 20);
				MAZE.remove(p, q);
				MAZE.put(p, q, cube);
				f += 1;
			}

		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (IndexOutOfBoundsException e) {
			System.out.print(e.getMessage());
			e.printStackTrace();
		}
	}

	public Cube get(int i, int j) {
		return MAZE.get(i, j);
	}

	public List<Cube> getArround(Cube center) {
		List<Cube> result = new ArrayList<>();
		int p = center.getM(), q = center.getN();
		if (p == 0 && q == 0) {
			List<Cube> tmp = Arrays.asList(MAZE.get(1, 0), MAZE.get(0, 1), MAZE.get(1, 1));
			for (Cube a : tmp) {
				if (a.getCost() != Integer.MAX_VALUE)
					result.add(a);
			}
		} else if (p == 0 && q == N - 1) {
			List<Cube> tmp = Arrays.asList(MAZE.get(1, N - 1), MAZE.get(0, N - 2), MAZE.get(1, N - 2));
			for (Cube a : tmp) {
				if (a.getCost() != Integer.MAX_VALUE)
					result.add(a);
			}
		} else if (p == M - 1 && q == N - 1) {
			List<Cube> tmp = Arrays.asList(MAZE.get(M - 2, N - 1), MAZE.get(M - 1, N - 2), MAZE.get(M - 2, N - 2));
			for (Cube a : tmp) {
				if (a.getCost() != Integer.MAX_VALUE)
					result.add(a);
			}
		} else if (p == M - 1 && q == 0) {
			List<Cube> tmp = Arrays.asList(MAZE.get(M - 2, 0), MAZE.get(M - 1, 1), MAZE.get(M - 2, 1));
			for (Cube a : tmp) {
				if (a.getCost() != Integer.MAX_VALUE)
					result.add(a);
			}
		} else if (p == 0 && q != 0 && q != N - 1) {
			List<Cube> tmp = new ArrayList<>();
			tmp.add(MAZE.get(p,q+1));
			tmp.add(MAZE.get(p,q-1));
			tmp.add(MAZE.get(p+1,q-1));
			tmp.add(MAZE.get(p+1,q));
			tmp.add(MAZE.get(p+1,q+1));
			for (Cube a : tmp) {
				if (a.getCost() != Integer.MAX_VALUE)
					result.add(a);
			}
		} else if (p == M - 1 && q != 0 && q != N - 1) {
			List<Cube> tmp = new ArrayList<>();
			tmp.add(MAZE.get(p,q+1));
			tmp.add(MAZE.get(p,q-1));
			tmp.add(MAZE.get(p-1,q-1));
			tmp.add(MAZE.get(p-1,q));
			tmp.add(MAZE.get(p-1,q+1));
			for (Cube a : tmp) {
				if (a.getCost() != Integer.MAX_VALUE)
					result.add(a);
			}
		} else if (q == 0 && p != 0 && p != M - 1) {
			List<Cube> tmp = new ArrayList<>();
			tmp.add(MAZE.get(p+1,q));
			tmp.add(MAZE.get(p-1,q));
			tmp.add(MAZE.get(p-1,q+1));
			tmp.add(MAZE.get(p,q+1));
			tmp.add(MAZE.get(p+1,q+1));
			//System.out.println(tmp.isEmpty());
			for (Cube a : tmp) {
				if (a.getCost() != Integer.MAX_VALUE)
					result.add(a);
			}
		} else if (q == N - 1 && p != 0 && p != M - 1) {
			List<Cube> tmp = new ArrayList<>();
			tmp.add(MAZE.get(p+1,q));
			tmp.add(MAZE.get(p-1,q));
			tmp.add(MAZE.get(p-1,q-1));
			tmp.add(MAZE.get(p,q-1));
			tmp.add(MAZE.get(p+1,q-1));
			for (Cube a : tmp) {
				if (a.getCost() != Integer.MAX_VALUE)
					result.add(a);
			}
		} else {
			List<Cube> tmp =new ArrayList<Cube>();
			tmp.add(MAZE.get(p - 1, q - 1));
			tmp.add(MAZE.get(p - 1, q));
			tmp.add(MAZE.get(p - 1, q + 1));
			tmp.add(MAZE.get(p, q - 1));
			tmp.add(MAZE.get(p, q + 1));
			tmp.add(MAZE.get(p + 1, q - 1));
			tmp.add(MAZE.get(p + 1, q));
			tmp.add(MAZE.get(p + 1, q + 1));
			for (Cube a : tmp) {
				if (a.getCost() != Integer.MAX_VALUE)
					result.add(a);
			}
		}
		return result;
	}

	public void setHvalues(Cube endCube) {
		for (Cube a : MAZE.values()) {
			double tmp = Math.abs(a.getM() - endCube.getM()) + Math.abs(a.getN() - endCube.getN());
			a.setH_value(tmp * 10);
		}
	}

}
