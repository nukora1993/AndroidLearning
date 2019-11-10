package com.example.shareapp1;

import android.os.Environment;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class DataUtils {
    public static final String fileName="/share.txt";
    public static String readData(String dataDir){
//        String dataDir=Environment.getExternalStorageDirectory().getAbsolutePath();
        String filePath=dataDir+fileName;
        File file=new File(filePath);
        if(!file.exists()){
            return null;
        }else{
            try{
                BufferedReader br=new BufferedReader(new InputStreamReader(
                        new FileInputStream(file)
                ));
                String line=null;
                StringBuilder sb=new StringBuilder();
                while(!TextUtils.isEmpty(line=br.readLine())){
                    sb.append(line);
                }
                br.close();
                return sb.toString();
            }catch(IOException e){
                e.printStackTrace();
                return null;
            }

        }
    }

    public static void writeData(String dataDir,String data){
//        String dataDir=Environment.getExternalStorageDirectory().getAbsolutePath();
        String filePath=dataDir+fileName;
        File file=new File(filePath);

        try{
            BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(file,true)
            ));
            bw.write(data);
            bw.close();
        }catch(IOException e){
            e.printStackTrace();

        }



    }
}
