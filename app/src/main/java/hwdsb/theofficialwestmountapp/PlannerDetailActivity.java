package hwdsb.theofficialwestmountapp;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Map;

public class PlannerDetailActivity extends AppCompatActivity {

    static RecyclerView goalListView;
    FloatingActionButton addNewGoalButton;
    static ArrayList<FeedItemDecoration> feedItemDecorations = new ArrayList<>();
    static TextView courseDefaultText;
    ArrayList<Goal> goals = new ArrayList<>();
    TextView courseTitle;
    static String courseName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planner_detail);

        SharedPreferences preferences = getSharedPreferences(courseName, 0);
        Map<String, String> courseSettings = (Map<String, String>) preferences.getAll();
        for (int i = 0; i < courseSettings.size() / 2; i++) {
            goals.add(new Goal(courseSettings.get(Integer.toString(i) + "name"), new Date(Long.parseLong(courseSettings.get(Integer.toString(i) + "date")))));
        }

        addNewGoalButton = (FloatingActionButton) findViewById(R.id.addNewGoal);
        addNewGoalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayNewGoalAlert("", Calendar.getInstance().getTime());
            }
        });
        courseDefaultText = (TextView) findViewById(R.id.courseDefaultText);
        goalListView = (RecyclerView) findViewById(R.id.goalListView);
        goalListView.setLayoutManager(new LinearLayoutManager(this));
        if (feedItemDecorations.size() > 0) {
            goalListView.removeItemDecoration(feedItemDecorations.get(0));
        }
        FeedItemDecoration itemDecoration = new FeedItemDecoration(32);
        feedItemDecorations.add(itemDecoration);
        goalListView.addItemDecoration(itemDecoration, 0);
        CourseAdapter adapter = new CourseAdapter(this);
        goalListView.setAdapter(adapter);
        ItemTouchHelper.Callback callback = new ItemTouchHelperCallback(adapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(goalListView);
        courseTitle = (TextView) findViewById(R.id.courseTitle);
        Bundle bundle = getIntent().getExtras();
        courseTitle.setText(bundle.getString("courseName"));
        if (goals.size() == 0) {
            PlannerDetailActivity.courseDefaultText.setVisibility(View.VISIBLE);
        } else {
            PlannerDetailActivity.courseDefaultText.setVisibility(View.GONE);
        }
    }

    public void displayNewGoalAlert(String startingText, final Date startingDate) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("New Goal");

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        final LinearLayout linearLayout = (LinearLayout) layoutInflater.inflate(R.layout.course_alert_layout, null);
        ((EditText) linearLayout.findViewById(R.id.goalNameAlert)).setText(startingText);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startingDate);
        ((DatePicker) linearLayout.findViewById(R.id.datePicker)).updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        builder.setView(linearLayout);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText activityName = (EditText) linearLayout.findViewById(R.id.goalNameAlert);
                DatePicker datePicker = (DatePicker) linearLayout.findViewById(R.id.datePicker);
                Calendar calendar = Calendar.getInstance();
                calendar.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
                try {
                    addNewGoal(String.valueOf(activityName.getText()), calendar.getTime());
                } catch (IntentFilter.MalformedMimeTypeException e) {
                    e.printStackTrace();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.show();
    }

    private void addNewGoal(final String goalName, final Date goalDate) throws IntentFilter.MalformedMimeTypeException {
        Calendar currentCalendar = Calendar.getInstance();
        Calendar goalCalendar = Calendar.getInstance();
        goalCalendar.setTime(goalDate);
        if (goalName == "") {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Invalid Course Name");
            builder.setMessage("The course name cannot be empty.");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    displayNewGoalAlert(goalName, goalDate);
                }
            });
            builder.show();
        } else if (goalCalendar.get(Calendar.YEAR) < currentCalendar.get(Calendar.YEAR) || (goalCalendar.get(Calendar.YEAR) == currentCalendar.get(Calendar.YEAR) && goalCalendar.get(Calendar.MONTH) < currentCalendar.get(Calendar.MONTH)) || (goalCalendar.get(Calendar.YEAR) == currentCalendar.get(Calendar.YEAR) && goalCalendar.get(Calendar.MONTH) == currentCalendar.get(Calendar.MONTH) && goalCalendar.get(Calendar.DAY_OF_MONTH) < currentCalendar.get(Calendar.DAY_OF_MONTH))) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Invalid Goal Deadline");
            builder.setMessage("The date of your goal cannot be before today.");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    displayNewGoalAlert(goalName, goalDate);
                }
            });
            builder.show();
        } else {
            for (int i = 0; i < goals.size(); i++) {
                if (goals.get(i).goalName == goalName) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Invalid Course Name");
                    builder.setMessage("The goal name you have chosen has already been used. Please enter another goal name.");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            displayNewGoalAlert(goalName, goalDate);
                        }
                    });
                    builder.show();
                    return;
                }
            }
            Goal goal = new Goal(goalName, goalDate);
            goals.add(goal);

            Collections.sort(goals, new Comparator<Goal>() {
                @Override
                public int compare(Goal o1, Goal o2) {
                    return o1.goalDate.compareTo(o2.goalDate);
                }
            });
            goalListView.getAdapter().notifyItemInserted(Collections.binarySearch(goals, goal, new Comparator<Goal>() {
                @Override
                public int compare(Goal o1, Goal o2) {
                    return o1.goalDate.compareTo(o2.goalDate);
                }
            }));

            SharedPreferences preferences = getSharedPreferences(courseName, 0);
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            for (int i = 0; i < goals.size(); i++) {
                editor.putString(Integer.toString(i) + "name", goals.get(i).goalName);
                editor.putString(Integer.toString(i) + "date", Long.toString(goals.get(i).goalDate.getTime()));
            }
            editor.commit();

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(goalDate);
            AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(this, AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent.putExtra("courseName", courseName).putExtra("goalName", goalName), PendingIntent.FLAG_UPDATE_CURRENT);
            manager.set(AlarmManager.RTC, calendar.getTimeInMillis() + 32400000, pendingIntent);

            if (goals.size() == 0) {
                PlannerDetailActivity.courseDefaultText.setVisibility(View.VISIBLE);
            } else {
                PlannerDetailActivity.courseDefaultText.setVisibility(View.GONE);
            }
        }
    }

    public class Goal {
        String goalName;
        Date goalDate;

        public Goal(String goalName, Date goalDate) {
            this.goalName = goalName;
            this.goalDate = goalDate;
        }
    }

    static public class AlarmReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context).setSmallIcon(R.drawable.ic_notification).setContentTitle("Course Schedule").setContentText(intent.getExtras().get("goalName") + " for " + intent.getExtras().get("courseName") + " is due tomorrow.").setAutoCancel(true).setVibrate(new long[]{0, 375, 125, 375});
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify((int) (System.currentTimeMillis() % (long) Integer.MAX_VALUE), builder.build());
        }
    }

}
