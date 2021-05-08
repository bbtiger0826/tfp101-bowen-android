package idv.tfp10101.tfp10101bowen2;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

/**
 * 注意：要註冊Service (如果用工具點擊創建的方式，會自動生成)
 */
public class MyService extends Service {
    private static final String TAG = "Bowen_Test";

    // WakeLock 所需要的參數
    private PowerManager powerManager;
    private PowerManager.WakeLock wakeLock;
    // 播音樂用
    private MediaPlayer mediaPlayer;

    /**
     * 初始化
     */
    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate()");
        super.onCreate();

        // 取得PowerManager物件
        powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        // WakeLock(不休眠) - 處理
        handleWakeLock();
    }

    /**
     * 啟動相關處理
     * @param intent
     * @param flags
     * @param startId
     * @return
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand()");

        // 播放音樂
        // 注意：若讓Service負責較費時的工作時，應在Service中另開執行緒來執行
        new Thread(() -> {
            if (mediaPlayer == null) {
                // 新建MediaPlayer物件，並指定音訊來源
                mediaPlayer = MediaPlayer.create(MyService.this, R.raw.tsunomakiwatame_ed);
                // 開始播放音訊
                mediaPlayer.start();
            }
        }).start();

        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 沒用到，但是要強制Override
     * @param intent
     * @return
     */
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind()");
        // 略過，return null即可
        return null;
    }

    /**
     * 釋放資源
     */
    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy()");
        super.onDestroy();

        if (wakeLock != null) {
            // 釋放
            wakeLock.release();
            wakeLock = null;
        }

        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    /**
     * Wake Lock (不休眠) 相關處理
     */
    private void handleWakeLock() {
        // 建立PowerManager.WakeLock物件 (PowerManager.PARTIAL_WAKE_LOCK, "自訂的名稱")
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "TFP101 01 Bowen2:MyService");
        // 佔用 (毫秒數)
        wakeLock.acquire(10 * 60 * 1000);
    }
}