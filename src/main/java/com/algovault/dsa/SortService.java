package com.algovault.dsa;

import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class SortService {
    public int[] bubbleSort(int[] arr) {
        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            for(int j = 0; j < n - i - 1; j++){
                if(arr[j] > arr[j+1]){
                    int temp = arr[j];
                    arr[j] = arr[j+1];
                    arr[j+1] = temp;
                }
            }
        }
        return arr;
    }
    public int[] mergeSort(int[] array){
        if(array.length <= 1){
            return array;
        }
        int mid = array.length/2;
        int max = array.length;
        int[] left=mergeSort(Arrays.copyOfRange(array,0,mid));
        int[] right=mergeSort(Arrays.copyOfRange(array, mid, max));
        return merge(left, right);
    }
    public int[] merge(int[] left, int[] right){
        int[] result = new int[left.length + right.length];
        int i = 0, j = 0, k = 0;

        while (i < left.length && j < right.length) {
            if (left[i] <= right[j]) {
                result[k++] = left[i++];
            } else {
                result[k++] = right[j++];
            }
        }

        while (i < left.length)  result[k++] = left[i++];
        while (j < right.length) result[k++] = right[j++];

        return result;
    }
}
