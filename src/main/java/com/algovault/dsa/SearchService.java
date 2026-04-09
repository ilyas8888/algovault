package com.algovault.dsa;

import org.springframework.stereotype.Service;

@Service
public class SearchService {

    public int linearSearch(int[] array, int target) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == target) {
                return i;
            }
        }
        return -1;
    }

    public int binarySearch(int[] array, int target) {
        int min=0;
        int max=array.length-1;
        while (max>=min){
            int mid = (max+min)/2;
            if (array[mid] == target) {
                return mid;
            }
            if (array[mid] < target) {
                min = mid + 1;
            }
            else if (array[mid] > target) {
                max = mid - 1;
            }
        }
        return -1;
    }

}