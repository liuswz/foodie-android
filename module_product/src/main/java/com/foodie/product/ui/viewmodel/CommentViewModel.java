package com.foodie.product.ui.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.foodie.base.config.RetrofitServiceManager;
import com.foodie.base.entity.PageResults;
import com.foodie.base.entity.ProductComment;
import com.foodie.base.enums.ResultCode;

import com.foodie.product.service.networkService.CommentService;
import com.rxjava.rxlife.RxLife;
import com.rxjava.rxlife.ScopeViewModel;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class CommentViewModel extends ScopeViewModel {

    public MutableLiveData<List<ProductComment>> commentList;
    private Integer size=10;
    private int total;
    private Integer productId;

    public CommentViewModel(@NonNull Application application) {
        super(application);
        commentList = new MutableLiveData<>();
    }
    public void setProductId(Integer productId){
        this.productId = productId;
    }
    public int getTotal(){
        return total;
    }
    public void loadCommentList(int page){

        RetrofitServiceManager.getInstance(getApplication()).create(CommentService.class)
                .getAllProductComment(page,size,productId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxLife.<PageResults<ProductComment>>as(this))
                .subscribe(new Consumer<PageResults<ProductComment>>() {
                               @Override
                               public void accept(PageResults<ProductComment> results) throws Exception {
                                   if(results.getCode()== ResultCode.SUCCESS.getIndex()){
                                       commentList.setValue(results.getRows());
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

    public MutableLiveData<List<ProductComment>> getCommentList(){
        if(commentList==null) return new MutableLiveData<List<ProductComment>>();
        return  commentList;
    }
}
