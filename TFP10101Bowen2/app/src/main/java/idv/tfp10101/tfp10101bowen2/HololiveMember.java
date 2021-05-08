package idv.tfp10101.tfp10101bowen2;

import java.util.ArrayList;
import java.util.List;

public class HololiveMember {
    private String name;
    private Integer imageID;

    public HololiveMember() {
        this("null", null);
    }

    public HololiveMember(String name, Integer imageID) {
        this.name = name;
        this.imageID = imageID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getImageID() {
        return imageID;
    }

    public void setImageID(Integer imageID) {
        this.imageID = imageID;
    }

    // 假資料
    static List<HololiveMember> getHololiveMember() {
        List<HololiveMember> list = new ArrayList<>();
        list.add(new HololiveMember("夏色祭", R.drawable.hololive1_1));
        list.add(new HololiveMember("赤井心", R.drawable.hololive1_2));
        list.add(new HololiveMember("白上吹雪", R.drawable.hololive1_3));

        list.add(new HololiveMember("湊阿库娅", R.drawable.hololive2_1));
        list.add(new HololiveMember("百鬼綾目", R.drawable.hololive2_2));
        list.add(new HololiveMember("大空昴", R.drawable.hololive2_3));

        list.add(new HololiveMember("兔田佩克拉", R.drawable.hololive3_1));
        list.add(new HololiveMember("不知火芙蕾雅", R.drawable.hololive3_2));
        list.add(new HololiveMember("白銀諾艾爾", R.drawable.hololive3_3));

        list.add(new HololiveMember("天音彼方", R.drawable.hololive4_1));
        list.add(new HololiveMember("桐生可可", R.drawable.hololive4_2));
        list.add(new HololiveMember("角卷綿芽", R.drawable.hololive4_3));
        list.add(new HololiveMember("常闇永遠", R.drawable.hololive4_4));
        list.add(new HololiveMember("姬森璐娜", R.drawable.hololive4_5));

        list.add(new HololiveMember("獅白牡丹", R.drawable.hololive5_1));
        list.add(new HololiveMember("雪花菈米", R.drawable.hololive5_2));
        list.add(new HololiveMember("尾丸波爾卡", R.drawable.hololive5_3));
        list.add(new HololiveMember("桃鈴音音", R.drawable.hololive5_4));
        return list;
    }
}


