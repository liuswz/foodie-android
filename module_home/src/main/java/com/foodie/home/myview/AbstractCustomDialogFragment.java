package com.foodie.home.myview;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

public abstract class AbstractCustomDialogFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Dialog alertDialog = new Dialog(getContext());
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        View customView = onCreateDialogView();
        alertDialog.setContentView(customView);
        alertDialog.setCanceledOnTouchOutside(false);

        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams windowParams = alertDialog.getWindow().getAttributes();
        windowParams.width = (int) (getResources().getDisplayMetrics().widthPixels -
                getResources().getDisplayMetrics().density * 20);
        alertDialog.getWindow().setAttributes(windowParams);

        return alertDialog;
    }

    //提供接口用来获取Dialog样式
    public abstract View onCreateDialogView();

}
