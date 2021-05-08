package idv.tfp10101.tfp10101bowen2;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.List;

/**
 * 實作RecyclerView
 */
public class MemberSearchFragment extends Fragment {
    private MainActivity mainActivity;
    private RecyclerView recyclerView;
    private MemberAdapter memberAdapter;
    private SearchView searchViewMember;

    /**
     * 取得xml元件
     * @param view Activity下的view
     */
    private void findViews(View view) {
        recyclerView = view.findViewById(R.id.recyclerViewMember);
        searchViewMember = view.findViewById(R.id.searchViewMember);
    }

    /**
     * 生命週期-2
     * 初始化與畫面無直接關係之資料 (設計: )
     * @param savedInstanceState
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

        return inflater.inflate(R.layout.fragment_member_search, container, false);
    }

    /**
     * 生命週期-4
     * Layout已建立後 (設計: 互動的處理)
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        findViews(view);
//        // test
//        Bundle bundle = getArguments(); // 取得Bundle物件
//        if (bundle != null) {
//            textView.setText(bundle.getString("memberName"));
//        }

        // RecyclerView的基本 - 處理
        handleRecyclerViewBasic();

        // RecyclerView的拖滑功能 - 處理
        handleRecyclerViewDragAndSwipe();

        // RecyclerView的search功能 - 處理
        handleRecyclerViewSearch();

    }

    /**
     * RecyclerView的基本
     */
    private void handleRecyclerViewBasic() {
        // 要一個Adapter來連結另一個or多個view，所以要先建立一個客製化的Adapter
        // (可以處理HololiveMember.class相關Data, RecyclerView用的Adapter)
        memberAdapter = new MemberAdapter(mainActivity, HololiveMember.getHololiveMember());
        recyclerView.setAdapter(memberAdapter);

        // 檢查首頁是否有Bundle
        checkBundle();


        // 使用setLayoutManager 設定要怎麼呈現
        // (端執行後，才會開始跑onCreateViewHolder() & onBindViewHolder())
        recyclerView.setLayoutManager(new LinearLayoutManager(mainActivity));
    }

    /**
     * RecyclerView的拖滑功能
     */
    private void handleRecyclerViewDragAndSwipe() {
        // 可拖滑功能 -> 實例化自定義的ItemTouchHelper.Callback物件
        MemberItemTouchHelper memberItemTouchHelper = new MemberItemTouchHelper(mainActivity, memberAdapter);
        // 可拖滑功能 -> 實例化ItemTouchHelper物件
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(memberItemTouchHelper);
        // 可拖滑功能 -> 附加至RecyclerView
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    /**
     * RecyclerView的search功能
     */
    private void handleRecyclerViewSearch() {
        // 監聽輸入文字
        searchViewMember.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // 當點擊提交鍵(虛擬鍵盤)時，自動被呼叫
            @Override
            public boolean onQueryTextSubmit(String query) {
                // 無功能
                return false;
            }

            // 當查詢文字改變時，自動被呼叫
            @Override
            public boolean onQueryTextChange(String newText) {
                // 重新製作Adapter，存入搜尋後應該顯示的資料
                // 這裡使用call by reference特性，把原本的MemberAdapter修改(向下轉型成自定義Adapter)
                MemberAdapter memberAdapter = (MemberAdapter) recyclerView.getAdapter();
                // 防止null
                if (memberAdapter == null) {
                    return false;
                }

                // 如果沒有輸入值就還原，否則就更新資料
                if (newText.isEmpty()) {
                    memberAdapter.list = HololiveMember.getHololiveMember();
                }else {
                    List<HololiveMember> resultList = new ArrayList<>();
                    for (HololiveMember hololiveMember : memberAdapter.list) {
                        // 兩邊都強制轉成小寫，使用contains()判斷是否有包含的文字
                        if (hololiveMember.getName().toLowerCase().contains(newText.toLowerCase())) {
                            resultList.add(hololiveMember);
                        }
                    }
                    memberAdapter.list = resultList;
                }
                // 通知Data已更改
                memberAdapter.notifyDataSetChanged();
                return true;
            }
        });
    }

    /**
     * 資料傳遞 功能 (首頁搜尋)
     */
    private void checkBundle() {
        // 取得Bundle物件
        Bundle bundle = getArguments();

        if (bundle != null) {
            // 取出資料
            String memberName = bundle.getString("memberName");

            // 重新製作Adapter，存入搜尋後應該顯示的資料
            // 這裡使用call by reference特性，把原本的MemberAdapter修改(向下轉型成自定義Adapter)
            MemberAdapter memberAdapter = (MemberAdapter) recyclerView.getAdapter();
            // 防止null
            if (memberAdapter == null) {
                return;
            }

            // 如果沒有輸入值就還原，否則就更新資料
            if (memberName.isEmpty()) {
                memberAdapter.list = HololiveMember.getHololiveMember();
            }else {
                List<HololiveMember> resultList = new ArrayList<>();
                for (HololiveMember hololiveMember : memberAdapter.list) {
                    // 兩邊都強制轉成小寫，使用contains()判斷是否有包含的文字
                    if (hololiveMember.getName().toLowerCase().contains(memberName.toLowerCase())) {
                        resultList.add(hololiveMember);
                    }
                }
                memberAdapter.list = resultList;
            }
            // 通知Data已更改
            memberAdapter.notifyDataSetChanged();
        }
    }
}