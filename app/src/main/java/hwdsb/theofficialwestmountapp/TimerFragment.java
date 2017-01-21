package hwdsb.theofficialwestmountapp;

import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class TimerFragment extends Fragment {

    static RecyclerView activityListView;
    FloatingActionButton addNewActivityButton;
    static ArrayList<FeedItemDecoration> feedItemDecorations = new ArrayList<>();
    static TextView totalTimeSpent;
    Context context;
    Button startButton;
    static int totalTime = 0;
    static ArrayList<Activity> activities = new ArrayList<>();
    static long timeStarted = 0;
    static boolean running = false;

    public TimerFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_timer, container, false);

        addNewActivityButton = (FloatingActionButton) rootView.findViewById(R.id.addNewActivity);
        addNewActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayNewActivityAlert("");
            }
        });
        totalTimeSpent = (TextView) rootView.findViewById(R.id.totalTimeSpent);
        totalTimeSpent.setText(Integer.toString(75 - totalTime) + " minute" + ((75 - totalTime) == 1 ? " is" : "s are") + " left to be planned.");
        activityListView = (RecyclerView) rootView.findViewById(R.id.activityListView);
        activityListView.setLayoutManager(new LinearLayoutManager(context));
        if (feedItemDecorations.size() > 0) {
            activityListView.removeItemDecoration(feedItemDecorations.remove(0));
        }
        FeedItemDecoration itemDecoration = new FeedItemDecoration(32);
        activityListView.addItemDecoration(itemDecoration, 0);
        activityListView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                return running;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });
        feedItemDecorations.add(itemDecoration);
        TimerAdapter timerAdapter = new TimerAdapter(context);
        activityListView.setAdapter(timerAdapter);
        ItemTouchHelper.Callback callback = new ItemTouchHelperCallback(timerAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(activityListView);
        startButton = (Button) rootView.findViewById(R.id.startTimer);
        startButton.setText("START TIMER");
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (startButton.getText() == "START TIMER") {
                    if (totalTime != 0) {
                        running = true;
                        startButton.setText("STOP TIMER");
                        addNewActivityButton.setEnabled(!running);
                        totalTimeSpent.setVisibility(View.GONE);

                        final Handler handler = new Handler() {
                            @Override
                            public void handleMessage(Message msg) {
                                switch (msg.what) {
                                    case 0:
                                        if (activities.size() >= 1) {
                                            NotificationCompat.Builder builder = new NotificationCompat.Builder(context).setSmallIcon(R.drawable.ic_notification).setContentTitle("Period Planner").setContentText("Start working on " + activities.get(0).activityName + ".").setAutoCancel(true);
                                            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                                            notificationManager.cancel(0);
                                            notificationManager.notify(0, builder.build());

                                            if (activities.size() >= 1) {
                                                sendMessageDelayed(obtainMessage(1), activities.get(0).timeSpent * 60000);
                                                timeStarted = System.currentTimeMillis();
                                            }
                                        }
                                        break;
                                    case 1:
                                        if (activities.size() <= 1) {
                                            totalTime -= activities.remove(0).timeSpent;
                                            totalTimeSpent.setText(Integer.toString(75 - TimerFragment.totalTime) + " minute" + ((75 - TimerFragment.totalTime) == 1 ? " is" : "s are") + " left to be planned.");
                                            activityListView.getAdapter().notifyItemRemoved(0);

                                            running = false;
                                            startButton.setText("START TIMER");
                                            addNewActivityButton.setEnabled(!running);
                                            totalTimeSpent.setVisibility(View.VISIBLE);
                                            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                                            notificationManager.cancel(0);
                                        } else {
                                            NotificationCompat.Builder builder = new NotificationCompat.Builder(context).setSmallIcon(R.drawable.ic_notification).setContentTitle("Period Planner").setContentText("Start working on " + activities.get(1).activityName + ".").setAutoCancel(true);
                                            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                                            notificationManager.cancel(0);
                                            notificationManager.notify(0, builder.build());

                                            totalTime -= activities.remove(0).timeSpent;
                                            totalTimeSpent.setText(Integer.toString(75 - TimerFragment.totalTime) + " minute" + ((75 - TimerFragment.totalTime) == 1 ? " is" : "s are") + " left to be planned.");
                                            activityListView.getAdapter().notifyItemRemoved(0);

                                            ((TextView) activityListView.findViewHolderForAdapterPosition(0).itemView.findViewById(R.id.textView)).setTypeface(null, Typeface.BOLD);
                                            ((TextView) activityListView.findViewHolderForAdapterPosition(0).itemView.findViewById(R.id.numberView)).setTypeface(null, Typeface.BOLD);

                                            if (activities.size() > 1) {
                                                sendMessageDelayed(obtainMessage(1), activities.get(1).timeSpent * 60000);
                                                timeStarted = System.currentTimeMillis();
                                            } else if (activities.size() >= 1) {
                                                sendMessageDelayed(obtainMessage(1), activities.get(0).timeSpent * 60000);
                                                timeStarted = System.currentTimeMillis();
                                            }
                                        }
                                        break;
                                    default:
                                        break;
                                }
                            }
                        };
                        handler.sendMessage(handler.obtainMessage(0));
                        ((TextView) activityListView.findViewHolderForAdapterPosition(0).itemView.findViewById(R.id.textView)).setTypeface(null, Typeface.BOLD);
                        ((TextView) activityListView.findViewHolderForAdapterPosition(0).itemView.findViewById(R.id.numberView)).setTypeface(null, Typeface.BOLD);
                        timeStarted = System.currentTimeMillis();
                        Handler timerHandler = new Handler() {
                            @Override
                            public void handleMessage(Message msg) {
                                switch (msg.what) {
                                    case 0:
                                        if (String.valueOf(startButton.getText()).contains("STOP TIMER") && activities.size() >= 1) {
                                            startButton.setText("STOP TIMER (" + Long.toString((activities.get(0).timeSpent * 60 - (System.currentTimeMillis() - timeStarted) / 1000) / 60) + ":" + String.format("%02d", (activities.get(0).timeSpent * 60 - (System.currentTimeMillis() - timeStarted) / 1000) % 60) + " remaining on current task)");
                                            sendMessageDelayed(obtainMessage(0), 1000);
                                        }
                                        break;
                                    default:
                                        break;
                                }
                            }
                        };
                        timerHandler.sendMessage(timerHandler.obtainMessage(0));
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("Add An Activity");
                        builder.setMessage("Please add an activity in order to start the timer.");
                        builder.setPositiveButton("OK", null);
                        builder.show();
                    }
                } else if (String.valueOf(startButton.getText()).contains("STOP TIMER")) {
                    running = false;
                    startButton.setText("START TIMER");
                    addNewActivityButton.setEnabled(!running);
                    totalTimeSpent.setVisibility(View.VISIBLE);

                    ((TextView) activityListView.findViewHolderForAdapterPosition(0).itemView.findViewById(R.id.textView)).setTypeface(null, Typeface.NORMAL);
                    ((TextView) activityListView.findViewHolderForAdapterPosition(0).itemView.findViewById(R.id.numberView)).setTypeface(null, Typeface.NORMAL);

                    NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.cancelAll();
                }
            }
        });

        return rootView;
    }

    public void addNewActivity(final String activityName, String timeSpent) {
        if (timeSpent == "" || activityName == "") {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Invalid Activity Name or Time Spent");
            builder.setMessage("Your activity name or time spent on the activity cannot be empty");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    displayNewActivityAlert(activityName);
                }
            });
            builder.show();
        } else if (Integer.parseInt(timeSpent) == 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Invalid Time Spent");
            builder.setMessage("Your time spent on this activity cannot be 0 minutes.");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    displayNewActivityAlert(activityName);
                }
            });
            builder.show();
        } else if (Integer.parseInt(timeSpent) + totalTime > 75) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Invalid Time Spent");
            builder.setMessage("Your total time spent should not be more than 75 minutes.");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    displayNewActivityAlert(activityName);
                }
            });
            builder.show();
        } else {
            totalTime += Integer.parseInt(timeSpent);
            totalTimeSpent.setText(Integer.toString(75 - totalTime) + " minute" + ((75 - totalTime) == 1 ? " is" : "s are") + " left to be planned.");

            Activity newActivity = new Activity(activityName, Integer.parseInt(timeSpent));
            activities.add(newActivity);

            activityListView.getAdapter().notifyItemInserted(activities.size() - 1);
        }
    }

    public void displayNewActivityAlert(String startingText) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("New Activity");

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        final LinearLayout linearLayout = (LinearLayout) layoutInflater.inflate(R.layout.alert_layout, null);
        ((EditText) linearLayout.findViewById(R.id.activityName)).setText(startingText);

        builder.setView(linearLayout);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText activityName = (EditText) linearLayout.findViewById(R.id.activityName);
                EditText timeSpent = (EditText) linearLayout.findViewById(R.id.timeSpent);

                addNewActivity(String.valueOf(activityName.getText()), String.valueOf(timeSpent.getText()));
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.show();
    }

    public class Activity {

        public String activityName;
        public int timeSpent;

        public Activity(String activityName, int timeSpent) {
            this.activityName = activityName;
            this.timeSpent = timeSpent;
        }

    }

}
