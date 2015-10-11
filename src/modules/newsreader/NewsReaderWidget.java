package modules.newsreader;

import hochberger.utilities.gui.font.FontLoader;
import hochberger.utilities.text.i18n.DirectI18N;
import hochberger.utilities.timing.ToMilis;
import it.sauronsoftware.feed4j.bean.FeedItem;

import java.awt.Font;
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

    public NewsReaderWidget(final ColorProvider colorProvider) {
        super();
        this.colorProvider = colorProvider;
        this.isBuilt = false;
        this.news = new LinkedList<>();
        this.timer = new Timer();
    }

    @Override
    public void updateWidget() {
        if (!this.isBuilt) {
            return;
        }
        this.headlineArea.setText(this.currentHeadline);
        this.descriptionArea.setText(this.currentDescription);
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
        this.panel.setBorder(BorderFactory.createLineBorder(this.colorProvider.foregroundColor()));
        this.panel.setOpaque(false);
        this.panel.setLayout(new MigLayout("height 150px!", "2[316!]2", "2[]2"));
        this.headlineArea = new JTextArea();
        this.headlineArea.setForeground(this.colorProvider.foregroundColor());
        this.headlineArea.setText(new DirectI18N("Loading...").toString());
        this.headlineArea.setEditable(false);
        this.headlineArea.setLineWrap(true);
        this.headlineArea.setWrapStyleWord(true);
        this.headlineArea.setFont(baseFont.deriveFont(14f));
        this.headlineArea.setOpaque(false);
        this.panel.add(this.headlineArea, "growx, wrap");
        this.descriptionArea = new JTextArea();
        this.descriptionArea.setForeground(this.colorProvider.foregroundColor());
        this.descriptionArea.setText(new DirectI18N("Loading...").toString());
        this.descriptionArea.setEditable(false);
        this.descriptionArea.setLineWrap(true);
        this.descriptionArea.setWrapStyleWord(true);
        this.descriptionArea.setOpaque(false);
        this.panel.add(this.descriptionArea, "growx");
        this.headlineArea.addMouseListener(new EventToParentForwardingMouseAdapter(this.panel));
        this.descriptionArea.addMouseListener(new EventToParentForwardingMouseAdapter(this.panel));
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
            private int currentNewsIndex = 0;

            @Override
            public void run() {
                if (0 >= NewsReaderWidget.this.news.size()) {
                    return;
                }
                final FeedItem currentNews = NewsReaderWidget.this.news.get(this.currentNewsIndex % NewsReaderWidget.this.news.size());
                NewsReaderWidget.this.currentHeadline = currentNews.getTitle();
                NewsReaderWidget.this.currentDescription = currentNews.getDescriptionAsText();
                this.currentNewsIndex++;
            }
        };
        this.timer.schedule(this.updateNewsTask, ToMilis.seconds(1), ToMilis.seconds(15));

    }

    public void setNews(final List<FeedItem> news) {
        this.news.clear();
        this.news.addAll(news);
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
