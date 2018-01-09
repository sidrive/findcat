package id.findcat.app.view.camera;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by rioswarawan on 6/6/17.
 */

public class DrawingView extends View {
    private boolean haveTouch = false;
    private Rect touchArea;
    private Paint paint;

    public DrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setColor(0xeed7d7d7);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);
        haveTouch = false;
    }

    public void setHaveTouch(boolean val, Rect rect) {
        haveTouch = val;
        touchArea = rect;
    }

    @Override
    public void onDraw(Canvas canvas) {
//        if (haveTouch) {
        //drawingPaint.setColor(Color.BLUE);
//            canvas.drawRect(
//                    touchArea.left, touchArea.top, touchArea.right, touchArea.bottom,
//                    paint);
//        }
    }
}
