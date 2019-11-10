package com.example.shareapp1;

import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ReadWriteSocketServer {
    private static final String TAG="ShareApp1";
    private static String serverState="idle";

    public static String DATA_DIR="";



    private ServerSocket serverSocket;
    private static ReadWriteSocketServer readWriteSocketServer;


    private ReadWriteSocketServer(){
        try{
            new Thread(){
                @Override
                public void run() {
                    try{
                        serverSocket=new ServerSocket(8888);
                    }catch(IOException e){
                        e.printStackTrace();
                    }

                }
            }.start();

        }catch(Exception e){
            e.printStackTrace();
        }

    }
    public static ReadWriteSocketServer getInstance(){
        if(readWriteSocketServer==null){
            return new ReadWriteSocketServer();
        }else{
            return readWriteSocketServer;
        }
    }

    public void startServer(){
        serverState="SATRTED";
        runServer();
    }

    public void stopServer(){
        serverState="STOP";
    }

    //运行server，以接收client消息或者发消息给server
    public void runServer(){
        new Thread(){
            @Override
            public void run() {
//                super.run();
                //可以accept多个client，每来一个client就新建一个Thread进行处理
                while(!serverState.equals("STOP")){
                    if(TextUtils.isEmpty(DATA_DIR)){
                       continue;
                    }
                    try{
                        //与client建立连接
                        final Socket client=serverSocket.accept();
                        Log.d(TAG,"server accepted client");
                        new Thread(){
                            @Override
                            public void run() {
                                BufferedReader in;
                                PrintWriter out;
                                try{
                                    //获取客户端发来的消息，判断是读取还是写入
                                    in=new BufferedReader(new InputStreamReader(
                                            client.getInputStream()
                                    ));
                                    out = new PrintWriter(new BufferedWriter(
                                            new OutputStreamWriter(client.getOutputStream())), true);
                                }catch(IOException e){
                                    e.printStackTrace();
                                    try{
                                        client.close();
                                    }catch(IOException ee){
                                        ee.printStackTrace();
                                    }

                                    return;
                                }

                                while(!serverState.equals("STOP")){
                                    try{
                                        String cmd=in.readLine();

                                        if(cmd.equals("read")){
                                            String data=DataUtils.readData(DATA_DIR);
                                            //发回消息给client
                                            out.println(data);
                                        }else if(cmd.equals("write")){
                                            String data=in.readLine();
                                            DataUtils.writeData(DATA_DIR,data);
                                            out.println("write success");
                                        }
                                    }catch(IOException e){
                                        e.printStackTrace();
                                        break;
                                    }
                                }

                                try{
                                    client.close();
                                }catch (IOException e){
                                    e.printStackTrace();
                                }

                            }
                        }.start();


                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }


            }
        }.start();
    }
}
