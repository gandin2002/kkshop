package cn.bohoon.product.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DescartesUtil {
	
	/** 
     * 递归实现dimValue中的笛卡尔积，结果放在result中 
     * @param dimValue 原始数据 
     * @param result 结果数据 
     * @param layer dimValue的层数 
     * @param curList 每次笛卡尔积的结果 
     */  
    public static <E> void recursive (List<List<E>> dimValue, List<List<E>> result, int layer, List<E> curList) {  
        if (layer < dimValue.size() - 1) {  
            if (dimValue.get(layer).size() == 0) {  
                recursive(dimValue, result, layer + 1, curList);  
            } else {  
                for (int i = 0; i < dimValue.get(layer).size(); i++) {  
                    List<E> list = new ArrayList<>(curList);  
                    list.add(dimValue.get(layer).get(i));  
                    recursive(dimValue, result, layer + 1, list);  
                }  
            }  
        } else if (layer == dimValue.size() - 1) {  
            if (dimValue.get(layer).size() == 0) {  
                result.add(curList);  
            } else {  
                for (int i = 0; i < dimValue.get(layer).size(); i++) {  
                    List<E> list = new ArrayList<>(curList);  
                    list.add(dimValue.get(layer).get(i));  
                    result.add(list);  
                }  
            }  
        }  
    }  
  
	

	
    public static List<String> descartes(List<List<String>> dimValue) {
        int total = 1;
        for (int i = 0; i < dimValue.size(); i++) {
            total *= dimValue.get(i).size();
        }
        String[] myResult = new String[total];
        int now = 1;
        int itemLoopNum = 1;
        int loopPerItem = 1;
        for (int i = 0; i < dimValue.size(); i++) {
            List<String> temp = dimValue.get(i);
            now = now * temp.size();
            int index = 0;
            int currentSize = temp.size();
            itemLoopNum = total / now;
            loopPerItem = total / (itemLoopNum * currentSize);
            int myindex = 0;
            for (int j = 0; j < temp.size(); j++) {
                for (int k = 0; k < loopPerItem; k++) {
                    if (myindex == temp.size())
                        myindex = 0;
                    for (int m = 0; m < itemLoopNum; m++) {
                        myResult[index] = (myResult[index] == null ? "" : myResult[index] + ",") + ((String) temp.get(myindex));
                        index++;
                    }
                    myindex++;
                }
            }
        }
        List<String> stringResult = Arrays.asList(myResult);
        return stringResult;
    }

}
