package idv.tfp10101.tfp10101bowen2;

import android.content.Context;
import android.content.res.Resources;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import idv.tfp10101.tfp10101bowen2.MemberAdapter;

/**
 * 3. 自定義ItemTouchHelper.Callback的子類別
 */
public class MemberItemTouchHelper extends ItemTouchHelper.Callback {
    // 此欄位只是為了取得顏色資源，非必要
    private Resources resources;
    // 3.1 欄位: 自定義的Adapter物件
    private MemberAdapter adapter;

    /**
     * 3.2 建構子: 1個參數(自定義的Adapter型態)，用來初始化欄位
     * @param context 只是為了取得Resources物件，非必要
     * @param adapter 自訂的Adapter物件
     */
    public MemberItemTouchHelper(Context context, MemberAdapter adapter) {
        this.resources = context.getResources();
        this.adapter = adapter;
    }

    /**
     * 3.3.1 回傳支援移動方向旗標
     * @param recyclerView
     * @param viewHolder
     * @return 支援移動方向旗標
     */
    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        // 支援上下拖移
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        // 不支援左右滑動
        int swipeFlags = 0;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    /**
     * 3.3.2 當選項被選取(長按)時，自動被呼叫
     * @param viewHolder
     * @param actionState
     */
    @Override
    public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
        // 如果不是閒置狀態
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            // 設定CardView的背景色為桃紅色
            CardView cardView = (CardView) viewHolder.itemView;
            final int color = resources.getColor(R.color.teal_200);
            cardView.setCardBackgroundColor(color);
        }
        super.onSelectedChanged(viewHolder, actionState);
    }

    /**
     * 3.3.3 當移動時，自動被呼叫
     * @param recyclerView
     * @param viewHolder
     * @param target
     * @return 交換位置否
     */
    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        adapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    /**
     * 3.3.4 結束動作後的處理
     * @param recyclerView
     * @param viewHolder
     */
    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        // 設定CardView的背景色為黑色(原本的顏色)
        final int color = resources.getColor(R.color.main_background);
        CardView cardView = (CardView) viewHolder.itemView;
        cardView.setCardBackgroundColor(color);
        super.clearView(recyclerView, viewHolder);
    }

    // 3.3.5 方法內直接留空即可
    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
    }
}