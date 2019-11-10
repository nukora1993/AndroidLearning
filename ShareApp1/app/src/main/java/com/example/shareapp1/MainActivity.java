package com.example.shareapp1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private static final String TAG="ShareApp1";

    private static final String ACTION_START="com.shareapp1.action.START";
    private static final String CATEGORY_READ_DATA="com.shareapp1.category.READ_DATA";
    private static final String CATEGROY_WRITE_DATA="com.shareapp1.category.WRITE_DATA";
    private static String DATA_DIR="";

    private ReadWriteServerReceiver readWriteServerReceiver;
    private ReadWriteSocketServer readWriteSocketServer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},0);
            }
        }

        DATA_DIR=getExternalCacheDir().getAbsolutePath();

        Log.d(TAG,"onCreate");
        Intent intent=getIntent();
        Log.d(TAG,"start action is:"+intent.getAction());
        Log.d(TAG,"start categories is"+intent.getCategories());

        if(!intent.getAction().equals(ACTION_START)){
            Log.d(TAG,"Not data action, ignored");
        }else{
            //判断通过哪一个category启动的，执行相应逻辑
            HashMap categories=new HashMap();
            for(String eachCategory:intent.getCategories()){
                categories.put(eachCategory,true);
            }
            if(categories.containsKey(CATEGORY_READ_DATA)){
                Log.d(TAG,"get category read data");
                String data=DataUtils.readData(DATA_DIR);
                if(TextUtils.isEmpty(data)){
                    Log.d(TAG,"no data in file or read error");
                }
                Intent result=new Intent();
                result.putExtra("result",data);
                setResult(200,result);
            }else if(categories.containsKey(CATEGROY_WRITE_DATA)){
                Log.d(TAG,"get category write data");
                String data=intent.getStringExtra("data");
                if(!TextUtils.isEmpty(data)){
                    DataUtils.writeData(DATA_DIR,data);
                }else{
                    Log.d(TAG,"no data to write");
                }
                Intent result=new Intent();
                intent.putExtra("result","OK");
                setResult(200,result);

            }
        }

        //动态注册BroadCastReceiver
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction("com.shareapp1.action.RECEIVE_BROAD_CAST");
        intentFilter.addCategory("com.shareapp1.category.READ_DATA");
        intentFilter.addCategory("com.shareapp1.category.WRITE_DATA");

        readWriteServerReceiver=new ReadWriteServerReceiver(DATA_DIR);
        registerReceiver(readWriteServerReceiver,intentFilter);

        //启动Socket Server，已授予权限，但是仍然有权限题
//        ReadWriteSocketServer.DATA_DIR=DATA_DIR;
//        readWriteSocketServer=ReadWriteSocketServer.getInstance();
//
//        readWriteSocketServer.startServer();


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode){
            case 0:
                if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){

                }else{
                    Toast.makeText(MainActivity.this,"You must allow permission",Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
                break;

        }
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG,"onDestroy");
        if(readWriteServerReceiver!=null){
            unregisterReceiver(readWriteServerReceiver);
        }
        if(readWriteSocketServer!=null){
            readWriteSocketServer.stopServer();
        }
        super.onDestroy();
    }
}
