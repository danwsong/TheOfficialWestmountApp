package hwdsb.theofficialwestmountapp;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class FeedItemDecoration extends RecyclerView.ItemDecoration {

    int space;

    public FeedItemDecoration(int space) {
        this.space = space;
    }

    // Set padding for card views in the recycler view
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.left = space;
        outRect.top = space / 2;
        outRect.bottom = space / 2;
        outRect.right = space;
    }

}
