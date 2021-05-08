package idv.tfp10101.tfp10101bowen2;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

public class BroadcastFragment extends Fragment {
    private MainActivity mainActivity;
    private Resources resources;
    //
    private TextView textViewBattery;
    private ProgressBar progressBarBattery;
    //
    MySystemBroadcastReceiver mySystemBroadcastReceiver;
    MyLocalBroadcastReceiver myLocalBroadcastReceiver;

    /**
     * 取得xml元件
     * @param view Activity下的view
     */
    private void findViews(View view) {
        textViewBattery = view.findViewById(R.id.textViewBattery);
        progressBarBattery = view.findViewById(R.id.progressBarBattery);
    }

    /**
     * 生命週期-2
     * 初始化與畫面無直接關係之資料 (設計: )
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * 生命週期-3
     * 載入並建立Layout (設計: )
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // 取得Activity參考
        mainActivity = (MainActivity) getActivity();
        // 取得Resources
        resources = getResources();

        return inflater.inflate(R.layout.fragment_broadcast, container, false);
    }

    /**
     * 生命週期-4
     * Layout已建立後 (設計: 互動的處理)
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        findViews(view);
        // 接收區域(Local)廣播 - 處理
        handleBroadcastReceiver();
        // 發送區域(Local)廣播 - 處理 (暫無功能)
        //handleSendBroadcast();
    }

    /**
     * BroadcastReceiver 接收 廣播 (System & Loacl)
     * 結構：register (BroadcastReceiver <-> IntentFilter)
     *        登記         廣播接收器            意圖篩選
     */
    private void handleBroadcastReceiver() {
        /** System */
        // 實例化廣播接收器物件
        mySystemBroadcastReceiver = new MySystemBroadcastReceiver();
        // 實例化IntentFilter物件，同時設定欲接收的動作(Action)
        IntentFilter intentFilterSystem = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        // 註冊區域廣播接收器
        mainActivity.registerReceiver(mySystemBroadcastReceiver, intentFilterSystem);


        /** Loacl */
        // 實例化廣播接收器物件
        myLocalBroadcastReceiver = new MyLocalBroadcastReceiver(textViewBattery, progressBarBattery);
        // 實例化IntentFilter物件，同時設定欲接收的動作(Action)
        IntentFilter intentFilterLocal = new IntentFilter(Constants.ACTION_BATTERY_BROADCAST);
        // 註冊區域廣播接收器
        LocalBroadcastManager
                .getInstance(mainActivity)
                .registerReceiver(myLocalBroadcastReceiver, intentFilterLocal);

    }

    /**
     * SendBroadcast 發送 區域廣播
     */
    private void handleSendBroadcast() {
        // 放入欲傳遞的資料
        Bundle bundle = new Bundle();
        bundle.putString("myBroadcast", "我的廣播來了");
        // 實例化Intent物件 ("自訂的動作(Action)字串")
        Intent intent = new Intent("TEST");
        // Bundle in Intent (putExtras)
        intent.putExtras(bundle);
        // 發送 sendBroadcast(Intent)
        LocalBroadcastManager.getInstance(mainActivity).sendBroadcast(intent);
    }

    /**
     * 生命週期-11
     * 銷毀時 (設計: )
     */
    @Override
    public void onDestroy() {
        super.onDestroy();

        // Receiver 在不需要時註銷
        mainActivity.unregisterReceiver(mySystemBroadcastReceiver);
        LocalBroadcastManager.getInstance(mainActivity).unregisterReceiver(myLocalBroadcastReceiver);
    }
}