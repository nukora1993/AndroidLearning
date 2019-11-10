package com.example.shareapp2;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.shareapp.aidl.IReadWrite;


import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG="ShareApp2";

    private static final String CMD_READ="cmd_read";
    private static final String CMD_WRITE="cmd_write";

    private TextView resultShow;
    private EditText dataToWrite;


    private IReadWrite readWriteBinder;
    private ServiceConnection connection;

    private ReadWriteSocketClient readWriteSocketClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //通过启动app1的activity来读取数据
        Button readDataFromShareApp1WithActivity=(Button)
                findViewById(R.id.read_data_from_share_app1_with_activity);
        //通过启动app1的activity来修改数据
        Button writeDataToShareApp1WithActivity=(Button)
                findViewById(R.id.write_data_to_share_app1_with_activity);

        //通过发送BroadCast以读取数据
        Button readDataFromShareApp1WithBroadCast=(Button)
                findViewById(R.id.read_data_from_share_app1_with_broadcast);

        //通过发送BroadCast以修改数据
        Button writeDataToShareApp1WithBroadCast=(Button)
                findViewById(R.id.write_data_to_share_app1_with_broadcast);

        //通过aidl调用以读取数据
        Button readDataFromShareApp1WithAIDL=(Button)
                findViewById(R.id.read_data_from_share_app1_with_aidl);

        //通过aidl调用以修改数据
        Button writeDataToShareApp1WithAIDL=(Button)
                findViewById(R.id.write_data_to_share_app1_with_aidl);

        //通过socket通信以读取数据
        Button readDataFromShareApp1WithSocket=(Button)
                findViewById(R.id.read_data_from_share_app1_with_socket);

        //通过socket通信以修改数据
        Button writeDataToShareApp1WithSocket=(Button)
                findViewById(R.id.write_data_to_share_app1_with_socket);

        //通过Content Provider读取数据
        Button readDataFromShareApp1WithContentProvider=(Button)
                findViewById(R.id.read_data_from_share_app1_with_content_provider);

        //通过Content Provider修改数据
        Button writeDataToShareApp1WithContentProvider=(Button)
                findViewById(R.id.write_data_to_share_app1_with_content_provider);



        //清空结果
        Button clearResult=(Button)findViewById(R.id.clear_result_show);


        //显示执行结果
        resultShow=(TextView)findViewById(R.id.result_show);
        //用于写入
        dataToWrite=(EditText)findViewById(R.id.data_to_write);


        readDataFromShareApp1WithActivity.setOnClickListener(this);
        writeDataToShareApp1WithActivity.setOnClickListener(this);

        readDataFromShareApp1WithBroadCast.setOnClickListener(this);
        writeDataToShareApp1WithBroadCast.setOnClickListener(this);

        readDataFromShareApp1WithAIDL.setOnClickListener(this);
        writeDataToShareApp1WithAIDL.setOnClickListener(this);

        readDataFromShareApp1WithSocket.setOnClickListener(this);
        writeDataToShareApp1WithSocket.setOnClickListener(this);

        readDataFromShareApp1WithContentProvider.setOnClickListener(this);
        writeDataToShareApp1WithContentProvider.setOnClickListener(this);

        clearResult.setOnClickListener(this);

        //动态注册BroadCastReceiver
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction("com.shareapp2.action.RESPONSE_BROAD_CAST");
        intentFilter.addCategory("com.shareapp2.category.RESPONSE_READ");
        intentFilter.addCategory("com.shareapp2.category.RESPONSE_WRITE");

        ReadWriteClientReceiver readWriteServerReceiver=new ReadWriteClientReceiver(resultShow);
        registerReceiver(readWriteServerReceiver,intentFilter);

        //bind service
        connection=new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                readWriteBinder=IReadWrite.Stub.asInterface(service);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                //do nothing for the time
            }
        };

//        Intent intent=new Intent("com.example.shareapp1.adil.BIND");
        Intent intent=new Intent();
        intent.setComponent(new ComponentName("com.example.shareapp1","com.example.shareapp.aidl.ReadWriteBinderService"));
        bindService(intent,connection,BIND_AUTO_CREATE);

        //Socket Client, 已授予权限，但是仍然有权限问题
