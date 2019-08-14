package com.foodie.base.util;

import java.util.Date;
import java.util.Random;

public class BaseUtil {

    public static String getPhotoName(){
        return new Date().getTime()+""+((int)(new Random().nextDouble()*(99999-10000 + 1))+ 10000)+"";
    }
}
