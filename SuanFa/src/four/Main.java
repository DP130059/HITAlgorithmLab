package four;

import java.util.*;

public class Main {
    public static void main(String[] args) {
    	
        for(int i = 0;i < 11;i ++){
        	int[] nums = generateDataArray(1000000, i);            
            long startTime = System.currentTimeMillis();
            quickSort(nums, 0, nums.length - 1);
            long endTime  = System.currentTimeMillis();
            System.out.println("重复率为" + i * 10+ "%的数组排序所花费的时间长�?" + (endTime - startTime) + "ms");
            System.gc();
        }
    }
    public static void quickSort(int[] nums, int left, int right){
        if(left < right){
            int k = random_Partition(nums, left, right);
            quickSort(nums, left, k - 1);
            quickSort(nums, k + 1, right);
        }
    }
    public static int random_Partition(int[] nums, int left, int right){
        Random rand = new Random();
        int randNumber = rand.nextInt(right - left + 1) + left;
        int temp = nums[right];
        nums[right] = nums[randNumber];
        nums[randNumber] = temp;
        int x = nums[right];
        randNumber = left - 1;
        for(int j = left;j < right;j ++){
            if(nums[j] <= x){
                randNumber ++;
                int t = nums[j];
                nums[j] = nums[randNumber];
                nums[randNumber] = t;
            }
        }
        temp = nums[right];
        nums[right] = nums[randNumber + 1];
        nums[randNumber + 1] = temp;
        return randNumber + 1;
    }
    //rate表示10*rate %的重复率
    public static int[] generateDataArray(int length, int rate){
        int[] res = new int[length];
        if(rate == 0){
            for(int i = 0;i < length;i++){
                res[i] = i;
            }
        }else if(rate == 10){
            Random rand = new Random();
            int randNumber = rand.nextInt(length);
            for(int i : res)
                i = randNumber;
        }else{
            Random rand = new Random();
            int randNumber = rand.nextInt(length);
            int numsOfRand = (int)(rate * 0.1 * length);
            boolean flag = false;
            for(int i = 0;i < length;i++){
                if(numsOfRand > 0) {
                    res[i] = randNumber;
                    numsOfRand--;
                    continue;
                }
                if(flag){
                    res[i] = i + 1;
                }else{
                    if(i == randNumber){
                        res[i] = i + 1;
                        flag = true;
                    }else{
                        res[i] = i;
                    }
                }
            }
        }
        //�?后再打乱数组
        for(int i=0;i < res.length;i++){
            int index = (int)(Math.random() * length);
            int temp = res[index];
            res[index] = res[i];
            res[i] = temp;
        }
        return res;
    }
}
