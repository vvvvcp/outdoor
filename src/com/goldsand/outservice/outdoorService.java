package com.goldsand.outservice;

import java.util.List;

import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.NeighboringCellInfo;
import android.telephony.TelephonyManager;
import android.util.Log;

public class outdoorService extends Service{

    private static final String TAG = "outdoorService";
    private TelephonyManager mTelecomManager;
    protected final Handler mHandler = new Handler();
    private boolean mStopDrawing = true;
    protected Runnable mCellRunnable = new Runnable() {
        @Override
        public void run() {
            if( mTelecomManager != null && !mStopDrawing ) {
                List<NeighboringCellInfo> infos = mTelecomManager.getNeighboringCellInfo();
                //StringBuffer sBuffer = new StringBuffer("Total Size:"+infos.size()+"\n");
                Log.i(TAG,"Tatol : "+infos.size());
                for (NeighboringCellInfo infoLoop : infos) {
                     //Log.i(TAG,"LAC : "+infoLoop.getLac());
                     //Log.i(TAG,"CID : "+infoLoop.getCid());
                     //Log.i(TAG,"Rssi : "+infoLoop.getRssi());
                    Uri insertUri = Uri.parse("content://com.goldsand.outdoor.cellinfo/cellid");
                    //Uri queryUri = Uri.parse("")
                    Cursor cursor = getContentResolver().query(insertUri, null, "lac ="+infoLoop.getLac()+" and "+"sid ="+infoLoop.getCid(), null, null);
                    if(cursor!=null && cursor.getCount()>0){
                        Log.i(TAG,"have");
                        cursor.close();
                        break;
                    }
                    cursor.close();
                    ContentValues values=new ContentValues();
                    values.put("lac",infoLoop.getLac());
                    values.put("sid", infoLoop.getCid());
                    Uri uri = getContentResolver().insert(insertUri, values);
                }
            }
            mHandler.postDelayed(mCellRunnable, 60000);
        }
    };
    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        mStopDrawing = false;
        mTelecomManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        mHandler.postDelayed(mCellRunnable,5000);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mStopDrawing = true;
    }
}
