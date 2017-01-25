package hwdsb.theofficialwestmountapp;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class NoSwipeViewPager extends ViewPager {

    public NoSwipeViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    // Do not allow touch events on tabbed view
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }

    // Do not allow touch events on tabbed view
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }

}
