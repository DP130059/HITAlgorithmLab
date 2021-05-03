package four;

import java.util.*;

public class QuickSort {

    public static void main(String[] args) {
//        int[] nums = new int[]{1, 2, 5, 3, 8, -4};
//        int[] nums2 = new int[]{1, 2, 9 ,7 ,8 ,6};
//        quickSort(nums, 0, nums2.length - 1);
//        quickSort(nums2, 0, nums2.length - 1);
//        for(int i : nums){
//            System.out.print(i + " ");
//        }
//        System.out.println();
//        for(int i : nums2){
//            System.out.print(i + " ");
//        }
//
//        int res[] = generateDataArray(20,3);
//        for(int i : res){
//            System.out.print(i + " ");
//        }
        for(int i = 0;i < 11;i ++){
            int[] nums = generateDataArray(1000000, i);
            int[] copy=Arrays.copyOf(nums, 1000000);
            long startTime = System.currentTimeMillis();
            quickSort(nums, 0, nums.length - 1);
            long endTime  = System.currentTimeMillis();
            System.out.println("重复率为" + i * 10+ "%的数组排序所花费的时间长为" + (endTime - startTime) + "ms");
            startTime=System.currentTimeMillis();
            Arrays.sort(copy);
            endTime=System.currentTimeMillis();
            System.out.println("重复率为" + i * 10+ "%的数组系统排序所花费的时间长为" + (endTime - startTime) + "ms");
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
    //rate表示每10个数里面有几个重复的
    public static int[] generateDataArray(int length, int rate){
        int[] res = new int[length];
        int count = 0;
        int temp = rate;
        while(count < length){
            if(count % 10 == 0){
                rate = temp;
            }
            res[count ++] = count;
            while(rate > 1){
                res[count] = res[count - 1];
                count ++;
                rate --;
            }
        }
        //最后再打乱数组
        for(int i=0;i < res.length;i++){
            int index = (int)(Math.random() * length);
            temp = res[index];
            res[index] = res[i];
            res[i] = temp;
        }
        return res;
    }
}
