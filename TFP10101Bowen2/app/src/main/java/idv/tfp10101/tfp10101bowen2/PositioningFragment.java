package idv.tfp10101.tfp10101bowen2;

import android.Manifest;
import android.content.IntentSender;
import android.content.res.Resources;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.Task;

/**
 * Positioning練習
 */
public class PositioningFragment extends Fragment {
    private static final int REQ_LOCATION_SETTINGS = 1;

    private MainActivity mainActivity;
    private Resources resources;
    /** 授權設定 */
    RepuestPermission repuestPermission = RepuestPermission.getInstance();

    /** 定位相關 */
    // 定位供應器物件
    private FusedLocationProviderClient fusedLocationClient;
    // 定期更新定位物件
    private LocationCallback locationCallback;
    // 定位設定
    private LocationRequest locationRequest;

    // view上的物件
    private Button buttonPositioning;
    private TextView textViewPositioning;

    /**
     * 取得xml元件
     * @param view Activity下的view
     */
    private void findViews(View view) {
        buttonPositioning = view.findViewById(R.id.buttonPositioning);
        textViewPositioning = view.findViewById(R.id.textViewPositioning);
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

        return inflater.inflate(R.layout.fragment_positioning, container, false);
    }

    /**
     * 生命週期-4
     * Layout已建立後 (設計: 互動的處理)
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        findViews(view);

        // 當下Activity存入到權限管理
        repuestPermission.setMainActivity(mainActivity);
        // 詢問並加入需要的權限
        String[] strings = {
                Manifest.permission.ACCESS_COARSE_LOCATION, // 大約位置
                Manifest.permission.ACCESS_FINE_LOCATION // 精準位置
        };
        repuestPermission.requestPermission(strings);

        // 定位功能
        handlePositioning();
    }

    /**
     * 定位 - 相關處裡
     */
    private void handlePositioning() {
        // 點選定位按鈕
        buttonPositioning.setOnClickListener(view -> {
            // 權限檢查
            if (!repuestPermission.checkPermission()) {
                repuestPermission.requestWaitingforPermission();
                return;
            }

            /**
             * 定位檢查
             * 定位回傳物件 <- 定位請求物件
             */
            checkPositioning();

            /**  取得位置 **/
            // 取得定位供應器物件
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(mainActivity);
            // 取得Task<Location>物件
            Task<Location> task = fusedLocationClient.getLastLocation();
            // 註冊/實作 定位成功監聽器
            task.addOnSuccessListener(location -> {
                if (location != null) {
                    // 取得緯度
                    final double lat = location.getLatitude();
                    // 取得經度
                    final double lng = location.getLongitude();
                    StringBuilder text = new StringBuilder()
                            .append("Lat: ")
                            .append(lat)
                            .append("\nLng: ")
                            .append(lng);
                    textViewPositioning.setText(text);
                    /**
                     * 定期更新
                     * 更新 (locationRequest + locationCallback)
                     */
                    intervalPositioning();
                }
            });
        });
    }

    /**
     * 定位功能檢查
     */
    private void checkPositioning() {
        // 檢查定位
        // 取得SettingsClient物件
        SettingsClient settingsClient = LocationServices.getSettingsClient(mainActivity);
        // 檢查裝置是否開啟定位設定
        Task<LocationSettingsResponse> task =
                settingsClient.checkLocationSettings(getLocationSettingsRequest());
        // 註冊/實作 失敗監聽器: 若裝置未開啟定位，跳轉至定位設定的對話框
        task.addOnFailureListener(e -> {
            if (e instanceof ResolvableApiException) {
                try {
                    ResolvableApiException resolvable = (ResolvableApiException) e;
                    // 跳轉至定位設定的對話框
                    resolvable.startResolutionForResult(mainActivity, REQ_LOCATION_SETTINGS);
                } catch (IntentSender.SendIntentException sendEx) {
                    Log.e(Constants.TAG, e.getMessage());
                }
            }
        });
    }

    /**
     * 定位設定
     */
    private LocationSettingsRequest getLocationSettingsRequest() {
        // 定位請求物件
        // 建立
        locationRequest = LocationRequest.create();
        // 設定更新週期
        locationRequest.setInterval(10000);
        // 設定最快更新週期
        locationRequest.setFastestInterval(5000);
        // 設定優先順序
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        // 建立定位設定物件，並加入建立的定位請求物件
        return new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest)
                .build();
    }

    /**
     * 定期更新
     */
    private void intervalPositioning() {
        // 定期更新定位
        // 實例化/實作 LocationCallback物件
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                /** 取得定位資訊 **/
                // 取得Location物件
                final Location location = locationResult.getLastLocation();
                // 取得緯度
                final double lat = location.getLatitude();
                // 取得經度
                final double lng = location.getLongitude();
                // 取得定位時間
                final long time = location.getTime();
                // 取得精確度 (單位為公尺)
                final float accuracy = location.getAccuracy();
                // 取得高度 (單位為公尺)
                final double altitude = location.getAltitude();
                // 取得速度 (單位為公尺/秒)
                final float speed = location.getSpeed();
                // 取得位移方向 (單位為角度)
                final float bearing = location.getBearing();

                StringBuilder text = new StringBuilder()
                        .append("Lat: ")
                        .append(lat)
                        .append("\nLng: ")
                        .append(lng)
                        .append("\nTime: ")
                        .append(time)
                        .append("\nAccuracy: ")
                        .append(accuracy)
                        .append("\nAltitude: ")
                        .append(altitude)
                        .append("\nSpeed: ")
                        .append(speed)
                        .append("\nBearing: ")
                        .append(bearing);
                textViewPositioning.setText(text);
            }
        };
        // 權限檢查
        if (!repuestPermission.checkPermission()) {
            return;
        }
        // 請求定位更新
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }
}