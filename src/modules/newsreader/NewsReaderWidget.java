package modules.newsreader;

import hochberger.utilities.gui.ImageButton;
import hochberger.utilities.gui.font.FontLoader;
import hochberger.utilities.images.loader.ImageLoader;
import hochberger.utilities.text.i18n.DirectI18N;
import hochberger.utilities.timing.ToMilis;
import it.sauronsoftware.feed4j.bean.FeedItem;

import java.awt.Font;
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
import javax.swing.JTextArea;

import modules.CustosModuleWidget;
import net.miginfocom.swing.MigLayout;
import view.ColorProvider;

public class NewsReaderWidget implements CustosModuleWidget {

    private final ColorProvider colorProvider;
    private String currentHeadline;
    private String currentDescription;
    private JTextArea headlineArea;
    private JTextArea descriptionArea;
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
        this.panel.setBorder(BorderFactory.createLineBorder(this.colorProvider.foregroundColor()));
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
        final Font baseFont = FontLoader.loadFromWithFallback("monofonto.ttf", new JPanel().getFont());
        this.isBuilt = true;
        this.panel = new JPanel();
        final EventToParentForwardingMouseAdapter eventForwarder = new EventToParentForwardingMouseAdapter(this.panel);
        this.panel.setBorder(BorderFactory.createLineBorder(this.colorProvider.foregroundColor()));
        this.panel.setOpaque(false);
        this.panel.setDoubleBuffered(true);
        this.panel.setLayout(new MigLayout("height 150px!", "2[316!]2", "2[]2"));
        final JPanel topPanel = new JPanel(new MigLayout("", "0[]2[260!]2[]0", "0[]0"));
        topPanel.addMouseListener(eventForwarder);
        topPanel.setOpaque(false);
        final ImageButton previousButton = new ImageButton(ImageLoader.loadImage("modules/newsreader/previous.png"));
        previousButton.addMouseListener(eventForwarder);
        previousButton.addActionListener(new ActionListener() {

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
        });
        topPanel.add(previousButton, "top");
        this.headlineArea = new JTextArea(new DirectI18N("Loading...").toString());
        this.headlineArea.setForeground(this.colorProvider.foregroundColor());
        this.headlineArea.setFont(baseFont.deriveFont(14f));
        this.headlineArea.setEditable(false);
        this.headlineArea.setLineWrap(true);
        this.headlineArea.setWrapStyleWord(true);
        this.headlineArea.setOpaque(false);
        topPanel.add(this.headlineArea, "growx");
        final ImageButton nextButton = new ImageButton(ImageLoader.loadImage("modules/newsreader/next.png"));
        nextButton.addMouseListener(eventForwarder);
        nextButton.addActionListener(new ActionListener() {

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
        });
        topPanel.add(nextButton, "top");
        this.panel.add(topPanel, "growx, wrap");
        this.descriptionArea = new JTextArea(new DirectI18N("Loading...").toString());
        this.descriptionArea.setForeground(this.colorProvider.foregroundColor());
        this.descriptionArea.setEditable(false);
        this.descriptionArea.setLineWrap(true);
        this.descriptionArea.setWrapStyleWord(true);
        this.descriptionArea.setOpaque(false);
        this.panel.add(this.descriptionArea, "growx");
        this.headlineArea.addMouseListener(eventForwarder);
        this.descriptionArea.addMouseListener(eventForwarder);
        this.panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(final MouseEvent e) {
                NewsReaderWidget.this.updateNewsTask.cancel();
            }

            @Override
            public void mouseExited(final MouseEvent e) {
                startUpdateNewsTimer();
            }
        });
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
        this.timer.schedule(this.updateNewsTask, ToMilis.seconds(6), ToMilis.seconds(15));

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
