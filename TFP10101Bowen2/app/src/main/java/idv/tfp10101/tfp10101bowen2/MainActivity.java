package idv.tfp10101.tfp10101bowen2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    NavigationView navigationView; // 導覽
    NavController navController; // 導覽控制
    DrawerLayout drawerLayout; // 抽屜

    /**
     * 取得xml元件
     */
    private void findViews() {
        // 取得NavigationView參考
        navigationView = findViewById(R.id.navigationView);
        // 取得NavController物件
        navController = Navigation.findNavController(this, R.id.navHost);
        // 取得DrawerLayout物件
        drawerLayout = findViewById(R.id.drawerLayout);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 取得元件
        findViews();
        // 加入 導覽功能 至 抽屜元件
        NavigationUI.setupWithNavController(navigationView, navController);
        // ActionBar() -> 連動返回鍵圖示
        handleActionBar();
    }

    /**
     * ActionBar
     */
    private void handleActionBar() {
        ActionBar actionBar = getSupportActionBar();
        // true 設定顯示返回鍵
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        /**
         * 功能選單 -> 抽屜 開/關, 監聽
         */
        // 實例化抽屜 開/關 監聽器 (ActionBar的抽屜icon點選後 -> 串連到drawerLayout)
        // (Activity, DrawerLayout, "開啟代表字串", "關閉代表字串") (後2參數是殘障輔主相關設定)
        ActionBarDrawerToggle actionBarDrawerToggle =
                new ActionBarDrawerToggle(this, drawerLayout, R.string.textOpen, R.string.textClose);
        // drawerLayout加入抽屜監聽
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        // 設定同步 -> 手動拉NavigationView時，actionBar會同步
        actionBarDrawerToggle.syncState();

        //
    }

    /**
     * 抽屜 開/關 -> 功能選單, 監聽 (覆寫)
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // return super.onOptionsItemSelected(item);

        final int itemId = item.getItemId();
        // home鍵
        if (itemId == android.R.id.home) {
            // if drawerLayout START方向開啟時
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            }else {
                drawerLayout.openDrawer(GravityCompat.START);
            }
            return true;
        }
        else {
            return super.onOptionsItemSelected(item);
        }
    }
}