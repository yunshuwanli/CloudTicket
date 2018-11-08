package com.pri.yunshuwanli.cloudticket.utils;

import android.text.TextUtils;

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
    private BufferedReader in;
    private boolean printerOk = true;

    public ServerThread() {
        try {
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
                        String nuicode = ByteUtils.hexStringToString("BEA94131323334352020");
                        L.i(TAG, "-----1----" + nuicode);
                        String original = ByteUtils.unicode2String(nuicode);
                        L.i(TAG, "-----2----" + original);

                        //TODO 打印
                    } else {
                        callBcak = "01";
                    }

                    //AAA5280000000100020000000382120A1F130631120A1F1406310064000A005A00BEA94131323334352020202000000E10000003E800B2A2CD
                    //打印成功与否的值
                    if (printerOk) {
                        //TODO
                        callBcak = "0200";
                        os.write(callBcak.getBytes("utf-8"));
                        L.i(TAG, "-----回执成功----返回码" + callBcak);

                    } else {
                        //TODO 返回失败；
                        os.write(callBcak.getBytes("utf-8"));
                        L.i(TAG, "-----回执成功----返回码" + callBcak);


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