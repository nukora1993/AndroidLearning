package com.example.shareapp.aidl;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.example.shareapp1.DataUtils;


public class ReadWriteBinderService extends Service {
    private static final String TAG="ShareApp1";

    private IBinder readWriteBinder;

    private static String DATA_DIR="";



    public ReadWriteBinderService() {
    }

    class ReadWriteBinder extends IReadWrite.Stub{
        @Override
        public String readData() throws RemoteException {
            String data= DataUtils.readData(DATA_DIR);
            return data;
        }

        @Override
        public void writeData(String data) throws RemoteException {
            DataUtils.writeData(DATA_DIR,data);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG,"binder service onCreate");
        DATA_DIR=getExternalCacheDir().getAbsolutePath();
        readWriteBinder=new ReadWriteBinder();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
//        throw new UnsupportedOperationException("Not yet implemented");
        Log.d(TAG,"binder service onBind");
        return readWriteBinder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"binder service destroy");

    }
}
