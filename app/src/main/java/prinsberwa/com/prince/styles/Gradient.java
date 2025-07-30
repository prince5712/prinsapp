package prinsberwa.com.prince.styles;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposeShader;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatImageView;

public class Gradient extends AppCompatImageView {

    private Paint paint;
    private BitmapShader bitmapShader;
    private Bitmap bitmap;
    private Path path;

    public Gradient(Context context) {
        super(context);
        init();
    }

    public Gradient(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (bitmapShader != null) {
            paint.setShader(bitmapShader);
            canvas.drawPath(path, paint);
        } else {
            super.onDraw(canvas);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        updateShader();
    }

    private void updateShader() {
        Drawable drawable = getDrawable();
        if (drawable == null) {
            return;
        }

        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
        }

        bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas tempCanvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, tempCanvas.getWidth(), tempCanvas.getHeight());
        drawable.draw(tempCanvas);

        bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

        Shader gradientShader = new LinearGradient(
                0, 0, getWidth(), getHeight(),
                new int[] {Color.RED, Color.BLUE},
                null,
                Shader.TileMode.CLAMP
        );

        Shader composedShader = new ComposeShader(bitmapShader, gradientShader, PorterDuff.Mode.SRC_OVER);
        paint.setShader(composedShader);

        path = new Path();
        path.addRect(0, 0, getWidth(), getHeight(), Path.Direction.CW);

        invalidate();
    }
}
