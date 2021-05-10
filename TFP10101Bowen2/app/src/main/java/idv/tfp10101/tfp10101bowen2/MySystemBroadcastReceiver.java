package idv.tfp10101.tfp10101bowen2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.os.Bundle;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class MySystemBroadcastReceiver extends BroadcastReceiver {
    /**
     * 覆寫onReceive()方法: 當接收到廣播時，自動被呼叫
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        // 取得電量
        final int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);

        // 接收到系統發出的電量改變廣播，就再自行發出區域型廣播 -> 實作 區域廣播接受器
        // 實例化Intent物件
        // 放入欲傳遞的資料
        Bundle bundle = new Bundle();
        bundle.putInt("batteryLevel", level);
        // 實例化Intent物件 ("自訂的動作(Action)字串")
        Intent intentLocal = new Intent(Constants.ACTION_BATTERY_BROADCAST);
        // Bundle in Intent (putExtras)
        intentLocal.putExtras(bundle);
        // 發送 sendBroadcast(區域的Intent)
        LocalBroadcastManager.getInstance(context).sendBroadcast(intentLocal);
    }
}
