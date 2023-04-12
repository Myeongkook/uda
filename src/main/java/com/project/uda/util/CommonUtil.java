package com.project.uda.util;

import java.util.Random;

public class CommonUtil {

    public static String generateKey(int length){
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for(int i=0;i<length;i++){
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }
}
