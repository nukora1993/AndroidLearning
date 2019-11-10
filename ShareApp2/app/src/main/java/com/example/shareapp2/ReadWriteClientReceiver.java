package com.example.shareapp2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

public class ReadWriteClientReceiver extends BroadcastReceiver {
    private static final String TAG="ShareApp2";

    private static final String RESPONSE_READ="response_read";
    private static final String RESPONSE_WRITE="response_write";



    private TextView resultShow;
    public ReadWriteClientReceiver(TextView resultShow){
        super();
        this.resultShow=resultShow;
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG,"onReceive");
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
//        throw new UnsupportedOperationException("Not yet implemented");
        //判断是读还是写
        String response=intent.getStringExtra("response");
        if(!TextUtils.isEmpty(response)){
            if(response.equals(RESPONSE_READ)){
                //获取读取到的数据
                String data=intent.getStringExtra("result");
                changeResultShow(data);
            }else if(response.equals(RESPONSE_WRITE)){
                //do nothing
            }
        }
    }

    private void changeResultShow(String newInfo){
        if(resultShow!=null){
            resultShow.setText(resultShow.getText().toString()+newInfo+"\n");
        }
    }

}
