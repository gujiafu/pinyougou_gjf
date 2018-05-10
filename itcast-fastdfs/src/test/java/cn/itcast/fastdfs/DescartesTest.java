package cn.itcast.fastdfs;

/**
 * @author GuJiaFu
 * @date 2018/5/7
 */
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DescartesTest {

    @Test
    public void testList(){
        String[][] oriArray = {{"1","2"},{"3","4"},{"5","6","7"}};
        List<String[]> list = new ArrayList<>();
        String[] array = new String[oriArray.length];
        getList(oriArray, 0, 0, list, array);
        int index =1;
        for (String[] strs : list) {
            String s ="";
            for (int i = 0; i < strs.length; i++) {
                s+=strs[i]+"--";
            }
            System.out.println(index+":  "+s);
            index++;
        }

    }

    /**
     *求二维数组的笛卡尔积
     * @param oriArray   需要求解的二维数组
     * @param oneIndex   二维数组在一维数组的索引
     * @param twoIndex   二维数组中元素的索引
     * @param oneList    结果集合
     * @param array      二维数组
     * @return
     */
    public List<String[]> getList(String[][] oriArray, int oneIndex, int twoIndex, List<String[]> oneList,String[] array){

        for (int i = 0; i < oriArray[oneIndex].length; i++) {
            // 遍历每个数组
            array[twoIndex] = oriArray[oneIndex][i];
            // 判断该数组是否最后一个数组,如果不是就到下一个数组
            if(oneIndex < oriArray.length-1){
                getList(oriArray,oneIndex+1, twoIndex+1, oneList, array);
            }else{
                // 到了最后一个数组,把数组中的元素添加到结合中
                String[] strings = Arrays.copyOf(array, array.length);
                oneList.add(strings);
            }
        }
        return oneList;
    }

}
