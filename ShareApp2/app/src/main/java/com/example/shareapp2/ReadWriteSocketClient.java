package com.example.shareapp2;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class ReadWriteSocketClient {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private String res;

    public ReadWriteSocketClient(){
            new Thread(){
                @Override
                public void run() {
                    try{
                        socket=new Socket("localhost",8888);
                        in=new BufferedReader(new InputStreamReader(
                                socket.getInputStream()
                        ));

                        out=new PrintWriter(new BufferedWriter(new OutputStreamWriter(
                                socket.getOutputStream()
                        )));
                    }catch (IOException e){
                        e.printStackTrace();
                    }

                }
            }.start();


    }

    public void readData(){
        new Thread(){
            @Override
            public void run() {
                out.println("read");
                try{
                    String data=in.readLine();
                    res=data;
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        }.start();

    }

    public String getRes() {
        return res;
    }

    public void writeData(final String data){
        new Thread(){
            @Override
            public void run() {
                out.println("write");
                out.println(data);
                try{
                    String res=in.readLine();
                }catch(IOException e){
                    e.printStackTrace();
                }
                res="failed";
            }
        }.start();
    }
}
