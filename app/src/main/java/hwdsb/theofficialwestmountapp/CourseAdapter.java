package hwdsb.theofficialwestmountapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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

/**
 * Created by danielsong on 2017-01-23.
 */
public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> implements ItemTouchHelperAdapter {

    Context context;

    public CourseAdapter(Context context) {
        this.context = context;
    }

    @Override
    public CourseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CourseViewHolder(LayoutInflater.from(context).inflate(R.layout.goal_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(final CourseViewHolder holder, int position) {
        final PlannerDetailActivity.Goal goal = ((PlannerDetailActivity) context).goals.get(position);
        holder.goalNameText.setText(goal.goalName);
        Calendar currentCalendar = Calendar.getInstance();
        Calendar goalCalendar = Calendar.getInstance();
        goalCalendar.setTime(goal.goalDate);
        if (goalCalendar.get(Calendar.YEAR) < currentCalendar.get(Calendar.YEAR) || (goalCalendar.get(Calendar.YEAR) == currentCalendar.get(Calendar.YEAR) && goalCalendar.get(Calendar.MONTH) < currentCalendar.get(Calendar.MONTH)) || (goalCalendar.get(Calendar.YEAR) == currentCalendar.get(Calendar.YEAR) && goalCalendar.get(Calendar.MONTH) == currentCalendar.get(Calendar.MONTH) && goalCalendar.get(Calendar.DAY_OF_MONTH) < currentCalendar.get(Calendar.DAY_OF_MONTH))) {
            holder.goalNameText.setTextColor(Color.RED);
            holder.goalDateText.setTextColor(Color.RED);
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
    }

    @Override
    public int getItemCount() {
        return ((PlannerDetailActivity) context).goals.size();
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {

    }

    @Override
    public void onItemDismiss(int position) {
        PlannerDetailActivity.Goal goal = ((PlannerDetailActivity) context).goals.remove(position);
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, PlannerDetailActivity.AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent.putExtra("courseName", ((PlannerDetailActivity) context).courseName).putExtra("goalName", goal.goalName), PendingIntent.FLAG_UPDATE_CURRENT);
        manager.cancel(pendingIntent);
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
            editor.putLong(Integer.toString(i) + "date", ((PlannerDetailActivity) context).goals.get(i).goalDate.getTime());
        }
        editor.commit();
        notifyItemRemoved(position);
    }

    public class CourseViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {
        CardView goalCardView;
        TextView goalNameText, goalDateText;

        public CourseViewHolder(View itemView) {
            super(itemView);
            goalCardView = (CardView) itemView.findViewById(R.id.goalCardView);
            goalNameText = (TextView) itemView.findViewById(R.id.goalNameText);
            goalDateText = (TextView) itemView.findViewById(R.id.goalDateText);
        }

        @Override
        public void onItemSelected() {

        }

        @Override
        public void onItemClear() {

        }
    }

}
