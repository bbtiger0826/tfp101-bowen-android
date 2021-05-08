package idv.tfp10101.tfp10101bowen2;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class JobSchedulerFragment extends Fragment {
    private MainActivity mainActivity;
    private Resources resources;
    // JobScheduler 專用
    JobScheduler jobScheduler;

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

        return inflater.inflate(R.layout.fragment_job_scheduler, container, false);
    }

    /**
     * 生命週期-4
     * Layout已建立後 (設計: 互動的處理)
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        findViews(view);


        // (當下機子的版本 < 功能所需的版本)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return;
        }
        // 取得JobScheduler物件
        jobScheduler = (JobScheduler) mainActivity.getSystemService(Context.JOB_SCHEDULER_SERVICE);

        // 自定義JobService類別 (MyJobService.class)

        // 實例化ComponentName物件 (JobService可能會被呼叫很多次，所以用ComponentName包起來)
        // (context, 自定義的 JobService類別名.class)
        // 注意：要註冊服務(權限允許)
        ComponentName componentName = new ComponentName(mainActivity, MyJobService.class);

        // 新建JobInfo物件
        JobInfo jobInfo = new JobInfo.Builder(Constants.MY_JOB, componentName)
                .setRequiresCharging(true)      // 設定是否必須在充電狀態(電量大於90%)下才啟動
                .build();

        // 排入行程
        switch (jobScheduler.schedule(jobInfo)) {
            case JobScheduler.RESULT_SUCCESS :
                Toast.makeText(mainActivity, "RESULT_SUCCESS", Toast.LENGTH_SHORT).show();
                break;
            case JobScheduler.RESULT_FAILURE :
                Toast.makeText(mainActivity, "RESULT_FAILURE", Toast.LENGTH_SHORT).show();
                break;

            default:
        }
    }
}