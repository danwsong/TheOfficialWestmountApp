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
        outRect.top = space / 4;
        outRect.bottom = space / 4;
        outRect.right = space;
    }

}
