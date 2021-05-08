package idv.tfp10101.tfp10101bowen2;

import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.List;

public class MessageBoardFragment extends Fragment {
    private MainActivity mainActivity;
    private Resources resources;
    private ConstraintLayout rootView;
    private EditText editTextMessage;
    private ScrollView scrollViewMessage;
    private LinearLayout linearLayoutMessage;
    private ImageButton imageButtonSubmit;

    // 計算型參數

    /**
     * 取得xml元件
     * @param view Activity下的view
     */
    private void findViews(View view) {
        rootView = view.findViewById(R.id.rootView);
        editTextMessage = view.findViewById(R.id.editTextMessage);
        scrollViewMessage = view.findViewById(R.id.scrollViewMessage);
        linearLayoutMessage = view.findViewById(R.id.linearLayoutMessage);
        imageButtonSubmit = view.findViewById(R.id.imageButtonSubmit);
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

        return inflater.inflate(R.layout.fragment_message_board, container, false);
    }

    /**
     * 生命週期-4
     * Layout已建立後 (設計: 互動的處理)
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        findViews(view);
        //訊息 - 處理
        handleMessage();
    }

    /**
     * Message相關功能
     */
    private void handleMessage() {
        // 監聽輸入框
        imageButtonSubmit.setOnClickListener(view -> {
            // 抓取message不可為空，不可為空
            final String message = String.valueOf(editTextMessage.getText());
            if (message.isEmpty()) {
                // 顯示紅色驚嘆號的錯誤
                editTextMessage.setError(resources.getString(R.string.textEmpty));
                return;
            }

            // 顯示結果在LinearLayout -> 新增一筆TextView(需自定義)
            linearLayoutMessage.addView(getTextView(message));

            /**
             * 捲動至最後新增的TextView處
             * 注意:
             * fullScroll方法在頁面還沒有顯示出來的時候是不能直接用，因為Android很多函數都是基於消息隊列來同步
             * 在addView完之後，不等於馬上就會顯示，而是在隊列中等待處理，這個時候調用會報錯
             * 通過handler在新線程中更新: handler.post(new Runnable(){});
             */
            scrollViewMessage.post(() -> {
                scrollViewMessage.fullScroll(View.FOCUS_DOWN);
            });
        });
    }

    /**
     * New一個TextView，類似xml檔的屬性描述
     * 賦予一個長按監聽事件
     */
    private TextView getTextView(final String text) {
        TextView textView = new TextView(mainActivity);

        // 宣告LayoutParams(父layout)參數設定
        // (長, 寬, 大小, 文字)
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        textView.setLayoutParams(layoutParams);
        textView.setTextSize(24);
        textView.setText(text);

        // 監聽長按功能，長按後會show menu
        textView.setOnLongClickListener(view -> {
            // 實例化PopupMenu，指定錨點元件
            PopupMenu popupMenu = new PopupMenu(mainActivity, view);
            // 使用inflate載入xml檔
            popupMenu.inflate(R.menu.message_popup_menu);

            // 監聽message_popup_menu的item被點擊
            popupMenu.setOnMenuItemClickListener(item -> {
                int itemID = item.getItemId();
                float x, y;
                //
                if (itemID == R.id.itemGood) {
                    // good圖顯示在message右方
                    x = textView.getRight();
                    y = textView.getTop();

                    // 實例化good元件
                    GoodIconView goodIconView = new GoodIconView(mainActivity);
                    goodIconView.setX(x);
                    goodIconView.setY(y);
                    // add
                    rootView.addView(goodIconView);
                }
                return true;
            });

            // 顯示彈出選單
            popupMenu.show();
            return  true;
        });

        return textView;
    }
}