package com.foodie.product.ui.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.foodie.base.base.BaseJson;
import com.foodie.base.entity.Product;
import com.foodie.base.entity.Product;
import com.foodie.base.enums.ResultCode;
import com.foodie.base.entity.PageResults;
import com.foodie.base.config.RetrofitServiceManager;
import com.foodie.base.entity.Product;
import com.foodie.product.service.networkService.ProductService;
import com.rxjava.rxlife.RxLife;
import com.rxjava.rxlife.ScopeViewModel;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by goldze on 2018/6/21.
 */

public class ProductViewModel extends ScopeViewModel {

    public MutableLiveData<List<Product>> productList;
    public MutableLiveData<Product> productDetail;
    private Integer size=10;
    private int total;
    private String type="大米";
    public Double sumPrice=0D;
    public MutableLiveData<Map<Integer, Product>> goodCarItemMap;
    public List<String> idList;

    private Integer id;
    public void setId(Integer id){
        this.id = id;
    }
    public int getTotal(){
        return total;
    }
    public void setType(String type){
        this.type = type;
    }
    public ProductViewModel(@NonNull Application application) {
        super(application);
        productList = new MutableLiveData<>();

        goodCarItemMap = new MutableLiveData<>();
        idList = new LinkedList<String>();
        productDetail = new MutableLiveData<>();
    }
    public void clearGoodCar(){
        idList.clear();
        Map<Integer,Product> map =  goodCarItemMap.getValue();
        map.clear();
        goodCarItemMap.setValue(map);

        sumPrice=0D;
        for(Product product:productList.getValue()){
            if(product.getNum()==null){
                break;
            }
            product.getNum().setValue(0);
        }
//        for(Product product:hotProductList.getValue()){
//            product.getNum().setValue(0);
//        }
    }

    public void clearGoodCarInDetail(){
        idList.clear();
        Map<Integer,Product> map =  goodCarItemMap.getValue();
        map.clear();
        goodCarItemMap.setValue(map);

        sumPrice=0D;
        productDetail.getValue().getNum().setValue(0);
//        for(Product product:hotProductList.getValue()){
//            product.getNum().setValue(0);
//        }
    }
    public void addProductMap(Product product){

        Map<Integer,Product> map = goodCarItemMap.getValue();
        if(map==null){
            map = new HashMap<>();
        }
        if(!map.containsKey(product.getId())){
            map.put(product.getId(),product);
            idList.add(product.getId()+"");
        }
        sumPrice+=product.getPriceForUser();
        goodCarItemMap.setValue(map);
    }
    public void minusProductMap(Product product){
        Map<Integer,Product> map = goodCarItemMap.getValue();
        if(map.containsKey(product.getId())){
            Product goodCarItem =  map.get(product.getId());
            if(goodCarItem.getNum().getValue()==0){

                map.remove(product.getId());
                idList.remove(product.getId()+"");

            }
        }
        if(sumPrice!=0){
            sumPrice-=product.getPriceForUser();
        }
        goodCarItemMap.setValue(map);
    }


//    public void addGoodCarItem(Product product){
//
//        Map<Integer,Product> map = goodCarItemMap.getValue();
//        if(map==null){
//            map = new HashMap<>();
//        }
//        if(map.containsKey(product.getId())){
//            Product goodCarItem =  map.get(product.getId());
//            goodCarItem.setNum(goodCarItem.getNum()+1);
//            goodCarItem.setPrice(goodCarItem.getNum()*goodCarItem.getPrice());
//        }else{
//            Product goodCarItem = new Product();
//            //       goodCarItem.setId(product.getId());
//            goodCarItem.setName(product.getProductName());
//            goodCarItem.setPrice(product.getPriceForUser());
//            goodCarItem.setPhotoUrl(product.getPhotoUrl());
//            goodCarItem.setNum(1);
//            map.put(product.getId(),goodCarItem);
//            idList.add(product.getId()+"");
//        }
//        sumPrice+=product.getPriceForShop();
//        goodCarItemMap.setValue(map);
//    }
//    public void minusGoodCarItem(Product product){
//        Map<Integer,Product> map = goodCarItemMap.getValue();
//        if(map.containsKey(product.getId())){
//            Product goodCarItem =  map.get(product.getId());
//            if(goodCarItem.getNum()==1){
//                map.remove(product.getId());
//
//                idList.remove(product.getId()+"");
//            }else{
//                goodCarItem.setNum(goodCarItem.getNum()-1);
//                goodCarItem.setPrice(goodCarItem.getNum()*goodCarItem.getPrice());
//            }
//        }
//        sumPrice-=product.getPriceForShop();
//        goodCarItemMap.setValue(map);
//    }
    public void addGoodCarProductMap(Integer productId){

        Map<Integer,Product> map = goodCarItemMap.getValue();
        Product goodCarItem = map.get(productId);

        goodCarItem.getNum().setValue( goodCarItem.getNum().getValue()+1);

        sumPrice+=goodCarItem.getPriceForUser();
        goodCarItemMap.setValue(map);
    }
    public void minusGoodCarProductMap(Integer productId){

        Map<Integer,Product> map = goodCarItemMap.getValue();
        Product goodCarItem = map.get(productId);

        if(goodCarItem.getNum().getValue()==1){
            map.remove(productId);
            idList.remove(productId+"");
        }else{
            goodCarItem.getNum().setValue( goodCarItem.getNum().getValue()-1);
        }
        sumPrice-=goodCarItem.getPriceForUser();
        goodCarItemMap.setValue(map);

    }


