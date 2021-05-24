package idv.tfp10101.tfp10101bowen2;

import android.content.Context;
import android.content.pm.PackageManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.HashSet;
import java.util.Set;

/**
 * 權限詢問
 */
public class RepuestPermission extends AppCompatActivity {
    private static final int REQ_PERMISSIONS = 1;
    //
    private MainActivity mainActivity;
    // 所需權限陣列
    private Set<String> permissions;
    // 尚未同意的權限
    private Set<String> waitingforPermission;

    // private - 不可被實例化
    private RepuestPermission() {
        this.permissions = new HashSet<>();
        this.waitingforPermission = new HashSet<>();
        this.mainActivity = null;
    }

    //創建 RepuestPermission 的一個對象
    private static RepuestPermission instance = new RepuestPermission();

    //get唯一可用的對象
    public static synchronized RepuestPermission getInstance(){
        if (instance == null)
        {
            instance = new RepuestPermission();
        }
        return instance;
    }

    public void setMainActivity (MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public void addPermission(String Permission) {
        permissions.add(Permission);
    }
    public void addPermission(String[] arrPermission) {
        for (String permission : arrPermission) {
            permissions.add(permission);
        }
    }

    /**
     * 檢查權限是否還未有授權
     * @return Boolean
     */
    public Boolean checkPermission() {
        // App所需權限陣列 逐一判斷
        for (String permission : permissions) {
            // 抓取檢查狀態int
            int result = ContextCompat.checkSelfPermission(mainActivity, permission);
            // 是否有授權
            if (result != PackageManager.PERMISSION_GRANTED) {
                // 存入尚未同意set 後續再詢問
                waitingforPermission.add(permission);

                return false;
            }
        }
        return  true;
    }

    /**
     * 加入權限並詢問
     */
    public void requestPermission(String[] arrPermission) {
        // 加入
        for (String permission : arrPermission) {
            permissions.add(permission);
        }
        // Activity中,詢問權限 (跳出對話框) -> (Activity activity, String[] permissions, int 自訂詢問代碼)
        ActivityCompat.requestPermissions(
                mainActivity,
                arrPermission,
                REQ_PERMISSIONS
        );
    }

    /**
     * 尚未同意權限 詢問
     */
    public void requestWaitingforPermission() {
        // Activity中,詢問權限 (跳出對話框) -> (Activity activity, String[] permissions, int 自訂詢問代碼)
        ActivityCompat.requestPermissions(
                mainActivity,
                waitingforPermission.toArray(new String[waitingforPermission.size()]),
                REQ_PERMISSIONS
        );
    }

    /**
     * 監聽 詢問結果
     * 覆寫onRequestPermissionsResult()
     * @param requestCode       詢問代碼
     * @param permissions
     * @param grantResults      是否同意授權[]
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQ_PERMISSIONS) {
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
