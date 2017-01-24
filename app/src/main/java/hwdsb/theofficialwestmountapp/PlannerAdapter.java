package hwdsb.theofficialwestmountapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;

/**
 * Created by danielsong on 2017-01-23.
 */

public class PlannerAdapter extends RecyclerView.Adapter<PlannerAdapter.PlannerViewHolder> implements ItemTouchHelperAdapter {

    Context context;

    public PlannerAdapter(Context context) {
        this.context = context;
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        String courseName = PlannerFragment.courses.remove(fromPosition);
        PlannerFragment.courses.add(toPosition > fromPosition ? toPosition - 1 : toPosition, courseName);
        SharedPreferences preferences = context.getSharedPreferences("COURSES", 0);
        SharedPreferences.Editor editor = preferences.edit();
        for (int i = 0; i < PlannerFragment.courses.size(); i++) {
            editor.putString(Integer.toString(i), PlannerFragment.courses.get(i));
        }
        editor.commit();
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onItemDismiss(int position) {
        PlannerFragment.courses.remove(position);
        if (PlannerFragment.courses.size() == 0) {
            PlannerFragment.plannerText.setVisibility(View.VISIBLE);
        } else {
            PlannerFragment.plannerText.setVisibility(View.GONE);
        }
        SharedPreferences preferences = context.getSharedPreferences("COURSES", 0);
        SharedPreferences.Editor editor = preferences.edit();
        for (int i = 0; i < PlannerFragment.courses.size(); i++) {
            editor.putString(Integer.toString(i), PlannerFragment.courses.get(i));
        }
        editor.commit();
        notifyItemRemoved(position);
    }

    @Override
    public PlannerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PlannerViewHolder(LayoutInflater.from(context).inflate(R.layout.course_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(final PlannerViewHolder holder, int position) {
        final String courseName = PlannerFragment.courses.get(position);
        holder.courseTextView.setText(courseName);
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
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PlannerDetailActivity.class);
                intent.putExtra("courseName", courseName);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return PlannerFragment.courses.size();
    }

    public class PlannerViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {

        TextView courseTextView;
        CardView cardView;

        public PlannerViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.plannerCardView);
            courseTextView = (TextView) itemView.findViewById(R.id.courseNameTextView);
        }

        @Override
        public void onItemSelected() {

        }

        @Override
        public void onItemClear() {

        }

    }

}
