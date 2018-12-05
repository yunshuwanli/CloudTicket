package com.pri.yunshuwanli.cloudticket.utils;

import android.text.TextUtils;

import com.google.zxing.common.StringUtils;
import com.pri.yunshuwanli.cloudticket.logger.KLogger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import yswl.com.klibrary.util.L;


//Server端的主线程
public class MonitorThread extends Thread {
    private static final int PORT = 9999;
    private static final String TAG = "ServerThread";
    ServerSocket serverSocket;
    private BufferedReader in;
    private boolean printerOk = true;

    public MonitorThread() {
        serverSocket = getServerSocket();
    }

    @Override
    public void run() {
        Socket client = null;
        String text = null;
        String callBcakStatue = "02";
        while (true) {
            try {
                if (getServerSocket() == null) {
                    return;
                }
                client = serverSocket.accept();
                KLogger.i(TAG, "-----Server获取到客户端Socket----");

                InputStream is = client.getInputStream();
                OutputStream os = client.getOutputStream();
// 接下来考虑输入流的读取显示到PC端和返回是否收到
                byte[] buffer = new byte[512];
                int len;
                while ((len = is.read(buffer)) != -1) {
                    text = new String(buffer, 0, len);
                    L.i(TAG, "-----获取到数据为----" + text);

                    if (!TextUtils.isEmpty(text) && text.startsWith("AAA5") && text.endsWith("CD")) {
                        text = text.trim().replace(" ","");
                        String content = text.substring(28, text.length() - 6);

                        String in_time = content.substring(0,12);

                        String nuicode = String2HexUtils.decodeGBK("BEA94131323334352020");
                        L.i(TAG, "-----1----" + nuicode);

                        //TODO 打印
                    } else {
                        callBcakStatue = "01";
                    }

                    //AAA5280000000100020000000382120A1F130631120A1F1406310064000A005A00BEA94131323334352020202000000E10000003E800B2A2CD
                    //打印成功与否的值
                    if (printerOk) {
                        //TODO
                        callBcakStatue = "0200";
                        os.write(callBcakStatue.getBytes("utf-8"));
                        L.i(TAG, "-----回执成功----返回码" + callBcakStatue);

                    } else {
                        //TODO 返回失败；
                        os.write(callBcakStatue.getBytes("utf-8"));
                        L.i(TAG, "-----回执成功----返回码" + callBcakStatue);


                    }
                }
                try{
                    //10秒后重新连接
                    Thread.sleep(1000);
                }catch(Exception e){
                    System.out.println(e.getMessage());
                }

            } catch (IOException e) {
                //连接客户端socket失败
                KLogger.e(TAG, "-----连接客户端socket失败---- msg:" + e.getMessage());
            }
        }


    }


    private ServerSocket getServerSocket() {
        if (serverSocket == null) {
            try {
                serverSocket = new ServerSocket(PORT);
                KLogger.i(TAG, "-----ServerSocket启动----");
            } catch (IOException e) {
                e.printStackTrace();
                KLogger.e(TAG, "-----ServerSocket启动失败---- msg:" + e.getMessage());
                return null;
            }
        }
        return serverSocket;
    }


    @Override
    public void destroy() {
        super.destroy();
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