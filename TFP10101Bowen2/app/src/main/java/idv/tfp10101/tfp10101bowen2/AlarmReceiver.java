package idv.tfp10101.tfp10101bowen2;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

import static android.content.Context.NOTIFICATION_SERVICE;

public class AlarmReceiver extends BroadcastReceiver {
    // 通知管理器
    private NotificationManager notificationManager;

    // 接收到後的實作
    @Override
    public void onReceive(Context context, Intent intent) {
        // 取得NotificationManager物件
        notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        // 取得NotificationChannel (另外開方法詳細設定)
        NotificationChannel notificationChannel = getNotificationChannel();

        // 將 NotificationChannel物件 加入至 NotificationManager物件
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(notificationChannel);
        }

        // Notification -> 設定/建立 Notification物件
        //Notification notification = getNotification(pendingIntent);

        //
        /** 發出通知 **/
        //notificationManager.notify(Constants.AlarmReceiver_Notification_ID, notification);
    }

    /**
     * NotificationChannel 設定
     */
    private NotificationChannel getNotificationChannel() {
        // 實例化NotificationChannel物件
        NotificationChannel notificationChannel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            /**("自訂通道識別ID", "自訂名稱", NotificationManager.IMPORTANCE_重要程度)*/
            notificationChannel = new NotificationChannel(
                    Constants.AlarmReceiver_NotificationChannel_ID,
                    "myChannel",
                    NotificationManager.IMPORTANCE_HIGH);

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
}
