package hwdsb.theofficialwestmountapp;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

public class ItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private final ItemTouchHelperAdapter itemTouchHelperAdapter;
    public int dragFlags = 0;

    public ItemTouchHelperCallback(ItemTouchHelperAdapter itemTouchHelperAdapter) {
        this.itemTouchHelperAdapter = itemTouchHelperAdapter;
    }

    // Get if the recycler view items are draggable or able to be slid
    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(dragFlags, ItemTouchHelper.START | ItemTouchHelper.END);
    }

    // Call the interface move function when an item in the recycler view is dragged
    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        itemTouchHelperAdapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    // Call the interface dismiss function when an item in the recycler view is slid
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        itemTouchHelperAdapter.onItemDismiss(viewHolder.getAdapterPosition());
    }

    // Get whether dragging items is enabled
    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    // Get whether swiping items is enabled
    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

}
