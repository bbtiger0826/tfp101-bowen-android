package idv.tfp10101.tfp10101bowen2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.util.List;

/**
 * step.0
 * Member專用Adapter<ViewHolder>
 * 步驟2完成後在指定泛型型態
 * 特殊功能：
 * 實作ItemTouchHelperListener介面 -> 可拖滑選項 (Drag & Swipe)
 */
public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.ViewHolder> implements ItemTouchHelperListener{
    // 主要Layout端的view，用來在Adapter與View
    private Context context;

    // 從主要Layout端傳進來的資料存入ViewHolder裡
    // (因為SerachView功能需要修改List的值，目前先改成public)
    public List<HololiveMember> list;

    /**
     * step.1
     * 建構子
     */
    public MemberAdapter(Context context, List<HololiveMember> list) {
        this.context = context;
        this.list = list;
    }

    /**
     * step.3
     * 主要Layout端的view & Adapter & ViewHolder 三者串起來，成為一個完整的RecyclerView架構
     * 要注意的一點是，ViewHolder的這一端的view是Layout層另一個xml檔，所以需要LayoutInflater的幫助
     *
     * @param parent 主要Layout端
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflate另一個xml檔的view
        View cardView = LayoutInflater.from(context).inflate(R.layout.card_view_member, parent, false);

        // 這裡會return後，會給onBindViewHolder()，一直來回直到每筆資料都存入到各個cardView裡
        // 回傳ViewHolder型態，直接new一個後回傳
        return new ViewHolder(cardView);
    }

    /**
     * step.4
     * 把創建Adapter constructor時存入的資料(一個or多個)Bind到ViewHolder裡
     *
     * @param holder -> onCreateViewHolder()時創立好的一個ViewHolder
     * @param position -> 因為有可能多個資料(Adapter的List資料)，指當下Bind時是第幾筆資料
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // 抓取一筆筆HololiveMember資料
        HololiveMember hololiveMember = list.get(position);
        // 存入到新創建好的ViewHolder裡
        holder.imageView.setImageResource(hololiveMember.getImageID());
        holder.textView.setText(hololiveMember.getName());
    }

    /**
     * step.5
     * 取得這個Adapter存入了幾筆資料
     */
    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    /**
     * 實作在資料物件(List)中換位置
     * 當選項移動時，自動被呼叫
     * @param fromPosition 移動開始的索引
     * @param toPosition 移動結束的索引
     */
    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        // 將選項從原本的位置移除
        final HololiveMember hololiveMember = list.remove(fromPosition);
        // 將選項加入至新位置
        list.add(toPosition, hololiveMember);
        // 通知選項已被移動，以改變RecyclerView
        notifyItemMoved(fromPosition, toPosition);
    }

    /**
     * step.2
     * Adapter的另一端的view(一個or多個)，需要ViewHolder的幫助來裝載，所以需要實作一個ViewHolder
     * ViewHolder -> 用來把另一端的view打包起來(也許會有多個)
     * Adapter＆ViewHolder成對出現，所以用inner class實作
     * (另一端的view用cradView實作)
     */
    static class ViewHolder extends RecyclerView.ViewHolder {
        // 一個view組成所需要的元件(元件用來去接javaBean的個參數)(需用public)
        public TextView textView;
        public ImageView imageView;

        // constructor
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // 抓取cardView元件
            textView = itemView.findViewById(R.id.textViewMembreName);
            imageView = itemView.findViewById(R.id.imageViewMember);
        }
    }
}
