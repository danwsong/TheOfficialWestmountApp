package hwdsb.theofficialwestmountapp;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;

/**
 * Created by danielsong on 2017-01-19.
 */

public class TimerAdapter extends RecyclerView.Adapter<TimerAdapter.TimerViewHolder> implements ItemTouchHelperAdapter {

    Context context;

    public TimerAdapter(Context context) {
        this.context = context;
    }

    @Override
    public TimerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TimerAdapter.TimerViewHolder(LayoutInflater.from(context).inflate(R.layout.activity_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(final TimerViewHolder holder, int position) {
        final TimerFragment.Activity activity = TimerFragment.activities.get(position);
        holder.textView.setText(activity.activityName + ": ");
        holder.numberView.setText(Integer.toString(activity.timeSpent) + " minute" + (activity.timeSpent == 1 ? "" : "s"));
        holder.cardView.setVisibility(View.INVISIBLE);
        AlphaAnimation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(1000);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                holder.cardView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        holder.cardView.startAnimation(animation);
    }

    @Override
    public int getItemCount() {
        return TimerFragment.activities.size();
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        TimerFragment.Activity activity = TimerFragment.activities.remove(fromPosition);
        TimerFragment.activities.add(toPosition > fromPosition ? toPosition - 1 : toPosition, activity);
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onItemDismiss(int position) {
        TimerFragment.totalTime -= TimerFragment.activities.remove(position).timeSpent;
        TimerFragment.totalTimeSpent.setText(Integer.toString(75 - TimerFragment.totalTime) + " minute" + ((75 - TimerFragment.totalTime) == 1 ? " is" : "s are") + " left to be planned.");
        notifyItemRemoved(position);
    }

    public class TimerViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {
        TextView textView, numberView;
        CardView cardView;

        public TimerViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.textView);
            numberView = (TextView) itemView.findViewById(R.id.numberView);
            cardView = (CardView) itemView.findViewById(R.id.activityCardView);
        }

        @Override
        public void onItemSelected() {

        }

        @Override
        public void onItemClear() {

        }
    }

}

