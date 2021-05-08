package idv.tfp10101.tfp10101bowen2;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class TwitterFragment extends Fragment {
    private MainActivity mainActivity;
    private WebView webView; // 網頁頁面

    /**
     * 取得xml元件
     * @param view Activity下的view
     */
    private void findViews(@Nullable View view) {
        webView = view.findViewById(R.id.webView);
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

        return inflater.inflate(R.layout.fragment_twitter, container, false);
    }

    // 生命週期-4 Layout已建立後 (設計: 互動的處理)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 取得xml元件
        findViews(view);
        // webView連結網址處理
        handleWebView();
    }

    /**
     * 網頁的處理
     * 注意：記得在manifest.xml加入網路權限，如果要連到非加密的網址也要加入相關設定
     */
    private void handleWebView() {
        // 啟用JavaScrip
        webView.getSettings().setJavaScriptEnabled(true);
        // watameTwitterURL
        webView.loadUrl("https://twitter.com/tsunomakiwatame");
        // 預設的網頁會彈出選擇瀏覽器，如果想使網頁顯示在自己的WebView上，則加上
        webView.setWebViewClient(new WebViewClient());
    }
}