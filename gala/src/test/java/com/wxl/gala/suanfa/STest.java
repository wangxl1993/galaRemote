package com.wxl.gala.suanfa;

import org.junit.jupiter.api.Test;

public class STest {

    @Test
    public void trans() {
        String s = "hello world ni hao";
        String[] sArr = s.split(" ");
        if (sArr != null && sArr.length > 1) {
            for (int i = 0, j = sArr.length - 1; i < j; i++, j--) {
                String tem = sArr[i];
                sArr[i] = sArr[j];
                sArr[j] = tem;
            }
        }
        String result = String.join(" ", sArr);
        System.out.println(result);

    }

    @Test
    public void arrTest() {
        String ss = "ab cd e f gh";
        char cc = ',';
        System.out.println(Character.isUpperCase(cc));
    }

}
