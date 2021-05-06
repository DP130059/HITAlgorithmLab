package four;

import java.io.File;
import java.io.IOException;
import java.util.*;

import org.javatuples.Pair;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

public class Four {
	public static final int HOW_MANY_NUMBERS =1000000;
	public static final int RATIO=1;

	public static void main(String[] args) throws java.lang.Exception {
		//generate();
		sort();		
	}
	
	public static void generate() {
		for(int i=0;i<11;i++) {
			Data tmp=new Data(i);
			Thread thread=new Thread(tmp);
			thread.start();
		}
	}
	public static void sort() {
		for(int i=0;i<=10;i++) {
			System.out.println(i);
			List<Double>dataList=readFromfile("result/4/originData."+i);
			List<Double>copyList=new ArrayList<Double>(HOW_MANY_NUMBERS);
			copyList.addAll(dataList);
			List<Double>copyList2=new ArrayList<Double>(HOW_MANY_NUMBERS);
			copyList2.addAll(dataList);
			long startTime = System.currentTimeMillis();
			//randomQuickSort(dataList, 0, HOW_MANY_NUMBERS - 1);
			long endTime =  System.currentTimeMillis();
			//System.out.println("随机快速排序算法用时： " + (endTime - startTime) + " ms");
			startTime= System.currentTimeMillis();
			copyList.sort(new Comparator<Double>() {

				@Override
				public int compare(Double o1, Double o2) {
					if(o1>o2)
						return 1;
					else 
						return -1;
				}
			});
			endTime= System.currentTimeMillis();
			System.out.println("Java实现的快速排序算法用时： " + (endTime - startTime) + " ms");
		    startTime = System.currentTimeMillis();
			randomQuickSortPlus(copyList2, 0, HOW_MANY_NUMBERS - 1);
			endTime =  System.currentTimeMillis();
			System.out.println("随机快速排序改进算法用时： " + (endTime - startTime) + " ms");
			
		}
	}
	public static void randomQuickSortPlus(List<Double>dataList,int p,int r) {
		if(p<r) {
			Pair<Integer, Integer>q=randomPartitionPlus(dataList, p, r);
			randomQuickSort(dataList, p,q.getValue0()-1);
			randomQuickSortPlus(dataList, q.getValue1()+1,r);
		}
	}
	public static Pair<Integer, Integer> randomPartitionPlus(List<Double>dataList,int p,int r){
		Random random=new Random();
		int randNumber=random.nextInt(r-p+1)+p;
		exchange(dataList, p, randNumber);
		double x=dataList.get(p);
		int i=p+1,j=r,k=p;
		while(i<=j) {
			if(dataList.get(i)<x) {
				exchange(dataList, i, k);
				i+=1;
				k+=1;
			}else if(dataList.get(i)>x) {
				exchange(dataList, i, j);
				j-=1;
			}else {
				i+=1;
			}
		}
		return new Pair<Integer, Integer>(k, j);
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

	public static void generateData(int i)  {
		int repeatcount=i*HOW_MANY_NUMBERS/10;
		List<Double>dataList=readFromfile("result/4/originData.txt");
		List<Double>result=new ArrayList<Double>(repeatcount);
		Random random=new Random();
		int index=random.nextInt(HOW_MANY_NUMBERS);
		double repeat=dataList.get(index);		
		for(int j=0;j<repeatcount;j++) {
			result.add(repeat);
		}
		Set<Double>last=new HashSet<Double>(HOW_MANY_NUMBERS-repeatcount);
		 while(last.size()<HOW_MANY_NUMBERS-repeatcount) {
			 index=random.nextInt(HOW_MANY_NUMBERS);
			 double tmp=dataList.get(index);
			 last.add(tmp);
		 }
		 result.addAll(last);
		 for(int j=0;j<repeatcount;j++) {
			 if(i==10) {
				 break;
			 }
			 index=random.nextInt(HOW_MANY_NUMBERS-repeatcount)+repeatcount;
			 if(Math.random()>0.618) {
				 exchange(result, j, index);
			 }
			
		 }
		writeTofile("result/4/originData."+i,result);
	}
	
	public static List<Double> readFromfile(String filePath)   {
		List<Double>result=new ArrayList<Double>(HOW_MANY_NUMBERS);
		File file=new File(filePath);
		List<String> readlines;
		try {
			readlines = Files.readLines(file, Charsets.UTF_8);
			for(String m:readlines) {
				double tmp=Double.parseDouble(m);
				result.add(tmp);
			}
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
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
class Data implements Runnable{
	public int i;
	
	public Data(int i) {
		this.i=i;
	}

	@Override
	public void run() {
		Four.generateData(i);
		System.out.println("第"+i+"个文件已生成");
	}
	
}
