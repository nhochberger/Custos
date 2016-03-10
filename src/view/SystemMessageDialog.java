package view;

import hochberger.utilities.eventbus.EventReceiver;
import hochberger.utilities.gui.ImageButton;
import hochberger.utilities.gui.PanelWrapper;
import hochberger.utilities.images.loader.ImageLoader;
import hochberger.utilities.text.i18n.DirectI18N;

import java.awt.Dialog.ModalExclusionType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import net.miginfocom.swing.MigLayout;
import controller.SystemMessage;
import controller.SystemMessageMemory;
import edt.EDT;

public class SystemMessageDialog implements EventReceiver<SystemMessage> {

    private boolean isBuilt;
    private JDialog dialog;
    private final ColorProvider colorProvider;
    private final SystemMessageMemory messageMemory;
    private JPanel messagesPanel;
    private JPanel contentPane;

    public SystemMessageDialog(final ColorProvider colorProvider, final SystemMessageMemory messageMemory) {
        super();
        this.colorProvider = colorProvider;
        this.messageMemory = messageMemory;
        this.isBuilt = false;
    }

    public void build() {
        if (this.isBuilt) {
            return;
        }
        this.isBuilt = true;
        this.dialog = new JDialog();
        this.dialog.setUndecorated(true);
        this.dialog.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
        this.dialog.setTitle("System Message History");
        this.dialog.setModal(true);
        this.dialog.setAlwaysOnTop(true);
        this.dialog.setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
        this.dialog.setSize(600, 300);
        this.dialog.setLocationRelativeTo(null);
        this.dialog.setResizable(false);
        this.contentPane = new JPanel(new MigLayout("", "2[596px!]2", "2[32px!]2[262px!]2"));
        this.contentPane.setBackground(this.colorProvider.backgroundColor());
        this.dialog.setContentPane(this.contentPane);
        this.messagesPanel = new JPanel(new MigLayout());
        this.messagesPanel.setOpaque(true);
        final JScrollPane scrollPane = new JScrollPane(this.messagesPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setOpaque(false);
        scrollPane.setAutoscrolls(true);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        final ImageButton closeApplicationButton = new ImageButton(ImageLoader.loadImage("close.png"));
        closeApplicationButton.setToolTipText(new DirectI18N("Close").toString());
        closeApplicationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                hide();
            }
        });
        final JPanel buttonPanel = PanelWrapper.wrap(closeApplicationButton);
        buttonPanel.setOpaque(false);
        this.contentPane.add(buttonPanel, "right, wrap");
        this.contentPane.add(scrollPane, "width 596px, height 262px");

    }

    public void show() {
        this.contentPane.setBackground(this.colorProvider.backgroundColor());
        this.messagesPanel.setBackground(this.colorProvider.backgroundColor());
        this.contentPane.setBorder(BorderFactory.createLineBorder(this.colorProvider.shadowColor(), 1));
        this.dialog.setVisible(true);
    }

    public void hide() {
        this.dialog.setVisible(false);
    }

    @Override
    public void receive(final SystemMessage event) {
        EDT.performBlocking(new Runnable() {

            @Override
            public void run() {
                SystemMessageDialog.this.messagesPanel.removeAll();
                for (final SystemMessage message : SystemMessageDialog.this.messageMemory.getMessages()) {
                    final JLabel label = new JLabel();
                    label.setForeground(message.getSeverity().getColorFrom(SystemMessageDialog.this.colorProvider));
                    label.setText(message.toString());
                    SystemMessageDialog.this.messagesPanel.add(label, "wrap");
                }
                SystemMessageDialog.this.messagesPanel.doLayout();
                SystemMessageDialog.this.dialog.getContentPane().doLayout();
            }
        });
    }
}
