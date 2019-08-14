package com.foodie.product.util;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.foodie.base.entity.Dish;
import com.foodie.base.entity.DishBase;
import com.foodie.base.entity.Product;
import com.foodie.base.entity.ProductBase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ACacheUtil {
    public static Map<Integer, ProductBase> changeFormProduct(Map<Integer, Product> productMap){
        Map<Integer, ProductBase> productBaseMap = new HashMap<>();
        for (Map.Entry<Integer, Product> entry : productMap.entrySet()) {

            productBaseMap.put(entry.getKey(),setProductBase(entry.getValue()));

        }
        return productBaseMap;
    }

    public static Map<Integer, Product > changeToProduct(Map<Integer, ProductBase> productBaseMap){
        Map<Integer, Product> productMap = new HashMap<>();
        for (Map.Entry<Integer, ProductBase> entry : productBaseMap.entrySet()) {
            productMap.put(entry.getKey(),setProduct(entry.getValue()));
        }
        return productMap;
    }
    private static Product setProduct(ProductBase productBase){
        Product product = new Product();
        MutableLiveData<Integer> num=new MutableLiveData<>();
        num.setValue(productBase.getNumber());
        product.setNum(num);
        product.setId(productBase.getId());
        product.setPriceForShop(productBase.getPriceForShop());
        product.setPriceForUser(productBase.getPriceForUser());
        product.setProductName(productBase.getProductName());
        product.setPhotoUrl(productBase.getPhotoUrl());
        product.setMinusNum(productBase.getMinusNum());
        product.setFullNum(productBase.getFullNum());
     return product;
    }

    private static ProductBase setProductBase(Product product){
        ProductBase productBase = new ProductBase();
        productBase.setNumber(product.getNum().getValue());
        productBase.setId(product.getId());
        productBase.setPriceForShop(product.getPriceForShop());
        productBase.setPriceForUser(product.getPriceForUser());
        productBase.setProductName(product.getProductName());
        productBase.setPhotoUrl(product.getPhotoUrl());
        productBase.setMinusNum(product.getMinusNum());
        productBase.setFullNum(product.getFullNum());
        return productBase;
    }


}
