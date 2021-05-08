package idv.tfp10101.tfp10101bowen2;

import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class DataAccessFragment extends Fragment {
    private static final String TAG = "Bowen_Error";

    private MainActivity mainActivity;
    private Resources resources;
    private EditText editTextSaveName, shardPreferencesSaveName, internalStorageSaveName, externalStorageSaveName;
    private TextView textViewLoadName, shardPreferencesLoadName, internalStorageLoadName, externalStorageLoadName;
    private Button buttonSave, buttonLoad;

    /**
     * 取得xml元件
     * @param view Activity下的view
     */
    private void findViews(View view) {
        editTextSaveName = view.findViewById(R.id.editTextSaveName);
        textViewLoadName = view.findViewById(R.id.textViewLoadName);
        shardPreferencesSaveName = view.findViewById(R.id.shardPreferencesSaveName);
        shardPreferencesLoadName = view.findViewById(R.id.shardPreferencesLoadName);
        internalStorageSaveName = view.findViewById(R.id.internalStorageSaveName);
        internalStorageLoadName = view.findViewById(R.id.internalStorageLoadName);
        externalStorageSaveName = view.findViewById(R.id.externalStorageSaveName);
        externalStorageLoadName = view.findViewById(R.id.externalStorageLoadName);
        buttonSave = view.findViewById(R.id.buttonSave);
        buttonLoad = view.findViewById(R.id.buttonLoad);
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

        return inflater.inflate(R.layout.fragment_data_access, container, false);
    }

    /**
     * 生命週期-4
     * Layout已建立後 (設計: 互動的處理)
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        findViews(view);

        // Asset sava & load - 處理
        handleAsset();
    }

    /**
     * 1. Asset 讀 功能
     * Android會預先載入至記憶體  -> 主要是 AssetManager 來做儲存
     * assets目錄，與res目錄相似，一樣儲存於Android專案內
     * 路徑: Android專案的app目錄 → 右鍵 → New → Folder → Assets Folder (需新建assets目錄)
     * 注意: 唯讀
     *
     * 2. Shared Preferences 存讀 功能
     * 偏好設定檔，是儲存於裝置上硬碟的XML檔 -> 主要是 SharedPreferences 來做儲存
     * 路徑: /data/data/App根套件/shared_prefs/
     * (以key-value pairs方式存取資料)
     * 注意: 資料僅限基本型態及字串，App被移除時，偏好設定檔會一併被移除
     *
     * 3. Internal Storage 存讀 功能
     * 內部儲存體，儲存裝置上硬碟的私有檔案(Private File) -> 主要是 openFileInput 來做儲存
     * 路徑: /data/data/App根套件/files/
     * 注意: 每個App只可存取自己建立的私有檔案當，App被移除時，私有檔案會一併被移除
     * (可配合物件 序列化/反序列化 使用 -> ObjectOutputStream ObjectInputStream)
     *
     * 4. External Storage 存讀 功能
     * 外部儲存體，儲存裝置上硬碟的外部檔案(External File)
     * 路徑: /storage/emulated/0/Android/data/App根套件/files/目錄種類/
     * (外部檔案沒有存取限制，所以可能因為外部原因，造成檔案暫時無法使用，
     * 或內容被修改，甚至刪除，安全性及私密性較低)
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void handleAsset() {
        // 取得AssetManager物件
        AssetManager assetManager = mainActivity.getAssets(); // 父代方法
        // 取得SharedPreferences物件 ("檔名", Context.MODE_PRIVATE)
        SharedPreferences sharedPreferences =
                mainActivity.getSharedPreferences("user_info", mainActivity.MODE_PRIVATE);


        /**
         * 讀入
         */
        buttonLoad.setOnClickListener(view -> {
            Toast.makeText(mainActivity, "SAVE", Toast.LENGTH_SHORT).show();
            /**
             * Asset
             */
            try (
                    // 要讀入的來源 (位元組 - 解讀)
                    InputStream is = assetManager.open("name_data.txt");
                    // 將InputStream 轉 Reader (文字 - 解讀)
                    InputStreamReader isr = new InputStreamReader(is);
                    // 以列為單位讀入 (一列一列 讀入)
                    BufferedReader br = new BufferedReader(isr);
            ) {
                // Java另提供的字串類別，不同的是它的內容是可變的(Mutable)，(String Pool字串池慨念)
                // 補充: StringBuilder適用單執行緒, StringBuffer適用多執行緒
                StringBuffer stringBuffer = new StringBuffer();

                String line;

                // 讀入每列資料
                while ((line = br.readLine()) != null) {
                    // 將指定型態的value轉成字串，並附加至原有的字串之後
                    stringBuffer
                            .append(line)
                            .append("\n");
                }
                textViewLoadName.setText(stringBuffer);
            }catch (Exception e) {
                Log.e(TAG, e.toString());
            }

            /**
             * Shared Preferences
             */
            String shardPreferencesName = sharedPreferences.getString("Name", "");
            shardPreferencesLoadName.setText(shardPreferencesName);

            /**
             * Internal Storage
             */
            try (
                    // 取得FileInputStream物件 (openFileOutput 父代方法)
                    FileInputStream fis = mainActivity.openFileInput("user_info");
                    // FileInputStream 轉 ObjectInputStream (讀寫Java任何型態 -> user是class)
                    ObjectInputStream ois = new ObjectInputStream(fis)
            ) {
                // 轉型
                final User user = (User)ois.readObject();
                internalStorageLoadName.setText(user.getName());

            }catch (Exception e){
                e.printStackTrace();
            }

            /**
             * External Storage
             */
            //檢查外部儲存體狀態
            if (checkExternalStorageState()) {
                // 外部儲存體路徑
                File dir, file;
                // 取得外部儲存體路徑
                dir = mainActivity.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
                file = new File(dir, "user_info.txt");
                try (
                        // 取得FileInputStream物件
                        FileInputStream fis = new FileInputStream(file);
                        // Java I/O相關程式
                        ObjectInputStream ois = new ObjectInputStream(fis);
                ) {
                    final User user = (User)ois.readObject();
                    externalStorageLoadName.setText(user.getName());

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        /**
         * 寫出
         */
        buttonSave.setOnClickListener(view ->{
            Toast.makeText(mainActivity, "LOAD", Toast.LENGTH_SHORT).show();
            /**
             * Shared Preferences
             */
            if (String.valueOf(shardPreferencesSaveName.getText()).isEmpty()) {
                Toast.makeText(mainActivity, "shardPreferencesSaveName isEmpty", Toast.LENGTH_SHORT).show();
            }
            String shardPreferencesName = String.valueOf(shardPreferencesSaveName.getText());
            // 開始寫出
            sharedPreferences.edit()
                    .putString("Name", shardPreferencesName)
                    .apply();

            /**
             * Internal Storage
             */
            if (String.valueOf(internalStorageSaveName.getText()).isEmpty()) {
                Toast.makeText(mainActivity, "internalStorageSaveName isEmpty", Toast.LENGTH_SHORT).show();
            }
            String internalStorageName = String.valueOf(internalStorageSaveName.getText());
            final User user = new User(internalStorageName);
            // 開始寫出
            try (
                    // 取得FileOutputStream物件 (openFileOutput 父代方法)
                    FileOutputStream fos = mainActivity.openFileOutput("user_info", mainActivity.MODE_PRIVATE);
                    // FileOutputStream 轉 ObjectOutputStream (讀寫Java任何型態 -> user是class)
                    ObjectOutputStream oos = new ObjectOutputStream(fos)
            ) {
                oos.writeObject(user);
                //oos.flush();
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }

            /**
             * External Storage
             */
            //檢查外部儲存體狀態
            if (checkExternalStorageState()) {
                // 外部儲存體路徑
                File dir, file;
                // 取得外部儲存體路徑
                dir = mainActivity.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
                file = new File(dir, "user_info.txt");

                if (String.valueOf(externalStorageSaveName.getText()).isEmpty()) {
                    Toast.makeText(mainActivity, "externalStorageSaveName isEmpty", Toast.LENGTH_SHORT).show();
                }
                String externalStoragename = String.valueOf(externalStorageSaveName.getText());
                final User externalUser = new User(externalStoragename);

                try (
                        // 取得FileOutputStream物件
                        FileOutputStream fos = new FileOutputStream(file);
                        // Java I/O相關程式
                        ObjectOutputStream oos = new ObjectOutputStream(fos);
                ) {
                    oos.writeObject(externalUser);
                    //oos.flush();
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }
            }
        });
    }

    /**
     * 檢查外部儲存體狀態
     */
    private boolean checkExternalStorageState() {
        // 取得外部儲存體狀態
        String state = Environment.getExternalStorageState();
        // 確認狀態(是否可讀寫)
        if (!Environment.MEDIA_MOUNTED.equals(state)) {
            Toast.makeText(mainActivity, "外部儲存體非可讀狀態！", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}