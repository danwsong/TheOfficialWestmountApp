package hwdsb.theofficialwestmountapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

public class PlannerFragment extends Fragment {

    static RecyclerView courseListView;
    FloatingActionButton addNewCourseButton;
    static ArrayList<FeedItemDecoration> feedItemDecorations = new ArrayList<>();
    static TextView plannerText;
    Context context;
    static ArrayList<String> courses = new ArrayList<>();

    public PlannerFragment() {
    }

    // Instantiate the main view for the planner view, load in the course names from persistent data storage, and instantiate the other views
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_planner, container, false);

        SharedPreferences preferences = context.getSharedPreferences("COURSES", 0);
        Map<String, String> courseSettings = (Map<String, String>) preferences.getAll();
        for (int i = 0; i < courseSettings.size(); i++) {
            courses.add("");
        }
        for (String index : courseSettings.keySet()) {
            courses.set(Integer.parseInt(index), courseSettings.get(index));
        }
        addNewCourseButton = (FloatingActionButton) rootView.findViewById(R.id.addNewCourse);
        addNewCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayNewCourseAlert("");
            }
        });
        plannerText = (TextView) rootView.findViewById(R.id.plannerText);
        courseListView = (RecyclerView) rootView.findViewById(R.id.courseListView);
        courseListView.setLayoutManager(new LinearLayoutManager(context));
        if (feedItemDecorations.size() > 0) {
            courseListView.removeItemDecoration(feedItemDecorations.get(0));
        }
        FeedItemDecoration itemDecoration = new FeedItemDecoration(32);
        feedItemDecorations.add(itemDecoration);
        courseListView.addItemDecoration(itemDecoration, 0);
        PlannerAdapter adapter = new PlannerAdapter(context);
        courseListView.setAdapter(adapter);
        ItemTouchHelperCallback callback = new ItemTouchHelperCallback(adapter);
        callback.dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(courseListView);
        if (PlannerFragment.courses.size() == 0) {
            PlannerFragment.plannerText.setVisibility(View.VISIBLE);
        } else {
            PlannerFragment.plannerText.setVisibility(View.GONE);
        }

        return rootView;
    }

    // Display an alert prompting the user for information about the new course they are adding
    public void displayNewCourseAlert(String startingText) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("New Course");

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        final LinearLayout linearLayout = (LinearLayout) layoutInflater.inflate(R.layout.planner_alert_layout, null);
        ((EditText) linearLayout.findViewById(R.id.courseNameAlert)).setText(startingText);

        builder.setView(linearLayout);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText activityName = (EditText) linearLayout.findViewById(R.id.courseNameAlert);

                addNewCourse(String.valueOf(activityName.getText()));
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.show();
    }

    // If the course name is empty or the course name has already been used, prompt the user again, otherwise add the course to the array containing the course names and save the new course to persistent data storage
    public void addNewCourse(final String courseName) {
        if (courseName == "") {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Invalid Course Name");
            builder.setMessage("The course name cannot be empty.");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    displayNewCourseAlert(courseName);
                }
            });
            builder.show();
        } else if (courses.contains(courseName)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Invalid Course Name");
            builder.setMessage("This course name has already been used. Please enter another course name.");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    displayNewCourseAlert(courseName);
                }
            });
            builder.show();
        } else {
            courses.add(courseName);

            SharedPreferences preferences = context.getSharedPreferences("COURSES", 0);
            SharedPreferences.Editor editor = preferences.edit();
            for (int i = 0; i < courses.size(); i++) {
                editor.putString(Integer.toString(i), courses.get(i));
            }
            editor.commit();

            courseListView.getAdapter().notifyItemInserted(courses.size() - 1);

            if (PlannerFragment.courses.size() == 0) {
                PlannerFragment.plannerText.setVisibility(View.VISIBLE);
            } else {
                PlannerFragment.plannerText.setVisibility(View.GONE);
            }
        }
    }

}
