package hwdsb.theofficialwestmountapp;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by danielsong on 2017-01-18.
 */

public class FeedItemAdapter extends RecyclerView.Adapter<FeedItemAdapter.FeedItemViewHolder> {

    ArrayList<FeedItem> feedItems;
    Context context;

    public FeedItemAdapter(Context context, ArrayList<FeedItem> feedItems) {
        this.context = context;
        this.feedItems = feedItems;
    }

    @Override
    public FeedItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.feed_item, parent, false);
        FeedItemViewHolder viewHolder = new FeedItemViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final FeedItemViewHolder holder, int position) {
        final FeedItem item = feedItems.get(position);
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM d, y");
        String summary = "<html><head><style>html{box-sizing:border-box;font-size:62.5%;white-space:pre-wrap;word-wrap:break-word}body{color:#606c76;font-family:'Roboto', 'Helvetica Neue', 'Helvetica', 'Arial', sans-serif;font-size:1.6em;font-weight:300;letter-spacing:.01em;line-height:1.6}hr{border:0;border-top:0.1rem solid #f4f5f6;margin:3.0rem 0}a{width:20px;height:20px;color:#9b4dca;text-decoration:none}a:focus,a:hover{color:#606c76}dl,ol,ul{list-style:none;margin-top:0;padding-left:0}dl dl,dl ol,dl ul,ol dl,ol ol,ol ul,ul dl,ul ol,ul ul{font-size:90%;margin:1.5rem 0 1.5rem 3.0rem}ol{list-style:decimal inside}ul{list-style:circle inside}dl,form,ol,p,table,ul{margin-bottom:2.5rem}table{width:100%}td,th{border-bottom:0.1rem solid #e1e1e1;padding:1.2rem 1.5rem;text-align:left}td:first-child,th:first-child{padding-left:0}td:last-child,th:last-child{padding-right:0}b,strong{font-weight:bold}p{margin-top:0}h1,h2,h3,h4,h5,h6{font-weight:300;letter-spacing:-.1rem;margin-bottom:2.0rem;margin-top:0}h1{font-size:4.0rem;line-height:1.2}h2{font-size:3.6rem;line-height:1.25}h3{font-size:3.0rem;line-height:1.3}h4{font-size:2.4rem;letter-spacing:-.08rem;line-height:1.35}h5{font-size:1.8rem;letter-spacing:-.05rem;line-height:1.5}h6{font-size:1.6rem;letter-spacing:0;line-height:1.4}</style></head><body><h4>" + item.getTitle() + "</h4><p>" + item.getLink().replaceAll("</(?!a)(.*?)>", "").replaceAll("<(?!/)(?!a)(.*?)>", "").replaceAll("<a(.*?)></a>", "").replace("\n", " ") + "</p><p align=\"right\"><em>" + dateFormat.format(item.getPubDate()) + "</em></p></body></html>";
        holder.cardView.setVisibility(View.INVISIBLE);
        holder.webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                YoYo.with(Techniques.FadeIn).playOn(holder.cardView);
                holder.cardView.setVisibility(View.VISIBLE);
            }
        });
        holder.webView.loadDataWithBaseURL(null, summary, "text/html", "utf-8", null);
    }

    @Override
    public int getItemCount() {
        return feedItems.size();
    }

    public class FeedItemViewHolder extends RecyclerView.ViewHolder {
        WebView webView;
        CardView cardView;
        public FeedItemViewHolder(View itemView) {
            super(itemView);
            webView = (WebView) itemView.findViewById(R.id.webView);
            cardView = (CardView) itemView.findViewById(R.id.cardView);
        }
    }

}
