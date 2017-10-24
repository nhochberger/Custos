package modules.newsreader;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextPane;

import hochberger.utilities.gui.ImageButton;
import hochberger.utilities.images.loader.ImageLoader;
import hochberger.utilities.timing.ToMilis;
import it.sauronsoftware.feed4j.bean.FeedItem;
import modules.CustosModuleWidget;
import net.miginfocom.swing.MigLayout;
import view.ColorProvider;

public class NewsReaderWidget implements CustosModuleWidget {

	private final ColorProvider colorProvider;
	private String currentHeadline;
	private String currentDescription;
	private JTextPane headlineArea;
	private JTextPane descriptionArea;
	private boolean isBuilt;
	private JPanel panel;
	private final List<FeedItem> news;
	private final Timer timer;
	private TimerTask updateNewsTask;
	private int currentNewsIndex;

	public NewsReaderWidget(final ColorProvider colorProvider) {
		super();
		this.colorProvider = colorProvider;
		this.isBuilt = false;
		this.news = new LinkedList<>();
		this.timer = new Timer();
		this.currentNewsIndex = -1;
	}

	@Override
	public void updateWidget() {
		if (!this.isBuilt) {
			return;
		}
		this.headlineArea.setForeground(this.colorProvider.foregroundColor());
		this.headlineArea.setText(this.currentHeadline);
		this.descriptionArea.setForeground(this.colorProvider.foregroundColor());
		this.descriptionArea.setText(this.currentDescription);
		this.panel.setBorder(BorderFactory.createLineBorder(this.colorProvider.shadowColor()));
	}

	@Override
	public JComponent getComponent() {
		return this.panel;
	}

	@Override
	public void build() {
		if (this.isBuilt) {
			return;
		}
		this.isBuilt = true;

		final Image previousImage = ImageLoader.loadImage("modules/newsreader/previous.png");
		final Image nextImage = ImageLoader.loadImage("modules/newsreader/next.png");

		this.panel = new JPanel(
				new MigLayout("fill, height 200px!, wrap 3", "2px[" + previousImage.getWidth(null) + "px!]5px[grow, center]5px[" + nextImage.getWidth(null) + "px!]2px", "2px[]5px[top]2px"));
		this.panel.addMouseListener(new StopReloadMouseAdapter());
		this.panel.setOpaque(false);

		final EventToParentForwardingMouseAdapter forwarder = new EventToParentForwardingMouseAdapter(this.panel);

		final ImageButton previousButton = new ImageButton(previousImage);
		previousButton.addActionListener(new PreviousHeadlineActionListener());
		previousButton.addMouseListener(forwarder);

		final ImageButton nextButton = new ImageButton(nextImage);
		nextButton.addActionListener(new NextHeadlineActionListener());
		nextButton.addMouseListener(forwarder);

		this.headlineArea = new JTextPane();
		this.headlineArea.setFont(this.headlineArea.getFont().deriveFont(26f));
		this.headlineArea.setOpaque(false);
		this.headlineArea.setEditable(false);
		this.headlineArea.setHighlighter(null);
		this.headlineArea.addMouseListener(forwarder);
		this.headlineArea.setForeground(this.colorProvider.foregroundColor());

		this.descriptionArea = new JTextPane();
		;
		this.descriptionArea.setOpaque(false);
		this.descriptionArea.setFont(this.descriptionArea.getFont().deriveFont(18f));
		this.descriptionArea.addMouseListener(forwarder);
		this.descriptionArea.setHighlighter(null);
		this.descriptionArea.setForeground(this.colorProvider.foregroundColor());

		this.panel.add(previousButton, "top");
		this.panel.add(this.headlineArea, "pushx");
		this.panel.add(nextButton, "top");
		this.panel.add(this.descriptionArea, "span 3, pushy");
		this.panel.setBorder(BorderFactory.createLineBorder(this.colorProvider.shadowColor()));
		this.panel.doLayout();
		startUpdateNewsTimer();
	}

	private void startUpdateNewsTimer() {
		this.updateNewsTask = new TimerTask() {

			@Override
			public void run() {
				if (0 >= NewsReaderWidget.this.news.size()) {
					return;
				}
				setCurrentNewsIndex((getCurrentNewsIndex() + 1) % NewsReaderWidget.this.news.size());
				final FeedItem currentNews = NewsReaderWidget.this.news.get(getCurrentNewsIndex());
				NewsReaderWidget.this.currentHeadline = currentNews.getTitle();
				NewsReaderWidget.this.currentDescription = currentNews.getDescriptionAsText();
			}
		};
		this.timer.schedule(this.updateNewsTask, ToMilis.seconds(3), ToMilis.seconds(15));

	}

	public void setNews(final List<FeedItem> news) {
		this.news.clear();
		this.news.addAll(news);
	}

	public int getCurrentNewsIndex() {
		return this.currentNewsIndex;
	}

	public void setCurrentNewsIndex(final int newIndex) {
		this.currentNewsIndex = newIndex;
	}

	@Override
	public String getLayoutConstraints() {
		return "top, growx, span 2";
	}

	private final class StopReloadMouseAdapter extends MouseAdapter {
		@Override
		public void mouseEntered(final MouseEvent e) {
			NewsReaderWidget.this.updateNewsTask.cancel();
		}

		@Override
		public void mouseExited(final MouseEvent e) {
			startUpdateNewsTimer();
		}
	}

	private final class PreviousHeadlineActionListener implements ActionListener {
		@Override
		public void actionPerformed(final ActionEvent e) {
			if (0 >= NewsReaderWidget.this.news.size()) {
				return;
			}
			// HACK
			setCurrentNewsIndex((0 > (getCurrentNewsIndex() - 1)) ? NewsReaderWidget.this.news.size() - 1 : (getCurrentNewsIndex() - 1) % NewsReaderWidget.this.news.size());
			final FeedItem currentNews = NewsReaderWidget.this.news.get(getCurrentNewsIndex());
			NewsReaderWidget.this.currentHeadline = currentNews.getTitle();
			NewsReaderWidget.this.currentDescription = currentNews.getDescriptionAsText();
			updateWidget();
		}
	}

	private final class NextHeadlineActionListener implements ActionListener {
		@Override
		public void actionPerformed(final ActionEvent e) {
			if (0 >= NewsReaderWidget.this.news.size()) {
				return;
			}
			setCurrentNewsIndex((getCurrentNewsIndex() + 1) % NewsReaderWidget.this.news.size());
			final FeedItem currentNews = NewsReaderWidget.this.news.get(getCurrentNewsIndex());
			NewsReaderWidget.this.currentHeadline = currentNews.getTitle();
			NewsReaderWidget.this.currentDescription = currentNews.getDescriptionAsText();
			updateWidget();
		}
	}

	public class EventToParentForwardingMouseAdapter extends MouseAdapter {

		private final JComponent parent;

		public EventToParentForwardingMouseAdapter(final JComponent parent) {
			super();
			this.parent = parent;
		}

		@Override
		public void mouseEntered(final MouseEvent e) {
			super.mouseEntered(e);
			this.parent.dispatchEvent(e);
		}

		@Override
		public void mouseExited(final MouseEvent e) {
			super.mouseExited(e);
			this.parent.dispatchEvent(e);
		}
	}
}
