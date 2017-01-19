package hwdsb.theofficialwestmountapp;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by danielsong on 2017-01-18.
 */

public class FeedItemDecoration extends RecyclerView.ItemDecoration {

    int space;

    public FeedItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.left = space;
        outRect.bottom = space;
        outRect.right = space;
        if (parent.getChildLayoutPosition(view) == 0) {
            outRect.top = space;
        }
    }

}
