package hwdsb.theofficialwestmountapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class FeedItemAdapter extends RecyclerView.Adapter<FeedItemAdapter.FeedItemViewHolder> {

    ArrayList<FeedItem> feedItems;
    Context context;
    LinearLayoutManager feedItemList;

    public FeedItemAdapter(Context context, ArrayList<FeedItem> feedItems, LinearLayoutManager feedItemList) {
        this.context = context;
        this.feedItems = feedItems;
        this.feedItemList = feedItemList;
    }

    // Instantiate the card views for the feed recycler view
    @Override
    public FeedItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FeedItemViewHolder(LayoutInflater.from(context).inflate(R.layout.feed_item, parent, false));
    }

    // Update the card views with data from the feed, formatted using HTML and CSS using a WebView, and fade the card views in
    @Override
    public void onBindViewHolder(final FeedItemViewHolder holder, final int position) {
        final FeedItem item = feedItems.get(position);
        holder.titleTextView.setText(item.getTitle());
        final SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM d, y");
        holder.dateTextView.setText(dateFormat.format(item.getPubDate()));
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
                context.startActivity(new Intent(context, FeedDetailActivity.class).putExtra("title", item.getTitle()).putExtra("link", item.getLink()).putExtra("pubDate", dateFormat.format(item.getPubDate())));
            }
        });
    }

    // Get the number of card views to instantiate, equal to the size of the feed data array
    @Override
    public int getItemCount() {
        return feedItems.size();
    }

    public class FeedItemViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, dateTextView;
        CardView cardView;

        public FeedItemViewHolder(View itemView) {
            super(itemView);
            titleTextView = (TextView) itemView.findViewById(R.id.titleTextView);
            dateTextView = (TextView) itemView.findViewById(R.id.dateTextView);
            cardView = (CardView) itemView.findViewById(R.id.cardView);
        }
    }

}
