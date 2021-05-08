package idv.tfp10101.tfp10101bowen2;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class NotificationFragment extends Fragment {
    private static final String CHANNEL_ID = "CH01";        // 通道識別ID
    private static final int REQ_FOR_NOTIFICATION = 1;      // 跳轉代碼
    private static final int NOTIFICATION_ID = 1;           // 通知識別ID
    //
    private MainActivity mainActivity;
    private Resources resources;
    // 通知管理器
    private NotificationManager notificationManager;
    //
    private Button buttonSend, buttonCancel;

    /**
     * 取得xml元件
     * @param view Activity下的view
     */
    private void findViews(View view) {
        buttonSend = view.findViewById(R.id.buttonSend);
        buttonCancel = view.findViewById(R.id.buttonCancel);
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
        // 取得NotificationManager物件
        notificationManager = (NotificationManager) mainActivity.getSystemService(mainActivity.NOTIFICATION_SERVICE);

        return inflater.inflate(R.layout.fragment_notification, container, false);
    }

    /**
     * 生命週期-4
     * Layout已建立後 (設計: 互動的處理)
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        findViews(view);
        // 發送通 - 處理
        handleSendButton();
    }

    /**
     * 發送通知功能
     */
    private void handleSendButton() {
        buttonSend.setOnClickListener(view -> {
            // 取得NotificationChannel (另外開方法詳細設定)
            NotificationChannel notificationChannel = getNotificationChannel();

            // 將 NotificationChannel物件 加入至 NotificationManager物件
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                notificationManager.createNotificationChannel(notificationChannel);
            }

            // PendingIntent -> 用來設定 使用者點擊狀態欄中的通知項目時，跳轉的Activity
            PendingIntent pendingIntent = getPendingIntent();

            // Notification -> 設定/建立 Notification物件
            Notification notification = getNotification(pendingIntent);

            /** 發出通知 **/
            notificationManager.notify(NOTIFICATION_ID, notification);
        });

        /** 監聽 取消(收回)通知 **/
        buttonCancel.setOnClickListener(view -> notificationManager.cancel(NOTIFICATION_ID));
    }

    /**
     * NotificationChannel 設定
     */
    private NotificationChannel getNotificationChannel() {
        // 實例化NotificationChannel物件
        NotificationChannel notificationChannel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            /**("自訂通道識別ID", "自訂名稱", NotificationManager.IMPORTANCE_重要程度)*/
            notificationChannel = new NotificationChannel(CHANNEL_ID, "myChannel", NotificationManager.IMPORTANCE_HIGH);

            // 設定NotificationChannel物件
            // 是否啟用指示燈
            notificationChannel.enableLights(true);
            // 設定指示燈顏色
            notificationChannel.setLightColor(Color.RED);
            // 是否啟用震動
            notificationChannel.enableVibration(true);
            // 設定震動頻率
            notificationChannel.setVibrationPattern(new long[]{200, 400, 600, 800});
        }
        return notificationChannel;
    }

    /**
     * PendingIntent 設定
     */
    private PendingIntent getPendingIntent() {
        // 準備帶入的參數
        Bundle bundle = new Bundle();
        bundle.putSerializable("message", "CH01_message");
        // 實例化及設定Intent物件 (代表從那一個Activity出發, 轉換目的地的類別)
        Intent intent = new Intent(mainActivity, MainActivity.class);
        // 複雜的資料傳遞使用putExtras(bundle)
        intent.putExtras(bundle);
        // 取得PendingIntent物件 (context, 自訂跳轉代碼, intent, PendingIntent.FLAG_待處理意圖旗標)
        PendingIntent pendingIntent =
                PendingIntent.getActivity(mainActivity, REQ_FOR_NOTIFICATION, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;
    }

    /**
     * 設定/建立 Notification物件
     */
    private Notification getNotification(PendingIntent pendingIntent) {
        return new NotificationCompat.Builder(mainActivity, CHANNEL_ID)     // 需與自訂的通道識別ID相同
                .setSmallIcon(android.R.drawable.ic_dialog_email)           // 設定圖示
                .setContentTitle("CH01_title")                              // 設定標題文字
                .setContentText("CH01_content")                             // 設定內容文字
                .setAutoCancel(true)                                        // 設定是否自動取消
                .setContentIntent(pendingIntent)                            // 設定點擊後開啟的Activity
                .build();                                                   // 建立Notification物件
    }
}