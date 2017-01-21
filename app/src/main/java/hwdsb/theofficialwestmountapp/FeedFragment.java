package hwdsb.theofficialwestmountapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class FeedFragment extends Fragment {

    RecyclerView feedItemList;
    static SwipeRefreshLayout swipeRefreshLayout;
    Context context;
    ArrayList<FeedItemDecoration> itemDecorations;

    public FeedFragment() {
        itemDecorations = new ArrayList<FeedItemDecoration>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_feed, container, false);
        feedItemList = (RecyclerView) rootView.findViewById(R.id.feedItemList);
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setRefreshing(true);
        new FeedReader(context, feedItemList).execute();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new FeedReader(context, feedItemList).execute();
            }
        });
        return rootView;
    }

    public static void finishedRefreshing() {
        swipeRefreshLayout.setRefreshing(false);
    }
}
