package idv.tfp10101.tfp10101bowen2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MyLocalBroadcastReceiver extends BroadcastReceiver {
    private final TextView textView;
    private final ProgressBar progressBar;

    public MyLocalBroadcastReceiver(TextView textView, ProgressBar progressBar) {
        this.textView = textView;
        this.progressBar = progressBar;
    }
    /**
     * 覆寫onReceive()方法: 當接收到廣播時，自動被呼叫
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        final Bundle bundle = intent.getExtras();
        final int batteryLevel = bundle.getInt("batteryLevel");
        textView.setText(String.valueOf(batteryLevel));
        progressBar.setProgress(batteryLevel);
    }
}
