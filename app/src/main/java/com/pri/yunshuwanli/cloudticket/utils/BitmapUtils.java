package com.pri.yunshuwanli.cloudticket.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pri.yunshuwanli.cloudticket.R;
import com.pri.yunshuwanli.cloudticket.entry.OrderInfo;
import com.pri.yunshuwanli.cloudticket.entry.PrinterBean;
import com.pri.yunshuwanli.cloudticket.entry.User;
import com.pri.yunshuwanli.cloudticket.entry.UserManager;

import java.math.BigDecimal;
import java.util.List;

public class BitmapUtils {
    private static Bitmap loadBitmapFromView(View v) {
        int w = v.getWidth();
        int h = v.getHeight();

        Bitmap bmp = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
        Canvas c = new Canvas(bmp);
        c.drawColor(Color.WHITE);          /** 如果不设置canvas画布为白色，则生成透明 */
        v.layout(0, 0, w, h);
        v.draw(c);
        return bmp;
    }


   /* public static Bitmap getBitmapFromView(View v) {
        Bitmap b = Bitmap.createBitmap(v.getMeasuredWidth(), v.getMeasuredHeight(), Bitmap.Config.RGB_565);
        Canvas c = new Canvas(b);
        v.layout(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
        // Draw background
        Drawable bgDrawable = v.getBackground();
        if (bgDrawable != null)
            bgDrawable.draw(c);
        else
            c.drawColor(Color.WHITE);
        // Draw view to canvas
        v.draw(c);
        return b;
    }*/


    /**
     * 停车场小票
     */
    public static Bitmap getTicktBitmap(Activity context, OrderInfo order,String url) {
        if (context == null || order == null) return null;
        DisplayMetrics metric = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;     // 屏幕宽度（像素）
        int height = metric.heightPixels;   // 屏幕高度（像素）
        View view = LayoutInflater.from(context).inflate(R.layout.ticket_layout, null, false);
        TextView tv_name = view.findViewById(R.id.name);
        TextView tv_date = view.findViewById(R.id.date);
        TextView tv_time = view.findViewById(R.id.time);
        TextView tv_carNo = view.findViewById(R.id.carNo);
        TextView tv_price = view.findViewById(R.id.price);
//        TextView tv_hint = view.findViewById(R.id.hint);
        ImageView iv_Code = view.findViewById(R.id.qr_code);


        tv_name.setText(UserManager.getUser().getKpdmc());
        tv_date.setText(order.getOrderDate().split(" ")[0]);
        tv_time.setText(order.getOrderDate().split(" ")[1]);
        tv_carNo.setText(order.getCarNo());
        tv_price.setText(String.valueOf(order.getTotalAmount()));
        Bitmap bitmap = QRCodeUtil.createQRCodeBitmap(url, width, width);
        iv_Code.setImageBitmap(bitmap);
        layoutView(view, width, height * 5 / 4);//去到指定view大小的函数
        return loadBitmapFromView(view);
    }

    /**
     * 购买商品小票
     */
    public static Bitmap getShappingBitmap(Activity context, PrinterBean bean, String url) {
        if (context == null) return null;
        DisplayMetrics metric = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;     // 屏幕宽度（像素）
        int height = metric.heightPixels;   // 屏幕高度（像素）
        View view = LayoutInflater.from(context).inflate(R.layout.shapping_ticket_layout, null, false);
        TextView tv_numb = view.findViewById(R.id.order_numb);
        TextView tv_date = view.findViewById(R.id.order_date);
        TextView tv_amount = view.findViewById(R.id.order_amount);
        ImageView iv_Code = view.findViewById(R.id.qr_code);


        OrderInfo order = bean.info;
        if (order != null) {
            tv_date.setText(DateUtil.getTodayDate2());
            tv_numb.setText(order.getOrderNo());
        }
        List<User.SpListBean> beans = bean.list;
        double totalAmount = 0;
        if (beans != null && beans.size() > 0) {
            for (User.SpListBean bean1 : beans) {
                totalAmount += bean1.getReal_total();
            }

        }
        tv_amount.setText(String.valueOf(new BigDecimal(totalAmount).setScale(2, BigDecimal.ROUND_HALF_UP)) + "元");
        Bitmap bitmap = QRCodeUtil.createQRCodeBitmap(url, width * 3 / 4, width * 3 / 4);
        iv_Code.setImageBitmap(bitmap);
        layoutView(view, width, height * 9 / 5);//去到指定view大小的函数
        return loadBitmapFromView(view);
    }

    public static Bitmap getTicketBillingBitmap(Activity context, OrderInfo order) {
        if (context == null || order == null) return null;
        DisplayMetrics metric = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;     // 屏幕宽度（像素）
        int height = metric.heightPixels;   // 屏幕高度（像素）
        View view = LayoutInflater.from(context).inflate(R.layout.ticket_layout, null, false);
        TextView tv_name = view.findViewById(R.id.name);
        TextView tv_date = view.findViewById(R.id.date);
        TextView tv_time = view.findViewById(R.id.time);
        TextView tv_carNo = view.findViewById(R.id.carNo);
        TextView tv_price = view.findViewById(R.id.price);
//        TextView tv_hint = view.findViewById(R.id.hint);
        ImageView iv_Code = view.findViewById(R.id.qr_code);


        tv_name.setText(UserManager.getUser().getKpdmc());
        tv_date.setText(order.getOrderDate().split(" ")[0]);
        tv_time.setText(order.getOrderDate().split(" ")[1]);
        tv_carNo.setText(order.getCarNo());
        tv_price.setText(String.valueOf(order.getTotalAmount()));
        Bitmap bitmap = QRCodeUtil.createQRCodeBitmap(SignUtil.getQrCodeUrl(new PrinterBean(order, null), false), width, width);
        iv_Code.setImageBitmap(bitmap);
        layoutView(view, width, height);//去到指定view大小的函数
        return loadBitmapFromView(view);
    }


    private static void layoutView(View v, int width, int height) {
        // 指定整个View的大小 参数是左上角 和右下角的坐标
        v.layout(0, 0, width, height);
        int measuredWidth = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);
        int measuredHeight = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.AT_MOST);
        /** 当然，measure完后，并不会实际改变View的尺寸，需要调用View.layout方法去进行布局。
         * 按示例调用layout函数后，View的大小将会变成你想要设置成的大小。
         */
        v.measure(measuredWidth, measuredHeight);
        v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
    }
}
