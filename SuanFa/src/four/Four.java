package four;

import java.io.File;
import java.io.IOException;
import java.util.*;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

public class Four {
	public static final int HOW_MANY_NUMBERS =1000000;
	public static final int RATIO=0;

	public static void main(String[] args) throws java.lang.Exception {
		List<Double> dataList = generateData(HOW_MANY_NUMBERS, RATIO);
		System.out.println("排序前数组已写入文件");
		//writeTofile("result/4/originData.txt", dataList);
		List<Double>copyList=new ArrayList<Double>(HOW_MANY_NUMBERS);
		copyList.addAll(dataList);
		long startTime = System.nanoTime();
		randomQuickSort(dataList, 0, HOW_MANY_NUMBERS - 1);
		long endTime = System.nanoTime();
		System.out.println("排序后数组已写入文件");
		//writeTofile("result/4/RandomQuickSortResult.txt", dataList);
		System.out.println("随机快速排序算法用时： " + (endTime - startTime) + " ns");
		startTime=System.nanoTime();
		copyList.sort(new Comparator<Double>() {

			@Override
			public int compare(Double o1, Double o2) {
				if(o1>o2)
					return 1;
				else 
					return -1;
			}
		});
		endTime=System.nanoTime();
		//writeTofile("result/4/SystemSortResult.txt", dataList);
		System.out.println("Java实现的快速排序算法用时： " + (endTime - startTime) + " ns");
	}

	public static void randomQuickSort(List<Double> dataList, int p, int r) {
		if (p < r) {
			int q = randomPartition(dataList, p, r);
			randomQuickSort(dataList, p, q - 1);
			randomQuickSort(dataList, q + 1, r);
		}

	}

	public static int randomPartition(List<Double> dataList, int p, int r) {
		int result = 0;
		Random random = new Random();
		result = random.nextInt(r - p + 1) + p;
		exchange(dataList, r, result);
		double tmp = dataList.get(r);
		int i = p - 1;
		for (int j = p; j < r; j++) {
			if (dataList.get(j) <= tmp) {
				i += 1;
				exchange(dataList, i, j);
			}
		}
		exchange(dataList, i + 1, r);
		return i + 1;
	}

	public static void exchange(List<Double> dataList, int a, int b) {
		double tmp = dataList.get(a);
		dataList.set(a, dataList.get(b));
		dataList.set(b, tmp);
	}

	public static List<Double> generateData(int n, int i) {
		List<Double> result = new ArrayList(n);
		Map<Double, Integer> count = new HashMap(n);
		int k = 0;
		while (k < n) {
			double tmp = Math.random() * 100d;
			if (result.contains(tmp)) {
				int c = count.get(tmp);
				if (c <= n * i / 10) {
					c += 1;
					count.remove(tmp);
					count.put(tmp, c);
					k += 1;
				} else {
					continue;
				}
			} else {
				result.add(tmp);
				count.put(tmp, 1);
				k += 1;
			}
		}
		return result;
	}

	public static void writeTofile(String filePath, List<Double> dataList) {
			StringBuffer sb=new StringBuffer();
			for(double m:dataList) {
				sb.append(m);
				sb.append("\n");
			}
			
			try {
				File file=new File(filePath);
				if(!file.exists())
					file.createNewFile();
				Files.write(sb, file, Charsets.UTF_8);
			} catch (IOException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}

}