//        readWriteSocketClient=new ReadWriteSocketClient();


    }

    private void changeResultShow(String newInfo){
        if(resultShow!=null){
            resultShow.setText(resultShow.getText().toString()+newInfo+"\n");
        }
    }







    @Override
    public void onClick(View v) {
        Intent intent=null;
        String data=null;
        Uri uri=null;
        Cursor cursor=null;
        switch(v.getId()){
            case R.id.read_data_from_share_app1_with_activity:
                intent=new Intent("com.shareapp1.action.START");
                intent.addCategory("com.shareapp1.category.READ_DATA");
                changeResultShow("start ShareApp1 Activity to read data");
                startActivityForResult(intent,0);
                break;
            case R.id.write_data_to_share_app1_with_activity:
                intent=new Intent("com.shareapp1.action.START");
                intent.addCategory("com.shareapp1.category.WRITE_DATA");

                data=dataToWrite.getText().toString();
                intent.putExtra("data",data);


                changeResultShow("start ShareApp1 Activity to write data, data is: "+data);
                startActivityForResult(intent,1);
                break;
            case R.id.read_data_from_share_app1_with_broadcast:
                intent=new Intent("com.shareapp1.action.RECEIVE_BROAD_CAST");
                intent.addCategory("com.shareapp1.category.READ_DATA");
                intent.putExtra("cmd",CMD_READ);
                sendBroadcast(intent);
                changeResultShow("send broadcast to ShareApp1 to read data");
                break;
            case R.id.write_data_to_share_app1_with_broadcast:
                intent=new Intent("com.shareapp1.action.RECEIVE_BROAD_CAST");
                intent.addCategory("com.shareapp1.category.WRITE_DATA");
                data=dataToWrite.getText().toString();
                intent.putExtra("data",data);
                intent.putExtra("cmd",CMD_WRITE);
                sendBroadcast(intent);
                changeResultShow("send broadcast to ShareApp1 to write data, data is: "+data);
                break;
            case R.id.read_data_from_share_app1_with_aidl:
                try{
                    data=readWriteBinder.readData();
                }catch(RemoteException e){
                    e.printStackTrace();
                    changeResultShow("error,"+e.getMessage());
                }

                changeResultShow("read data from ShareApp1 by aidl, data is:"+data);
                break;
            case R.id.write_data_to_share_app1_with_aidl:
                data=dataToWrite.getText().toString();
                try{
                    readWriteBinder.writeData(data);
                }catch(RemoteException e){
                    e.printStackTrace();
                    changeResultShow("error,"+e.getMessage());
                }

                changeResultShow("write data to ShareApp1 by aidl, data is: "+data);
                break;
            case R.id.read_data_from_share_app1_with_socket:
                readWriteSocketClient.readData();
                data=readWriteSocketClient.getRes();
                changeResultShow("read data from ShareApp1 by socket, data is: "+data);
                break;
            case R.id.write_data_to_share_app1_with_socket:
                data=dataToWrite.getText().toString();
                readWriteSocketClient.writeData(data);
                String res=readWriteSocketClient.getRes();
                changeResultShow("write data to ShareApp1 by socket, "+res);
                break;
            case R.id.read_data_from_share_app1_with_content_provider:
                uri=Uri.parse("content://com.example.shareapp1.readwrite.provider/read");
                cursor=getContentResolver().query(uri,new String[]{"_id","content"},"_id=?",new String[]{"1"},null);
                while(cursor.moveToNext()){
                    int id=cursor.getInt(0);
                    data=cursor.getString(1);
                }
                changeResultShow("read data from ShareApp1 by Content Provider, data is: "+data);
                break;

            case R.id.write_data_to_share_app1_with_content_provider:
                uri=Uri.parse("content://com.example.shareapp1.readwrite.provider/write");
                ContentValues values=new ContentValues();
                values.put("_id",1);
                data=dataToWrite.getText().toString();
                values.put("content",data);
                getContentResolver().update(uri,values,"_id=?",new String[]{"1"});
                break;

            case R.id.clear_result_show:
                resultShow.setText("");
            default:
                break;

        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        switch (requestCode){
            case 0:
                if(resultCode==200){
                    //获取返回的数据
                    String res=data.getStringExtra("result");
                    changeResultShow("read data from ShareApp1 finished, data is: "+res);
                }
                break;
            case 1:
                if(resultCode==200){
                    changeResultShow("write data to ShareApp1 finished");
                }


        }
    }

    @Override
    protected void onDestroy() {
        unbindService(connection);
        super.onDestroy();
        Log.d(TAG,"onDestroy");
    }
}
