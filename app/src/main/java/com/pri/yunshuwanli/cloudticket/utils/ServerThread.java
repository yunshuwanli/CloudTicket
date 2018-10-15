package com.pri.yunshuwanli.cloudticket.utils;

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
    ExecutorService mExecutorService;
    private BufferedReader in;
    private boolean printerOk;

    public ServerThread() {
        try {
            serverSocket = new ServerSocket(PORT);
            KLogger.i(TAG,"-----ServerSocket启动----");
        } catch (IOException e1) {
            KLogger.e(TAG,"-----ServerSocket启动失败---- msg:"+e1.getMessage());
            e1.printStackTrace();
        }
    }

    @Override
    public void run() {
//        mExecutorService = Executors.newCachedThreadPool();  //创建一个线程池

        Socket client = null;
        String text = null;
        while (true) {
            try {
                client = serverSocket.accept();
                KLogger.i(TAG,"-----Server获取到客户端Socket----");
                //把客户端放入客户端集合中
//                mExecutorService.execute(new Service(client)); //启动一个新的线程来处理连接

                InputStream is = client.getInputStream();
                OutputStream os = client.getOutputStream();
// 接下来考虑输入流的读取显示到PC端和返回是否收到
                byte[] buffer = new byte[1024];
                int len;
                while ((len = is.read(buffer)) != -1) {
                    text = new String(buffer, 0, len);
                    KLogger.i(TAG,"-----获取到数据为----"+text);
                    ToastUtil.showToast("接收到数据："+text);
//                    if() //拿到数据 结束 关闭这个socket
                }

                //TODO 接受数据解析所需要的数据
                //TODO 打印数据
                //打印成功与否的值
                if (printerOk) {
                    //TODO
                    os.write("打印成功".getBytes("utf-8"));
                    client.close();
                } else {
                    //TODO 返回失败；

                }

            } catch (IOException e) {
                //连接客户端socket失败
                KLogger.e(TAG,"-----连接客户端socket失败---- msg:"+e.getMessage());
            }
        }


    }


}