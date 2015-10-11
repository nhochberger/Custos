package modules.newsreader;

import hochberger.utilities.gui.font.FontLoader;
import hochberger.utilities.text.i18n.DirectI18N;

import java.awt.Font;

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

    public NewsReaderWidget(final ColorProvider colorProvider) {
        super();
        this.colorProvider = colorProvider;
        this.isBuilt = false;
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

    }

    public void setCurrentNews(final String headline, final String description) {
        this.currentHeadline = headline;
        this.currentDescription = description;
    }
}