    public Double  getSumPrice(){
        return sumPrice;
    }
    public void setSumPrice(Double price){
        this.sumPrice = price;
    }
    public void setIdList(List<String> idList){
        this.idList = idList;
    }
    public void setProductMap(Map<Integer, Product> goodCarItemMap){
        this.goodCarItemMap.setValue(goodCarItemMap);
    }
    public List<String> getIdList(){
        return idList;
    }
    public MutableLiveData<Map<Integer,Product>> getProductMap(){
        return  goodCarItemMap;
    }



    public void loadProductList(int page){

        RetrofitServiceManager.getInstance(getApplication()).create(ProductService.class)
                .findAllProductWithMoneyOffByType(page,size,type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxLife.<PageResults<Product>>as(this))
                .subscribe(new Consumer<PageResults<Product>>() {
                               @Override
                               public void accept(PageResults<Product> results) throws Exception {
                                   if(results.getCode()== ResultCode.SUCCESS.getIndex()){
                                       productList.setValue(setProduct(results.getRows()));
                                       total= (int) (results.getTotal()%size==0?results.getTotal()/size:results.getTotal()/size+1);
                                   }
                               }}, new Consumer<Throwable>() {
                               @Override
                               public void accept(Throwable throwable) throws Exception {
                                   Log.e("TAG>>>", "" + throwable.getMessage());

                               }
                           }
                );
    }
    public void loadProductDetail(){

        RetrofitServiceManager.getInstance(getApplication()).create(ProductService.class)
                .getProductDetailForUser(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxLife.<BaseJson<Product>>as(this))
                .subscribe(new Consumer<BaseJson<Product>>() {
                               @Override
                               public void accept(BaseJson<Product> baseJson) throws Exception {
                                   if(baseJson.getCode()== ResultCode.SUCCESS.getIndex()){
                                       productDetail.setValue(setGoodCarByDetail(baseJson.getResult()));
                                   }

                               }}, new Consumer<Throwable>() {
                               @Override
                               public void accept(Throwable throwable) throws Exception {
                                   Log.e("TAG>>>", "" + throwable.getMessage());

                               }
                           }
                );
    }
    private List<Product> setProduct (List<Product> list){
        Map<Integer, Product> carItemMap = goodCarItemMap.getValue();
        if(carItemMap==null||list==null||carItemMap.size()==0||list.size()==0){
            return list;
        }
        for(Product product:list){
            if(carItemMap.containsKey(product.getId())){
                product.setNum(carItemMap.get(product.getId()).getNum());
            }
        }

        return list;
    }
//
    private Product setGoodCarByDetail (Product productUserDto){
        Map<Integer, Product> carItemMap = goodCarItemMap.getValue();
        if(carItemMap.containsKey(productUserDto.getId())){
            productUserDto.setNum(carItemMap.get(productUserDto.getId()).getNum());
        }else{
            MutableLiveData<Integer> num = new MutableLiveData<Integer>();
            num.setValue(0);
            productUserDto.setNum(num);
        }
        return productUserDto;
    }
    public MutableLiveData<List<Product>> getProductList(){
        if(productList==null) return new MutableLiveData<List<Product>>();
        return  productList;
    }
    public MutableLiveData<Product> getProductDetail(){
        return  productDetail;
    }

}
