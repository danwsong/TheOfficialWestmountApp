package hwdsb.theofficialwestmountapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> implements ItemTouchHelperAdapter {

    Context context;

    public CourseAdapter(Context context) {
        this.context = context;
    }

    // Instantiate the card views within the recycler view
    @Override
    public CourseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CourseViewHolder(LayoutInflater.from(context).inflate(R.layout.goal_layout, parent, false));
    }

    // Update the card views based on the data found in the goals array and fade the card views in
    @Override
    public void onBindViewHolder(final CourseViewHolder holder, final int position) {
        final PlannerDetailActivity.Goal goal = ((PlannerDetailActivity) context).goals.get(position);
        holder.goalNameText.setText(goal.goalName);
        final Calendar currentCalendar = Calendar.getInstance();
        final Calendar goalCalendar = Calendar.getInstance();
        goalCalendar.setTime(goal.goalDate);
        // Set goal name to red if the goal date has already been passed, and green if marked complete
        if (goal.markedComplete) {
            holder.goalDateText.setTextColor(Color.argb(255, 64, 192, 64));
            holder.goalNameText.setTextColor(Color.argb(255, 64, 192, 64));
        } else if (goalCalendar.get(Calendar.YEAR) < currentCalendar.get(Calendar.YEAR) || (goalCalendar.get(Calendar.YEAR) == currentCalendar.get(Calendar.YEAR) && goalCalendar.get(Calendar.MONTH) < currentCalendar.get(Calendar.MONTH)) || (goalCalendar.get(Calendar.YEAR) == currentCalendar.get(Calendar.YEAR) && goalCalendar.get(Calendar.MONTH) == currentCalendar.get(Calendar.MONTH) && goalCalendar.get(Calendar.DAY_OF_MONTH) < currentCalendar.get(Calendar.DAY_OF_MONTH))) {
            holder.goalNameText.setTextColor(Color.RED);
            holder.goalDateText.setTextColor(Color.RED);
        } else {
            holder.goalDateText.setTextColor(ContextCompat.getColor(context, android.R.color.tertiary_text_light));
            holder.goalNameText.setTextColor(ContextCompat.getColor(context, android.R.color.tertiary_text_light));
        }
        holder.goalDateText.setText(new SimpleDateFormat("MMMM d, y").format(goal.goalDate));
        holder.goalCardView.setVisibility(View.INVISIBLE);
        AlphaAnimation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(1000);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                holder.goalCardView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        holder.goalCardView.startAnimation(animation);
        holder.goalCardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ((PlannerDetailActivity) context).goals.set(position, new PlannerDetailActivity.Goal(goal.goalName, goal.goalDate, !goal.markedComplete));
                SharedPreferences preferences = context.getSharedPreferences(((PlannerDetailActivity) context).courseName, 0);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(Integer.toString(position) + "complete", Boolean.toString(((PlannerDetailActivity) context).goals.get(position).markedComplete));
                editor.commit();
                notifyItemChanged(position);

                return true;
            }
        });
    }

    // Get the number of card views to instantiate, equal to the number of goals
    @Override
    public int getItemCount() {
        return ((PlannerDetailActivity) context).goals.size();
    }

    // Moving items is not allowed as items are sorted by due date
    @Override
    public void onItemMove(int fromPosition, int toPosition) {

    }

    // When the goal is slid to the left or right, remove the goal from the data array, save the updated array to data persistence, and remove the scheduled notification associated with the goal that was removed
    @Override
    public void onItemDismiss(int position) {
        ((PlannerDetailActivity) context).goals.remove(position);
        if (((PlannerDetailActivity) context).goals.size() == 0) {
            PlannerDetailActivity.courseDefaultText.setVisibility(View.VISIBLE);
        } else {
            PlannerDetailActivity.courseDefaultText.setVisibility(View.GONE);
        }
        SharedPreferences preferences = context.getSharedPreferences(((PlannerDetailActivity) context).courseName, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        for (int i = 0; i < ((PlannerDetailActivity) context).goals.size(); i++) {
            editor.putString(Integer.toString(i) + "name", ((PlannerDetailActivity) context).goals.get(i).goalName);
            editor.putString(Integer.toString(i) + "date", Long.toString(((PlannerDetailActivity) context).goals.get(i).goalDate.getTime()));
            editor.putString(Integer.toString(i) + "complete", Boolean.toString(((PlannerDetailActivity) context).goals.get(i).markedComplete));
        }
        editor.commit();
        notifyItemRemoved(position);
    }

    public class CourseViewHolder extends RecyclerView.ViewHolder {
        CardView goalCardView;
        TextView goalNameText, goalDateText;

        public CourseViewHolder(View itemView) {
            super(itemView);
            goalCardView = (CardView) itemView.findViewById(R.id.goalCardView);
            goalNameText = (TextView) itemView.findViewById(R.id.goalNameText);
            goalDateText = (TextView) itemView.findViewById(R.id.goalDateText);
        }

    }

}
