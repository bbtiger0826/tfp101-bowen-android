package idv.tfp10101.tfp10101bowen2;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

/**
 * DIY畫一個讚
 */
public class GoodIconView extends View {
    private Resources resources;
    private Paint paint;

    public GoodIconView(Context context) {
        super(context);

        resources = getResources();
        paint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        paint.setColor(resources.getColor(R.color.colorYellow));

        canvas.drawRect(0, 0, 10, 20, paint) ;
        canvas.drawRect(0, 20, 50, 50, paint) ;
    }
}
