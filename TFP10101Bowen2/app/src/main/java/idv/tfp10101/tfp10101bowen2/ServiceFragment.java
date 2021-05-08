package idv.tfp10101.tfp10101bowen2;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class ServiceFragment extends Fragment {
    private MainActivity mainActivity;
    private Resources resources;
    //
    private Button buttonPlaySound, buttonStopSound;

    /**
     * 取得xml元件
     * @param view Activity下的view
     */
    private void findViews(View view) {
        buttonPlaySound = view.findViewById(R.id.buttonPlaySound);
        buttonStopSound = view.findViewById(R.id.buttonStopSound);
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

        return inflater.inflate(R.layout.fragment_service, container, false);
    }

    /**
     * 生命週期-4
     * Layout已建立後 (設計: 互動的處理)
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        findViews(view);

        // 啟動Service -> new Intent(context, 自訂的Service類別名.class)
//        mainActivity.startService(new Intent(mainActivity, MyService.class));

        //
        handleButton();
    }

    /**
     * Button 相關處理
     */
    private void handleButton() {
        // 啟動Service -> new Intent(context, 自訂的Service類別名.class)
        final Intent intent = new Intent(mainActivity, MyService.class);

        // 啟動Service
        buttonPlaySound.setOnClickListener(view -> mainActivity.startService(intent));
        // 停止Service
        buttonStopSound.setOnClickListener(view -> mainActivity.stopService(intent));
    }
}