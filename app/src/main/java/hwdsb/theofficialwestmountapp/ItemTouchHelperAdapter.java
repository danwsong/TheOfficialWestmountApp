package hwdsb.theofficialwestmountapp;

// Interface for recycler view item sliding and dragging
public interface ItemTouchHelperAdapter {

    void onItemMove(int fromPosition, int toPosition);

    void onItemDismiss(int position);

}
