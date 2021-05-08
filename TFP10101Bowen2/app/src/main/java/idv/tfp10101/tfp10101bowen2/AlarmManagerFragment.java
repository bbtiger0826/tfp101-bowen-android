package idv.tfp10101.tfp10101bowen2;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Calendar;

public class AlarmManagerFragment extends Fragment {
    private MainActivity mainActivity;
    private Resources resources;
    // AlarmManager 專用
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private Intent intent;

    /**
     * 取得xml元件
     * @param view Activity下的view
     */
    private void findViews(View view) {
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

        return inflater.inflate(R.layout.fragment_alarm_manager, container, false);
    }

    /**
     * 生命週期-4
     * Layout已建立後 (設計: 互動的處理)
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        findViews(view);

//        // 設定鬧鐘(Alarm)
//        setAlarm(hourOfDay, minute);
    }

    /**
     * 設定鬧鐘(Alarm)
     */
    private void setAlarm(final int hours, final int minutes) {
        /**
         * 建立意圖跳轉(alarmManager <- PendingIntent <- Intent)
         */
//        // 取得AlarmManager物件
//        alarmManager = (AlarmManager) mainActivity.getSystemService(Context.ALARM_SERVICE);
//        // 實例化Intent物件 (Receiver 要先註冊)
//        intent = new Intent(this, AlarmReceiver.class);
//        // 建立PendingIntent物件
//        pendingIntent = PendingIntent.getBroadcast(
//                this, REQ_ALARM_RECEIVE, intent, PendingIntent.FLAG_CANCEL_CURRENT);
//
//        // 4.設定鬧鐘(Alarm)
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(Calendar.HOUR_OF_DAY, hours);
//        calendar.set(Calendar.MINUTE, minutes);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            // 在休眠狀態、低電量仍觸發的單次準時鬧鐘
//            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
//        } else {
//            // 設定單次鬧鐘
//            // API 1就有的方法，但API 19(+，改成不準時觸發
//            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
//        }
//
//        // 儲存至偏好設定檔(Shared Preferences)
//        sharedPreferences.edit()
//                .putLong(KEY_ALARM_TIME, calendar.getTimeInMillis())
//                .apply();
    }
}