package idv.tfp10101.tfp10101bowen2;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class DialogFragment extends Fragment implements DialogInterface.OnClickListener{
    private MainActivity mainActivity;
    private Resources resources;
    private TextView textViewDate, textViewTime;
    private Button buttonDate, buttonTime, buttonDelete;

    /**
     * 取得xml元件
     * @param view Activity下的view
     */
    private void findViews(View view) {
        textViewDate = view.findViewById(R.id.textViewDate);
        textViewTime = view.findViewById(R.id.textViewTime);
        buttonDate = view.findViewById(R.id.buttonDate);
        buttonTime = view.findViewById(R.id.buttonTime);
        buttonDelete = view.findViewById(R.id.buttonDelete);
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

        return inflater.inflate(R.layout.fragment_dialog, container, false);
    }

    /**
     * 生命週期-4
     * Layout已建立後 (設計: 互動的處理)
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        findViews(view);

        // 日期 - 處理
        handleDate();
        // 時間 - 處理
        handleTime();
        // 清空 - 處理
        handleDelete();
    }

    /**
     * DateDialog功能
     */
    private void handleDate() {
        buttonDate.setOnClickListener(view -> {
            // 取得Calendar物件(Calendar不能被New只有唯一一個，屬於 單例模式 設計)
            Calendar calendar = Calendar.getInstance();

            // 實例化DatePickerDialog物件
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    mainActivity,
                    getDateSetListener(), // onDateSet()
                    calendar.get(Calendar.YEAR), // 預選年
                    calendar.get(Calendar.MONTH), // 預選月
                    calendar.get(Calendar.DAY_OF_MONTH) // 預選日
            );

            // 設定可選取日期區間
            // 1. 取得DatePicker物件
            DatePicker datePicker = datePickerDialog.getDatePicker();
            // 2. 設定可選取的最小日期 (當月)
            datePicker.setMinDate(calendar.getTimeInMillis());
            // 3. 設定可選取的最大日期 (後2個月)
            calendar.add(Calendar.MONTH, 2);
            datePicker.setMaxDate(calendar.getTimeInMillis());

            // 顯示對話框
            datePickerDialog.show();
        });
    }

    /**
     * DatePickerDialog的監聽器 (在Fragment要自己實作)
     * 實作 DatePickerDialog.OnDateSetListener
     * 覆寫 onDateSet()
     * 當日期被選取時，自動被呼叫
     */
    private DatePickerDialog.OnDateSetListener getDateSetListener() {
        return new DatePickerDialog.OnDateSetListener() {
            /**
             *
             * @param view       DatePicker物件
             * @param year       選取的年
             * @param month      選取的月
             * @param dayOfMonth 選取的日
             */
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                // 注意: month是0~11
                final String text = "" + year + "/" + (month + 1) + "/" + dayOfMonth;
                textViewDate.setText(text);
            }
        };
    }

    /**
     * TimeDialog功能
     */
    private void handleTime() {
        buttonTime.setOnClickListener(view -> {
            // 取得Calendar物件(Calendar不能被New只有唯一一個，屬於 單例模式 設計)
            Calendar calendar = Calendar.getInstance();
            // 實例化TimePickerDialog物件
            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    mainActivity,
                    getTimeSetListener(), // onTimeSet()
                    calendar.get(Calendar.HOUR_OF_DAY), // 預選小時
                    calendar.get(Calendar.MINUTE), // 預選分鐘
                    true // 是否使用24小時制
            );
            // 顯示對話框
            timePickerDialog.show();
        });
    }

    /**
     * TimePickerDialog的監聽器 (在Fragment要自己實作)
     * 實作 TimePickerDialog.OnTimeSetListener
     * 覆寫 onTimeSet()
     * 當日期被選取時，自動被呼叫
     */
    private TimePickerDialog.OnTimeSetListener getTimeSetListener() {
        return new TimePickerDialog.OnTimeSetListener() {
            /**
             *
             * @param view      TimePicker物件
             * @param hourOfDay 選取的小時
             * @param minute    選取的分鐘
             */
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                final String text = "" + hourOfDay + ":" + minute;
                textViewTime.setText(text);
            }
        };
    }

    /**
     * Dialog輸入的訊息清空功能
     * 注意：這邊使用 非靜態方法參考 -> (參考同類別內，或繼承來的方法)
     * 要implements DialogInterface.OnClickListener
     * (this::方法名)
     */
    private void handleDelete() {
        buttonDelete.setOnClickListener(view -> {
            new AlertDialog.Builder(mainActivity)                               // 實例化AlertDialog.Builder物件
                    .setTitle(R.string.textDeleteTitle)                         // 設定標題文字
                    .setIcon(android.R.drawable.ic_dialog_alert)                // 設定標題圖示
                    .setMessage(R.string.textDeleteMessage)                     // 設定訊息文字
                    /**
                     * (int textId, DialogInterface.OnClickListener listener)
                     * 因為有繼承介面了，所以可以直接this(要實作onClick())
                     */
                    .setPositiveButton(R.string.textYes, this)          // 設定確定按鈕-顯示文字及監聽器
                    .setNegativeButton(R.string.textNo, this)           // 設定否定按鈕-顯示文字及監聽器
                    .setNeutralButton(R.string.textExit, this)          // 設定不決定按鈕-顯示文字及監聽器
                    .setCancelable(false)                               // 設定是否可點擊對話框以外之處離開對話框
                    .show();                                            // 顯示對話框
        });
    }

    /**
     * AlertDialog的Button點擊監聽器
     * 當按鈕(確定、否定、不決定)被點擊時，自動被呼叫
     *
     * @param dialog AlertDialog物件
     * @param which 對話框按鈕編號
     */
    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            // 確定按鈕
            case DialogInterface.BUTTON_POSITIVE:
                Toast.makeText(mainActivity, "Submit successfully！", Toast.LENGTH_SHORT).show();
                textViewDate.setText("");
                textViewTime.setText("");
                break;
            // 否定/不決定 按鈕
            case DialogInterface.BUTTON_NEGATIVE:
            case DialogInterface.BUTTON_NEUTRAL:
                dialog.cancel();
                break;
        }
    }
}