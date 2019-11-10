package com.example.shareapp1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

public class ReadWriteServerReceiver extends BroadcastReceiver {
    private static final String TAG="ShareApp1";

    private static final String RESPONSE_READ="response_read";
    private static final String RESPONSE_WRITE="response_write";

    private static final String CMD_READ="cmd_read";
    private static final String CMD_WRITE="cmd_write";
    private static String DATA_DIR="";

    public ReadWriteServerReceiver(String dataDir){
        super();
        DATA_DIR=dataDir;

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG,"onReceive");
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
//        throw new UnsupportedOperationException("Not yet implemented");
        String cmd=intent.getStringExtra("cmd");
        if(!TextUtils.isEmpty(cmd)){
            if (cmd.equals(CMD_READ)){
                String data=DataUtils.readData(DATA_DIR);
                //读取之后发回ShareApp2
                Intent result=new Intent("com.shareapp2.action.RESPONSE_BROAD_CAST");
                result.addCategory("com.shareapp2.category.RESPONSE_READ");
                result.putExtra("response",RESPONSE_READ);
                result.putExtra("result",data);
                context.sendBroadcast(result);
            }else if(cmd.equals(CMD_WRITE)){
                String data=intent.getStringExtra("data");
                DataUtils.writeData(DATA_DIR,data);

                Intent result=new Intent("com.shareapp2.action.RECEIVE_BROAD_CAST");
                result.addCategory("com.shareapp2.category.RESPONSE_WRITE");
                result.putExtra("response",RESPONSE_WRITE);
                context.sendBroadcast(result);

            }
        }
    }
}
