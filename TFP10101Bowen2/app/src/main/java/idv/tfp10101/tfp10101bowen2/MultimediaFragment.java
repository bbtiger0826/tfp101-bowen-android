package idv.tfp10101.tfp10101bowen2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class MultimediaFragment extends Fragment {
    private static final int REQ_CAMERA = 1;
    private static final int REQ_ALBUM = 2;
    private static final int RRQ_PERMISSIONS = 3;

    private MainActivity mainActivity;
    private Resources resources;
    private File fileCamera; // 相機File
    private ImageView imageViewCamera;
    private Button buttonCamera, buttonAlbum;
    private Button buttonVolumeUp, buttonVolumeDown, buttonVolumeOff;
    private Set<String> permissionsRequest; // 使用者已同意的權限
    private TextView textViewSound;
    private Button buttonSound, buttonMedia, buttonInterentMedia;
    private SoundPool soundPool; // 音效專用
    private int soundID; // 音訊ID
    private int streamID; // 音訊串流ID
    private MediaPlayer mediaPlayer, mediaPlayerExternal; // 音樂專用 , 外部音樂專用

    /**
     * 取得xml元件
     * @param view Activity下的view
     */
    private void findViews(View view) {
        imageViewCamera = view.findViewById(R.id.imageViewCamera);
        buttonCamera = view.findViewById(R.id.buttonCamera);
        buttonAlbum = view.findViewById(R.id.buttonAlbum);
        buttonVolumeUp = view.findViewById(R.id.buttonVolumeUp);
        buttonVolumeDown = view.findViewById(R.id.buttonVolumeDown);
        buttonVolumeOff = view.findViewById(R.id.buttonVolumeOff);
        textViewSound = view.findViewById(R.id.textViewSound);
        buttonSound = view.findViewById(R.id.buttonSound);
        buttonMedia = view.findViewById(R.id.buttonMedia);
        buttonInterentMedia = view.findViewById(R.id.buttonInterentMedia);
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

        return inflater.inflate(R.layout.fragment_multimedia, container, false);
    }

    /**
     * 生命週期-4
     * Layout已建立後 (設計: 互動的處理)
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        findViews(view);
        // 照相 - 處理
        handleCamera();
        // 相簿 - 處理
        handleAlbum();
        // 音量 - 處理
        handleVolume();
        // 聲音 - 處理
        handleSound();
    }

    /**
     * 照相功能
     */
    private void handleCamera() {
        buttonCamera.setOnClickListener(view -> {
            // 實例化Intent物件 - 跳轉相機App
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // 檢查
            if (isIntentAvailable(intent)) {
                // 取得File物件，並設定存檔路徑，後續作為圖取照片的路徑 注意: (原圖專用)
                fileCamera = mainActivity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                fileCamera = new File(fileCamera, "20210427.jpg");

                // 使用FileProvider建立Uri物件 注意: (原圖專用)
                // getPackageName() + ".fileProvider" -> 須與authorities屬性之值相同
                // 程式中的getPackageName() 等同 XML中的 ${applicationId}
                Uri uri = FileProvider.getUriForFile(
                        mainActivity,
                        mainActivity.getPackageName() + ".fileProvider",
                        fileCamera);

                /**
                 * 設定存檔路徑 (原圖專用)
                 * 注意: 若要使用縮圖，不可以加上此行，否則會發生錯誤!
                 */
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

                /**
                 * 跳轉: 有2種方法 startActivity() & startActivityForResult()
                 * startActivityForResult() 才可以接收回傳的資料 (intent, 自訂跳轉代碼)
                 * 需要覆寫 onActivityResult() 監聽
                 */
                startActivityForResult(intent, REQ_CAMERA);
            }
        });
    }

    /**
     * 抓取相簿照片功能
     */
    private void handleAlbum() {
        buttonAlbum.setOnClickListener(view -> {
            // 實例化Intent物件
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

            // 檢查  (此段android有問題 先拿掉)
//            if (isIntentAvailable(intent)) {
//            }
            // 跳轉至內建相簿App (intent, 自訂跳轉代碼)
            startActivityForResult(intent, REQ_ALBUM);
        });
    }

    /**
     *  調整音量功能
     */
    private void handleVolume() {
        // 檢查 是否有開啟權限
        checkPermission();
        // 音量調整 (系統級服務 須使用AudioManager來完成)
        // 取得AudioManager物件
        AudioManager audioManager = (AudioManager) mainActivity.getSystemService(mainActivity.AUDIO_SERVICE);

        // 升音
        buttonVolumeUp.setOnClickListener(view -> {
            // 檢查 是否有開啟權限
            if (checkPermission()) {
                audioManager.adjustVolume(AudioManager.ADJUST_RAISE, 0);
            }
        });

        // 降音
        buttonVolumeDown.setOnClickListener(view -> {
            // 檢查 是否有開啟權限
            if (checkPermission()) {
                audioManager.adjustVolume(AudioManager.ADJUST_LOWER, 0);
            }
        });

        // 靜音
        buttonVolumeOff.setOnClickListener(view -> {
            // 檢查 是否有開啟權限
            if (checkPermission()) {
                // 靜音: ADJUST_MUTE、取消靜音: ADJUST_UNMUTE、改變靜音狀態: ADJUST_TOGGLE_MUTE
                audioManager.adjustVolume(AudioManager.ADJUST_TOGGLE_MUTE, AudioManager.FLAG_SHOW_UI);
            }
        });
    }

    /**
     * 音效音樂相關功能
     */
    private void handleSound() {
        // 新建SoundPool物件 (if API版本)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            soundPool = new SoundPool.Builder().build();
        }

        /**
         * 音效
         */
        // 載入音訊，並存下音訊ID(soundID)
        // 其中資源目錄，使用原始檔目錄(res/raw)
        soundID = soundPool.load(mainActivity, R.raw.tsunomakiwatame, 1);
        buttonSound.setOnClickListener(view -> {
            // 開始播放音訊，並存下音訊串流ID(streamID)
            streamID = soundPool.play(soundID, 1, 1, 0, 0, 1);
        });

        /**
         * 音樂
         */
        buttonMedia.setOnClickListener(view -> {
            // 新建MediaPlayer物件，並指定音訊來源
            mediaPlayer = MediaPlayer.create(mainActivity, R.raw.tsunomakiwatame_ed);
            // 開始播放音訊
            mediaPlayer.start();
        });

        /**
         * 網路外部音樂 (電腦裡的 or 網路)
         * 需要權限 (網路權限 -> Protection level: normal) 不用檢查
         */
        buttonInterentMedia.setOnClickListener(view -> {
            // 取得音訊源
            // 電腦裡的
        /*File dir = mainActivity.getExternalFilesDir(Environment.DIRECTORY_MUSIC);
        File audioFile = new File(dir, "音訊檔檔名");
        String path = audioFile.getPath();*/

            // 網路
            String path = "https://www.youtube.com/watch?v=NMs9Z_G9X0k";

            // 實例化MediaPlayer物件，並指定音訊來源setDataSource(path)
            mediaPlayerExternal = new MediaPlayer();

            // 設定音訊源
            // 要處理異常
            try {
                mediaPlayerExternal.setDataSource(path);
            } catch (IOException e) {
                e.printStackTrace();
            }

            // 設定音訊種類
            mediaPlayerExternal.setAudioStreamType(AudioManager.STREAM_MUSIC);

            //註冊/實作 播放器已準備監聽器 (音訊源為外部儲存體/網路時，須在監聽裡開始播放)
            mediaPlayerExternal.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start(); // 準備好就開始播放
                }
            });

            /**
             * 生命週期
             * stop() → reset() → setDataSource() → prepareAsync()
             */
            // 準備播放器
            mediaPlayerExternal.prepareAsync();
        });
    }

    /**
     * 監聽 ActivityResult
     *
     * @param requestCode   跳轉代碼(抓取當初定義的代碼來片段回傳的對應Result)
     * @param resultCode    result(結果)
     * @param intent        intent所有資料(包含在另一端使用者所操作的Data)
     */
    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        /**
         * REQ_CAMERA 的Result
         */
        if (resultCode == mainActivity.RESULT_OK && requestCode == REQ_CAMERA) {
            /**
             * 取得縮圖 or 取得原圖
             */
            // 取得縮圖
//            Bitmap bitmap = (Bitmap) intent.getExtras().get("data");

            // 取得原圖-Android 9(+  注意: (原圖專用)
            // 從File物件建立ImageDecoder.Source物件
            ImageDecoder.Source source = ImageDecoder.createSource(fileCamera);

            //取得Bitmap物件 注意: (原圖專用)
            Bitmap bitmap = null;
            try {
                bitmap = ImageDecoder.decodeBitmap(source);
            } catch (IOException e) {
                e.printStackTrace();
            }

            imageViewCamera.setImageBitmap(bitmap);
        }
        /**
         * REQ_ALBUM 的Result
         */
        if (resultCode == mainActivity.RESULT_OK && requestCode == REQ_ALBUM) {
            // 取得Uri物件
            Uri uri = intent.getData();

            // Android 9(+
            // 從Uri物件建立ImageDecoder.Source物件
            ImageDecoder.Source source = ImageDecoder.createSource(
                    mainActivity.getContentResolver(),
                    uri
            );

            // 取得Bitmap物件
            Bitmap bitmap = null;
            try {
                bitmap = ImageDecoder.decodeBitmap(source);
            } catch (IOException e) {
                e.printStackTrace();
            }

            imageViewCamera.setImageBitmap(bitmap);
        }
        super.onActivityResult(requestCode, resultCode, intent);
    }

    /**
     * 檢查是否有內建Intent
     */
    private boolean isIntentAvailable(Intent intent) {
        PackageManager packageManager = mainActivity.getPackageManager();
        return intent.resolveActivity(packageManager) != null;
    }

    /**
     * 檢查權限功能
     */
    public Boolean checkPermission() {
        // 詢問要使用的權限
        // 宣告App所需權限陣列 {Manifest.permission.權限名1, Manifest.permission.權限名2, .. }
        String[] permissions = { Manifest.permission.RECORD_AUDIO};

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
            requestPermission();
            return false;
        } else {
            return true;
        }
    }

    /**
     * 權限詢問功能
     */
    public void requestPermission() {
        // Activity中再次詢問 (如果不需要詢問，set<String>是空的要避開)
        if (!permissionsRequest.isEmpty()) {
            // Activity中,詢問權限 (跳出對話框) -> (Activity activity, String[] permissions, int 自訂詢問代碼)
            ActivityCompat.requestPermissions(
                    mainActivity,
                    permissionsRequest.toArray(new String[permissionsRequest.size()]),
                    RRQ_PERMISSIONS
            );
        }
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
        if (requestCode == RRQ_PERMISSIONS) {
            for (int index = 0; index < grantResults.length; index++) {
                if (grantResults[index] == PackageManager.PERMISSION_GRANTED) {
                    // 已同意權限的後續處理..
                    Toast.makeText(mainActivity, "已同意權限", Toast.LENGTH_SHORT).show();
                } else{
                    // 未同意權限的後續處理，EX. Toast提醒未同意無法使用xx功能
                    Toast.makeText(mainActivity, "未同意權限", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (soundPool != null) {
            // 釋放資源
            soundPool.release();
        }

        if (mediaPlayer != null) {
            // 釋放資源
            mediaPlayer.release();
        }

        if (mediaPlayerExternal != null) {
            // 釋放資源
            mediaPlayerExternal.release();
        }

//        if (mediaRecorder != null) {
//            // 釋放資源
//            mediaRecorder.release();
//        }
    }
}