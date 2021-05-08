package idv.tfp10101.tfp10101bowen2;

import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class HomepageFragment extends Fragment {

    private MainActivity mainActivity;
    private ImageView imageViewPersonal; // 個人圖像
    private ImageView imageViewHololive;
    private TextView textViewName; // 姓名
    private SearchView searchView; // 標題搜尋
    private ImageView imageViewAnimation; // 動畫使用的圖
    private ProgressBar progressBarPersonal; // 進度條(人物圖像)
    private SeekBar seekBarNameRGB; // Name RGB條

    // 計算型參數
    private enum Personal {WTA_01, WTA_02}; // 人物圖像轉換
    private Personal personal = Personal.WTA_01;

    /**
     * 取得xml元件
     * @param view Activity下的view
     */
    private void findViews(@Nullable View view) {
        imageViewPersonal = view.findViewById(R.id.imageViewPersonal);
        textViewName = view.findViewById(R.id.textViewName);
        imageViewHololive = view.findViewById(R.id.imageViewHololive);
        imageViewAnimation = view.findViewById(R.id.imageViewAnimation);
        progressBarPersonal = view.findViewById(R.id.progressBarPersonal);
        seekBarNameRGB = view.findViewById(R.id.seekBarNameRGB);
    }

    // 生命週期-2 初始化與畫面無直接關係之資料	 (設計: )
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // 生命週期-3 載入並建立Layout  (設計: )
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // 取得Activity參考 (有需要傳入Context時可用)
        mainActivity = (MainActivity) getActivity();
        return inflater.inflate(R.layout.fragment_homepage, container, false);
    }

    // 生命週期-4 Layout已建立後 (設計: 互動的處理)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // set(開啟)功能選單 (必須在onViewCreated方法執行)
        setHasOptionsMenu(true);
        // 取得xml元件
        findViews(view);
        // 按鈕點擊處理
        handleImageViewHololive();
        handleClickHeadShot();
        // 動畫處理
        handleAnimation();
        // Name RGB 處理
        handleSeekBarNameRGB();
    }

    /**
     * 建立功能選單(Fragment的版本)
     */
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        // Inflate 搜索選單的 action bar.
        inflater.inflate(R.menu.options_menu, menu);
        // 取得menu的SearchView元件(之後可以做監聽)
        MenuItem menuItem = menu.findItem(R.id.search);
        searchView = (SearchView) menuItem.getActionView();
        // 提示字
        searchView.setQueryHint("搜尋其它Hololive成員");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // 當點擊提交鍵(虛擬鍵盤)
            @Override
            public boolean onQueryTextSubmit(String query) {
                Bundle bundle = new Bundle();
                bundle.putString("memberName", query); // (key, value)
                // 傳送 (因為searchView跟navigationController 沒關聯到，所以無法使用)
                navigationController(imageViewPersonal, R.id.action_homepageFragment_to_memberSearchFragment, bundle);
                return false;
            }
            // 當查詢文字改變
            @Override
            public boolean onQueryTextChange(String newText) {
                // 假資料
                List<HololiveMember> memberList = HololiveMember.getHololiveMember();

                if (newText.isEmpty()) {
                    return false;
                }

                List<HololiveMember> resultList = new ArrayList<>();
                for (HololiveMember member : memberList) {
                    // 比較雙方都轉成全小寫，contains()判斷是否包含
                    if (member.getName().toLowerCase().contains(newText.toLowerCase())) {
                        // 暫時無實作
                    }
                }
                return true;
            }
        });


    }

    /**
     * 功能選單監聽器
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // 暫無功能
        return true;
    }

    /**
     * NavController切換頁面
     * @param view 當下Fragment內的任一元件
     * @param actionID 跳至頁面ID
     */
    private void navigationController(View view, int actionID) {
        NavController navController = Navigation.findNavController(view);
        navController.navigate(actionID);
    }
    private void navigationController(View view, int actionID, Bundle bundle) {
        NavController navController = Navigation.findNavController(view);
        navController.navigate(actionID, bundle);
    }

    /**
     * 動畫功能 - 處理
     */
    private void handleAnimation() {
        // 宣告一個AnimationDrawable -> 存入數張圖片播放
        AnimationDrawable animationDrawable = new AnimationDrawable();
        // 宣告一個drawable -> 分次抓取圖片存入AnimationDrawable
        Drawable drawable;
        // 圖1~2
        drawable = ContextCompat.getDrawable(mainActivity, R.drawable.wta_ani01);
        animationDrawable.addFrame(drawable, 80); // 毫秒數
        drawable = ContextCompat.getDrawable(mainActivity, R.drawable.wta_ani02);
        animationDrawable.addFrame(drawable, 80); // 毫秒數
        // Set進背景
        imageViewAnimation.setBackground(animationDrawable);
        // 播放動畫
        animationDrawable.start();
        // 監聽(動畫圖) -> 可以點擊轉換開關
        imageViewAnimation.setOnClickListener(view -> {
            // 若動畫播放中
            if (animationDrawable.isRunning()) {
                // 停止播放
                animationDrawable.stop();
                view.clearAnimation();
            } else {
                // 開始播放
                animationDrawable.start();
            }
        });
    }

    /**
     * 點擊大頭貼 - 處理
     */
    private void handleClickHeadShot() {
        // 監聽(人物圖) -> 點擊會跑假Lodin後換圖
        imageViewPersonal.setOnClickListener(view -> {
//            Toast.makeText(mainActivity, "imageViewPersonal被點擊", Toast.LENGTH_SHORT).show();
            // 點擊圖片後，顯示進度Bar
            progressBarPersonal.setVisibility(View.VISIBLE);
            // Handler -> 在子執行緒中，接收主執行序的訊息(Message)做處理
            Handler handler = new Handler();
            new Thread(() -> {
                try {
                    Thread.sleep(2000); // 延遲2秒
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // handler.post -> 子執行緒可以取得呼叫端執行緒的訊息(Message)
                handler.post(() -> {
                    // 隱藏progressBar
                    progressBarPersonal.setVisibility(View.INVISIBLE);
                    // 輪流換圖
                    switch (personal) {
                        case WTA_01:
                            imageViewPersonal.setImageResource(R.drawable.watame002);
                            personal = Personal.WTA_02;
                            break;
                        case WTA_02:
                            imageViewPersonal.setImageResource(R.drawable.watame001);
                            personal = Personal.WTA_01;
                            break;
                        default:

                            break;
                    }
                });
            }).start();
        });
    }

    private void handleSeekBarNameRGB() {
        seekBarNameRGB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int R = (int)(Math.random() * 256);
                int G = (int)(Math.random() * 256);
                int B = (int)(Math.random() * 256);

                textViewName.setTextColor(Color.rgb(R, G, B));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    /**
     * 前往fragment_hololive頁面
     * imageViewHololive -> 點擊觸發
     */
    private void handleImageViewHololive() {
        imageViewHololive.setOnClickListener(view -> {
            navigationController(view, R.id.action_homepageFragment_to_hololiveFragment);
        });
    }
}