package idv.tfp10101.tfp10101bowen2;

/**
 * RecyclerView特殊功能 - 可拖滑選項
 * 定義ItemTouchHelperListener介面
 */
public interface ItemTouchHelperListener {
    /**
     *
     * @param fromPosition 移動開始的索引
     * @param toPosition 移動結束的索引
     */
    void onItemMove(int fromPosition, int toPosition);
}
