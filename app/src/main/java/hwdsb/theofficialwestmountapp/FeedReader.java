package hwdsb.theofficialwestmountapp;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class FeedReader extends AsyncTask<Void, Void, Void> {

    Context context;
    ArrayList<FeedItem> feedItems = new ArrayList<>();
    static ArrayList<FeedItemDecoration> feedItemDecorations = new ArrayList<>();
    RecyclerView feedItemList;

    public FeedReader(Context context, RecyclerView feedItemList) {
        this.context = context;
        this.feedItemList = feedItemList;
    }

    // No task to execute before loading data from URLs
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    // Once finished loading data, hide refreshing progress view, sort feed data array by publication date, and instantiate the recycler view acting as the user interface for the feed data
    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        FeedFragment.finishedRefreshing();
        Collections.sort(feedItems, new Comparator<FeedItem>() {
            @Override
            public int compare(FeedItem o1, FeedItem o2) {
                return o2.getPubDate().compareTo(o1.getPubDate());
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        FeedItemAdapter adapter = new FeedItemAdapter(context, feedItems, linearLayoutManager);
        if (feedItemDecorations.size() != 0) {
            feedItemList.removeItemDecoration(feedItemDecorations.get(0));
            feedItemDecorations.remove(0);
        }
        feedItemList.setLayoutManager(linearLayoutManager);
        FeedItemDecoration itemDecoration = new FeedItemDecoration(32);
        feedItemList.addItemDecoration(itemDecoration, 0);
        feedItemDecorations.add(itemDecoration);
        feedItemList.setAdapter(adapter);
    }

    // Loading in the feed data from two URLs
    @Override
    protected Void doInBackground(Void... params) {
        try {
            processRssFeed(retrieveDataFromRssFeed(new URL("https://595.commons.hwdsb.on.ca/presentation/announcements/feed/")));
            processRssFeed(retrieveDataFromRssFeed(new URL("https://www.hwdsb.on.ca/westmount/2016/feed/")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Add items to the feed data array from the RSS format feed URLs
    private void processRssFeed(Document data) {
        if (data != null) {
            Element root = data.getDocumentElement();
            NodeList rssFeed = root.getChildNodes();
            for (int h = 0; h < rssFeed.getLength(); h++) {
                if (rssFeed.item(h).getNodeName().equalsIgnoreCase("channel")) {
                    NodeList feedData = rssFeed.item(h).getChildNodes();
                    for (int i = 0; i < feedData.getLength(); i++) {
                        Node currentFeedData = feedData.item(i);
                        if (currentFeedData.getNodeName().equalsIgnoreCase("item")) {
                            FeedItem feedItem = new FeedItem();
                            NodeList feedItemMetadata = currentFeedData.getChildNodes();
                            for (int j = 0; j < feedItemMetadata.getLength(); j++) {
                                Node currentFeedItemMetadata = feedItemMetadata.item(j);
                                if (currentFeedItemMetadata.getNodeName().equalsIgnoreCase("title")) {
                                    feedItem.setTitle(currentFeedItemMetadata.getTextContent());
                                }
                                if (currentFeedItemMetadata.getNodeName().equalsIgnoreCase("pubDate")) {
                                    String pubDate = currentFeedItemMetadata.getTextContent();
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss ZZ");
                                    try {
                                        Date date = dateFormat.parse(pubDate);
                                        feedItem.setPubDate(date);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                }
                                if (currentFeedItemMetadata.getNodeName().equalsIgnoreCase("content:encoded")) {
                                    feedItem.setLink(currentFeedItemMetadata.getTextContent());
                                }
                            }
                            feedItems.add(feedItem);
                        }
                    }
                }
            }
        }
    }

    // Convert the URL into a Document readable by the Android API
    public Document retrieveDataFromRssFeed(URL url) {
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            InputStream inputStream = connection.getInputStream();
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            return builder.parse(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
