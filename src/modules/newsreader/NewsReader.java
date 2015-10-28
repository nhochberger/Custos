package modules.newsreader;

import hochberger.utilities.application.session.BasicSession;
import hochberger.utilities.text.i18n.DirectI18N;
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

import modules.CustosModuleConfiguration;
import modules.CustosModuleConfigurationEntry;
import modules.CustosModuleWidget;
import modules.VisibleCustosModule;
import view.ColorProvider;
import controller.HeartbeatEvent;
import edt.EDT;

public class NewsReader extends VisibleCustosModule {

    private static final String NEWSREADER_URL_KEY = "newsreader.url";
    private final NewsReaderWidget widget;
    private final List<FeedItem> feedItems;
    private final Timer timer;
    private final CustosModuleConfiguration configuration;

    public NewsReader(final BasicSession session, final ColorProvider colorProvider) {
        super(session, colorProvider);
        this.widget = new NewsReaderWidget(colorProvider());
        this.feedItems = new LinkedList<>();
        this.timer = new Timer();
        this.configuration = new CustosModuleConfiguration(new DirectI18N("News Reader Configuration"));
        this.configuration.addConfigurationEntry(new CustosModuleConfigurationEntry(new DirectI18N("RSS-Feed:"), new DirectI18N("The address of the feed from which news are to be loaded."),
                NEWSREADER_URL_KEY, "http://www.tagesschau.de/xml/rss2"));
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
                    final String feedUrl = NewsReader.this.configuration.getConfigurationEntries().get(NEWSREADER_URL_KEY).getValue();
                    final Feed feedRepresenation = FeedParser.parse(new URL(feedUrl));
                    logger().info("Successfully fetched " + feedRepresenation.getItemCount() + " items from " + feedUrl);
                    NewsReader.this.feedItems.clear();
                    for (int i = 0; i < feedRepresenation.getItemCount(); i++) {
                        NewsReader.this.feedItems.add(feedRepresenation.getItem(i));
                    }
                    NewsReader.this.widget.setNews(NewsReader.this.feedItems);
                } catch (FeedIOException | FeedXMLParseException | UnsupportedFeedException | MalformedURLException e) {
                    logger().error("Unable to retrieve RSS", e);
                }

            }
        };
        this.timer.scheduleAtFixedRate(fetchNewsTask, ToMilis.seconds(5), ToMilis.minutes(10));
    }

    @Override
    public void stop() {
        this.timer.cancel();
    }

    @Override
    public void receive(final HeartbeatEvent event) {
        // TODO Auto-generated method stub
    }

    @Override
    public CustosModuleConfiguration getConfiguration() {
        return this.configuration;
    }
}