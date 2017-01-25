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

public class PlannerAdapter extends RecyclerView.Adapter<PlannerAdapter.PlannerViewHolder> implements ItemTouchHelperAdapter {

    Context context;

    public PlannerAdapter(Context context) {
        this.context = context;
    }

    // When course is held and dragged, update view and the array containing the names of the courses, and update persistent data storage
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

    // When item is slide to the left or right, update view, text view with no items message, the array containing the names of the courses, and update persistent data storage
    @Override
    public void onItemDismiss(int position) {
        context.getSharedPreferences(PlannerFragment.courses.remove(position), 0).edit().clear().commit();
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

    // Instantiate the individual card views within the recycler view
    @Override
    public PlannerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PlannerViewHolder(LayoutInflater.from(context).inflate(R.layout.course_layout, parent, false));
    }

    // Update individual card view with data from the courses array and fade the card view in
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

    // Get number of items in the recycler view
    @Override
    public int getItemCount() {
        return PlannerFragment.courses.size();
    }

    public class PlannerViewHolder extends RecyclerView.ViewHolder {

        TextView courseTextView;
        CardView cardView;

        public PlannerViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.plannerCardView);
            courseTextView = (TextView) itemView.findViewById(R.id.courseNameTextView);
        }
    }

}
