package prinsberwa.com.prince.styles;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatTextView;

public class Rainbow extends AppCompatTextView {

    private Paint paint;
    private LinearGradient gradient;
    private ObjectAnimator animator;

    public Rainbow(Context context) {
        super(context);
        init();
    }

    public Rainbow(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Rainbow(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        paint = getPaint();
        // Create a LinearGradient for rainbow effect
        gradient = new LinearGradient(0, 0, getWidth(), 0,
                new int[]{0xFFFF0000, 0xFFFF8000, 0xFFFFFF00, 0xFF00FF00, 0xFF0000FF, 0xFF8B00FF},
                null, Shader.TileMode.MIRROR);
        // Apply the shader to the TextView's paint
        paint.setShader(gradient);
        // Start animation
        startAnimation();
    }

    private void startAnimation() {
        animator = ObjectAnimator.ofFloat(this, "gradientX", 0, getWidth());
        animator.setDuration(5000); // Duration in milliseconds
        animator.setRepeatCount(ObjectAnimator.INFINITE); // Repeat indefinitely
        animator.setRepeatMode(ObjectAnimator.REVERSE); // Reverse animation when it reaches the end
        animator.start();
    }

    public void setGradientX(float x) {
        // Update the gradient's position
        gradient = new LinearGradient(x, 0, x + getWidth(), 0,
                new int[]{0xFFFF0000, 0xFFFF8000, 0xFFFFFF00, 0xFF00FF00, 0xFF0000FF, 0xFF8B00FF},
                null, Shader.TileMode.MIRROR);
        paint.setShader(gradient);
        // Redraw the view
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
