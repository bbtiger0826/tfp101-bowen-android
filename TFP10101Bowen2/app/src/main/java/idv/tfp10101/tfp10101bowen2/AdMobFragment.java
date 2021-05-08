package idv.tfp10101.tfp10101bowen2;

import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;

public class AdMobFragment extends Fragment {
    private MainActivity mainActivity;
    private Resources resources;
    // AdView
    private AdView adView;

    /**
     * 取得xml元件
     * @param view Activity下的view
     */
    private void findViews(View view) {
        adView = view.findViewById(R.id.adView); // 取得AdView物件
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

        return inflater.inflate(R.layout.fragment_ad_mob, container, false);
    }

    /**
     * 生命週期-4
     * Layout已建立後 (設計: 互動的處理)
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        findViews(view);

        // 初始化MobileAds
        MobileAds.initialize(mainActivity);

        // 新建AdRequest物件 (準備接收)
        AdRequest adRequest = new AdRequest.Builder().build();

        // 載入廣告 -> view 載入 Request 要顯示的東西
        adView.loadAd(adRequest);

        /**
         * 註冊/實作 廣告監聽器
         */
        adView.setAdListener(new AdListener() {
            // 當使用者點開廣告後
            @Override
            public void onAdClosed() {
                super.onAdClosed();
            }

            // 當廣告載入失敗時
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
            }

            // 當廣告開啟並顯示
            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }

            // 當廣告載入完畢時
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
            }

            // 當廣告被點擊時
            @Override
            public void onAdClicked() {
                super.onAdClicked();
            }
        });
    }

    /**
     * 生命週期-7
     * 畫面即將顯示時 (設計: )
     */
    @Override
    public void onResume() {
        super.onResume();

        adView.resume();
    }

    /**
     * 生命週期-8
     * Activity暫停或Fragment將要分離 (設計: )
     */
    @Override
    public void onPause() {
        super.onPause();

        adView.pause();
    }

    /**
     * 生命週期-11
     * 銷毀時 (設計: )
     */
    @Override
    public void onDestroy() {
        super.onDestroy();

        adView.destroy();
    }
}