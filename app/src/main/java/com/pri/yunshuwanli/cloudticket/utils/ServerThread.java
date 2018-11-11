package com.pri.yunshuwanli.cloudticket.utils;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.pri.yunshuwanli.cloudticket.App;
import com.pri.yunshuwanli.cloudticket.entry.OrderInfo;
import com.pri.yunshuwanli.cloudticket.entry.PrinterAsyncTask;
import com.pri.yunshuwanli.cloudticket.logger.KLogger;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import yswl.com.klibrary.util.L;
import yswl.com.klibrary.util.ToastUtil;


//Server端的主线程
public class ServerThread implements Runnable {
    private static final int PORT = 9999;
    private static final String TAG = "ServerThread";
    ServerSocket serverSocket;
    Handler handler;
    Activity activity;
    private BufferedReader in;
    private boolean printerOk = true;

    public ServerThread(Activity activity, Handler handler) {
        try {
            this.activity = activity;
            this.handler = handler;
            serverSocket = new ServerSocket(PORT);
            KLogger.i(TAG, "-----ServerSocket启动----");
        } catch (IOException e1) {
            KLogger.e(TAG, "-----ServerSocket启动失败---- msg:" + e1.getMessage());
            e1.printStackTrace();
        }
    }

    @Override
    public void run() {

        Socket client = null;
        String text = null;
        String callBcak = "02";
        while (true) {
            try {
                if (serverSocket == null) {
                    return;
                }
                client = serverSocket.accept();
                KLogger.i(TAG, "-----Server获取到客户端Socket----");
                //把客户端放入客户端集合中

                InputStream is = client.getInputStream();
                OutputStream os = client.getOutputStream();
// 接下来考虑输入流的读取显示到PC端和返回是否收到
                byte[] buffer = new byte[512];
                int len;
                while ((len = is.read(buffer)) != -1) {
                    text = new String(buffer, 0, len);
                    L.i(TAG, "-----获取到数据为----" + text);

                    if (!TextUtils.isEmpty(text) && text.startsWith("AAA5") && text.endsWith("CD")) {
                        OrderInfo info = String2HexUtils.getOrderInroForOrg(text);
                        String start = text.substring(0, 28);
                        String end = text.substring(text.length()-6);
                        if (info == null) return;


                        //打印
//                        PrinterUtil.initPrinter(App.getIdal());
//                       boolean printResult =  PrinterUtil.startCarPrinter(activity, info);
                       if(true){
                           //通知handle 请求服务
                           Message message = Message.obtain();
                           message.obj = info;
                           message.what = 1;
                           handler.sendMessage(message);
                           //回执

                           callBcak = "0200";
                           callBcak = start +String2HexUtils.hex(callBcak)+end;
                           os.write(callBcak.getBytes());
                           L.i(TAG, "-----回执成功----返回码" + callBcak);
                           try {
                               Thread.sleep(3000);
                               callBcak = "0200";
                               start = "AAA5280000000100020000000323";
                               end = String2HexUtils.getCRC(callBcak.getBytes())+"CD";
                               callBcak = start +callBcak+end;
                               os.write(callBcak.getBytes());
                               L.i(TAG, "-----回执成功----返回码" + callBcak);
                           } catch (InterruptedException e) {
                               e.printStackTrace();
                           }


                       }else {
                           callBcak = "021";
                        os.write(callBcak.getBytes());
                        L.i(TAG, "-----回执fail----返回码" + callBcak);

                    }

                    } else {
                        callBcak = "021";
                        os.write(callBcak.getBytes());
                    }


                }


            } catch (IOException e) {
                //连接客户端socket失败
                KLogger.e(TAG, "-----连接客户端socket失败---- msg:" + e.getMessage());
            }
        }


    }


    public void destroy() {
        if (serverSocket != null) {
            try {
                if (!serverSocket.isClosed())
                    serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            serverSocket = null;
        }


    }

}