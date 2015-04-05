package com.goldsand.outservice;

import java.util.List;

import android.R.string;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.telecom.TelecomManager;
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
                     Log.i(TAG,"LAC : "+infoLoop.getLac());
                     Log.i(TAG,"CID : "+infoLoop.getCid());
                     Log.i(TAG,"Rssi : "+infoLoop.getRssi());
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
