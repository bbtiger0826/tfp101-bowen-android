package idv.tfp10101.tfp10101bowen2;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

/**
 * 繼承JobService類別
 */
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class MyJobService extends JobService {
    private Thread thread;
    // 當工作開始執行時，自動被呼叫
    @Override
    public boolean onStartJob(JobParameters params) {
        // 測試 (在其中另開執行緒來執行)
        thread = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                try {
                    Log.d(Constants.TAG, "i = : " + i);
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();

        return true;
    }

    // 當工作被取消時，自動被呼叫
    @Override
    public boolean onStopJob(JobParameters params) {
        Toast.makeText(this, "onStopJob()", Toast.LENGTH_SHORT).show();
        return false;
    }
}
