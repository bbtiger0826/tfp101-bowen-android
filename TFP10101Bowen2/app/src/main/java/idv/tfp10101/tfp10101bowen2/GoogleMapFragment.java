package idv.tfp10101.tfp10101bowen2;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GoogleMapFragment extends Fragment {
    private MainActivity mainActivity;
    private Resources resources;
    private Set<String> permissionsRequest; // 使用者已同意的權限
    //
    private static final int REQ_POSITIONING = 1;
    private MapView mapView;
    private GoogleMap googleMap;
    //
    private int markerCount = 0;

    /**
     * 取得xml元件
     * @param view Activity下的view
     */
    private void findViews(View view) {
        mapView = view.findViewById(R.id.mapView);
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

        return inflater.inflate(R.layout.fragment_google_map, container, false);
    }

    /**
     * 生命週期-4
     * Layout已建立後 (設計: 互動的處理)
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        findViews(view);

        //
//        if (checkPermission()) {
//            handleMap(savedInstanceState);
//        }


    }

    /**
     * 地圖 相關處理
     */
    private void handleMap(Bundle savedInstanceState) {
        // 初始化地圖
        mapView.onCreate(savedInstanceState);
        // 啟動地圖
        mapView.onStart();
        // 註冊/實作MapView監聽器
        mapView.getMapAsync(googleMap -> {
            this.googleMap = googleMap;
            // 註冊 地圖長按監聽器 (非靜態方法參考)
            googleMap.setOnMapLongClickListener(this::addMarker);
            // 地圖 加入 自訂的訊息視窗
            googleMap.setInfoWindowAdapter(new MyInfoWindowAdapter(mainActivity));
            // 註冊 訊息視窗長按監聽器
            googleMap.setOnInfoWindowLongClickListener(marker -> {
                marker.remove();
            });

            // 取得UiSetting物件
            UiSettings uiSettings = googleMap.getUiSettings();
            uiSettings.setZoomControlsEnabled(true);

            showMyLocation();
        });
    }

    /**
     * 顯示當前位置(小藍點) 及 定位按鈕(右上角)
     * 並移動到該位置
     */
    private void showMyLocation() {
        if (ActivityCompat.checkSelfPermission(mainActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mainActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        // 顯示當前位置(小藍點) 及 定位按鈕(右上角)
        googleMap.setMyLocationEnabled(true);
        // 取得定位供應器物件
        FusedLocationProviderClient fusedLocationClient =
                LocationServices.getFusedLocationProviderClient(mainActivity);
        // 取得Task<Location>物件: 取得最後記錄位置
        Task<Location> task = fusedLocationClient.getLastLocation();
        // 註冊/實作 定位成功監聽器
        task.addOnSuccessListener(location -> {
            if (location != null) {
                // 取得緯度
                final double lat = location.getLatitude();
                // 取得經度
                final double lng = location.getLongitude();
                // 新建CameraPosition物件，並設定細節
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(new LatLng(lat, lng))  // 設定焦點
                        .zoom(18)       // 設定縮放倍數
                        .tilt(45)       // 設定傾斜角度
                        .bearing(90)    // 設定旋轉角度
                        .build();
                // 新建CameraUpdate物件
                CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
                // 使用鏡頭
                googleMap.animateCamera(cameraUpdate);
            }
        });
    }

    /**
     * 加入標記
     */
    private void addMarker(final LatLng latLng) {
        /**
         * 標記物件
         */
        // 實例化並設定 標記物件
        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng)
                .title("Marker" + ++markerCount)
                .snippet(latLngToName(latLng.latitude, latLng.longitude))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker))
                .draggable(true);
        // 地圖加入標記
        googleMap.addMarker(markerOptions);

        /**
         * 圓形範圍
         */
        // 實例化CircleOptions物件
        CircleOptions circleOptions = new CircleOptions()
                .center(latLng)                                             // 設定圓心
                .radius(50)                                                // 設定半徑
                .strokeWidth(5)                                             // 設定線寬
                .strokeColor(Color.rgb(239, 119, 220))     // 設定線色
                .fillColor(resources.getColor(R.color.colorFill));          // 設定填滿色
        // 地圖 加入 圓形
        googleMap.addCircle(circleOptions);
    }

    /**
     * 自定義類別訊息視窗Adapter類別
     * 實作InfoWindowAdapter介面
     * (static inner class)
     */
    private static class MyInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
        // 欄位: Context型態
        Context context;

        // 建構子: 1個參數，Context型態
        public MyInfoWindowAdapter(Context context) {
            this.context = context;
        }

        /**
         * (覆寫)
         * 回傳訊息視窗元件
         */
        @Override
        public View getInfoWindow(Marker marker) {
            // 載入外部xml檔案
            View view = View.inflate(context, R.layout.info_window, null);
            // 在此將marker(標記)相關資訊，設定至view(元件)
            final TextView tvTitle = view.findViewById(R.id.tvTitle);
            final TextView tvSnippet = view.findViewById(R.id.tvSnippet);
            tvTitle.setText(marker.getTitle());
            tvSnippet.setText(marker.getSnippet());
            return view;
        }

        /**
         * (覆寫)
         * 回傳訊息視窗的內容元件
         */
        @Override
        public View getInfoContents(Marker marker) {
            return null;
        }
    }

    /**
     * 緯經度 轉 地名/地址
     */
    private String latLngToName(final double lat, final double lng) {
        try {
            if (!Geocoder.isPresent()) {
                return null;
            }
            Geocoder geocoder = new Geocoder(mainActivity);
            List<Address> addressList = geocoder.getFromLocation(lat, lng, 1);
            StringBuilder name = new StringBuilder();
            Address address = addressList.get(0);
            if (address != null) {
                for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                    name.append(address.getAddressLine(i))
                            .append("\n");
                }
            }
            return name.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 檢查權限功能
     */
    public Boolean checkPermission() {
        // 詢問要使用的權限
        // 宣告App所需權限陣列 {Manifest.permission.權限名1, Manifest.permission.權限名2, .. }
        String[] permissions = { Manifest.permission.ACCESS_FINE_LOCATION};

        // 使用者已同意的權限
        permissionsRequest = new HashSet<>(); // 不知道有幾個授權，用set儲存

        // App所需權限陣列 逐一判斷
        for (String permission : permissions) {
            int result = ContextCompat.checkSelfPermission(mainActivity, permission); // 抓取檢查狀態int
            // 如果未授權，存入set後續再詢問
            if (result != PackageManager.PERMISSION_GRANTED) {
                permissionsRequest.add(permission);
            }
        }

        // Activity中再次詢問 (如果不需要詢問，set<String>是空的要避開)
        if (!permissionsRequest.isEmpty()) {
            requestPermission(permissionsRequest.toArray(new String[permissionsRequest.size()]));
            return false;
        } else {
            return true;
        }
    }

    /**
     * 權限詢問功能
     */
    public void requestPermission(String[] strings) {
        // Activity中,詢問權限 (跳出對話框) -> (Activity activity, String[] permissions, int 自訂詢問代碼)
        ActivityCompat.requestPermissions(
                mainActivity,
                strings,
                REQ_POSITIONING
        );
    }

    /**
     * 監聽詢問結果: 覆寫onRequestPermissionsResult()
     * (int requestCode, String[] permissions, rantResults)
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQ_POSITIONING) {
            for (int index = 0; index < grantResults.length; index++) {
                if (grantResults[index] == PackageManager.PERMISSION_GRANTED) {
                    // 已同意權限的後續處理..
                    Toast.makeText(mainActivity, "已同意權限", Toast.LENGTH_SHORT).show();
                    // handleMap(savedInstanceState);
                } else{
                    // 未同意權限的後續處理，EX. Toast提醒未同意無法使用xx功能
                    Toast.makeText(mainActivity, "未同意權限", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}