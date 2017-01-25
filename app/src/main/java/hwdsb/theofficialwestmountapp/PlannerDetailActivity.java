package hwdsb.theofficialwestmountapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.MenuItem;
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

    RecyclerView goalListView;
    FloatingActionButton addNewGoalButton;
    static ArrayList<FeedItemDecoration> feedItemDecorations = new ArrayList<>();
    static TextView courseDefaultText;
    static ArrayList<Goal> goals;
    TextView courseTitle;
    static String courseName;

    // Instantiate the main view for the course detail activity, load goals for the course from persistent data storage, and instantiate other views within the main view
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planner_detail);
        courseName = getIntent().getExtras().getString("courseName");
        SharedPreferences preferences = getSharedPreferences(courseName, 0);
        Map<String, String> courseSettings = (Map<String, String>) preferences.getAll();
        goals = new ArrayList<>();
        for (int i = 0; i < courseSettings.size() / 3; i++) {
            goals.add(new Goal(courseSettings.get(Integer.toString(i) + "name"), new Date(Long.parseLong(courseSettings.get(Integer.toString(i) + "date"))), Boolean.parseBoolean(courseSettings.get(Integer.toString(i) + "complete"))));
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
        courseTitle.setText(courseName);
        if (goals.size() == 0) {
            PlannerDetailActivity.courseDefaultText.setVisibility(View.VISIBLE);
        } else {
            PlannerDetailActivity.courseDefaultText.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Display an alert prompting the user for data for the new goal they are creating
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

    // If the goal name is empty, the goal date is before the current data, or the goal name has already been used, prompt the user again, otherwise add the new goal to the data array and save the new goal to persistent data storage and create a notification that is to be issued one day before the goal date
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
            Goal goal = new Goal(goalName, goalDate, false);
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
                editor.putString(Integer.toString(i) + "complete", Boolean.toString(goals.get(i).markedComplete));
            }
            editor.commit();

            if (goals.size() == 0) {
                PlannerDetailActivity.courseDefaultText.setVisibility(View.VISIBLE);
            } else {
                PlannerDetailActivity.courseDefaultText.setVisibility(View.GONE);
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Add Goal to Calendar");
            builder.setMessage("Would you like to add this goal to your calendar?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(goalDate);

                    Intent intent = new Intent(Intent.ACTION_INSERT).setData(CalendarContract.Events.CONTENT_URI);
                    intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, calendar.getTimeInMillis());
                    intent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true);
                    intent.putExtra(CalendarContract.Events.TITLE, courseName);
                    intent.putExtra(CalendarContract.Events.DESCRIPTION, goalName);
                    startActivity(intent);
                }
            });
            builder.setNegativeButton("No", null);
            builder.show();
        }
    }

    // Data structure for the goal containing the name and due date
    static public class Goal {
        String goalName;
        Date goalDate;
        Boolean markedComplete;

        public Goal(String goalName, Date goalDate, Boolean markedComplete) {
            this.goalName = goalName;
            this.goalDate = goalDate;
            this.markedComplete = markedComplete;
        }
    }

}
