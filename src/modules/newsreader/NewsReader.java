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

import model.configuration.CustosModuleConfiguration;
import model.configuration.CustosModuleStringConfigurationEntry;
import modules.CustosModuleWidget;
import modules.VisibleCustosModule;
import view.ColorProvider;
import controller.HeartbeatEvent;
import controller.SystemMessage;
import controller.SystemMessage.MessageSeverity;
import edt.EDT;

public class NewsReader extends VisibleCustosModule {

	private final class FetchNewsTask extends TimerTask {
		@Override
		public void run() {
			try {
				final Feed feedRepresenation = FeedParser.parse(new URL(NewsReader.this.feedUrl));
				logger().info("Successfully fetched " + feedRepresenation.getItemCount() + " items from " + NewsReader.this.feedUrl);
				session().getEventBus().publish(
						new SystemMessage(MessageSeverity.NORMAL, new DirectI18N("Retrieved ${0} items from ${1}", String.valueOf(feedRepresenation.getItemCount()), NewsReader.this.feedUrl)
								.toString()));
				NewsReader.this.feedItems.clear();
				for (int i = 0; i < feedRepresenation.getItemCount(); i++) {
					NewsReader.this.feedItems.add(feedRepresenation.getItem(i));
				}
				NewsReader.this.widget.setNews(NewsReader.this.feedItems);
			} catch (FeedIOException | FeedXMLParseException | UnsupportedFeedException | MalformedURLException e) {
				logger().error("Unable to retrieve RSS", e);
				session().getEventBus().publish(new SystemMessage(MessageSeverity.SEVERE, new DirectI18N("Unable to retrieve news from ${0}", NewsReader.this.feedUrl).toString()));
			}

		}
	}

	private static final String DEFAULT_RSS = "http://www.tagesschau.de/xml/rss2";
	private static final String NEWSREADER_URL_KEY = "newsreader.url";
	private final NewsReaderWidget widget;
	private final List<FeedItem> feedItems;
	private final Timer timer;
	private final CustosModuleConfiguration configuration;
	private String feedUrl;
	private TimerTask fetchNewsTask;

	public NewsReader(final BasicSession session, final ColorProvider colorProvider) {
		super(session, colorProvider);
		this.widget = new NewsReaderWidget(colorProvider());
		this.feedItems = new LinkedList<>();
		this.timer = new Timer();
		this.configuration = new CustosModuleConfiguration(new DirectI18N("News Reader Configuration"));
		this.configuration.addConfigurationEntry(new CustosModuleStringConfigurationEntry(new DirectI18N("RSS-Feed:"), new DirectI18N("The address of the feed from which news are to be loaded."),
				NEWSREADER_URL_KEY, DEFAULT_RSS));
	}

	@Override
	public CustosModuleWidget getWidget() {
		return this.widget;
	}

	@Override
	public void start() {
		logger().info("Starting NewsReader");
		EDT.perform(new Runnable() {

			@Override
			public void run() {
				NewsReader.this.widget.build();
			}
		});
		scheduleTask();
	}

	private void scheduleTask() {
		if (null != this.fetchNewsTask) {
			this.fetchNewsTask.cancel();
		}
		this.fetchNewsTask = new FetchNewsTask();
		this.timer.schedule(this.fetchNewsTask, ToMilis.seconds(1.5), ToMilis.minutes(10));
	}

	@Override
	public void stop() {
		this.timer.cancel();
		logger().info("NewsReader stopped");
	}

	@Override
	public void applyConfiguration() {
		this.feedUrl = String.valueOf(this.configuration.getEntryFor(NEWSREADER_URL_KEY).getValue());

		scheduleTask();
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
