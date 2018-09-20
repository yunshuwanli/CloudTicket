package com.pri.yunshuwanli.cloudticket.acitivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.pri.yunshuwanli.cloudticket.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import yswl.com.klibrary.base.MActivity;
import yswl.com.klibrary.util.L;

public class ServerSocketTestActivity extends MActivity implements View.OnClickListener {
    public static void JumpAct(Context context) {
        Intent intent = new Intent(context, ServerSocketTestActivity.class);
        context.startActivity(intent);
    }

    private static final int PORT = 9999;
    private List<Socket> mList = new ArrayList<Socket>();
    private volatile ServerSocket server = null;
    private ExecutorService mExecutorService = null; //线程池
    private String hostip;//本机IP
    private TextView mText1;
    private TextView mText2;
    private Button mBut1 = null;
    private Handler myHandler = null;
    private volatile boolean flag = true;//线程标志位

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serversocket_test);
        hostip = getLocalIpAddress();  //获取本机IP
        mText1 = (TextView) findViewById(R.id.textView1);
        mText1.setText(hostip);
        mText1.setEnabled(false);

        mText2 = (TextView) findViewById(R.id.textView2);

        mBut1 = (Button) findViewById(R.id.but1);
        mBut1.setOnClickListener(this);
        //取得非UI线程传来的msg，以改变界面
        myHandler = new Handler() {
            @SuppressLint("HandlerLeak")
            public void handleMessage(Message msg) {
                if (msg.what == 0x1234) {
                    mText2.append("\n" + msg.obj.toString());
                }
            }
        };


    }

    //获取本地IP
    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
            L.e("WifiPreference IpAddress", ex.toString());
        }
        return null;
    }

    @Override
    public void onClick(View v) {
//如果是“启动”，证明服务器是关闭状态，可以开启服务器
        if (mBut1.getText().toString().equals("启动")) {
            System.out.println("flag:" + flag);
            ServerThread serverThread = new ServerThread();
            flag = true;
            serverThread.start();
            mBut1.setText("关闭");
        } else {
            try {
                flag = false;
                server.close();
                for (int p = 0; p < mList.size(); p++) {
                    Socket s = mList.get(p);
                    s.close();
                }
                mExecutorService.shutdownNow();
                mBut1.setText("启动");
                System.out.println("服务器已关闭");
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

    }

    //Server端的主线程
    class ServerThread extends Thread {

        public void stopServer(){
            try {
                if(server!=null){
                    server.close();
                    System.out.println("close task successed");
                }
            } catch (IOException e) {
                System.out.println("close task failded");
            }
        }
        public void run() {
            try {
                server = new ServerSocket(PORT);
            } catch (IOException e1) {
                System.out.println("S2: Error");
                e1.printStackTrace();
            }
            mExecutorService = Executors.newCachedThreadPool();  //创建一个线程池
            System.out.println("服务器已启动...");
            Socket client = null;
            while(flag) {
                try {
                    client = server.accept();
                    //把客户端放入客户端集合中
                    mList.add(client);
                    mExecutorService.execute(new Service(client)); //启动一个新的线程来处理连接
                }catch ( IOException e) {
                    System.out.println("S1: Error");
                    e.printStackTrace();
                }
            }


        }

        //处理与client对话的线程
        class Service implements Runnable {
            private volatile boolean kk=true;
            private Socket socket;
            private BufferedReader in = null;
            private String msg = "";

            public Service(Socket socket) {
                this.socket = socket;
                try {
                    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    msg="OK";
                    this.sendmsg(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            public void run() {

                while(kk) {
                    try {
                        if((msg = in.readLine())!= null) {
                            //当客户端发送的信息为：exit时，关闭连接
                            if(msg.equals("exit")) {
                                mList.remove(socket);
                                //in.close();
                                //socket.close();
                                break;
                                //接收客户端发过来的信息msg，然后发送给客户端。
                            } else {
                                Message msgLocal = new Message();
                                msgLocal.what = 0x1234;
                                msgLocal.obj =msg+" （客户端发送）" ;
                                System.out.println(msgLocal.obj.toString());
                                System.out.println(msg);
                                myHandler.sendMessage(msgLocal);
                                msg = socket.getInetAddress() + ":" + msg+"（服务器发送）";
                                this.sendmsg(msg);
                            }

                        }
                    } catch (IOException e) {
                        System.out.println("close");
                        kk=false;
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }


            }
            //向客户端发送信息
            public void sendmsg(String msg) {
                System.out.println(msg);
                PrintWriter pout = null;
                try {
                    pout = new PrintWriter(new BufferedWriter(
                            new OutputStreamWriter(socket.getOutputStream())),true);
                    pout.println(msg);
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

}
