package modules.newsreader;

import hochberger.utilities.application.session.BasicSession;
import hochberger.utilities.timing.ToMilis;
import it.sauronsoftware.feed4j.FeedIOException;
import it.sauronsoftware.feed4j.FeedParser;
import it.sauronsoftware.feed4j.FeedXMLParseException;
import it.sauronsoftware.feed4j.UnsupportedFeedException;
import it.sauronsoftware.feed4j.bean.Feed;
import it.sauronsoftware.feed4j.bean.FeedItem;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import modules.CustosModuleWidget;
import modules.VisibleCustosModule;
import view.ColorProvider;
import controller.HeartbeatEvent;
import edt.EDT;

public class NewsReader extends VisibleCustosModule {

    private static final String FEED_URL = "http://www.tagesschau.de/xml/rss2";
    private final NewsReaderWidget widget;
    private int currentNewsIndex = 0;
    private final List<FeedItem> feedItems;
    private final Timer timer;

    public NewsReader(final BasicSession session, final ColorProvider colorProvider) {
        super(session, colorProvider);
        this.widget = new NewsReaderWidget(colorProvider());
        this.feedItems = new LinkedList<>();
        this.timer = new Timer();
    }

    @Override
    public CustosModuleWidget getWidget() {
        return this.widget;
    }

    @Override
    public void start() {
        EDT.perform(new Runnable() {

            @Override
            public void run() {
                NewsReader.this.widget.build();
            }

        });
        final TimerTask fetchNewsTask = new TimerTask() {

            @Override
            public void run() {
                try {
                    final Feed feedRepresenation = FeedParser.parse(new URL(FEED_URL));
                    logger().info("Successfully fetched " + feedRepresenation.getItemCount() + " items from " + FEED_URL);
                    NewsReader.this.feedItems.clear();
                    for (int i = 0; i < feedRepresenation.getItemCount(); i++) {
                        NewsReader.this.feedItems.add(feedRepresenation.getItem(i));
                    }
                } catch (FeedIOException | FeedXMLParseException | UnsupportedFeedException | MalformedURLException e) {
                    logger().error("Unable to retrieve RSS", e);
                }

            }
        };
        this.timer.schedule(fetchNewsTask, ToMilis.seconds(5), ToMilis.minutes(5));

        final TimerTask updateNewsTask = new TimerTask() {

            @Override
            public void run() {
                if (NewsReader.this.feedItems.isEmpty()) {
                    logger().info("No news to display at this moment.");
                    return;
                }
                final FeedItem nextNews = NewsReader.this.feedItems.get(NewsReader.this.currentNewsIndex % NewsReader.this.feedItems.size());
                NewsReader.this.widget.setCurrentNews(nextNews.getTitle(), nextNews.getDescriptionAsText());
                NewsReader.this.currentNewsIndex++;
            }
        };
        this.timer.schedule(updateNewsTask, ToMilis.seconds(8), ToMilis.seconds(15));
    }

    @Override
    public void stop() {
        this.timer.cancel();
    }

    @Override
    public void receive(final HeartbeatEvent event) {
        // TODO Auto-generated method stub
    }
}
