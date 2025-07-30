package prinsberwa.com.prince.styles;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

/* compiled from: textview */
public class Blinking extends androidx.appcompat.widget.AppCompatTextView {
    public Blinking(Context context) {
        super(context);
        setTypeface(Typeface.createFromAsset(context.getAssets(), "prince.ttf"));
        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(300);
        anim.setStartOffset(500);
        anim.setRepeatMode(2);
        anim.setRepeatCount(-1);
        startAnimation(anim);
    }

    public Blinking(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTypeface(Typeface.createFromAsset(context.getAssets(), "prince.ttf"));
        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(300);
        anim.setStartOffset(500);
        anim.setRepeatMode(2);
        anim.setRepeatCount(-1);
        startAnimation(anim);
    }

    public Blinking(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setTypeface(Typeface.createFromAsset(context.getAssets(), "prince.ttf"));
        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(300);
        anim.setStartOffset(500);
        anim.setRepeatMode(2);
        anim.setRepeatCount(-1);
        startAnimation(anim);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}