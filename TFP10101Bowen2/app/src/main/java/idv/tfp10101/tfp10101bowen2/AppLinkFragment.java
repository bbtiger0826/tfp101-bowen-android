package idv.tfp10101.tfp10101bowen2;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class AppLinkFragment extends Fragment {
    private static final int REQUEST_CODE_CONTACTS = 1;
    private MainActivity mainActivity;
    private Resources resources;
    private EditText editTextName, editTextNumber, editTextType;
    private Button buttonPhoneBook, buttonCall;

    // new Intent物件 Intent.ACTION_PICK 對應 androidManifest的設定
    // (Intent.ACTION_動作, Uri.parse("URI字串"));
    Intent intent_actionPick = new Intent(Intent.ACTION_PICK);

    /**
     * 取得xml元件
     * @param view Activity下的view
     */
    private void findViews(View view) {
        editTextName = view.findViewById(R.id.editTextName);
        editTextNumber = view.findViewById(R.id.editTextNumber);
        editTextType = view.findViewById(R.id.editTextType);
        buttonPhoneBook = view.findViewById(R.id.buttonPhoneBook);
        buttonCall = view.findViewById(R.id.buttonCall);
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

        return inflater.inflate(R.layout.fragment_app_link, container, false);
    }

    /**
     * 生命週期-4
     * Layout已建立後 (設計: 互動的處理)
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        findViews(view);

        // 電話簿 - 處理
        handlePhoneBook();
        // 撥打 - 處理
        handleCall();
    }

    /**
     * 連到電話簿的功能
     */
    private void handlePhoneBook() {
        buttonPhoneBook.setOnClickListener(view -> {
            // 檢查 Intent 是否在manifest有設定
            if (isIntentAvailable(intent_actionPick)) {
                // 設定Content Type
                intent_actionPick.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                /**
                 * 跳轉: 有2種方法 startActivity() & startActivityForResult()
                 * startActivityForResult() 才可以接收回傳的資料 (intent, 自訂跳轉代碼)
                 * 需要覆寫 onActivityResult() 監聽
                 */
                startActivityForResult(intent_actionPick, REQUEST_CODE_CONTACTS);
            }
        });
    }

    /**
     * 檢查是否有符合意圖的App(Activity)
     *
     * @param intent 檢查目標intent
     * @return
     */
    private boolean isIntentAvailable(Intent intent) {
        // PackageManager屬於Context的子代
        PackageManager packageManager = mainActivity.getPackageManager();
        return intent.resolveActivity(packageManager) != null;
    }

    /**
     * 監聽 取得回應資料
     *
     * @param requestCode   跳轉代碼(抓取當初定義的代碼來片段回傳的對應Result)
     * @param resultCode
     * @param intent        intent所有資料(包含在另一端使用者所操作的Data)
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        // Override一定要加上
        super.onActivityResult(requestCode, resultCode, intent);
        // if (requestCode == 步驟3自訂的跳轉代碼)
        if (requestCode == REQUEST_CODE_CONTACTS) {
            /**
             * 解析Uri物件中的資料
             * 用法與JDBC程式概念相同
             *  設定欲查詢的欄位(Columns)，查詢後會取得結果集物件(Cursor)
             *  再進而將結果集物件中的各欄位取出
             */
            // 取出Uri物件
            Uri uri = intent.getData();
            // 宣告欲查詢的欄位
            final String[] columns = {
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                    ContactsContract.CommonDataKinds.Phone.NUMBER,
                    ContactsContract.CommonDataKinds.Phone.TYPE
            };
            // 查詢
            try (Cursor cursor = mainActivity.getContentResolver().query(uri, columns, null, null, null)) {
                // 如果有查詢到資料
                if (cursor.moveToNext()) {
                    // 取出資料
                    final String name = cursor.getString(0);
                    final String number = cursor.getString(1);
                    final int type = cursor.getInt(2);
                    // 將電話種類代碼(type) 解析成 電話種類文字
                    final String typeString = getString(
                            ContactsContract.CommonDataKinds.Phone.getTypeLabelResource(type)
                    );
                    // 存入
                    editTextName.setText(name);
                    editTextNumber.setText(number);
                    editTextType.setText(typeString);
                }
            }
        }
    }

    /**
     * 撥打電話號碼功能
     */
    private void handleCall() {
        buttonCall.setOnClickListener(view -> {
            String number = String.valueOf(editTextNumber.getText());
            // new Intent物件 Intent.ACTION_VIEW 對應 androidManifest的設定
            // (Intent.ACTION_動作, Uri.parse("URI字串"));
            Intent intent_actionView = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:" + number));
            // 檢查是否有符合意圖的App(Activity)
            if (isIntentAvailable(intent_actionView)) {
                // 跳轉 (不需接收回應)
                startActivity(intent_actionView);
            }
        });
    }
}