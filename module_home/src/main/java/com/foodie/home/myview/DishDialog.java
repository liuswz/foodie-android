package com.foodie.home.myview;

;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import com.foodie.base.entity.Dish;
import com.foodie.home.R;
import com.foodie.home.databinding.DishDialogBinding;
import com.foodie.home.databinding.DishItemBinding;

public class DishDialog extends DialogFragment {
    private Dish dish;
    //private View.OnClickListener listener;
    public DishDialog(Dish dish){
        this.dish = dish;
      //  this.listener = listener;
    }
    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.CENTER;
        window.setAttributes(params);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
      //  window.setWindowAnimations(R.style.BottomDialog_Animation);
        //设置边距
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        window.setLayout((int) (dm.widthPixels*0.8), ViewGroup.LayoutParams.WRAP_CONTENT);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        DishDialogBinding binding = DataBindingUtil
                .inflate(inflater, R.layout.dish_dialog,
                        container, false);
        binding.setDish(dish);
      //  binding.closePage.setOnClickListener(listener);
        return binding.getRoot();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }
}
