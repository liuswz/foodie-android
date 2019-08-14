package com.foodie.home.util;

import androidx.lifecycle.MutableLiveData;

import com.foodie.base.entity.Dish;
import com.foodie.base.entity.DishBase;
import com.foodie.base.entity.Product;
import com.foodie.base.entity.ProductBase;

import java.util.HashMap;
import java.util.Map;

public class ACacheUtil {

    public static Map<Integer, DishBase> changeFormDish(Map<Integer, Dish> dishMap){
        Map<Integer, DishBase> dishBaseMap = new HashMap<>();
        for (Map.Entry<Integer, Dish> entry : dishMap.entrySet()) {

            dishBaseMap.put(entry.getKey(),setDishBase(entry.getValue()));

        }
        return dishBaseMap;
    }

    public static Map<Integer, Dish> changeToDish(Map<Integer, DishBase> dishBaseMap){
        Map<Integer, Dish> dishMap = new HashMap<>();
        for (Map.Entry<Integer, DishBase> entry : dishBaseMap.entrySet()) {
            dishMap.put(entry.getKey(),setDish(entry.getValue()));
        }
        return dishMap;
    }
    private static Dish setDish(DishBase dishBase){
        Dish dish = new Dish();
        MutableLiveData<Integer> num=new MutableLiveData<>();
        num.setValue(dishBase.getNumber());
        dish.setNum(num);
        dish.setId(dishBase.getId());
        dish.setPrice(dishBase.getPrice());
        dish.setName(dishBase.getName());
        dish.setPhotoUrl(dishBase.getPhotoUrl());
        return dish;
    }

    private static DishBase setDishBase(Dish dish){
        DishBase dishBase = new DishBase();
        dishBase.setNumber(dish.getNum().getValue());
        dishBase.setId(dish.getId());
        dishBase.setPrice(dish.getPrice());
        dishBase.setPhotoUrl(dish.getPhotoUrl());
        dishBase.setName(dish.getName());
        return dishBase;
    }


}
