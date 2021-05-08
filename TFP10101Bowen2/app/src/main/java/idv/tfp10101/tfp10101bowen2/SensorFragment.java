package idv.tfp10101.tfp10101bowen2;

import android.content.Context;
import android.content.res.Resources;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Arrays;

public class SensorFragment extends Fragment {
    private MainActivity mainActivity;
    private Resources resources;
    // Sensor基本
    private SensorManager sensorManager;
    private Sensor sensor;
    private SensorEventListener sensorEventListener;
    // 方位感應器用 (加速度感應器值, 磁場感應器值)
    private float[] valuesAccelerometer, valuesMagnitude;
    private Sensor accSensor, magSensor;
    //
    private TextView textViewName, textViewValues;

    /**
     * 取得xml元件
     * @param view Activity下的view
     */
    private void findViews(View view) {
        textViewName = view.findViewById(R.id.textViewName);
        textViewValues = view.findViewById(R.id.textViewValues);
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

        return inflater.inflate(R.layout.fragment_sensor, container, false);
    }

    /**
     * 生命週期-4
     * Layout已建立後 (設計: 互動的處理)
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        findViews(view);

        // 加速度
//        handleAccelerometerSensor();

        // 陀螺儀
//        handleGyroscopeSensor();

        // 方位 -> 加速度 + 磁場
//        handleOrientationSensor();

        // 接近感應器
        handleProximitySensor();

        // 光度感應器
//        handleLightSensor();

    }

    /**
     * 生命週期-8
     * Activity暫停或Fragment將要分離 (設計: 註銷)
     */
    @Override
    public void onPause() {
        super.onPause();

        // 註銷 感應器事件監聽器
        sensorManager.unregisterListener(sensorEventListener, sensor);
    }

    /**
     * AccelerometerSensor 相關處理 (加速度)
     */
    private void handleAccelerometerSensor() {
        sensorRegister(Sensor.TYPE_ACCELEROMETER);
    }

    /**
     * GyroscopeSensor 相關處理 (陀螺儀)
     */
    private void handleGyroscopeSensor() {
        sensorRegister(Sensor.TYPE_GYROSCOPE);
    }

    /**
     * OrientationSensor 相關處理 (方位 -> 加速度 + 磁場)
     */
    private void handleOrientationSensor() {
        sensorRegister(Sensor.TYPE_ACCELEROMETER);
        sensorRegister(Sensor.TYPE_MAGNETIC_FIELD);
    }

    /**
     * Proximity 相關處理
     */
    private void handleProximitySensor() {
        sensorRegister(Sensor.TYPE_PROXIMITY);
    }

    /**
     * Proximity 相關處理
     */
    private void handleLightSensor() {
        sensorRegister(Sensor.TYPE_LIGHT);
    }

    /**
     * SensorManager 的設定
     */
    private void sensorRegister(int sensorType) {
        // 取得SensorManager物件 (取得SystemService -> SENSOR_SERVICE)
        sensorManager = (SensorManager) mainActivity.getSystemService(Context.SENSOR_SERVICE);
        // 註冊 感應器事件監聽器
        // 1 實例化監聽器物件 (拉出去實作)
        /** getSensorEventListener() */
        // 2 取得感應器
        sensor = sensorManager.getDefaultSensor(sensorType);
        // 註冊監聽並設定事件發送頻率
        sensorManager.registerListener(getSensorEventListener(), sensor, SensorManager.SENSOR_DELAY_UI);
    }

    /**
     * 自定義感應器事件監聽器
     * (覆寫)
     */
    private SensorEventListener getSensorEventListener() {
        return sensorEventListener = new SensorEventListener() {
            // 數值改變時，自動被呼叫
            @Override
            public void onSensorChanged(SensorEvent event) {
                /**
                 * 一般感應監聽
                 */
                final Sensor sensor = event.sensor;       // 感應器
                final String name = sensor.getName();     // 感應器名稱
                final float[] values = event.values;      // 數值

                textViewName.setText(name);
                textViewValues.setText(Arrays.toString(values));

                /**
                 * OrientationSensor感應監聽 (特別要計算)
                 */
                // 各別儲存加速度感應器 及 磁場感應器 的數值
                final int type = sensor.getType();        // 感應器代碼

                // 注意：event.values是 float[] 屬於 傳參考呼叫
                switch (type) {
                    // 加速度感應器
                    case Sensor.TYPE_ACCELEROMETER:
                        valuesAccelerometer = values.clone();
                        break;
                    // 磁場感應器
                    case Sensor.TYPE_MAGNETIC_FIELD:
                        valuesMagnitude = values.clone();
                        break;
                }

                // 如果值都有了(orientation需要的2個感應)
                if (valuesAccelerometer != null && valuesMagnitude != null) {
                    // 轉成旋轉矩陣
                    float[] R = new float[9];   		// 轉換出的旋轉矩陣，會存在此變數中
                    SensorManager.getRotationMatrix(R, null, valuesAccelerometer, valuesMagnitude);

                    // 計算出目前方位的數值
                    float[] orientationValues = new float[3];      // 計算出的方位數值，會存在此變數中
                    SensorManager.getOrientation(R, orientationValues);

                    //
                    textViewName.setText("OrientationSensor");
                    textViewValues.setText(Arrays.toString(orientationValues));
                }
            }

            // 精準度改變時，自動被呼叫
            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
    }
}