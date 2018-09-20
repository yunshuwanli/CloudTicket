package yswl.com.klibrary.util;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import yswl.com.klibrary.R;
import yswl.com.klibrary.http.okhttp.MScreenUtils;


public class WaitingDialogUtil {

    public static Dialog getWaitDialog(Context context) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.view_progressdialog, null);
        // 定义Dialog布局和参数
        Dialog dialog = new Dialog(context, R.style.Theme_AppCompat_Dialog_Alert);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(false);
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = MScreenUtils.dip2px(context, 160); // 宽度
        lp.height = MScreenUtils.dip2px(context, 120); // 高度
        dialogWindow.setAttributes(lp);
        return dialog;
    }

}